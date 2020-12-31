<template>
  <div>
    <a-card>
      <a-row type="flex">
        <a-col :span="12">
          <a-card :bordered="false">
            <a-spin :spinning="confirmLoading">
              <a-form :form="form">

                <a-row>
                  <a-col span="12">
                    <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjName', validatorRules.prjName]" placeholder="请输入项目名称"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col span="12">
                    <a-form-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjNumber', validatorRules.prjNumber]" placeholder="请输入项目编号"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">
                    <a-form-item label="项目分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <j-dict-select-tag v-decorator="['prjType', validatorRules.prjType]" :trigger-change="true"
                                         dictCode="project_class" placeholder="请输入项目分类"></j-dict-select-tag>
                    </a-form-item>
                  </a-col>
                  <a-col span="12">
                    <a-form-item label="项目阶段" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <j-dict-select-tag type="list" v-decorator="['prjStage', validatorRules.prjStage]"
                                         :trigger-change="true" dictCode="project_process" placeholder="请选择项目阶段"/>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">


                    <a-form-item label="匡算" :labelCol="labelCol" :wrapperCol="wrapperCol" >

                        <a-input v-decorator="['prjRoughEstimate', validatorRules.prjRoughEstimate]"
                                 prefix="￥" suffix="万元" placeholder="请输入匡算" style="width: 100%"/>
                      {{convertCurrency(this.form.getFieldValue('prjRoughEstimate'))}}
                    </a-form-item>


                  </a-col>
                  <a-col span="12">

                    <a-form-item label="估算" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjEstimate', validatorRules.prjEstimate]" placeholder="请输入估算"
                               prefix="￥" suffix="万元" style="width: 100%"/>
                      {{convertCurrency(this.form.getFieldValue('prjEstimate'))}}

                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">
                    <a-form-item label="概算" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjBudgetEstimate', validatorRules.prjBudgetEstimate]"
                               prefix="￥" suffix="万元" placeholder="请输入概算" style="width: 100%"/>
                       {{convertCurrency(this.form.getFieldValue('prjBudgetEstimate'))}}

                    </a-form-item>
                  </a-col>
                  <a-col span="12">
                    <a-form-item label="决算" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjFinalEstimate', validatorRules.prjFinalEstimate]"
                               prefix="￥" suffix="万元" placeholder="请输入决算" style="width: 100%"/>
                       {{convertCurrency(this.form.getFieldValue('prjFinalEstimate'))}}

                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">
                    <a-form-item label="建设规模" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjBuildingSize', validatorRules.prjBuildingSize]"
                               placeholder="请输入建设规模"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col span="12">

                    <a-form-item label="工程地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjBuildingAddress', validatorRules.prjBuildingAddress]"
                               placeholder="请输入工程地址"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">
                    <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjLongitude', validatorRules.prjLongitude]" disabled
                               placeholder="请点击地图获取数据"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col span="12">
                    <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjLatitude', validatorRules.prjLatitude]" disabled
                               placeholder="请点击地图获取数据"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col span="12">
                    <a-form-item label="工程参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input v-decorator="['prjParams', validatorRules.prjParams]" placeholder="请输入工程参数"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col span="12">
                    <a-form-item label="建设内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-textarea v-decorator="['prjBuildingContent', validatorRules.prjBuildingContent]" rows="4"
                                  placeholder="请输入建设内容"/>
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>
            </a-spin>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card :bordered="false" title="项目定位">
            <div id="mapDiv" style="width: 100%;"></div>
          </a-card>
        </a-col>

      </a-row>
