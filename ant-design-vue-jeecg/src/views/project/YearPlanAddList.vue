<template>
  <a-card title="年度计划" :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator" >
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
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
        :dataSource="this.yeardata"
        :pagination="false"
        :loading="loading"
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
           <a-popconfirm  title="确定删除吗?" @confirm="() => deleteRecord(record)">
            <a-tag color="red" >删除</a-tag>
          </a-popconfirm>
        </span>

        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>

      </a-table>
    </div>

    <yearPlan-modal ref="modalForm" :oldData="this.yeardata" @addYearData="addYearData" @ok="modalFormOk" :mainId="mainId"></yearPlan-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YearPlanModal from './modules/YearPlanAddModal'



  export default {
    name: "YearPlanAddList",
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
        yeardata: [],
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
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
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
      deleteRecord(record){
        for(let i=0;i<this.yeardata.length;i++){
          if (record.ypYear==this.yeardata[i].ypYear) {
            this.yeardata.splice(i,1)
          }
        }
      },
      addYearData(e) {
        this.yeardata.push(e)
      },
      getData(){
        return this.yeardata
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
