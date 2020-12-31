<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目名称">
              <j-input placeholder="请输入项目名称" v-model="queryParam.prjName"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目分类">
              <j-dict-select-tag placeholder="请输入项目分类" v-model="queryParam.prjType" dictCode="project_class"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>

          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('单位信息')">导出</a-button>

      <a-dropdown v-has="'project:delete'"  v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1"  @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :customRow="clickThenSelect"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record" style="">
              <router-link @click.native="jump(record)" :to="{ name: 'project-BaseInfo' ,query:{record:record}}">
                <a-tag color="pink">详情</a-tag>
              </router-link>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a-tag color="red" v-has="'project:delete'" >删除</a-tag>
                </a-popconfirm>
        </span>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>
      </a-table>
    </div>


    <project-modal ref="modalForm" @ok="modalFormOk"></project-modal>
    <project-info-modal ref="modalFormd" @ok="modalFormOk"></project-info-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import {initDictOptions,filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'
  import ProjectModal from '@views/project/modules/ProjectModal.vue'
  import ProjectInfoModal from './modules/ProjectInfoModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery'
  import JInput from '@comp/jeecg/JInput'
  import Storage from 'vue-ls'
  import Vue from 'vue'

  export default {
    name: "ProjectList",
    mixins:[JeecgListMixin],
    components: {
      ProjectModal,
      ProjectInfoModal,
      JSuperQuery,
      JInput

    },
    data () {
      return {
        leaders:{
          other:'' ,
          preOwner:'' ,
          build:'',
          office: '',
          bridge: '',
          plan: '',
          finance: '',
          midOwner: '',
          quality: ''
        },
        description: '项目信息管理页面',

        // 表头
        columns: [
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'prjName'
          },
          {
            title:'项目编号',
            align:"center",
            dataIndex: 'prjNumber'
          },
          {
            title:'项目分类',
            align:"center",
            dataIndex: 'prjType_dictText'
          },
          {
            title:'项目阶段',
            align:"center",
            dataIndex: 'prjStage_dictText',
          },
          {
            title:'匡算(万元)',
            align:"center",
            dataIndex: 'prjRoughEstimate',
            scopedSlots:{customRender:'money'},
          },
          {
            title:'估算(万元)',
            align:"center",
            scopedSlots:{customRender:'money'},

            dataIndex: 'prjEstimate'
          },
          {
            title:'概算(万元)',
            align:"center",
            scopedSlots:{customRender:'money'},

            dataIndex: 'prjBudgetEstimate'
          },
          {
            title:'决算(万元)',
            align:"center",
            scopedSlots:{customRender:'money'},

            dataIndex: 'prjFinalEstimate'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/project/project/list",
          delete: "/project/project/delete",
          deleteBatch: "/project/project/deleteBatch",
          exportXlsUrl: "/project/project/exportXls",
          importExcelUrl: "project/project/importExcel",
          leader:"/projectleader/projectLeader/lists"
        },
        dictOptions:{
         prjStage:[],
        },

        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 20,
          pageSizeOptions: ['20', '30', '50'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        selectedMainId:''

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {

      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
     jump(val){
        Vue.ls.set('projectInfo',val,50*60*60*1000)
     },

      initDictConfig(){
        initDictOptions('project_process').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'prjStage', res.result)
          }
        })
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
      },
      clickThenSelect(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.toString().split(","), [record]);
            }
          }
        }
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.selectedMainId=''
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedMainId=selectedRowKeys[0]
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.onClearSelected()
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>