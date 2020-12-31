<template>
  <j-modal
    :title="title"
    :width="width+500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    okText="添加"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="单位名称">
                <j-input placeholder="请输入单位名称" v-model="queryParam.comName"></j-input>
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


      <!-- table区域-begin -->
      <div>

        <a-row>
          <a-col >
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="record.id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange,type:'radio'}"
              class="j-table-force-nowrap"
              @change="handleTableChange">
            </a-table>
          </a-col>

        </a-row>
      </div>

    </a-card>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction, postAction } from '../../../api/manage'
  import JInput from '../../../components/jeecg/JInput'



  export default {
    name: "CompanyAddModal",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JInput,
    },
    props:{
      mainId:{
      },
      type:{
      }
    },
    data () {
      return {
        description: '选择你想要添加的单位',
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
          }
        ],
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
          list: "/company/company/list",
          delete: "/company/company/delete",
          deleteBatch: "/company/company/deleteBatch",
          addBatch:"/project/project/addProjectCompanyBatch",
          exportXlsUrl: "/company/company/exportXls",
          importExcelUrl: "company/company/importExcel",
        },
        dictOptions:{},
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
          this.form.setFieldsValue(pick(this.model,'comName','comAddress','comCertificate','comTax','comBank','comContacts','comPhone'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      handleOk () {
        var companydata=[];
        if (this.selectedRowKeys.length <= 0) {
          return;
        } else {
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
             let notemp = this.selectedRowKeys[a] ;
            this.dataSource[notemp].comType=this.type
             companydata.push(this.dataSource[notemp])
          }
          this.$emit('addCompanyData',companydata)
          this.close()
        }
      },


      popupCallback(row){
        this.form.setFieldsValue(pick(row,'comName','comAddress','comCertificate','comTax','comBank','comContacts','comPhone'))
      },


    }
  }
</script>