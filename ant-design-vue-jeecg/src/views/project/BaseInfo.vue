<template>
<div >
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
    <a-card style="margin-top: 24px" :bordered="true" title="项目信息" >
      <a slot="extra" v-has="'project:edit'" @click="edit()">编辑</a>
      <detail-list>
        <detail-list >
        <detail-list-item term="项目名称" >{{model.prjName}}</detail-list-item>
        <detail-list-item term="项目编号">{{model.prjNumber}}</detail-list-item>
        <detail-list-item term="项目分类">{{model.prjType}}</detail-list-item>
        </detail-list>
        <detail-list>
        <detail-list-item term="项目阶段">{{model.prjStage}}</detail-list-item>
        <detail-list-item term="匡算">{{formatMoney(model.prjRoughEstimate)}}  万元</detail-list-item>
        <detail-list-item term="估算">{{formatMoney(model.prjEstimate)}} 万元</detail-list-item>
        </detail-list>
        <detail-list >
        <detail-list-item term="概算">{{formatMoney(model.prjBudgetEstimate)}} 万元</detail-list-item>
        <detail-list-item term="决算">{{formatMoney(model.prjFinalEstimate)}} 万元</detail-list-item>
        <detail-list-item term="工程地址">{{model.prjBuildingAddress}}</detail-list-item>
        </detail-list>
        <detail-list >
        <detail-list-item term="经度">{{model.prjLongitude}}</detail-list-item>
        <detail-list-item term="纬度">{{model.prjLatitude}}</detail-list-item>
        <detail-list-item term="工程参数">{{model.prjParams}}</detail-list-item>
        </detail-list>
        <detail-list >
        <detail-list-item term="建设规模">{{model.prjBuildingSize}}</detail-list-item>
            <detail-list-item term="建设内容">{{model.prjBuildingContent}}</detail-list-item>
        </detail-list>
      </detail-list>

      <a-divider></a-divider>
      <detail-list size="small" :col="1">
        <detail-list-item term="前期业主代表">{{preOwner}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="实施业主代表">{{midOwner}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="总师室">{{office}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="道桥部">{{bridge}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="建筑部">{{build}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="质安部">{{quality}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="财务部">{{finance}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="计划部">{{plan}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="跟踪审计（外）">{{other}}</detail-list-item>

      </detail-list>
    </a-card>



    <project-modal ref="modalForm" @ok1="modalFormOk" @setVs="setVs" @initData="initData"></project-modal>
    <YearPlanList :mainId="model.id" />
  <a-tabs default-active-key="1" >
    <a-tab-pane key="1" tab="监管单位">
      <company-list :mainId="model.id" :title="title1"></company-list>
    </a-tab-pane>
    <a-tab-pane key="2" tab="施工单位">
      <company-list :mainId="model.id" :title="title2"></company-list>
    </a-tab-pane>

  </a-tabs>







</div>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'
  const DetailListItem = DetailList.Item
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import CompanyList from './CompanyList'
  import { getAction } from '../../api/manage'
  import { JeecgListMixin } from '../../mixins/JeecgListMixin'
  import ProjectModal from './modules/ProjectModal'
  import YearPlanList from './YearPlanList'

  import Vue from 'vue'


  export default {
    name: "BaseInfo",
    components: {
      YearPlanList,
      CompanyList,
      JDictSelectTag,
      DetailList,
      DetailListItem,
      JeecgListMixin,
      ProjectModal
    },
    data () {
      return {
        title1:'监管单位',
        title2:'施工单位',
        info:{},
        other:'' ,
        preOwner:'' ,
        build:'',
        office: '',
        bridge: '',
        plan: '',
        finance: '',
        midOwner: '',
        quality: '',

        record:[],
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules: {
          prjName: {
            rules: [
              { required: true, message: '请输入项目名称!' },{ pattern: /^.{0,24}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          },
          prjNumber: {
            rules: [
              { required: true, message: '请输入项目编号!' },{ pattern: /^.{0,24}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          },
          prjType: {
            rules: [
              { required: true, message: '请输入项目分类!' }
            ]
          },
          prjStage: {
            rules: [
              { required: true, message: '请输入项目阶段!' }
            ]
          },
          prjRoughEstimate: {
            rules: [
              { required: false, message: '请输入匡算!'},
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjEstimate: {
            rules: [
              { required: false, message: '请输入估算!'},
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjBudgetEstimate: {
            rules: [
              { required: false, message: '请输入概算!'},
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },

          prjBuildingSize: {
            rules: [
              { required: false, message: '请输入建设规模!' },{ pattern: /^.{0,24}$/, message:'超出最大输入长度'}
            ]
          },
          prjBuildingContent: {
            rules: [
              { required: false, message: '请输入建设内容!' },{ pattern: /^.{0,500}$/, message:'超出最大输入长度'},
            ]
          },
          prjBuildingAddress: {
            rules: [
              { required: false, message: '请输入工程地址!' },{ pattern: /^.{0,49}$/, message:'超出最大输入长度'}
            ]
          },
          prjLongitude: {
            rules: [
              { required: false, message: '请输入经度!' }
            ]
          },
          prjLatitude: {
            rules: [
              { required: false, message: '请输入纬度!' }
            ]
          },
          prjParams: {
            rules: [
              { required: false, message: '请输入工程参数!' },{ pattern: /^.{0,49}$/, message:'超出最大输入长度'}
            ]
          },
          prjLongitude: {
            rules: [
              { required: false, message: '请输入工程参数!'},
            ]
          },
        },
        url: {
          add: "/project/project/add",
          edit: "/project/project/edit",
          leader:"/projectleader/projectLeader/lists"
        }

      }
    },
    created () {

      this.initData()



    },


    methods: {

      initData(){
        if (typeof (this.$route.query.record)!='string'&&typeof (this.$route.query.record)!= 'undefined'){
          this.info=this.$route.query.record
        }else {
          this.info = Vue.ls.get('projectInfo')
        }
        this.form.resetFields();

        this.model = Object.assign({}, this.info);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','prjName','prjNumber','prjType','prjStage','prjRoughEstimate','prjEstimate','prjBudgetEstimate','prjFinalEstimate','prjBuildingSize','prjBuildingContent','prjBuildingAddress','prjLatitude','prjParams','prjLongitude'))
        })
        function getNames (plan) {
          let name = ''
          if (plan.length > 0) {
            for (let i = 0; i < plan.length; i++) {
              let na = JSON.stringify(plan[i])
              na = na.substring(1, na.length - 1)
              name += na + ','
            }
            name = name.slice(0, name.length - 1)
            return name
          }


        }
        getAction(this.url.leader, { prjlPrjId: this.model.id}).then((res) => {
          this.other=getNames(res.result.other)
          this.preOwner=getNames(res.result.preOwner)
          this.build=getNames(res.result.build)
          this.office=getNames(res.result.office)
          this.bridge=getNames(res.result.bridge)
          this. plan=getNames(res.result.plan)
          this. finance=getNames(res.result.finance)
          this.midOwner=getNames(res.result.midOwner)
          this. quality=getNames(res.result.quality)

        })
      },

      setVs(val){
        Vue.ls.set('projectInfo',val,50*60*60*1000)
        this.info = val
        this.form.resetFields();

        this.model = Object.assign({}, this.info);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','prjName','prjNumber','prjType','prjStage','prjRoughEstimate','prjEstimate','prjBudgetEstimate','prjFinalEstimate','prjBuildingSize','prjBuildingContent','prjBuildingAddress','prjLatitude','prjParams','prjLongitude'))
        })


      },

      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      modalFormOk() {
        if (typeof (this.$route.query.record)!='string'&&typeof (this.$route.query.record)!= 'undefined'){

          this.info=this.$route.query.record
        }else {
          this.info = Vue.ls.get('projectInfo')
        }
        this.form.resetFields();

        this.model = Object.assign({}, this.info);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','prjName','prjNumber','prjType','prjStage','prjRoughEstimate','prjEstimate','prjBudgetEstimate','prjFinalEstimate','prjBuildingSize','prjBuildingContent','prjBuildingAddress','prjLatitude','prjParams','prjLongitude'))
        })
        function getNames (plan) {
          let name = ''
          if (plan.length > 0) {
            for (let i = 0; i < plan.length; i++) {
              let na = JSON.stringify(plan[i])
              na = na.substring(1, na.length - 1)
              name += na + ','
            }
            name = name.slice(0, name.length - 1)
            return name
          }
        }
        getAction(this.url.leader, { prjlPrjId: this.model.id}).then((res) => {
          this.other=getNames(res.result.other)
          this.preOwner=getNames(res.result.preOwner)
          this.build=getNames(res.result.build)
          this.office=getNames(res.result.office)
          this.bridge=getNames(res.result.bridge)
          this. plan=getNames(res.result.plan)
          this. finance=getNames(res.result.finance)
          this.midOwner=getNames(res.result.midOwner)
          this. quality=getNames(res.result.quality)

        })


      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      edit(){
          this.$refs.modalForm.edit(this.model);
          this.$refs.modalForm.title = "编辑";
          this.$refs.modalForm.disableSubmit = false;

      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'id','prjName','prjNumber','prjType','prjStage','prjRoughEstimate','prjEstimate','prjBudgetEstimate','prjFinalEstimate','prjBuildingSize','prjBuildingContent','prjBuildingAddress','prjLatitude','prjParams','prjLongitude'))
      },


    }
  }
</script>