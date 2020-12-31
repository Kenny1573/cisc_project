<template>
  <j-modal
    :title="title"
    :width="width*2"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-form :form="form">
      <a-row>
        <a-col span="12" style="align-content: center">
          <a-form-item label="截至当月投资累计(万元)"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
            {{this.helpInfo.t2}}
          </a-form-item>
          <a-form-item label="今年至今投资累计(万元)"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
            {{this.helpInfo.t1}}
          </a-form-item>
          <a-form-item label="当月计划投资金额(万元)"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
            {{this.helpInfo.thisInvestment}}
          </a-form-item>
 <!--         <a-form-item  label="当月计划财务金额(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
            {{this.helpInfo.thisFinance}}
          </a-form-item>-->
          <a-form-item label="当月计划进度" :labelCol="labelCol" :wrapperCol="wrapperCol" >
            {{this.helpInfo.thisPlan}}
          </a-form-item>
          <a-form-item label="下月计划投资数(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['nextInvestment',validatorRules.nextInvestment]" rows="4" placeholder="请输入下月计划投资数"/>
            {{convertCurrency(this.form.getFieldValue('nextInvestment'))}}

          </a-form-item>
<!--          <a-form-item label="下月计划财务数(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['nextFinance']" rows="4" placeholder="请输入下月计划投资数"/>
          </a-form-item>-->
          <a-form-item label="下月形象进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-textarea v-decorator="['nextPlan',validatorRules.nextPlan]" rows="4" placeholder="请输入总体进展情况描述"/>
          </a-form-item>

        </a-col>
        <a-col span="12">
          <a-spin :spinning="confirmLoading">


            <a-form-item label="年份" v-show="show" :labelCol="labelCol"  :wrapperCol="wrapperCol" >
              <a-select @change="yearChange" :value="this.valueSelected"   v-decorator="['year',validatorRules.year]">
                <a-select-option v-for="(year,index) in this.yearList" :key="year.ypYear"> {{year.ypYear}}

                </a-select-option>
              </a-select>
            </a-form-item>

            <a-form-item label="月份" v-show="show" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list"  v-decorator="['month',validatorRules.month]" :trigger-change="true" dictCode="month" placeholder="请选择月份" @change="monthChange"/>
            </a-form-item>
            <a-form-item label="当月完成投资数(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['mrFinishInvestment',validatorRules.mrFinishInvestment]" rows="4" placeholder="请输入当月完成投资数"/>
              {{convertCurrency(this.form.getFieldValue('mrFinishInvestment'))}}

            </a-form-item>
