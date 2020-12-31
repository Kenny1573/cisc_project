<template xmlns:background-color="http://www.w3.org/1999/xhtml">
  <div>
<a-card>
  <a-row :gutter="10">
    <a-col :md="8" :sm="24">
      <a-card :bordered="false">

        <!-- 按钮操作区域 -->
        <a-row style="margin-left: 14px;">
            <a-button @click="handleEd" type="link ">项目选择</a-button>

          <a-button v-if="showadd" @click="handleAdd(2)" type="link" >添加节点</a-button>
          <a-button @click="gantt" type="link" >甘特图</a-button>{{projectName}}
        </a-row>
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <a-alert type="info" :showIcon="true">
            <div slot="message">
              当前选择：<span v-if="this.currSelected.title">{{ getCurrSelectedTitle() }}</span>
              <a v-if="this.currSelected.title" style="margin-left: 10px" @click="onClearSelected">取消选择</a>
            </div>
          </a-alert>
          <!-- 树-->
          <a-col :md="10" :sm="24">
            <template>
              <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
               <span style="user-select: none">
            <a-tree
              @select="onSelect"
              @rightClick="rightHandle"
              :treeData="planTree"
              :expandedKeys="iExpandedKeys"
              :autoExpandParent="autoExpandParent"
              @expand="onExpand"/>
                </span>
                <!--新增右键点击事件,和增加添加和删除功能-->
                <a-menu slot="overlay">
                  <a-menu-item @click="handleAdd(2)" key="1">添加</a-menu-item>
                  <a-menu-item @click="handleDelete" key="2">删除</a-menu-item>
                </a-menu>
              </a-dropdown>
            </template>
          </a-col>
        </div>
      </a-card>

      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
    </a-col>
    <a-col :md="16" :sm="24">
      <a-tabs defaultActiveKey="1">
          <a-tab-pane tab="基本信息" key="1" >
          <a-card :bordered="false" v-if="selectedKeys.length>0&&this.show">
            <a slot="extra" @click="update">编辑</a>

            <a-form :form="form">
              <a-form-item label="节点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['spNodeNumber']" placeholder="请输入节点编号" :disabled="disabledd"></a-input>
              </a-form-item>
              <a-form-item label="节点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['spNodeName']" placeholder="请输入节点名称" :disabled="disabledd"></a-input>
              </a-form-item>
              <a-form-item label="节点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list"  v-decorator="['spNodeState']" :trigger-change="true" dictCode="scheduleplan_status" placeholder="请输入节点状态"  :disabled="disabledd" />
              </a-form-item>
              <a-form-item label="计划开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择计划开始时间" v-decorator="['spNodeBeginTime']" :trigger-change="true"  :disabled="disabledd" style="width: 100%"/>
              </a-form-item>
              <a-form-item label="计划完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择计划完成时间" v-decorator="['spNodeEndTime']" :trigger-change="true" :disabled="disabledd" style="width: 100%"/>
              </a-form-item>
              <a-form-item label="实际开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择实际开始时间" v-decorator="['spNodeInFactBegin']" :trigger-change="true"  :disabled="disabledd" style="width: 100%"/>
              </a-form-item>
              <a-form-item label="实际完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择实际完成时间" v-decorator="['spNodeInFactEnd']" :trigger-change="true" :disabled="disabledd" style="width: 100%"/>
              </a-form-item>
              <a-form-item label="责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['spNodePerson']" placeholder="请输入责任人" :disabled="disabledd" style="width: 100%"/>
              </a-form-item>
              <a-form-item label="上传附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-upload v-decorator="['spNodeAttachment']" :disabled="disabledd" :trigger-change="true"></j-upload>
              </a-form-item>
              <a-form-item label="是否里程碑节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-switch checked-children="是" un-checked-children="否" :disabled="disabledd"  :checked="checked" @click="switchChange"></a-switch>
              </a-form-item>
            </a-form>
            <div class="anty-form-btn">
              <a-button v-if="!disabledd" @click="emptyCurrForm" type="default" htmlType="button" icon="sync">重置</a-button>
              <a-button v-if="!disabledd" @click="submitCurrForm" type="primary" htmlType="button" icon="form">保存</a-button>
            </div>
          </a-card>
          <a-card v-else >
            <a-empty>
              <span slot="description"> 请先选择一个计划节点! </span>
            </a-empty>
          </a-card>
        </a-tab-pane>

      </a-tabs>

    </a-col>


    <schedulePlan-modal ref="planModal" @ok="refresh" :parentId="this.currSelected.key" :prjId="this.info.id"></schedulePlan-modal>
  </a-row>
  <schedule-plan-gantt-list ref="gantt"  :prjId="this.info.id"></schedule-plan-gantt-list>
    <project-select ref="modalForma" @ok="modalFormOk" @selectProject="selectProject"  @prjName="prjName"></project-select>
