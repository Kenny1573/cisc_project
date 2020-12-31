<template>
    <a-card title="头盔信息" :bordered="false">
      <a-tag color="pink" slot="extra"  @click="handleAdd()">添加安全帽</a-tag>

<!--      <a-tag color="red" slot="extra" >
      <a-popconfirm :title="warningMsg" @confirm="confirm">解绑</a-popconfirm></a-tag>-->

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

      <helmet-modal ref="modalForm" @ok="initData" :userId="userId"></helmet-modal>
    </a-card>


</template>

<script>
  import DetailList from '@/components/tools/DetailList'
  import HelmetModal from './modules/HelmetModal'
  const DetailListItem = DetailList.Item
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import store from '@/store'
  import { getAction } from '../../api/manage'
  import {Empty} from 'ant-design-vue'
  import { filterDictTextByCache } from '../../components/dict/JDictSelectUtil'

  export default {
        name: 'HelmetInfo',
      mixins:[JeecgListMixin, mixinDevice],

      components:{
          DetailList,
        DetailListItem,
        HelmetModal,store},
    created () {
      this.initData();

    },
    data(){
          return{

            simpleImage:Empty.PRESENTED_IMAGE_SIMPLE,
            warningMsg:'确认解绑吗',
            disableMixinCreated:true,
            userId:store.getters.userInfo.username,
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
            ],
            model:{},
            url:{
              getHelmetInfo:'/helmet/getHelmetInfo',
              unlinkHelmet:'/helmet/unlinkHelmet'
            }
          }
      },
      methods:{
        initData(){
          getAction(this.url.getHelmetInfo, {username:this.username}).then((res) => {
            this.model = Object.assign({}, res.result);
            console.log(JSON.stringify(this.model))
            if (this.model.deviceSerial!=null) {
              this.dataSource.push(this.model)
            }
          })
        },

        confirm(){
          getAction(this.url.unlinkHelmet, {username:this.username,deviceSerial:this.model.deviceSerial}).then((res)=>{
            this.dataSource=null;
            this.model.deviceSerial=null
            this.initData();

          })
        }
      }
    }
</script>

<style scoped>

</style>