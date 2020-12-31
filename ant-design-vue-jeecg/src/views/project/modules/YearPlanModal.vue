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

        <a-form-item label="年度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker  format="YYYY" mode="year" :allowClear="false"  :value="this.year" :open="this.open" @openChange="openChange" @panelChange="panelChange" @change="handleChange"></a-date-picker>
        </a-form-item>
        <a-form-item label="目标投资数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input prefix="￥" suffix="万元" v-decorator="['ypInvestment', validatorRules.ypInvestment]" placeholder="请输入目标投资数"></a-input>
          {{convertCurrency(this.form.getFieldValue('ypInvestment'))}}

        </a-form-item>
<!--        <a-form-item label="目标财务数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input prefix="￥" suffix="万元" v-decorator="['ypFinance', validatorRules.ypFinance]" placeholder="请输入目标财务数"></a-input>
        </a-form-item>-->
        <a-form-item label="形象进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ypImageProgress',validatorRules.ypImageProgress]" placeholder="请输入形象进度"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import moment from 'moment'

  export default {
    name: "YearPlanModal",
    components: {
    },
    props:{
      mainId:{
        type:Number,
        required:false,
      }
    },
    data () {
      return {
        year:moment('2020','YYYY'),
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
          ypImageProgress: {
            rules: [
              { required: false, message: '请输入形象进度!' },{ pattern: /^.{0,100}$/, message:'超出最大输入长度'},
            ]
          },
          ypYear: {
            rules: [
              { pattern: /^-?\d+\.?\d*$/, message: '请输入正确的年份!'},
            ]
          },
          ypInvestment: {
            rules: [
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          ypFinance: {
            rules: [
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
        },
        url: {
          add: "/project/project/addYearPlan",
          edit: "/project/project/editYearPlan",
        }

      }
    },
    created () {
    },
    methods: {
      moment,
      handleChange(value){
        this.year = value
      },
      openChange(status){
        if(status){
          this.open = true;

        }else{
          this.open=false;
        }
      },
      panelChange(value){


        this.year=value;
        this.open=false;
        this.model.ypYear=value.format('YYYY')
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        if(this.model.ypYear!=null) {
          this.year = moment(this.model.ypYear, 'YYYY')
        }else{
          this.model.ypYear='2020'
        }
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'ypPrjId','ypYear','ypInvestment','ypFinance','ypImageProgress'))

        })

      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.year=null
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
            formData['ypPrjId'] = this.mainId
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
        this.form.setFieldsValue(pick(row,'ypPrjId','ypYear','ypInvestment','ypFinance','ypImageProgress'))
      },
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


    }
  }
</script>
