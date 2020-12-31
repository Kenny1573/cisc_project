<template>
  <a-card title="安全帽管理" :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备序列号">
              <j-input placeholder="请输入设备序列号" v-model="queryParam.deviceSerial"></j-input>
            </a-form-item>
          </a-col>


          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="query" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>

            </span>

          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>


    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">


        <span slot="action" slot-scope="text, record">
          <a-tag color="pink" @click="onSearchDepUser(record)">分配</a-tag>
          <a-tag color="blue" @click="handleEditd(record)">配置参数</a-tag>
      <a-tag color="green">
      <a-popconfirm title="确定与该使用者解除绑定关系吗?" @confirm="() => unlink(record)">解绑</a-popconfirm></a-tag>
            <a-tag color="red">
                <a-popconfirm title="确定删除吗?" @confirm="() => deleteHelmet(record)">
                  <a>删除</a>
                </a-popconfirm>
            </a-tag>


        </span>

        <template slot="batteryStatus" slot-scope="record">

          <a-progress type="circle" :width="35" :percent="record"/>


        </template>

      </a-table>
    </div>

<helmet-modal ref="modalForm" @ok="modalFormOk" ></helmet-modal>
    <helmet-config-modal ref="modalFormd" @ok="modalFormOk" ></helmet-config-modal>
    <j-select-user-by-dep-radio-modal ref="selectModal" :modal-width="modalWidth" :multi="multi" @ok="selectOK" :user-ids="username" @initComp="initComp"></j-select-user-by-dep-radio-modal>
  </a-card>
</template>

<script>
  import JInput from '@comp/jeecg/JInput'

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HelmetModal from './modules/HelmetModal'
  import HelmetConfigModal from './modules/HelmetConfigModal'

  import { filterDictTextByCache } from '../../components/dict/JDictSelectUtil'
  import { getAction } from '../../api/manage'
  import store from '@/store'
  import JSelectUserByDepRadioModal from '../componments/JSelectUserByDepRadioModal'


  export default {
    name: 'HelmetList',
    mixins: [JeecgListMixin, mixinDevice],
    components: { HelmetModal, JInput,store ,JSelectUserByDepRadioModal,HelmetConfigModal},
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    data () {
      return {
        url: {
          list: '/helmet/getList',
          importExcelUrl: '/helmet/importExcel',
          unlinkHelmet:'/helmet/unlinkHelmet',
          linkHelmet:'/helmet/linkHelmet',
          delete:'/helmet/dismissHelmet',
          likeList:'/helmet/getLikeList'
        },
        selectedDeviceSerial:'',
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function (t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '设备序列号',
            align: 'center',
            dataIndex: 'deviceSerial'
          },

          {
            title: '是否启用',
            align: 'center',
            dataIndex: 'status',
            customRender: (text) => {
              return filterDictTextByCache('online_status', text)
            }
          },

          {
            title: '脱戴帽状态',
            align: 'center',
            dataIndex: 'wearStatus',
            customRender: (text) => {
              return filterDictTextByCache('helmet_status', text)
            }
          },
          {
            title: '电池电量',scopedSlots:{customRender:'batteryStatus'},
            align: 'center',
            dataIndex: 'batteryStatus'
          },
          {
            title: '使用者 ',
            align: 'center',
            dataIndex: 'owner'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            // fixed:"right",
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        modalWidth:1500,
        multi:false,
        userNames:'',
        username : ''
      }
    },
    methods: {
      query(arg){
        if(!this.url.likeList){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        console.log("查询条件"+JSON.stringify(params))
        this.loading = true;
        getAction(this.url.likeList, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            console.log("查询结果"+JSON.stringify(this.dataSource))
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      unlink(val){

        getAction(this.url.unlinkHelmet, {username:val.username,deviceSerial:val.deviceSerial}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.loadData();
          }else{
            this.$message.warning(res.message);
          }
          this.loadData();

        })

      },

      onSearchDepUser(record) {
        this.selectedDeviceSerial=record.deviceSerial
        this.$refs.selectModal.showModal()
      },

      handleEditd: function (record) {
        this.$refs.modalFormd.edit(record);
        this.$refs.modalFormd.title = "编辑";
        this.$refs.modalFormd.disableSubmit = false;
      },
      selectOK(rows, idstr) {



        let that = this
        if (rows) {
          getAction(this.url.linkHelmet, {username:rows[0].username,deviceSerial:this.selectedDeviceSerial}).then((res)=>{

              if(res.success){
                that.$message.success(res.message);
              }else{
                that.$message.warning(res.message);
              }
            this.loadData();
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
              this.selectedDeviceSerial=''
            })

        }
      },
      initComp(userNames) {
        this.userNames = userNames
      },
      deleteHelmet(record){
        let that=this
        getAction(this.url.delete,{deviceSerial:record.deviceSerial,username:record.username}).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
          }else{
            that.$message.warning(res.message);
          }
          this.loadData();
        }).finally(() => {
          that.confirmLoading = false;
          that.close();
          this.selectedDeviceSerial=''
        })
      }

    }

  }
</script>

<style scoped>

</style>