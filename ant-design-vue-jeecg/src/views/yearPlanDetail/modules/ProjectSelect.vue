<template>
  <j-modal
    :title="title"
    :width="1000"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目名称">
              <j-input placeholder="请输入项目名称"  v-model="queryParam.prjName"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目分类">
              <j-dict-select-tag  placeholder="请输入项目分类" v-model="queryParam.prjType" dictCode="project_class"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>

          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div  >


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
        :rowSelection="{selectedRowKeys: selectedRowKeys,  onChange: onSelectChange,type:'radio'}"
        :customRow="clickThenSelect"
        @change="handleTableChange">


      </a-table>
    </div>

  </a-card>
  </j-modal>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  import { getAction } from '@/api/manage'
  import {initDictOptions,filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery'
  import JInput from '@comp/jeecg/JInput'
  import Storage from 'vue-ls'
  import Vue from 'vue'
  export default {
    name: "ProjectSelect",
    mixins:[JeecgListMixin],
    components: {
      JSuperQuery,
      JInput

    },
    data () {
      return {

        title:'',
        visible:false,
        description: '项目信息管理页面',

        resName:{},
        // 表头
        columns: [
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'prjName'
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
          pageSize: 10,
          pageSizeOptions: ['10', '20', '50'],
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
        this.resName=selectionRows[0]
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
      },
      handleOk(){

        this.$emit('selectProject',this.resName)
        this.close()
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.visible = false
      },
    },

  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>