<template>
  <!-- 定义在这里的参数都是不可在外部覆盖的，防止出现问题 -->
  <j-select-biz-component
    :value="value"
    :ellipsisLength="25"
    :listUrl="url.list"
    :columns="columns"
    v-on="$listeners"
    v-bind="attrs"
  />
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectMultiKc',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        url: { list: '/jxkc/jxKc/list' },
        columns: [
          { title: '课程名称', align: 'center', width: '40%', widthRight: '70%', dataIndex: 'kcmc' },
          { title: '课程码', align: 'center', width: '20%', dataIndex: 'kcm' },
          { title: '总学时', align: 'center', width: '20%', dataIndex: 'zxs' },
          { title: '周学时', align: 'center', width: '20%', dataIndex: 'zhxs' }
        ],
        // 定义在这里的参数都是可以在外部传递覆盖的，可以更灵活的定制化使用的组件
        default: {
          name: '课程列表',
          width: 1200,
          //returnId:true,
          displayKey: 'kcmc',
          returnKeys: ['id', 'kctmpid'],
          queryParamText: '课程名称',
        }
      }
    },
    computed: {
      attrs() {
        //console.log(this.$attrs)
        //console.log(this.default)
        //console.log(Object.assign(this.default, this.$attrs))
        return Object.assign(this.default, this.$attrs)
      }
    }
  }
</script>

<style lang="less" scoped></style>