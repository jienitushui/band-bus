// 服务器请求地址
var apiroot;
var searchType = ["home-main-line", "home-main-quna", "home-main-station"];
var nowtype = 1;
var showHtml = "line.html";
var stationListTemp = [];// 线路站点列表
var realTimeInfo;// 动态车辆数据
var selectStationId = 5;
var lastStationId; //前一次选中站点
var busSpeed = 400;// 公交车平均速度
var transferList = [];// 换乘方案列表
var realTimeThread;// 实时数据线程
var lineNameTemp;// 线路名称
var directionTemp;// 线路方向
var stationTemp;

var locationcity;
var cityList;
var newsList;

var mapFlag = false;
var nearFlag = false;

var showCustom = false;
var allLine = false;
var ziXun = true;
var showXianglingbus = false;
var showWangyuebus = false;

var map;// 地图
var infoWindow;
var polyline;// 轨迹线
var lineArr = [];// 轨迹点
var stationMks = [];// 站点列表
var busMks = [];// 实时车辆标注
var firstStation;
var routeInfo;// 线路详情
var first = true;

var allLinelist = [];//所有线路列表

var ONEDIR = 0;

var searchText;// 是否带了查询参数

var specialText = "";

var MZTstyle = false;//

var lastBusIndexMap = {};//记录每辆车最近上一次拟合点
var latestBusMarkerId = -1;//离候车站最近车辆marker，下标
var latestBusName;//最近车辆名称
var mapView = false;
var initMap = false;

init = function() {
	// var root = getRootPath();
	// if (!root) {
	// root = "WebBusServer"
	// }
	apiroot = "ApiData.do";
	// localStorage.setItem("cityName", "宁德市");
	// localStorage.setItem("cityName", '');
	// localStorage.removeItem("cityName");
	// sessionStorage.removeItem("tab-index");

}

// 解析定位结果
function onComplete(res) {
	// document.getElementById('status').innerHTML = '定位成功'
	// var str = [];
	// str.push('定位结果：' + data.position);
	// str.push('定位类别：' + data.location_type);
	// if (data.accuracy) {
	// str.push('精度：' + data.accuracy + ' 米');
	// }// 如为IP精确定位结果则没有精度信息
	// str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
	// document.getElementById('result').innerHTML = str.join('<br>');
	try {
		console.log(res);
		var cityname = localStorage.getItem("cityName");
		var locationCity = res.addressComponent.city;

		if ("xijiu101401" == localStorage.getItem("citykey")) {
			locationCity = "习酒通勤"
		}
		if (locationCity == "贵阳市" && res.addressComponent.district == "白云区") {
			locationCity = "贵阳市白云区";
		}
		if (locationCity == "漳州市" && "zzg0001" == localStorage.getItem("citykey")) {
			locationCity = "漳州港";
		}
		if (locationCity == "福州市" && res.addressComponent.district == "平潭县") {
			locationCity = "平潭综合实验区";
		}
		if (locationCity == "杭州市" && "chunan072022" == localStorage.getItem("citykey")) {
			locationCity = "淳安";
		}
		if (locationCity == "宁波市" && res.addressComponent.district == "慈溪市") {
			locationCity = "慈溪市";
		}
		if (locationCity == "宁波市" && res.addressComponent.district == "象山县") {
			locationCity = "象山县";
		}
		if (locationCity != cityname && res.addressComponent.district != cityname) {
			$("#index-text").html("市中心");
			searchNearBy('', '');
			return;
		}
		$("#index-text").html("附近");
		var myLat = res.position.lat;
		var myLng = res.position.lng
		sessionStorage.setItem("myLat", myLat);
		sessionStorage.setItem("myLng", myLng);
		searchNearBy(myLng, myLat);

		$("#input_start").val("我的位置");
		sessionStorage.setItem("transfer-start", "我的位置");
		sessionStorage.setItem("transfer-slat", myLat);
		sessionStorage.setItem("transfer-slng", myLng);
	} catch (d) {
		$("#index-text").html("市中心");
		searchNearBy("", "");
		return
	}
	// searchNearBy('','');
}

// 解析定位错误信息
function onError(data) {
	// document.getElementById('status').innerHTML = '定位失败'
	// document.getElementById('result').innerHTML = '失败原因排查信息:' + data.message;
	$("#index-text").html("市中心");
	console.log('失败原因排查信息:' + data.message + '</br>浏览器返回信息：' + data.originMessage);
	//var citykey = getKEY();
	//if (citykey == "cangzhou00001") {
	//	alert(data.message);
	//}
	searchNearBy('', '');
}

function searchNearBy(lng, lat) {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		toCityList();
		return;
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": cityname,
			"LAT": lat,
			"LNG": lng,
			"CMD": "106",
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			var html = "<div class=\"no-nearsta\">";
			html += "<img style=\"width: 150px; height: 150px; margin-bottom: 10px;\" src=\"images/default_img_route.png\">";
			html += "<div>附近暂未搜索到站点</div>";
			html += "</div>"
			if ("xijiu101401" === citykey) {
				if (data.status != 1 || data.data.length == 0) {
					html = "";
					if (!xijiuSpecial1) {
						var h1 = "";
						h1 += "<div onclick=\"toStationLineV2('习酒高速站','28.1692166357','106.1861581859')\" class=\"near-list-view-con\">";
						h1 += "<div class=\"near-list-view-station\">";
						h1 += "<img src=\"images/home_icon_station_big.png\">";
						h1 += "<div class=\"near-list-line\">";
						h1 += '习酒高速站';
						h1 += "</div>";
						h1 += "</div>";
						h1 += "<div class=\"near-list-dir\">";
						h1 += "</div>";
						h1 += "</div>";
						h1 += "<div id=\"first-station-line\"></div>";
						firstStation = {
							name: "习酒高速站",
							lat: "28.1692166357",
							lng: "106.1861581859",
						}
						html = h1 + html;
					}
					if (!xijiuSpecial2) {
						var h1 = "";
						h1 += "<div onclick=\"toStationLineV2('高速习水站','28.3014964818','106.2098903192')\" class=\"near-list-view-con\">";
						h1 += "<div class=\"near-list-view-station\">";
						h1 += "<img src=\"images/home_icon_station_big.png\">";
						h1 += "<div class=\"near-list-line\">";
						h1 += '高速习水站';
						h1 += "</div>";
						h1 += "</div>";
						h1 += "<div class=\"near-list-dir\">";
						h1 += "</div>";
						h1 += "</div>";
						html = h1 + html;
					}
					$("#near-list-view").html(html);
					searchNearLine();
				}
			}
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				$("#near-list-view").html(html);
				return;
			}
			var stationList = data.data;
			if (stationList.length == 0) {
				$("#near-list-view").html(html);
				return;
			}
			html = "";
			var xijiuSpecial1 = false;
			var xijiuSpecial2 = false;
			for (var i = 0; i < stationList.length; i++) {
				if (i > 3) {
					continue;
				}
				var station = stationList[i];
				if (station.name == "习酒高速站") {
					xijiuSpecial1 = true;
				}
				if (station.name == "高速习水站") {
					xijiuSpecial2 = true;
				}
				//html += "<li>";
				html += "<div onclick=\"toStationLineV2('" + station.name + "','" + station.lat + "','" + station.lon + "')\" class=\"near-list-view-con\">";
				html += "<div class=\"near-list-view-station\">";
				html += "<img src=\"images/home_icon_station_big.png\">";
				html += "<div class=\"near-list-line\">";
				html += station.name;
				html += "</div>";
				html += "</div>";
				html += "<div class=\"near-list-dir\">";
				if (station.dis > 0) {
					html += station.dis;
					html += "m";
				}
				html += "</div>";
				html += "</div>";
				if (i == 0) {
					html += "<div id=\"first-station-line\"></div>";
					firstStation = {
						name: station.name,
						lat: station.lat,
						lng: station.lon,
					}
				}
				//html += "</li>";
			}
			if ("xijiu101401" === citykey) {
				if (!xijiuSpecial1) {
					var h1 = "";
					h1 += "<div onclick=\"toStationLineV2('习酒高速站','28.1692166357','106.1861581859')\" class=\"near-list-view-con\">";
					h1 += "<div class=\"near-list-view-station\">";
					h1 += "<img src=\"images/home_icon_station_big.png\">";
					h1 += "<div class=\"near-list-line\">";
					h1 += '习酒高速站';
					h1 += "</div>";
					h1 += "</div>";
					h1 += "<div class=\"near-list-dir\">";
					h1 += "</div>";
					h1 += "</div>";
					html = h1 + html;
				}
				if (!xijiuSpecial2) {
					var h1 = "";
					h1 += "<div onclick=\"toStationLineV2('高速习水站','28.3014964818','106.2098903192')\" class=\"near-list-view-con\">";
					h1 += "<div class=\"near-list-view-station\">";
					h1 += "<img src=\"images/home_icon_station_big.png\">";
					h1 += "<div class=\"near-list-line\">";
					h1 += '高速习水站';
					h1 += "</div>";
					h1 += "</div>";
					h1 += "<div class=\"near-list-dir\">";
					h1 += "</div>";
					h1 += "</div>";
					html = h1 + html;
				}
			}
			$("#near-list-view").html(html);
			searchNearLine();
		},
		error: function(data) {
			reqError();
		}
	});
}


function searchNearLine() {
	var cityname = localStorage.getItem("cityName");
	if (!cityname) {
		return;
	}
	if (!firstStation) {
		return;
	}
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "115",
			"CITYNAME": cityname,
			"STATIONNAME": firstStation.name,
			"MYLAT": firstStation.lat,
			"MYLNG": firstStation.lng,
			"ALL": 0,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var html = "";
			var lineList = data.data;
			for (var i = 0; i < lineList.length; i++) {
				var line = lineList[i];
				if (i == (lineList.length - 1)) {
					html += "<div class=\"near-list-view-con3\"";
					html += " onclick=\"toBusLineDetails2('";
					html += line.lineName;
					html += "','";
					html += line.upperOrDown
					html += "','";
					html += line.stationOrder
					html += "')\">";
				} else {
					html += "<div class=\"near-list-view-con2\"";
					html += " onclick=\"toBusLineDetails2('";
					html += line.lineName;
					html += "','";
					html += line.upperOrDown
					html += "','";
					html += line.stationOrder
					html += "')\">";
				}
				html += "<div class=\"near-list-view-left\">";
				html += "<div class=\"near-list-line\">";
				html += line.lineName;
				html += "</div>";
				html += "<div class=\"near-list-dir\">方向 ";
				html += line.to;
				html += "</div>";
				html += "</div>";
				html += "<div class=\"near-list-view-right\">";
				if (MZTstyle) {
					html += "<div class=\"near-list-num_mzt\">";
				} else {
					html += "<div class=\"near-list-num\">";
				}
				html += line.neartext;
				html += "</div>";
				html += "<div class=\"near-list-dir\">";
				html += line.neardis;
				html += "</div>";
				html += "</div>";
				html += "</div>";
			}
			$("#first-station-line").html(html);

		},
		error: function(data) {
			reqError();
		}
	});
}

// 获取当前定位
function getLocation() {
	var map = new AMap.Map('container', {
		resizeEnable: true,
		addOns: ['moveAnimation']
	});

	AMap.plugin('AMap.Geolocation', function() {
		var geolocation = new AMap.Geolocation({
			enableHighAccuracy: true,// 是否使用高精度定位，默认:true
			timeout: 5000, // 超过10秒后停止定位，默认：5s
			buttonPosition: 'RB', // 定位按钮的停靠位置
			// buttonOffset : new AMap.Pixel(10, 20),//
			// 定位按钮与设置的停靠位置的偏移量，默认：Pixel(10,
			// 20)
			// zoomToAccuracy : true, // 定位成功后是否自动调整地图视野到定位点

		});
		map.addControl(geolocation);
		geolocation.getCurrentPosition(function(status, result) {
			if (status == 'complete') {
				// var cityname = localStorage.getItem("cityName");
				// if (!cityname) {
				// cityInfo(result)
				// return;
				// }
				onComplete(result)
			} else {
				onError(result)
			}
		});
	});
}

// home

function searchLineByKey() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		return;
	}
	var keyword = $("#input_line").val();
	var html = "";
	if (!keyword || keyword.length == 0) {
		$("#list-view").html(html);
		showHistory('1');
		return;
	}
	$("#history").removeClass("history");
	$("#history").addClass("history-none");
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": cityname,
			"KEYWORD": keyword,
			"CMD": "114",
			"CITYKEY": citykey
		},
		success: function(data) {
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			var lineList = data.buslines;
			for (var i = 0; i < lineList.length; i++) {
				html += "<li onclick=\"toBusLineDetails('";
				html += lineList[i].lineName;
				html += "','";
				html += lineList[i].upperOrDown
				html += "')\">";
				html += "<img src=\"images/home_icon_route_big.png\">";
				html += "<div>";
				html += lineList[i].lineName;
				html += " 开往 ";
				html += lineList[i].to;
				html += "</div></li>";
			}
			$("#list-view").html(html);
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchStationByKey() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		return;
	}
	var keyword = $("#input_station").val();
	var html = "";
	if (!keyword || keyword.length == 0) {
		$("#list-view").html(html);
		showHistory('2');
		return;
	}
	$("#history").removeClass("history");
	$("#history").addClass("history-none");
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "110",
			"CITYNAME": cityname,
			"KEYWORD": keyword,
			"CITYKEY": citykey
		},
		success: function(data) {
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			var busstations = data.busstations;
			for (var i = 0; i < busstations.length; i++) {
				html += "<li onclick=\"toStationLine('"
					+ busstations[i].stationName + "')\">";
				html += "<img src=\"images/home_icon_station_big.png\">";
				html += "<div>";
				html += busstations[i].stationName;
				html += "</div></li>";
			}
			$("#list-view").html(html);
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchLineAnStation() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		return;
	}
	var keyword = $("#input_line").val();
	var html = "";
	if (!keyword || keyword.length == 0) {
		$("#list-view").html(html);
		showHistory('1');
		return;
	}
	$("#history").removeClass("history");
	$("#history").addClass("history-none");

	//线路站点隐藏历史
	$("#his-text").addClass("hide");
	$("#his-clean").addClass("hide");

	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": cityname,
			"KEYWORD": keyword,
			"CMD": "102",
			"CITYKEY": citykey,
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			var lineList = data.buslines;
			for (var i = 0; i < lineList.length; i++) {
				html += "<li onclick=\"toBusLineDetails('";
				html += lineList[i].lineName;
				html += "','";
				html += lineList[i].upperOrDown
				html += "')\">";
				html += "<img src=\"images/home_icon_route_big.png\">";
				html += "<div>";
				html += lineList[i].lineName;
				html += " 开往 ";
				html += lineList[i].to;
				html += "</div></li>";
			}
			var busstations = data.busstations;
			for (var i = 0; i < busstations.length; i++) {
				html += "<li onclick=\"toStationLineV2('"
					+ busstations[i].stationName + "')\">";
				html += "<img src=\"images/home_icon_station_big.png\">";
				html += "<div>";
				html += busstations[i].stationName;
				html += "</div></li>";
			}
			$("#list-view").html(html);
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchAllLine(key, oneDir) {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		return;
	}
	ONEDIR = oneDir;
	//var lineListH = sessionStorage.getItem(cityname + "allLine");
	var html = "";
	//	if(lineListH){
	//		var lineList = JSON.parse(lineListH);
	//		for (var i = 0; i < lineList.length; i++) {
	//			html += "<li onclick=\"toBusLineDetails('";
	//			html += lineList[i].lineName;
	//			html += "','";
	//			html += lineList[i].upperOrDown
	//			html += "')\">";
	//			html += "<img src=\"images/home_icon_route_big.png\">";
	//			html += "<div>";
	//			html += lineList[i].lineName;
	//			html += " 开往 ";
	//			html += lineList[i].to;
	//			html += "</div></li>";
	//		}
	//		$("#all-list-view").html(html);
	//		return;
	//	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": cityname,
			// "KEYWORD" : keyword,
			"CMD": "119",
			"CITYKEY": citykey,
			"KEY": key
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			allLinelist = data.buslines;
			if (ONEDIR == 0) {
				for (var i = 0; i < allLinelist.length; i++) {
					var line = allLinelist[i];
					var lineName = line.lineName;
					if (cityname == "防城港市") {
						var str = ",3路,4路,7路,8路,9路,10路,11路,103路,104路,115路,117路,202路,203路,206路,207路,208路,210路,211路,212路,213路,";
						if (str.indexOf("," + lineName + ",") < 0) {
							continue;
						}
					}
					if (citykey == "juye082903" && lineName == "巨野923路") {
						continue;
					}
					if (citykey == "juye082903" && lineName == "郓城至巨野") {
						continue
					}
					if (citykey == "chengwu0817" && lineName == "成武983路") {
						continue
					}
					if (citykey == "jichang012324" && lineName.indexOf("地铁") >= 0) {
						continue
					}
					if (citykey == "chengwu0817" && lineName == "单县-成武-菏泽机场") {
						continue
					}
					if (citykey == "shanxian062501" && lineName == "单县-成武-菏泽机场") {
						continue
					}
					if (citykey == "shanxian062501" && lineName == "单县973路") {
						continue
					}
					if (citykey == "huanghua202106" && (lineName == "101路" || lineName == "102路" || lineName == "103路")) {
						continue
					}
					if (citykey == "xjinkaibus001" && (lineName != "6路" && lineName != "咸阳郭杜线")) {
						continue
					}
					if (line.upperOrDown == "1" && line.routeNameUp) {
						lineName = line.routeNameUp;
					}
					if (line.upperOrDown == "2" && line.routeNameDown) {
						lineName = line.routeNameDown;
					}
					html += "<li onclick=\"toBusLineDetails('";
					html += line.lineName;
					html += "','";
					html += line.upperOrDown
					html += "')\">";
					html += "<img src=\"images/home_icon_route_big.png\">";
					html += "<div>";
					html += lineName;
					html += " 开往 ";
					html += line.to;
					html += "</div></li>";
				}
			} else {
				for (var i = 0; i < allLinelist.length; i++) {
					var line = allLinelist[i];
					var lineName = line.lineName;
					if (cityname == "防城港市") {
						var str = ",3路,4路,7路,8路,9路,10路,11路,103路,104路,115路,117路,202路,203路,206路,207路,208路,210路,211路,212路,213路,";
						if (str.indexOf("," + lineName + ",") < 0) {
							continue;
						}
					}
					if (citykey == "juye082903" && lineName == "巨野923路") {
						continue;
					}
					if (citykey == "juye082903" && lineName == "郓城至巨野") {
						continue
					}
					if (citykey == "chengwu0817" && lineName == "成武983路") {
						continue
					}
					if (citykey == "shanxian062501" && lineName == "单县973路") {
						continue
					}
					if (citykey == "shanxian062501" && lineName == "单县-成武-菏泽机场") {
						continue
					}
					if (citykey == "huanghua202106" && (lineName == "101路" || lineName == "102路" || lineName == "103路")) {
						continue
					}
					if (citykey == "xjinkaibus001" && (lineName != "6路" && lineName != "咸阳郭杜线")) {
						continue
					}

					if (line.upperOrDown == "1" && line.routeNameUp) {
						lineName = line.routeNameUp;
					}
					if (line.upperOrDown == "2") {
						continue;
					}
					html += "<li onclick=\"toBusLineDetails('";
					html += line.lineName;
					html += "','";
					html += line.upperOrDown
					html += "')\">";
					html += "<img src=\"images/home_icon_route_big.png\">";
					html += "<div>";
					html += lineName;
					html += "【";
					html += line.from + "~" + line.to;
					html += "】";
					html += "</div></li>";
				}
			}
			$("#all-list-view").html(html);
			//	sessionStorage.setItem(cityname + "allLine", JSON.stringify(allLinelist));
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchAllLineLeShan(key) {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	if (!cityname) {
		return;
	}
	var html = "";
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": cityname,
			// "KEYWORD" : keyword,
			"CMD": "119",
			"CITYKEY": citykey,
			"KEY": ""
		},
		success: function(data) {
			console.log(data);
			console.log(key);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			allLinelist = data.buslines;
			for (var i = 0; i < allLinelist.length; i++) {
				var line = allLinelist[i];
				var lineName = line.lineName;
				if ("沙湾" == key) {
					if (lineName.indexOf("沙湾") < 0) {
						continue;
					}
				} else if ("五通桥" == key) {
					if (lineName.indexOf("五通") < 0) {
						continue;
					}
				} else if ("夹江" == key) {
					if (lineName.indexOf("夹江") < 0) {
						continue;
					}
				} else if ("城际线路" == key) {
					//304 305 306 307 601 602  301.302.308.309
					if (lineName != "301路" && lineName != "302路" && lineName != "308路" && lineName != "309路" && lineName != "304路" && lineName != "305路" && lineName != "304路" && lineName != "306路" && lineName != "307路" && lineName != "601路" && lineName != "602路") {
						continue;
					}
				} else {
					if (lineName.indexOf("沙湾") >= 0 || lineName.indexOf("五通") >= 0 || lineName.indexOf("夹江") >= 0 || lineName == "304路" || lineName == "305路" || lineName == "304路" || lineName == "306路" || lineName == "307路" || lineName == "601路" || lineName == "602路" || lineName == "301路" || lineName == "302路" || lineName == "308路" || lineName == "309路") {
						continue;
					}
				}
				if (line.upperOrDown == "1" && line.routeNameUp) {
					lineName = line.routeNameUp;
				}
				if (line.upperOrDown == "2") {
					continue;
				}
				html += "<li onclick=\"toBusLineDetails('";
				html += line.lineName;
				html += "','";
				html += line.upperOrDown
				html += "')\">";
				html += "<img src=\"images/home_icon_route_big.png\">";
				html += "<div>";
				html += lineName;
				html += "【";
				html += line.from + "~" + line.to;
				html += "】";
				html += "</div></li>";
			}
			$("#all-list-view").html(html);
			//	sessionStorage.setItem(cityname + "allLine", JSON.stringify(allLinelist));
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchAllLineXiJiu(company) {
	var html = "";
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYNAME": "习酒通勤",
			"CMD": "119",
			"CITYKEY": "xijiu101401",
			"KEY": ""
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return
			}
			swal.close();
			allLinelist = data.buslines;
			for (var g = 0; g < allLinelist.length; g++) {
				var f = allLinelist[g];
				if (f.company.indexOf(company) < 0) {
					continue;
				}
				var h = f.lineName;
				if (f.upperOrDown == "1" && f.routeNameUp) {
					h = f.routeNameUp
				}
				if (f.upperOrDown == "2") {
					continue
				}
				html += "<li onclick=\"toBusLineDetails('";
				html += f.lineName;
				html += "','";
				html += f.upperOrDown;
				html += "')\">";
				html += '<img src="images/home_icon_route_big.png">';
				html += "<div>";
				html += h;
				html += "【";
				html += f.from + "~" + f.to;
				html += "】";
				html += "</div></li>"
			}
			$("#list-view").html(html)
		},
		error: function(f) {
			reqError()
		}
	})
}


