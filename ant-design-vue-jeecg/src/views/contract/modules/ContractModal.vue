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

        <a-form-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['conName', validatorRules.conName]" placeholder="请输入合同名称"></a-input>
        </a-form-item>
        <a-form-item label="合同编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['conNumber', validatorRules.conNumber]" placeholder="请输入合同编码"></a-input>
        </a-form-item>
        <a-form-item label="建设单位(甲方)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['conPartyA',validatorRules.requireTrue]" placeholder="请输入甲方" :trigger-change="true" dictCode="pjplat.BS_COMPANY,COM_NAME,COM_NAME,DEL_FLAG=0"></j-dict-select-tag>
        </a-form-item>
        <a-form-item label="承包人(乙方)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['conPartyB',validatorRules.requireTrue]" placeholder="请输入乙方" :trigger-change="true" dictCode="pjplat.BS_COMPANY,COM_NAME,COM_NAME,DEL_FLAG=0"></j-dict-select-tag>
        </a-form-item>
        <a-form-item label="第三方(丙方)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list"   v-decorator="['conPartyC']" placeholder="请输入丙方" :trigger-change="true" dictCode="pjplat.BS_COMPANY ,COM_NAME ,COM_NAME,DEL_FLAG=0 "></j-dict-select-tag>
        </a-form-item>
        <a-form-item label="合同金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input prefix="￥" suffix="万元" v-decorator="['conMoney', validatorRules.conMoney]" placeholder="请输入合同金额"></a-input>
          {{convertCurrency(this.form.getFieldValue('conMoney'))}}

        </a-form-item>
        <a-form-item label="合同签订日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择合同签订日期" v-decorator="['conSignDate',validatorRules.requireTrue]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="履行开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择履行开始日期" v-decorator="['conBeginDate']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="履行结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择履行结束日期" v-decorator="['conEndDate']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="合同附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['conAttachment']"  :number="5" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="清单附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['conDetailList']" :trigger-change="true"></j-upload>
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
  import JDictSelectTag from '@/components/dict/JDictSelectTag'



  export default {
    name: "ContractModal",
    components: {
      JDate,
      JUpload,JDictSelectTag
    },
    props:{
     conPrjId:{
      }
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
          conName:{
            rules: [
              { required: true, message: '请输入合同名称!' },
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
              { pattern: /^.{0,20}$/, message:'超出最大输入长度'},

            ]

          },
          conNumber:{
            rules: [
              { required: true, message: '请输入合同编码!' },
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
              { pattern: /^.{0,8}$/, message:'超出最大输入长度'},

            ]
          },
          requireTrue:{
            rules: [
              { required: true, message: '此处信息需要完善!' },
            ]
          },
          conMoney: {
            rules: [
              { required: true, message: '此处信息需要完善!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
        },
        url: {
          add: "/contract/contract/add",
          edit: "/contract/contract/edit",
        }
      }
    },
    created () {
    },
    methods: {
      convertCurrency (money) {
        if (money==null){
          money=0
        }
        money=money*10000
        //汉字的数字
        var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖')
        //基本单位
        var cnIntRadice = new Array('', '拾', '佰', '仟')
        //对应整数部分扩展单位
        var cnIntUnits = new Array('', '万', '亿', '兆')
        //对应小数部分单位
        var cnDecUnits = new Array('角', '分', '毫', '厘')
        //整数金额时后面跟的字符
        var cnInteger = '整'
        //整型完以后的单位
        var cnIntLast = '元'
        //最大处理的数字
        var maxNum = 9999999999.999999
        //金额整数部分
        var integerNum
        //金额小数部分
        var decimalNum
        //输出的中文金额字符串
        var chineseStr = ''
        //分离金额后用的数组，预定义
        var parts
        if (money == '') {
          return ''
        }
        money = parseFloat(money)
        if (money == 0) {
          chineseStr = cnNums[0] + cnIntLast + cnInteger
          return chineseStr
        }
        //转换为字符串
        money = money.toString()
        if (money.indexOf('.') == -1) {
          integerNum = money
          decimalNum = ''
        } else {
          parts = money.split('.')
          integerNum = parts[0]
          decimalNum = parts[1].substr(0, 6)
        }
        //获取整型部分转换
        if (parseInt(integerNum, 10) > 0) {
          var zeroCount = 0
          var IntLen = integerNum.length
          for (var i = 0; i < IntLen; i++) {
            var n = integerNum.substr(i, 1)
            var p = IntLen - i - 1
            var q = p / 4
            var m = p % 4
            if (n == '0') {
              zeroCount++
            } else {
              if (zeroCount > 0) {
                chineseStr += cnNums[0]
              }
              //归零
              zeroCount = 0
              chineseStr += cnNums[parseInt(n)] + cnIntRadice[m]
            }
            if (m == 0 && zeroCount < 4) {
              chineseStr += cnIntUnits[q]
            }
          }
          chineseStr += cnIntLast
        }
        //小数部分
        if (decimalNum != '') {
          var decLen = decimalNum.length
          for (var i = 0; i < decLen; i++) {
            var n = decimalNum.substr(i, 1)
            if (n != '0') {
              chineseStr += cnNums[Number(n)] + cnDecUnits[i]
            }
          }
        }
        if (chineseStr == '') {
          chineseStr += cnNums[0] + cnIntLast + cnInteger
        } else if (decimalNum == '') {
          chineseStr += cnInteger
        }
        return chineseStr
      },
      beforeunload(file){
        let size=file.size/1024/1024<5
        if(!size){
          this.$message.error('请上传大小在5M以内的文件')
        }

      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'conPrjId','conName','conNumber','conPartyA','conPartyB','conPartyC','conMoney','conSignDate','conBeginDate','conEndDate','conAttachment','conDetailList'))
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
            formData.conPrjId=this.conPrjId
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
        this.form.setFieldsValue(pick(row,'conName','conNumber','conPartyA','conPartyB','conPartyC','conMoney','conSignDate','conBeginDate','conEndDate','conAttachment','conDetailList'))
      },


    }
  }
</script>