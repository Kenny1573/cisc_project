<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总销售额" total="￥126,560">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px;">
              <span slot="term">周同比</span>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">日同比</span>
              11%
            </trend>
          </div>
          <template slot="footer">
            日均销售额
            <span>￥ 234.56</span>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="订单量" :total="8846 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area />
          </div>
          <template slot="footer">
            日订单量
            <span>{{ '1234' | NumberFormat }}</span>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="支付笔数" :total="6560 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" />
          </div>
          <template slot="footer">
            转化率
            <span>60%</span>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="运营活动效果" total="78%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              80%
            </trend>
          </template>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :bordered="false" :body-style="{padding: '0', marginBottom: '24px'}" title="天气">
      <a-row>
        <div class="tianqi">
          <a-col :xl="4" :lg="4" :md="4" :sm="12" :xs="12">
            <a class="weather_oneday">
              <div class="weather_hover"></div>
              <div class="weather_split"></div>
              <div class="tianqi1">
                <p
                  calss="weather_date"
                  style="text-align: left; padding: 16px 0 0 12px; color: #e7edf2; font-size: 14px;"
                >08月03日 周一 农历六月十四</p>
                <div class="weather_shishi">
                  <div class="weather_shishi_icon"></div>
                  <div class="weather_shishi_info">
                    <span class="weather_shishi_title">31</span>
                    <span class="weather_shishi_data">
                      <i class="weather_shishi_sup">°C</i>
                      <i class="weather_shishi_sub">晴（实时）</i>
                    </span>
                  </div>
                </div>
                <p class="weather_temp">27 ~ 34°C</p>
                <p class="weather_weath">小雨转中雨</p>
                <p class="weather_wind">东风微风</p>
                <div class="weather_wrap">
                  <span class="weather_aqi_text">优</span>
                </div>
              </div>
            </a>
          </a-col>
          <a-col :xl="2" :lg="2" :md="2" :sm="12" :xs="12">
            <a class="weather_oneday">
              <div class="weather_hover"></div>
              <div class="weather_split"></div>
              <div class="tianqi1">
                <p
                  calss="weather_date"
                  style="text-align: center; padding-top: 52px; color: #e7edf2;"
                >周三</p>
                <p calss="weather_date_day">08月05日</p>
                <div class="weather_future_icon weather_future_img1"></div>
                <p class="weather_temp">28 ~ 34°C</p>
                <p class="weather_weath">小雨转阴</p>
                <p class="weather_wind">西风微风</p>
                <div class="weather_wrap">
                  <span class="weather_aqi_text">优</span>
                </div>
              </div>
            </a>
          </a-col>
          <a-col :xl="2" :lg="2" :md="2" :sm="12" :xs="12">
            <a class="weather_oneday">
              <div class="weather_hover"></div>
              <div class="weather_split"></div>
              <div class="tianqi1">
                <p
                  calss="weather_date"
                  style="text-align: center; padding-top: 52px; color: #e7edf2;"
                >周四</p>
                <p calss="weather_date_day">08月06日</p>
                <div class="weather_future_icon weather_future_img2"></div>
                <p class="weather_temp">29 ~ 35°C</p>
                <p class="weather_weath">多云</p>
                <p class="weather_wind">西风微风</p>
                <div class="weather_wrap">
                  <span class="weather_aqi_text">优</span>
                </div>
              </div>
            </a>
          </a-col>
          <a-col :xl="2" :lg="2" :md="2" :sm="12" :xs="12">
            <a class="weather_oneday">
              <div class="weather_hover"></div>
              <div class="weather_split"></div>
              <div class="tianqi1">
                <p
                  calss="weather_date"
                  style="text-align: center; padding-top: 52px; color: #e7edf2;"
                >周五</p>
                <p calss="weather_date_day">08月07日</p>
                <div class="weather_future_icon weather_future_img3"></div>
                <p class="weather_temp">29 ~ 33°C</p>
                <p class="weather_weath">小雨</p>
                <p class="weather_wind">南风微风</p>
                <div class="weather_wrap">
                  <span class="weather_aqi_text">优</span>
                </div>
              </div>
            </a>
          </a-col>
          <a-col :xl="2" :lg="2" :md="2" :sm="12" :xs="12">
            <a class="weather_oneday">
              <div class="weather_hover"></div>
              <div class="weather_split"></div>
              <div class="tianqi1">
                <p
                  calss="weather_date"
                  style="text-align: center; padding-top: 52px; color: #e7edf2;"
                >周六</p>
                <p calss="weather_date_day">08月08日</p>
                <div class="weather_future_icon weather_future_img4"></div>
                <p class="weather_temp">29 ~ 32°C</p>
                <p class="weather_weath">小雨</p>
                <p class="weather_wind">西南风微风</p>
                <div class="weather_wrap">
                  <span class="weather_aqi_text">优</span>
                </div>
              </div>
            </a>
          </a-col>
        </div>
        <a-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
          <div id="myecharts" style="width: 100%; height: 300px;"></div>
        </a-col>
      </a-row>
    </a-card>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs
          default-active-key="1"
          size="large"
          :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}"
        >
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{width: '256px'}" />
          </div>
          <a-tab-pane loading="true" tab="销售额" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="销售额排行" :dataSource="barData" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="销售趋势" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="销售额趋势" :dataSource="barData" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card
          :loading="loading"
          :bordered="false"
          title="最近一周访问量统计"
          :style="{ marginTop: '24px' }"
        >
          <a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="environment" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="team" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="rise" style="font-size: 24px" />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import RankList from '@/components/chart/RankList'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'

