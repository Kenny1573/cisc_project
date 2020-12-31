//地图全局变量
var map;
var divId = "map";

//鼠标移入移除图层时显示内容
var changeCursor = function (layerObj, attributeName, elementid) {
    require(["esri/basemaps", "esri/map", "esri/geometry/Point", "esri/SpatialReference",
        "esri/layers/FeatureLayer",
        "esri/renderers/SimpleRenderer", "esri/symbols/PictureMarkerSymbol",
        "esri/geometry/ScreenPoint",
        "esri/geometry/Polyline",
        "esri/symbols/SimpleMarkerSymbol", "esri/symbols/SimpleLineSymbol", "esri/Color",
        "esri/graphic", "esri/layers/GraphicsLayer"],
        function (esriBasemaps, Map, Point, SpatialReference,
            FeatureLayer,
            SimpleRenderer, PictureMarkerSymbol,
            ScreenPoint,
            Polyline, SimpleMarkerSymbol, SimpleLineSymbol, Color,
            Graphic, GraphicsLayer) {

            layerObj.onMouseOver = function (e) {
                //当鼠标经过图层时变成手形
                map.setMapCursor('pointer');
                var scrPt = map.toScreen(e.graphic.geometry);
                var textDiv = dojo.doc.createElement("div");
                dojo.attr(textDiv, {
                    "id": elementid
                });
                dojo.style(textDiv, {
                    "left": scrPt.x + 10 + "px",
                    "top": scrPt.y + 10 + "px",
                    "position": "absolute",
                    "z-index": 200,
                    "background": "#fcffd1",
                    "font-size": "10px",
                    "border": "1px solid #0096ff",
                    "padding": "0.1em 0.3em 0.1em",
                    "font-size": "11px",
                    "border-radius": "3px",
                    "box-shadow": "0 0 0.75em #777777"
                });
                textDiv.innerHTML = e.graphic.attributes[attributeName];
                dojo.byId(divId).appendChild(textDiv);
            };

            layerObj.onMouseOut = function () {
                //当鼠标离开图层时变成默认
                map.setMapCursor('default');
                dojo.byId(divId).removeChild(dojo.byId(elementid));
            };
        });
};

