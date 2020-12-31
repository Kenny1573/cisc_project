<template>
  <a-card  :bordered="false" :class="'cust-erp-sub-tab'" style="width: 1520px" >
    <!-- 操作按钮区域 -->

      <div align="center" style="width: 100%" >
        <a-dropdown  style="width: 80px" v-if="this.initYear" >
          <a v-if="JSON.stringify(this.initYear)!='{}'">{{this.initYear}}年<a-icon type="down"/></a>
          <a-menu slot="overlay">
          <a-menu-item v-for="(year,index) in this.yearList" :key="year.id" :value="year.ypYear"><a @click="yearChange(year)">{{year.ypYear}}</a>
          </a-menu-item>
          </a-menu>
        </a-dropdown>{{this.projectName}}投资情况表
      </div>

    <!-- table区域-begin -->
    <div>


      <a-table
        ref="table"
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        @change="handleTableChange">

        <template slot="title" slot-scope="currentPageData">
          <a-row span="24">
            <a-col  span="12" style="text-align: left"> 项目总概算 : {{prjBudgetEstimate}}</a-col>
            <a-col span="12" style="text-align: right"><a-tag color="pink" >单位：万元</a-tag></a-col>
          </a-row>
          </template>

        <template slot="planProcess" slot-scope="record">

          <a-popover title="计划形象进度" trigger="hover" placement="leftTop">
            <template slot="content">
              <div style="width: 500px">
                {{record}}</div>
            </template>
            {{record}}
            </a-popover>
        </template>

        <template slot="realProcess" slot-scope="record">

          <a-popover title="实际形象进度" trigger="hover" placement="leftTop">
            <template slot="content">
              <div style="width: 500px">
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
    <project-select ref="modalForma" @ok="modalFormOk" @selectProject="selectProject" ></project-select>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YearPlanModal from './modules/YearPlanModal'

  import moment from 'moment'
  import Vue from 'vue'
  import YearPlanDetailList from './YearPlanDetailList'
  import { getAction } from '../../api/manage'
 import ProjectSelect from './modules/ProjectSelect'


  export default {
    name: "InvestmentStatistics",
    mixins:[JeecgListMixin],
    components: { YearPlanModal ,YearPlanDetailList,ProjectSelect},

    data () {
      return {
        prjBudgetEstimate:'',
        ypPrjId:'',
        initYear:'',
        yearList:[],
        projectName:'',
        text:'项目选择',
        project:[],
        info:{},
        year:'',
        open:false,
        description: '项目信息管理页面',
        disableMixinCreated:true,
        // 表头
        columns: [
          {
            title:'月份',
            align:"center",
            dataIndex: 'month'
          },
          {
            title:'计划投资数',scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'planInvestment'
          },
/*          {
            title:'计划财务数',
            align:"center",
            dataIndex: 'planFinance'
          },*/
          {
            title:'实际投资数', scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'realInvestment'
          },
/*          {
            title:'实际财务数',
            align:"center",
            dataIndex: 'realFinance'
          },*/
          {
            title:'截至本月累计投资数', scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'totalInvestment'
          },
/*          {
            title:'截至本月累计财务数',
            align:"center",
            dataIndex: 'totalFinance'
          },*/
          {
            title:'月度投资占比',
            align:"center",
            dataIndex: 'perMonth'
          },
          {
            title:'截至当前月累计投资完成',
            align:"center",
            dataIndex: 'perTotal'
          },
          {
            title:'计划形象进度',
            dataIndex:'planProcess',
            align:"center",
            ellipsis: true,

            scopedSlots:{customRender:'planProcess'}
          },
          {
            title:'实际形象进度',
            align:"center",
            dataIndex: 'realProcess',
            ellipsis: true,
            scopedSlots:{customRender:'realProcess'}

          }

        ],
        url: {
          getReport:'/investmentStatistics/getMonthPlanList',
          listYear:'/project/project/listYearPlanByMainId',
          listProject:"/project/project/listProject",
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
      this.info=Vue.ls.get('projectInfo')
      this.selectProject(this.info)
      getAction(this.url.listProject).then((res) => {
        if (res.success) {
          this.project=res.result
        }

      })



    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.info.id}`;
      }
    },
    methods: {
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      yearChange(value){
        this.yearId=value.id
        this.year=value.ypYear+'年'
        this.initYear = value.ypYear
          getAction(this.url.getReport,{yearId:value.id}).then((res) => {
          if (res.success) {
            let total = res.result.total
            total.month='合计:'
            this.dataSource=res.result.list
            this.dataSource.push(total)
            console.log("this.dataSource is "+JSON.stringify(this.dataSource))

          }

        })

      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      selectProject(value){
        this.ypPrjId=value.id
        this.projectName=value.prjName
        this.prjBudgetEstimate=value.prjBudgetEstimate

        getAction(this.url.listYear,{ypPrjId:this.ypPrjId}).then((res)=>{
          if(res.success){
            this.yearList=res.result.records
            if (this.yearList.length>0){

              let date = new Date();
              let nowYear=date.getFullYear()
              this.initYear=this.yearList[0]
              if(this.yearList.length>1) {
                for (let i = 1; i < this.yearList.length; i++) {
                  if(Math.abs(this.yearList[i-1].ypYear-nowYear)>Math.abs(this.yearList[i].ypYear-nowYear)){
                    this.initYear=this.yearList[i]
                  }

                }
              }

              this.yearChange(this.initYear)
            }else{
              this.initYear={}
              this.dataSource=[]

            }

          }else{
            this.$message.warning(res.message);
          }
        })
      },


    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
