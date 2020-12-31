<template>
  <a-card :title="this.info.prjName" :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div  style="font-weight: 500;font-size: 20px;text-align: center">

        {{this.projectName}}


    </div>
    <div class="table-operator">
      <a-row>
      <a-col span="22"><a-button v-if="this.projectName" @click="handleAdd" type="link" >新增</a-button>


        <a-button v-if="selectedRowKeys.length > 0" style="margin-left: 5px" type="link" @click="batchDel"> 删除 <a-icon type="delete" /></a-button>
      </a-col ><a-col span="2">
      <a-button @click="handleEd" type="link ">项目选择</a-button>
      </a-col>
      </a-row>
    </div>

    <!-- table区域-begin -->
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
          <a-tag color="pink" @click="handleEdit(record)">编辑</a-tag>
          <a-dropdown>
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

        <template slot="mrDescription" slot-scope="record">

          <a-popover title="总体进展情况描述" trigger="hover" placement="leftTop">
            <template slot="content">
              <div style="width: 500px">
                {{record}}</div>
            </template>
            {{record}}
          </a-popover>
        </template>

        <template slot="mrNeedCoordinate" slot-scope="record">


          <a-popover title="需协调处理的问题" trigger="hover" placement="leftTop">

            <template slot="content">
              <div style="width: 500px">
                {{record}}</div>

            </template>
            {{record}}

          </a-popover>

        </template>
        <template slot="money" slot-scope="record">


          {{formatMoney(record)}}

        </template>

      </a-table>
    </div>
    <project-select ref="modalForma" @ok="modalFormOk" @selectProject="selectProject"  @prjName="prjName"></project-select>

    <month-report-modal-menu ref="modalForm" @ok="modalFormOk" :mainId="this.info.id"></month-report-modal-menu>
  </a-card>
</template>

<script>
  import ProjectSelect from '@views/yearPlanDetail/modules/ProjectSelect'

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MonthReportModalMenu from './modules/MonthReportModalMenu'
  import Vue from 'vue'
  import { getAction } from '../../api/manage'
  import { filterDictTextByCache, initDictOptions } from '../../components/dict/JDictSelectUtil'
  import {colAuthFilter} from "@/utils/authFilter"

  export default {
    name: "MonthReportListMenu",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      MonthReportModalMenu,ProjectSelect
    },
    created () {

      this.disableMixinCreated=true
      this.columns=colAuthFilter(this.columns,"monthreport:")
      //初始化字典配置 在自己页面定义
      this.initDictConfig();
      getAction(this.url.listProject).then((res) => {
        if (res.success) {
          this.project=res.result
        }

      })
    },

    mounted () {
      this.$refs.modalForma.visible = true;

    },
    data () {
      return {
        projectName:'',
        text:'项目选择',
        project:[],
        info:{},
        description: '进度月报管理页面',
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
/*          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },*/
          {
            title:'年份',
            align:"center",
            dataIndex: 'year',
            sorter:(a,b)=>a.year-b.year
          },
          {
            title:'月份',
            align:"center",
            dataIndex: 'month',
            customRender:(text)=>{
              return filterDictTextByCache('month' ,text)
            },
            sorter:(a,b)=>a.month-b.month
          },

          {
            title:'当月完成投资数(万元)', scopedSlots:{customRender:'money'},
            align:"center",
            dataIndex: 'mrFinishInvestment'
          },
/*          {
            title:'当月完成财务数(万元)',
            align:"center",
            dataIndex: 'mrFinishFinance'
          },*/
          {
            title:'总体进展情况描述',
            align:"center",
            dataIndex: 'mrDescription',
            scopedSlots:{customRender:'mrDescription'},
            ellipsis:true

          },
          {
            title:'需协调处理的问题',
            align:"center",
            dataIndex: 'mrNeedCoordinate',
            scopedSlots:{customRender:'mrNeedCoordinate'},

            ellipsis:true
          },
          {
            title:'是否显示在可视化',
            align:"center",
            dataIndex: 'mrNeedCoordinateShow',
            dictCode:'sure_or_not',

            customRender:(text)=>{
              return filterDictTextByCache('sure_or_not' ,text)
            },
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
          listProject:"/project/project/listProject",
          list: "/monthreport/monthReport/listByPrjId",
          delete: "/monthreport/monthReport/delete",
          deleteBatch: "/monthreport/monthReport/deleteBatch",
          exportXlsUrl: "/monthreport/monthReport/exportXls",
          importExcelUrl: "monthreport/monthReport/importExcel",
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
      selectProject(value){
        this.text=value.id
        this.prjName(value.prjName)
        this.info.id = value.id
        this.queryParam['prjId'] = this.info.id
        this.loadData(1);
        //初始化字典配置 在自己页面定义
        this.initDictConfig()
      },
      getBool(value){
        if(value=='true'){
          return true

        }else{
          return false
        }

      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
      prjName(value){
        this.projectName=value
      },
      modalFormOk() {
        this.queryParam['prjId'] = this.info.id
        this.loadData(1);
      },
      initDictConfig(){
        initDictOptions('sure_or_not').then((res)=>{

        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>