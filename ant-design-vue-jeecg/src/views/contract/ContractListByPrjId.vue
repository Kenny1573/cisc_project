<template>
  <a-card :title="this.info.prjName" style="width: 100%" :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="合同名称">
              <j-input placeholder="请输入合同名称" v-model="queryParam.conName"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="合同编码">
              <j-input placeholder="请输入合同编码" v-model="queryParam.conNumber"></j-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="甲方">


                <j-search-select-tag placeholder="请输入甲方"   v-model="queryParam.conPartyA" dict="pjplat.BS_COMPANY,COM_NAME,COM_NAME,DEL_FLAG=0" :async="true"/>

              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="乙方">
                <j-search-select-tag placeholder="请输入乙方" v-model="queryParam.conPartyB" dict="pjplat.BS_COMPANY,COM_NAME,COM_NAME,DEL_FLAG=0" :async="true"/>

              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="丙方">
                <j-search-select-tag placeholder="请输入丙方" v-model="queryParam.conPartyC" dict="pjplat.BS_COMPANY,COM_NAME,COM_NAME,DEL_FLAG=0" :async="true"/>
              </a-form-item>
            </a-col>

            <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="签订日期">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.conSignDate_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.conSignDate_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-has="'contract:opt'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button v-has="'contract:opt'" type="primary" icon="download" @click="handleExportXls('合同信息')">导出</a-button>
      <a-upload v-has="'contract:opt'" name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-has="'contract:opt'" v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px" > 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div style="width: 100%">
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
          <a-tag color="pink" v-has="'contract:opt'" @click="handleEdit(record)">编辑</a-tag>

          <a-dropdown v-has="'contract:opt'">
            <a-tag color="red" class="ant-dropdown-link">更多 <a-icon type="down" /></a-tag>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>


        <template slot="conName" slot-scope="record">


          <a-popover title="文本内容" trigger="hover" placement="leftTop">

            <template slot="content">
              <div style="width: 200px">
                {{record}}</div>

            </template>
            {{record}}

          </a-popover>

        </template>
        <template slot="comName" slot-scope="record">


          <a-popover title="公司名称" trigger="hover" placement="leftTop">

            <template slot="content">
              <div style="width: 200px">
                {{record}}</div>

            </template>
            {{record}}

          </a-popover>

        </template>
      </a-table>
    </div>

    <contract-modal ref="modalForm" @ok="modalFormOk" :conPrjId="this.info.id"></contract-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractModal from './modules/ContractModal'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JInput from '../../components/jeecg/JInput'
import Vue from 'vue'
  import {colAuthFilter} from "@/utils/authFilter"

  export default {
    name: "ContractListByPrjId",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDate,
      ContractModal,
      JInput
    },
    data () {
      return {
        info:{},
        disableMixinCreated:true,
        description: '合同信息管理页面',
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
            title:'合同名称',scopedSlots:{customRender:'conName'},
            align:"center",
            width:200,
            ellipsis:true,

            dataIndex: 'conName'
          },
          {
            title:'项目名称',scopedSlots:{customRender:'conName'},
            align:"center",
            width:200,
            ellipsis:true,

            dataIndex: 'prjName'
          },
          {
            title:'合同编码',scopedSlots:{customRender:'conName'},
            align:"center",
            width:100,
            ellipsis:true,
            dataIndex: 'conNumber'
          },
          {
            title:'建设单位(甲方)',scopedSlots:{customRender:'comName'},
            align:"center",
            width:250,
            ellipsis:true,

            dataIndex: 'conPartyA'
          },
          {
            title:'承包人(乙方)',scopedSlots:{customRender:'comName'},
            align:"center",
            width:250,
            ellipsis:true,

            dataIndex: 'conPartyB'
          },
          {
            title:'第三方(丙方)',scopedSlots:{customRender:'comName'},
            align:"center",
            width:250,
            ellipsis:true,

            dataIndex: 'conPartyC'
          },
          {
            title:'合同金额(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            width:200,
            ellipsis:true,


            dataIndex: 'conMoney'
          },
          {
            title:'合同签订日期',
            align:"center",
            width:200,
            ellipsis:true,

            dataIndex: 'conSignDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'履行开始日期',
            align:"center",
            dataIndex: 'conBeginDate',
            width:200,
            ellipsis:true,
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'履行结束日期',
            align:"center",
            dataIndex: 'conEndDate',
            width:200,
            ellipsis:true,
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'合同附件',
            align:"center",
            width:200,
            ellipsis:true,
            dataIndex: 'conAttachment',
            scopedSlots: {customRender: 'fileSlot'}
          },
          {
            title:'清单附件',
            align:"center",
            width:200,
            ellipsis:true,
            dataIndex: 'conDetailList',
            scopedSlots: {customRender: 'fileSlot'}
          },
          {

            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
            fixed:'right'
          }
        ],
        url: {
          list: "/contract/contract/list",
          delete: "/contract/contract/delete",
          deleteBatch: "/contract/contract/deleteBatch",
          exportXlsUrl: "/contract/contract/exportXls",
          importExcelUrl: "contract/contract/importExcel",
        },
        dictOptions:{},
      }
    },
    created () {
      this.disableMixinCreated=true
      this.columns=colAuthFilter(this.columns,"contract:")
      this.info=Vue.ls.get('projectInfo')
      this.queryParam['conPrjId'] = this.info.id
      this.loadData(1);
      //初始化字典配置 在自己页面定义
      this.initDictConfig();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>