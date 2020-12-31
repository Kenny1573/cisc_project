<template>
  <a-card  :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div  style="font-weight: 500;font-size: 20px;text-align: center">

        {{this.projectName}}


    </div>
    <div class="table-operator">
      <a-row>
      <a-col span="22">


        <a-button v-if="selectedRowKeys.length > 0" style="margin-left: 5px" type="link" @click="batchDel"> 删除 <a-icon type="delete" /></a-button>
      </a-col ><a-col span="2">
      <a-button @click="handleEd" type="link ">项目选择</a-button>
      </a-col>
      </a-row>
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
          <a-tag color="pink" v-has="'ydetail:edit'" @click="handleEditd(record,false)">制定详细计划</a-tag>
          <a-tag color="green" v-has="'ydetail:search'" @click="handleEditd(record,true)">详情</a-tag>

          <a-tag color="red"><a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
            </a-tag>
        </span>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>
      </a-table>
    </div>

    <year-plan-detail-list ref="modalFormd" @ok="modalFormOk" :mainId="this.info.id"></year-plan-detail-list>
    <yearPlan-modal ref="modalForm" @ok="modalFormOk" :mainId="this.info.id"></yearPlan-modal>
    <project-select ref="modalForma" @ok="modalFormOk" @selectProject="selectProject"  @prjName="prjName"></project-select>
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

  import {colAuthFilter} from "@/utils/authFilter"

  export default {
    name: "YearPlanListMenu",
    mixins:[JeecgListMixin],
    components: { YearPlanModal ,YearPlanDetailList,ProjectSelect},

    data () {
      return {
        projectName:'',
        text:'项目选择',
        project:[],
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
            title:'目标投资数(万元)',
            scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'ypInvestment'
          },
/*          {
            title:'目标财务数(万元)',
            align:"center",
            dataIndex: 'ypFinance'
          },*/
          {
            title:'计划投资数(万元)',
            scopedSlots:{customRender:'money'},
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
      this.disableMixinCreated=true
      this.columns=colAuthFilter(this.columns,"yearOpt:")
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

    mounted () {
      this.$refs.modalForma.visible = true;

    },
    methods: {
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      selectProject(value){
        this.text=value.id
        this.prjName(value.prjName)
        this.info.id = value.id
        this.queryParam['ypPrjId'] = this.info.id
        this.loadData(1);
        //初始化字典配置 在自己页面定义
        this.initDictConfig()
      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      handleEditd: function (record,val) {
        this.$refs.modalFormd.editable = val;
        this.$refs.modalFormd.edit(record);
        this.$refs.modalFormd.title = "投资计划明细(万元)";
        this.$refs.modalFormd.disableSubmit = false;
      },
      moment,
      handleChange(value){
        this.year = value
      },
      prjName(value){
        this.projectName=value
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