function searchLineForindex() {
	if (!allLinelist) {
		return;
	}
	var key = $("#input_allline").val();
	var html = "";
	if (ONEDIR == 0) {
		for (var i = 0; i < allLinelist.length; i++) {
			var line = allLinelist[i];
			var lineName = line.lineName;
			if (line.upperOrDown == "1" && line.routeNameUp) {
				lineName = line.routeNameUp;
			}
			if (line.upperOrDown == "2" && line.routeNameDown) {
				lineName = line.routeNameDown;
			}
			if (key && key.length > 0 && lineName.indexOf(key) < 0) {
				continue;
			}
			html += "<li onclick=\"toBusLineDetails('";
			html += line.lineName;
			html += "','";
			html += line.upperOrDown
			html += "')\">";
			html += "<img src=\"images/home_icon_route_big.png\">";
			html += "<div>";
			html += lineName;
			html += " 开往 ";
			html += line.to;
			html += "</div></li>";
		}
	} else {
		for (var i = 0; i < allLinelist.length; i++) {
			var line = allLinelist[i];
			var lineName = line.lineName;
			if (line.upperOrDown == "1" && line.routeNameUp) {
				lineName = line.routeNameUp;
			}
			if (line.upperOrDown == "2") {
				continue;
			}
			if (key && key.length > 0 && lineName.indexOf(key) < 0) {
				continue;
			}
			html += "<li onclick=\"toBusLineDetails('";
			html += line.lineName;
			html += "','";
			html += line.upperOrDown
			html += "')\">";
			html += "<img src=\"images/home_icon_route_big.png\">";
			html += "<div>";
			html += lineName;
			html += "【";
			html += line.from + "~" + line.to;
			html += "】";
			html += "</div></li>";
		}
	}
	$("#all-list-view").html(html);
}



function hengyun() {
	localStorage.setItem("cityName", "衡水市");
	localStorage.setItem("citykey", "hengshui0501");
	searchAllLine('', 0);
}

function feixian() {
	localStorage.setItem("cityName", "临沂市");
	localStorage.setItem("citykey", "lyfx0001");
	searchAllLine('费县', 0);
}

function yuhuan() {
	localStorage.setItem("cityName", "玉环市");
	localStorage.setItem("citykey", "yuhuan202106");
	searchAllLine('', 0);
}

function anda() {
	localStorage.setItem("cityName", "漳州港");
	localStorage.setItem("citykey", "zzg0001");
	searchAllLine('', 0);
}

function longquan() {
	localStorage.setItem("cityName", "丽水市");
	localStorage.setItem("citykey", "longquan001");
	searchAllLine('龙泉', 0);
}

function quangang() {
	localStorage.setItem("cityName", "泉州市");
	localStorage.setItem("citykey", "qg595803");
	searchAllLine('泉港', 0);
}

function yanggu() {
	localStorage.setItem("cityName", "聊城市");
	localStorage.setItem("citykey", "yanggu0105");
	searchAllLine('阳谷', 0);
}

function zhangjiachuan() {
	localStorage.setItem("cityName", "天水市");
	localStorage.setItem("citykey", "zhangjiachuan2201");
	searchAllLine('张家川', 0);
}

function xinye() {
	localStorage.setItem("cityName", "新野县");
	localStorage.setItem("citykey", "xy595803");
	searchAllLine('', 0);
}

function huanghua() {
	localStorage.setItem("cityName", "黄骅市");
	localStorage.setItem("citykey", "huanghua202106");
	searchAllLine('', 0);
}

function handancx() {
	localStorage.setItem("cityName", "邯郸市");
	localStorage.setItem("citykey", "handancx0621");
	searchAllLine('', 0);
}
function ycsanxialvyou() {
	localStorage.setItem("cityName", "宜昌市");
	localStorage.setItem("citykey", "ycsanxialvyou");
	searchAllLine('', 0);
}
function yidu() {
	localStorage.setItem("cityName", "宜昌市");
	localStorage.setItem("citykey", "yidu0427001");
	searchAllLine('宜都', 0);
}
function lanzhou_jc() {
	localStorage.setItem("cityName", "兰州市");
	localStorage.setItem("citykey", "jcdb0427001");
	searchAllLine('机场巴士', 0);
}
function songzi() {
	localStorage.setItem("cityName", "松滋市");
	localStorage.setItem("citykey", "sz10302001");
	searchAllLine('', 0);
}
function daxing_jc() {
	localStorage.setItem("cityName", "北京市");
	localStorage.setItem("citykey", "jichang012324");
	searchAllLine('大兴机场', 0);
}
function jinkaibus() {
	localStorage.setItem("cityName", "咸阳市");
	localStorage.setItem("citykey", "xjinkaibus001");
	searchAllLine("", 0)
}

function alllist(key) {
	var code = key;
	var text = "";
	var ti = "掌上公交";
	var lineKey = '';
	var onedir = 0;//是否只显示上行
	switch (true) {
		case code === 'jiajiang00119':
			text = "乐山市";
			break;
		case code === 'leshan000119':
			text = "乐山市";
			ti = "实时公交查询";
			onedir = 1;
			break;
		case code === 'ycsanxialvyou':
			text = "宜昌市";
			break;
		case code === 'jmts0001':
			text = "江门市";
			break;
		case code === 'djy040001':
			text = "都江堰市";
			onedir = 1;
			break;
		case code === 'xxpy04070001':
			text = "新乡市";
			lineKey = '平原新区';
			break;
		case code === 'jiangyin0607':
			text = "无锡市";
			lineKey = '江阴';
			break;
		case code === 'shanxian062501':
			text = "菏泽市";
			lineKey = "单县";
			onedir = 1;
			break;
		case code === 'linqu0625001':
			text = "潍坊市";
			lineKey = "临朐";
			onedir = 1;
			break;
		case code === 'fcg240723':
			text = "防城港市";
			//lineKey = "临朐";
			onedir = 1;
			break;
		case code === 'chengwu0817':
			text = "菏泽市";
			lineKey = "成武";
			//onedir = 1;
			break;
		case code === 'juye082903':
			text = "菏泽市";
			lineKey = "巨野";
			onedir = 1;
			break;
		case code === 'sjzjx090123':
			text = "石家庄市";
			//lineKey = "巨野";
			onedir = 1;
			break;
		case code === 'sz10302001':
			text = "松滋市";
			//lineKey = "巨野";
			break;
		case code === 'ems0204001':
			text = "峨眉山市";
			break;
		case code === 'chengwu0817':
			text = "菏泽市";
			lineKey = "成武";
			//onedir = 1;
			break;
		case code === 'lengshuijiang001':
			text = "冷水江市";
			lineKey = "冷水江";
			//onedir = 1;
			break;
		case code === "sy606000":
			text = "顺义公交";
			break;
		case code === "mudanqu051001":
			text = "菏泽市";
			ti = "菏泽牡丹公交";
			break;
		case code === "xijiu06190001":
			text = "遵义市";
			ti = "习酒通勤";
			lineKey = "习酒";
			break;
		case code === "pingbao07050001":
			text = "平顶山市";
			ti = "平宝智慧通勤";
			lineKey = "平宝";
			onedir = 1;
			break;
		case code === "jiangl0001":
			text = "荆州市";
			ti = "江陵公交";
			lineKey = "江陵";
			break;
		case code === "lyjy0314001":
			text = "洛阳市";
			ti = "洛阳交运";
			break;
		case code === "shouxianl0001":
			text = "淮南市";
			ti = "寿县公交";
			lineKey = "寿县";
			break;
		case code === "xiangshan001":
			text = "象山县";
			break;
		case code === "tongguanbus001":
			text = "渭南市";
			lineKey = "潼关";
			break;
		case code === 'shanghe112701':
			text = "济南市";
			lineKey = "商河";
			onedir = 1;
			break;
		default:
			text = "";
			break;
	}
	localStorage.setItem("cityName", text);
	localStorage.setItem("citykey", key);
	$('title').html(ti);
	searchAllLine(lineKey, onedir);

}


function toBusLineDetails(linename, direction) {
	toBusLineDetails2(linename, direction, 0);
}

function toBusLineDetails2(linename, direction, order) {
	linename = encodeURIComponent(encodeURIComponent(linename));
	location.href = showHtml + "?linename=" + linename + "&direction="
		+ direction + "&stationorder=" + order;
}

function getLineDetailsNew(linename, direction, stationorder) {
	// 等车站点
	if (stationorder > 0) {
		selectStationId = stationorder;
	}
	directionTemp = direction;
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var lastpoint = "";
	linename = decodeURIComponent(linename);
	lineNameTemp = linename;
	$('title').html(linename);

	//清理车辆最后位置
	lastBusIndexMap = {};
	initMap = true;
	//临时解决首次进入地图聚焦大小不合适问题
	var mlat = 0.0;
	var mlng = 0.0;
	var distanceMin = 0;
	var myOrder = 0;
	if (stationorder == 0) {// 需要定位站点
		var myLat = sessionStorage.getItem("myLat");
		var myLng = sessionStorage.getItem("myLng");
		try {
			mlat = parseFloat(myLat);
			mlng = parseFloat(myLng);
		} catch (e) {
		}
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "103",
			"CITYNAME": cityname,
			"LINENAME": linename,
			"DIRECTION": direction,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				history.back();
				return;
			}
			swal.close();
			routeInfo = data;
			var stationList = data.data;
			if (stationList.length <= 5 && stationorder == 0) {
				selectStationId = stationList.length;
			}
			stationListTemp = stationList;// 保存站点列表
			var name = data.routeName;
			var commonts = data.commonts;
			var to = "方向：" + stationList[stationList.length - 1].stationName;
			lastpoint = stationList[stationList.length - 1].stationName;
			// var beginTime = data.beginTime;
			// var endTime = data.endTime;
			// if (!beginTime || beginTime.length == 0) {
			// beginTime = "--:--";
			// }
			// if (!endTime || endTime.length == 0) {
			// endTime = "--:--";
			// }
			// 时刻表是否显示
			var showDepart = data.showDepart;
			if (showDepart == 1) {
				$("#depart-btn").attr("style", "display:block");
			} else {
				$("#depart-btn").attr("style", "display:none");
			}
			// 首末班是否显示
			var firstLast = data.firstLast;
			if (firstLast.length == 1) {
				$("#line-info-se").attr("style", "display:flex;width: 100px;");
				$("#beginTime").html(firstLast[0].first);
				$("#endTime").html(firstLast[0].last);
			} else {
				$("#line-info-se").attr("style", "display:none");
			}
			var nihelist = data.nihelist;
			$("#line-title-name").html(name);
			$("#line-dir").html(to);
			$("#commonts").html("");
			$("#commonts1").html("");
			if (commonts.length <= 8) {
				$("#commonts").html(commonts);
			} else {
				$("#commonts1").html(commonts);
			}
			$("#commonts-actions").html(commonts);
			// 默认速度
			var speedList = "<div class='station-speed' id='station-speed'>";
			for (var i = 0; i < stationList.length - 1; i++) {
				speedList += "<div class='item-speed-green'></div>";
			}
			speedList += "</div>";
			var html = "<div class='station-list-scroll' id='scroll-view'>";
			html += speedList;
			html += "<ul class='station-list'>";

			for (var i = 0; i < stationList.length; i++) {
				var station = stationList[i];
				var dirImg = "";
				var order = station.stationOrder;
				var icon;
				var marker;
				var zooms;
				var zoomStyleMapping1;
				var positions = [station.station_lon, station.station_lat];
				if (i == 0) {// 首站
					dirImg = "<img src='images/icon_index_start.png'>";

				} else if (i == stationList.length - 1) {// 末站
					html += "<li>";
					html += "<div class='station-bus-num' id='busnum1_";
					html += order;
					html += "'></div>";
					html += "<div class='station-bus-img' id='busimg1_";
					html += order;
					html += "'></div>";
					html += "</li>";
					dirImg = "<img src='images/icon_index_end.png'>";

				} else {
					html += "<li>";
					html += "<div class='station-bus-num' id='busnum1_";
					html += order;
					html += "'></div>";
					html += "<div class='station-bus-img' id='busimg1_";
					html += order;
					html += "'></div>";
					html += "</li>";
					dirImg = "<img src='images/line_icon_arrow.png'>";
				}
				///html += "<li onclick='stationSelect(" + order + ")'>";
				html += "<li onclick='lineStationChange(" + order + ")'>";
				html += "<div class='station-bus-num' id='busnum_";
				html += order;
				html += "'></div>";
				html += "<div class='station-bus-img' id='busimg_";
				html += order;
				html += "'></div>";
				html += "<div class='station-dir-img' id='station_dir_";
				html += order;
				html += "'>";
				html += dirImg;
				html += "</div>";
				html += "<div class='station-order' id='station_order_";
				html += order;
				html += "'>";
				html += order;
				html += "</div>";
				html += "<div class='station-name' id='station_name_";
				html += order;
				html += "'>";
				html += station.showName;
				if (station.stationsStatus != 1) {
					var status;
					switch (station.stationsStatus) {
						case 0:
							status = "临时不停靠";
							break;
						case 2:
							status = "只上不下";
							break;
						case 3:
							status = "只下不上";
							break;
						case 4:
							status = "临时停靠";
							break;
						case 5:
							status = "响应式停靠";
							break;
						case 6:
							status = "响铃式停靠";
							break;
						case 7:
							status = "招手即停";
							break;
						case 8:
							status = "大站车停靠";
							break;
						default:
							status = ""
					}
					if (status.length > 0) {
						html += "<div class='station-status'>";
						html += status;
						html += "</div>"
					}
				}
				html += "</div></li>";

				if (mlng > 0 && mlat > 0) {
					var distanceTemp = getFlatternDistance(mlat, mlng, station.station_lat, station.station_lon);
					if (distanceMin == 0) {
						distanceMin = distanceTemp;
						myOrder = 1;
					} else {
						if (distanceTemp < distanceMin) {
							distanceMin = distanceTemp;
							myOrder = (i + 1);
						}
					}
				}
			}

			if (stationorder == 0) {
				if (myOrder > 1) {
					selectStationId = myOrder;
				} else {
					selectStationId = 5;
					if (stationList.length <= 5) {
						selectStationId = stationList.length;
					}
				}
			}
			html += "</ul></div>";
			$("#station-info").html(html);
			// 选择站点
			if (selectStationId > 6) {
				$("#scroll-view").scrollLeft((selectStationId - 6) * 60 + 100);
			}
			swal.close();
			// 保存历史查询
			addHistory(1, linename, direction, lastpoint, "", "", "");
			getRealTime(linename, direction);
		},
		error: function(data) {
			reqError();
		}
	});
	linename = decodeURIComponent(linename);
	var linelist = [];
	var lineinfo = {
		lineName: linename,
		direction: direction,
	}
	linelist.push(lineinfo);
	getLineSpecialNoticeByLines(linelist);
}