import Trend from '@/components/Trend'
import { getLoginfo, getVisitInfo } from '@/api/api'

const rankList = []
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: '白鹭岛 ' + (i + 1) + ' 号店',
    total: 1234.56 - i * 100
  })
}
const barData = []
for (let i = 0; i < 12; i += 1) {
  barData.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200
  })
}
export default {
  name: 'IndexChart',
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo
  },
  data() {
    return {
      loading: true,
      center: null,
      rankList,
      barData,
      loginfo: {},
      visitFields: ['ip', 'visit'],
      visitInfo: [],
      indicator: <a-icon type="loading" style="font-size: 24px" spin />
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.initLogInfo()
  },
  methods: {
    initLogInfo() {
      getLoginfo(null).then(res => {
        if (res.success) {
          Object.keys(res.result).forEach(key => {
            res.result[key] = res.result[key] + ''
          })
          this.loginfo = res.result
        }
      })
      getVisitInfo().then(res => {
        if (res.success) {
          console.log('aaaaaa', res.result)
          this.visitInfo = res.result
        }
      })
    },
    //创建echarts的方法
    myEcharts: function() {
      let _this = this
      // 基于准备好的dom，初始化echarts实例
      this.mydefaultchart = this.$echarts.init(document.getElementById('myecharts'))

      // 指定图表的配置项和数据
      let colors = ['#1E90FF', '#1E90FF', '#AFEEEE']
      let data = ['26.3', '30.2', '30.3', '33.4', '33.0', '32.5', '32.0']

      let option = {
        color: colors,

        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        grid: {
          right: '8%'
        },
        toolbox: {
          top: '5%',
          right: '5%',
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: false },
            saveAsImage: { show: true }
          }
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisTick: {
              alignWithLabel: true
            },
            axisLine: {
              lineStyle: {
                color: colors[0]
              }
            },
            data: ['', '07-31', '08-01', '08-02', '08-03', '08-04', '08-05', '08-06']
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '温度',
            min: 0,
            max: 40,
            position: 'left',
            axisTick: {
              show: false
            },
            axisLine: {
              show: false,
              lineStyle: {
                color: colors[0]
              }
            },
            axisLabel: {
              formatter: '{value} °C'
            }
          }
        ],
        series: [
          {
            name: '平均温度',
            type: 'line',
            data: ['26.3', '28.6', '30.2', '30.3', '33.4', '33.0', '32.5', '32.0'],
            itemStyle: {
              normal: {
                lineStyle: {
                  color: colors[1]
                }
              }
            },
            label: {
              show: true,
              //formatter: '{c} °C'
              formatter: function(param) {
                var index = param.dataIndex
                if (index === 0) {
                  return '' //虚拟数据上不显示标签
                } else {
                  return param.value + '°C'
                }
              }
            },
            symbolSize: function(value, param) {
              var index = param.dataIndex
              if (index === 0) {
                return 0 //虚拟数据上不显示标点
              } else {
                return 4
              }
            },
            areaStyle: {
              color: colors[2]
            }
          }
        ]
      }

      // 使用刚指定的配置项和数据显示图表。
      this.mydefaultchart.setOption(option)

      // 这里添加一个onresize事件用于调整echarts框大小尺寸
      window.onresize = function() {
        setTimeout(function() {
          _this.mydefaultchart.resize()
        }, 400)
      }
    }
  },
  mounted: function() {
    let _this = this
    // 这里延迟400毫秒执行echarts生成方法,防止页面刷新时eharts显示异常
    setTimeout(function() {
      _this.myEcharts()
    }, 400)
  }
}
</script>

