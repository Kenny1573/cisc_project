<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" span="24" type="flex" justify="space-around">
      <a-col span="12">
        <a-form-item label="前期业主代表">
            <a-col :span="22">
                <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
            </a-col>
        </a-form-item>
      </a-col>
      <a-col span="12">
        <a-form-item label="实施业主代表">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col span="12">
        <a-form-item label="总工程师办公室">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
      <a-col span="12">
        <a-form-item label="计划部">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col span="12">
        <a-form-item label="道桥部">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
      <a-col span="12">
        <a-form-item label="计划部">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col span="12">
        <a-form-item label="质安部">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
      <a-col span="12">
        <a-form-item label="财务部">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col span="12">
        <a-form-item label="跟踪审计(外)">
          <a-col :span="22">
            <j-select-user-by-dep v-model="userIds" :multi="true"></j-select-user-by-dep>
          </a-col>
        </a-form-item>
      </a-col>
      <a-col span="12"></a-col>
    </a-row>

    <a-form-item v-if="showSubmit">
      <a-button htmlType="submit" >Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<script>

  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import RepositoryForm from '../../form/advancedForm/RepositoryForm'
  import TaskForm from '../../form/advancedForm/TaskForm'
    export default {
        name: 'ProjectManagePerson',
      components: {
        JSelectUserByDep
      },
    }
</script>

<style scoped>

</style>