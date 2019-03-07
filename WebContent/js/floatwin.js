//引入script的时候可以接受如下参数  
//<script type="text/javascript" src="float.js?id=id&stepx=stepx&stepy=stepy&delay=delay"></script>  
//id  飘窗元素id  
//stepx  每经过delay时间，水平方向上移动stepx个像素  
//stepy  每经过delay时间，竖直方向上移动stepy个像素  
//delay  每次移动的时间间隔，以ms为单位  
  
var id, stepX, stepY, delay, left = 0, top1 = 0, objWidth, objHeight, bodyWidth, bodyHeight, directionX = "right", directionY = "down", floatObj;  
//解析js文件后面的参数  
var getArgs = (function(){  
var sc=document.getElementsByTagName("script");  
var paramsArr=sc[sc.length-1].src.split('?')[1].split('&');  
var args={},argsStr=[],param,t,name,value;  
for(var ii=0,len=paramsArr.length;ii<len;ii++){  
param=paramsArr[ii].split('=');  
name=param[0],value=param[1];  
if(typeof args[name]=="undefined"){ //参数尚不存在  
args[name]=value;  
}else if(typeof args[name]=="string"){ //参数已经存在则保存为数组  
args[name]=[args[name]]  
args[name].push(value);  
}else{ //已经是数组的  
args[name].push(value);  
}  
}  
return function(){return args;} //以json格式返回获取的所有参数  
})();  
  
var move = function() {  
    //判断移动方向  
    if(directionX == "right") {  
        if((left + objWidth + stepX) > bodyWidth) {  
            directionX = "left";  
        }  
    } else {  
        if((left - stepX) < 0) {  
            directionX = "right";  
        }  
    }  
      
    if(directionY == "down") {  
        if((top1 + objHeight + stepY) > bodyHeight) {  
            directionY = "up";  
        }  
    } else {  
        if((top1 - stepY) < 0) {  
            directionY = "down";  
        }  
    }  
      
    //移动  
    if(directionX == "right") {  
        left += stepX;  
    } else {  
        left -= stepX;  
    }  
    if(directionY == "down") {  
        top1 += stepY;  
    } else {  
        top1 -= stepY;  
    }  
    floatObj.style.left = left + "px";  
    floatObj.style.top = top1 + "px";  
};  
  
var start = function() {  
    interval = setInterval('move()', delay);  
};  
  
//获取参数  
var params = getArgs();  
id = params.id;  
stepX = parseFloat(params.stepx);  
stepY = parseFloat(params.stepy);  
delay = parseFloat(params.delay);  
  
window.addEventListener("load", function() {  
    floatObj = document.getElementById(id);  
    objWidth = parseFloat(floatObj.style.width);  
    objHeight = parseFloat(floatObj.style.height);  
    floatObj.style.position = "fixed";  
    floatObj.style.zIndex = 99999999;  
    floatObj.style.left = left + "px";  
    floatObj.style.top = top1 + "px";  
      
    bodyWidth = parseFloat(document.body.clientWidth);  
    bodyHeight = parseFloat(document.body.clientHeight);  
  
    start();  
      
    floatObj.addEventListener("mouseover", function(){clearInterval(interval)});  
    floatObj.addEventListener("mouseout", function(){interval=setInterval('move()', delay)});  
});  