<template>
  <a-card  :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->

    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :scroll="{y:250}"
        class="j-table-force-nowrap"
        @change="handleTableChange">



      </a-table>
    </div>

    <company-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId" :type="type"></company-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyModal from './modules/CompanyModal'
  import { deleteAction, postAction } from '../../api/manage'

  export default {
    name: "CompanyResultList",
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
          this.clearList()
          if(!this.mainId){

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
      }
    },
    data () {
      return {
        type:'',
        description: '单位信息管理页面',
        // 表头
        columns: [

          {

            title:'单位名称',
            align:"center",
            dataIndex: 'comName'
          }
        ],
        url: {
          list: "/project/project/getProjectCompanyInfoById",
          delete: "/project/project/deleteProjectCompanyInfoById",
          deleteBatch: "/project/project/deleteBatchProjectCompany",
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


      handleAdds(){
        this.$refs.modalForm.oldData=this.dataSource;
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;

      },
      handleDeleted: function (id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        deleteAction(this.url.delete, {id: id,prjId:this.mainId}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        });
      },
      batchDeld: function () {
        let prjId=this.mainId
        if(!this.url.deleteBatch){
          this.$message.error("请设置url.deleteBatch属性!")
          return
        }
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.dataSource[a].id + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              postAction(that.url.deleteBatch, {ids:ids,prjId:prjId}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
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