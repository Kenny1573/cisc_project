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
    name: 'JSelectMultiXs',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        url: { list: '/jcxs/jcxs/list' },
        columns: [
          { title: '姓名', align: 'center', width: '25%', widthRight: '70%', dataIndex: 'xm' },
          { title: '性别', align: 'center', width: '10%', dataIndex: 'xbm_dictText' },
          { title: '出生日期', align: 'center', width: '20%', dataIndex: 'csrq' },
          { title: '照片', align: 'center', width: '45%', dataIndex: 'zp',scopedSlots: {customRender: 'imgSlot'} }
        ],
        // 定义在这里的参数都是可以在外部传递覆盖的，可以更灵活的定制化使用的组件
        default: {
          name: '学生列表',
          width: 1200,
          //returnId:true,
          displayKey: 'xm',
          returnKeys: ['id', 'xstmpid'],
          queryParamText: '姓名',
          //queryParam:jzid="-1";
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