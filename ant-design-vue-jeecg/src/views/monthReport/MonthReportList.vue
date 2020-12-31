<template>
  <a-card :title="this.info.prjName" :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div v-has="'monthReport:opt'" class="table-operator">
      <a-button  @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('进度月报')">导出</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
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
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{x:1500}"
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

        <span v-has="'monthReport:opt'" slot="action" slot-scope="text, record">
          <a-tag color="pink" @click="handleEdit(record)">编辑</a-tag>

          <a-dropdown>
            <a-tag color="red" class="ant-dropdown-link">更多 <a-icon type="down" /></a-tag>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>


        </span>
        <template slot="mrDescription" slot-scope="record">

          <a-popover title="总体进展情况描述" trigger="hover" placement="leftTop">
            <template slot="content">
              <div style="width: 200px">
                {{record}}</div>
            </template>
            {{record}}
          </a-popover>
        </template>

        <template slot="mrNeedCoordinate" slot-scope="record">


          <a-popover title="需协调处理的问题" trigger="hover" placement="leftTop">

            <template slot="content">
              <div style="width: 200px">
                {{record}}</div>

            </template>
            {{record}}

          </a-popover>

        </template>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>


      </a-table>
    </div>

    <monthReport-modal ref="modalForm" @ok="modalFormOk" :mainId="this.info.id"></monthReport-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MonthReportModal from './modules/MonthReportModal'
  import Vue from 'vue'
  import { filterDictText, filterDictTextByCache, initDictOptions } from '../../components/dict/JDictSelectUtil'
  import {colAuthFilter} from "@/utils/authFilter"

  export default {
    name: "MonthReportList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      MonthReportModal
    },
    created () {
      //初始化字典配置 在自己页面定义
      this.initDictConfig();
      this.info=Vue.ls.get('projectInfo')
      this.disableMixinCreated=true
      this.queryParam['prjId'] = this.info.id

      this.columns=colAuthFilter(this.columns,"monthreport:")
      this.loadData(1);

    },
    data () {
      return {
        info:{},
        description: '进度月报管理页面',
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
            title:'年份',
            align:"center",
            dataIndex: 'year',
            width:130,
            sorter:(a,b)=>a.year-b.year
          },
          {
            title:'月份',
            align:"center",
            width:130,

            dataIndex: 'month',
            customRender:(text)=>{
              return filterDictTextByCache('month' ,text)
            },
            sorter:(a,b)=>a.month-b.month
          },

          {
            title:'当月完成投资数(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            width:200,

            dataIndex: 'mrFinishInvestment'
          },
          {
            title:'总体进展情况描述',
            align:"center",
            width:300,

            dataIndex: 'mrDescription',
            scopedSlots:{customRender:'mrDescription'},
            ellipsis:true

          },
          {
            title:'需协调处理的问题',
            align:"center",
            width:300,

            dataIndex: 'mrNeedCoordinate',
            scopedSlots:{customRender:'mrNeedCoordinate'},

            ellipsis:true
          },
          {
            title:'是否显示在可视化',
            align:"center",
            width:200,

            dataIndex: 'mrNeedCoordinateShow',
            customRender:(text)=>{
              return filterDictTextByCache('sure_or_not' ,text)
            },
          },
/*          {
            title:'当月完成财务数(万元)',
            align:"center",
            dataIndex: 'mrFinishFinance'
          },

          */
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/monthreport/monthReport/listByPrjId",
          delete: "/monthreport/monthReport/delete",
          deleteBatch: "/monthreport/monthReport/deleteBatch",
          exportXlsUrl: "/monthreport/monthReport/exportXls",
          importExcelUrl: "monthreport/monthReport/importExcel",
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
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      getBool(value){
        if(value=='true'){
          return true

        }else{
          return false
        }

      },
      modalFormOk() {
        this.queryParam['prjId'] = this.info.id
        this.loadData(1);
      },
      initDictConfig(){
        initDictOptions('sure_or_not').then((res)=>{
          if(res.success){}
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>