<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <j-search-select-tag  v-model="queryParam.psPrjId" placeholder="请输入工程名称" dict="pjplat.bs_project,prj_name,id"></j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工单位">
              <j-search-select-tag  v-model="queryParam.psConstructionUnit" placeholder="请输入施工单位" dict="pjplat.bs_company,com_name,id"></j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="监理单位">
              <j-search-select-tag  v-model="queryParam.psSupervisingUnit" placeholder="请输入监理单位" dict="pjplat.bs_company,com_name,id"></j-search-select-tag>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="12" :lg="12" :md="12" :sm="24">
              <a-form-item label="问题发起时间">
                <j-date
                  placeholder="请选择开始日期"
                  class="query-group-cust"
                  v-model="queryParam.psTime_begin"
                ></j-date>
                <span class="query-group-split-cust"></span>
                <j-date
                  placeholder="请选择结束日期"
                  class="query-group-cust"
                  v-model="queryParam.psTime_end"
                ></j-date>
              </a-form-item>
            </a-col>
          </template>
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

      <a-button type="primary" icon="download" @click="handleExportXls('检查整改单')">导出</a-button>
      <!--<img :src="getbdImgView('test/daiban.png')" :preview="getbdImgView('test/daiban.png')" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>-->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
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
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handlepsDetail(record)">详情</a>

          <a-divider type="vertical" />

          <a @click="handleptDetail(record)">打印</a>
          <!-- <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <bsProblemSheet-modal ref="modalForm"></bsProblemSheet-modal>
    <bsProblemSheetDY-modal ref="dymodalForm"></bsProblemSheetDY-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BsProblemSheetModal from './modules/BsProblemSheetModal'
  import BsProblemSheetDYModal from './modules/BsProblemSheetDYModal'
  //import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  //import { loadCategoryData } from '@/api/api'
  //import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "BsProblemSheetList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BsProblemSheetModal,
      BsProblemSheetDYModal,
      JSearchSelectTag,
      JDate
    },
    data () {
      return {
        description: '检查整改单管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'psPrjId_dictText'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'psConstructionUnit_dictText'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'psSupervisingUnit_dictText'
          },
          {
            title:'问题部位',
            align:"center",
            dataIndex: 'psLocation'
          },
          {
            title:'问题分类',
            align:"center",
            dataIndex: 'psType_dictText'
            //dataIndex: 'psType',
            //customRender: (text) => (text ? filterMultiDictText(this.dictOptions['psType'], text) : '')
          },
          {
            title:'隐患等级',
            align:"center",
            dataIndex: 'psLevel_dictText'
          },
          {
            title:'流程状态',
            align:"center",
            dataIndex: 'actStatus'
          },
          //{
          //  title:'隐患描述',
          //  align:"center",
          //  dataIndex: 'psDescription'
          //},
          //{
          //  title:'整改意见',
          //  align:"center",
          //  dataIndex: 'psAdvice'
          //},
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/problemsheet/bsProblemSheet/list",
          delete: "/problemsheet/bsProblemSheet/delete",
          deleteBatch: "/problemsheet/bsProblemSheet/deleteBatch",
          exportXlsUrl: "/problemsheet/bsProblemSheet/exportXls",
          importExcelUrl: "problemsheet/bsProblemSheet/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
        //loadCategoryData({code:'B03'}).then((res) => {
          //if (res.success) {
            //this.$set(this.dictOptions, 'psType', res.result)
          //}
        //})
      },
      handlepsDetail: function (record) {
        //console.log(this.$refs);
        //console.log(this.$refs.modalForm);
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.handlepic(record);
        this.$refs.modalForm.see = true;
        this.$refs.modalForm.selectedKeys=['1'];
        this.$refs.modalForm.title = "详情";
        this.$refs.modalForm.disableSubmit = true;
      },
      handleptDetail: function (record) {
        //console.log(this.$refs);
        //console.log(this.$refs.dymodalForm);
        this.$refs.dymodalForm.edit(record);
        this.$refs.dymodalForm.handlepic(record);
        //this.$refs.modalForm.see = true;
        this.$refs.dymodalForm.title = "打印";
        this.$refs.dymodalForm.disableSubmit = true;
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>