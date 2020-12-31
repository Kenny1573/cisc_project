<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="单位名称">
              <j-input placeholder="请输入单位名称" v-model="queryParam.comName" maxLength="10"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="单位地址">
              <j-input placeholder="请输入单位地址" v-model="queryParam.comAddress"></j-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="法定代表人">
                <j-input placeholder="请输入法定代表人" v-model="queryParam.comLegalPerson"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="社会统一信用代码">
                <j-input placeholder="请输入社会统一信用代码" v-model="queryParam.comSocialCode"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="单位资质信息">
                <j-input placeholder="请输入单位资质信息" v-model="queryParam.comCertificate"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="单位银行信息">
                <j-input placeholder="请输入单位银行信息" v-model="queryParam.comBank"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="单位联系人">
                <j-input placeholder="请输入单位联系人" v-model="queryParam.comContacts"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="单位联系电话">
                <j-input placeholder="请输入单位联系电话" v-model="queryParam.comPhone"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="经营范围">
                <j-input placeholder="请输入经营范围" v-model="queryParam.comBussinessScope"></j-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('单位信息')">导出</a-button>
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
          <a-tag color="blue" @click="handleEdit(record)">编辑</a-tag>


                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a-tag color="red">删除</a-tag>
                </a-popconfirm>

        </span>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>
      </a-table>
    </div>

    <company-modal ref="modalForm" @ok="modalFormOk"></company-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyModal from './modules/CompanyModal'
  import JInput from '../../components/jeecg/JInput'


  export default {
    name: "CompanyList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyModal,
      JInput
    },
    data () {
      return {
        description: '单位信息管理页面',
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
            title:'单位名称',
            align:"center",
            dataIndex: 'comName'
          },
          {
            title:'单位地址',
            align:"center",
            dataIndex: 'comAddress'
          },
          {
            title:'法定代表人',
            align:"center",
            dataIndex: 'comLegalPerson'
          },
          {
            title:'社会统一信用代码',
            align:"center",
            dataIndex: 'comSocialCode'
          },
          {
            title:'单位资质信息',
            align:"center",
            dataIndex: 'comCertificate'
          },
          {
            title:'单位银行信息',
            align:"center",
            dataIndex: 'comBank'
          },
          {
            title:'单位联系人',
            align:"center",
            dataIndex: 'comContacts'
          },
          {
            title:'单位联系电话',
            align:"center",
            dataIndex: 'comPhone'
          },
          {
            title:'经营范围',
            align:"center",
            dataIndex: 'comBussinessScope'
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
          list: "/company/company/list",
          delete: "/company/company/delete",
          deleteBatch: "/company/company/deleteBatch",
          exportXlsUrl: "/company/company/exportXls",
          importExcelUrl: "company/company/importExcel",
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
      formatMoney(num){

        return parseFloat(num.toString()).toLocaleString()

      },
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>