function showMapStation() {
	// 清理线路轨迹点
	if (map && polyline) {
		polyline.setMap(null);
	}
	// 清理地图站点标注
	if (map && stationMks) {
		map.remove(stationMks);
	}
	if (!infoWindow && map) {
		infoWindow = new AMap.InfoWindow();
	}
	stationMks = [];
	var zoomStyleMapping1 = {
		3: 0,
		4: 0,
		5: 0,
		6: 0,
		7: 0,
		8: 0,
		9: 0,
		10: 0,
		11: 0,
		12: 0,
		13: 0,
		14: 0,
		15: 0,
		16: 0,
		17: 0,
		18: 0,
		19: 0,
		20: 0
	}
	var zoomStyleMapping2 = {
		13: 0,
		14: 0,
		15: 0,
		16: 0,
		17: 0,
		18: 0,
		19: 0,
		20: 0
	}
	var stationList = routeInfo.data;
	for (var i = 0; i < stationList.length; i++) {
		var station = stationList[i];
		var icon;
		var marker;
		var zooms;
		var zoomStyleMapping1;
		var positions = [station.station_lon, station.station_lat];
		if (i == 0) {// 首站
			// 地图站点图标
			icon = {
				img: "images/icon_qidian.png",
				size: [24, 24],//可见区域的大小
				anchor: 'center',//锚点
				fitZoom: 13,//最合适的级别
				scaleFactor: 0,//地图放大一级的缩放比例系数
				maxScale: 1,//最大放大比例
				minScale: 1//最小放大比例
			};
			zooms = [3, 20];
			zoomStyleMapping = zoomStyleMapping1;
		} else if (i == stationList.length - 1) {// 末站
			// 地图站点图标
			icon = {
				img: "images/icon_zhongdian.png",
				size: [24, 24],//可见区域的大小
				anchor: 'center',//锚点
				fitZoom: 13,//最合适的级别
				scaleFactor: 1,//地图放大一级的缩放比例系数
				maxScale: 1,//最大放大比例
				minScale: 1//最小放大比例
			}
			zooms = [3, 20];
			zoomStyleMapping = zoomStyleMapping1;
		} else if ((i + 1) == selectStationId) {// 选中站点
			// 地图站点图标
			icon = {
				img: "images/direction_bus_list_target@2x.png",
				size: [24, 24],//可见区域的大小
				anchor: 'center',//锚点
				fitZoom: 13,//最合适的级别
				scaleFactor: 1,//地图放大一级的缩放比例系数
				maxScale: 1,//最大放大比例
				minScale: 1//最小放大比例
			}
			zooms = [3, 20];
			zoomStyleMapping = zoomStyleMapping1;
		} else {
			icon = {
				img: "images/busstation_normal.png",
				size: [16, 16],//可见区域的大小
				imageSize: [16, 16],
				anchor: 'center',//锚点
				fitZoom: 13,//最合适的级别
				scaleFactor: 1,//地图放大一级的缩放比例系数
				maxScale: 0,//最大放大比例
				minScale: 0//最小放大比例
			}
			zooms = [10, 20];
			zoomStyleMapping = zoomStyleMapping2;
		}

		marker = new AMap.ElasticMarker({
			position: positions,
			map: map,
			zooms: zooms,
			styles: [{
				icon: icon,
				label: {
					content: (i + 1) + "." + station.stationName,
					position: 'BM',
					minZoom: 15
				}
			}],
			zoomStyleMapping: zoomStyleMapping
		});
		stationMks.push(marker);
	}

	//轨迹点
	var nihelist = routeInfo.nihelist;
	if (nihelist) {
		lineArr = [];
		for (var j = 0; j < nihelist.length; j++) {
			var p = [nihelist[j].lng, nihelist[j].lat];
			lineArr.push(p);
		}
	}
	var linecolor = "#64c832";
	polyline = new AMap.Polyline({
		path: lineArr, // 设置线覆盖物路径
		strokeColor: linecolor, // 线颜色
		strokeOpacity: 1, // 线透明度
		strokeWeight: 8, // 线宽
		strokeStyle: "solid", // 线样式
		strokeDasharray: [10, 5],
		showDir: true,
		lineJoin: 'round',
		// 补充线样式
	});
	polyline.setMap(map);
}

function showMapBusMarker() {
	if (!realTimeInfo) {
		return;
	}
	if (map && busMks) {
		map.remove(busMks);
	}
	if (!infoWindow && map) {
		infoWindow = new AMap.InfoWindow();
	}
	var nihelist = routeInfo.nihelist;
	latestBusMarkerId = -1;
	var busInfoList = realTimeInfo.list;
	AMap.plugin('AMap.MoveAnimation', function() {//加载动画插件
		// 地图车辆标注
		for (var i = 0; i < busInfoList.length; i++) {
			var bus = busInfoList[i];
			var positions = [bus.bus_lng, bus.bus_lat];
			var npi = bus.nihePointIndex;//拟合点序号
			if (npi < 0) {
				npi = 0;
			}
			positions = [parseFloat(nihelist[npi].lng), parseFloat(nihelist[npi].lat)];
			var images;
			if (latestBusName == bus.busNumber) {//最近车辆
				latestBusMarkerId = i;
				images = "images/bus1@2x.png";
			} else {
				images = "images/bus_notcare1@2x.png";
			}
			var icon = new AMap.Icon({
				imageSize: new AMap.Size(18, 36), // 图标大小
				image: images,
			});
			var marker = new AMap.Marker({
				offset: new AMap.Pixel(-9, -18),
				icon: icon,
				map: map,
				angle: 90 - busInfoList[i].angle,
				position: positions
			});
			marker.content = bus.busNumber + "<br>开往" + bus.stationName;
			marker.on('click', markerClick);
			busMks.push(marker);
			if (lastBusIndexMap[bus.busNumber]) {
				var movePath = getMovePath(nihelist, lastBusIndexMap[bus.busNumber], npi);
				if (movePath && movePath.length > 0) {
					marker.moveAlong(movePath, {
						// 每一段的时长
						duration: 1000,//可根据实际采集时间间隔设置
						// JSAPI2.0 是否延道路自动设置角度在 moveAlong 里设置
						autoRotation: true,
					});
				}
			}
			lastBusIndexMap[bus.busNumber] = npi;
		}
	});
}

function searchLine(linename, direction, stationorder) {
	showLoader();
	var myLat = sessionStorage.getItem("myLat");
	if (myLat && myLat != "") {
		getLineDetailsNew(linename, direction, stationorder);
		return;
	}
	if (!map) {
		map = new AMap.Map('container', {
			resizeEnable: true
		});
	}
	AMap.plugin('AMap.Geolocation', function() {
		var geolocation = new AMap.Geolocation({
			enableHighAccuracy: true,// 是否使用高精度定位，默认:true
			timeout: 5000, // 超过10秒后停止定位，默认：5s
			buttonPosition: 'RB', // 定位按钮的停靠位置
			// buttonOffset : new AMap.Pixel(10, 20),//
			// 定位按钮与设置的停靠位置的偏移量，默认：Pixel(10,
			// 20)
			// zoomToAccuracy : true, // 定位成功后是否自动调整地图视野到定位点

		});
		map.addControl(geolocation);
		geolocation.getCurrentPosition(function(status, result) {
			if (status == 'complete') {
				// var cityname = localStorage.getItem("cityName");
				// if (!cityname) {
				// cityInfo(result)
				// return;
				// }
				// onComplete2(result)
				var myLat = result.position.lat;
				var myLng = result.position.lng
				sessionStorage.setItem("myLat", myLat);
				sessionStorage.setItem("myLng", myLng);
				getLineDetailsNew(linename, direction, stationorder);
				console.log(result);
			} else {
				getLineDetailsNew(linename, direction, stationorder);
				console.log(result);
			}
		});
	});
}

function getLineSpecialNotice(linename, direction) {
	specialText = "";
	var cityname = localStorage.getItem("cityName");
	linename = decodeURIComponent(linename);
	var linelist = [];
	var lineinfo = {
		lineName: linename,
		direction: direction,
	}
	linelist.push(lineinfo);
	lineliststr = JSON.stringify(linelist);
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "120",
			"CITYNAME": cityname,
			"LINELIST": lineliststr,
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				return;
			}
			var speciallist = data.info;
			if (speciallist && speciallist.length > 0) {
				var text = "";
				$("#special-notice").removeClass("special-hide");
				for (var i = 0; i < speciallist.length; i++) {
					var special = speciallist[i];
					text += special.text;
					text += "<br>";
				}
			}
			specialText = text;
			$("#special-text").html(text);

		},
		error: function(data) {
			reqError();
		}
	});
}

function getLineSpecialNotice(linename, direction) {
	specialText = "";
	var cityname = localStorage.getItem("cityName");
	linename = decodeURIComponent(linename);
	var linelist = [];
	var lineinfo = {
		lineName: linename,
		direction: direction,
	}
	linelist.push(lineinfo);
	lineliststr = JSON.stringify(linelist);
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "120",
			"CITYNAME": cityname,
			"LINELIST": lineliststr,
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				return;
			}
			var speciallist = data.info;
			if (speciallist && speciallist.length > 0) {
				var text = "";
				$("#special-notice").removeClass("special-hide");
				for (var i = 0; i < speciallist.length; i++) {
					var special = speciallist[i];
					text += special.text;
					text += "<br>";
				}
			}
			specialText = text;
			$("#special-text").html(text);

		},
		error: function(data) {
			reqError();
		}
	});
}

function getLineSpecialNoticeByLines(linelist) {
	if (!linelist || linelist.length == 0) {
		return;
	}
	specialText = "";
	var cityname = localStorage.getItem("cityName");
	//linename = decodeURIComponent(linename);
	lineliststr = JSON.stringify(linelist);
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "120",
			"CITYNAME": cityname,
			"LINELIST": lineliststr,
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				return;
			}
			var speciallist = data.info;
			var lastline = "";
			if (speciallist && speciallist.length > 0) {
				var text = "";
				$("#special-notice").addClass("special-notice");
				$("#special-notice").removeClass("special-hide");
				var reg = new RegExp("\n", 'g');
				if (linelist.length == 1) {
					var special = speciallist[0];
					text += special.text.replace(reg, "<br/>");
					text += "<br>";
				} else {
					for (var i = 0; i < speciallist.length; i++) {
						var special = speciallist[i];
						if (!lastline || lastline != special.lineName) {
							text += special.lineName + "：";
						}
						text += special.text.replace(reg, "<br/>");
						text += "<br>";
						if (i < (speciallist.length - 1)) {
							text += "<br>";
						}
						lastline = special.lineName;
					}
				}

			}
			specialText = text;
			$("#special-text").html(text);

		},
		error: function(data) {
			reqError();
		}
	});
}

function showSpecial() {
	if (!specialText || specialText.length == 0) {
		return;
	}
	$("#zhe-content").removeClass("special-hide");
	$("#zhe").removeClass("special-hide");
	$("#zhe-list").html(specialText);

}

function hideSpecial() {
	$("#zhe-content").addClass("special-hide");
	$("#zhe").addClass("special-hide");
}


function directionChange() {
	if (routeInfo && routeInfo.loopType == "0") {
		swal({
			title: "",
			text: "此为单向线路，没有反向",
			type: "warning",
			timer: 2000,
			showConfirmButton: false
		});
		return;
	}
	if (directionTemp == 1) {
		directionTemp = 2;
	} else {
		directionTemp = 1;
	}
	getLineDetailsNew(lineNameTemp, directionTemp, 0);
}

function showCommonts() {
	//	var commonts = $("#commonts").html();
	// swal("Here's a message!", "It's pretty, isn't it?");
	//swal(commonts);
}
function showCommonts1() {
	//var commonts = $("#commonts1").html();
	// swal("Here's a message!", "It's pretty, isn't it?");
	//swal(commonts);
}

function getRealTime(linename, direction) {
	if (realTimeThread) {
		clearTimeout(realTimeThread);
	}
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "104",
			"CITYNAME": cityname,
			"LINENAME": linename,
			"DIRECTION": direction,
			"STATIONORDER": selectStationId,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				// swal({
				// title : "",
				// text : data.msg,
				// type : "error",
				// timer : 2000,
				// showConfirmButton : false
				// });
				return;
			}
			realTimeInfo = data;// 保存动态车辆数据
			// 判断是否有计划发车
			if (realTimeInfo.planTime) {
				var planstr = "起点预计发车：" + realTimeInfo.planTime;
				if (citykey === "leshan000119") {
					if (realTimeInfo.routeName == "304路") {
						planstr += "（站点调整中，信息仅供参考）"
					} else {
						planstr += "（智能调度，仅供参考）";
					}
				}
				$("#line-plantime").html(planstr);
			} else {
				$("#line-plantime").html("");
			}
			// showRealTimeInfo();
			stationSelect(selectStationId);
		},
		error: function(data) {
			reqError();
		}
	});
	realTimeThread = setTimeout("getRealTime('" + linename + "','" + direction + "')", 10000);
}

function showRealTimeInfo() {
	if (!realTimeInfo) {
		return;
	}
	latestBusName = null;
	var html = "";
	// 判断线路是否停运
	if (realTimeInfo.runState == 1) {
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		html += "该线路已停运</div></div>";
		$("#realtime-info").html(html);
		return;
	}
	// 判断线路是否开通实时查询
	if (realTimeInfo.hasReal == 0) {
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		html += "该线路未开通实时数据查询</div></div>";
		$("#realtime-info").html(html);
		return;
	}
	var citykey = getKEY();
	if (citykey == "djy040001") {
		return;
	}
	// 清除到站状态
	for (var i = 0; i < stationListTemp.length; i++) {
		var stationId = i + 1;
		if (i > 0) {// 首站没有开往状态
			$("#busnum1_" + stationId).html("");
			$("#busimg1_" + stationId).html("");
		}
		$("#busnum_" + stationId).html("");
		$("#busimg_" + stationId).html("");
	}
	// 每个站点到离站信息
	var data = realTimeInfo.data;
	for (var i = 0; i < data.length; i++) {
		var bus = data[i];
		var order = bus.index;
		var stationId = order + 1;
		if (stationListTemp[order].stationOrder != (data[i].index + 1)) {
			continue;
		}
		var arrive = "";// 展示到达车辆数据
		var come = "";// 展示开往车辆数据
		var arriveimg = "";
		var comeimg = "";
		if (bus.arrive > 0) {// 是否有到站
			arriveimg = "<img src='images/icon_bus_daoda.png'>";
		}
		if (bus.come > 0) {// 是否有开往
			comeimg = "<img src='images/icon_bus_daoda.png'>";
		}
		if (bus.arrive > 1) {// 多辆显示车辆数
			arrive = bus.arrive + "辆";
		}
		if (bus.come > 1) {// 多辆显示车辆数
			come = bus.come + "辆";
		}
		if (order > 0) {// 首站没有开往状态
			$("#busnum1_" + stationId).html(come);
			$("#busimg1_" + stationId).html(comeimg);
		}
		$("#busnum_" + stationId).html(arrive);
		$("#busimg_" + stationId).html(arriveimg);
	}

	// 展示等车站最近车辆信息
	var speedList = realTimeInfo.speedlist;
	var busInfoList = realTimeInfo.list;
	var disList = realTimeInfo.dislist;
	var stationTem = stationListTemp;
	var station = stationTem[selectStationId - 1];
	var nihelist = routeInfo.nihelist;

	var first = -1; // 最近车辆
	var next = -1; // 第二辆
	var dis1 = 0; // 最近车辆距离
	var dis2 = 0; // 第二辆距离
	var t1 = 0; // 最近车辆时间
	var t2 = 0; // 第二辆时间
	var disstr1 = ""; // 最近车辆描述
	var disstr2 = ""; // 第二辆描述
	var tstr1 = ""; // 最近车时间描述
	var tstr2 = ""; // 第二辆时间描述
	var a1 = 0; // 最近是否到站状态
	var a2 = 0; // 第二是否到站状态
	var sta = 0; // 最近车辆站点
	var count = 0;

	var previousBusNum = -999;//上一班车距离

	//习酒通勤显示车辆
	if (citykey == "xijiu101401") {
		for (var I = 0; I < busInfoList.length; I++) {
			var bus = busInfoList[I];
			var z = bus.index + 1;

			var busK = 0;
			if (bus.statusType == "0") {
				busK = "busnum_" + z;
			} else {
				busK = "busnum1_" + z;
			}
			console.log(bus.busNumber);
			$("#" + busK).html(bus.busNumber);
		}
	}

	for (var i = 0; i < busInfoList.length; i++) {
		var businfo = busInfoList[i];
		var order = businfo.index + 1;
		// 判断车辆是否在等车站前面
		if (order <= selectStationId) {
			sta = order;
			var num = selectStationId - order;
			if (businfo.statusType != "0") {
				num += 1;
			}
			var tempdis = 0;// 车辆到等车站距离
			if (first == -1) {
				first = num;
				dis1 = getDistance(businfo, disList, selectStationId);
				t1 = getCostTimes(businfo, disList, speedList, selectStationId);
				if (businfo.statusType == "0") {
					a1 = 1;
				}
				latestBusName = businfo.busNumber;
			} else {
				if (businfo.statusType == "0") { // 到站的直接赋值为最近站
					next = first;
					dis2 = dis1;
					a2 = a1;
					t2 = t1;
					first = num;
					dis1 = getDistance(businfo, disList, selectStationId);
					a1 = 1;
					t1 = getCostTimes(businfo, disList, speedList, selectStationId);
					latestBusName = businfo.busNumber;
				} else { // 第二辆开往的判断是否比较近 较近的为最近站
					tempdis = getDistance(businfo, disList, selectStationId);
					var timeTemp = getCostTimes(businfo, disList, speedList, selectStationId);
					if (tempdis < dis1) {
						next = first;
						dis2 = dis1;
						a2 = a1;
						t2 = t1;
						first = num;
						dis1 = tempdis;
						a1 = 0;
						t1 = timeTemp;
						latestBusName = businfo.busNumber;
					}
				}
			}
		} else {
			previousBusNum = (order - selectStationId);
			break;
		}
	}
	if (dis1 != 0) {
		if (dis1 < 300) {
			tstr1 = "小于1分钟";
		} else {
			//t1 = Math.ceil(dis1 / busSpeed);
			//tstr1 = t1 + "分钟";
			tstr1 = getTimeStr(t1);
		}
		if (dis1 >= 1000) {
			dis1 = (dis1 / 1000).toFixed(1);
			disstr1 = dis1 + "公里"
		} else {
			dis1 = dis1.toFixed(0);
			disstr1 = dis1 + "米"
		}
		count++;
	}
	if (dis2 != 0) {
		if (dis2 < 300) {
			tstr2 = "小于1分钟";
		} else {
			//	t2 = Math.ceil(dis2 / busSpeed);
			//	tstr2 = t2 + "分钟";
			tstr2 = getTimeStr(t2);
		}
		if (dis2 >= 1000) {
			dis2 = (dis2 / 1000).toFixed(1);
			disstr2 = dis2 + "公里"
		} else {
			dis2 = dis2.toFixed(0);
			disstr2 = dis2 + "米"
		}
		count = 2;
	}
	var routeOnStationRTimeInfoList = realTimeInfo.routeOnStationRTimeInfoList;
	if (routeOnStationRTimeInfoList && routeOnStationRTimeInfoList[0].busToStationTips != "等待发车") {
		// 最近车辆动态数据
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		html += routeOnStationRTimeInfoList[0].busToStationTips;
		html += "</div>";
		if (routeOnStationRTimeInfoList[0].busToStationTimeTips && routeOnStationRTimeInfoList[0].busToStationDistanceTips != "0米") {
			html += "<div class='real-info-time'>";
			html += routeOnStationRTimeInfoList[0].busToStationTimeTips;
			html += " / ";
			html += routeOnStationRTimeInfoList[0].busToStationDistanceTips;
			html += "</div>";
		}
		html += "</div>";
		if (routeOnStationRTimeInfoList[1]) {
			// 最近车辆动态数据
			html += "<div class='real-info-details'>";
			html += "<div class='real-info-dis'>";
			html += routeOnStationRTimeInfoList[1].busToStationTips;
			html += "</div>";
			if (routeOnStationRTimeInfoList[1].busToStationTimeTips && routeOnStationRTimeInfoList[1].busToStationDistanceTips != "0米") {
				html += "<div class='real-info-time'>";
				html += routeOnStationRTimeInfoList[1].busToStationTimeTips;
				html += " / ";
				html += routeOnStationRTimeInfoList[1].busToStationDistanceTips;
				html += "</div>";
			}
			html += "</div>";
		}
	} else {
		// 最近车辆动态数据
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		if (first == -1) {
			if (realTimeInfo.planTime && realTimeInfo.planTime.length > 0) {
				html += "预计" + realTimeInfo.planTime + "发车";
			} else {
				html += "等待发车";
			}
			if (previousBusNum != -999 && previousBusNum > 0) {
				tstr1 = "上一辆已过" + previousBusNum + "站"
			}
		} else if (first == 0) {
			html += '已到站';
		} else if (first == 1 && a1 == 0) {
			html += '即将到站';
		} else {
			html += first + '站';
		}
		html += "</div>";
		if (tstr1 != "") {
			html += "<div class='real-info-time'>";
			html += tstr1;
			if (disstr1 != "") {
				html += " / ";
				html += disstr1;
			}
			html += "</div>";
		}
		html += "</div>";
		if (count > 1) {
			html += "<div class='real-info-details2'>";
			html += "<div class='real-info-dis'>";
			if (next == 0) {
				html += '已到站';
			} else if (next == 1 && a2 == 0) {
				html += '即将到站';
			} else {
				html += next + '站';
			}
			html += "</div>";
			if (tstr2 != "") {
				html += "<div class='real-info-time'>";
				html += tstr2;
				html += " / ";
				html += disstr2;
				html += "</div>";
			}
			html += "</div>";
		}
	}

	$("#realtime-info").html(html);
	// 站点速度
	var speedhtml = "";
	for (var i = 0; i < speedList.length; i++) {
		var co = speedList[i].co;
		speedhtml += "<div class='item-speed-" + co + "'></div>";
	}
	if (speedhtml != "") {
		$("#station-speed").html(speedhtml);
	}
	if (mapView) {//地图模式时展示
		showMapBusMarker();
		showMapStation();
	}

}

