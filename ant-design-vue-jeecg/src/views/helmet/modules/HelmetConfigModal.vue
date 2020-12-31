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
        <a-form-item label="静止告警间隔时间(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-slider v-decorator="['abnormalRestTime']" :min="model.abnormalRestTimeMin" :max="model.abnormalRestTimeMax" placeholder="请输入静止告警时间间隔"></a-slider>
          当前值：<a-tag color="blue"  >{{this.form.getFieldValue('abnormalRestTime')}}</a-tag> 最小值：<a-tag color="pink"  >{{this.model.abnormalRestTimeMin}}</a-tag>最大值：<a-tag color="red"  >{{this.model.abnormalRestTimeMax}}</a-tag>
        </a-form-item>
        <a-form-item label="GPS定位频率(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-slider v-decorator="['gpsReportInterval']"  :min="model.gpsReportIntervalMin" :max="model.gpsReportIntervalMax" placeholder="请输入GPS定位频率"></a-slider>
          当前值：<a-tag color="blue"  >{{this.form.getFieldValue('gpsReportInterval')}}</a-tag> 最小值：<a-tag color="pink"  >{{this.model.gpsReportIntervalMin}}</a-tag>最大值：<a-tag color="red"  >{{this.model.gpsReportIntervalMax}}</a-tag>

        </a-form-item>
        <a-form-item label="脱戴帽消抖时间(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-slider v-decorator="['eliminatingJitterTime']" :min="model.eliminatingJitterTimeMin" :max="model.eliminatingJitterTimeMax" placeholder="请输入脱戴帽消抖时间"></a-slider>
          当前值：<a-tag color="blue"  >{{this.form.getFieldValue('eliminatingJitterTime')}}</a-tag> 最小值：<a-tag color="pink"  >{{this.model.eliminatingJitterTimeMin}}</a-tag>最大值：<a-tag color="red"  >{{this.model.eliminatingJitterTimeMax}}</a-tag>

        </a-form-item>
        <a-form-item label="撞击灵敏度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row>
            <a-col :span="3">
              <a-select  v-decorator="['hitSensitivity']" >
              <a-select-option value="0">
                0
              </a-select-option>
              <a-select-option value="2" :disabled="dis2">
                2
              </a-select-option>
              <a-select-option value="4" :disabled="dis4">
                4
              </a-select-option>
            </a-select></a-col><a-col :span="1"></a-col><a-col :span="6">
            <j-dict-select-tag type="list"  v-decorator="['hitSensitivity']" :trigger-change="true" :disabled="true" dictCode="helmet_sentivity" placeholder="请输入撞击灵敏度" />
          </a-col>
          </a-row>



        </a-form-item>



      </a-form>
    </a-spin>
  </j-modal>

</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { getAction } from '@/api/manage'
  import { postAction } from '../../../api/manage'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'HelmetConfigModal',

    props:{
      userId:''

    },

    data () {
      return {
        takeoff:{
          min:30,
          max:21600,
          value:30
        },
        wear:{
          min:30,
          max:7200,
          value:30
        },
        gps:{
          min:10,max:3300,value:10
        },
        hit:{
          min:0,
          max:4,
          value:0
      },
        eli:{
          min:2,
          max:30,
          value:2
        },
        abnormal:{
          min:60,max:480,value:60
        },
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
        dis2:false,
        dis4:false,
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
              { pattern: /^[a-zA-Z0-9]$/, message: '只能输入字母或数字'},

              { required: true, message: '请输入设备验证码!' },
              { pattern: /^.{0,10}$/, message:'超出最大输入长度'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          }

        },
        url: {
          edit:'/helmet/configHelmet'

        }
      }
    },
    methods:{

      edit(record){
        this.visible = true;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'wearReportInterval','wearReportIntervalMin','wearReportIntervalMax','takeoffReportInterval','takeoffReportIntervalMin','takeoffReportIntervalMax',
              'abnormalRestTime','abnormalRestTimeMin','abnormalRestTimeMax','eliminatingJitterTime','eliminatingJitterTimeMin','eliminatingJitterTimeMax',
              'hitSensitivity','hitSensitivityMin','hitSensitivityMax','gpsReportInterval','gpsReportIntervalMin','gpsReportIntervalMax'))
        })
        if(this.model.hitSensitivityMax==4){

        }else if(this.model.hitSensitivityMax==2){
          this.dis4=true
        }else{
          this.dis2=true
          this.dis4=true
        }

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

            let formData = Object.assign({}, values);

              postAction(this.url.edit, formData).then((res) => {
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