<style lang="less" scoped>
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }
  p {
    line-height: 42px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}

.tianqi {
  .tianqi1 {
    height: 300px;
    //background: url('../images/fine.png') no-repeat;
    background-image: linear-gradient(#257bca, #6ba8df);
  }
  color: #fff;
}

.weather_split {
  position: absolute;
  left: 0px;
  top: 42px;
  width: 1px;
  height: 240px;
  background: #fff;
  filter: alpha(opacity=15);
  opacity: 0.2;
}

.weather_shishi {
  text-align: center;
}

.weather_shishi_icon {
  width: 64px;
  height: 64px;
  margin: 5px 0 0 70px;
  background: url('../images/fine.png') no-repeat;
}

.weather_shishi_info {
  position: relative;
}

.weather_shishi_title {
  font-size: 48px;
  line-height: 64px;
  display: inline-block;
  font-family: Arial;
  text-shadow: 1px 1px 1px #555;
}

.weather_shishi_data {
  position: relative;
  display: inline-block;
}
.weather_shishi_sup,
.weather_shishi_sub {
  display: block;
  text-align: left;
  font-style: normal;
  text-shadow: 1px 1px 1px #555;
}

.tianqi1 p {
  text-align: center;
  font-size: 12px;
  text-shadow: 1px 1px 1px #555;
  line-height: 1em;
}

.weather_date_day {
  margin-top: 8px;
  filter: alpha(opacity=50);
  opacity: 0.5;
  text-shadow: none;
}

.weather_future_icon {
  width: 64px;
  height: 64px;
  margin: 20px 0 0 20px;
  //background: url('../images/fine.png') no-repeat;
}

.weather_future_img1 {
  background: url('../images/brain.png') no-repeat;
}

.weather_future_img2 {
  background: url('../images/srain.png') no-repeat;
}

.weather_future_img3 {
  background: url('../images/cloud1.png') no-repeat;
}

.weather_future_img4 {
  background: url('../images/cloud2.png') no-repeat;
}

.weather_wrap {
  width: 100%;
  text-align: center;
}

.weather_aqi_text {
  font-family: arial, '微软雅黑';
  background-color: #40c057;
  display: block;
  width: 38px;
  height: 19px;
  line-height: 19px;
  border-radius: 3px;
  margin: 0px auto;
  margin-top: 12px;
}

.weather_oneday {
  color: #fff;
  position: absolute;
  width: 100%;
  height: 300px;
  text-decoration: none;
  border: none;
  z-index: 20;
  outline: none;
  cursor: default;
}

.weather_oneday:hover .weather_hover {
  background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0.015), rgba(255, 255, 255, 0.15));
}

.weather_hover {
  z-index: 10;
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>