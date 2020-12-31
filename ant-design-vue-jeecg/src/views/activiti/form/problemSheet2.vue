<template>
 <div class="form-main">
    <a-card :body-style="{padding: '24px 32px'}" :bordered="false">
      <a-form @submit="handleSubmit" :form="form">

        <a-form-item label="项目ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
         <!-- <j-search-select-tag v-decorator="['psPrjId']" dict="" /> -->
           <a-input v-decorator="['psPrjId']" placeholder="请输入项目ID"></a-input>
        </a-form-item>
        <a-form-item label="问题部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['psLocation']" placeholder="请输入问题部位"></a-input>
        </a-form-item>
        <a-form-item label="问题分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-category-select v-decorator="['psType']" pcode="B03" placeholder="请选择问题分类" />
        </a-form-item>
        <a-form-item label="隐患等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['psLevel']" :trigger-change="true" dictCode="hazard_level" placeholder="请选择隐患等级"/>
        </a-form-item>
        <a-form-item label="隐患描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['psDescription']" rows="4" placeholder="请输入隐患描述"/>
        </a-form-item>
        <a-form-item label="整改意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['psAdvice']" rows="4" placeholder="请输入整改意见"/>
        </a-form-item>
 <a-form-item v-if="!disabled" :wrapperCol="{ span: 24 }" style="text-align: center">
          <a-button
            htmlType="submit"
            type="primary"
            :disabled="disabled||btndisabled"
            @click="handleSubmit"
          >保存</a-button>
          <a-button style="margin-left: 8px" :disabled="disabled" @click="close">取消</a-button>
        </a-form-item>
    </a-form>
    </a-card>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JCategorySelect from '@/components/jeecg/JCategorySelect'
  import {httpAction, deleteAction, getAction, downFile,postAction } from '@/api/manage'
  import JUpload from '@/components/jeecg/JUpload'

  export default {
    name: "ProblemSheetModal",
    components: { 
      JDictSelectTag,
      JSearchSelectTag,
      JCategorySelect,
    },
    components: { JUpload },
      props: {
        /*全局禁用，可表示查看*/
        disabled: {
          type: Boolean,
          default: false,
          required: false
        },
        /*流程数据*/
        processData: {
          type: Object,
          default: () => {
            return {}
          },
          required: false
        },
        /*是否新增*/
        isNew: { type: Boolean, default: false, required: false }
      },
  data() {
    return {
      url: {
        getForm: '/actBusiness/getForm',
        addApply: '/actBusiness/add',
        editForm: '/actBusiness/editForm'
      },
      description: '流程表单demo，按例开发表单。需在 activitiMixin.js 中加入写好的表单',
      // form
      form: this.$form.createForm(this),
      /*表单回显数据*/
      data: {},
      btndisabled: false
    }
  },
  created() {
    console.log('流程数据', this.processData)
    if (!this.isNew) {
      this.init()
    }
  },
  methods: {
    /*回显数据*/
    init() {
      this.btndisabled = true
      var r = this.processData
      this.getAction(this.url.getForm, {
        tableId: r.tableId,
        tableName: r.tableName
      }).then(res => {
        if (res.success) {
          let formData = res.result
          formData.tableName = r.tableName
          this.data = formData
          console.log('表单回显数据', this.data)
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.data, 'name', 'item','file'))
          })
          this.btndisabled = false
        } else {
          this.$message.error(res.message)
        }
      })
    },
    // handler
    handleSubmit(e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          let formData = Object.assign(this.data || {}, values)
          formData.procDefId = this.processData.id
          console.log("++++"+this.processData.id);
          formData.procDeTitle = this.processData.name
          if (!formData.tableName) formData.tableName = this.processData.businessTable
              console.log("++++"+this.processData.businessTable);
          formData.filedNames = _.keys(values).join(',')
          console.log('formData', values)
          console.log('formData', formData)
          var url = this.url.addApply
          if (!this.isNew) {
            url = this.url.editForm
          }
          this.btndisabled = true
          getAction(url, formData)
            .then(res => {
              if (res.success) {
                this.$message.success('保存成功！')
                this.$emit('afterSubmit', formData)
              } else {
                this.$message.error(res.message)
              }
            })
            .finally(() => {
              this.btndisabled = false
            })
        }
      })
    },
    close() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="less" scoped>
</style>