<template>
    <a-row span="24">
      <a-col style="float:left;" span="3" ><a-tag v-if=" data.status" color="red"> {{ getText(data.status)  }}</a-tag></a-col>
      <a-col style="float:left;" span="11" ><a-tag v-if="data.node" color="pink"> {{ data.node }}</a-tag></a-col>
      <a-col style="float:left;" span="2"><a-tag v-if="data.name" color="green"> {{ data.name }}</a-tag></a-col>

      <a-col style="float:left;" span="4"><a-tag v-if="getFormat(data.gtArray[0].start)" color="cyan"> {{ getFormat(data.gtArray[0].start) }}</a-tag></a-col>
      <a-col style="float:left;" span="4"><a-tag v-if="getFormat(data.gtArray[0].end)" color="purple"> {{ getFormat(data.gtArray[0].end) }}</a-tag></a-col>
    </a-row>
</template>

<script>
import dayjs from 'dayjs'
import {filterDictTextByCache} from '@/components/dict/JDictSelectUtil'

export default {
  name: "PlanLeft",
  props: {
    data: Object
  },
  methods:{
    getFormat(value){
        return dayjs(value).format("YYYY-MM-DD");

    },

    getText(text){
      return filterDictTextByCache('scheduleplan_status',text)
    }

  }
};
</script>

<style scoped>
.name {
  display: flex;
  box-sizing: border-box;
  overflow: hidden;
  height: 100%;
  width: 100%;
  padding: 0 5px 0 0;
  border-radius: 8px 0 0 8px;
  align-items: center;
}
.colorBar {
  width: 10px;
  height: 100%;
}

.carId {
  flex: 1;
}

.type {
  padding: 0 5px 0 0;
  font-size: 1.2rem;
}
</style>
