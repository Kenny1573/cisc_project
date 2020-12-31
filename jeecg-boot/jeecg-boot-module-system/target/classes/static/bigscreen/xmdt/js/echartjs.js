var legal_person_data = {
	"uploadData": [{ "count": 630 }, { "count": 986 }, { "count": 792 }, { "count": 642 }, { "count": 521 }, { "count": 573 }
		, { "count": 832 }, { "count": 747 }, { "count": 983 }, { "count": 836 }, { "count": 831 }, { "count": 633 }],
	"updateData": [{ "count": 110 }, { "count": 181 }, { "count": 123 }, { "count": 197 }, { "count": 123 }, { "count": 173 }
		, { "count": 123 }, { "count": 151 }, { "count": 101 }, { "count": 152 }, { "count": 101 }, { "count": 177 }],
	"viewData": [{ "count": 651 }, { "count": 842 }, { "count": 223 }, { "count": 223 }, { "count": 221 }, { "count": 812 }
		, { "count": 928 }, { "count": 219 }, { "count": 613 }, { "count": 254 }, { "count": 981 }, { "count": 301 }]
};
var people_data = {
	"uploadData": [{ "count": 1300 }, { "count": 1686 }, { "count": 1692 }, { "count": 1742 }, { "count": 1621 }, { "count": 773 }
		, { "count": 832 }, { "count": 1047 }, { "count": 1483 }, { "count": 1336 }, { "count": 831 }, { "count": 973 }],
	"updateData": [{ "count": 103 }, { "count": 281 }, { "count": 203 }, { "count": 197 }, { "count": 173 }, { "count": 154 }
		, { "count": 223 }, { "count": 251 }, { "count": 201 }, { "count": 252 }, { "count": 201 }, { "count": 277 }],
	"viewData": [{ "count": 651 }, { "count": 842 }, { "count": 223 }, { "count": 223 }, { "count": 221 }, { "count": 812 }
		, { "count": 928 }, { "count": 219 }, { "count": 613 }, { "count": 254 }, { "count": 981 }, { "count": 301 }]
};
var picture_data = {
	"uploadData": [{ "count": 330 }, { "count": 786 }, { "count": 492 }, { "count": 842 }, { "count": 421 }, { "count": 673 }
		, { "count": 932 }, { "count": 447 }, { "count": 583 }, { "count": 436 }, { "count": 331 }, { "count": 433 }],
	"updateData": [{ "count": 10 }, { "count": 81 }, { "count": 23 }, { "count": 97 }, { "count": 23 }, { "count": 73 }
		, { "count": 23 }, { "count": 51 }, { "count": 01 }, { "count": 52 }, { "count": 01 }, { "count": 77 }],
	"viewData": [{ "count": 451 }, { "count": 342 }, { "count": 523 }, { "count": 323 }, { "count": 421 }, { "count": 812 }
		, { "count": 728 }, { "count": 619 }, { "count": 613 }, { "count": 554 }, { "count": 481 }, { "count": 301 }]
};

// 基于准备好的dom，初始化echarts实例
var myChart1 = echarts.init(document.getElementById('echarts1'));
var myChart2 = echarts.init(document.getElementById('echarts2'));
var myChart3 = echarts.init(document.getElementById('echarts3'));

// setTimeout(init_myChart1(),2000);
//init_myChart2();
//init_myChart3(legal_person_data);


