<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="项目名称">
          <a-input
            placeholder="请输入项目名称"
            v-decorator="[
              'project.name',
              {rules: [{ required: true, message: '请输入项目名称', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="项目分类">
        <a-select placeholder="项目分类" v-decorator="[ 'project.class', {rules: [{ required: true, message: '请选择分类'}]} ]">
          <a-select-option value="市政">市政</a-select-option>
          <a-select-option value="交通">交通</a-select-option>
          <a-select-option value="房建">房建</a-select-option>
          <a-select-option value="景观绿化">景观绿化</a-select-option>
          <a-select-option value="其他">其他</a-select-option>
        </a-select>
          </a-form-item>
      </a-col>
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="项目编号">
          <a-input
            placeholder="请输入项目编号"
            v-decorator="[
              'project.no',
              {rules: [{ required: true, message: '请输入项目编号', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="建设阶段">
          <a-input
            placeholder="请输入建设阶段"
            v-decorator="[
              'project.stage',
              {rules: [{ required: true, message: '请选择建设阶段', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row>
<a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="建设规模">
          <a-input
            placeholder="请输入建设规模"
            v-decorator="[
              'project.scale',
              {rules: [{ required: true, message: '请输入建设规模', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>


      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="工程地址">
          <a-input
            placeholder="请输入工程地址"
            v-decorator="[
              'project.address',
              {rules: [{ required: true, message: '请输入工程地址', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
  <a-col :lg="4" :md="12" :sm="24">
    <a-form-item label="经纬度">
      <a-input
        placeholder="请选择经纬度"
        v-decorator="[
              'project.lonAndLat',
              {rules: [{ required: true, message: '请选择经纬度', whitespace: true}]}
            ]" />
    </a-form-item>
  </a-col>
        <a-col :lg="4" :md="12" :sm="24">
          <a-form-item label="工程参数">
            <a-input
              placeholder="请输入工程参数"
              v-decorator="[
              'project.param',
              {rules: [{ required: true, message: '请输入工程参数', whitespace: true}]}
            ]" />
          </a-form-item>
        </a-col>
        </a-row >
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="匡算">
          <a-input prefix="￥" suffix="万元"
            placeholder="请输入匡算金额"
            v-decorator="[
              'project.roughCalculation',
              {rules: [{ required: true, message: '请输入匡算金额', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>

      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="估算">
          <a-input prefix="￥" suffix="万元"
            placeholder="请输入估算金额"
            v-decorator="[
              'project.Estimate',
              {rules: [{ required: true, message: '请输入估算金额', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="概算">
          <a-input prefix="￥" suffix="万元"
            placeholder="请输入概算金额"
            v-decorator="[
              'project.budgetEstimate',
              {rules: [{ required: true, message: '请输入概算金额', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :lg="4" :md="12" :sm="24">
        <a-form-item label="决算">
          <a-input prefix="￥" suffix="万元"
            placeholder="请输入决算金额"
            v-decorator="[
              'project.finalAccounts',
              {rules: [{ required: true, message: '请输入决算金额', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
    </a-row >
    <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
    <a-col :lg="22" :md="12" :sm="24">
      <a-form-item label="建设内容">
        <a-textarea
          allow-clear
          :autoSize="{minRows:3,maxRows:5}"
          placeholder="请输入建设内容"
          v-decorator="[
              'project.content',
              {rules: [{ required: true, message: '请输入建设内容', whitespace: true}]}
            ]" />
      </a-form-item>
    </a-col>
    </a-row>
    <a-form-item v-if="showSubmit">
      <a-button htmlType="submit" >Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<script>
    export default {
        name: 'ProjectCreateBaseInfo'
    }
</script>

<style scoped>

</style>