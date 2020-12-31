<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @cancel="handleCancel"
    cancelText="关闭"
>
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
    <v-gantt-chart
      :startTime="times[0]"
      :endTime="times[1]"
      :cellWidth="cellWidth"
      :cellHeight="cellHeight"
      :scale="scale"
      :datas="datas"
      showCurrentTime
      :scrollToPostion="positionA"
      :titleWidth="titleWidth"
      :titleHeight="titleHeight"
    >
      <template v-slot:block="{ data, item }">
        <test
          :data="data"
          :updateTimeLines="updateTimeLines"
          :cellHeight="cellHeight"
          :currentTime="currentTime"
          :item="item"

        ></test>


      </template>




      <template v-slot:left="{ data }">
            <plan-left :data="data" ></plan-left>

      </template>
      <template v-slot:title>
        <a-row span="24">
          <a-col style="float:left;" span="3" >节点进度</a-col>
          <a-col style="float:left;" span="11" >节点名称</a-col>
          <a-col style="float:left;" span="2">类别</a-col>

          <a-col style="float:left;" span="4">开始时间</a-col>
          <a-col style="float:left;" span="4">结束时间</a-col>
        </a-row>
      </template>
    </v-gantt-chart>


  </j-modal>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import dayjs from 'dayjs'
  import { getAction } from '../../api/manage'
  import PlanLeft from './modules/PlanLeft'
  import test from './modules/test'



  export default {
    name: "SchedulePlanGanttList",
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      PlanLeft,test
    },
    props:{
      prjId:{},
    },
    data () {
      return {
        disableMixinCreated:true,
        title:'节点计划甘特图',
        width:1600,
        visible:false,
        dataSource:[],
        times: [
          dayjs()
            .toString(),
          dayjs()
            .add(2, "day")
            .toString()
        ],
        scale: 1440*7 ,
        datas:[],
        scrollToTime: dayjs()
          .add(1, "day")
          .toString(),
        positionA: {},
        cellWidth: 100,
        cellHeight: 25,
        titleHeight: 30,
        titleWidth: 500,
        currentTime: dayjs(),
        timeLines: [
          {
            time: dayjs()
              .add(2, "hour")
              .toString()
          },
          {
            time: dayjs()
              .add(5, "hour")
              .toString(),
            color: "#747e80"
          }
        ],

      }
    },
    created () {

      this.init(this.prjId)
    },
    methods: {
      init(value){
        this.datas=[]
        getAction('/scheduleplan/schedulePlan/ganttList',{prjId:value}).then((res)=>{
          if(res.result.array!=null&&res.result.start!=null&&res.result.end!=null) {
            this.dataSource = res.result.array
            this.times[0] = dayjs(res.result.start).subtract(1, 'month').toString()
            this.times[1] = dayjs(res.result.end).add(1, 'month').toString()
            this.datas = res.result.array
          }


        })
      },
      updateTimeLines(timeA, timeB) {
        this.timeLines = [
          {
            time: timeA
          },
          {
            time: timeB,
            color: "#747e80"
          }
        ];
      },
      handleCancel () {
        this.close()
      },
      close(){
        this.visible=false
      }
    }
  }
</script>

<style>
  .container {
    height: calc(100% - 58px);
    display: flex;
    flex-direction: column;
    flex: 1;
  }
</style>

