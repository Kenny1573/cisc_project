<template>
  <a-card title="年度计划" :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->

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
        @change="handleTableChange">
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>
      </a-table>
    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YearPlanModal from './modules/YearPlanModal'



  export default {
    name: "YearPlanResultList",
    mixins:[JeecgListMixin],
    components: { YearPlanModal },
    props:{
      mainId:{
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['ypPrjId'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '项目信息管理页面',
        disableMixinCreated:true,
        // 表头
        columns: [
          {
            title:'年度',
            align:"center",
            dataIndex: 'ypYear'
          },
          {
            title:'目标投资数(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            scopedSlots:{customRender:'money'},

            dataIndex: 'ypInvestment'
          },
/*          {
            title:'目标财务数(万元)',
            align:"center",
            dataIndex: 'ypFinance'
          },*/
          {
            title:'形象进度',
            align:"center",
            dataIndex: 'ypImageProgress'
          },
        ],
        url: {
          list: "/project/project/listYearPlanByMainId",
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
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.mainId}`;
      }
    },
    methods: {
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

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