<!--<img src="../../../views/images/Location.png" style="height: 200px;width: 150px;" />-->
      <a-row>
        <a-col :span="24">
          <a-card :bordered="false">


            <a-form :form="form" class="form">
              <a-row class="form-row" span="24" type="flex" justify="space-around">
                <a-col span="12">
                  <a-form-item label="前期业主代表">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leaderspreOwner" v-model="leaders.preOwner"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="实施业主代表">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersmidOwner" v-model="leaders.midOwner"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
                <a-col span="12">
                  <a-form-item label="总工程师办公室">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersoffice" v-model="leaders.office"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="计划部">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersplan" v-model="leaders.plan"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
                <a-col span="12">
                  <a-form-item label="道桥部">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersbridge" v-model="leaders.bridge"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="建筑部部">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersbuild" v-model="leaders.build"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
                <a-col span="12">
                  <a-form-item label="质安部">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersquality" v-model="leaders.quality"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="财务部">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersfinance" v-model="leaders.finance"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row class="form-row" :gutter="16" type="flex" justify="space-around">
                <a-col span="12">
                  <a-form-item label="跟踪审计(外)">
                    <a-col :span="22">
                      <j-select-user-by-dep ref="leadersother" v-model="leaders.other"
                                            :multi="true"></j-select-user-by-dep>
                    </a-col>
                  </a-form-item>
                </a-col>
                <a-col span="12"></a-col>
              </a-row>
            </a-form>

          </a-card>
        </a-col>
      </a-row>


      <YearPlanList ref="yearForm"/>
      <a-card :bordered="false">
        <a-row>
          <a-col span="12">
            <a-card title="监管单位" :bordered="false">
              <company-list ref="comForm1" :title="title1"></company-list>
            </a-card>
          </a-col>
          <a-col span="12">
            <a-card title="施工单位" :bordered="false">
              <company-list ref="comForm2" :title="title2"></company-list>
            </a-card>
          </a-col>
        </a-row>


      </a-card>
      <a-button style="float: right" @click="handleOk">添加</a-button>
    </a-card>
  </div>