</a-card>
  </div>
</template>
<script>
  import SchedulePlanModal from './modules/SchedulePlanModal'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import pick from 'lodash.pick'
  import {httpAction, deleteAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { getAction } from '@api/manage'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import SchedulePlanGanttList from  './SchedulePlanGanttList'
  import ProjectSelect from '@views/yearPlanDetail/modules/ProjectSelect'

  export default {
    name: 'SchedulePlanMenu',
    mixins: [JeecgListMixin],
    components: {
      SchedulePlanModal,JUpload,JDate,JDictSelectTag,SchedulePlanGanttList,ProjectSelect

    },

    data() {
      return {
        showadd:false,
        projectName:'',
        disabledd:true,
        disableMixinCreated:true,
        info:{},
        iExpandedKeys: [],
        loading: false,
        autoExpandParent: true,
        disable: true,
        treeData: [],
        visible: false,
        planTree: [],
        rightClickSelectedKey: '',
        rightClickSelectedOrgCode: '',
        hiding: true,
        model: {},
        dropTrigger: '',
        depart: {},
        disableSubmit: false,
        checkedKeys: [],
        selectedKeys: [],
        autoIncr: 1,
        currSelected: {},
        checked: false,
        allTreeKeys:[],
        checkStrictly: true,

        form: this.$form.createForm(this),
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        graphDatasource: {
          nodes: [],
          edges: []
        },
        validatorRules: {

        },
        url: {
          listProject:"/project/project/listProject",

          getSchedulePlanById:'/scheduleplan/schedulePlan/getSchedulePlanById',
          list:'/scheduleplan/schedulePlan/treeList',
          delete: '/scheduleplan/schedulePlan/delete',
          edit: "/scheduleplan/schedulePlan/edit",
        },
        orgCategoryDisabled:false,
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
mounted () {
  this.$refs.modalForma.visible = true;

},
    methods: {
      selectProject(value){
        this.prjName(value.prjName)

        this.info = value
        this.refresh();
        this.$refs.gantt.init(this.info.id);
      },
      prjName(value){
        this.projectName=value
      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      modalFormOk() {
      },
      gantt(){
        this.$refs.gantt.visible=true;
      },
      update(){
        this.disabledd=false
      },
      loadTree() {
        var that = this
        that.treeData = []
        that.planTree = []
        getAction(this.url.list,{prjId:this.info.id}).then((res) => {
          if (res.success) {
            //部门全选后，再添加部门，选中数量增多
            this.allTreeKeys = [];
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i]
              that.treeData.push(temp)
              that.planTree.push(temp)
              that.setThisExpandedKeys(temp)
              that.getAllKeys(temp);
              // console.log(temp.id)
            }
            this.loading = false
          }
        })
      },
      switchChange(checked){

        console.log("_______________"+checked)
        this.checked=checked
        this.model.spIsMilestone=checked

      },
      getBool(value){
        if(value=='true'){
          return true

        }else{
          return false
        }

      },
      setThisExpandedKeys(node) {
        if (node.children && node.children.length > 0) {
          this.iExpandedKeys.push(node.key)
          for (let a = 0; a < node.children.length; a++) {
            this.setThisExpandedKeys(node.children[a])
          }
        }
      },
      refresh() {
        this.loading = true
        this.loadTree()
        this.disabledd=this.getBool('true')

      },
      // 右键操作方法
      rightHandle(node) {
        this.dropTrigger = 'contextmenu'
        console.log(node.node.eventKey)
        this.rightClickSelectedKey = node.node.eventKey
        this.rightClickSelectedOrgCode = node.node.dataRef.orgCode
      },
      onExpand(expandedKeys) {
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },

      // 右键点击下拉框改变事件
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },




      onSelect(selectedKeys, e) {

        console.log('selected', e )

        this.hiding = false
        let record = e.node.dataRef
        if(record.spNodeParent==1||record.spNodeParent==2||record.spNodeParent==3){
          this.showadd=false
        }else{
          this.showadd=true
        }
        console.log('onSelect-record',JSON.stringify(record) )
        let result={}
        getAction(this.url.getSchedulePlanById,{id:record.key}).then((res)=>{
          result=res.result


          if (result==null){
            this.show=false
          }else{
            this.show=true
          }
          this.currSelected = Object.assign({}, result)

          this.currSelected.key=record.key
          this.model = this.currSelected
          this.checked=this.getBool(this.model.spIsMilestone)
          this.selectedKeys = [record.key]
          this.model.parentId = record.parentId
          this.setValuesToForm(result)
          this.disabledd=this.getBool('true')

        })


      },
      // 触发onSelect事件时,为部门树右侧的form表单赋值
      setValuesToForm(record) {
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(record, 'id','spNodeNumber','spNodeName', 'spNodeState', 'spNodeBeginTime', 'spNodeEndTime', 'spNodePerson', 'spNodeAttachment', 'spIsMilestone','spNodeInFactBegin','spNodeInFactEnd'))
        })
        this.checked=this.getBool(this.model.spIsMilestone)
      },
      getCurrSelectedTitle() {
        return !this.currSelected.title ? '' : this.currSelected.title
      },

      submitCurrForm() {
        this.form.validateFields((err, values) => {
          if (!err) {
            if (!this.currSelected.id) {
              this.$message.warning('请点击选择要修改的节点!')
              return
            }

            let formData = Object.assign(this.model, values)
            console.log('Received values of form: ', formData)
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                this.$message.success('保存成功!')
                this.loadTree()
                this.disabledd=this.getBool('true')
              } else {
                this.$message.error(res.message)
              }
            })
          }
        })

      },
      emptyCurrForm() {
        let result={}
        getAction(this.url.getSchedulePlanById,{id:this.model.id}).then((res)=>{
          result=res.result
          console.log(JSON.stringify(result))
          this.currSelected = Object.assign({}, result)

          this.currSelected.key=this.model.id
          this.model = this.currSelected
          this.checked=this.getBool(this.model.spIsMilestone)
          this.selectedKeys = [this.model.id]
          this.setValuesToForm(result)
        })
        this.disabledd=this.getBool('true')
      },


      handleAdd(num) {
        if (num == 1) {
          this.$refs.planModal.add()
          this.$refs.planModal.title = '新增'
        } else if (num == 2) {
          let key = this.currSelected.key
          if (!key) {
            this.$message.warning('请先点击选中上级节点！')
            return false
          }else if( this.currSelected.key!='1'&&this.currSelected.key!='2'&&this.currSelected.key!='3'){
            this.$message.warning('不能再添加下级节点啦！')
            return false
          }
          this.$refs.planModal.add(this.selectedKeys)
          this.$refs.planModal.title = '新增'
        } else {
          this.$refs.planModal.add(this.rightClickSelectedKey)
          this.$refs.planModal.title = '新增'
        }

      },
      handleDelete() {
        var that = this
        this.$confirm({
          title: '确认删除',
          content: '确定要删除此节点数据吗?',
          onOk: function () {
            getAction(that.url.delete,{id:that.rightClickSelectedKey}).then((res)=>{
              this.refresh();


            })


          }
        })
      },

      getFlowGraphData(node) {
        this.graphDatasource.nodes.push({
          id: node.id,
          text: node.flowNodeName
        })
        if (node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            let temp = node.children[a]
            this.graphDatasource.edges.push({
              source: node.id,
              target: temp.id
            })
            this.getFlowGraphData(temp)
          }
        }
      },

      getAllKeys(node) {
        // console.log('node',node);
        this.allTreeKeys.push(node.key)
        if (node.children && node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            this.getAllKeys(node.children[a])
          }
        }
      }

    },


  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin: 15px;
  }

  .anty-form-btn {
    width: 100%;
    text-align: center;
  }

  .anty-form-btn button {
    margin: 0 5px;
  }

  .anty-node-layout .ant-layout-header {
    padding-right: 0
  }

  .header {
    padding: 0 8px;
  }

  .header button {
    margin: 0 3px
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  #app .desktop {
    height: auto !important;
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .drawer-bootom-button {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: left;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>