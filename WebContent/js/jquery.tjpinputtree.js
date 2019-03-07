
//借花献佛
//直接给list就行，如果给的是tree。。。，我暂不处理
;(function($) {
	$.tjpinputtree = $.noop;
	$.fn.tjpinputtree = function(list, selid, afterclick) {
		
		//如果给了初始选中id。则从列表中查出并设置input的文字
		if (list!=null && selid!=null){
			$(this).data("___id", selid)
			for(var i=0;i<list.length;i++){
				if (list[i].id==selid){
					$(this).val(list[i].name)
					break;
				}
			}
		}
		
		var ___onclick = function(event, treeId, treeNode){
			var t = $.fn.zTree.getZTreeObj(treeId)
			var eipt = $(t).data("___input")
			if (eipt.val() == treeNode.name){//点击已经选中的，则取消选中，这是个最简便的方法
				eipt.val("").data("___id", "")
				t.selectNode(null)//树节点也要取消选中
			}else{
				eipt.val(treeNode.name).data("___id", treeNode.id)
			}
			$(".___clstjptree").remove();
			if (afterclick && typeof(afterclick)=="function"){
				afterclick()
			}
		}
		
		//点击后出来树型控件
		$(this).click(function(e) {
			if ($(".___clstjptree").length>0) {
				$(".___clstjptree").remove()
			}else{
				var d = $("<div class='___clstjptree' style='border-radius:2px;overflow:auto;margin-top:2px;position:absolute;max-height:310px;height:310px;background:rgb(253,253,253);z-index:9999999;border:1px solid #ccc'></div>")
				d.css("top",$(this).offset().top+$(this).outerHeight()).css("left", $(this).offset().left).css("width",$(this).width())
				var h = $(window).height()-parseInt(d.css("top"))-2-4-2//2像素距离input,4像素距离文档底边，2像素边框宽度
				if (h<300) d.css("height",h)
				d.append("<div style='overflow:auto;max-height:"+parseInt(d.css("height"))+"px'><ul class='ztree' style='padding:0' id='___tjpitid"+$(".___clstjptree").size()+"'></ul></div>")
				$("body").append(d)
				var setting = {
					data: {simpleData: {enable: true}},//这个是默认不需要转json格式，直接从数据库读list就行，还能转字段名
					callback: {onClick: ___onclick},
				};
	       		var treeObj = $.fn.zTree.init(d.find("ul"), setting, list);
	       		treeObj.setting.view.expandSpeed = "";//展开是动画，此时取树的高度是展开之前的，就不准
	       		treeObj.expandAll(true);
	       		treeObj.setting.view.expandSpeed = "fast";//https://tieba.baidu.com/p/2210467721?red_tag=0616035354
	       		if (d.find("ul").height()<parseInt(d.css("height"))){
	       			d.css("height", d.find("ul").height()+2)
	       			d.css("max-height", parseInt(d.css("height")))
	       		}
	       		$(treeObj).data("___input", $(this))
	       		
	       		//差点忘了，还得选中啊
	       		var sn=$(this).data("___id");
	       		if (sn==undefined && selid) { 
	       			sn = selid;//设置了初始值
	       			$(this).data("___id", sn)
	       		}
	       		treeObj.selectNode(treeObj.getNodeByParam("id", sn));
			}
		})
		
		$("body").find(":not(.___clstjptree)").mousedown(function(ev){
			$(".___clstjptree").remove();
		})
	};
})(jQuery);