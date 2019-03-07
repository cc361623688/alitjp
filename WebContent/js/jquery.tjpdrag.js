
//我觉得这个插件绝对可以做一期视频，因为我现在也有点看不懂了
;(function($) {
	$.tjpDrag = $.noop;
	$.fn.tjpDrag = function(options) {

		var defaults = {
	        tmd: 82,//透明度
	        fixids:[],//哪些id的元素不参与拖动和交换
	        "z-index":99999,
	        sudu:500,//元素交换时的动画速度
	        beforeswap:function(old,nuu){},//在交换元素之前干点事，调用这个函数时已经确定要交换两个元素了
			afterswap:function(){}//在交换元素之后干点事，调用这个函数时，交换的动画都已经完成了
	    };
		var settings = $.extend({},defaults, options||{});//将一个空对象做为第一个参数

		
		var g_clked = false;//是否点击过鼠标左键
		var g_msposl =0;//点击鼠标左键时的鼠标绝对定位left
		var g_pgxLast =0;//用来判断是左移还是右移
		
		var g_cure=null;//当前正在操作的元素
		
		var test=0;
		
		var setTmd  =function(e, t){
			if (e==null)return;
			if (t==""){
				e.css({
					filter:"alpha(opacity='')",
					opacity:'',
					"-moz-opacity":'',
					"-khtml-opacity":'',
					"z-index":'auto'
				})
			}else{
				try{
					t = parseInt(settings.tmd);
				}catch(e){
					t = defaults.tmd;
				}
				if (isNaN(t)) t=defaults.tmd;
				if (!(t>=0 && t<=100)){
					t  =defaults.tmd;
				}
				var xiaos = t/100.0;
				e.css({
					filter:"alpha(opacity="+t+")",
					opacity:xiaos,
					"-moz-opacity":xiaos,
					"-khtml-opacity":xiaos,
					"z-index":settings["z-index"]
				})
			}
		}

		//判断是否是固定的元素
		var isfix = function(e){
			for (j=0;j<settings.fixids.length;j++){
				if (settings.fixids[j] == e.attr('id')){
					return true;
				}
			}
			
			return false;
		};
		
		//判断移动过程中是否需要交换元素了
		var nedswap = function( isleft){
			if (isleft){
				if (g_cure.index()==0) return null;//第一个肯定没法再和左边的交换了
				if (isfix(g_cure.prev())) return null;//左边那个是固定的
				if (g_cure.offset().left+5>=(2*g_cure.prev().offset().left+g_cure.prev().width())/2) return null;//还未超过一半再偏移一点，防止鼠标位置飘
				return g_cure.prev()
			}else{
				if (g_cure.index()==g_cure.siblings().size()) return null;
				if (isfix(g_cure.next())) return null;
				if ((g_cure.offset().left+g_cure.width()-5)<=(2*g_cure.next().offset().left+g_cure.next().width())/2) return null;//还未超过一半
				return g_cure.next()
			}

			return null;
		};
		var swapele = function(tare ,isleft){
			
			var tarcl = tare.offset().left;
			var curcl = g_cure.offset().left
			//console.log("(tare.is(:animated)="+tare.is(":animated"))
			if (tare.is(":animated")){//动画中
				tare.stop()//直接终止动画，不再动了
			}
			//test++;

			if (isleft){
				g_cure.insertBefore(tare);
			}else{
				tare.insertBefore(g_cure)
			}
			
			var cecssl = parseInt(g_cure.css("left"))
			if (isNaN(cecssl)) cecssl=0
			var trcssl = parseInt(tare.css("left"))
			if (isNaN(trcssl)) trcssl=0
			var mgn = curcl-(g_cure.offset().left-cecssl);
			g_cure.css("left", mgn);
			//console.log(" tarcl-tare.offset().left+trcssl"+(tarcl-tare.offset().left+trcssl))
			tare.css("left", tarcl-tare.offset().left+trcssl).animate({"left":0},settings.sudu, settings.afterswap)
			
			return mgn
		};

		return this.each(function() {

			//鼠标左键单击
			$(this).mousedown(function(e){
				if(1 == e.which){//鼠标左键事件
					if (isfix($(this))) return;
					g_clked = true;
					g_cure = $(this);
					$("body").data("___tjpdragcure", g_cure);
					g_msposl = e.pageX;
					g_pgxLast = -1;
				} 
			})
			
			//鼠标移动
			$(this).parent().mousemove(function(ev){
				if(!g_clked){return;}
				
				//if(test>=1){return;}

				//console.log("ev.pageX="+ev.pageX)
				//console.log("g_pgxLast="+g_pgxLast)
				var off = ev.pageX- g_msposl;//鼠标当前位置与点击时位置的差值
				if (off!=0 && ev.pageX!=g_pgxLast){
					var isleft = (ev.pageX-g_pgxLast<0);
					if (isleft && g_cure.position().left<=0){//到了左边缘
						
					}else if (!isleft && g_cure.position().left+g_cure.outerWidth(true)>=g_cure.parent().width()){//到了右边缘
						
					}else{
						//console.log("mousemove g_cure.offleft="+g_cure.offset().left)
						//console.log("off ="+off)
						setTmd(g_cure)
						g_cure.css({left:off})
						var tare = nedswap(isleft);
						if (tare!=null){
							//console.log("nedswap"+g_cure.offset().left)
							//alert($(this)[0].outerHTML)
							//alert(g_cure[0].outerHTML)
							settings.beforeswap($(this).children("li").index(g_cure), $(this).children("li").index(tare));
							var mgn = swapele(tare,isleft);//拖动到目标元素的一半位置了，就把这两个元素交换一下
							g_msposl = ev.pageX - mgn;
						}
					}
					g_pgxLast = ev.pageX;
				}else{//是垂直上下移动或者回到了原始位置
					
				}
				
				//如果移动时Y方向到了上下边缘，默认结束拖动
				if (ev.pageY<=$(this).offset().top || ev.pageY>=$(this).offset().top+$(this).children(":eq(0)").height()-1){
					$(this).trigger('mouseup', [1])
				}
			})

			$("body").mousemove(function(ev){
				if (g_clked && g_cure!=null){
					var l = g_cure.offset().left, t = g_cure.offset().top, r = l+g_cure.width(), b = t+g_cure.height();
					if (ev.pageX< l || ev.pageX> r || ev.pageY< t ||ev.pageY > b){//鼠标在元素之外
						//console.log("mouseupmouseup,body")
						g_cure.parent().trigger('mouseup', [1])
					}
				}
			})
			
			$(window).blur(function(){
				if (g_clked && g_cure!=null){
					g_cure.parent().trigger('mouseup', [1])
				}
			})

			//鼠标左键弹起
			$(this).parent().mouseup(function(e, which){
				if(1 == e.which || which==1){//鼠标左键事件
					g_clked = false;
					if (g_cure!=null){
						setTmd(g_cure,"")
						if (g_cure.is(":animated")){//动画中
							g_cure.stop()
						}
						g_cure.animate({left:0}, settings.sudu)
						g_cure = null
						$("body").data("___tjpdragcure", null)
					}
				}
			})
			
		})
	};
	$.extend($.tjpDrag, {
		cureReturn: function() {
			var ___tjpdragcure  = $("body").data("___tjpdragcure")
			if (___tjpdragcure) {
				___tjpdragcure.parent().trigger('mouseup', [1])
			}		
		}
	});
})(jQuery);