function init_myChart1(data1) {
	var color1 = ['#6a98ff', '#5dfcac', '#8b95aa', '#facb21', '#f8725d', '#FF7C44', '#FA3E5F', '#6635EF', '#FFAFDA'];
	option = {
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)",
			transitionDuration: 0,
			backgroundColor: 'rgba(83,93,105,0.8)',
			borderColor: '#535b69',
			borderRadius: 6,
			borderWidth: 2,
			extraCssText: 'width:150px;height:50px',
		},
		legend: {
			icon: 'circle',
			orient: 'vertical',
			left: '79%',
			top: 'bottom',
			data: ['市政', '交通', '房建', '景观绿化', '其他'],
			textStyle: {
				color: '#8b95aa',
				fontSize: '17',
				fontFaily: '微软雅黑'
			}
		},
		graphic: {
			elements: [{
				type: 'image',
				style: {
					/*image: giftImageUrl,*/
					width: 50,
					height: 50
				},
				left: 'center',
				top: 'center'
			}]
		},
		series: [{
			name: '资源量',
			type: 'pie',
			radius: ['45%', '65%'],
			color: color1,
			center: ['45%', '50%'],
			hoverAnimation: true,
			hoverOffset: 5,
			itemStyle: {
				normal: {
					// 扇形边框及宽度
					borderWidth: 10,
					//borderColor: '#FFF',
					// 扇形阴影及宽度
					shadowBlur: 120,
					//shadowColor: '#44EAB1'
				}
			},
			label: {
				show: false,
			},
			labelLine: {
				normal: {
					show: true,
					length: 10,
					length2: 15,
					lineStyle: {
						width: 2
					}
				}
			},
			label: {
				normal: {
					formatter: '{b|{b}}\n{hr|}\n{d|{d}%}',
					rich: {
						b: {
							fontSize: 20,
							// color: '#12EABE',
							align: 'left',
							padding: 2
						},
						hr: {
							borderColor: '#12EABE',
							width: '80%',
							borderWidth: 2,
							height: 0
						},
						d: {
							fontSize: 20,
							color: '#fff',
							align: 'left',
							padding: 4
						},
						c: {
							fontSize: 20,
							color: '#fff',
							align: 'left',
							padding: 4
						}
					}
				}
			},
			data: [
				{ value: 2875, name: '市政' },//, selected: true
				{ value: 3571, name: '交通' },
				{ value: 1429, name: '房建' },
				{ value: 714, name: '景观绿化' },
				{ value: 1429, name: '其他' },
			]
		}
		]
	};

	// var data1 = [
	// 	{ value: 2875, name: '市政' },//, selected: true
	// 	{ value: 3571, name: '交通' },
	// 	{ value: 1429, name: '房建' },
	// 	{ value: 714, name: '景观绿化' },
	// 	{ value: 1429, name: '其他' },
	// ];

	myChart1.setOption(option);

	var seriesData = [];
	for (var i = 0; i < data1.length; i++) {
		seriesData.push({
			value: data1[i].value,
			name: data1[i].name,
			itemStyle: {
				normal: {
					borderWidth: 5,
					shadowBlur: 10,
					borderColor: color1[i],
					shadowColor: color1[i]
				}
			}
		}, {
			value: 0.01,
			name: '',
			itemStyle: {
				normal: {
					label: {
						show: false
					},
					labelLine: {
						show: false
					},
					color: 'rgba(0, 0, 0, 0)',
					borderColor: 'rgba(0, 0, 0, 0)',
					borderWidth: 0
				}
			}
		});
	};

	myChart1.setOption({
		series: [{
			data: seriesData
		}]
	});

	window.addEventListener('resize', function () { myChart1.resize(); })
}


function init_myChart2(yvalue, data1) {
	option = {
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		legend: {
			left: '10%',
			top: '88%',
			icon: 'circle',
			data: ['投资完成比'],
			textStyle: {
				color: '#8b95aa',
				fontSize: '16',
				fontFaily: '微软雅黑'
			}
		},
		grid: {
			top: '30',
			left: '30',
			right: '30',
			bottom: '50',
			containLabel: true
		},
		xAxis: {
			type: 'value',
			boundaryGap: [0, 0.01],
			axisLine: {
				lineStyle: {
					color: '#5dfcac',
					width: '3'
				}
			},
			axisLabel: {
				textStyle: {
					color: '#8b95aa',
				},
				fontSize: '16'
			},
			splitLine: {
				lineStyle: {
					color: '#8b95aa40',
					width: '2'
				}
			}
		},
		yAxis: {
			type: 'category',
			data: ['项目8', '项目7', '项目6', '项目5', '项目4', '项目3', '项目2', '项目1'],
			axisLine: {
				lineStyle: {
					color: '#8b95aa'
				},
			},
			// axisLabel: {
			// 	textStyle: {
			// 		color: '#8b95aa',
			// 	},
			// 	fontSize: '16'
			// }
			axisLabel: {
				color: "#8b95aa",
				fontSize: '16',
				interval: 0,
				formatter: function (params) {
					var newParamsName = ""; // 最终拼接成的字符串
					var paramsNameNumber = params.length; // 实际标签的个数
					var provideNumber = 7; // 每行能显示的字的个数
					var rowNumber = Math.ceil(paramsNameNumber / provideNumber); // 换行的话，需要显示几行，向上取整
					/**
					 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
					 */
					// 条件等同于rowNumber>1
					if (paramsNameNumber > provideNumber) {
						/** 循环每一行,p表示行 */
						for (var p = 0; p < rowNumber; p++) {
							var tempStr = ""; // 表示每一次截取的字符串
							var start = p * provideNumber; // 开始截取的位置
							var end = start + provideNumber; // 结束截取的位置
							// 此处特殊处理最后一行的索引值
							if (p == rowNumber - 1) {
								// 最后一次不换行
								tempStr = params.substring(start, paramsNameNumber);
							} else {
								// 每一次拼接字符串并换行
								tempStr = params.substring(start, end) + "\n";
							}
							newParamsName += tempStr; // 最终拼成的字符串
						}

					} else {
						// 将旧标签的值赋给新标签
						newParamsName = params;
					}
					//将最终的字符串返回
					return newParamsName
				}
			},
		},
		series: [
			{
				name: '投资完成比',
				type: 'bar',
				barWidth: '60%',
				data: [23, 42, 28, 74, 76, 75, 43, 58],
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'right',
							textStyle: {
								color: '#ffffff',
								fontSize: '16'
							}
						},
						color: function (params) {
							let colorList = [
								'#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff', '#f48d36ab', '#6a98ff'
							];
							return colorList[params.dataIndex];
						},
					}
				}
			}
		]
	};

	myChart2.setOption(option);

	myChart2.setOption({
		yAxis: {
			data: yvalue,
		},
		series: [{
			data: data1,
		}]
	});

	window.addEventListener('resize', function () { myChart2.resize(); })
}


