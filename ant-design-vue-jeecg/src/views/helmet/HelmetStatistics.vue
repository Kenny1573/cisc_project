<template>

    <a-card title="异常统计" :bordered="false">


<div>
      <a-tag  color="pink" icon="unordered-list"  type="primary" @click="handleEd()">选择项目</a-tag>
      <a-tag icon="export"  type="primary"  color="red" @click="exportData()">导出</a-tag>
</div>
      <a-row slot="extra" >
        <a-col>
          <a-radio-group v-model="valueRadio"  color="cyan"  size="small" button-style="solid" @change="unitChange">
            <a-radio-button value="day">日</a-radio-button>
            <a-radio-button value="week">周</a-radio-button>
            <a-radio-button value="month">月</a-radio-button>
            <a-radio-button value="year">年</a-radio-button>
          </a-radio-group>

        </a-col>


      </a-row>
      <div >
        <a-row >
          <div style="text-align: center"><a>{{this.projectName}}</a> </div>
          <div id="errorWarning"  style="margin-left:7%;width: 80%;height: 450px;">
          </div>

        </a-row>
        <div >
          <div style="float: left;margin-left: 25%">
            选择时间区间：
            <a-select default-value="8" style="width: 70px"  v-decorator="['start']" size="small" @change="startChange">
              <a-select-option v-for="item in hour24" :key="item" :value="item">
                {{item}}
              </a-select-option>

            </a-select> ~
            <a-select default-value="17"  style="width: 70px"  v-decorator="['start']" size="small" @change="endChange" >
              <a-select-option v-for="item in hour24" :key="item" :value="item">
                {{item}}
              </a-select-option>

            </a-select>`
          </div>
          <div style="float: right;margin-right: 25%">选择日期：
            <div v-show="dayable" style="float: right">
              <a-date-picker  size="small" placeholder="初始日期" :value="moment(this.startDate,'YYYY-MM-DD HH:mm:ss')" @change="daySChange"> </a-date-picker> ~ <a-date-picker :value="moment(this.endDate,'YYYY-MM-DD HH:mm:ss')"  size="small" placeholder="结束日期"  @change="dayEChange" > </a-date-picker>
            </div>
        <div v-show="weekable" style="float: right">
            <a-week-picker  size="small" placeholder="初始周" :value="moment(this.startDate,'YYYY-MM-DD HH:mm:ss')" @change="daySChange" > </a-week-picker> ~ <a-week-picker :value="moment(this.endDate,'YYYY-MM-DD HH:mm:ss')" size="small" placeholder="结束周" @change="dayEChange"> </a-week-picker>
        </div>

        <div v-show="monthable" style="float: right">
            <a-month-picker  size="small" placeholder="初始月份" :value="moment(this.startDate,'YYYY-MM-DD HH:mm:ss')" @change="daySChange" > </a-month-picker> ~ <a-month-picker :value="moment(this.endDate,'YYYY-MM-DD HH:mm:ss')"  size="small" placeholder="结束月份" @change="dayEChange"> </a-month-picker>
        </div>
        <div v-show="yearable" style="float: right">
          <a-date-picker  format="YYYY" mode="year" :open="this.opens" size="small" :value="moment(this.startDate,'YYYY-MM-DD HH:mm:ss')" :allowClear="true" placeholder="初始年份" @openChange="openChanges" @panelChange="daySChange"  ></a-date-picker>
      ~           <a-date-picker  :value="moment(this.endDate,'YYYY-MM-DD HH:mm:ss')" :open="this.opene" format="YYYY" mode="year" size="small" :allowClear="true" placeholder="结束年份" @openChange="openChangee"    @panelChange="dayEChange" ></a-date-picker>

        </div>

          </div>


      </div>
      </div>
      <project-select ref="modalForma" @ok="modalFormOk" @selectProject="selectProject" ></project-select>
    </a-card>



</template>

<script>
  import JInput from '@comp/jeecg/JInput'
  import ProjectSelect from '@views/yearPlanDetail/modules/ProjectSelect'

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import store from '@/store'
  import JSelectUserByDepRadioModal from '../componments/JSelectUserByDepRadioModal'
  import STable from '@/components/table/'
  import echarts from 'echarts'
  import moment from 'moment'
  import { downFile, getAction, postAction, postBlobAction } from '../../api/manage'

  export default {
    name: 'HelmetStatistics',
    mixins: [JeecgListMixin, mixinDevice],
    components: { JInput,store ,JSelectUserByDepRadioModal,STable,echarts,ProjectSelect},
    mounted () {
      this.$refs.modalForma.visible = true;
      this.errorCharts()
/*
      this.wearCharts()
*/

    },
    computed: {

    },
    data () {
      return {
        prjId:'',
        valueRadio:'day',
        dayable:true,monthable:false,weekable:false,yearable:false,
        modeMonth:['month','month'],modeYear:['year','year'],

        projectName:'',
        isRouteAlive:true,
        hour24:['00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'],
        start1:'8',
        end1:'17',
        startDate: this.initDay(),
        endDate:this.initDay(),

        opens:false,opene:false,
        xdata1:['8','9','10','11','12','13','14','15','16','17'],
        xdata2:['脱帽','戴帽','BeaconId','SOS','撞击','静止警告','上班','下班'],
        ydata1:['86','98','100','11','12','13','14','75','16','17'],
        ydata2:[0,0,0,0,0,0,0,0,0,0],

        disableMixinCreated:true,
        url: {
          getErrorData:'/helmet/errorStatistics',
          exportDate:'/helmet/exportData'
        },
        value:[],
        modalWidth:1500,
        multi:false,
      }
    },
    methods: {
      initDay () {
        var date = new Date()
        var sep="-"
        var year = date.getFullYear()
        var month = date.getMonth()+1
        var strDate=date.getDate()
        if(month>=1&&month<=9){
          month="0"+month
        }
        if (strDate>=0&&strDate<=9){
          strDate="0"+strDate
        }
        return "\""+year+sep+month+sep+strDate+" 08:00:00\""

      },
      openChanges(){
        this.opens=true
      },
      openChangee(){
        this.opene=true
      },
      moment,
      daySChange(e){

        this.startDate=e
        this.getData(this.valueRadio)
        this.opens=false
      },
      dayEChange(e){

        this.endDate=e
        this.getData(this.valueRadio)
        this.opene=false

      },
      startChange(start){
        this.start1=start
        console.log("this type is "+this.valueRadio)
        this.getData(this.valueRadio)

      },
      endChange(end){
        this.end1=end
        this.getData(this.valueRadio)

      },
/*
      startChange(start){
        this.start1=start
        this.xchange()
      },
      endChange(end){
        this.end1=end
        this.xchange()
      },
      xchange(){
        this.xdata1=this.hour24.slice(this.start1-1,this.end1)

        this.wearCharts()
*/

      getData(type){
        getAction(this.url.getErrorData,{prjId:this.prjId,startTime:this.start1,endTime:this.end1,type:type,startDate:this.startDate,endDate:this.endDate}).then((res)=>{
          console.log(JSON.stringify(res))
          this.ydata2=res.result
          this.errorCharts();

        })
      },
      unitChange(e){
        console.log(this.valueRadio)
        this.valueRadio=e.target.value


        switch (this.valueRadio) {
          case 'day':{
            this.dayable=true
            this.monthable=false
            this.yearable=false
            this.weekable=false
            this.getData(this.valueRadio)

            break
          }
          case 'week':{
          this.dayable=false
          this.monthable=false
          this.yearable=false
          this.weekable=true
            this.getData(this.valueRadio)

            break
          }
          case 'month':{
          this.dayable=false
          this.monthable=true
          this.yearable=false
          this.weekable=false
            this.getData(this.valueRadio)

            break
        }
        case 'year':{
          this.dayable=false
          this.monthable=false
          this.yearable=true
          this.weekable=false
          this.getData(this.valueRadio)

          break
        }

        }

      },
      errorCharts(){

        let that =this
        let errorCharts= this.$echarts.init(document.getElementById('errorWarning'))
        let option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#223bf8'
              }
            }
          },
          toolbox: {
            feature: {
              dataView:{show:true,
              realOnly:true,lang:['','关闭','刷新'],
              optionToContent:function (opt) {
                console.log(opt.series)
                var axisData = opt.xAxis[0].data
                var series=opt.series[0].data
                var table = '<table border="3" style="text-align: center;width: 30%;margin-left: 35%"><thead style="font-size: 16px"><tr><td style="width: 50%;text-align: center;">事件类型</td><td  style="width: 50%;text-align: center;">报警次数</td></tr></thead><tbody>'
                for (let i = 0; i <series.length ; i++) {
                  table+='<tr><td  style="width: 50%;text-align: center;" >'+axisData[i]+'</td><td  style="width: 50%;text-align: center;">'+series[i]+'</td></tr>'
                }
                table+='</tbody></table>'
                return table;


              }},
              magicType: {show: true, type: ['line', 'bar']},
              saveAsImage: {show: true}
            }
          },
          formatter: function (params){
            return '事件类型: '+params[0].axisValue+'<br/>'+'次数：'+params[0].value+'次'
          },
          xAxis: [
            {
              type: 'category',
              data: that.xdata2,
              name:'事件类型',
              axisPointer: {
                type: 'line'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '数量',
              min: 0,
              interval: 50,
              axisLabel: {
                formatter: '{value} '
              }
            },

          ],
          series: [
            {
              name: '数量',
              type: 'bar',
              barWidth: 30,
              data: that.ydata2,
              itemStyle: {
                normal:{
                  label:{
                    show: true,
                    position:'top',
                    textStyle: {
                      color:'red',
                      fontSize:14
                    }
                  },
                  color:'#0fbdc2',
                }
              }
            },


          ]
        };
        errorCharts.setOption(option)


      },
      exportData(){
        let fileName="安全帽统计数据"
        postBlobAction(this.url.exportDate,{xData:this.xdata2,yData:this.ydata2,type:this.valueRadio,startDate:this.startDate,endDate:this.endDate,startTime:this.start1,endTime:this.end1}).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
          }else{
            console.log(JSON.stringify(data)+"ssss")
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })

      },
/*
      wearCharts(){

        let that =this
        let wearCharts= this.$echarts.init(document.getElementById('wearHelmet'))
        let option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#223bf8'
              }
            }
          },
          toolbox: {
            feature: {
              magicType: {show: true, type: ['line', 'bar']},
              saveAsImage: {show: true}
            }
          },
          formatter: function (params){
            return '戴帽率: '+params[0].value+'%<br/>时间: '+params[0].axisValue+'h'
          },
          xAxis: [
            {
              type: 'category',
              data: that.xdata1,
              name:'时间/h',
              axisPointer: {
                type: 'line'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '戴帽率',
              min: 0,
              max: 100,
              interval: 25,
              axisLabel: {
                formatter: '{value}% '
              }
            },

          ],
          series: [
            {
              name: '戴帽率',
              type: 'bar',
              barWidth: 30,
              data: that.ydata1,
              itemStyle: {
                normal:{
                  color:'#0fbdc2',
                }
              }
            },

          ]
        };
          wearCharts.setOption(option)


      },*/


nowTime(){
  let time = new Date();
  this.startDate=time
    this.endDate=time

},
      selectProject(value){
        this.projectName=value.prjName
        this.prjId=value.id
        this.getData(this.valueRadio)


      },
      handleEd:function(){
        this.$refs.modalForma.title = "项目选择";
        this.$refs.modalForma.visible = true;
        this.$refs.modalForma.disableSubmit = false;
      },
    }

  }
</script>

<style scoped>

</style>