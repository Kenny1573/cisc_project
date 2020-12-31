<template>
  <j-modal
    :title="title"
    :width="width*1.6"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <a-form :form="form">
        <a-row>
          <a-col :span="12">

            <a-form-item label="项目" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['bsPrjId']" :trigger-change="true" dictCode="pjplat.BS_PROJECT,PRJ_NAME,ID" placeholder="请选择项目"/>
            </a-form-item>
            <a-form-item label="通道ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsVideoChannelid', validatorRules.bsVideoChannelid ]" placeholder="请输入通道ID"></a-input>
            </a-form-item>
            <a-form-item label="通道名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsVideoChannelname',validatorRules.bsVideoChannelname ]" placeholder="请输入通道名称"></a-input>
            </a-form-item>
            <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsVideoLon']" :disabled="true" placeholder="请从右侧点击获取经度数据"></a-input>
            </a-form-item>
            <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsVideoLat']" :disabled="true" placeholder="请从右侧点击获取纬度数据"></a-input>
            </a-form-item>
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['bsVideoStatus']" :trigger-change="true" dictCode="online_status" placeholder="请选择状态"/>
            </a-form-item>
            <a-form-item label="IP地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsIp', validatorRules.bsIp]" placeholder="请输入IP地址"></a-input>
            </a-form-item>
            <a-form-item label="网关" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bsGateway', validatorRules.bsGateway]" placeholder="请输入网关"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
           <a-card :bordered="false" title="监控定位">
            <div id="mapDiv" style="width: 100%;height:400px;"></div>
          </a-card>
          </a-col>
        </a-row>


      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import { loadModules } from 'esri-loader'


  export default {
    name: "VideoModal",
    components: {
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          bsVideoChannelname: {
            rules: [
              { pattern: /^.{0,40}$/, message: '超出最大输入长度'},

              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},              { pattern: /^\S.*$/, message:'不能以空格作为开头'},

            ]
          },
          bsVideoChannelid: {
            rules: [
              { pattern: /^.{0,40}$/, message: '超出最大输入长度'},

              { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]$/, message: '不能含特殊符号'},
              { pattern: /^\S.*$/, message:'不能以空格作为开头'},
            ]
          },

          bsIp: {
            rules: [
              { pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/, message: '请输入正确的ip地址!'},
            ]
          },
          bsGateway: {
            rules: [
              { pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/, message: '请输入正确格式的网关!'}
            ]
          },
        },
        url: {
          add: "/video/video/add",
          edit: "/video/video/edit",
        }
      }
    },
    mounted() {
  
    },
    methods: {
      add () {
        this.edit({});
       
        this.loadMap();
      },
      edit (record) {
       
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'bsPrjId','bsVideoChannelid','bsVideoChannelname','bsVideoLon','bsVideoLat','bsVideoStatus','bsIp','bsGateway'))
        })
         this.loadMap();
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'bsPrjId','bsVideoChannelid','bsVideoChannelname','bsVideoLon','bsVideoLat','bsVideoStatus','bsIp','bsGateway'))
      },

      loadMap(){
                // 先清空div
          window.onload=function(){
            document.getElementById('mapDiv').innerHTML = ''
          }
          // 定义一个_self防止后续操作中this丢失
          const _self = this;
          // 定义一个包含有JS API中js开发包和css样式文件的对象
          const option = {
          url: 'http://192.168.101.231/arcgis_js_api/library/3.30/3.30/init.js',
          css: 'http://192.168.101.231/arcgis_js_api/library/3.30/3.30/esri/css/esri.css'
        // http://192.168.101.52:3000/arcgis_api/library/3.29/3.29/dijit/themes/claro.css
          //url: 'http://36.155.109.193:3000/arcgis_api/library/3.29/3.29/init.js',
          //css: 'http://36.155.109.193:3000/arcgis_api/library/3.29/3.29/esri/css/esri.css'
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
              'esri/Color',
              'esri/graphic',
              'esri/layers/GraphicsLayer',
              "esri/symbols/Font",
                "esri/geometry/Extent",
                "esri/layers/TileInfo",
                "esri/dijit/FeatureTable",
                "esri/dijit/LocateButton",
                "esri/layers/LabelClass",
                "esri/renderers/UniqueValueRenderer",
                "esri/symbols/PictureMarkerSymbol",
                "esri/symbols/TextSymbol",
                "esri/InfoTemplate",
                "esri/tasks/query",
                "esri/symbols/Font",
                "dojo/on",
                "dojo/dom",
                "dojo/domReady!"
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
                Color,
                Graphic,
                GraphicsLayer,
                Extent,
                TileInfo,
                FeatureTable,
                LocateButton,
                LabelClass,
                UniqueValueRenderer,
                PictureMarkerSymbol,
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
                var lat = _self.model.prjLatitude, lon=_self.model.prjLongitude;
                if (lat == null || lon == null) {
                  lat = 31.592643;
                  lon = 120.364384;
                }
                // 地图默认中心点
                const centerPoint = new Point(
                  lon,
                  lat,
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
                var pointLayer = new GraphicsLayer();
                map.addLayer(pointLayer);

                var location;
                setTimeout(function() {
                  var pic = "../../../views/images/Location.png";	//new PictureMarkerSymbol(pic, 26, 50),
                  var attr = {};
                  if (_self.model.prjLatitude != null && _self.model.prjLongitude != null) {
                    //如果是编辑,则显示传过来的点位
                    location = new Graphic(centerPoint,
                              new SimpleMarkerSymbol('circle', 20, null, new Color([0,0,0,0.8]))
                            );
                    pointLayer.add(location); 
                  }
                  var tb = new Draw(map);
                
                  //启用绘画工具条画点
                  tb.activate(Draw.POINT);
                  tb.onDrawEnd = function(geometry) {
                    if (location != null) {
                      pointLayer.remove(location);
                    }
                  console.log(geometry.getLatitude());
                  console.log(this.model);

                    _self.model.bsVideoLat = geometry.getLatitude(); //geometry.x
                    _self.model.bsVideoLon = geometry.getLongitude();  // geometry.y
                    console.log(_self.model);
                    _self.$nextTick(() => {
                              _self.form.setFieldsValue(pick(_self.model, 'bsVideoLat',  'bsVideoLon'))
                            })
                    location = new Graphic(geometry,
                      new SimpleMarkerSymbol('circle', 20, null, new Color([0,0,0,0.8])));
                  
                    pointLayer.add(location);
                  };
                }, 1000);
              }
            )
            .catch(err => {
            // this.$message('地图创建失败，' + err)
              console.log("地图创建失败"+err);
            })
      },
    }
  }
</script>