<template>
  <div>

    <a-card class="project-base-info" title="添加项目基本信息" :bordered="false">
      <project-base-info ref="repository" :showSubmit="false" />
    </a-card>
    <br/>
    <a-card class="project-manage-person" title="添加项目管理人员" :bordered="false">
      <project-manage-person ref="task" :showSubmit="false" />
    </a-card>
    <br/>
    <!-- table -->
      <a-card title="单位信息"  :bordered="false" >

        <a-button @click="handleAdd" slot="extra" type="link">新增</a-button>

      </a-card>
<br/>
      <a-card title="年度计划"  :bordered="false" >
        <a-button @click="handleAdd" slot="extra" type="link">新增</a-button>

        <year-plan-modal ref="modalForm" @ok="modalFormOk"></year-plan-modal>
      </a-card>

    <!-- fixed footer toolbar -->
    <footer-tool-bar>
      <a-button type="primary" @click="validate" :loading="loading">提交</a-button>
    </footer-tool-bar>
  </div>

</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import projectBaseInfo from '../project/form/ProjectCreateBaseInfo'
  import projectManagePerson from '../project/form/ProjectManagePerson'
  import companyModal from '../project/form/CompanyModal'
  import yearPlanModal from '../project/form/YearPlanModal'
    export default {
        name: 'ProjectCreate',
      mixins:[JeecgListMixin],
      components: {
        projectBaseInfo,
        projectManagePerson,
        companyModal,
        yearPlanModal
      },
      data () {
        return {}
      },
      methods: {}
    }
</script>

<style scoped>

</style>