function getMovePath(nihelist, startIndex, endIndex) {
	var movePath = [];
	if (startIndex >= endIndex) {
		return movePath;
	}
	for (var i = startIndex; i <= endIndex; i++) {
		var positions = [parseFloat(nihelist[i].lng), parseFloat(nihelist[i].lat)];
		movePath.push(positions);
	}
	return movePath;
}

function showRealTimeInfoBak() {
	if (!realTimeInfo) {
		return;
	}
	if (map && busMks) {
		map.remove(busMks);
	}
	if (!infoWindow && map) {
		infoWindow = new AMap.InfoWindow();
	}
	var html = "";
	// 判断线路是否停运
	if (realTimeInfo.runState == 1) {
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		html += "该线路已停运</div></div>";
		$("#realtime-info").html(html);
		return;
	}
	// 判断线路是否开通实时查询
	if (realTimeInfo.hasReal == 0) {
		html += "<div class='real-info-details'>";
		html += "<div class='real-info-dis'>";
		html += "该线路未开通实时数据查询</div></div>";
		$("#realtime-info").html(html);
		return;
	}

	// 清除到站状态
	for (var i = 0; i < stationListTemp.length; i++) {
		var stationId = i + 1;
		if (i > 0) {// 首站没有开往状态
			$("#busnum1_" + stationId).html("");
			$("#busimg1_" + stationId).html("");
		}
		$("#busnum_" + stationId).html("");
		$("#busimg_" + stationId).html("");
	}
	// 每个站点到离站信息
	var data = realTimeInfo.data;
	for (var i = 0; i < data.length; i++) {
		var bus = data[i];
		var order = bus.index;
		var stationId = order + 1;
		if (stationListTemp[order].stationName != data[i].stationName) {
			continue;
		}
		var arrive = "";// 展示到达车辆数据
		var come = "";// 展示开往车辆数据
		var arriveimg = "";
		var comeimg = "";
		if (bus.arrive > 0) {// 是否有到站
			arriveimg = "<img src='images/icon_bus_daoda.png'>";
		}
		if (bus.come > 0) {// 是否有开往
			comeimg = "<img src='images/icon_bus_daoda.png'>";
		}
		if (bus.arrive > 1) {// 多辆显示车辆数
			arrive = bus.arrive + "辆";
		}
		if (bus.come > 1) {// 多辆显示车辆数
			come = bus.come + "辆";
		}
		if (order > 0) {// 首站没有开往状态
			$("#busnum1_" + stationId).html(come);
			$("#busimg1_" + stationId).html(comeimg);
		}
		$("#busnum_" + stationId).html(arrive);
		$("#busimg_" + stationId).html(arriveimg);
	}
	// 展示等车站最近车辆信息
	var speedList = realTimeInfo.speedlist;
	var busInfoList = realTimeInfo.list;
	var disList = realTimeInfo.dislist;
	var stationTem = stationListTemp;
	var station = stationTem[selectStationId - 1];
	var first = -1; // 最近车辆
	var next = -1; // 第二辆
	var dis1 = 0; // 最近车辆距离
	var dis2 = 0; // 第二辆距离
	var t1 = 0; // 最近车辆时间
	var t2 = 0; // 第二辆时间
	var disstr1 = ""; // 最近车辆描述
	var disstr2 = ""; // 第二辆描述
	var tstr1 = ""; // 最近车时间描述
	var tstr2 = ""; // 第二辆时间描述
	var a1 = 0; // 最近是否到站状态
	var a2 = 0; // 第二是否到站状态
	var sta = 0; // 最近车辆站点
	var count = 0;
	for (var i = 0; i < busInfoList.length; i++) {
		var businfo = busInfoList[i];
		var order = businfo.index + 1;
		// 判断车辆是否在等车站前面
		if (order <= selectStationId) {
			sta = order;
			var num = selectStationId - order;
			if (businfo.statusType != "0") {
				num += 1;
			}
			var tempdis = 0;// 车辆到等车站距离
			if (first == -1) {
				first = num;
				dis1 = getDistance(businfo, disList, selectStationId);
				if (businfo.statusType == "0") {
					a1 = 1;
				}
			} else {
				if (businfo.statusType == "0") { // 到站的直接赋值为最近站
					next = first;
					dis2 = dis1;
					a2 = a1;
					first = num;
					dis1 = getDistance(businfo, disList, selectStationId);
					a1 = 1;
				} else { // 第二辆开往的判断是否比较近 较近的为最近站
					tempdis = getDistance(businfo, disList, selectStationId);
					if (tempdis < dis1) {
						next = first;
						dis2 = dis1;
						a2 = a1;
						first = num;
						dis1 = tempdis;
						a1 = 0;
					}
				}
			}
		} else {
			break;
		}
	}
	if (dis1 != 0) {
		if (dis1 < 300) {
			tstr1 = "小于1分钟";
		} else {
			t1 = Math.ceil(dis1 / busSpeed);
			tstr1 = t1 + "分钟";
		}
		if (dis1 >= 1000) {
			dis1 = (dis1 / 1000).toFixed(1);
			disstr1 = dis1 + "km"
		} else {
			dis1 = dis1.toFixed(0);
			disstr1 = dis1 + "m"
		}
		count++;
	}
	// 最近车辆动态数据
	html += "<div class='real-info-details'>";
	html += "<div class='real-info-dis'>";
	if (first == -1) {
		html += "等待发车";
	} else if (first == 0) {
		html += '已到站';
	} else if (first == 1 && a1 == 0) {
		html += '即将到站';
	} else {
		html += first + '站';
	}
	html += "</div>";
	if (tstr1 != "") {
		html += "<div class='real-info-time'>";
		html += tstr1;
		html += " / ";
		html += disstr1;
		html += "</div>";
	}
	html += "</div>";

	if (dis2 != 0) {
		if (dis2 < 300) {
			tstr2 = "小于1分钟";
		} else {
			t2 = Math.ceil(dis2 / busSpeed);
			tstr2 = t2 + "分钟";
		}
		if (dis2 >= 1000) {
			dis2 = (dis2 / 1000).toFixed(1);
			disstr2 = dis2 + "km"
		} else {
			dis2 = dis2.toFixed(0);
			disstr2 = dis2 + "m"
		}
		count = 2;
	}

	if (count > 1) {
		html += "<div class='real-info-details2'>";
		html += "<div class='real-info-dis'>";
		if (next == 0) {
			html += '已到站';
		} else if (next == 1 && a2 == 0) {
			html += '即将到站';
		} else {
			html += next + '站';
		}
		html += "</div>";
		if (tstr2 != "") {
			html += "<div class='real-info-time'>";
			html += tstr2;
			html += " / ";
			html += disstr2;
			html += "</div>";
		}
		html += "</div>";
	}
	$("#realtime-info").html(html);
	// 地图车辆标注
	for (var i = 0; i < busInfoList.length; i++) {
		var positions = [busInfoList[i].bus_lng, busInfoList[i].bus_lat];
		var icon = new AMap.Icon({
			imageSize: new AMap.Size(20, 20), // 图标大小
			image: "images/icon_bus.png",
		});
		var marker = new AMap.Marker({
			offset: new AMap.Pixel(-10, -10),
			icon: icon,
			map: map,
			position: positions
		});
		marker.content = busInfoList[i].busNumber + "<br>开往" + busInfoList[i].stationName;
		marker.on('click', markerClick);
		busMks.push(marker);
	}
	// 站点速度
	var speedhtml = "";
	for (var i = 0; i < speedList.length; i++) {
		var co = speedList[i].co;
		speedhtml += "<div class='item-speed-" + co + "'></div>";
	}
	if (speedhtml != "") {
		$("#station-speed").html(speedhtml);
	}
}

markerClick = function(e) {
	infoWindow.setContent(e.target.content);
	infoWindow.open(map, e.target.getPosition());
}

function getDistance(bus, disList, selectID) {
	var dis = 0;
	try {
		dis = bus.busToStationNiheDistance;
		var s = bus.index;
		var e = selectID - 2;
		for (var i = s; i <= e; i++) {
			dis += disList[i].d;
		}
	} catch (e) {
		dis = -1;
	}
	return dis;
}

function getCostTimes(bus, disList, speedList, selectID) {
	var time = 0;
	try {
		var s = bus.index;
		var e = selectID - 2;
		dis = bus.busToStationNiheDistance;

		if (bus.statusType != "0") {
			time = dis / (speedList[s - 1].speed * 1.0);
		}
		for (var i = s; i <= e; i++) {
			if (bus.statusType == "0" && i == s) {
				time += disList[i].d / (speedList[i].speed * 1.0);
			} else {
				//time += (disList[i].d / speedList[i].speed) + 0.5;// 0.5分钟为每停靠一个站所需的时间
				time += (disList[i].d / (speedList[i].speed * 1.0)) + 0.5;// 0.5分钟为每停靠一个站所需的时间
			}
		}
	} catch (e) {
		time = -1;
	}
	return time;
}

function getTimeStr(min) {
	if (min < 1) {
		return "小于1分钟";
	}
	if (min < 60) {
		return Math.round(min) + "分钟";
	}
	return Math.round(min / 60) + "小时" + Math.round((min % 60)) + "分钟";
}

function toStationLineV2(stationname, lat, lng) {
	stationname = encodeURIComponent(encodeURIComponent(stationname));
	location.href = "station.html?stationname=" + stationname + "&lat=" + lat + "&lng=" + lng;
}

function toStationLine(stationname) {
	stationname = encodeURIComponent(encodeURIComponent(stationname));
	location.href = "station.html?stationname=" + stationname;
}

function getLineByStation(stationname) {
	stationname = decodeURIComponent(stationname);
	stationTemp = stationname;
	$('title').html(stationname);
	$('#line-title-name').html(stationname);
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "105",
			"CITYNAME": cityname,
			"STATIONNAME": stationname,
			"MYLAT": '',
			"MYLNG": '',
			"CITYKEY": citykey
		},
		success: function(data) {
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var lineList = data.data;
			var html = "";
			for (var i = 0; i < lineList.length; i++) {
				html += "<div class='station-line'";
				html += "onclick=\"toBusLineDetails('";
				html += lineList[i].lineName;
				html += "','";
				html += lineList[i].upperOrDown;
				html += "')\">";
				html += "<div>";
				html += lineList[i].lineName;
				html += "<span>方向   ";
				html += lineList[i].to;
				html += "</span>";
				html += "</div>";
				html += "<div class='station-line-real'>";
				html += lineList[i].neartext;
				html += "</div></div>";
			}
			$("#station-line-list").html(html);
			addHistory(2, stationname, '', '', '', '', '');
		},
		error: function(data) {
			reqError();
		}
	});
}

function getLineByStationV2(stationname, lat, lng) {
	if (realTimeThread) {
		clearTimeout(realTimeThread)
	}
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "115",
			"CITYNAME": cityname,
			"STATIONNAME": stationname,
			"MYLAT": lat,
			"MYLNG": lng,
			"ALL": 1,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return
			}
			var html = "";
			var linelist = data.data;
			for (var d = 0; d < linelist.length; d++) {
				var staline = linelist[d];
				html += '<div class="line-item"';
				html += " onclick=\"toBusLineDetails2('";
				html += staline.lineName;
				html += "','";
				html += staline.upperOrDown;
				html += "','";
				html += staline.stationOrder;
				html += "')\">";
				html += '<div class="near-list-view-left">';
				html += '<div class="near-list-line">';
				html += staline.lineName;
				html += "</div>";
				if (cityname == "习酒通勤") {
					html += '<div class="near-list-dir2">方向 ';
				} else {
					html += '<div class="near-list-dir">方向 ';
				}
				html += staline.to;
				html += "</div>";
				html += "</div>";
				html += '<div class="near-list-view-right">';
				if (MZTstyle) {
					html += '<div class="near-list-num_mzt">'
				} else {
					html += '<div class="near-list-num">'
				}
				html += staline.neartext;
				html += "</div>";
				html += '<div class="near-list-dir">';
				html += staline.neardis;
				html += "</div>";
				html += "</div>";
				html += "</div>"
			}
			$("#station-line-list").html(html)
		},
		error: function(data) {
			reqError()
		}
	});
	realTimeThread = setTimeout("getLineByStationV2('" + stationname + "','" + lat + "','" + lng + "')", 15000)
}

function searchStation(stationname, lat, lng) {
	stationname = decodeURIComponent(stationname);
	stationTemp = stationname;
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "209",
			"CITYNAME": cityname,
			"STATIONNAME": stationname,
			"MYLAT": lat,
			"MYLNG": lng,
			"LAT": lat,
			"LNG": lng,
			"CITYKEY": citykey
		},
		success: function(data) {
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return
			}
			console.log(data);
			if (!data.info || data.info.length < 1) {
				history.back();
				return
			}
			sameStationIndex = 0;
			sameStationList = data.info;
			var station = data.info[sameStationIndex];
			var html = station.name;
			if (station.dis > 0) {
				html += '<span class="station-title-name-dis">';
				html += station.dis;
				html += "米</span>"
			}
			$("#station-title-name").html(html);
			if (station.sameNum > 1) {
				$("#station-title-name-desc").html(station.sameNum + "个同名站台");
				$("#station-title-change").removeClass("view-hide");
				if (station.sameNum == 2) {
					$("#change-name").html("反向站台")
				} else {
					$("#change-name").html("切换站台")
				}
			} else {
				$("#station-title-change").addClass("view-hide")
			}
			addHistory(2, stationname, station.lat, station.lon, "", "", "");
			getLineByStationV2(station.name, station.lat, station.lon)
		},
		error: function(data) {
			reqError()
		}
	})
}

function stationChange() {
	if (!sameStationList || sameStationList.length < 1) {
		return
	}
	sameStationIndex++;
	if (sameStationIndex > (sameStationList.length - 1)) {
		sameStationIndex = 0
	}
	var station = sameStationList[sameStationIndex];
	getLineByStationV2(station.name, station.lat, station.lon)
}

function lineStationChange(stationOrder) {
	lastStationId = selectStationId;// 上次选中站点
	selectStationId = stationOrder;
	getRealTime(lineNameTemp, directionTemp);
	console.log(lineNameTemp + "-" + directionTemp);
}

function stationSelect(stationid) {
	selectStationId = stationid;
	var normal = "<img src='images/line_icon_arrow.png'>";
	var sel = "<img src='images/direction_bus_list_target@2x.png'>";
	var oldchange = true;
	var newchange = true;
	// 判断是否首末站,首末站样式不变
	if (stationid == 1 || stationid == (stationListTemp.length)) {
		newchange = false;
	}
	if (lastStationId == 1 || lastStationId == (stationListTemp.length)) {
		oldchange = false;
	}
	// 清除上次选中样式
	if (lastStationId) {
		$("#station_order_" + lastStationId).removeClass("station-sel");
		$("#station_name_" + lastStationId).removeClass("station-sel");
		if (oldchange) {
			$("#station_dir_" + lastStationId).html(normal);
		}
	}
	// 选中站点添加样式
	$("#station_order_" + selectStationId).addClass("station-sel");
	$("#station_name_" + selectStationId).addClass("station-sel");
	if (newchange) {
		$("#station_dir_" + selectStationId).html(sel);
	}
	showRealTimeInfo();
}

function sameStation() {
	var stationName = stationListTemp[selectStationId - 1].stationName;
	toStationLine(stationName);
}

/**
 * 换乘点查询
 * 
 * @param type
 *            1起点 2终点
 * @returns
 */
