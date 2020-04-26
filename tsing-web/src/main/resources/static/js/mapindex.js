var map = new BMap.Map("map", {
		enableMapClick: false
	});
map.setCurrentCity("深圳"); 
map.enableScrollWheelZoom(); //启用滚轮放大缩小，默认禁用
map.enableContinuousZoom();
map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]}));
map.setMapStyle({
	styleJson:[
		{
			"featureType": "poilabel",
			"elementType": "all",
			"stylers": {
				"visibility": "off"
            }
		},
		{
			"featureType": "manmade",
            "elementType": "all",
            "stylers": {
            	"visibility": "off"
            }
		},
		{
			"featureType": "town",
			"elementType": "all",
			"stylers": {
				"visibility": "off"
			}
        },
        {
            "featureType": "subway",
            "elementType": "all",
            "stylers": {
                      "visibility": "off"
            }
        },{
            "featureType": "highway",
            "elementType": "all",
            "stylers": {
                      "visibility": "off"
            }
        },
        {
            "featureType": "arterial",
            "elementType": "all",
            "stylers": {
                      "visibility": "off"
            }
        }
	]
});
var overlays = [];
var street_polygon_style = {  
	    strokeColor:"yellow",    //边线颜色。  
	    strokeWeight: 8,       //边线的宽度，以像素为单位。  
	    strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。  
	    fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。  
	    strokeStyle: 'dashed' //边线的样式，solid或dashed。  
	}
var street_label_style = {color : "#f8a600", fontSize : "24px", backgroundColor :"0.05",border :"0", fontWeight :"bold" }

var community_polygon_style = {  
	    strokeColor:"#df0000",    //边线颜色。  
	    strokeWeight: 3,       //边线的宽度，以像素为单位。  
	    strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。  
	    fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。  
	    strokeStyle: 'dashed' //边线的样式，solid或dashed。  
	}
var community_label_style = {color : "#d31b0a", fontSize : "18px", backgroundColor :"0.05",border :"0", fontWeight :"bold" }

var grid_polygon_style = {  
		strokeColor:"#4a86e8",    //边线颜色。  
		strokeWeight: 1,       //边线的宽度，以像素为单位。  
		strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。  
		fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。  
		strokeStyle: 'solid' //边线的样式，solid或dashed。  
}
var grid_label_style = {color : "#61d2d2", fontSize : "12px", backgroundColor :"0.05",border :"0", fontWeight :"bold" }

var building_polygon_style = {  
		strokeColor:"#000",    //边线颜色。  
		fillColor: "#98dcf3",
		strokeWeight: 0.5,       //边线的宽度，以像素为单位。  
		strokeOpacity: 0.5,    //边线透明度，取值范围0 - 1。  
		fillOpacity: 0.5,      //填充的透明度，取值范围0 - 1。  
		strokeStyle: 'solid' //边线的样式，solid或dashed。  
}
var building_label_style = {color : "#000",opacity: "0.5", fontSize : "12px", backgroundColor :"0.05",border :"0", fontWeight :"100" }

var druger_label_style = {color : "#000",fontSize : "12px", backgroundColor :"0.05",border :"0", fontWeight :"100" }

var draw_style = {  
		strokeColor:"red",    //边线颜色。  
	    strokeWeight: 3,       //边线的宽度，以像素为单位。  
	    strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。  
	    fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。  
	    strokeStyle: 'solid' //边线的样式，solid或dashed。  
	} 
//实例化鼠标绘制工具  
var drawingManager = new BMapLib.DrawingManager(map, {  
    isOpen: false, //是否开启绘制模式  
    drawingToolOptions: {  
        anchor: BMAP_ANCHOR_TOP_RIGHT, //位置  
        offset: new BMap.Size(5, 5), //偏离值  
    },  
    circleOptions: draw_style, //圆的样式  
    polylineOptions: draw_style, //线的样式  
    polygonOptions: draw_style, //多边形的样式  
    rectangleOptions: draw_style //矩形的样式  
});    

function draw(type){  
    drawingManager.open();   
    drawingManager.setDrawingMode(type);  
};  
//获得中心点
function getCenterPoint(path){
    var x = 0.0;
    var y = 0.0;
    for(var i=0;i<path.length;i++){
         x=x+ parseFloat(path[i].lng);
         y=y+ parseFloat(path[i].lat);
    }
    x=x/path.length;
    y=y/path.length;
    return new BMap.Point(x,y);
}