</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import YearPlanList from '../YearPlanAddList'
  import CompanyList from '../CompanyAddList'
  import { getAction, postAction } from '../../../api/manage'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import { loadModules } from 'esri-loader'
  import locationPic from '@/views/images/Location.png'

  export default {
    name: 'ProjectModal',
    components: {
      JDictSelectTag,
      CompanyList,
      YearPlanList,
      JSelectUserByDep
    },
    data () {
      return {
        title1: '监管单位',
        title2: '施工单位',
        userIds: '',
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
        visible: false,

        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        leaders: {
          other: '',
          preOwner: '',
          build: '',
          office: '',
          bridge: '',
          plan: '',
          finance: '',
          midOwner: '',
          quality: '',
          prjId: ''
        },

        confirmLoading: false,
        validatorRules: {
          prjName: {
            rules: [
              { required: true, message: '请输入项目名称!' }, { pattern: /^.{0,24}$/, message: '超出最大输入长度' },
              { pattern: /^\S.*$/, message: '不能以空格作为开头' }
            ]
          },
          prjNumber: {
            rules: [
              { required: true, message: '请输入项目编号!' }, { pattern: /^.{0,24}$/, message: '超出最大输入长度' },
              { pattern: /^\S.*$/, message: '不能以空格作为开头' }
            ]
          },
          prjType: {
            rules: [
              { required: true, message: '请输入项目分类!' }
            ]
          },
          prjStage: {
            rules: [
              { required: true, message: '请输入项目阶段!' }
            ]
          },
          prjRoughEstimate: {
            rules: [
              { required: false, message: '请输入匡算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!' }
            ]
          },
          prjEstimate: {
            rules: [
              { required: false, message: '请输入估算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!' }
            ]
          },
          prjBudgetEstimate: {
            rules: [
              { required: false, message: '请输入概算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!' }
            ]
          },
          prjFinalEstimate: {
            rules: [
              { required: false, message: '请输入决算!' },
              { pattern: /^(([0-9]\d{0,9})|([0]\.\d{0,6}|[1-9][0-9]*\.\d{0,6}))$/, message: '请输入正确的金额!' }
            ]
          },
          prjBuildingSize: {
            rules: [
              { required: false, message: '请输入建设规模!' }, { pattern: /^.{0,24}$/, message: '超出最大输入长度' }
            ]
          },
          prjBuildingContent: {
            rules: [
              { required: false, message: '请输入建设内容!' }, { pattern: /^.{0,500}$/, message: '超出最大输入长度' }
            ]
          },
          prjBuildingAddress: {
            rules: [
              { required: false, message: '请输入工程地址!' }, { pattern: /^.{0,49}$/, message: '超出最大输入长度' }
            ]
          },
          prjLongitude: {
            rules: [
              { required: false, message: '请输入经度!' }
            ]
          },
          prjLatitude: {
            rules: [
              { required: false, message: '请输入纬度!' }
            ]
          },
          prjParams: {
            rules: [
              { required: false, message: '请输入工程参数!' }, { pattern: /^.{0,49}$/, message: '超出最大输入长度' }
            ]
          }
        },
        url: {
          add: '/project/project/add',
          edit: '/project/project/edit',
          leader: '/projectleader/projectLeader/liste',
          editleaders: '/projectleader/projectLeader/edit'
        }

      }
    },
    created () {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'prjName', 'prjNumber', 'prjType', 'prjStage', 'prjRoughEstimate', 'prjEstimate', 'prjBudgetEstimate', 'prjFinalEstimate', 'prjBuildingSize', 'prjBuildingContent', 'prjBuildingAddress', 'prjLatitude', 'prjParams', 'prjLongitude'))
      })
    },

    mounted () {
      //alert(require("@/views/images/Location.png"));
      // 先清空div
      document.getElementById('mapDiv').innerHTML = ''
      // 定义一个_self防止后续操作中this丢失
      const _self = this
      // 定义一个包含有JS API中js开发包和css样式文件的对象
      const option = {
         url: 'http://192.168.101.231/arcgis_js_api/library/3.30/3.30/init.js',
        css: 'http://192.168.101.231/arcgis_js_api/library/3.30/3.30/esri/css/esri.css'
        // http://192.168.101.53:3000/arcgis_api/library/3.29/3.29/dijit/themes/claro.css
       //url: 'http://192.168.101.53:3000/arcgis_api/library/3.29/3.29/init.js',
        //css: 'http://192.168.101.53:3000/arcgis_api/library/3.29/3.29/esri/css/esri.css'
      }
      // 底图图层
      const map_wuxi = 'http://192.168.101.231:6080/arcgis/rest/services/wuxi/MapServer'
      //const map_wuxi = 'http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineCommunity/MapServer'
      // 通过loadModules来做衔接
      loadModules(
        [
          'esri/basemaps',
          'esri/map',
          'esri/toolbars/draw',
          'esri/geometry/Point',
          'esri/geometry/Polygon',
          'esri/SpatialReference',
          'esri/geometry/webMercatorUtils',
          'esri/symbols/SimpleMarkerSymbol',
          'esri/symbols/SimpleLineSymbol',
          'esri/symbols/SimpleFillSymbol',
          'esri/symbols/PictureMarkerSymbol',
          'esri/Color',
          'esri/graphic',
          'esri/layers/GraphicsLayer',
          'esri/symbols/Font',
          'esri/geometry/Extent',
          'esri/layers/TileInfo',
          'esri/dijit/FeatureTable',
          'esri/dijit/LocateButton',
          'esri/layers/LabelClass',
          'esri/renderers/UniqueValueRenderer',
          'esri/symbols/TextSymbol',
          'esri/InfoTemplate',
          'esri/tasks/query',
          'esri/symbols/Font',
          'dojo/on',
          'dojo/dom',
          'dojo/domReady!'
        ],
        option
      )
        .then(
          ([
            esriBasemaps,
            Map,
            Draw,
            Point,
            Polygon,
            SpatialReference,
            webMercatorUtils,
            SimpleMarkerSymbol,
            SimpleLineSymbol,
            SimpleFillSymbol,
            PictureMarkerSymbol,
            Color,
            Graphic,
            GraphicsLayer,
            Extent,
            TileInfo,
            FeatureTable,
            LocateButton,
            LabelClass,
            UniqueValueRenderer,
            TextSymbol,
            InfoTemplate,
            query,
            Font,
            on,
            dom
          ]) => {
            // 无锡地图
            esriBasemaps.wuxi = {
              baseMapLayers: [
                {
                  url: map_wuxi
                }
              ],
              title: 'wuxi'
            }

            // 地图默认中心点
            const centerPoint = new Point(
              120.364384,
              31.592643,
              new SpatialReference({
                wkid: 4326
              })
            )

            // 定义地图
            var map = new Map('mapDiv', {
              backgroundColor: '#7ddce3',
              basemap: 'wuxi',
              center: centerPoint,
              zoom: 15,
              minZoom: 6,
              maxZoom: 17,
              fadeOnZoom: true,
              logo: false,
              slider: false,
              sliderLabels: [6, 8, 10, 12, 14, 16, 18],
              sliderOrientation: 'vertical',
              sliderStyle: 'large',
              showLabels: true
            })

            var pic = "../../../views/images/Location.png";
            var attr = {};
            setTimeout(function() {
              //如果是编辑,则显示传过来的点位
              var graphic = new Graphic(new Point(_self.model.prjLatitude, _self.model.prjLongitude,
                      new SpatialReference({
                        wkid : 4326
                      })),
                      new PictureMarkerSymbol(pic, 26, 50),
                      attr,
                      null);
                map.graphics.add(graphic); 
              
            }, 500)
            /*var pointLayer = new GraphicsLayer();
                  map.addLayer(pointLayer);*/
            var tb = new Draw(map)
            var location
            //启用绘画工具条画点
            tb.activate(Draw.POINT)
            tb.onDrawEnd = function (geometry) {
              if (location != null) {
                map.graphics.remove(location);
              }
            /*var symbol = {
              type: "picture-marker",
              url: "../../../views/images/Location.png",
              width: "26px"
            },
              var newPoint = new Point(geometry.getLatitude(), geometry.getLongitude(), new SpatialReference({wkid:4326}));
              var picSymbol = new PictureMarkerSymbol("../../../views/images/Location.png", 26, 60);
              //location = new Graphic(geometry, new PictureMarkerSymbol("../../../views/images/Location.png", 26, 60));
              var picGraphic = new Graphic(newPoint, picSymbol);../../../views/images/
            
             location = new Graphic(geometry,
                    new PictureMarkerSymbol("../../../views/images/Location.png", 26, 60));
              */
              location = new Graphic(geometry,
                    new SimpleMarkerSymbol('circle', 20, null, new Color([0,0,0,0.8])));
                
              map.graphics.add(location);
              
              console.log(geometry.getLatitude())

              _self.model.prjLatitude = geometry.getLatitude() //geometry.x
              _self.model.prjLongitude = geometry.getLongitude()  // geometry.y
              _self.$nextTick(() => {
                _self.form.setFieldsValue(pick(_self.model, 'prjLatitude', 'prjLongitude'))
              })

             
            // alert(locationPic);
             
            }

            
          }
        )
        .catch(err => {
          // this.$message('地图创建失败，' + err)
          console.log('地图创建失败' + err)
        })
    },
    methods: {

      add () {
        this.edit({})
      },
      edit (record) {

        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'prjName', 'prjNumber', 'prjType', 'prjStage', 'prjRoughEstimate', 'prjEstimate', 'prjBudgetEstimate', 'prjFinalEstimate', 'prjBuildingSize', 'prjBuildingContent', 'prjBuildingAddress', 'prjLatitude', 'prjParams', 'prjLongitude'))
        })
      },
      addYearData (e) {
        this.yeardata.push(e)
      },
      close () {
        this.$emit('close')
        this.visible = false
      },
      handleOk () {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            console.log('formdata is ' + JSON.stringify(formData))
            this.leaders.plan = this.$refs.leadersplan.userIds
            this.leaders.build = this.$refs.leadersbuild.userIds
            this.leaders.finance = this.$refs.leadersfinance.userIds
            this.leaders.midOwner = this.$refs.leadersmidOwner.userIds

            this.leaders.preOwner = this.$refs.leaderspreOwner.userIds

            this.leaders.office = this.$refs.leadersoffice.userIds
            this.leaders.bridge = this.$refs.leadersbridge.userIds
            this.leaders.other = this.$refs.leadersother.userIds
            this.leaders.quality = this.$refs.leadersquality.userIds
            let jsonPost = {}
            jsonPost.project = formData
            let c1 = this.$refs.comForm1.getData()
            console.log('======' + JSON.stringify(c1))
            jsonPost.companies1 = c1
            let c2 = this.$refs.comForm2.getData()
            console.log('======' + JSON.stringify(c2))

            //jsonPost.companies2=c2;
            jsonPost.leaders = this.leaders
            jsonPost.yearPlans = this.$refs.yearForm.getData()
            console.log('jsonPost is ' + JSON.stringify(jsonPost))

            postAction(httpurl, jsonPost).then((res) => {
              if (res.success) {

                that.$message.success(res.message)
                that.$emit('ok1')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }

        })
      },
      convertCurrency (money) {
        if (money==null){
          money=0
        }
        money=money*10000
        //汉字的数字
        var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖')
        //基本单位
        var cnIntRadice = new Array('', '拾', '佰', '仟')
        //对应整数部分扩展单位
        var cnIntUnits = new Array('', '万', '亿', '兆')
        //对应小数部分单位
        var cnDecUnits = new Array('角', '分', '毫', '厘')
        //整数金额时后面跟的字符
        var cnInteger = '整'
        //整型完以后的单位
        var cnIntLast = '元'
        //最大处理的数字
        var maxNum = 9999999999.999999
        //金额整数部分
        var integerNum
        //金额小数部分
        var decimalNum
        //输出的中文金额字符串
        var chineseStr = ''
        //分离金额后用的数组，预定义
        var parts
        if (money == '') {
          return ''
        }
        money = parseFloat(money)
        if (money == 0) {
          chineseStr = cnNums[0] + cnIntLast + cnInteger
          return chineseStr
        }
        //转换为字符串
        money = money.toString()
        if (money.indexOf('.') == -1) {
          integerNum = money
          decimalNum = ''
        } else {
          parts = money.split('.')
          integerNum = parts[0]
          decimalNum = parts[1].substr(0, 6)
        }
        //获取整型部分转换
        if (parseInt(integerNum, 10) > 0) {
          var zeroCount = 0
          var IntLen = integerNum.length
          for (var i = 0; i < IntLen; i++) {
            var n = integerNum.substr(i, 1)
            var p = IntLen - i - 1
            var q = p / 4
            var m = p % 4
            if (n == '0') {
              zeroCount++
            } else {
              if (zeroCount > 0) {
                chineseStr += cnNums[0]
              }
              //归零
              zeroCount = 0
              chineseStr += cnNums[parseInt(n)] + cnIntRadice[m]
            }
            if (m == 0 && zeroCount < 4) {
              chineseStr += cnIntUnits[q]
            }
          }
          chineseStr += cnIntLast
        }
        //小数部分
        if (decimalNum != '') {
          var decLen = decimalNum.length
          for (var i = 0; i < decLen; i++) {
            var n = decimalNum.substr(i, 1)
            if (n != '0') {
              chineseStr += cnNums[Number(n)] + cnDecUnits[i]
            }
          }
        }
        if (chineseStr == '') {
          chineseStr += cnNums[0] + cnIntLast + cnInteger
        } else if (decimalNum == '') {
          chineseStr += cnInteger
        }
        return chineseStr
      },
      handleCancel () {
        this.close()
      },
      popupCallback (row) {
        this.form.setFieldsValue(pick(row, 'prjName', 'prjNumber', 'prjType', 'prjStage', 'prjRoughEstimate', 'prjEstimate', 'prjBudgetEstimate', 'prjFinalEstimate', 'prjBuildingSize', 'prjBuildingContent', 'prjBuildingAddress', 'prjLatitude', 'prjParams', 'prjLongitude'))
      }
    }

  }
</script>