function searchTransferPoint(type) {
	var cityname = localStorage.getItem("cityName");
	if (!cityname) {
		return;
	}
	var citykey = getKEY();
	var keyword;
	if (type == 1) {
		keyword = $("#input_start").val();
	} else {
		keyword = $("#input_end").val();
	}
	var html = "";
	if (!keyword || keyword.length == 0) {
		$("#list-view").html(html);
		return;
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "110",
			"CITYNAME": cityname,
			"KEYWORD": keyword,
			"CITYKEY": citykey
		},
		success: function(data) {
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			swal.close();
			var html = "";
			var busstations = data.busstations;
			for (var i = 0; i < busstations.length; i++) {
				html += "<li onclick=\"transferPointSelect('";
				html += busstations[i].stationName;
				html += "','','','";
				html += type;
				html += "')\">";
				html += "<img src=\"images/home_icon_station_big.png\">";
				html += "<div>";
				html += busstations[i].stationName;
				html += "</div></li>";
			}
			$("#list-view").html(html);
			//poiSearch(keyword, type, html);
		},
		error: function(data) {
			reqError();
		}
	});
}

function poiSearch(keyword, searchType, html) {
	var cityname = localStorage.getItem("cityName");
	var placeSearch = new AMap.PlaceSearch({
		// city 指定搜索所在城市，支持传入格式有：城市名、citycode和adcode
		city: cityname,
		pageSize: 30,
	})

	placeSearch.search(keyword, function(status, result) {
		// console.log(status);
		// console.log(result);
		if (status != "complete") {
			return;
		}
		if (result.info != "OK") {
			return;
		}
		var pois = result.poiList.pois;
		for (var i = 0; i < pois.length; i++) {
			var poi = pois[i];
			var lat = poi.location.lat;
			var lng = poi.location.lng;
			html += "<li onclick=\"transferPointSelect('";
			html += poi.name;
			html += "','";
			html += lat;
			html += "','";
			html += lng;
			html += "','";
			html += searchType;
			html += "')\">";
			html += "<img src=\"images/icon_place.png\">";
			html += "<div>";
			html += "<div>";
			html += poi.name;
			html += "</div>";
			html += "<div class='address'>";
			html += poi.address;
			html += "</div>";
			html += "</div></li>";
		}
		$("#list-view").html(html);
	})

}

function transferPointSelect(stationName, lat, lng, type) {
	$("#list-view").html("");
	if (type == 1) {
		$("#input_start").val(stationName);
		sessionStorage.setItem("transfer-start", stationName);
		sessionStorage.setItem("transfer-slat", lat);
		sessionStorage.setItem("transfer-slng", lng);
		var endPoint = $("#input_end").val();
		if (endPoint) {
			toSearchTransfer();
		}
	} else {
		$("#input_end").val(stationName);
		sessionStorage.setItem("transfer-end", stationName);
		sessionStorage.setItem("transfer-elat", lat);
		sessionStorage.setItem("transfer-elng", lng);
		var startPoint = $("#input_start").val();
		if (startPoint) {
			toSearchTransfer();
		}
	}
}

function toSearchTransfer() {
	location.href = "transfer2.html";
}

function toSearchTransferForHis(o1, o2, o3, o4, o5, o6) {
	sessionStorage.setItem("transfer-start", o1);
	sessionStorage.setItem("transfer-slng", o2);
	sessionStorage.setItem("transfer-slat", o3);
	sessionStorage.setItem("transfer-end", o4);
	sessionStorage.setItem("transfer-elng", o5);
	sessionStorage.setItem("transfer-elat", o6);
	location.href = "transfer2.html";
}
function searchTransfer() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var startPointName = sessionStorage.getItem("transfer-start");
	var slat = sessionStorage.getItem("transfer-slat");
	var slng = sessionStorage.getItem("transfer-slng");
	var endPointName = sessionStorage.getItem("transfer-end");
	var elat = sessionStorage.getItem("transfer-elat");
	var elng = sessionStorage.getItem("transfer-elng");
	$("#transfer-start").html(startPointName);
	$("#transfer-end").html(endPointName);
	$
		.ajax({
			type: "POST",
			dataType: "json",
			url: apiroot,
			data: {
				"CMD": "111",
				"CITYNAME": cityname,
				"STARTPOINTNAME": startPointName,
				"STARTPOINTLNG": slng,
				"STARTPOINTLAT": slat,
				"ENDPOINTNAME": endPointName,
				"ENDPOINTLNG": elng,
				"ENDPOINTLAT": elat,
				"CITYKEY": citykey
			},
			success: function(data) {
				console.log(data);
				if (data.status != 1) {
					swal({
						title: "",
						text: data.msg,
						type: "error",
						timer: 2000,
						showConfirmButton: false
					});
					return;
				}
				transferList = data.data;
				if (!transferList) {
					return;
				}
				// 保存换乘方案，查看详情使用
				var transferStr = JSON.stringify(transferList);
				sessionStorage.setItem("transferList", transferStr);
				// 展示换乘列表
				var html = "";
				for (var i = 0; i < transferList.length; i++) {
					transfer = transferList[i];
					html += "<li onclick=\"toTransferDetails('" + i
						+ "')\">";
					html += "<div class='transfer-list-soft'>方案";
					html += (i + 1);
					html += "</div>";
					html += "<div class='transfer-list-item'>";
					html += "<div>";
					html += transfer.startStation;
					html += "</div>";
					html += "<div class='transfer-list-line'>";
					html += transfer.startLineName;
					html += "</div>";
					if (transfer.flag == 1) {
						html += "<div>";
						html += transfer.endChangeStation;
						html += "</div>";
						html += "<div class='transfer-list-line'>";
						html += transfer.endLineName;
						html += "</div>";
					}
					html += "<div id='transfer_real_";
					html += i;
					html += "'>";
					html += "</div>"
					html += "</div></li>";
				}
				html += "</div>";
				$("#transfer-list").html(html);
				addHistory(3, startPointName, slng, slat, endPointName,
					elng, elat);
				searchTransferReal();
			},
			error: function(data) {
				swal({
					title: "",
					text: "未搜索到相关结果",
					type: "error",
					timer: 5000,
					showConfirmButton: false
				});
			}
		});
}

function searchTransfer2() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var startPointName = sessionStorage.getItem("transfer-start");
	var slat = sessionStorage.getItem("transfer-slat");
	var slng = sessionStorage.getItem("transfer-slng");
	var endPointName = sessionStorage.getItem("transfer-end");
	var elat = sessionStorage.getItem("transfer-elat");
	var elng = sessionStorage.getItem("transfer-elng");
	$("#transfer-start").html(startPointName);
	$("#transfer-end").html(endPointName);
	showLoader();
	$
		.ajax({
			type: "POST",
			dataType: "json",
			url: apiroot,
			data: {
				"CMD": "118",
				"CITYNAME": cityname,
				"STARTPOINTNAME": startPointName,
				"STARTPOINTLNG": slng,
				"STARTPOINTLAT": slat,
				"ENDPOINTNAME": endPointName,
				"ENDPOINTLNG": elng,
				"ENDPOINTLAT": elat,
				"CITYKEY": citykey
			},
			success: function(data) {
				console.log(data);
				if (data.status != 1) {
					swal({
						title: "",
						text: data.msg,
						type: "error",
						timer: 2000,
						showConfirmButton: false
					});
					return;
				}
				swal.close();
				transferList = data.info;
				if (!transferList) {
					return;
				}
				// 保存换乘方案，查看详情使用
				var transferStr = JSON.stringify(transferList);
				sessionStorage.setItem("transferList", transferStr);
				// 展示换乘列表
				var html = "";
				for (var i = 0; i < transferList.length; i++) {
					var transfer = transferList[i];
					var walkDistance = transfer.totalWalkDistance;
					html += "<li onclick=\"toTransferDetails('" + i + "')\">";
					html += "<div class=\"transfers-time\">";
					html += "<div>";
					html += transfer.totalTime;
					html += "</div>";
					if (walkDistance && walkDistance != "") {
						html += "<div class=\"transfers-walk\">";
						html += "<img src=\"images/icon_walk.png\">";
						html += walkDistance;
						html += "</div>";
					}
					html += "</div>";
					html += "<div class=\"transfer-stano\">";
					html += transfer.stationNum + " ";
					html += transfer.upStation;
					html += " 上车</div>";
					var lines = transfer.lines;
					var count = lines.length;
					html += "<div class=\"transfers-scheme-list\">";
					for (var j = 0; j < count; j++) {
						var lineNames = lines[j].lineNames;
						lineNames = lineNames.replace(/#/g, " / ");

						html += "<div class=\"transfers-scheme\">";
						html += "<div class=\"scheme\">";
						html += "<img src=\"images/route_icon_bus@3x.png\">";
						html += "<div class=\"scheme-line\">";
						html += lineNames;
						html += "</div>";
						html += "</div>";
						if (count > 0 && (j < count - 1)) {
							html += "<img src=\"images/icon_to_right.png\">";
						}
						html += "</div>";
					}
					html += "</div>";
					html += "<div id=\"transfer_real_" + i + "\"></div>";
					html += "</li>";
				}

				$("#transfer-list").html(html);
				if (html == "") {
					swal({
						title: "",
						text: "未搜索到相关结果",
						type: "warnning",
						timer: 2000,
						showConfirmButton: false
					});
					return;
				}
				addHistory(3, startPointName, slng, slat, endPointName, elng, elat);
				searchTransferReal2();
				searchTransferSpecial();
			},
			error: function(data) {
				swal({
					title: "",
					text: "未搜索到相关结果",
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
			}
		});
}

function searchTransferSpecial() {
	if (!transferList) {
		return;
	}
	var linelist = [];

	var strlist = [];

	for (var i = 0; i < transferList.length; i++) {
		var lines = transferList[i].lines;
		for (var j = 0; j < lines.length; j++) {
			var str = lines[j].lineNames + lines[j].dirs;
			if (strlist.indexOf(str) >= 0) {
				continue;
			}
			strlist.push(str);
			var lineinfo = {
				lineName: lines[j].lineNames,
				direction: lines[j].dirs,
			}

			linelist.push(lineinfo);
		}

	}
	getLineSpecialNoticeByLines(linelist);
}

function searchTransferDetailSpecial(event) {
	var transferList = JSON.parse(sessionStorage.getItem("transferList"));
	if (!transferList) {
		return;
	}
	var transfer = transferList[event];
	var lines = transfer.lines;
	// 第一换乘线路
	var linelist = [];
	for (var i = 0; i < lines.length; i++) {
		var lineinfo = {
			lineName: lines[i].lineNames,
			direction: lines[i].dirs,
		}
		linelist.push(lineinfo);
	}
	getLineSpecialNoticeByLines(linelist);
}

function searchTransferReal() {
	if (realTimeThread) {
		clearTimeout(realTimeThread);
	}
	if (!transferList) {
		return;
	}
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var realLine = "";
	var realDir = "";
	var stationOrder = "";
	for (var i = 0; i < transferList.length; i++) {
		realLine += transferList[i].realLine;
		realDir += transferList[i].realDir;
		stationOrder += transferList[i].stationOrder;
		if (i < (transferList.length - 1)) {
			realLine += ",";
			realDir += ",";
			stationOrder += ",";
		}
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "112",
			"CITYNAME": cityname,
			"REALLINE": realLine,
			"REALDIR": realDir,
			"STATIONORDER": stationOrder,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var realtimeinfo = data.data;
			for (var i = 0; i < realtimeinfo.length; i++) {
				var html = "";
				if (realtimeinfo[i].desc != "") {
					var html = "<div class='transfer-list-real'>";
					html += realtimeinfo[i].desc;
					html += "</div>";
				}
				$("#transfer_real_" + i).html(html);
			}
		},
		error: function(data) {
			reqError();
		}
	});
	realTimeThread = setTimeout("searchTransferReal()", 10000);
}

function searchTransferReal2() {
	if (realTimeThread) {
		clearTimeout(realTimeThread);
	}
	if (!transferList) {
		return;
	}
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var realLine = "";
	var realDir = "";
	var stationOrder = "";
	var stationName = "";
	for (var i = 0; i < transferList.length; i++) {
		var lines = transferList[i].lines;
		realLine += lines[0].lineNames;
		realDir += lines[0].dirs;
		stationOrder += lines[0].orders;
		stationName += lines[0].stations;
		if (i < (transferList.length - 1)) {
			realLine += ",";
			realDir += ",";
			stationOrder += ",";
			stationName += ",";
		}
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "112",
			"CITYNAME": cityname,
			"REALLINE": realLine,
			"REALDIR": realDir,
			"STATIONORDER": stationOrder,
			"STATIONNAME": stationName,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var realtimeinfo = data.data;
			for (var i = 0; i < realtimeinfo.length; i++) {
				var info = realtimeinfo[i];
				var html = '<div class="transfers-real">';
				html += '<span class="transfers-real-line">' + info.name + "</span>";
				if ("等待发车" == info.staNum && info.plantime) {
					html += "预计发车：" + info.plantime
				} else {
					if (info.costTm != "") {
						html += "最近车辆：" + info.staNum + " | " + info.costTm
					} else {
						html += info.staNum
					}
				}
				html += "</div>";

				$("#transfer_real_" + i).html(html);
			}
		},
		error: function(data) {
			reqError();
		}
	});
	realTimeThread = setTimeout("searchTransferReal2()", 10000);
}

function toTransferDetails(event) {
	location.href = "transferDetails2.html?transferId=" + event;
}

function showTransferDetails2(event) {
	// 从获取换乘方案
	var transferList = JSON.parse(sessionStorage.getItem("transferList"));
	if (!transferList) {
		return;
	}
	var transfer = transferList[event];
	var startName = transfer.startName;
	var endName = transfer.endName;
	// var upStation = transfer.upStation;

	var startWalkDistance = transfer.startWalkDistance;
	var firstWalkDistance = transfer.firstWalkDistance;
	var secondWalkDistance = transfer.secondWalkDistance;
	var endWalkDistance = transfer.endWalkDistance;

	var routeUpDownSimple1 = transfer.routeUpDownSimple1;
	var routeUpDownSimple2 = transfer.routeUpDownSimple2;
	var routeUpDownSimple3 = transfer.routeUpDownSimple3;

	var firstUp = routeUpDownSimple1[0].upStationName;
	var firstIn = routeUpDownSimple1[0].entryName;
	if (firstIn != "") {
		firstIn = "（" + firstIn + ")";
	}


	var firstChange = false;// 是否有一次换乘
	var secondChange = false;// 是否有二次换乘
	if (routeUpDownSimple2 && routeUpDownSimple2.length > 0) {
		firstChange = true;
	}
	if (routeUpDownSimple3 && routeUpDownSimple3.length > 0) {
		secondChange = true;
	}

	var html = "<div class=\"out-view\">";
	html += "<img src=\"images/icon_index_start.png\" class='start-image'>";
	html += "<div class=\"start-view\">";
	html += "<div class=\"start-view-text\">";
	html += transfer.startName;
	html += "</div>";
	html += "</div>";
	html += "</div>";
	if (startWalkDistance && startWalkDistance != "") {
		html += "<div class=\"out-view\">";
		html += "<div class=\"insite-view\">";
		html += "<div class=\"walk-view\">步行约";
		html += startWalkDistance;
		html += "</div>";
		html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
		html += "</div>";
		html += "</div>";
	}
	if (startName != firstUp) {
		html += "<div class=\"out-view\">";
		html += "<div class=\"insite-view\">";
		html += "<div class=\"station-view\">";
		html += firstUp + firstIn;
		html += "</div>";
		html += "</div>";
		html += "</div>";
	}
	var count = 0;
	var firstDown;// 第一段下车站
	var firstOut;
	if (routeUpDownSimple1) {
		for (var i = 0; i < routeUpDownSimple1.length; i++) {
			var ruds = routeUpDownSimple1[i];
			var stadis = ruds.downStationIndex - ruds.upStationIndex;
			html += "<div class=\"out-view-line\">";
			html += "<div class=\"insite-view\">";
			html += "<img src=\"images/icon_huancheng_bus.png\" class=\"line-view-image\">";
			html += "<div class=\"line-view\">";
			html += "<div class=\"line-view-name\" onclick=\"toBusLineDetails2('" + ruds.routeName + "','" + ruds.uod + "','" + (ruds.upStationIndex + 1) + "')\">";
			if (i > 0) {
				html += "<text>或：</text>";
			}
			html += ruds.routeName + "<span class=\"line-view-name-to\"> 开往" + ruds.endStationName;
			html += "</span></div>";
			html += "<div class=\"line-view-desc\">途经：";
			html += stadis + "个站";
			html += "</div>";
			html += "<div id=\"line-view-real_" + count + "\">";
			html += "<div class=\"line-view-real\">";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			count++;
			if (!firstDown) {
				firstDown = ruds.downStationName;
				firstOut = ruds.outName;
				if (firstOut != "") {
					firstOut = "（" + firstOut + "）";
				}
			}
		}
	}

	// 有一次换乘
	if (firstChange) {
		// 第一段下车站
		html += "<div class=\"out-view\">";
		html += "<div class=\"insite-view\">";
		html += "<div class=\"station-view\">";
		html += firstDown + firstOut;
		html += "</div>";
		html += "</div>";
		html += "</div>";

		if (firstWalkDistance) {
			// 步行距离
			html += "<div class=\"out-view\">";
			html += "<div class=\"insite-view\">";
			html += "<div class=\"walk-view\">步行约";
			html += firstWalkDistance;
			html += "</div>";
			html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
			html += "</div>";
			html += "</div>";
			// 步行后 上车站
			html += "<div class=\"out-view\">";
			html += "<div class=\"insite-view\">";
			html += "<div class=\"station-view\">";
			html += routeUpDownSimple2[0].upStationName;
			if (routeUpDownSimple2[0].entryName != "") {
				html += "（" + routeUpDownSimple2[0].entryName + "）";
			}
			html += "</div>";
			html += "</div>";
			html += "</div>";
		}

		var secondDown;// 第一段下车站
		var secondOut;
		if (routeUpDownSimple2) {
			for (var i = 0; i < routeUpDownSimple2.length; i++) {
				var ruds = routeUpDownSimple2[i];
				var stadis = ruds.downStationIndex - ruds.upStationIndex;
				html += "<div class=\"out-view-line\">";
				html += "<div class=\"insite-view\">";
				html += "<img src=\"images/icon_huancheng_bus.png\" class=\"line-view-image\">";
				html += "<div class=\"line-view\">";
				html += "<div class=\"line-view-name\" onclick=\"toBusLineDetails2('" + ruds.routeName + "','" + ruds.uod + "','" + (ruds.upStationIndex + 1) + "')\">";
				if (i > 0) {
					html += "<text>或：</text>";
				}
				html += ruds.routeName + "<span class=\"line-view-name-to\"> 开往" + ruds.endStationName;
				html += "</span></div>";
				html += "<div class=\"line-view-desc\">途经：";
				html += stadis + "个站";
				html += "</div>";
				html += "<div id=\"line-view-real_" + count + "\">";
				html += "<div class=\"line-view-real\">";
				html += "</div>";
				html += "</div>";
				html += "</div>";
				html += "</div>";
				html += "</div>";
				count++;
				if (!secondDown) {
					secondDown = ruds.downStationName;
					secondOut = ruds.outName;
					if (secondOut != "") {
						secondOut = "（" + secondOut + "）";
					}
				}
			}
		}

		// 有二次换乘
		if (secondChange) {
			// 第二段下车站
			html += "<div class=\"out-view\">";
			html += "<div class=\"insite-view\">";
			html += "<div class=\"station-view\">";
			html += secondDown + secondOut;
			html += "</div>";
			html += "</div>";
			html += "</div>";

			if (secondWalkDistance) {
				// 步行距离
				html += "<div class=\"out-view\">";
				html += "<div class=\"insite-view\">";
				html += "<div class=\"walk-view\">步行约";
				html += secondWalkDistance;
				html += "</div>";
				html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
				html += "</div>";
				html += "</div>";
				// 步行后 上车站
				html += "<div class=\"out-view\">";
				html += "<div class=\"insite-view\">";
				html += "<div class=\"station-view\">";
				html += routeUpDownSimple3[0].upStationName;
				if (routeUpDownSimple3[0].entryName != "") {
					html += "（" + routeUpDownSimple3[0].upStationName + "）";
				}
				html += "</div>";
				html += "</div>";
				html += "</div>";
			}

			var thirdDown;// 第三段下车站
			var thirdOut;//
			if (routeUpDownSimple3) {
				for (var i = 0; i < routeUpDownSimple3.length; i++) {
					var ruds = routeUpDownSimple3[i];
					var stadis = ruds.downStationIndex - ruds.upStationIndex;
					html += "<div class=\"out-view-line\">";
					html += "<div class=\"insite-view\">";
					html += "<img src=\"images/icon_huancheng_bus.png\" class=\"line-view-image\">";
					html += "<div class=\"line-view\">";
					html += "<div class=\"line-view-name\" onclick=\"toBusLineDetails2('" + ruds.routeName + "','" + ruds.uod + "','" + (ruds.upStationIndex + 1) + "')\">";
					if (i > 0) {
						html += "<text>或：</text>";
					}
					html += ruds.routeName + "<span class=\"line-view-name-to\"> 开往" + ruds.endStationName;
					html += "</span></div>";
					html += "<div class=\"line-view-desc\">途经：";
					html += stadis + "个站";
					html += "</div>";
					html += "<div id=\"line-view-real_" + count + "\">";
					html += "<div class=\"line-view-real\">";
					html += "</div>";
					html += "</div>";
					html += "</div>";
					html += "</div>";
					html += "</div>";
					count++;
					if (!thirdDown) {
						thirdDown = ruds.downStationName;
						thirdOut = ruds.outName;
						if (thirdOut) {
							thirdOut = "（" + thirdOut + "）";
						}
					}
				}
				// 第二次换乘下车
				if (endName != thirdDown) {
					html += "<div class=\"out-view\">";
					html += "<div class=\"insite-view\">";
					html += "<div class=\"station-view\">";
					html += thirdDown + thirdOut;
					html += "</div>";
					html += "</div>";
					html += "</div>";
					if (endWalkDistance && endWalkDistance != "") {
						html += "<div class=\"out-view\">";
						html += "<div class=\"insite-view\">";
						html += "<div class=\"walk-view\">步行约";
						html += endWalkDistance;
						html += "</div>";
						html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
						html += "</div>";
						html += "</div>";
					}
				}
			}
		} else {// 只有一次换乘的情况
			if (endName != secondDown) {
				html += "<div class=\"out-view\">";
				html += "<div class=\"insite-view\">";
				html += "<div class=\"station-view\">";
				html += secondDown + secondOut;
				html += "</div>";
				html += "</div>";
				html += "</div>";
				if (endWalkDistance && endWalkDistance != "") {
					html += "<div class=\"out-view\">";
					html += "<div class=\"insite-view\">";
					html += "<div class=\"walk-view\">步行约";
					html += endWalkDistance;
					html += "</div>";
					html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
					html += "</div>";
					html += "</div>";
				}
			}
		}
	} else {// 直达下车站
		if (endName != firstDown) {
			html += "<div class=\"out-view\">";
			html += "<div class=\"insite-view\">";
			html += "<div class=\"station-view\">";
			html += firstDown + firstOut;
			html += "</div>";
			html += "</div>";
			html += "</div>";
			if (endWalkDistance && endWalkDistance != "") {
				html += "<div class=\"out-view\">";
				html += "<div class=\"insite-view\">";
				html += "<div class=\"walk-view\">步行约";
				html += endWalkDistance;
				html += "</div>";
				html += "<img class=\"walk-image\" src=\"images/tarns-walk.png\">";
				html += "</div>";
				html += "</div>";
			}
		}
	}
	html += "<div class=\"out-view\">";
	html += "<img src=\"images/icon_index_end.png\" class=\"end-image\">";
	html += "<div class=\"end-view\">";
	html += "<div class=\"end-view-text\">";
	html += endName;
	html += "</div>";
	html += "</div>";
	html += "</div>";

	$("#transfer-detail").html(html);
	searchTransferDetailReal2(event);
	searchTransferDetailSpecial(event)
}