//鼠标点击图层显示自定义的infowindow
var customInfoWindow = function (layerObj, title, attributeNames) {
    require(["esri/basemaps", "esri/map", "esri/geometry/Point", "esri/SpatialReference",
        "esri/layers/FeatureLayer",
        "esri/renderers/SimpleRenderer", "esri/symbols/PictureMarkerSymbol",
        "esri/geometry/ScreenPoint",
        "esri/geometry/Polyline",
        "esri/symbols/SimpleMarkerSymbol", "esri/symbols/SimpleLineSymbol", "esri/Color",
        "esri/graphic", "esri/layers/GraphicsLayer"],
        function (esriBasemaps, Map, Point, SpatialReference,
            FeatureLayer,
            SimpleRenderer, PictureMarkerSymbol,
            ScreenPoint,
            Polyline, SimpleMarkerSymbol, SimpleLineSymbol, Color,
            Graphic, GraphicsLayer) {

            var divid = layerObj.id;
            //点击图标显示div
            var pointx, pointy, pointmap;
            //是否显示customInfoWindow
            var iscustomInfoWindowshown = false;
            //添加画线方法
            var drawCustomInfo = function (layerid, startPoint, endPointx, endPointy) {
                map.getLayer("graphicsLayer_LineDrawn_" + layerid).clear();
                var simpleMarkerSymbol = new SimpleMarkerSymbol(
                    SimpleMarkerSymbol.STYLE_CIRCLE,
                    10,
                    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
                        new Color('#4e4bc7'), 1),
                    new Color('#4e4bc7'));
                var graphicPointDrawn = new Graphic(startPoint, simpleMarkerSymbol);
                map.getLayer("graphicsLayer_LineDrawn_" + layerid).add(graphicPointDrawn);

                var endPoint = map.toMap(new ScreenPoint(endPointx + 2, endPointy + 100));
                var polylineJson = {
                    "paths": [[[startPoint.x, startPoint.y], [endPoint.x, endPoint.y]]],
                    "spatialReference": { "wkid": 102100 }
                };
                var polyline = new Polyline(polylineJson);
                var simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color('#4e4bc7'), 2);
                var graphicLineDrawn = new Graphic(polyline, simpleLineSymbol);
                map.getLayer("graphicsLayer_LineDrawn_" + layerid).add(graphicLineDrawn);
            };

            //用于添加各类画线的图层
            var graphicsLayer_LineDrawn = new GraphicsLayer({
                id: "graphicsLayer_LineDrawn_" + layerObj.id
            });
            map.addLayer(graphicsLayer_LineDrawn, 9999);

            //点击图层显示customInfoDiv
            layerObj.onClick = function (e) {
                iscustomInfoWindowshown = true;

                //pointmap = e.graphic.geometry;
                var scrPt = map.toScreen(e.graphic.geometry);
                //pointx = scrPt.x + 10;
                //pointy = scrPt.y - 150;


                var customInfoDiv;
                if ($('#customInfoDiv_' + layerObj.id).length > 0) {
                    $('#customInfoDiv_scrPtx_' + layerObj.id).text(e.graphic.geometry.x);
                    $('#customInfoDiv_scrPty_' + layerObj.id).text(e.graphic.geometry.y);
                    $('#customInfoDiv_pointx_' + layerObj.id).text(scrPt.x + 10);
                    $('#customInfoDiv_pointy_' + layerObj.id).text(scrPt.y - 150);
                    $('#customInfoDiv_iscustomInfoWindowshown_' + layerObj.id).text(1);
                    //将customInfoDiv设置为可拖动
                    $('#customInfoDiv_' + layerObj.id).draggable({
                        start: function () {
                            graphicsLayer_LineDrawn.clear();
                        },
                        drag: function () {
                            //console.log('拖动中..');
                        },
                        stop: function () {
                            //pointx = $('#customInfoDiv_' + divid).position().left;
                            //pointy = $('#customInfoDiv_' + divid).position().top;
                            $('#customInfoDiv_pointx_' + layerObj.id).text($('#customInfoDiv_' + layerObj.id).position().left);
                            $('#customInfoDiv_pointy_' + layerObj.id).text($('#customInfoDiv_' + layerObj.id).position().top);
                            drawCustomInfo(layerObj.id, e.graphic.geometry, parseInt($('#customInfoDiv_pointx_' + layerObj.id).text()), parseInt($('#customInfoDiv_pointy_' + layerObj.id).text()));
                        }
                    });
                } else {
                    customInfoDiv = $("<div id='customInfoDiv_" + layerObj.id + "' class='customInfoDiv'>" +
                        "<div id='customInfoDiv_close_" + layerObj.id + "' class='customInfoDiv_close' title='关闭框体' onClick=\"$('#customInfoDiv_" + layerObj.id + "').hide();$('#customInfoDiv_iscustomInfoWindowshown_" + layerObj.id + "').text(0);map.getLayer('graphicsLayer_LineDrawn_" + layerObj.id + "').clear();\"><font size=\"5\">x</font></div>" +
                        
                        "<div id='customInfoDiv_content_" + layerObj.id + "' class='customInfoDiv_content'></div>" +
                        "<div id='customInfoDiv_footer_" + layerObj.id + "' class='customInfoDiv_footer'>" +
                        "<label id='customInfoDiv_scrPtx_" + layerObj.id + "' class='customInfoDiv_config'>" + e.graphic.geometry.x + "</label>" +
                        "<label id='customInfoDiv_scrPty_" + layerObj.id + "' class='customInfoDiv_config'>" + e.graphic.geometry.y + "</label>" +
                        "<label id='customInfoDiv_pointx_" + layerObj.id + "' class='customInfoDiv_config'>" + (scrPt.x + 10) + "</label>" +
                        "<label id='customInfoDiv_pointy_" + layerObj.id + "' class='customInfoDiv_config'>" + (scrPt.y - 150) + "</label>" +
                        "<label id='customInfoDiv_iscustomInfoWindowshown_" + layerObj.id + "' class='customInfoDiv_config'>1</label>" +
                        "</div>" +
                        "</div>");
                    $('#'+divId).append(customInfoDiv);
                    
                    //将customInfoDiv设置为可拖动
                    $('#customInfoDiv_' + layerObj.id).draggable({
                        start: function () {
                            graphicsLayer_LineDrawn.clear();
                        },
                        drag: function () {
                            //console.log('拖动中..');
                        },
                        stop: function () {
                            //pointx = $('#customInfoDiv_' + divid).position().left;
                            //pointy = $('#customInfoDiv_' + divid).position().top;
                            $('#customInfoDiv_pointx_' + layerObj.id).text($('#customInfoDiv_' + layerObj.id).position().left);
                            $('#customInfoDiv_pointy_' + layerObj.id).text($('#customInfoDiv_' + layerObj.id).position().top);
                            drawCustomInfo(layerObj.id, e.graphic.geometry, parseInt($('#customInfoDiv_pointx_' + layerObj.id).text()), parseInt($('#customInfoDiv_pointy_' + layerObj.id).text()));
                        }
                    });
                }

                //向DIV中添加节点的属性值
                var content = "";
                for (var key in attributeNames) {
                	if (key == "IDS") {
                		continue;
                	}
                	if (key == "PRJ_NAME") {
                		content += "<div id='customInfoDiv_title_" + layerObj.id + "' class='customInfoDiv_title' onclick=\"openFrm('"+e.graphic.attributes[attributeNames["IDS"]]+"')\" ><font size=\"4\">" + e.graphic.attributes[attributeNames["PRJ_NAME"]] + "</font></div>";
                		continue;
                	}
                    content += key + "：" + e.graphic.attributes[attributeNames[key]] + "<br/>";
                }
                $('#customInfoDiv_content_' + layerObj.id).html(content);

                //设定DIV框的位置
                $('#customInfoDiv_' + layerObj.id).css({
                    "left": scrPt.x + 10 + "px",
                    "top": scrPt.y - 150 + "px"
                });
                $('#customInfoDiv_' + layerObj.id).show();


                drawCustomInfo(layerObj.id, e.graphic.geometry, parseInt($('#customInfoDiv_pointx_' + layerObj.id).text()), parseInt($('#customInfoDiv_pointy_' + layerObj.id).text()));
            };

            //鼠标拖动和缩放地图时 修改customInfoDiv的位置
            map.onPan = function (extent, delta) {
                for (i in map.graphicsLayerIds) {
                    var customInfoDiv = document.getElementById("customInfoDiv_" + map.graphicsLayerIds[i]);
                    if (customInfoDiv != null) {
                        if (customInfoDiv.style.display != "none") {
                            $("#customInfoDiv_" + map.graphicsLayerIds[i]).css({
                                "left": parseInt($('#customInfoDiv_pointx_' + map.graphicsLayerIds[i]).text()) + delta.x + "px",
                                "top": parseInt($('#customInfoDiv_pointy_' + map.graphicsLayerIds[i]).text()) + delta.y + "px"
                            });
                        }
                    }
                }
            };
            map.onPanEnd = function (extent, delta) {
                for (i in map.graphicsLayerIds) {
                    var customInfoDiv = document.getElementById("customInfoDiv_" + map.graphicsLayerIds[i]);
                    if (customInfoDiv != null) {
                        if (customInfoDiv.style.display != "none") {
                            $('#customInfoDiv_pointx_' + map.graphicsLayerIds[i]).text($('#customInfoDiv_' + map.graphicsLayerIds[i]).position().left);
                            $('#customInfoDiv_pointy_' + map.graphicsLayerIds[i]).text($('#customInfoDiv_' + map.graphicsLayerIds[i]).position().top);
                        }
                    }
                }
            };
            map.onZoom = function () {
                for (i in map.graphicsLayerIds) {
                    var customInfoDiv = document.getElementById("customInfoDiv_" + map.graphicsLayerIds[i]);
                    if (customInfoDiv != null) {
                        if (customInfoDiv.style.display != "none") {
                            $("#customInfoDiv_" + map.graphicsLayerIds[i]).hide();
                        }
                    }
                }
            };
            map.onZoomEnd = function () {
                for (i in map.graphicsLayerIds) {
                    var customInfoDiv = document.getElementById("customInfoDiv_" + map.graphicsLayerIds[i]);
                    if (customInfoDiv != null) {
                        if ($('#customInfoDiv_iscustomInfoWindowshown_' + map.graphicsLayerIds[i]).text() == "1") {
                            var scrPtx = $('#customInfoDiv_scrPtx_' + map.graphicsLayerIds[i]).text();
                            var scrPty = $('#customInfoDiv_scrPty_' + map.graphicsLayerIds[i]).text();
                            var startmappoint = new Point(scrPtx, scrPty, new SpatialReference({
                                wkid: 102100
                            }));
                            var scrPt = map.toScreen(startmappoint);
                            $('#customInfoDiv_pointx_' + map.graphicsLayerIds[i]).text(scrPt.x + 10);
                            $('#customInfoDiv_pointy_' + map.graphicsLayerIds[i]).text(scrPt.y - 150);
                            $("#customInfoDiv_" + map.graphicsLayerIds[i]).css({
                                "left": scrPt.x + 10 + "px",
                                "top": scrPt.y - 150 + "px"
                            });
                            $("#customInfoDiv_" + map.graphicsLayerIds[i]).show();
                            drawCustomInfo(map.graphicsLayerIds[i], startmappoint, parseInt($('#customInfoDiv_pointx_' + map.graphicsLayerIds[i]).text()), parseInt($('#customInfoDiv_pointy_' + map.graphicsLayerIds[i]).text()));
                        }
                    }
                }
            };

        });
};

function openFrm(id) {
	window.open("../zhcx/index.html?id="+id);
}
