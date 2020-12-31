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
        <a-form-item label="设备序列号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['deviceSerial',validatorRules.deviceSerial]" placeholder="请输入设备序列号"></a-input>
        </a-form-item>
        <a-form-item label="设备验证码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['validateCode',validatorRules.validateCode]" placeholder="请输入设备验证码"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>

</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { getAction } from '@/api/manage'

  export default {
    name: 'HelmetModal',

    props:{
      username:''

    },

    data () {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {
          deviceSerial:{
            rules: [
              { required: true, message: '请输入设备序列号!' },
              { pattern: /^.{0,10}$/, message:'超出最大输入长度'},
              { pattern: /^[a-zA-Z0-9]$/, message: '只能输入字母或数字'},

              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          },
          validateCode:{
            rules: [
              { required: true, message: '请输入设备验证码!' },
              { pattern: /^[a-zA-Z0-9]$/, message: '只能输入字母或数字'},

              { pattern: /^.{0,10}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          }

        },
        url: {
          addMine:'/helmet/addMyHelmet',
          add:'helmet/addHelmet'

        }
      }
    },
    methods:{
      add () {
        this.form.resetFields();
        this.model = Object.assign({}, {});
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'deviceSerial','validateCode'))
        })
        this.visible = true;

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
            let formData = Object.assign(this.model, values);
            if(this.username==null||this.username==''){
              getAction(this.url.add, formData).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.$emit('ok');
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }else {

              formData.username = this.username
              getAction(this.url.addMine, formData).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.$emit('ok');
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }


          }
          })


        },
      handleCancel () {
        this.close()
      },
    }
  }

</script>

<style scoped>

</style>