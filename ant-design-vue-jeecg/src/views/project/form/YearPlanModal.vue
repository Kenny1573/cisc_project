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


        <a-form-item label="年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :lg="10">
          <a-date-picker format="YYYY" mode="year"  :value="year" :open="open" @openChange="openChange" @panelChange="panelChange" placeholder="请选择年份"></a-date-picker>
          </a-row>
        </a-form-item>
        <a-form-item label="投资金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :lg="10">
            <a-input prefix="￥" suffix="万元"
                     :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                     :parser="value => value.replace(/\$\s?|(,*)/g, '')"  v-decorator="['comCertificate']" placeholder="请输入投资金额" >

            </a-input>

          </a-row>


        </a-form-item>
        <a-form-item label="财务金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :lg="10">
            <a-input prefix="￥" suffix="万元"
                     :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                     :parser="value => value.replace(/\$\s?|(,*)/g, '')"  v-decorator="['comCertificate']" placeholder="请输入财务金额" >

            </a-input>

          </a-row>

        </a-form-item>
        <a-form-item label="形象进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['comTax']" placeholder="请输入形象进度"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['comBank']" placeholder="请输入备注"></a-textarea>
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
    name: "YearPlanModal",
    components: {
    },
    data () {
      return {
        open:false,
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

      openChange(status){
        if(status){
          this.open = true;

        }else{
          this.open=false;
        }
      },
      panelChange(value){
        this.year=value;
        this.open=value;
        },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'comName','comAddress','comCertificate','comTax','comBank','comContacts','comPhone'))
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
        this.form.setFieldsValue(pick(row,'comName','comAddress','comCertificate','comTax','comBank','comContacts','comPhone'))
      },


    }
  }
</script>