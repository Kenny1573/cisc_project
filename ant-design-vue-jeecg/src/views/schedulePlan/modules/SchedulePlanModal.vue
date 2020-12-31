<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="节点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['spNodeNumber',validatorRules.spNodeNumber]" placeholder="请输入节点编号"></a-input>
        </a-form-item>
        <a-form-item label="节点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['spNodeName',validatorRules.spNodeName]" placeholder="请输入节点名称"></a-input>
        </a-form-item>
        <a-form-item label="节点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list"  v-decorator="['spNodeState',validatorRules.spNodeState]" :trigger-change="true" dictCode="scheduleplan_init" placeholder="请输入节点状态" />
        </a-form-item>
        <a-form-item  label="计划开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date  placeholder="请选择计划开始时间" v-decorator="['spNodeBeginTime', validatorRules.spNodeBeginTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="计划完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择计划完成时间" v-decorator="['spNodeEndTime',validatorRules.spNodeEndTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['spNodePerson',validatorRules.spNodePerson]" placeholder="请输入责任人" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="上传附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['spNodeAttachment']" :number="5" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="是否里程碑节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checked-children="是" un-checked-children="否"  :checked="checked" @click="switchChange"></a-switch>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "SchedulePlanModal",
    components: {
      JDate,
      JUpload,
      JDictSelectTag
    },
    props:{
      parentId:{

      },
      prjId:{},
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        checked: false,
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
          spNodeNumber: {
            rules: [
              { required: true, message: '请输入节点编号' },
              { pattern: /^.{0,8}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'}
            ]
          },
          spNodeName: {
            rules: [
              { required: true, message: '请输入节点名称' },
              { pattern: /^.{0,15}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},
            ]
          },
          spNodePerson: {
            rules: [
              { required: true, message: '请输入责任人' },
              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},
              { pattern: /^.{0,15}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          },
          spNodeState: {
            rules: [
              { required: true, message: '请选择节点状态' }
            ]
          },
          spNodeBeginTime: {
            rules: [
              { required: true, message: '请输入计划开始时间' }
            ]
          },
          spNodeEndTime: {
            rules: [
              { required: true, message: '请输入计划完成时间' }
            ]
          }

        },
        url: {
          add: "/scheduleplan/schedulePlan/add",
          edit: "/scheduleplan/schedulePlan/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);

        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'spNodeNumber','spNodeName','spNodeState','spNodeBeginTime','spNodeEndTime','spNodePerson','spNodeAttachment','spIsMilestone'))
        })
        this.checked=this.getBool(this.model.spIsMilestone)
      },
      switchChange(checked){

        this.checked=checked
        this.model.spIsMilestone=checked

      },
      getBool(value){
        if(value=='true'){
          return true

        }else{
          return false
        }

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
            formData.spNodeParent=this.parentId
            formData.spPrjId=this.prjId
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
        this.form.setFieldsValue(pick(row,'spNodeNumber','spNodeName','spNodeState','spNodeBeginTime','spNodeEndTime','spNodePerson','spNodeAttachment','spIsMilestone'))
      },


    }
  }
</script>