function init_myChart3(xname, pvalue) {
	option = {
		color: ['#04a7f3', '#e7c42c', '#38c171', '#b445d9'],
		legend: {
			left: '5%',
			top: '86%',
			icon: 'circle',
			data: ['一般问题', '安全问题', '质量问题', '设备问题'],
			textStyle: {
				color: '#8b95aa',
				fontSize: '14',
				fontFaily: '微软雅黑'
			},
			itemGap: 40
		},
		grid: {
			top: '30',
			left: '30',
			right: '30',
			bottom: '100',
		},
		yAxis: {
			type: 'value',
			axisLabel: {
				textStyle: {
					color: '#9faed5',
				}
			},
			axisLine: {
				show: false
			},
			splitLine: {
				show: true,
				lineStyle: {
					color: '#274072'
				}
			}
		},
		xAxis: {
			type: 'category',
			data: ['项目1', '项目2', '项目3', '项目4', '项目5', '项目6', '项目7', '项目8', '项目9', '项目10'],
			// axisLabel: {
			// 	textStyle: {
			// 		color: '#9faed5',
			// 		margin: 15,
			// 	},
			// 	rotate: 40
			// },
			axisLabel: {
				textStyle: {
					color: '#9faed5',
					margin: 15,
				},
				rotate: 40,
				fontSize: '16',
				interval: 0,
				formatter: function (params) {
					var newParamsName = ""; // 最终拼接成的字符串
					var paramsNameNumber = params.length; // 实际标签的个数
					var provideNumber = 4; // 每行能显示的字的个数
					var rowNumber = Math.ceil(paramsNameNumber / provideNumber); // 换行的话，需要显示几行，向上取整
					/**
					 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
					 */
					// 条件等同于rowNumber>1
					if (paramsNameNumber > provideNumber) {
						/** 循环每一行,p表示行 */
						for (var p = 0; p < rowNumber; p++) {
							var tempStr = ""; // 表示每一次截取的字符串
							var start = p * provideNumber; // 开始截取的位置
							var end = start + provideNumber; // 结束截取的位置
							// 此处特殊处理最后一行的索引值
							if (p == rowNumber - 1) {
								// 最后一次不换行
								tempStr = params.substring(start, paramsNameNumber);
							} else {
								// 每一次拼接字符串并换行
								tempStr = params.substring(start, end) + "\n";
							}
							newParamsName += tempStr; // 最终拼成的字符串
						}

					} else {
						// 将旧标签的值赋给新标签
						newParamsName = params;
					}
					//将最终的字符串返回
					return newParamsName
				}
			},
			axisLine: {
				show: true,
				lineStyle: {
					color: 'rgb(0, 255, 255)'
				},
			},
		},
		series: [
			{
				name: '一般问题',
				type: 'bar',
				stack: '总量',
				data: [2, 2, 1, 4, 3, 3, 2, 1, 2, 2],
				barWidth: 20,
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'inside',
							textStyle: {
								color: '#ffffff',
								fontSize: '16'
							}
						}
					}
				}
			},
			{
				name: '安全问题',
				type: 'bar',
				stack: '总量',
				data: [2, 3, 1, 4, 1, 2, 2, 3, 3, 2],
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'inside',
							textStyle: {
								color: '#ffffff',
								fontSize: '16'
							}
						}
					}
				}
			},
			{
				name: '质量问题',
				type: 'bar',
				stack: '总量',
				data: [2, 2, 1, 4, 2, 3, 1, 3, 1, 4],
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'inside',
							textStyle: {
								color: '#ffffff',
								fontSize: '16'
							}
						}
					}
				}
			},
			{
				name: '设备问题',
				type: 'bar',
				stack: '总量',
				data: [5, 1, 1, 4, 9, 3, 1, 3, 3, 2],
				itemStyle: {
					normal: {
						barBorderRadius: [3, 3, 0, 0],
						label: {
							show: true,
							position: 'inside',
							textStyle: {
								color: '#ffffff',
								fontSize: '16'
							}
						}
					}
				}
			}
		]
	};
	if (option && typeof option === "object") {
		myChart3.setOption(option, true);
	}

	myChart3.setOption(option);

	myChart3.setOption({
		xAxis: {
			data: xname,
		},
		series: [
			pvalue
		]
	});

	window.addEventListener('resize', function () { myChart3.resize(); })
}
