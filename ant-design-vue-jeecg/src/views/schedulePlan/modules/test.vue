<template>
  <span>
    <div
      slot="reference"
      class="plan"
      :style="{
        'background-color': statusColor,
        'margin-top': 0.1 * cellHeight + 'px'
      }"
      @click="onClick"
    >
      <div class="runTime" style="font-size: 5px">
        <span>{{getFormat(this.item.start)}}至{{getFormat(this.item.end)}}</span>
      </div>

    </div>

  </span>
</template>

<script>
import dayjs from "dayjs";

const NOW_PLAN = "#D5F8EA";
const FUTHER_PLAN = "#BFF2FE";
const PAST_PLAN = "#f2e5f2";
export default {
  name: "Test",
  props: {
    data: Object,
    item: Object,
    currentTime: dayjs,
    updateTimeLines: Function,
    cellHeight: Number,
    startTimeOfRenderArea: Number
  },
  data() {
    return {
      dayjs: dayjs
    };
  },
  computed: {
    statusColor() {
      let { item, currentTime } = this;
      if(item.name=='计划时间'){
        return NOW_PLAN
      }else{
        return PAST_PLAN
      }

    },

  },
  methods: {
    getFormat(value){
      return dayjs(value).format("YYYY-MM-DD");

    },
    onClick() {
      this.updateTimeLines(this.item.start, this.item.end);
    }
  }
};
</script>

<style lang="scss" scoped>
.middle {
  flex: 1;
  text-align: center;
  padding-left: 5px;
}
.runTime {
  display: flex;
  flex-direction: column;
}
.plan {
  display: flex;
  align-items: center;
  box-sizing: border-box;
  height: 80%;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  color: #333333;
  padding-left: 5px;
  font-size: 0.8rem;
  // opacity: 0.8;
}

.detail {
  .header {
    text-align: center;
    font-size: 1rem;
  }
}

.detail ul {
  list-style: none;
  padding: 0px;
  li {
    span {
      display: inline-block;
      width: 80px;
      color: #777;
      font-size: 0.8rem;
    }
    span:first-child {
      text-align: right;
    }

    span:last-child {
    }
  }
}
</style>
