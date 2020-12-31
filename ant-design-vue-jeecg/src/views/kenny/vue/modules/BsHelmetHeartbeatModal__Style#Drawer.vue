<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="设备序列号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['deviceSerial']" placeholder="请输入设备序列号"></a-input>
        </a-form-item>
        <a-form-item label="通道号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['channelNo']" placeholder="请输入通道号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="消息id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['messageId']" placeholder="请输入消息id"></a-input>
        </a-form-item>
        <a-form-item label="消息时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择消息时间" v-decorator="['messageTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="脱戴帽状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['helmetStatus']" placeholder="请输入脱戴帽状态" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="蓝牙beaconID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['beaconId']" placeholder="请输入蓝牙beaconID"></a-input>
        </a-form-item>
        <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['longitude']" placeholder="请输入经度"></a-input>
        </a-form-item>
        <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['latitude']" placeholder="请输入纬度"></a-input>
        </a-form-item>
        <a-form-item label="电池电量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['powerPercent']" placeholder="请输入电池电量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="上下班" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['workStatus']" placeholder="请输入上下班"></a-input>
        </a-form-item>
        <a-form-item label="GPS信号强度
" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['rssiSignalStrength']" placeholder="请输入GPS信号强度
"></a-input>
        </a-form-item>
        <a-form-item label="静止警告" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['isAbnormalStationary']" placeholder="请输入静止警告" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="SOS状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['isSos']" placeholder="请输入SOS状态" style="width: 100%"/>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  
  export default {
    name: "BsHelmetHeartbeatModal",
    components: { 
      JDate,
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
        },
        url: {
          add: "/kenny/bsHelmetHeartbeat/add",
          edit: "/kenny/bsHelmetHeartbeat/edit",
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
          this.form.setFieldsValue(pick(this.model,'deviceSerial','channelNo','messageId','messageTime','helmetStatus','beaconId','longitude','latitude','powerPercent','workStatus','rssiSignalStrength','isAbnormalStationary','isSos'))
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
        this.form.setFieldsValue(pick(row,'deviceSerial','channelNo','messageId','messageTime','helmetStatus','beaconId','longitude','latitude','powerPercent','workStatus','rssiSignalStrength','isAbnormalStationary','isSos'))
      }
      
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>