function showTransferDetails(event) {
	// 从获取换乘方案
	var transferList = JSON.parse(sessionStorage.getItem("transferList"));
	if (!transferList) {
		return;
	}
	var transfer = transferList[event];
	var startPoint = transfer.startPoint;
	var startWalk = transfer.startWalk;
	var startStation = transfer.startStation;
	var flag = transfer.flag;
	var transferStart = [];
	var transferEnd = [];
	var transferDetails = transfer.transferDetails;
	for (var i = 0; i < transferDetails.length; i++) {
		if (transferDetails[i].type == 0) {
			transferStart.push(transferDetails[i]);
		} else {
			transferEnd.push(transferDetails[i]);
		}
	}
	var startChangeStation = transfer.startChangeStation;
	var endChangeStation = transfer.endChangeStation;
	var changeWalk = transfer.changeWalk;
	var endPoint = transfer.endPoint;
	var endWalk = transfer.endWalk;
	var endStation = transfer.endStation;
	// 起点
	var html = "";
	html += "<div class='out-view'>";
	html += "<img src='images/icon_index_start.png' class='start-image'>";
	html += "<div class='start-view'>";
	html += "<div class='start-view-text'>";
	html += startPoint
	html += "</div>";
	html += "</div>";
	html += "</div>";
	// 起始是否要步行
	if (startWalk > 0) {
		html += "<div class='out-view'>";
		html += "<div class='insite-view'>";
		html += "<div class='walk-view'>步行约";
		html += startWalk;
		html += "米</div>";
		html += "<img class='walk-image' src='images/tarns-walk.png'>";
		html += "</div>";
		html += "</div>";
		html += "<div class='out-view'>";
		html += "<div class='insite-view'>";
		html += "<div class='station-view'>";
		html += startStation;
		html += "</div>";
		html += "</div>";
		html += "</div>";
	}

	// 首次乘车方案
	html += "<div class='out-view-line'>";
	for (var i = 0; i < transferStart.length; i++) {
		var item = transferStart[i];
		html += "<div class='insite-view'>";
		html += "<img src='images/icon_huancheng_bus.png' class='line-view-image'>";
		html += "<div class='line-view'>";

		html += "<div class='line-view-name' onclick=\"toBusLineDetails2('" + item.lineName + "','" + item.upperOrdown + "','" + item.startOrder + "')\">";
		if (i > 0) {
			html += "或：";
		}
		html += item.routeNumber;
		html += "</div>";

		html += "<div class='line-view-desc'>途经：";
		html += item.stationNum;
		html += "个站点(";
		html += item.busDistance;
		html += " ";
		html += item.costTM;
		html += ")</div>";

		// 判断下车站是否终点站
		if (flag == 0 && (item.endStation != endStation)) {
			html += "<div class='line-view-desc'>下车站：";
			html += item.endStation;
			html += " 下车步行：";
			html += item.eWalk;
			html += "米</div>";
		}

		html += "<div id='line-start-real_";
		html += i;
		html += "'>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
	}
	html += "</div>";

	// 换乘站
	if (startChangeStation != '') {
		html += "<div class='out-view'>";
		html += "<div class='insite-view'>";
		html += "<div class='station-view'>";
		html += startChangeStation;
		html += "</div>";
		html += "</div>";
		html += "</div>";
		// 中途是否要步行
		if (changeWalk > 0) {
			html += "<div class='out-view'>";
			html += "<div class='insite-view'>";
			html += "<div class='walk-view'>步行约";
			html += changeWalk;
			html += "米</div>";
			html += "<img class='walk-image' src='images/tarns-walk.png'>";
			html += "</div>";
			html += "</div>";
			html += "<div class='out-view'>";
			html += "<div class='insite-view'>";
			html += "<div class='station-view'>";
			html += endChangeStation;
			html += "</div>";
			html += "</div>";
			html += "</div>";
		}
	}

	// 二次乘车方案
	if (transferEnd.length > 0) {
		html += "<div class='out-view-line'>";
		for (var i = 0; i < transferEnd.length; i++) {
			var item = transferEnd[i];
			html += "<div class='insite-view'>";
			html += "<img src='images/icon_huancheng_bus.png' class='line-view-image'>";
			html += "<div class='line-view'>";

			html += "<div class='line-view-name' onclick=\"toBusLineDetails2('" + item.lineName + "','" + item.upperOrdown + "','" + item.startOrder + "')\">";
			if (i > 0) {
				html += "或：";
			}
			html += item.routeNumber;
			html += "</div>";

			html += "<div class='line-view-desc'>途经：";
			html += item.stationNum;
			html += "个站点(";
			html += item.busDistance;
			html += " ";
			html += item.costTM;
			html += ")</div>";

			// 判断下车站是否终点站
			if (flag == 0 && item.endStation != endStation) {
				html += "<div class='line-view-desc'>下车站：";
				html += item.endStation;
				html += " 下车步行：";
				html += item.eWalk;
				html += "米</div>";
			}

			html += "<div id='line-end-real_";
			html += i;
			html += "'>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
		}
		html += "</div>";
	}

	// 判断下车后是否要步行
	if (endWalk > 0) {
		html += "<div class='out-view'>";
		html += "<div class='insite-view'>";
		html += "<div class='station-view'>";
		html += endStation;
		html += "</div>";
		html += "</div>";
		html += "</div>";
		html += "<div class='out-view'>";
		html += "<div class='insite-view'>";
		html += "<div class='walk-view'>步行约";
		html += endWalk;
		html += "米</div>";
		html += "<img class='walk-image' src='images/tarns-walk.png'>";
		html += "</div>";
		html += "</div>";
	}
	// 终点
	html += "<div class='out-view'>";
	html += "<img src='images/icon_index_end.png' class='end-image'>";
	html += "<div class='end-view'>";
	html += "<div class='end-view-text'>";
	html += endPoint;
	html += "</div>";
	html += "</div>";
	html += "</div>";

	$("#transfer-detail").html(html);
	searchTransferDetailReal(event);
}

function searchTransferDetailReal2(event) {
	if (realTimeThread) {
		clearTimeout(realTimeThread);
	}
	var transferList = JSON.parse(sessionStorage.getItem("transferList"));
	if (!transferList) {
		return;
	}
	var transfer = transferList[event];
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var lines = transfer.lines;
	var realLine = "";
	var realDir = "";
	var stationOrder = "";
	var stationName = "";
	var countLine = 0;
	// 第一换乘线路
	for (var i = 0; i < lines.length; i++) {
		countLine++;
		realLine += lines[i].lineNames;
		realDir += lines[i].dirs;
		stationOrder += lines[i].orders;
		stationName += lines[i].stations;
		if (i < (lines.length - 1)) {
			realLine += "#";
			realDir += "#";
			stationOrder += "#";
			stationName += "#";
		}
	}

	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "113",
			"CITYNAME": cityname,
			"REALLINE": realLine,
			"REALDIR": realDir,
			"STATIONORDER": stationOrder,
			"STATIONNAME": stationName,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var startreal = data.data;
			for (var i = 0; i < startreal.length; i++) {
				var html = '<div class="line-view-real">';
				var info = startreal[i];
				if ("等待发车" == info.staNum && info.plantime) {
					html += "下一班：" + info.plantime
				} else {
					if (info.costTm != "") {
						html += "最近车辆：" + info.staNum + " | " + info.costTm
					} else {
						html += info.staNum
					}
				}
				html += "</div>";
				$("#line-view-real_" + i).html(html);
			}
		},
		error: function(data) {
			reqError();
		}
	});
	realTimeThread = setTimeout("searchTransferDetailReal2('" + event + "')",
		10000);
}


function searchTransferDetailReal(event) {
	if (realTimeThread) {
		clearTimeout(realTimeThread);
	}
	var transferList = JSON.parse(sessionStorage.getItem("transferList"));
	if (!transferList) {
		return;
	}
	var transfer = transferList[event];
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	var transferList = transfer.transferDetails;
	var realLine = "";
	var realDir = "";
	var stationOrder = "";
	// 第一换乘线路
	for (var i = 0; i < transferList.length; i++) {
		if (transferList[i].type != 0) {
			continue;
		}
		realLine += transferList[i].lineName;
		realDir += transferList[i].upperOrdown;
		stationOrder += transferList[i].startOrder;

		realLine += "#";
		realDir += "#";
		stationOrder += "#";

	}
	var realLine1 = "";
	var realDir1 = "";
	var stationOrder1 = "";
	// 第二换乘线路
	for (var i = 0; i < transferList.length; i++) {
		if (transferList[i].type != 1) {
			continue;
		}
		realLine1 += transferList[i].lineName;
		realDir1 += transferList[i].upperOrdown;
		stationOrder1 += transferList[i].startOrder;
		realLine1 += "#";
		realDir1 += "#";
		stationOrder1 += "#";
	}
	if (realLine1 != "") {
		realLine += "," + realLine1;
		realDir += "," + realDir1;
		stationOrder += "," + stationOrder1;
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "113",
			"CITYNAME": cityname,
			"REALLINE": realLine,
			"REALDIR": realDir,
			"STATIONORDER": stationOrder,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var startreal = data.data;
			for (var i = 0; i < startreal.length; i++) {
				var html = "";
				if (startreal[i].desc && startreal[i].desc != "") {
					html = "<div class='line-view-real'>";
					html += startreal[i].desc;
					html += "</div>";
				}
				$("#line-start-real_" + i).html(html);

			}
			var endreal = data.data2;
			for (var i = 0; i < endreal.length; i++) {
				var html = "";
				if (endreal[i].desc && endreal[i].desc != "") {
					html = "<div class='line-view-real'>";
					html += endreal[i].desc;
					html += "</div>";
				}
				$("#line-end-real_" + i).html(html);

			}
		},
		error: function(data) {
			reqError();
		}
	});
	realTimeThread = setTimeout("searchTransferDetailReal('" + event + "')",
		10000);
}

function transferChange() {
	var startPointName = sessionStorage.getItem("transfer-start");
	var slat = sessionStorage.getItem("transfer-slat");
	var slng = sessionStorage.getItem("transfer-slng");

	var endPointName = sessionStorage.getItem("transfer-end");
	var elat = sessionStorage.getItem("transfer-elat");
	var elng = sessionStorage.getItem("transfer-elng");

	sessionStorage.setItem("transfer-start", endPointName);
	sessionStorage.setItem("transfer-slat", elat);
	sessionStorage.setItem("transfer-slng", elng);

	sessionStorage.setItem("transfer-end", startPointName);
	sessionStorage.setItem("transfer-elat", slat);
	sessionStorage.setItem("transfer-elng", slng);
	toSearchTransfer();
}

/**
*
*设置合适的地图显示
*
*/
function setFitView() {
	var showViews = [];
	showViews.push(stationMks[(selectStationId - 1)]);
	if (busMks && busMks.length > 0 && latestBusMarkerId >= 0) {//判断是否有最近车辆
		showViews.push(busMks[latestBusMarkerId]);
	}
	console.log(showViews.length);
	map.setFitView(showViews,
		false,  // 动画过渡到制定位置
		[50, 50, 50, 50],  // 周围边距，上、下、左、右
		15
	);

}

function toMap() {
	$("#show-list-view").addClass("hide-view");
	$("#container").removeClass("hide-view");
	mapView = true;
	if (initMap) {
		/*map = new AMap.Map('container', {
			zoom: 14,
			//addOns: ['moveAnimation']
		});*/
		//map.setZoomAndCenter(14,stationMks[(selectStationId - 1)].getPosition(),false);
	}
	initMap = false;
	showMapBusMarker();
	showMapStation();

	setFitView();
}

function toList() {
	$("#show-list-view").removeClass("hide-view");
	$("#container").addClass("hide-view");
	mapView = false;
}

function showChange() {
	var html = "";
	// 当前地图页
	if (mapFlag) {
		html = "<img class=\"btn-map-img\" src=\"images/icon_map.png\">";
		html += "<div>地图</div>";
		toList();
	} else {
		html = "<img class=\"btn-map-img\" src=\"images/icon_zhanpai.png\">";
		html += "<div>站牌</div>";
		toMap();
	}
	$("#btn-map").html(html);
	mapFlag = !mapFlag;
}

function isMobile() {
	var userAgentInfo = navigator.userAgent;

	var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone",
		"iPad", "iPod"];

	var mobile_flag = false;

	// 根据userAgent判断是否是手机
	for (var v = 0; v < mobileAgents.length; v++) {
		if (userAgentInfo.indexOf(mobileAgents[v]) > 0) {
			mobile_flag = true;
			break;
		}
	}

	var screen_width = window.screen.width;
	var screen_height = window.screen.height;

	// 根据屏幕分辨率判断是否是手机
	if (screen_width < 500 && screen_height < 800) {
		mobile_flag = true;
	}

	return mobile_flag;
}

/** 3换乘 （起点,'起点经度','起点纬度','终点','终点经度','终点纬度'） */
function addHistory(ty, o1, o2, o3, o4, o5, o6) {
	var cityName = localStorage.getItem("cityName");
	// localStorage.setItem(cityName + "||His", "[]");
	try {
		var hisT = [];
		var his = localStorage.getItem(cityName + "||His")
		if (his) {
			hisT = JSON.parse(his);
		}
		console.log(hisT);
		if (hisT.length > 0) {
			var num = -1;
			for (var i = 0; i < hisT.length; i++) {
				if (hisT[i].type != ty) {
					continue;
				}
				if (ty == 1) {// 线路查下
					if (hisT[i].o1 == o1 && hisT[i].o2 == o2) {
						num = i;
						break;
					}
				} else if (ty == 2) {// 站点查下
					if (hisT[i].o1 == o1) {
						num = i;
						break;
					}
				} else if (ty == 3) {// 换乘查下
					if (hisT[i].o1 == o1 && hisT[i].o4 == o4) {
						num = i;
						break;
					}
				}
			}
			// 判断是否已经存在查下记录，存在删除旧记录
			if (num != -1) {
				hisT.splice(num, 1);
			}
		}
		hisT.push({
			'type': ty,
			'o1': o1,
			'o2': o2,
			'o3': o3,
			'o4': o4,
			'o5': o5,
			'o6': o6
		});
		localStorage.setItem(cityName + "||His", JSON.stringify(hisT));
	} catch (e) {
		console.log(e);
	}
}

