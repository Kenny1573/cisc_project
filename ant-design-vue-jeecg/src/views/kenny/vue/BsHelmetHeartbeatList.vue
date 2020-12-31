<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('BS_HELMET_HEARTBEAT')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

        <template slot="batteryStatus" slot-scope="record">
          <a-progress type="circle" :width="35" :percent='record'/>
        </template>

      </a-table>
    </div>

    <bsHelmetHeartbeat-modal ref="modalForm" @ok="modalFormOk"></bsHelmetHeartbeat-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BsHelmetHeartbeatModal from './modules/BsHelmetHeartbeatModal'

  export default {
    name: "BsHelmetHeartbeatList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BsHelmetHeartbeatModal
    },
    data () {
      return {
        description: 'BS_HELMET_HEARTBEAT管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'设备序列号',
            align:"center",
            dataIndex: 'deviceSerial'
          },
          {
            title:'通道号',
            align:"center",
            dataIndex: 'channelNo'
          },
          {
            title:'消息id',
            align:"center",
            dataIndex: 'messageId'
          },
          {
            title:'消息时间',
            align:"center",
            dataIndex: 'messageTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'脱戴帽状态',
            align:"center",
            dataIndex: 'helmetStatus'
          },
          {
            title:'蓝牙beaconID',
            align:"center",
            dataIndex: 'beaconId'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'longitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'电池电量',
            align:"center",
            scopedSlots:{customRender:'batteryStatus'},
            dataIndex: 'powerPercent',
          },
          {
            title:'上下班',
            align:"center",
            dataIndex: 'workStatus'
          },
          {
            title:'GPS信号强度',
            align:"center",
            dataIndex: 'rssiSignalStrength'
          },
          {
            title:'静止警告',
            align:"center",
            dataIndex: 'isAbnormalStationary'
          },
          {
            title:'SOS状态',
            align:"center",
            dataIndex: 'isSos'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/kenny/bsHelmetHeartbeat/list",
          delete: "/kenny/bsHelmetHeartbeat/delete",
          deleteBatch: "/kenny/bsHelmetHeartbeat/deleteBatch",
          exportXlsUrl: "/kenny/bsHelmetHeartbeat/exportXls",
          importExcelUrl: "kenny/bsHelmetHeartbeat/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      }
    },
    created(){

    },
    updated(){

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>