<!--            <a-form-item label="当月完成财务数(万元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['mrFinishFinance']" rows="4" placeholder="请输入当月完成财务数"/>
            </a-form-item>-->
            <a-form-item label="总体进展情况描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['mrDescription',validatorRules.mrDescription]" rows="4" placeholder="请输入总体进展情况描述"/>
            </a-form-item>


            <a-form-item label="需协调处理的问题" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['mrNeedCoordinate',validatorRules.mrNeedCoordinate]" rows="4" placeholder="请输入需协调处理的问题"/>
            </a-form-item>
            <a-form-item label="下月计划安排" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['mrNextPlan',validatorRules.mrNextPlan]" rows="4" placeholder="请输入下月计划安排"/>
            </a-form-item>
            <a-form-item label="是否显示在可视化" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-switch checked-children="是" un-checked-children="否"  :checked="checked" @click="switchChange"></a-switch>
            </a-form-item>

          </a-spin>
        </a-col>
      </a-row>
    </a-form>
  </j-modal>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'
  const DetailListItem = DetailList.Item
  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import { postAction } from '../../../api/manage'

  export default {
    name: "MonthReportModalMenu",
    components: {
      DetailList,
      DetailListItem,
      JDictSelectTag
    },
    props:{
      mainId:{
      }
    },
    created () {
      this.disableMixinCreated=true
      getAction(this.url.listYear,{ypPrjId:this.mainId}).then((res)=>{
        if(res.success){
          this.yearList=res.result.records
        }else{
          this.$message.warning(res.message);
        }
      })
    },
    data () {
      return {
        disableMixinCreated:true,
        helpInfo:{},
        show:false,
        valueSelected:'',
        checked: false,
        yearId: '',
        status: false,
        projectName: '',
        ypPrjId: '',
        yearName: '',
        yearList: [],
        text: '项目选择',
        project: [],
        info: {},
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
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
        url: {
          plan:'/yearplandetail/yearPlanDetail/addOrUpdateMonthPlanList',
          helpInfo:'/monthreport/monthReport/getLastMonthData',
          listProject: "/project/project/listProject",
          add: "/monthreport/monthReport/add",
          edit: "/monthreport/monthReport/edit",
          listYear: '/project/project/listYearPlanByMainId'
        },
        validatorRules: {
          month: {
            rules: [
              { required: true, message: '请选择月份' }
            ]
          },
          year: {
            rules: [
              { required: true, message: '请选择年份或是先创建一个年度投资计划' }]

          },

          nextPlan: {
            rules: [
              { required: false },
              { pattern: /^.{0,500}$/, message:'超出最大输入长度'},            ]
          },
          mrDescription: {
            rules: [
              { required: false },
              { pattern: /^.{0,500}$/, message:'超出最大输入长度'},            ]
          },
          mrNeedCoordinate: {
            rules: [
              { required: false },
              { pattern: /^.{0,500}$/, message:'超出最大输入长度'},
            ]
          },
          mrNextPlan: {
            rules: [
              { required: false },
              { pattern: /^.{0,500}$/, message:'超出最大输入长度'},
            ]
          },
          mrFinishInvestment: {
            rules: [
              { required: true, message: '请输入当月完成投资金额' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          },
          nextInvestment:{
            rules: [
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!'},
            ]
          }

        },
      }},


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
      monthChange(value){
        if(this.model.year!=null) {
          getAction(this.url.helpInfo, { prjId: this.mainId, year: this.model.year, month: value }).then((res) => {

            this.helpInfo = Object.assign({}, res.result);
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.helpInfo, 'yearPlanDetailId', 'thisFinance','mrNextPlan', 'nextFinance', 'lastYear','mrNeedCoordinate','mrNeedCoordinateShow', 'nextInvestment', 'nextYear', 'thisInvestment','status','thisPlan','nextPlan','t1','t2','mrFinishInvestment','mrDescription'))


            })
            this.checked=this.getBool(this.helpInfo.mrNeedCoordinateShow)

          })
        }
        this.model.month=value

      },

      yearChange(value){
        this.model.year=value
        this.valueSelected=value
        for (let i = 0; i <this.yearList.length ; i++) {
          if (this.yearList[i].ypYear==value){
            this.yearId=this.yearList[i].id

          }

        }
        if(this.model.month!=null) {
          getAction(this.url.helpInfo, { prjId: this.mainId, year: this.model.year, month: this.model.month }).then((res) => {
            this.helpInfo = Object.assign({}, res.result);
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.helpInfo, 'yearPlanDetailId', 'thisFinance','mrNextPlan', 'nextFinance', 'lastYear','mrNeedCoordinate','mrNeedCoordinateShow', 'nextInvestment', 'nextYear', 'thisInvestment','status','thisPlan','nextPlan','t1','t2','mrFinishInvestment','mrDescription'))

            })
            this.checked=this.getBool(this.helpInfo.mrNeedCoordinateShow)

          })
        }
      },
      switchChange(checked){

        this.checked=checked
        this.model.mrNeedCoordinateShow=checked



      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      getBool(value){
        if(value=='true'){
          return true

        }else{
          return false
        }

      },
      add () {
        this.helpInfo.thisInvestment=''
        this.helpInfo.t2=''
        this.helpInfo.t1=''
        this.helpInfo.thisPlan=''
        getAction(this.url.listYear,{ypPrjId:this.mainId}).then((res)=>{
          if(res.success){
            this.yearList=res.result.records
          }else{
            this.$message.warning(res.message);
          }
        }),
        this.edit({});
        this.show=true
      },
      edit (record) {
        this.show=false
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'year','month','mrNeedCoordinateShow','mrDescription','mrFinishInvestment','mrFinishFinance','mrNextPlan','mrNeedCoordinate'))

        })
        this.checked=this.getBool(this.model.mrNeedCoordinateShow)
        this.valueSelected=this.model.year
        if(this.model.year!=null&&this.model.year!='') {
          getAction(this.url.helpInfo, {
            prjId: this.mainId,
            year: this.model.year,
            month: this.model.month
          }).then((res) => {
            this.helpInfo = Object.assign({}, res.result);
            this.form.setFieldsValue(pick(this.helpInfo, 'yearPlanDetailId', 'thisFinance','mrNextPlan', 'nextFinance', 'lastYear','mrNeedCoordinate','mrNeedCoordinateShow', 'nextInvestment', 'nextYear', 'thisInvestment','status','thisPlan','nextPlan','t1','t2','mrFinishInvestment','mrDescription'))
            this.checked=this.getBool(this.model.mrNeedCoordinateShow)
          })
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
            formData.yearId=this.yearId
            formData.mrNeedCoordinateShow=this.checked
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
          if(this.mainId){
            let formData = Object.assign(this.helpInfo, values);
            postAction(this.url.plan,{ypdYear:this.model.year,prjId:this.mainId,ypdFinance:formData.nextFinance,ypdInvestment:formData.nextInvestment,id:formData.yearPlanDetaiilId,ypdMonth:this.getMonth(this.model.month),ypdPlan:formData.nextPlan}).then((res)=>{
            })
          }
        })


      },
      getMonth(month){
        switch (month){
        case "1": return "2";
        case "2": return "3";
        case "3": return "4";
        case "4": return "5";
        case "5": return "6";
        case "6": return "7";
        case "7": return "8";
        case "8": return "9";
        case "9": return "10";
        case "10": return "11";
        case "11": return "12";
        case "12": return "1";
        }

      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'mrNeedCoordinateShow','mrDescription','mrFinishInvestment','mrFinishFinance','mrNextPlan','mrNeedCoordinate'))
      },


    }
  }
</script>