function showHistory(ty) {
	var cityName = localStorage.getItem("cityName");
	try {
		var hisT = [];
		var his = localStorage.getItem(cityName + "||His")
		if (his) {
			hisT = JSON.parse(his);
		}
		var html = "";
		if (hisT) {
			for (var i = hisT.length - 1; i >= 0; i--) {
				if (ty == 3) {
					if (hisT[i].type != ty) {
						continue;
					}
				} else {
					if (hisT[i].type == 3) {
						continue;
					}
				}
				if (hisT[i].type == 1) {
					html += "<li onclick=\"toBusLineDetails('";
					html += hisT[i].o1;
					html += "','";
					html += hisT[i].o2
					html += "')\">";
					html += "<img src=\"images/icon_route.png\">";
					html += "<div>";
					html += hisT[i].o1;
					html += " 开往 ";
					html += hisT[i].o3;
					html += "</div></li>";
				} else if (hisT[i].type == 2) {
					html += "<li onclick=\"toStationLine('" + hisT[i].o1
						+ "')\">";
					html += "<img src=\"images/icon_station.png\">";
					html += "<div>";
					html += hisT[i].o1;
					html += "</div></li>";
				} else {
					html += "<li onclick=\"toSearchTransferForHis('";
					html += hisT[i].o1;
					html += "','";
					html += hisT[i].o2;
					html += "','";
					html += hisT[i].o3;
					html += "','";
					html += hisT[i].o4;
					html += "','";
					html += hisT[i].o5;
					html += "','";
					html += hisT[i].o6;
					html += "')\">";
					html += "<img src=\"images/icon_trans.png\">";
					html += "<div>";
					html += hisT[i].o1;
					html += "⇀";
					html += hisT[i].o4;
					html += "</div></li>";
				}
			}
			$("#list-view").html(html);
			if (html != '') {
				$("#history").removeClass("history-none");
				$("#history").addClass("history");
				//线路站点搜索页去
				$("#his-text").removeClass("hide");
				$("#his-clean").removeClass("hide");
			} else {
				//线路站点搜索页去
				$("#his-text").addClass("hide");
				$("#his-clean").addClass("hide");
			}
		}
	} catch (e) {
	}
}

function delHistory() {
	var ty = nowtype;
	var cityName = localStorage.getItem("cityName");
	// localStorage.setItem(cityName + "||His", "[]");
	try {
		var hisT = [];
		var his = localStorage.getItem(cityName + "||His")
		if (his) {
			hisT = JSON.parse(his);
		}
		var tempHis = [];
		for (var i = 0; i < hisT.length; i++) {
			if (ty == 3) {
				if (hisT[i].type == "3")
					continue;
			} else {
				if (hisT[i].type != "3")
					continue
			}
			// if (hisT[i].type == ty) {
			// continue;
			// }
			tempHis.push(hisT[i]);
		}
		localStorage.setItem(cityName + "||His", JSON.stringify(tempHis));
		showHistory(ty);
		$("#history").removeClass("history");
		$("#history").addClass("history-none");
		//线路站点隐藏历史
		$("#his-text").addClass("hide");
		$("#his-clean").addClass("hide");
	} catch (e) {
		console.log(e);
	}
}

function showTitle() {

	var citykey = getKEY();
	if (citykey === 'cangzhou2905001') {
		var u = navigator.userAgent;
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
		if (isAndroid) {
			return;
		}
	}

	if (isMobile()) {
		$("#line-title").attr("style", "display:none");
	} else {
		//	$("#bottom-image").attr("style", "display:none");
	}


}

function getCityList() {
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "101",
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var html = "";
			var citylist = data.data;
			cityList = citylist;
			for (var i = 0; i < citylist.length; i++) {
				var cityname = citylist[i].cityname;
				html += "<li onclick=\"citySelect('" + cityname + "')\">";
				html += cityname;
				html += "</li>";
			}
			$("#city-list").html(html);
		},
		error: function(data) {
			reqError();
		}
	});
}

function searchCityList() {
	var key = $("#city-input").val();
	var html = "";
	if (!cityList) {
		return;
	}
	for (var i = 0; i < cityList.length; i++) {
		var cityname = cityList[i].cityname;
		if (!key || cityname.indexOf(key) >= 0) {
			html += "<li onclick=\"citySelect('" + cityname + "')\">";
			html += cityname;
			html += "</li>";
		}
	}
	$("#city-list").html(html);
}

// 获取用户所在城市信息
function getCityInfo() {
	$("#location-city").html("定位中...");
	var map = new AMap.Map('container', {
		resizeEnable: true,
		addOns: ['moveAnimation']
	});

	AMap.plugin('AMap.Geolocation', function() {
		var geolocation = new AMap.Geolocation({
			enableHighAccuracy: true,// 是否使用高精度定位，默认:true
			timeout: 10000, // 超过10秒后停止定位，默认：5s
			buttonPosition: 'RB', // 定位按钮的停靠位置
		});
		map.addControl(geolocation);
		geolocation.getCurrentPosition(function(status, result) {
			if (status == 'complete') {
				var cityname = result.addressComponent.city;
				locationcity = cityname;
				$("#location-city").html("定位城市：" + cityname);
			} else {
				// onError(result)
				$("#location-city").html("定位失败");
			}
		});
	});
}

function locationSelect() {
	if (!locationcity) {
		return;
	}
	if (!cityList) {
		return;
	}
	var hav = false;
	for (var i = 0; i < cityList.length; i++) {
		if (cityList[i].cityname == locationcity) {
			hav = true;
		}
	}
	if (!hav) {
		swal({
			title: "",
			text: "当前城市尚未开通实时公交查询!",
			type: "error",
			timer: 2000,
			showConfirmButton: false
		});
		return;
	}
	localStorage.setItem("cityName", locationcity);
	location.href = "index.html";
}

function setCity(cityname) {
	if (cityname) {
		cityname = decodeURIComponent(cityname);
		localStorage.setItem("cityName", cityname);
	} else {
		cityname = localStorage.getItem("cityName");
	}
	$("#cityName").html(cityname);
}

function toCityList() {
	location.href = "city.html";
}

function citySelect(cityname) {
	localStorage.setItem("cityName", cityname);
	location.href = "index.html";
}

function reqError() {
	swal({
		title: "",
		text: "服务器繁忙，请稍后再试!",
		type: "error",
		timer: 2000,
		showConfirmButton: false
	});
	return;
}

function toDownload() {
	var citykey = getKEY();
	//location.href = "https://www.mygolbs.com/home";
	location.href = "https://a.app.qq.com/o/simple.jsp?pkgname=com.mygolbs.mybus";
}

getRootPath = function() {
	var pathName = window.document.location.pathname;
	var projectName = pathName
		.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (projectName);
}

function getNews() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "203",
			"CITYNAME": cityname,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				$("#review_box").attr("style", "display:none");
				return;
			}
			$("#review_box").attr("style", "display:inblock");
			var html = "";
			var newslist = data.data;
			newsList = newslist;
			for (var i = 0; i < newslist.length; i++) {
				if (i > 3) {
					continue;
				}
				var news = newslist[i];
				html += "<li onclick=\"toNewsDetails('" + i + "')\">";
				html += "<div class=\"new-type\">";
				html += news.type;
				html += "</div><div class=\"new-title\">";
				html += news.title;
				html += "</div></li>";
			}
			$("#comment1").html(html);
			roll(3000);
		},
		error: function(data) {
			reqError();
		}
	});
}

function getNewsList() {
	var cityname = localStorage.getItem("cityName");
	var citykey = getKEY();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "203",
			"CITYNAME": cityname,
			"CITYKEY": citykey
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				var html = "<div class='news-none'>尚未发布相关内容0_o!</div>";
				// $("#news-main").html(html);
				$("#new-list-view").html(html);
				return;
			}
			newsList = data.data;
			showNews();
		},
		error: function(data) {
			reqError();
		}
	});

}

function showNews() {
	var html = "";
	if (newsList.length == 0) {
		html += "<div class='news-none'>尚未发布相关内容0_o!</div>";
	} else {
		for (var i = 0; i < newsList.length; i++) {
			var news = newsList[i];
			html += "<li class=\"new-list-li\" onclick=\"toNewsDetails('" + i
				+ "')\">";
			html += "<img src=\"images/icon_news@2x.png\">";
			html += "<div class=\"news-con\">";
			html += "<div class=\"news-list-type\">";
			html += "<div>【";
			html += news.city;
			html += "】";
			html += news.type;
			html += "</div>"
			html += "<div class=\"new-date\">";
			html += news.date;
			html += "</div>";
			html += "</div>"
			html += "<div class=\"news-list-title\">";
			html += news.title;
			html += "</div></li>";
		}
	}
	$("#new-list-view").html(html);
}

function callbackToParent(type) {
	var message = {};
	if (type == 1) {
		message.busLine = lineNameTemp;
	} else if (type == 2) {
		message.station = stationTemp;
	} else {
		var startPointName = sessionStorage.getItem("transfer-start");
		var endPointName = sessionStorage.getItem("transfer-end");
		message.beginPlace = startPointName;
		message.endPlace = endPointName;
	}
	window.parent.postMessage(message, "*");
}

function cityInfo(result) {
	var cityname = result.addressComponent.city;
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "204",
			"CITYNAME": cityname
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				location.href = "city.html";
				return;
			}
			localStorage.setItem("cityName", cityname);
			$("#cityName").html(cityname);
			$("#index-text").html("附近");
			searchNearBy(result.position.lng, result.position.lat);
		},
		error: function(data) {
			reqError();
		}
	});
}

function getKEY() {
	var citykey = localStorage.getItem("citykey");
	if (citykey) {
		return citykey;
	} else {
		return "";
	}
}

// 获取当前城市配置
function getCityConfig(cityKey, text) {
	var setTitle = "掌上公交";
	if (cityKey == "nc100791") {
		showCustom = true;
	}
	if (cityKey == "hengshui0501") {
		allLine = true;
	}
	if (cityKey == "ycbh0001") {
		setTitle = "滨海公交";
	}
	if (cityKey == "ycsy0001") {
		setTitle = "射阳公交";
		showXianglingbus = true;
		showWangyuebus = true;
	}
	if (cityKey == "zzg0001") {
		// $(document).attr("title","射阳公交");
		ziXun = false;
	}
	if (cityKey == "qingyuan1110") {
		setTitle = "清远公交";
	}
	if (cityKey == "leshan000119") {
		setTitle = "实时公交";
	}
	if (cityKey == "yongj090501") {
		setTitle = "畅行永嘉";
	}
	if (cityKey == "nd032702" || cityKey == "zhangzhou112002") {
		MZTstyle = true;
	}
	var html = "<div>掌上公交-技术支持</div>";
	if (cityKey == "wuhai1206") {
		html = "<div id=\"wuhai\">";
		html += "<div>数据来源：乌海市行政审批和政务服务局</div>";
		html += "<div>";
		html += "服务热线：<span class=\"phone\">0473-3998312</span>";
		html += "</div>";
		html += "</div>";
	}
	if (cityKey == "dltq22107") {
		html = "<div id=\"wuhai\">";
		html += "<div>达拉特旗通达公交客运有限公司</div>";
		html += "<div>";
		html += "服务热线：<span class=\"phone\">0477-3968512</span>";
		html += "</div>";
		html += "</div>";
	}
	if (cityKey == "eeds3281") {
		html = "<div id=\"wuhai\">";
		html += "<div>鄂尔多斯市天安公共交通集团有限公司</div>";
		html += "<div>";
		html += "服务热线：<span class=\"phone\">0477-8373825</span>";
		html += "</div>";
		html += "</div>";
	}
	if (cityKey == "dangtu1214") {
		$("#banner").removeClass("hide");
		html = "<div id=\"wuhai\">";
		html += "<div>本服务由当涂县交通运输局提供<div>掌上公交-技术支持</div></div>";
		html += "<div>";
		html += "</div>";
		html += "</div>";
	}
	$("#bottom-info").html(html);
	$(document).attr("title", setTitle);
	if (text) {
		searchText = text;
		// alert(searchText);
	}
	$
		.ajax({
			type: "POST",
			dataType: "json",
			url: apiroot,
			data: {
				"CMD": "205",
				"CITYKEY": cityKey,
			},
			success: function(data) {
				console.log(data);
				// 未授权
				if (data.status == 2) {
					location.href = "error.html";
					return;
				}
				if (data.status != 1) {
					localStorage.setItem("cityName", "");
					swal({
						title: "",
						text: data.msg,
						type: "warning",
						showCancelButton: false,
						confirmButtonColor: '#FF682C',
						confirmButtonText: '我知道了',
						closeOnConfirm: false
					}, function() {
						localStorage.setItem("citykey", "");
						toDownload();
					});

					return;
				}
				var city = data.city;
				localStorage.setItem("cityName", city.cityname);
				localStorage.setItem("citykey", cityKey);
				$("#cityname").html(city.showName);
				if (city.logo) {
					$("#citylogo").attr("src", city.logo);
				}
				if (city.company) {
					$("#company").html(city.company);
				}
				if (city.nearStation == 1) {
					nearFlag = true;
				}
				if (city.download == 1) {
					$("#download-btn")
						.attr("style",
							"width:147px;height:70px;position:fixed;bottom:200px;right: 0;z-index: 9999");
				} else {
					$("#download-btn").attr("style",
						"width: 0px; height: 0px;");
				}
				if ("leshan000119" == cityKey) {
					setConfig2();
				} else {
					setConfig();
				}

			},
			error: function(data) {
				reqError();
			}
		});
}

// 线路站点搜索页获取当前城市配置
function getCityConfig2(cityKey) {
	$(document).attr("title", "搜索");
	showHistory('1');
	document.getElementById("input_line").focus();
	$
		.ajax({
			type: "POST",
			dataType: "json",
			url: apiroot,
			data: {
				"CMD": "205",
				"CITYKEY": cityKey,
			},
			success: function(data) {
				console.log(data);
				// 未授权
				if (data.status == 2) {
					location.href = "error.html";
					return;
				}
				if (data.status != 1) {
					localStorage.setItem("cityName", "");
					swal({
						title: "",
						text: data.msg,
						type: "warning",
						showCancelButton: false,
						confirmButtonColor: '#FF682C',
						confirmButtonText: '我知道了',
						closeOnConfirm: false
					}, function() {
						localStorage.setItem("citykey", "");
						toDownload();
					});
					return;
				}

				var city = data.city;
				localStorage.setItem("cityName", city.cityname);
				localStorage.setItem("citykey", cityKey);
			},
			error: function(data) {
				reqError();
			}
		});
}

function toNewsDetails(item) {
	if (!newsList) {
		return;
	}
	var url = newsList[item].clickurl;
	if (!url) {
		return;
	}
	url = url.replace("http://quanguo.mygolbs.com:8081/MyBusServer", "https://quanguo.mygolbs.com/MyBusServer");
	var citykey = getKEY();
	if (citykey && citykey == "nanning920901") {
		url += "&qrcode=0";
	}
	location.href = url;
}

function toNanHu() {
	toBusLineDetails("矿业大学校车去南湖", 1)
}

function toWenChang() {
	toBusLineDetails("矿业大学校车去文昌", 1)
}

function toWhrx() {
	toBusLineDetails("武汉文旅城市环游巴士（日线）", 1)
}

function toWhyx() {
	toBusLineDetails("武汉文旅城市环游巴士（夜线）", 1)
}

function toLineInfo(linename) {
	toBusLineDetails(linename, 1)
}

function toLineInfoAPP(linename) {
	try {
		MyBus.searchBus(linename, '1');
	} catch (e) {
		toBusLineDetails(linename, 1);
	}
}

function getDepart() {
	var cityname = localStorage.getItem("cityName");
	var routeId = routeInfo.routeId;
	var upperOrDown = routeInfo.upperOrDown;
	var routeName = routeInfo.routeName;
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "116",
			"CITYNAME": cityname,
			"DIRECTION": upperOrDown,
			"ROUTEID": routeId,
			"CITYKEY": ""
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return;
			}
			var html = "";
			var firstLast = routeInfo.firstLast;
			if (firstLast.length > 2) {
				html = "<div class=\"depart-title\">发车时段</div>";
				html += "<div class=\"depart-time\">";
				for (var i = 0; i < firstLast.length; i++) {
					html += "<div>";
					html += firstLast[i].first;
					html += "~";
					html += firstLast[i].last;
					html += "</div>";
				}
				html += "</div>";
				html += "</div>";
			}
			if (cityname == "泉州市") {
				html += "<div class=\"depart-title\">根据历史数据计算得出，仅供参考，请提前候车</div>";
			} else {
				if (data.special == "1") {
					html += "<div class=\"depart-title\">时刻表，仅供参考</div>";
				} else {
					html += "<div class=\"depart-title\">时刻表根据历史计算得出，仅供参考</div>";
				}
			}
			var departs = data.list;
			for (var i = 0; i < departs.length; i++) {
				html += "<div class=\"depart-group\">";
				html += departs[i].group;
				html += "</div>";
				html += "<div class=\"depart-time\">";
				var times = departs[i].times;
				for (var j = 0; j < times.length; j++) {
					html += "<div>";
					html += times[j];
					html += "</div>";
				}
				html += "</div>";
			}
			$("#depart-popup-title").html(routeName);
			$("#depart-popup").html(html);
			$modal = $('#my-popup');
			$modal.modal('open');
		},
		error: function(data) {
			reqError();
		}
	});
}

