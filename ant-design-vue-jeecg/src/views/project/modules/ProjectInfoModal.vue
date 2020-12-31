<template>
    <j-modal
      :title="title"
      :width="width*2"
      :visible="visible"
      :confirmLoading="confirmLoading"
      switchFullscreen
      @cancel="handleCancel"
      cancelText="关闭">

      <template slot="footer">
        <a-button @click="handleCancel">关闭</a-button>
      </template>
    <a-card style="margin-top: 24px" :bordered="true" title="项目信息" >
      <detail-list>
        <detail-list-item term="项目名称" >{{model.prjName}}</detail-list-item>
        <detail-list-item term="项目编号">{{model.prjNumber}}</detail-list-item>
        <detail-list-item term="项目分类">{{model.prjType}}</detail-list-item>
        <detail-list-item term="项目阶段">{{model.prjStage}}</detail-list-item>
        <detail-list-item term="匡算">{{model.prjRoughEstimate}}  万元</detail-list-item>
        <detail-list-item term="估算">{{model.prjEstimate}} 万元</detail-list-item>
        <detail-list-item term="概算">{{model.prjBudgetEstimate}} 万元</detail-list-item>
        <detail-list-item term="决算">{{model.prjFinalEstimate}} 万元</detail-list-item>
        <detail-list-item term="工程地址">{{model.prjBuildingAddress}}</detail-list-item>
        <detail-list-item term="经度">{{model.prjLongitude}}</detail-list-item>
        <detail-list-item term="纬度">{{model.prjLatitude}}</detail-list-item>
        <detail-list-item term="工程参数">{{model.prjParams}}</detail-list-item>
        <detail-list-item term="建设规模">{{model.prjBuildingSize}}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
      <detail-list-item term="建设内容">{{model.prjBuildingContent}}</detail-list-item>
      </detail-list>

      <a-divider></a-divider>

      <detail-list>
        <detail-list-item term="前期业主代表" >{{leaders.preOwner}}</detail-list-item>
      </detail-list><detail-list>
        <detail-list-item term="实施业主代表">{{leaders.midOwner}}</detail-list-item>
      </detail-list><detail-list>
      <detail-list-item term="总师室">{{leaders.office}}</detail-list-item>
    </detail-list><detail-list>
      <detail-list-item term="道桥部">{{leaders.bridge}}</detail-list-item>
    </detail-list><detail-list>
      <detail-list-item term="建筑部">{{leaders.build}}</detail-list-item>
    </detail-list><detail-list>
      <detail-list-item term="质安部">{{leaders.quality}}</detail-list-item></detail-list><detail-list>
      <detail-list-item term="财务部">{{leaders.finance}}</detail-list-item></detail-list><detail-list>
      <detail-list-item term="计划部">{{leaders.plan}}</detail-list-item></detail-list><detail-list>
      <detail-list-item term="跟踪审计（外）">{{leaders.other}}</detail-list-item>

    </detail-list>
    </a-card>




      <project-modal ref="modalForm" @ok="modalFormOk"></project-modal>
      <project-leader-result :mainId="model.id"></project-leader-result>
      <YearPlanResultList :mainId="model.id" />
      <a-tabs default-active-key="1" >
        <a-tab-pane key="1" tab="监管单位">
          <company-result-list :mainId="model.id" :title="title1"></company-result-list>
        </a-tab-pane>
        <a-tab-pane key="2" tab="施工单位">
          <company-result-list :mainId="model.id" :title="title2"></company-result-list>
        </a-tab-pane>

      </a-tabs>

    </j-modal>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'
  const DetailListItem = DetailList.Item
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import CompanyList from '../CompanyList'
  import YearPlanResultList from '../YearPlanResultList'
  import CompanyResultList from '../CompanyResultList'
  import { getAction } from '../../../api/manage'
  import { JeecgListMixin } from '../../../mixins/JeecgListMixin'
  import ProjectModal from './ProjectModal'


  export default {
    name: "ProjectInfoModal",
    components: {
      YearPlanResultList,
      CompanyList,
      JDictSelectTag,
      DetailList,
      DetailListItem,
      CompanyResultList,
      JeecgListMixin,
      ProjectModal
    },
    data () {
      return {
        title1:'监管单位',
        title2:'施工单位',
        record:{},
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        leaders:{
          other:'' ,
          preOwner:'' ,
          build:'',
          office: '',
          bridge: '',
          plan: '',
          finance: '',
          midOwner: '',
          quality: ''
        },
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
              { required: true, message: '请输入项目名称!' },
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},

            ]
          },
          prjNumber: {
            rules: [
              { required: true, message: '请输入项目编号!' },
              { pattern: /^.{0,8}$/, message:'超出最大输入长度'},
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
              { required: true, message: '请输入项目阶段!'},
            ]
          },
          prjRoughEstimate: {
            rules: [
              { required: false, message: '请输入匡算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjEstimate: {
            rules: [
              { required: false, message: '请输入估算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjBudgetEstimate: {
            rules: [
              { required: false, message: '请输入概算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjFinalEstimate: {
            rules: [
              { required: false, message: '请输入决算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          prjBuildingSize: {
            rules: [
              { required: false, message: '请输入建设规模!' },              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},

            ]
          },
          prjBuildingContent: {
            rules: [
              { required: false, message: '请输入建设内容!' },
              { pattern: /^.{0,500}$/, message:'超出最大输入长度'},

            ]
          },
          prjBuildingAddress: {
            rules: [
              { required: false, message: '请输入工程地址!' },
              { pattern: /^.{0,50}$/, message:'超出最大输入长度'},

            ]
          },
          prjLatitude: {
            rules: [
              { required: true, message: '请输入经度!'},
            ]
          },
          prjParams: {
            rules: [
              { required: true, message: '请输入纬度!'},
            ]
          },
          prjLongitude: {
            rules: [
              { required: false, message: '请输入工程参数!' },              { pattern: /^.{0,100}$/, message:'超出最大输入长度'},

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
    },
    methods: {
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
      },
      add () {
        this.edit({});
      },
      edit (record) {

        this.record=record
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','prjName','prjNumber','prjType','prjStage','prjRoughEstimate','prjEstimate','prjBudgetEstimate','prjFinalEstimate','prjBuildingSize','prjBuildingContent','prjBuildingAddress','prjLatitude','prjParams','prjLongitude'))
        })

        function getNames (plan) {
          let name='';
          if (plan.length>0){
            for (let i = 0; i <plan.length ; i++) {
              let na=JSON.stringify(plan[i]);
              na=na.substring(1,na.length-1)
              name+=na+','
            }
            name=name.slice(0,name.length-1)
            return name
          }
        }

        getAction(this.url.leader,{prjlPrjId:this.model.id}).then((res) => {
          if (res.success) {
            this.leaders.plan = getNames(res.result.plan);
            this.leaders.quality = getNames(res.result.quality);
           this.leaders.other = getNames(res.result.other);
           this.leaders.preOwner = getNames(res.result.preOwner);
           this.leaders.midOwner=getNames(res.result.midOwner)
           this.leaders.build =getNames(res.result.build);
           this.leaders.office = getNames(res.result.office);
           this.leaders.bridge = getNames(res.result.bridge);
            this.leaders.finance = getNames(res.result.finance);
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
        })

      },

      close () {
        this.$emit('close');
        this.visible = false;
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