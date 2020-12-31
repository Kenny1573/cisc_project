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
    <div class="table-operator" >
      <a-button @click="handleAdd" type="primary" icon="plus">选择</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>


      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="cid"
        :columns="columns"
        :dataSource="this.companydata"
        :pagination="false"
        :loading="loading"
        :scroll="{y:500}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">

                <a-popconfirm title="确定删除吗?" @confirm="() => deleteRecord(record)">
                  <a>删除</a>
                </a-popconfirm>
        </span>

      </a-table>

    </div>

    <company-modal  @addCompanyData="addCompanyData" ref="modalForm" @ok="modalFormOk" :mainId="mainId" :type="type"></company-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyModal from './modules/CompanyAddModal'
  import { deleteAction } from '../../api/manage'

  export default {
    name: "CompanyAddList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyModal
    },
    props:{
      mainId:{
      },

      title:{
      },
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['cid'] = val

          }
        },
      }
    },
    created () {
      if (this.title=='施工单位'){
        this.type=1
        this.queryParam['type']=1
      }else if(this.title=='监管单位'){
        this.type=2
        this.queryParam['type']=2
      }
    },
    data () {
      return {
        type:'',
        companydata:[],
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
          }
        ],
        url: {
          list: "/project/project/getProjectCompanyInfoById",
          delete: "/project/project/deleteProjectCompanyInfoById",
          deleteBatch: "/company/company/deleteBatch",
          exportXlsUrl: "/company/company/exportXls",
          importExcelUrl: "company/company/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.mainId}`;
      }
    },
    methods: {
     deleteRecord(record){
        for(let i=0;i<this.companydata.length;i++){
          if (record==this.companydata[i])
            this.companydata.splice(i,1);
        }
      },
      addCompanyData(e) {
        this.companydata=e
      },
      handler1(val) {
        if(!this.mainId){
          this.clearList()
        }else{
          this.queryParam['cid'] = val
          if (this.title=='施工单位'){
            this.type=1
            this.queryParam['type']=1
          }else if(this.title=='监管单位'){
            this.type=2
            this.queryParam['type']=2
          }
        }
      },
      handleDeleted: function (id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        deleteAction(this.url.delete, {id: id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        });
      },
      getData(){
        return this.companydata
      },
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>