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

        <a-form-item label="单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comName',validatorRules.comName]" placeholder="请输入单位名称"></a-input>
        </a-form-item>
        <a-form-item label="单位地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comAddress',validatorRules.comAddress]" placeholder="请输入单位地址"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comLegalPerson',validatorRules.comLegalPerson]" placeholder="请输入法定代表人"></a-input>
        </a-form-item>
        <a-form-item label="社会统一信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comSocialCode',validatorRules.comSocialCode]" placeholder="请输入社会统一信用代码"></a-input>
        </a-form-item>
        <a-form-item label="单位资质信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comCertificate',validatorRules.comCertificate]" placeholder="请输入单位资质信息"></a-input>
        </a-form-item>
        <a-form-item label="单位银行信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comBank',validatorRules.comBank]" placeholder="请输入单位银行信息"></a-input>
        </a-form-item>
        <a-form-item label="单位联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comContacts',validatorRules.comContacts]" placeholder="请输入单位联系人"></a-input>
        </a-form-item>
        <a-form-item label="单位联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comPhone',validatorRules.comPhone]" placeholder="请输入单位联系电话"></a-input>
        </a-form-item>
        <a-form-item label="经营范围" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comBussinessScope',validatorRules.comBussinessScope]" placeholder="请输入经营范围"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'


  export default {
    name: "CompanyModal",
    components: {
    },
    data () {
      return {
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
          comPhone: {
            rules: [
              { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
            ]
          },
          comName: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},

              { pattern: /^\S.*$/, message:'不能以空格作为开头'},


            ]
          },
          comAddress: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comLegalPerson: {
            rules: [
              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comSocialCode: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comCertificate: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comBank: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comContacts: {
            rules: [
              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},

              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },
          comBussinessScope: {
            rules: [
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},            ]
          },


        },
        url: {
          add: "/company/company/add",
          edit: "/company/company/edit",
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
          this.form.setFieldsValue(pick(this.model,'comName','comAddress','comLegalPerson','comSocialCode','comCertificate','comBank','comContacts','comPhone','comBussinessScope'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
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
        this.form.setFieldsValue(pick(row,'comName','comAddress','comLegalPerson','comSocialCode','comCertificate','comBank','comContacts','comPhone','comBussinessScope'))
      },


    }
  }
</script>