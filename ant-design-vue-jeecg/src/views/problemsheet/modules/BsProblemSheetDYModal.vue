<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    okText="打印"
    @cancel="handleCancel"
    >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="24">
          <a-col :span="24">
            <div
              class="account-settings-info-main"
              :style="'min-height:'+mainInfoHeight+';overflow:hidden'"
            >
              <div class="account-settings-info-left">
                <a-menu
                  :mode="'inline'"
                  :style="{ border: '0', width:'auto'}"
                  :defaultSelectedKeys="defaultSelectedKeys"
                  @click="handleClick"
                  :selectedKeys="selectedKeys"
                  type="inner"
                >
                  <a-menu-item key="1" :id="'xcjcxx'">现场检查记录</a-menu-item>
                  <a-menu-item key="2">整改完成情况</a-menu-item>
                </a-menu>
              </div>
              
              <div class="account-settings-info-right" id="scjlb" v-show="see">
                <div style="width:90%;margin:auto;text-align: center;" id="jbxx">

                  <a-row :gutter="24">
                    <a-col :span="8"></a-col>
                    <a-col :span="8">
                          <a-form-item>
                          <a-input id="psPrjname" style="text-align:center;border-left-width:0px!important;border-top-width:0px!important;border-right-width:0px!important;border-radius:0px;" v-model="psPrjId_dictText"></a-input>
                          </a-form-item>
                    </a-col>
                    <a-col :span="8"></a-col>
                  </a-row>

                  <a-row :gutter="24">
                    <a-col :span="8"></a-col>
                    <a-col :span="8">
                      <a-form-item>
                          <a-input id="tbname" style="font-weight:bolder;text-align:center;border:0px!important;" v-model="jctablebame"></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="8"></a-col>
                  </a-row>

                  <a-row :gutter="24" style="text-align: left;height: inherit;border:1px solid black;">
                    <a-col :span="12" style="vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:26px;min-height:26px;">施工单位:{{psConstructionUnit_dictText}}</div>
                    </a-col>
                    <a-col :span="12" style="vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:26px;min-height:26px;">监理单位:{{psSupervisingUnit_dictText}}</div>
                    </a-col>

                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:125px;min-height:125px;padding-top:10px;"> 
                        <div>
                        <p style="margin-bottom: 5px;">发现问题:{{psLocation}}</p>
                        <p style="margin-bottom: 5px;">分类<br>
                        {{wtms}}</p>
                        <p style="margin-bottom: 5px;">问题描述:{{psDescription}}</p>
                        </div>
                      </div>
                    </a-col>

                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:380px;min-height:380px;">
                       <a-row>
                        <a-col :span="18"><p><a-divider orientation="left">发现问题图片</a-divider></p></a-col>
                        <a-col :span="6"></a-col>
                        <!-- 预览区域 -->
                        <a-col :span="24">
                        <template>
                        <div v-for="(fileDetail,i) in zgpic[0].fileDetails" :key="i" >
                        <div style="float: left;width:320px;height:350px;margin-right: 15px;margin: 0 4px 4px 0;">
                          <div  style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 0px;">
                            <img style="width: 100%;" :src="fileDetail.imgUrl" :preview="zgpic[0].key">
                          </div>
                        </div>
                       </div>
                       </template>
                       </a-col>
                      </a-row> 
                    </div>
                    </a-col>

                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:80px;min-height:80px;padding-top:10px;"> 
                        <div>
                        <p style="margin-bottom: 5px;">整改意见:<br>
                        &emsp;&emsp;{{psAdvice}}
                        </p>
                        </div>
                      </div>
                      <a-col :span="16">                  
                      </a-col>
                      <a-col :span="8" style="width: 0%;">
                          <a-divider style="text-align: left;font-size:15px;" orientation="right">检查人员：{{jcry}}<br>检查时间：{{jcsj}}</a-divider>
                      </a-col>
                    </a-col>
                    
                  </a-row>
                </div>

              </div>

              <div class="account-settings-info-right" id="zgbg" v-show="!see">
                    <div style="width:90%;margin:auto;text-align: center;" id="qtxx">
                    
                      <a-row :gutter="24">
                        <a-col :span="8"></a-col>
                         <a-col :span="8">
                          <a-form-item>
                          <a-input id="tbname" style="font-weight:bolder;text-align:center;border:0px!important;" v-model="zgtablebame"></a-input>
                          </a-form-item>
                        </a-col>
                       <a-col :span="8"></a-col>
                      </a-row>

                      <a-row :gutter="24" style="text-align: left;margin-bottom: 8px">
                         <a-col :span="24" style="vertical-align:middle;height: inherit;">
                            <span>工程名称:</span><a-input id="psPrjname" style="width:90%;text-align: center;border-left-width:0px!important;border-top-width:0px!important;border-right-width:0px!important;border-radius:0px;" v-model="psPrjId_dictText"/>
                         </a-col>
                      </a-row>
                      <a-row :gutter="24" style="text-align: left;margin-bottom: 8px">
                         <a-col :span="12" style="vertical-align:middle;height: inherit;">
                            <span>施工单位:</span><a-input id="sgdw" style="width:80%;border-left-width:0px!important;border-top-width:0px!important;border-right-width:0px!important;border-radius:0px;" v-model="psConstructionUnit_dictText"/>
                         </a-col>
                         <a-col :span="12" style="vertical-align:middle;height: inherit;">
                            <span>监理单位:</span><a-input id="jldw" style="width:80%;border-left-width:0px!important;border-top-width:0px!important;border-right-width:0px!important;border-radius:0px;" v-model="psSupervisingUnit_dictText"/>
                         </a-col>
                      </a-row>

                    <a-row :gutter="24" style="text-align: left;height: inherit;border:1px solid black;">
                    
                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:380px;min-height:380px;">
                       <a-row>
                        <a-col :span="18"><p><a-divider orientation="left">整改前问题图片</a-divider></p></a-col>
                        <a-col :span="6"></a-col>
                        <!-- 预览区域 -->
                        <a-col :span="24">
                        <template>
                        <div v-for="(fileDetail,i) in zgpic[0].fileDetails" :key="i" >
                        <div style="float: left;width:320px;height:350px;margin-right: 15px;margin: 0 4px 4px 0;">
                          <div  style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 0px;">
                            <img style="width: 100%;" :src="fileDetail.imgUrl" :preview="zgpic[0].key">
                          </div>
                        </div>
                       </div>
                       </template>
                       </a-col>
                      </a-row> 
                    </div>
                    </a-col>

                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:380px;min-height:380px;">
                       <a-row>
                        <a-col :span="18"><p><a-divider orientation="left">整改图片</a-divider></p></a-col>
                        <a-col :span="6"></a-col>
                        <!-- 预览区域 -->
                        <a-col :span="24">
                        <template>
                        <div v-for="(zfileDetail,i) in zgwpic[0].ZfileDetails" :key="i" >
                        <div style="float: left;width:320px;height:350px;margin-right: 15px;margin: 0 4px 4px 0;">
                          <div  style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
                            <img style="width: 100%;" :src="zfileDetail.imgUrl" :preview="zgwpic[0].key">
                          </div>
                          </div>
                         </div>
                       </template>
                       </a-col>
                      </a-row> 
                    </div>
                    </a-col>

                    <a-col :span="24" style="text-align: left;vertical-align:middle;height: inherit;border:1px solid black;">
                      <div style="height:auto;height:80px;min-height:80px;padding-top:10px;"> 
                        <div>
                        <p style="margin-bottom: 5px;">整改情况:<br>
                        &emsp;&emsp;{{zgqk}}
                        </p>
                        </div>
                      </div>
                      <a-col :span="16">                  
                      </a-col>
                      <a-col :span="8" style="width: 0%;">
                          <a-divider style="text-align: left;font-size:15px;" orientation="right">项目经理：{{xmjl}}<br>整改日期：{{xmjlrq}}</a-divider>
                      </a-col>
                    </a-col>

                    <a-col :span="24" style="text-align: left;height: inherit;vertical-align:middle;border:1px solid black;">

                             <div style="height:auto;height:60px;min-height:60px;padding-top:10px;"> 
                               <div>
                               <p style="margin-bottom: 5px;">监理单位复查意见:
                                 <br>
                                 &emsp;&emsp;{{jlfcyj}}
                               </p>
                              </div>
                             </div>
                             <a-col :span="16">                  
                             </a-col>
                             <a-col :span="8" style="width: 0%;">
                                 <a-divider style="text-align: left;font-size:15px;" orientation="right">监理人员：{{xmzj}}<br>复查日期：{{xmzjrq}}</a-divider>
                             </a-col>
                    </a-col>

                    <a-col :span="24" style="text-align: left;height: inherit;vertical-align:middle;border:1px solid black;">

                             <div style="height:auto;height:60px;min-height:60px;padding-top:10px;"> 
                               <div>
                               <p style="margin-bottom: 5px;">复查意见:
                                 <br>
                                 &emsp;&emsp;{{jcfcyj}}
                               </p>
                              </div>
                             </div>
                             <a-col :span="16">                  
                             </a-col>
                             <a-col :span="8" style="width: 0%;">
                                 <a-divider style="text-align: left;font-size:15px;" orientation="right">检查人员：{{xmjcry}}<br>检查日期：{{xmjcrq}}</a-divider>
                             </a-col>
                    </a-col>

                  </a-row>

                    </div>
              </div>

            </div>

          </a-col>
        </a-row>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  //import JDictSelectTag from "@/components/dict/JDictSelectTag"
  //import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  //import JCategorySelect from '@/components/jeecg/JCategorySelect'

  export default {
    name: "BsProblemSheetModal",
    components: { 
      //JDictSelectTag,
      //JSearchSelectTag,
      //JCategorySelect,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:'90%',
        visible: false,
        mainInfoHeight: '100%',
        defaultSelectedKeys: ['1'],
        selectedKeys:['1'],
        jctablebame: "现 场 检 查 记 录 表", //tablebame
        zgtablebame:"整改完成报告单",
        see: true, //信息table展示
        model: {},
        zgpic: [
          {
          "fileDetails": 
          [{},{},{},{},{},{},{},{},{}],
          //[{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""},{"imgUrl": ""}],
          "key": 0
          }
        ],
        psPrjId_dictText:'',
        psLocation:'',
        psConstructionUnit_dictText:'',
        psSupervisingUnit_dictText:'',
        psType_dictText:'',
        psLevel_dictText:'',
        psType:'',
        psLevel:'',
        psDescription:'',
        psAdvice:'',
        wtms:'',
        jcry:"",
        jcsj:"",
        zgxx: true,//整改信息
        zgqk:"",
        xmjl:"",
        xmjlrq:"",
        jlfcyj:"",
        xmzj:"",
        xmzjrq:"",
        jcfcyj:"",
        xmjcry:"",
        xmjcrq:"",
        zgwpic: [
          {
          "ZfileDetails": 
          [{},{},{},{},{},{},{},{},{}],
           "key": 0
          }
        ],
        //fileDetails:[],
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
        dataSource: [],
        url: {
          add: "/problemsheet/bsProblemSheet/add",
          zgqpiclist: "/problemsheet/bsProblemSheet/PSpiclist",
          zgqklist: "/problemsheet/bsProblemSheet/PSdealcplist",
          zgwpiclist: "/problemsheet/bsProblemSheet/ZGpiclist",
          edit: "/problemsheet/bsProblemSheet/edit",
          loadPic: "/sys/common/static/"
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
        
        this.handlezg(record.id);

        this.visible = true;

        this.psPrjId_dictText=record.psPrjId_dictText;
        this.psLocation=record.psLocation;
        this.psConstructionUnit_dictText=record.psConstructionUnit_dictText;
        this.psSupervisingUnit_dictText=record.psSupervisingUnit_dictText;
        this.psType_dictText=record.psType_dictText;
        this.psLevel_dictText=record.psLevel_dictText;
        this.psType=record.psType;
        this.psLevel=record.psLevel;
        this.psDescription=record.psDescription;
        this.psAdvice=record.psAdvice_dictText;
        this.wtms=record.wtms;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {

        var div_print = "";

        if(this.see){
          div_print = document.getElementById("scjlb");
        }else{
          //zgbg
          div_print = document.getElementById("zgbg");
        }

        //var newstr = div_print.innerHTML;
        //var oldstr = document.body.innerHTML;

        //window.document.body.innerHTML = newstr;
        //window.print();
        //window.location.replace();
        //window.document.body.innerHTML = oldstr;

        //console.log(div_print.innerHTML);

        //判断iframe是否存在，不存在则创建iframe
        var iframe=document.getElementById("print-iframe");
        if(!iframe){  
            //var el = document.getElementById("scjlb");
            iframe = document.createElement('IFRAME');
            var doc = null;
            iframe.setAttribute("id", "print-iframe");
            //iframe.setAttribute('style', 'position:absolute;width:1000px;height:0px;left:-500px;top:-500px;');
            document.body.appendChild(iframe);
            doc = iframe.contentWindow.document;
            //console.log(iframe);
            //console.log(doc);
            //这里可以自定义样式
            doc.write('<LINK rel="stylesheet" type="text/css" href="../dyprint.css">');
            doc.write(div_print.innerHTML);
            doc.close();
            //console.log(div_print.innerHTML);
            
            if(this.see){
              iframe.contentWindow.document.getElementById("psPrjname").value = this.psPrjId_dictText;
              iframe.contentWindow.document.getElementById("tbname").value = this.jctablebame;
            }else{
                //zgbg
              iframe.contentWindow.document.getElementById("psPrjname").value = this.psPrjId_dictText;
              iframe.contentWindow.document.getElementById("tbname").value = this.zgtablebame;
              iframe.contentWindow.document.getElementById("sgdw").value = this.psConstructionUnit_dictText;
              iframe.contentWindow.document.getElementById("jldw").value = this.psSupervisingUnit_dictText;
            }
            
            iframe.contentWindow.focus();            
        }else{
            
            var doc = null;
            //iframe.setAttribute("id", "print-iframe");
            //iframe.setAttribute('style', 'position:absolute;width:1000px;height:0px;left:-500px;top:-500px;');
            //document.body.appendChild(iframe);
            doc = iframe.contentWindow.document;
            //这里可以自定义样式
            doc.write('<LINK rel="stylesheet" type="text/css" href="../dyprint.css">');
            doc.write('<div style="page-break-after:always;">'+div_print.innerHTML+'</div>');
            doc.close();
            
            if(this.see){
              iframe.contentWindow.document.getElementById("psPrjname").value = this.psPrjId_dictText;
              iframe.contentWindow.document.getElementById("tbname").value = this.jctablebame;
            }else{
                //zgbg
              iframe.contentWindow.document.getElementById("psPrjname").value = this.psPrjId_dictText;
              iframe.contentWindow.document.getElementById("tbname").value = this.zgtablebame;
              iframe.contentWindow.document.getElementById("sgdw").value = this.psConstructionUnit_dictText;
              iframe.contentWindow.document.getElementById("jldw").value = this.psSupervisingUnit_dictText;
            }

            iframe.contentWindow.focus();
        }

        //console.log(""+iframe);

        iframe.onload = function(){
          iframe.contentWindow.print();
          //document.body.removeChild(iframe);
        }
        //document.body.removeChild(iframe);
        //if (navigator.userAgent.indexOf("MSIE") > 0){
          //document.body.removeChild(iframe);
        //}
        return false;
      },
      handleCancel () {
        //window.print()
        this.close()
      },
      handleClick: function(e) {
      //e相当于点击的item
        console.log(e);
        if (e.key == '1') {
          this.see = true
          this.selectedKeys = ['1']
        } else if (e.key == '2') {
          this.see = false
          this.selectedKeys = ['2']
        }
    },
      popupCallback(row){
      },
      handleCategoryChange(value,backObj){
        this.form.setFieldsValue(backObj)
      },      
      getbdImgView(text){

      if(text && text.startsWith('http')){
        return text;
      }

      if(text && text.indexOf(",")>0){
        text = text.substring(0,text.indexOf(","))
      }
      
      text = window._CONFIG['domianURL'] + "/" + text
      return text
      },
      handlepic(record){

        const that = this;
        
        let httpurl = '';
        let method = '';
           
        httpurl+=this.url.zgqpiclist;
        method = 'get';

        let domianURLtext = window._CONFIG['domianURL'] + this.url.loadPic;

        console.log(domianURLtext);

        let formData = "";   
        getAction(httpurl,{psid:record.id,pictype:'1',picurl:domianURLtext,ydy:'dy'}).then((res)=>{
              //console.log(res.result);
              if(res.success){
                that.zgpic = Object.assign({},that.zgpic,res.result)
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              //that.confirmLoading = false;
              //that.close();
            })
      },
      handlezgpic(sheetid,tsid){

        const that = this;
        
        let httpurl = '';
        let method = '';
           
        httpurl+=this.url.zgwpiclist;
        method = 'get';

        let domianURLtext = window._CONFIG['domianURL'] + this.url.loadPic;

        //console.log("---"+httpurl)
        getAction(httpurl,{psid:sheetid,taskid:tsid,picurl:domianURLtext,ydy:'dy'}).then((res)=>{
              //console.log(res.result)
              if(res.success){
                that.zgwpic = res.result
                //console.log(that.zgwpic)
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
            })
      },
      handlezg(psheetid){

        const that = this;
        
        let httpurl = '';
        let method = '';
           
        //httpurl+=this.url.zgqpiclist;
        httpurl+=this.url.zgqklist;
        method = 'get';

        let formData = "";
        let taskid = 0;  

        getAction(httpurl,{sheetid:psheetid,stype:'1'}).then((res)=>{
              if(res.success){
                //console.log(res.result);
                //console.log(res.result['deal']);
                that.jcry = res.result['start'].uname;
                that.jcsj = this.ltimetoe(res.result['start'].dealsj);
                that.zgqk = res.result['deal'].avmeg;
                that.xmjl = res.result['deal'].uname;
                that.xmjlrq = res.result['deal'].dealsj;
                that.jlfcyj = res.result['check'].avmeg;
                that.xmzj = res.result['check'].uname;
                that.xmzjrq = res.result['check'].dealsj,
                that.jcfcyj = res.result['sure'].avmeg;
                that.xmjcry = res.result['sure'].uname;
                that.xmjcrq = res.result['sure'].dealsj;

                taskid = res.result['deal'].taskid;

                this.handlezgpic(psheetid,taskid);

              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              //that.confirmLoading = false;
              //that.close();
            })
      },
      ltimetoe(ltstr){

        let tmpstr = '';

        if(ltstr.length>10){
           tmpstr = ltstr.slice(0,10).replace(/\//g,'.');
        }else{
           tmpstr = ltstr;
        }
        return tmpstr;
      }
    }
  }
</script>
<style lang="less" scoped>
.account-settings-info-main {
  width: 100%;
  display: flex;
  height: 100%;
  overflow: auto;

  &.mobile {
    display: block;

    .account-settings-info-left {
      border-right: unset;
      border-bottom: 1px solid #e8e8e8;
      width: 100%;
      height: 50px;
      overflow-x: auto;
      overflow-y: scroll;
    }
    .account-settings-info-right {
      padding: 20px 40px;
    }
  }

  .account-settings-info-left {
    border-right: 1px solid #e8e8e8;
    width: 180px;
  }

  .account-settings-info-right {
    flex: 1 1;
    padding: 8px 40px;

    .account-settings-info-title {
      color: rgba(0, 0, 0, 0.85);
      font-size: 20px;
      font-weight: 500;
      line-height: 28px;
      margin-bottom: 12px;
    }
    .account-settings-info-view {
      padding-top: 12px;
    }
  }
}
.animate__animated {
  -webkit-animation-duration: 0.5s;
  animation-duration: 0.5s;
  -webkit-animation-duration: 0.5s;
  animation-duration: 0.5s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}

  .table-operator {
    margin-bottom: 10px
  }

  .ant-form-item {
    margin-bottom: 2px;
    line-height: 10px;
  }

  .ant-divider .ant-divider-horizontal .ant-divider-with-text-center{
    margin: 10px 0px;
  }

  .clName .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle {
    width: 10px !important;
  }

  .clName .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
    background-color: #1890FF !important;
  }
</style>