function getDepartV2() {
	departSelIndex = -1;
	var cityName = localStorage.getItem("cityName");
	var routeId = routeInfo.routeId;
	var uod = routeInfo.upperOrDown;
	var routeName = routeInfo.routeName;
	var firstLast = routeInfo.firstLast;
	if (firstLast.length >= 2) {
		var html = '<div class="depart-time-desc-t">发车时段</div>';
		html += '<div class="depart-time-desc-ts">';
		for (var c = 0; c < firstLast.length; c++) {
			html += '<div class="depart-time-desc-ts-i">';
			html += firstLast[c].first;
			html += "-";
			html += firstLast[c].last;
			html += "</div>"
		}
		html += "</div>";
		$("#facheshiduan").html(html);
	}
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CMD": "207",
			"CITYNAME": cityName,
			"DIRECTION": uod,
			"ROUTEID": routeId,
			"CITYKEY": ""
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.msg,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				return
			}
			var deschtml = "本时刻表数据根据历史数据计算得出，仅供参考";
			if (data.special == "1" && cityName != "泉州市") {
				deschtml = "本时刻表为计划发车时间，可能临时调整，仅供参考"
			}
			$("#depart-time-desc").html(deschtml);
			var list = data.list;
			var html = "";
			var m = -1;
			for (var p = 0; p < list.length; p++) {
				var s = list[p];
				if (s.isNow == 1) {
					m = p
				}
				html += '<div class="depart-time-item" onclick="departTimeSel(\'' + p + "')\">";
				html += '<div class="depart-time-group">';
				html += '<div class="depart-time-group-title">';
				html += '<img class="depart-time-group-depart" id="depart-time-group-depart_0_' + p + '" src="images/icon_depart1.png">';
				html += '<img class="depart-time-group-depart hide-view" id="depart-time-group-depart_1_' + p + '" src="images/icon_depart2.png">';
				html += '<span id="depart-time-group-title_' + p + '">';
				html += s.group;
				html += "</span>";
				if (s.peak == 1) {
					html += '<span class="zw-gaofeng">早高峰</span>';
				} else {
					if (s.peak == 2) {
						html += '<span class="zw-gaofeng">晚高峰</span>';
					}
				}
				html += "</div>";
				html += '<img class="depart-time-group-uod" id="depart-time-group-uod_0_' + p + '" src="images/icon_down.png">';
				html += '<img class="depart-time-group-uod hide-view" id="depart-time-group-uod_1_' + p + '" src="images/icon_up.png">';
				html += "</div>";
				html += '<div class="depart-time hide-view" id="depart-time_' + p + '">';
				var times = s.times;
				for (var n = 0; n < times.length; n++) {
					var time = times[n];
					if (time.status == 1) {
						html += '<div class="time-his">';
					} else if (time.status == 2) {
						html += '<div class="time-now">';
					} else if (time.status == 3) {
						html += '<div class="time-pro">';
					}
					html += time.time;
					if (time.notes) {
						html += '<span class="time-zhu" onclick="showMessage(\'' + time.notes + "')\">";
						html += "注";
						html += "<span>"
					}

					html += "</div>";
				}
				html += "</div>";
				html += "</div>";
			}
			if (html == "") {
				html = '<div style="margin-left:15px;">暂无数据<div>';
			}
			$("#depart-time-list").html(html);
			departTimeSel(m);
			$("#depart-popup-title").html(routeName);
			if (data.nextTime && data.nextTime.length > 0) {
				$("#next-time").html("下一班预计<span>" + data.nextTime + "</span>从起点发车");
			}
			$modal = $("#my-popup");
			$modal.modal("open");
		},
		error: function(e) {
			reqError()
		}
	})
}

function departTimeSel(a) {
	if (a == -1) {
		return
	}
	if (a == departSelIndex) {
		$("#depart-time-group-depart_1_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-uod_1_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-depart_0_" + departSelIndex).removeClass("hide-view");
		$("#depart-time-group-uod_0_" + departSelIndex).removeClass("hide-view");
		$("#depart-time_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-title_" + departSelIndex).removeClass("depart-time-group-active");
		departSelIndex = -1;
		return
	}
	if (departSelIndex != -1) {
		$("#depart-time-group-depart_1_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-uod_1_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-depart_0_" + departSelIndex).removeClass("hide-view");
		$("#depart-time-group-uod_0_" + departSelIndex).removeClass("hide-view");
		$("#depart-time_" + departSelIndex).addClass("hide-view");
		$("#depart-time-group-title_" + departSelIndex).removeClass("depart-time-group-active")
	}
	departSelIndex = a;
	$("#depart-time-group-depart_1_" + a).removeClass("hide-view");
	$("#depart-time-group-uod_1_" + a).removeClass("hide-view");
	$("#depart-time-group-depart_0_" + a).addClass("hide-view");
	$("#depart-time-group-uod_0_" + a).addClass("hide-view");
	$("#depart-time_" + a).removeClass("hide-view");
	$("#depart-time-group-title_" + a).addClass("depart-time-group-active")
}

function showMessage(a) {
	swal({
		title: "",
		text: a,
		showCancelButton: false,
		confirmButtonColor: "#FF682C",
		confirmButtonText: "OK",
		closeOnConfirm: false
	}, function() {
		swal.close()
	})
}

function toCustombus() {
	location.href = "https://wx.mygolbs.com/fzwj/nanchanAdvertise";
}

function toWangyuebus() {
	location.href = "https://microbus-web.mygolbs.com/index?channel=zsgj&cityCode=320924";
}

function toXianglingbus() {
	location.href = "https://reactivebus-web.mygolbs.com/index?channel=zsgj&cityName=射阳县&cityCode=320924";
}

function searchBusLineByStationQRCodeNew(stationid, citycode) {
	showLoader();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: apiroot,
		data: {
			"CITYCODE": citycode,
			"STATIONID": stationid,
			"CMD": "117",
		},
		success: function(data) {
			console.log(data);
			if (data.status != 1) {
				swal({
					title: "",
					text: data.desc,
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
				location.href = "index.html";
				return;
			}
			localStorage.setItem("cityName", data.cityname);
			localStorage.setItem("cityCode", citycode);
			if (data.data.length > 0) {
				$("#qr-title").html(data.data[0].stationName);
				var html = "";
				$("#busstation-list-ul").html(html);
				for (var i = 0; i < data.data.length; i++) {
					var line = data.data[i];
					var sid = line.stationID;// 等车站点Id
					var disclass = "line-list-dis";
					if (line.neartext == "已到站" || line.neartext == "未发车") {
						disclass += " line-list-dis-con";
					}
					html += "<li onclick=\"toBusLineDetails2('" + line.lineName + "','" + line.upperOrDown + "','" + sid + "')\">";
					html += "<div class=\"line-list-left\">";
					html += "<div class=\"line-list-name\">";
					html += line.lineName;
					html += "</div>";
					html += "<div class=\"line-list-dir\">开往 ";
					html += line.to;
					html += "</div>";
					html += "</div>";
					html += "<div class=\"line-list-right\">";
					html += "<div class=\"" + disclass + "\">";
					html += line.neartext;
					html += "</div>";
					html += "</div>";
					html += "</li>";
					/*
					 * html += "<li onclick=\"searchBusLineDetailsNew('"+line.lineName+"','"+line.upperOrDown+"','"+ line.comments+"','"+sid+"')\">";
					 * html += "<div class=\"line-info\">"; html += "<div
					 * class=\"line-name\">"; html += line.lineName; html += "</div>";
					 * html += "<div class=\"line-msg\">"; html += "<div
					 * class=\"line-to-info\">开往"; html += "<span
					 * class=\"line-to\">"; html += line.to; html += "</span>";
					 * html += "</div>"; html += "<div class=\"line-common\">";
					 * html += "首班:" + line.beginTime; html += " "; html += "末班:" +
					 * line.endTime; html += " "; html += "</div>"; html += "</div>";
					 * html += "</div>"; html += "<div class=\"real-time\">"; html +=
					 * line.neartext; html += "</div>"; html += "</li>";
					 */
				}
				$("#qr-line-list").html(html);
			} else {
				swal({
					title: "",
					text: "搜索不到线路，使用掌上公交App查看",
					type: "error",
					timer: 2000,
					showConfirmButton: false
				});
			}
			swal.close();
		},
		error: function(data) {
			reqError();
		}
	});
	setTimeout("searchBusLineByStationQRCode('" + stationid + "','" + citycode + "')", 20000);
}


/**
 * approx distance between two points on earth ellipsoid
 * 
 * @param {Object}
 *            lat1
 * @param {Object}
 *            lng1
 * @param {Object}
 *            lat2
 * @param {Object}
 *            lng2
 */
function getFlatternDistance(lat1, lng1, lat2, lng2) {
	lat1 = lat1 || 0;
	lng1 = lng1 || 0;
	lat2 = lat2 || 0;
	lng2 = lng2 || 0;

	var rad1 = lat1 * Math.PI / 180.0;
	var rad2 = lat2 * Math.PI / 180.0;
	var a = rad1 - rad2;
	var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;

	var r = 6378137;
	return r * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
}

function showLoader() {
	swal({
		title: "",
		text: "拼命加载中...",
		imageUrl: 'images/loading.gif',
		showConfirmButton: false
	});
}

function searText() {
	if (searchText) {
		$("#input_line").val(searchText);
		searchLineAnStation();
	}
}


init();
// getCityConfig('009');

function setConfig() {

	var html = "";
	html = "<ul class=\"home-main-view\">";
	if (nearFlag) {
		html += "<li id=\"near-bar\"><div>";
		html += "<img  src=\"images/home_icon_near.png\">";
		html += "<span class=\"home-main-item-text\">附近站点</span></div></li>";
	}
	html += "<li><div>";
	html += "<img  src=\"images/home_icon_search.png\">";
	html += "<span class=\"home-main-item-text\">线路站点</span></div></li>";
	html += "<li><div>";
	html += "<img  src=\"images/home_icon_qunan.png\">";
	html += "<span class=\"home-main-item-text\">去哪查询</span></div></li>";

	if (ziXun) {
		html += "<li><div>";
		html += "<img  src=\"images/home_icon_news.png\">";
		html += "<span class=\"home-main-item-text\">资讯公告</span>";
		html += "</div></li>";
	}

	// 是否显示线路列表
	if (allLine) {
		html += "<li><div>";
		html += "<img  src=\"images/home_icon_line.png\">";
		html += "<span class=\"home-main-item-text\">线路列表</span>";
		html += "</div></li>";
	}

	if (showCustom) {
		html += "<li onclick=\"toCustombus()\"><div>";
		html += "<img  src=\"images/home_icon_dingzhigongjiao.png\">";
		html += "<span class=\"home-main-item-text\">定制公交</span>";
		html += "</div></li>";
	}
	//响铃公交
	if (showXianglingbus) {
		html += "<li onclick=\"toXianglingbus()\"><div>";
		html += "<img src=\"images/home_icon_baoche.png\">";
		html += "<span class=\"home-main-item-text\">响应式公交</span>";
		html += "</div></li>";
	}
	//网约公交
	if (showWangyuebus) {
		html += "<li onclick=\"toWangyuebus()\"><div>";
		html += "<img  src=\"images/home_icon_wgj.png\">";
		html += "<span class=\"home-main-item-text\">点呼公交</span>";
		html += "</div></li>";
	}
	html += "</ul>";

	html += "<ul class=\"tab-content\">";
	if (nearFlag) {
		html += "<li id=\"near-bar-content\"><div id=\"index-text\">附近</div></li>";
	}
	html += "<li><div class=\"home-search-view\">";
	html += "<img class=\"home-search-view-img\" src=\"images/icon_search.png\">";
	html += "<input type=\"text\" id=\"input_line\" placeholder=\"请输入线路、站点\" oninput=\"searchLineAnStation();\">";
	html += "</div></li>";

	html += "<li><div class=\"home-search-view\">";
	html += "<img class=\"home-search-view-qiehuan\" src=\"images/home_icon_qiehuan.png\">";
	html += "<div class=\"home-search-view-box\">";
	html += "<input type=\"text\" id=\"input_start\" placeholder=\"请输入起点\" oninput=\"searchTransferPoint(1);\">";
	html += "<div class=\"home-search-view-box-line\"></div>";
	html += "<input type=\"text\" id=\"input_end\" placeholder=\"请输入终点\" oninput=\"searchTransferPoint(2);\">";
	html += "</div></div></li>";
	html += "<li></li>";
	html += "</ul>";
	$("#home-main").html(html);

	// 展示历史记录
	$("#history").removeClass("history");
	$("#history").addClass("history-none");

	// if (nearFlag) {
	// getLocation();
	// } else {
	tabShow(0);
	// }
	// }

	$(".home-main-view li").each(function(index) {
		$(this).click(function() {
			if ((showCustom || showWangyuebus || showXianglingbus) && index == 3) {
				return;
			}
			tabShow(index);
			sessionStorage.setItem("tab-index", index);
		});
	})
	// showHistory(nowtype);
	searText();
}



function tabShow(index) {

	// 清空列表
	$("#list-view").html("");
	if (MZTstyle) {
		$(".home-main-view div.active-img-mzt").removeClass("active-img-mzt");
		$(".home-main-view div").eq(index).addClass("active-img-mzt");
	} else {
		$(".home-main-view div.active-img").removeClass("active-img");
		$(".home-main-view div").eq(index).addClass("active-img");
	}


	$(".tab-content li.active-view").removeClass("active-view");
	$(".tab-content li").eq(index).addClass("active-view");

	$("#list-view").attr("style", "display:none");
	$("#near-list-view").attr("style", "display:none");
	$("#new-list-view").attr("style", "display:none");
	$("#all-list-view").attr("style", "display:none");

	// 展示历史记录
	$("#history").removeClass("history");
	$("#history").addClass("history-none");
	$("#xijiu-company").attr("style", "display:none");
	if (nearFlag) {
		// 附近站点
		if (index == 0) {
			$("#near-list-view").attr("style", "display:block");
			getLocation();
		} else if (index == 1) {// 线路站点
			/*	$("#list-view").attr("style", "display:block");
				nowtype = 1;
				showHistory(nowtype);*/
			$("#list-view").attr("style", "display:block");
			if ("xijiu101401" == localStorage.getItem("citykey")) {
				$("#xijiu-company").attr("style", "display:flex");
			} else {
				nowtype = 1;
				showHistory(nowtype)
			}
		} else if (index == 2) {
			$("#list-view").attr("style", "display:block");
			nowtype = 3;
			showHistory(nowtype);
		} else if (index == 3) {
			$("#new-list-view").attr("style", "display:block");
			getNewsList();
		} else if (index == 4) {
			$("#all-list-view").attr("style", "display:block");
			searchAllLine('', 0);
		}
	} else {
		if (index == 0) {// 线路站点
			$("#list-view").attr("style", "display:block");
			nowtype = 1;
			showHistory(nowtype);
		} else if (index == 1) {
			$("#list-view").attr("style", "display:block");
			nowtype = 3;
			showHistory(nowtype);
		} else if (index == 2) {
			$("#new-list-view").attr("style", "display:block");
			getNewsList();
		} else if (index == 3) {
			$("#all-list-view").attr("style", "display:block");
			searchAllLine('', 0);
		}
	}
}

function setConfig2() {

	var html = "";
	html = "<ul class=\"home-main-view\">";

	html += "<li><div>";
	html += "<img  src=\"images/home_icon_line.png\">";
	html += "<span class=\"home-main-item-text\">线路列表</span>";
	html += "</div></li>";

	html += "<li id=\"near-bar\"><div>";
	html += "<img  src=\"images/home_icon_near.png\">";
	html += "<span class=\"home-main-item-text\">附近站点</span></div></li>";

	html += "<li><div>";
	html += "<img  src=\"images/home_icon_search.png\">";
	html += "<span class=\"home-main-item-text\">线路站点</span></div></li>";

	html += "<li><div>";
	html += "<img  src=\"images/home_icon_qunan.png\">";
	html += "<span class=\"home-main-item-text\">去哪查询</span></div></li>";


	html += "<li><div>";
	html += "<img  src=\"images/home_icon_news.png\">";
	html += "<span class=\"home-main-item-text\">资讯公告</span>";
	html += "</div></li>";

	html += "</ul>";

	html += "<ul class=\"tab-content\">";

	html += "<li></li>";

	html += "<li id=\"near-bar-content\"><div id=\"index-text\">附近</div></li>";

	html += "<li><div class=\"home-search-view\">";
	html += "<img class=\"home-search-view-img\" src=\"images/icon_search.png\">";
	html += "<input type=\"text\" id=\"input_line\" placeholder=\"请输入线路、站点\" oninput=\"searchLineAnStation();\">";
	html += "</div></li>";

	html += "<li><div class=\"home-search-view\">";
	html += "<img class=\"home-search-view-qiehuan\" src=\"images/home_icon_qiehuan.png\">";
	html += "<div class=\"home-search-view-box\">";
	html += "<input type=\"text\" id=\"input_start\" placeholder=\"请输入起点\" oninput=\"searchTransferPoint(1);\">";
	html += "<div class=\"home-search-view-box-line\"></div>";
	html += "<input type=\"text\" id=\"input_end\" placeholder=\"请输入终点\" oninput=\"searchTransferPoint(2);\">";
	html += "</div></div></li>";

	html += "</ul>";
	$("#home-main").html(html);

	// 展示历史记录
	$("#history").removeClass("history");
	$("#history").addClass("history-none");

	// if (nearFlag) {
	// getLocation();
	// } else {
	tabShow2(0);
	// }
	// }

	$(".home-main-view li").each(function(index) {
		$(this).click(function() {
			tabShow2(index);
			sessionStorage.setItem("tab-index", index);
		});
	})
	// showHistory(nowtype);
	searText();
}

function tabShow2(index) {

	// 清空列表
	$("#list-view").html("");

	if (MZTstyle) {
		$(".home-main-view div.active-img-mzt").removeClass("active-img-mzt");
		$(".home-main-view div").eq(index).addClass("active-img-mzt");
	} else {
		$(".home-main-view div.active-img").removeClass("active-img");
		$(".home-main-view div").eq(index).addClass("active-img");
	}

	$(".tab-content li.active-view").removeClass("active-view");
	$(".tab-content li").eq(index).addClass("active-view");

	$("#list-view").attr("style", "display:none");
	$("#near-list-view").attr("style", "display:none");
	$("#new-list-view").attr("style", "display:none");
	$("#all-list-view").attr("style", "display:none");

	// 展示历史记录
	$("#history").removeClass("history");
	$("#history").addClass("history-none");
	$("#leshan-company").attr("style", "display:none");

	// 附近站点
	if (index == 1) {
		$("#near-list-view").attr("style", "display:block");
		getLocation();
	} else if (index == 2) {// 线路站点
		$("#list-view").attr("style", "display:block");
		nowtype = 1;
		showHistory(nowtype);
	} else if (index == 3) {
		$("#list-view").attr("style", "display:block");
		nowtype = 3;
		showHistory(nowtype);
	} else if (index == 4) {
		$("#new-list-view").attr("style", "display:block");
		getNewsList();
	} else if (index == 0) {
		$("#all-list-view").attr("style", "display:block");
		if ('leshan000119' == localStorage.getItem("citykey")) {
			searchAllLineLeShan("市中区");
			$("#leshan-company").attr("style", "display:flex");
		} else {
			searchAllLine('', 1);
		}
	}

}

//function mapZoomstart() {
//	document.querySelector("#commonts").innerText = '缩放开始';
//}
//function mapZoom() {
//	document.querySelector("#commonts").innerText = '正在缩放';
//}
/*function mapZoomend() {
	document.querySelector("#commonts").innerText = '缩放结束-' + map.getZoom();;
}*/

$(function() {
	showTitle();
	map = new AMap.Map('container', {
		//zoom: 14,
		//addOns: ['moveAnimation']
	});
	//map.on('zoomstart', mapZoomstart);
	//map.on('zoomchange', mapZoom);
	//map.on('zoomend', mapZoomend);

	var cityname = localStorage.getItem("cityName");
	$("#cityName").html(cityname);
});