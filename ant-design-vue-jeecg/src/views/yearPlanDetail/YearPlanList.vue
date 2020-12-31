<template>
  <a-card :title="this.info.prjName" :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator">

      <a-dropdown v-has="'pyearplan:edit'" v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"

        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">


        <span slot="action"  slot-scope="text, record">
          <a-tag color="pink" v-has="'ydetail:edit'" @click="handleEditd(record,false)">制定详细计划</a-tag>
          <a-tag color="green" v-has="'ydetail:search'" @click="handleEditd(record,true)">详情</a-tag>
          <a-popconfirm v-has="'pyearplan:edit'" title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a-tag color="red">删除</a-tag>
          </a-popconfirm>
        </span>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>
      </a-table>
    </div>

    <year-plan-detail-list ref="modalFormd" @ok="modalFormOk" ></year-plan-detail-list>
    <yearPlan-modal ref="modalForm" @ok="modalFormOk" :mainId="this.info.id"></yearPlan-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YearPlanModal from './modules/YearPlanModal'

  import moment from 'moment'
  import Vue from 'vue'
  import YearPlanDetailList from './YearPlanDetailList'

  import {colAuthFilter} from "@/utils/authFilter"


  export default {
    name: "YearPlanList",
    mixins:[JeecgListMixin],
    components: { YearPlanModal ,YearPlanDetailList},

    data () {
      return {
        info:{},
        year:moment('2020-06','YYYY-MM'),
        open:false,
        description: '项目信息管理页面',
        disableMixinCreated:true,
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
            title:'年度',
            align:"center",
            dataIndex: 'ypYear'
          },
          {
            title:'目标投资数(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'ypInvestment'
          },
/*          {
            title:'目标财务数(万元)',
            align:"center",
            dataIndex: 'ypFinance'
          },*/
          {
            title:'计划投资数(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'investmentTotal'
          },
/*          {
            title:'计划财务数(万元)',
            align:"center",
            dataIndex: 'financeTotal'
          },*/
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/project/project/listYearPlanWithFinanceAndInvestmentByMainId",
          delete: "/project/project/deleteYearPlan",
          deleteBatch: "/project/project/deleteBatchYearPlan",
          exportXlsUrl: "/project/project/exportYearPlan",
          importUrl: "/project/project/importYearPlan",
        },
        dictOptions:{
         prjStage:[],
        },

      }
    },
    created () {
      this.info = Vue.ls.get('projectInfo')
      this.queryParam['ypPrjId'] = this.info.id

      this.disableMixinCreated=true
      this.columns=colAuthFilter(this.columns,"yearOpt:")
      this.loadData(1);
      //初始化字典配置 在自己页面定义
      this.initDictConfig()

    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.info.id}`;
      }
    },
    methods: {
      handleEditd: function (record,val) {
        this.$refs.modalFormd.editable = val;
        this.$refs.modalFormd.edit(record);
        this.$refs.modalFormd.title = "投资计划明细(万元)";
        this.$refs.modalFormd.disableSubmit = false;
      },
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      moment,
      handleChange(value){
        this.year = value
      },
      openChange(status){
        if(status){
          this.open = true;

        }else{
          this.open=false;
        }
      },
      panelChange(value){
        this.year=value;
        this.open=false;
        this.model.ypYear=value.format('YYYY')
      },
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
