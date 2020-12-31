<template>
  <j-modal
    :title="title"
    :width="1700"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <template slot="footer">
      <a-button @click="handleOk">关闭</a-button>
    </template>
  <a-card :bordered="false" >
    <!-- table区域-begin -->

      <div style="margin-bottom: 50px">
        <a-row :span="24" style="margin-left: 200px;font-size: 16px;margin-bottom: 40px ;align-content: center" >
          <a-col :span="8"><span style="font-weight: 500" >计划年度：</span><span>{{this.yearTarget}}</span></a-col>
          <a-col :span="8"><span style="font-weight: 500"> 目标投资数：</span><span>{{this.model.ypInvestment}}万元</span></a-col>
<!--
          <span style="font-weight: 500">目标财务数：</span><span>{{this.model.ypFinance}}</span></a-col>
-->
        </a-row>
        <a-row>
          <a-col  :span="12" style="margin-left: 200px;font-size: 16px;margin-bottom: 40px"> <span style="font-weight: 500">形象进度：</span><span>{{this.model.ypImageProgress}}</span></a-col>

        </a-row>

      </div>

      <div style="margin-bottom: 100px">


        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="this.dataListShow"
          :pagination="false"
          :loading="loading"
          :scroll="{x:1000,y:300}"
          @change="handleTableChange"
        >
          <a slot="Jan"  @click="editd('1')" :disabled="editable"> 1月
            <a-icon type="edit"/>
          </a>
          <a slot="Feb" @click="editd('2')" :disabled="editable"> 2月
            <a-icon type="edit"/>
          </a>
          <a slot="Mar" @click="editd('3')" :disabled="editable"> 3月
            <a-icon type="edit"/>
          </a>
          <a slot="Apr" @click="editd('4')" :disabled="editable"> 4月
            <a-icon type="edit"/>
          </a>
          <a slot="May" @click="editd('5')" :disabled="editable"> 5月
            <a-icon type="edit"/>
          </a>
          <a slot="Jun" @click="editd('6')" :disabled="editable"> 6月
            <a-icon type="edit"/>
          </a>
          <a slot="Jul" @click="editd('7')" :disabled="editable"> 7月
            <a-icon type="edit"/>
          </a>
          <a slot="Aug" @click="editd('8')" :disabled="editable"> 8月
            <a-icon type="edit"/>
          </a>
          <a slot="Sep" @click="editd('9')" :disabled="editable"> 9月
            <a-icon type="edit"/>
          </a>
          <a slot="Oct" @click="editd('10')" :disabled="editable"> 10月
            <a-icon type="edit"/>
          </a>
          <a slot="Nov" @click="editd('11')" :disabled="editable"> 11月
            <a-icon type="edit"/>
          </a>
          <a slot="Dec" @click="editd('12') " :disabled="editable"> 12月
            <a-icon type="edit"/>
          </a>
          <template slot="money" slot-scope="record">


            {{formatMoney(record)}}

          </template>

        </a-table>

      </div>


    <yearPlanDetail-modal ref="modalForm"  @editChange="editChange"></yearPlanDetail-modal>
  </a-card>

  </j-modal>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YearPlanDetailModal from './modules/YearPlanDetailModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import Vue from 'vue'
  import { getAction } from '../../api/manage'
  import moment from 'moment'
  import pick from 'lodash.pick'

  export default {
    name: 'YearPlanDetailList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      YearPlanDetailModal
    },
    props:{
      mainId:{
        type:String||Number,
        required:false,
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
          }else{
            this.queryParam['prjId'] = val
            this.initData()
            //初始化字典配置 在自己页面定义
            this.initDictConfig()
          }
        },
      }
    },
    data () {
      return {
        editable:true,
        form: this.$form.createForm(this),
        title:'明细',
        visible:false,
        yearDetail: {},
        info: {},
        yearTarget: '',
        disableMixinCreated: true,
        idList:[],
        yearList: [],
        dataList: [],
        dataListShow: [],
        model:{},
        description: '月度计划管理页面',
        // 表头
        columns: [
          {
            align: 'center',
            dataIndex: 'clazz'
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '1',
            slots: { title: 'Jan' ,text:'money'}
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '2',
            slots: { title: 'Feb' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '3',
            slots: { title: 'Mar' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '4',
            slots: { title: 'Apr' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '5',
            slots: { title: 'May' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '6',
            slots: { title: 'Jun' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '7',
            slots: { title: 'Jul' }
          }, {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '8',
            slots: { title: 'Aug' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '9',
            slots: { title: 'Sep' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '10',
            slots: { title: 'Oct' }
          },
          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '11',
            slots: { title: 'Nov' }
          },

          {
            align: 'center',scopedSlots:{customRender:'money'},
            dataIndex: '12',
            slots: { title: 'Dec' }
          }
          ,

          {
            title:'合计',scopedSlots:{customRender:'money'},
            align: 'center',
            dataIndex: 'total',
          }

        ],
        url: {
          list: '/yearplandetail/yearPlanDetail/getMonthPlanList',
          delete: '/yearplandetail/yearPlanDetail/delete',
          deleteBatch: '/yearplandetail/yearPlanDetail/deleteBatch',
          exportXlsUrl: '/yearplandetail/yearPlanDetail/exportXls',
          importExcelUrl: 'yearplandetail/yearPlanDetail/importExcel'
        },
        dictOptions: {}
      }
    },
    created () {
      this.info = Vue.ls.get('projectInfo')
      this.queryParam['prjId'] = this.info.id
      this.initData()
      //初始化字典配置 在自己页面定义
      this.initDictConfig()
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      handleOk(){
        this.$emit('ok');
        this.handleCancel()
      },
      handleCancel(){
        this.visible = false
      },
      edit(record){
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'ypPrjId','ypYear','ypInvestment','ypFinance','ypImageProgress'))

        })
        this.radioChange(record.ypYear)
        this.visible=true
      },
      formatMoney(num){

        if (num!=null) {
          return parseFloat(num.toString()).toLocaleString()
        }

      },
      editd (month) {
        let detail={}
        detail.ypdYear = this.yearTarget
        detail.ypdMonth = month
        detail.ypdInvestment = this.dataList[0][month]
/*
        detail.ypdFinance = this.dataList[1][month]
*/
        detail.ypdPlan=this.dataList[1][month]
        detail.prjId = this.queryParam['prjId']
        detail.id=this.idList[0][month]

        this.$refs.modalForm.edit(detail)
        this.$refs.modalForm.title = '编辑'
        this.$refs.modalForm.disableSubmit = false
      },
      editChange(event){
        console.log("editChange this.yearTarget is " +this.yearTarget)
        this.yearTarget = event

        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }

        var params = this.getQueryParams()//查询条件
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result
            if (this.dataSource.length > 0) {
              for (let i = 0; i < this.dataSource.length; i++) {
                if (this.yearTarget == this.dataSource[i].year) {
                  this.dataSource[i].investment.clazz = '月计划投资数'
                  /*this.dataSource[i].finance.clazz = '月计划财务数'*/
                  this.dataListShow.length = 0
                  this.dataList.length = 0
                  this.dataList.push(this.dataSource[i].investment)
                  /*this.dataList.push(this.dataSource[i].finance)*/
                  this.dataListShow.push(this.dataSource[i].investment)
                  /*this.dataListShow.push(this.dataSource[i].finance)*/
                  this.dataList.push(this.dataSource[i].plan)

                  this.idList.length=0
                  this.idList.push(this.dataSource[i].id)
                }
              }
            }
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })

      },
      radioChange (event) {
        this.yearTarget = event
        console.log("this.dataSouce is"+JSON.stringify(this.dataSource)+this.yearTarget)
        for (let i = 0; i < this.dataSource.length; i++) {
          if (this.yearTarget == this.dataSource[i].year) {
            this.dataSource[i].investment.clazz = '月计划投资数'
            //this.dataSource[i].finance.clazz = '月计划财务数'
            this.dataList.length = 0
            this.dataListShow.length = 0
            this.dataList.push(this.dataSource[i].investment)
            //this.dataList.push(this.dataSource[i].finance)
            this.dataListShow.push(this.dataSource[i].investment)
            //this.dataListShow.push(this.dataSource[i].finance)

            this.dataList.push(this.dataSource[i].plan)

            this.idList.length=0
            this.idList.push(this.dataSource[i].id)

          }
        }
      },
      initData () {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }

        var params = this.getQueryParams()//查询条件
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result
            if (this.dataSource.length > 0) {

              for (let i = 0; i < this.dataSource.length; i++) {
                this.yearList.push(this.dataSource[i].year.toString())
              }
              this.yearList.sort()
              this.yearTarget = this.yearList[0]
              for (let i = 0; i < this.dataSource.length; i++) {
                if (this.yearList[0] == this.dataSource[i].year) {
                  this.dataSource[i].investment.clazz = '月计划投资数'
                  //this.dataSource[i].finance.clazz = '月计划财务数'
                  this.dataList.push(this.dataSource[i].investment)
                  //this.dataList.push(this.dataSource[i].finance)
                  this.dataListShow.push(this.dataSource[i].investment)
                  //this.dataListShow.push(this.dataSource[i].finance)

                  this.dataList.push(this.dataSource[i].plan)
                  this.idList.push(this.dataSource[i].id)

                }
              }
            }
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      initDictConfig () {
      },

    }
  }
</script>
<style scoped>

  @import '~@assets/less/common.less';
</style>