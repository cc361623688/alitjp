/**
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * 2018-05-03：Htgl也就是后台管理
 */
package com.tjpcms.spring.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ndktools.javamd5.Mademd5;
import com.tjpcms.cfg.XT;
import com.tjpcms.common.Aevzd;
import com.tjpcms.common.CL;
import com.tjpcms.common.HS;
import com.tjpcms.common.HT;
import com.tjpcms.common.QX;
import com.tjpcms.common.Crud.Crud;
import com.tjpcms.spring.model.Usrpt;

@Controller
@RequestMapping("/"+XT.htgllj)
public class HtglController extends TjpcmsController{
	private static final Log logger = LogFactory.getLog(HtglController.class);

	
//===================================================================================================================================
//	tjpcms的后台内置功能相关开始
//===================================================================================================================================
	//后台管理的首页
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HS.setGlobal(request);
		request.setAttribute("wdxxs", _e.cnt("select count(*) cnt from t_hd where delf=0 and yd=0"));//未读信息数

		return "adm/index_ad";
	}

	//后台“我的主页”
	@RequestMapping(value = "welcome")
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Thread.sleep(500000);
		HS.setGlobal(request);
		
		return new ModelAndView("adm/welcome", map);
	}

	//cms必备技能1：栏目维护
	@RequestMapping(value = "lanmu")
	public ModelAndView lanmu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("sel_sys0", JSONArray.fromObject(HS.fnsel("zdb", _e, "lanmuleixing", "sys=0")));
		m.put("sel_sys1", JSONArray.fromObject(HS.fnsel("zdb", _e, "lanmuleixing", "sys=1")));
		m.put("lcs",  JSONArray.fromObject(_e.r("select id val,mc txt from tjpcms_liucheng where jsids is not null or mc='无审核' or mc= '自定义'  order by px")));

		return new ModelAndView("adm/lanmu", m);
	}

	@RequestMapping(value = "getLmById")
	public void getLmById(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ServletException, IOException {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			Map<String, Object> obj = _e.obj("select url_d,url3,donly2,donly3,lcid,px from tjpcms_lanmu where id='"+id+"'");
			HS.flushResponse(response, JSONObject.fromObject(obj));
			return;
		}

		HS.flushResponse(response, "-1");
	}
	
	//【栏目列表】菜单里刷新栏目类型
	@RequestMapping(value = "aj_lmlxsx")
	public void aj_lmlxsx(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("ret",0);
		m.put("sel_sys0",HS.fnsel("zdb", _e, "lanmuleixing", "sys=0"));
		m.put("sel_sys1",HS.fnsel("zdb", _e, "lanmuleixing", "sys=1"));
		HS.flushResponse(response, JSONObject.fromObject(m));
	}

	//栏目列表页面初始化
	@RequestMapping(value = "getlmtree")
	public void getlmtree(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("all", _e.getLmTree(request.getParameter("showid")));

		String selid = request.getParameter("selid");
		if (StringUtils.isEmpty(selid)){
			selid = CL.TREE_ROOT_ID.toString();
		}
		m.put("dyc", _e.getLmList(Integer.valueOf(selid),0,15));
		int recTotal = _e.cntLmList(Integer.valueOf(selid));
		m.put("recTotal", recTotal);
		m.put("pgTotal", (int)Math.ceil(recTotal/(double)15));
		m.put("pg", 1);
		m.put("perPage",15);
		m.put("sel_sys0",HS.fnsel("zdb", _e, "lanmuleixing", "sys=0"));
		m.put("sel_sys1",HS.fnsel("zdb", _e, "lanmuleixing", "sys=1"));
		HS.flushResponse(response, JSONObject.fromObject(m));
	}

	//根据pid和页数来查询栏目列表
	@RequestMapping(value = "getlmlistpg")
	public void getlmlistpg(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		Map<String, Object> m = new HashMap<String, Object>();
		String pg = request.getParameter("pg");
		String pId = request.getParameter("pId");
		if (StringUtils.isEmpty(pg) || StringUtils.isEmpty(pId)){
			m.put("ret","-1");
		}else{
			Integer npid = 0,npg=0;
			try{
				npid = Integer.valueOf(pId);
				npg = Integer.valueOf(pg);
			}catch(Exception e){
				npid = 0;
				npg=0;
			}
			int recTotal = _e.cntLmList(npid);
			int pgTotal = (int)Math.ceil(recTotal/(double)15);
			if (!(npg>=1&& npg<=pgTotal)){
				m.put("ret","-1");
				m.put("recTotal",0);
				m.put("pgTotal",0);
			}else{
				m.put("ret","0");
				m.put("pg",pg);
				m.put("recTotal",recTotal);
				m.put("pgTotal",pgTotal);
				m.put("dyc", _e.getLmList(npid,(npg-1)*15,15));
			}
			m.put("perPage",15);
		}
		HS.flushResponse(response, JSONObject.fromObject(m));
	}

	@RequestMapping(value = "delLanmu")
	public void delLanmu(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		String id = request.getParameter("id");
		if (StringUtils.isEmpty(id) || _e.cnt("select count(*) from tjpcms_lanmu where pId='"+id+"'")>0) {
			HS.flushResponse(response, "-1");
			return;
		}

		HS.flushResponse(response, _e.del("delete from tjpcms_lanmu where id='"+id+"'")==1?"0":"-1");
	}

	//新增或编辑栏目
	@RequestMapping(value = "lmaddedit")
	public void lmaddedit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//如果是编辑栏目，要校验一下审核流程是否已经被应用了，如果是，则不能修改了
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {//是更新
			List<Map<String, Object>> r = _e.r("select * from tjpcms_lanmu where id='"+id+"'");//select * 和select xxx竟然还是有区别的
			if (CollectionUtils.isEmpty(r) || r.size()!=1){
				HS.flushResponse(response, "-1");
				return;
			}
			Object nrtbl = r.get(0).get("nrtbl");
			if (nrtbl!=null ){
				List<Map<String, Object>> tblZiduan = _e.getTblZiduan(nrtbl.toString(), CL.DB);
				boolean cidInDbcol = HS.zdInDbcol("cid", tblZiduan);
				boolean delfInDbcol = HS.zdInDbcol("delf", tblZiduan);
				if (cidInDbcol && _e.cnt("select count(*) from "+nrtbl.toString()+" where cid="+id+(delfInDbcol?" and delf=0 ":""))>0){//只查未逻辑删除的数量
					HS.flushResponse(response, "该栏目已有下属内容存在，将下属内容全部删除后方可修改！");
					return;
				}
			}
		}

		HS.flushResponse(response, HS.ppbc(_e, "tjpcms_lanmu", request));
	}

	//栏目列表新增
	@RequestMapping(value = "getlmmaxpx")
	public void getlmmaxpx(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		HS.flushResponse(response, HS.vpx(_e, "tjpcms_lanmu"));
	}

	//友情链接
	@RequestMapping(value = "yqlj")
	public ModelAndView yqlj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_yqlj", "友情链接");
		//o.getR().setPerPage(1);
		
		String cxs[][]= {{"网站名称","mc"},{"网址","wz"}};
		String hds[][]= {{"网站名称","mc"},{"网址","wz"},{"Logo","logo", "pic:未上传图片"},{"点击次数","cs"},{"排序","px"},{"更新时间","gx"}};
		HS.setList(request, o,hds, cxs);//查询的配置

		String aev[][]= {
				{"网站名称","mc","required"},
				{"网址","wz","\\http://","required   		http:请输入以http://或https://开头的网址   	  not_have_kg:网址不能含有空格"},//或者regexp#^[^\\s+]*$:提示语
				{"Logo","logo", Aevzd.PIC.toString()},
				{"点击次数","cs", Aevzd.TEXT.toString()},
				{"排序","px"}};	
		HS.setAev(request, o,aev);//aev的配置

		return new ModelAndView("adm/list", null);
	}

	//字典表(主子表类型的)
	@RequestMapping(value = "zdb")
	public ModelAndView zdb(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e,request, "tjpcms_zdb", "字典表");
		o.getAev().setNeedv(false);

		//查询的配置
		String cxs[][]= {{"字典表名称","mc"},{"字典表拼音","py"},{"字典表id","id","op:eq,like"}};
		String hds[][]= {{"字典表id","id"},{"名称","mc"},{"拼音","py"},
				{"字典项总数","cnt","sql:select count(*) from tjpcms_zdx where pId=t.id","style:width:7em"},
				{"字典项内容","zdxnr","sql:select group_concat(zdxmc) from (select * from tjpcms_zdx order by px desc)s where s.pId=t.id"},
				{"排序","px"},{"更新时间","gx"}};
		String czs[][]= {{"text:字典项","href:zdx.dhtml?mc=&id=","idx:0"},//操作排序按数字是几就放在第几个，默认位置是追加在后面
								{"text:gray:编辑:case:mc=栏目类型", "js:layer.msg('【栏目类型】为系统预置类型，不可编辑！')"},
								{"text:val:删除:case:cnt<=0#gray:删除:case:cnt>0","js:fn_cmn_del(this,id,删除所有字典项后才可删除该字典表！):listczq.js"}};
		HS.setList(request, o,hds, cxs, czs);

		//aev的配置
		String aev[][]= {
				{"字典表名称","mc","required unique"},
				{"字典表拼音","py","required unique","onfocus=fn_py2(this)=pinyin.js"},
				{"排序","px"}
		};
		HS.setAev(request, o,aev);

		return new ModelAndView("adm/list", null);
	}

	//字典项(主子表类型的)
	//这里的两个ArrayUtils.add其实也可以不写，并不十分影响业务逻辑，这里加上主要是为了演示crud配置的灵活性
	@RequestMapping(value = "zdx")
	public ModelAndView zdx(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String id = request.getParameter("id");
		String mc = request.getParameter("mc");
		Crud o = new Crud(_e,request, "tjpcms_zdx",HS.zzBread("返回上级字典表 - "+mc,"zdb.dhtml", "字典项"));
		o.getR().setExwhere("pId="+id);//要写在setList之前
		o.getAev().setHook_befgx("com.tjpcms.common.Hook.lmmcBaocunbef");
		o.getD().setHook_befdel("com.tjpcms.common.Hook.befDelZdx");

		//查询的配置
		String cxs[][]= {{"字典项名称","zdxmc"}, {"字典项拼音","zdxpy"},{"字典项值","zdxval"}};
		String hds[][]= {{"字典项id","id"},{"字典项名称","zdxmc"},{"字典项拼音","zdxpy"},{"字典项值","zdxval"}, {"系统预置","sys","text:zdb:shibushi"},{"备注","bz"},{"排序","px"},{"更新时间","gx"}};
		String czs[][]= {
			{"text:gray:编辑:case:sys=1", "js:layer.msg('【系统预置】为【是】的栏目不可编辑！')"},
			{"text:val:删除:case:sys=0#gray:删除:case:sys=1","js:fn_cmn_del(this,id,系统预置类型不可删除！):listczq.js"}};
		//栏目类型这个字典表中的字典项才需要“是否系统预置”这个选项
		if ("4".equals(id)){//是栏目类型这个字典表
			cxs = ArrayUtils.add(cxs, new String[]{"是否系统预置","sys","zdb:shibushi","def:eq:0"});
		}
		HS.setList(request, o,hds,cxs, czs);

		//aev的配置
		String aev[][]= {
				{"字典表名称",Aevzd.TEXT.toString(),mc},
				{"字典项名称","zdxmc","required unique#pId="+id+":字典项名称不能重复"},
				{"字典项拼音","zdxpy","required unique#pId="+id,"onfocus=fn_py3(this)=pinyin.js"},//第四种类型了，带js的
				{"字典项值","zdxval","required unique#pId="+id,"onfocus=fn_py(this)"},//上面写过pinyinjs，这里也要用到这个js，但是就可以不写了，方便吧
				{"排序","px"},
				{"备注","bz"},
				{"pId",Aevzd.HIDDEN.toString(),id}
		};
		//栏目类型这个字典表中，新增编辑查看时“是否系统预置”默认是否
		if ("4".equals(id)){//是栏目类型这个字典表
			aev = ArrayUtils.add(aev, 5,new String[]{"是否系统预置","sys",Aevzd.SELECT.toString(),"shibushi"});
		}
		HS.setAev(request, o,aev);
		
		return new ModelAndView("adm/list", null);
	}
	
	//通用文章
	//以通用文章为例，如果某个栏目需要审核，只要保证在【配置网站】-【栏目列表】中增加该栏目，并为其指定某个审核流程即可。实际上系统会为配置了审核流程的栏目自动加上配置项来完成审核流程的功能，只是这个过程不需要用户了解，但实现的原理还是crud配置。
	//具体实际上增加了如下的配置项
	//废弃：{"是否本人记录","sfbrjl","sql:case when t.uid='"+Ht.getUid(request)+"' then '是' else '否' end","style:display:none"},//这个display:none犀利了，就是有这个表头，但不显示，因此相应的数据列也不显示，但不影响查询
	//废弃：{"是否需要审核","needsh","sql:case when y.mc is null or y.mc='无审核' then 0 when t.shzt='未提交'  then 1  when y.jsids is null and y.mc!='自定义' then 2 else 3 end", "style:display:none"},
	@RequestMapping(value = "tywz")
	public String tywz(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_tywz", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}//下个版本里去掉这个判断，改为系统自动判断
		//o.getR().setJubu();//可以局部刷新也不可以不局部，看你的需要

		//查询的配置
		String cxs[][]= {{"标题","title","op:eq,like"},{"日期","gx"}};
		String hds[][]= {{"id","id"},{"标题","title"},{"点击次数","cs"},{"排序","px"},{"更新时间","gx"}};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		o.getAev().getExtjs().add("extaevjs.js");//这个只是是为了演示如何在前台保存校验之前或者之后进行自定义的校验，这样就可以不局限于aev中只能用正则来校验，当然后台也可以校验，这样前后台就齐全了
		o.getAev().setJs_bef_zdyjy("fn_test_jy1("+123+")");
		o.getAev().setJs_aft_zdyjy("fn_test_jy2()");
		String aev[][]= {
				{"标题","title","required"},
				{"内容","nr",Aevzd.RICH.toString(),"required"},
				{"排序","px"},
				{"缩略图","tu", Aevzd.PIC.toString()},
				{"摘要","zy",Aevzd.TEXTAREA.toString()},
				{"关键词","gjc"},
				{"备注","bz"},
				{"点击次数","cs",Aevzd.TEXT.toString()},
				{"更新日期","gx",Aevzd.DATE.toString()},
		};
		HS.setAev(request, o,aev);

		return "adm/list";
	}

	//图片类
	@RequestMapping(value = "tupianlei")
	public String tupianlei(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_pic", "图片");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}

		//查询的配置
		String cxs[][]= {{"标题","title"},{"日期","gx"},{"是否启用","flag1","zdb:sfqy"}};
		String hds[][]= {{"标题","title"},{"缩略图","tu", "pic:未上传图片"},{"是否启用","flag1","text:zdb:sfqy"},{"排序","px"},{"更新时间","gx"}};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		String aev[][]= {
				{"标题","title"},
				{"内容","nr", Aevzd.RICH.toString(),"required"},
				{"缩略图","tu", Aevzd.PIC.toString(),"required"},
				{"是否启用","flag1", Aevzd.SELECT.toString(),"required","zdb:sfqy"},//或者直接写sfqy，之所以可以不写前缀，是因为每行的字段是可以根据内容区分出来的，比如required肯定是校验
				{"排序","px"}
		};
		HS.setAev(request, o,aev);

		return "adm/list";
	}

	//点击父栏目，显示子栏目列表
	@RequestMapping(value = "fulanmu_zi")
	public String fulanmu_zi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_lanmu", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		o.getR().setExtjoinstr("left join tjpcms_zdx s on t.lx=s.zdxmc left join tjpcms_zdb d on s.pId=d.id");
		List<String> exjoinzdm = new ArrayList<String>();
		exjoinzdm.add("s.zdxpy py");
		o.getR().setExtjoinzdm(exjoinzdm);
		o.getR().setJubu();

		//查询的配置
		String cxs[][]= {{"栏目名称","name"},{"导航栏显示","nav","zdb:navxs"},{"栏目类型","lx","zdb:lanmuleixing"}};
		String hds[][]= {
			{"栏目id","id"},{"父栏目id","pId"},{"栏目名称","name"},{"导航栏显示","nav"},{"栏目类型","lx","ext:py:(:)"},{"前台url","url_d"},
			{"直接子栏目数","cnt","sql:select count(*) from tjpcms_lanmu where pId=t.id"},{"排序","px"},{"更新时间","gx"}
		};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		String br  =o.getBread();
		String aev[][]= {
			{"上级栏目",Aevzd.TEXT.toString(),br.substring(br.indexOf("：")+1, br.length())},
			{"栏目名称","name"},
			{"导航栏显示","nav",Aevzd.SELECT.toString(),"navxs"},
			{"栏目类型","lx"},
			{"排序","px"}
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}

	//配置系统预置的【富文本】类型
	@RequestMapping(value = "fuwenben")
	public String fuwenben(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_fwb", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}

		//aev的配置
		Map<String, Object> objw = HS.objw(_e, o.getTb(), "cid="+o.getCid());
		String id ="";	
		if (objw !=null) id = objw.get("id").toString();
		String aev[][]= {
				{"标题","title"},
				{"摘要","zy", Aevzd.TEXTAREA.toString()},
				{"内容","nr",Aevzd.RICH.toString(),"required"},
				{"图片","tu",Aevzd.PIC.toString()},
				{"id",Aevzd.HIDDEN.toString(),id}
		};
		HS.setAev(request, o,aev);
		request.setAttribute("obj", objw);

		return "adm/aev";//o.r.need默认为false，如果调用了Hanshu.setList，会设置为true
	}

	//单行文本（变量型），即始终只有一项
	@RequestMapping(value = "danhangwenben")
	public String danhangwenben(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_dhwb", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}

		//查询的配置
		String cxs[][]= {{"内容","title"}};
		String hds[][]= {
			{"内容","title"},
		};
		HS.setList(request, o,hds,cxs);

		//aev的配置
		Map<String, Object> objw = HS.objw(_e, o.getTb(), "cid="+o.getCid());
		String id ="";	
		if (objw !=null) id = objw.get("id").toString();//这样保证一个叶子栏目始终时能只能修改一条记录
		String aev[][]= {
				{"内容","title","required"},
				{"id",Aevzd.HIDDEN.toString(),id}
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}

	//单行文本（列表型），即可以有多项，形成列表
	@RequestMapping(value = "dhwblbx")
	public String dhwblbx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Crud o = new Crud(_e,request, "tjpcms_dhwb", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		//查询的配置
		String cxs[][]= {{"内容","title"}};
		String hds[][]= {
				{"id","id"},{"内容","title"},
		};
		HS.setList(request, o,hds,cxs);
		
		//aev的配置
		String aev[][]= {
				{"内容","title","required"},
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}

	//网站配置项(不可编辑)
	@RequestMapping(value = "cfg")
	public String cfg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_cfg", "配置项(不可编辑)");
		o.getAev().setNeede(false);
		o.getD().setNeed(false);
		o.getR().setExwhere("kbj=0");

		//查询的配置
		String cxs[][]= {{"配置项名称","mc"},{"配置项拼音","py"},{"配置项值","val"}};
		String hds[][]= {{"配置项名称","mc"},{"配置项拼音","py"},{"配置项值","val"},{"更新时间","gx"}};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		String aev[][]= {
				{"配置项名称","mc","required unique"},
				{"配置项拼音","py", "required unique","onfocus=fn_py1(this)=pinyin.js"},
				{"配置项值","val", Aevzd.ZDB.toString(),"zdy"},//配了这个类型，表里要有zdb这个字段
				{"kbj",Aevzd.HIDDEN.toString(),"0"}
		};
		HS.setAev(request, o,aev);

		return "adm/list";
	}

	//网站配置项(可编辑)
	@RequestMapping(value = "cfg_kbj")
	public String cfg_kbj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "tjpcms_cfg", "配置项(可编辑)");
		o.getR().setExwhere("kbj='1'");

		//查询的配置
		String cxs[][]= {{"配置项名称","mc"},{"配置项拼音","py"},{"配置项值","val"}};
		String hds[][]= {{"配置项名称","mc"},{"配置项拼音","py"},{"配置项值","val"},{"更新时间","gx"}};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		String aev[][]= {
				{"配置项名称","mc","required unique"},
				{"配置项拼音","py", "required unique","onfocus=fn_py1(this)=pinyin.js"},
				{"配置项值","val", Aevzd.ZDB.toString(),"zdy"},//配了这个类型，表里要有zdb这个字段。有zdy这个字符串的话最后就多了一个zdy这一项
				{"kbj",Aevzd.HIDDEN.toString(),"1"}
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}

	@RequestMapping(value = "zdbchange")
	public void zdbchange(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			List<Map<String, Object>> r = _e.r("select zdxmc val,zdxmc txt from tjpcms_zdx where pId='"+id+"'");
			if (CollectionUtils.isNotEmpty(r)) {
				HS.flushResponse(response, JSONArray.fromObject(r));
				return;
			}
		}

		HS.flushResponse(response, "-1");
	}

	//cms必备技能2：网站静态化
	@RequestMapping(value = "wzjth")
	public String wzjth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> r2 = _e.r("select py,val from tjpcms_cfg");
		for (Map<String, Object> e:r2) {
			request.setAttribute(((String)e.get("py")).toLowerCase(),e.get("val"));
		}
		request.setAttribute("bread", "当前位置：网站静态化");

		return "adm/wzjth";
	}

	//一键静态化
	@RequestMapping(value = "aj_yjjth")
	public void aj_yjjth(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ServletException, IOException {
		String gly = HT.getCjgly(request);
		if (StringUtils.isEmpty(gly) || "test".equalsIgnoreCase(gly)){
			HS.flushResponse(response, "抱歉，测试账户不可以改动数据");
			return;
		}

		boolean[] baret = {false};
		aj_syjth(request, response,baret);//首页静态化
		if (!baret[0]) {
			HS.flushResponse(response, "-1");
			return;
		}
		baret[0] = false;
		aj_lmjth(request, response,baret);//栏目静态化
		if (!baret[0]) {
			HS.flushResponse(response, "-1");
			return;
		}
		baret[0] = false;
		aj_lmnrjth(request, response,baret);//栏目内容静态化
		if (!baret[0]) {
			HS.flushResponse(response, "-1");
			return;
		}
		
		HS.flushResponse(response, "0");
	}
		
	//首页静态化
	@RequestMapping(value = "aj_syjth")
	public void aj_syjth(HttpServletRequest request, HttpServletResponse response, boolean[] flush) throws ServletException, IOException   {
		String gly = HT.getCjgly(request);
		if (StringUtils.isEmpty(gly) || "test".equalsIgnoreCase(gly)){
			HS.flushResponse(response, "抱歉，测试账户不可以改动数据");
			return;
		}

		String jth = request.getParameter("jth");
		if ("1".equals(jth)){//静态化
			final ByteArrayOutputStream os = new ByteArrayOutputStream();  
			final ServletOutputStream stream = new ServletOutputStream() {  
	            public void write(byte[] data, int offset, int length) {    os.write(data, offset, length);  }  
	            public void write(int b) throws IOException {    os.write(b);   }  
	        };
	        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));  
	        HttpServletResponse rep = new HttpServletResponseWrapper(response) {  
	            public ServletOutputStream getOutputStream() {    return stream;   }
	            public PrintWriter getWriter() {   return pw;   }
	        };

		    File mulu = new File(request.getServletContext().getRealPath("")+File.separator+"static"+File.separator);
			if (!mulu.exists()){
				mulu.mkdirs();
			}
			ServletContext servletContext = request.getServletContext();
			RequestDispatcher rd = servletContext.getRequestDispatcher("/index.dhtml?___opjth=1&___url_d=/");
			rd.include(request, rep);
			pw.flush();
			String path = request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"index.html";
			FileOutputStream fos = new FileOutputStream(path);  
	        os.writeTo(fos);
	        boolean succ = _e.upd("update tjpcms_cfg set val='静态化' where py='syjth' ")==1;
			if (flush==null || flush[0]) {
				HS.flushResponse(response, succ?"0":"-1");
			}else{
				flush[0] = succ;
			}
		}else if ("0".equals(jth)){//动态化
			boolean succ = _e.upd("update tjpcms_cfg set val='动态化' where py='syjth' ")==1;
			if (flush==null || flush[0]) {
				HS.flushResponse(response, succ?"0":"-1");
			}else{
				flush[0] = succ;
			}
		}else{
			if (flush==null || flush[0]) {
				HS.flushResponse(response, "-1");
			}else{
				flush[0] = false;
			}
		}
	}

	//栏目静态化
	@RequestMapping(value = "aj_lmjth")
	@Transactional
	public void aj_lmjth(HttpServletRequest request, HttpServletResponse response,  boolean[] flush) throws InterruptedException, ServletException, IOException {
		String gly = HT.getCjgly(request);
		if (StringUtils.isEmpty(gly) || "test".equalsIgnoreCase(gly)){
			HS.flushResponse(response, "抱歉，测试账户不可以改动数据");
			return;
		}
		
		String jth = request.getParameter("jth");
		if ("1".equals(jth)){//静态化
			//遍历lanmu表，对填写了url字段的栏目进行静态化
			File mulu = new File(request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"lanmu"+File.separator);
			if (!mulu.exists()){
				mulu.mkdirs();
			}
			ServletContext servletContext = request.getServletContext();
			//查出需要静态化的的栏目，外部链接不参与栏目静态化
			List<Map<String, Object>> r =  _e.r("select distinct url_d from tjpcms_lanmu where url_d is not null and  url_d!='' and donly2='否' and lx!='链接' ");
			for (int i = 0; i < r.size(); i++) {
				if (r.get(i)==null || r.get(i).get("url_d")==null || StringUtils.isEmpty(r.get(i).get("url_d").toString()))continue;
				final ByteArrayOutputStream os = new ByteArrayOutputStream();  
				final ServletOutputStream stream = new ServletOutputStream() {
					public void write(byte[] data, int offset, int length) {    os.write(data, offset, length);  }  
					public void write(int b) throws IOException {    os.write(b);   }  
				};
				final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));  
				HttpServletResponse rep = new HttpServletResponseWrapper(response) {  
					public ServletOutputStream getOutputStream() {    return stream;   }
					public PrintWriter getWriter() {   return pw;   }
				};
				String url = (String)r.get(i).get("url_d");
				RequestDispatcher rd = servletContext.getRequestDispatcher("/"+url+".dhtml?___opjth=1&___url_d="+url);
				rd.include(request, rep);
				pw.flush();
				FileOutputStream fos = new FileOutputStream(request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"lanmu"+File.separator+url+".html");
				os.writeTo(fos);
			}
			int upd1 = _e.upd("update tjpcms_cfg set val='静态化',gx='"+CL.fmt.format(new Date())+"' where py='lmjth' ");
			int upd2 = _e.upd("update tjpcms_lanmu set url=url_s where url is not null and  url!='' and donly2='否' and lx!='链接'");
			if (flush==null || flush[0]) {
				HS.flushResponse(response, (upd1==1 && upd2==r.size())?"0":"-1");
			}else{
				flush[0] = (upd1==1 && upd2==r.size());
			}
		}else if ("0".equals(jth)){//动态化
			int upd1 = _e.upd("update tjpcms_cfg set val='动态化',gx= '"+CL.fmt.format(new Date())+"' where py='LMJTH' ");
			String path = request.getContextPath(); 
			_e.upd("update tjpcms_lanmu set url=concat('"+path+"/',url_d,'.dhtml') where url_d is not null and  url_d!='' and lx!='链接' ");
			if (flush==null || flush[0]) {
				HS.flushResponse(response, upd1==1?"0":"-1");
			}else{
				flush[0] = upd1==1;
			}
		}else{
			if (flush==null || flush[0]) {
				HS.flushResponse(response, "-1");
			}else{
				flush[0] = false;
			}
		}
	}

	//栏目内容静态化
	@RequestMapping(value = "aj_lmnrjth")
	@Transactional
	public void aj_lmnrjth(HttpServletRequest request, HttpServletResponse response, boolean[] flush) throws InterruptedException, ServletException, IOException {
		String gly = HT.getCjgly(request);
		if (StringUtils.isEmpty(gly) || "test".equalsIgnoreCase(gly)){
			HS.flushResponse(response, "抱歉，测试账户不可以改动数据");
			return;
		}

		String jth = request.getParameter("jth");
		if ("1".equals(jth)){//静态化
			List<Map<String, Object>>  lstlmjth = _e.r("select distinct url3, id, nrtbl,name from tjpcms_lanmu where url3 is not null and  url3!='' and  donly3='否' ");
			int i=0;
			for (; i < lstlmjth.size(); i++) {//外围循环URL3
				String strrecid = lstlmjth.get(i).get("id").toString();
				String strurl3 = (String)lstlmjth.get(i).get("url3");
				String strnrtbl = (String)lstlmjth.get(i).get("nrtbl");
				if (StringUtils.isEmpty(strurl3) || StringUtils.isEmpty(strnrtbl)){
					continue;
				}

				boolean hasUrlZd = false, hasUrl_sZd = false, hasUrl_dZd = false, hasDelfZd = false, hasShztZd = false, hasCidZd = false;
				List<Map<String, Object>> tblZiduan = _e.getTblZiduan(strnrtbl, CL.DB);//内容表的所有字段
				for (Map<String, Object> m1: tblZiduan) {
					if ("url".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrlZd = true;
					else if ("url_s".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrl_sZd = true;
					else if ("url_d".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrl_dZd = true;
					else if ("delf".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasDelfZd = true;
					else if ("shzt".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasShztZd = true;
					else if ("cid".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasCidZd = true;
				}
				int cntTbRec = _e.cnt("select count(*) from "+strnrtbl);
				if (cntTbRec<=0)continue;//表里没记录就没法静态化了
				if (!hasUrlZd || !hasUrl_sZd || !hasUrl_dZd || !hasCidZd){
					continue;
				}
				String contextPath = request.getContextPath();
				String tpstruls = contextPath+"/static/neirong/"+strurl3+"/";
				String sqlwh="";
				if (hasDelfZd) sqlwh+= " and delf=0 ";//未删除的
				if (hasShztZd) sqlwh+= " and shzt= '审核通过' ";//审核通过的
				String[] strCidIn={strrecid+","};
				HS.getStrCidIn(strCidIn, strrecid, _e.r("select id,pId from tjpcms_lanmu "));
				sqlwh += " and cid in ("+strCidIn[0].substring(0,strCidIn[0].length()-1)+")";
				int updRecs =0;
				updRecs= _e.upd(" update "+strnrtbl+" set url_s=concat('"+tpstruls+"', id,'.html'), url=url_s where 1=1 "+sqlwh);
				if (updRecs>0){//表里的记录被更新过了，下面生成对应的文件
					ServletContext servletContext = request.getServletContext();
					File mulu = new File(servletContext.getRealPath("")+File.separator+"static"+File.separator+"neirong"+File.separator+strurl3);
					if (!mulu.exists()){
						mulu.mkdirs();
					}
					List<Map<String, Object>> lstlmcf = _e.r("select id,url,url_d from "+strnrtbl+" where 1=1"+sqlwh);
					for (Map<String, Object> map : lstlmcf) {
						final ByteArrayOutputStream os = new ByteArrayOutputStream();  
						final ServletOutputStream stream = new ServletOutputStream() {
							public void write(byte[] data, int offset, int length) {    os.write(data, offset, length);  }  
							public void write(int b) throws IOException {    os.write(b);   }  
						};
						final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));  
						HttpServletResponse rep = new HttpServletResponseWrapper(response) {  
							public ServletOutputStream getOutputStream() {    return stream;   }
							public PrintWriter getWriter() {   return pw;   }
						};
						String recid = map.get("id").toString();
						String url_d = (String)map.get("url_d");
						RequestDispatcher rd = servletContext.getRequestDispatcher("/"+url_d+".dhtml?id="+recid+"&___opjth=1&___url_d="+url_d);
						rd.include(request, rep);
						pw.flush();
						FileOutputStream fos = new FileOutputStream(servletContext.getRealPath("")+File.separator+"static"+File.separator+"neirong"+File.separator+url_d+File.separator+recid+".html");
						os.writeTo(fos);
					}
				}
			}
			if (i>=lstlmjth.size() && _e.upd("update tjpcms_cfg set val='静态化' where py='LMNRJTH' ")==1){
				if (flush==null || flush[0]) {
					HS.flushResponse(response, "0");
				}else{
					flush[0] = true;
				}
			}else{
				if (flush==null || flush[0]) {
					HS.flushResponse(response, "-1");
				}else{
					flush[0] = false;
				}
			}
		}else if ( "0".equals(jth)){//动态化
			List<Map<String, Object>>  lstlmjth = _e.r("select distinct url3, id, nrtbl,name from tjpcms_lanmu where url3 is not null and  url3!='' ");
			int i=0;
			for (; i < lstlmjth.size(); i++) {//外围循环URL3
				String strrecid = lstlmjth.get(i).get("id").toString();
				String strurl3 = (String)lstlmjth.get(i).get("url3");
				String strnrtbl = (String)lstlmjth.get(i).get("nrtbl");
				if (StringUtils.isEmpty(strurl3) || StringUtils.isEmpty(strnrtbl)){
					logger.error("url3或nrtbl为空，"+strurl3+"，"+strnrtbl);
					break;
				}

				boolean hasUrlZd = false, hasUrl_sZd = false, hasUrl_dZd = false, hasDelfZd = false, hasShztZd = false, hasCidZd = false;
				List<Map<String, Object>> tblZiduan = _e.getTblZiduan(strnrtbl, CL.DB);//内容表的所有字段
				for (Map<String, Object> m1: tblZiduan) {
					if ("url".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrlZd = true;
					else if ("url_s".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrl_sZd = true;
					else if ("url_d".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasUrl_dZd = true;
					else if ("delf".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasDelfZd = true;
					else if ("shzt".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasShztZd = true;
					else if ("cid".equalsIgnoreCase((String)m1.get("COLUMN_NAME"))) hasCidZd = true;
				}
				int cntTbRec = _e.cnt("select count(*) from "+strnrtbl);
				if (cntTbRec<=0)continue;
				if (!hasUrlZd || !hasUrl_sZd || !hasUrl_dZd || !hasCidZd){
					logger.error(strnrtbl+"表必须包含3个url字段和cid字段");
					break;
				}
				String contextPath = request.getContextPath();
				String sqlwh="";
				if (hasDelfZd) sqlwh+= " and delf=0 ";//未删除的
				if (hasShztZd) sqlwh+= " and shzt= '审核通过' ";//审核通过的
				String[] strCidIn={strrecid+","};
				HS.getStrCidIn(strCidIn, strrecid, _e.r("select id,pId from tjpcms_lanmu "));
				sqlwh += " and cid in ("+strCidIn[0].substring(0,strCidIn[0].length()-1)+")";
				_e.upd(" update "+strnrtbl+" set url=concat('"+contextPath+"/"+"', url_d,'.dhtml?id=',id) where 1=1"+sqlwh);
			}
			if (i>=lstlmjth.size() && _e.upd("update tjpcms_cfg set val='动态化' where py='LMNRJTH' ")==1){
				if (flush==null || flush[0]) {
					HS.flushResponse(response, "0");
				}else{
					flush[0] = true;
				}
			}else{
				if (flush==null || flush[0]) {
					HS.flushResponse(response, "-1");
				}else{
					flush[0] = false;
				}
			}
		}else{
			if (flush==null || flush[0]) {
				HS.flushResponse(response, "-1");
			}else{
				flush[0] = false;
			}
		}
	}
	
	//获取栏目静态化的配置值
	@RequestMapping(value = "getlmjthval")
	public void getlmjthval(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ServletException, IOException {

		Map<String, Object> obj = _e.obj("select val from tjpcms_cfg where PY='LMJTH'");
		HS.flushResponse(response, obj.get("val"));
	}
	
	//类似于栏目列表中的fulanmu_zi
	//为什么此处，需要显式的先出Qx的过滤语句呢，因为首先fucaidan_zi是系统的一个功能，我当然要考虑权限，其次fucaidan_zi与其他菜单或栏目不同的是，它需要查出直接子菜单
	//但是直接子菜单当然是有权限的了，所以必须要查一下。只有三种类型是会牵扯到直接子节点的：fulanmu_zi,fucaidan_zi,fulanmu_nr
	@RequestMapping(value = "fucaidan_zi")
	public String fucaidan_zi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ___mid = request.getParameter("___mid");
		if(StringUtils.isEmpty(___mid)){return "adm/welcome";}

		Crud o = new Crud(_e,request, "tjpcms_caidan", "");
		o.getR().setExwhere("pId="+___mid+" and id in "+HS.arr2instr(QX.getValidCdids(_e, request))).setOdrby("px asc, gx desc");
		o.getAev().setNeeda(false).setNeede(0);
		o.getD().setNeed(0);

		//查询的配置
		String cxs[][]= {{"菜单id","id"}, {"父菜单id","pId"}, {"菜单名称","name"},{"链接/表和字段","lj"},{"是否动态","dt","zdb:shifou"},{"更新时间","gx"}};
		String hds[][]= {
			{"菜单id","id"},{"父菜单id","pId"},{"菜单名称","name"}, {"链接/表和字段","lj"},{"是否动态","dt","zdb:shifou"},{"排序","px"},{"更新时间","gx"}//text:zdb:xxx也可以简写为zdb:xxx
		};
		HS.setList(request, o,hds, cxs);
		
		return "adm/list";
	}
//===================================================================================================================================
//	tjpcms的后台内置功能相关结束
//===================================================================================================================================
	

	
	
	
	//捐赠列表
	@RequestMapping(value = "juanzeng")
	public String juanzeng(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_jz", "捐赠信息");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		o.getD().setBatdel(false);

		//查询的配置
		String cxs[][]= {{"捐赠人","jzr"}, {"金额", "je", "op:>,<,>=,<=,=,!="}, {"备注","bz"}, {"来源","ly","zdb:juanzenglaiyuan"},{"捐赠日期","jzrq", "lx:sj"},{"入库日期","rq"}};
		String hds[][]= {{"捐赠人","jzr"},{"金额","je"},{"捐赠日期","jzrq"},{"来源","ly"},{"备注","bz"},{"入库日期","rq"}};
		HS.setList(request, o,hds, cxs);

		//aev的配置
		String aev[][]= {
				{"捐赠人","jzr", "required"},
				{"金额","je", "required plusdouble:金额请输入正数"},
				{"捐赠日期","jzrq",Aevzd.DATE.toString(),"required"},
				{"来源","ly",Aevzd.SELECT.toString(),"juanzenglaiyuan",  "required"},
				{"捐赠备注","bz"}
		};
		HS.setAev(request, o,aev);

		return"adm/list";
	}
	
	//网站互动信息
	@RequestMapping(value = "wzhdxx")
	public String wzhdxx(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_hd", "网站互动信息");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		o.getAev().setNoaev();

		//查询的配置
		String cxs[][]= {{"内容","nr"}, {"日期","rq","def:eq:"+CL.YMD.format(new Date())}, {"ip地址","ip"}};
		String hds[][]= {{"内容","nr"}, {"ip地址","ip"}, {"日期","","ext:rq::::19"}};
		HS.setList(request, o,hds, cxs);

		return"adm/list";
	}

	//未读消息
	//这里有个什么问题，就是如果hds中有rq这个字段，在list.jsp里会自动截取前10位（即精确到日期），但是这里我需要精确到时间
	//ext:rq:::tail:19，这个配置是有点坑了，但也没办法。具体的解释是：需要追加rq这个字段，左边和右边不需要括号这种，即类似于ext:rq:(:)
	//然后ext:rq:::tail:19中的tail是配置追加的文字是放在正式内容前还是后，默认是后，所以这里tail可以不填
	//最后的19是截取rq这个字段的长度
	//好吧，其实没这么细的需求，正常这么用就行了  ext:rq
	//如果mysql的timestap类型直接显示到页面，时分秒的最后会多一位
	@RequestMapping(value = "wdxx")
	public String wdxx(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_hd", "未读消息");
		o.getAev().setNoaev();
		o.getR().setExwhere("yd=0");

		//查询的配置
		String cxs[][]= {{"类型","lx", "sel:捐赠反馈,首页点赞,首页下载"}, {"内容","nr"}, {"日期","rq"," def:ks:eq:"+CL.YMD.format(new Date())}, {"ip地址","ip"}};//sel或select都可以
		String hds[][]= {{"类型","lx"}, {"内容","nr"}, {"ip地址","ip"}, {"日期","","ext:rq:::tail:19","style:width:12em"}};//不写字段名，以ext来截取内容。rq后左右不加前后缀，最后的19代表截取rq字段
		String ans[][]= {
			{"本页已读", "js:fn_op_yidu(0):listanq.jsp", "clazz:cls_yidu"},
			{"全部已读", "js:fn_op_yidu(1)", "clazz:cls_yidu"}};//再来个全部已读，我觉得这很有必要，几千个未读了都
		HS.setList(request, o,hds, cxs, null, ans);

		return"adm/list";
	}

	//大事记
	@RequestMapping(value = "dashiji")
	public String dashiji(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Crud o = new Crud(_e,request, "t_dashi", "大事记");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		//查询的配置
		String cxs[][]= {{"标题","title"},{"日期","gx"}, {"审核状态","shzt","zdb:wzshzt","def:eq:待审核"}};//除了时间，其他字段默认的操作符是like,可以多个操作符
		String hds[][]= {{"标题","title"},{"审核状态（审核意见）","shzt","ext:shyj:（:）"}, {"作者uid", "uid"},
				{"作者昵称","zznc","sql:select nc zznc from tjpcms_usr  where id=t.uid"},{"年","nian"},{"里程碑","lcb"},{"点击次数","cs"},{"排序","px"},{"更新时间","gx"}};
		String czs[][]={{"text:审核","js:fn_cmn_sh(this,id,shzt):listczq.js","idx:1"},
				{"text:gray:删除:case:shzt!=审核不通过#case:shzt=审核不通过:val:删除","js:fn_cmn_del(this,id,审核状态为【审核不通过】时，方可删除！):listczq.js?test=1"}};
		HS.setList(request, o,hds, cxs, czs);
		
		//aev的配置
		String aev[][]= {
				{"标题","title","required"},
				{"内容","nr",Aevzd.RICH.toString(),"required"},
				{"年","nian","required"},
				{"里程碑","lcb","required"},
				{"点击次数","cs",Aevzd.TEXT.toString()},
				{"排序","px"}
		};
		HS.setAev(request, o,aev);

		return "adm/list";
	}

	//开发笔记
	@RequestMapping(value = "kaifabiji")
	public String kaifabiji(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Crud o = new Crud(_e,request, "t_kfbj", "开发笔记");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		//查询的配置
		String cxs[][]= {{"标题","title"},{"地点","dd"},{"日期","gx"}, {"审核状态","shzt","zdb:wzshzt","def:eq:待审核"}};
		String hds[][]= {{"内容","nr"},{"审核状态（审核意见）","shzt","ext:shyj:（:）"},{"作者uid", "uid"},
				{"作者昵称","zznc","sql:select nc zznc from tjpcms_usr  where id=t.uid"},{"地点","dd"},{"头像","tu","pic"},{"排序","px"},{"更新时间","gx"}};
		String czs[][]={{"text:审核","js:fn_cmn_sh(this,id,shzt):listczq.js","idx:1"},
				{"text:gray:删除:case:shzt!=审核不通过#case:shzt=审核不通过:val:删除","js:fn_cmn_del(this,id,审核状态为【审核不通过】时，方可删除！):listczq.js"}};
		HS.setList(request, o,hds, cxs, czs);

		//aev的配置
		String aev[][]= {
				{"标题","title"},
				{"内容","nr","required",Aevzd.RICH.toString()},
				{"地点","dd","required","\\江苏南京"},
				{"头像","tu","required",Aevzd.PIC.toString(),"\\"+request.getContextPath()+"/upload/pic/2017/02/"+"20170122160120bb7396dc58618.png"},
				{"排序","px"}
		};
		HS.setAev(request, o,aev);

		return "adm/list";
	}


//=====================================================================================
//	自定义ajax开始
//=====================================================================================
	//如果是超级管理员的test账号，退出时退到登录后台的测试链接，否则退出到正式链接
	@RequestMapping(value = "tuichu")
	public void tuichu(HttpServletRequest request, HttpServletResponse response) {
		String lianjie = XT.dlhtlj;
		if (HT.isTestCjgly(request)){
			lianjie = XT.dlhtlj_test;
		}
		HT.clearLogin(request);
		HS.flushResponse(response, lianjie);
	}

	//本页已读，全部已读
	@RequestMapping(value = "aj_wdxx_yd")
	public void aj_wdxx_yd(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ServletException, IOException {

		if (HT.isTestCjgly(request) | HT.isUsrpt(request)){
			HS.flushResponse(response, "抱歉，测试账户不可以改动数据");
			return;
		}

		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		if (o==null || CollectionUtils.isEmpty(o.getR().getTbs())){
			HS.flushResponse(response, "-1");
			return;
		}
		
		String quanbu = request.getParameter("quanbu");
		if(!"0".equals(quanbu) && !"1".equals(quanbu)){
			HS.flushResponse(response, "-1");
			return;
		}

		if ("0".equals(quanbu)){//本页
	 		String[] arr = new String[o.getR().getTbs().size()];
	 		for (int i=0;i<arr.length;i++) {
	 			arr[i] = o.getR().getTbs().get(i).get("id").toString();
			}
	 		int upd = _e.upd("update t_hd set yd=1 where id in "+HS.arr2instr(arr));
	 		if (upd!=arr.length){
	 			HS.flushResponse(response, "-1");
	 			return;
	 		}
		}else{//全部
			 _e.upd("update t_hd set yd=1 where 1=1");
		}

		request.getSession().setAttribute("ses_ht_tip", "操作成功！");
		HS.flushResponse(response, "0");
	}

	//系统的审核功能
	//4.0版本的时候，审核没有流程一说，只能是超管审核，也没有未提交这个状态（因为没有暂存），所以审核比较简单，这里也没做校验
	//现在5.0就要稍微复杂点了，有很多审核流程相关的校验，也引入了审核阶段shjd
	@RequestMapping(value = "aj_cmn_sh")
	public void aj_cmn_sh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String u = request.getParameter("u");
		String shzt = request.getParameter("shzt");
		String shyj = request.getParameter("shyj");

		//校验参数和前端的逻辑
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		if (o==null || CollectionUtils.isEmpty(o.getR().getTbs())){
			HS.flushResponse(response, "-1");
			return;
		}

		if (StringUtils.isEmpty(id) && StringUtils.isEmpty(u)) {
			HS.flushResponse(response, "审核失败！参数异常！");
			return;
		}
		
		if (StringUtils.isNotEmpty(shyj) && shyj.length()>100){
			HS.flushResponse(response, "审核失败！审核意见需在100字以内！");
			return;
		}
		
		if (("退回修改".equals(shzt) || "审核不通过".equals(shzt)) && StringUtils.isEmpty(shyj)){
			HS.flushResponse(response, "审核失败！设置【"+shzt+"】时，审核意见必填！");
			return;
		}
		
		if (!"退回修改".equals(shzt) && !"审核不通过".equals(shzt) && !"审核通过".equals(shzt)){
			HS.flushResponse(response, "审核失败！shzt参数异常！");
			return;
		}

		//校验后台业务逻辑
		//1、是否待我审核，是的前台才能看到审核按钮。sfdwsh=1，需要：待审核，shjd到自己的角色了，记录所在栏目的审核流程不能是空，也不能是自定义
		Map<String, Object> rec = HS.obj(_e, o.getTb(), id);
		if (rec==null){
			HS.flushResponse(response, "审核失败！该记录无效！");
			return;
		}
		if (rec.get("shzt")==null || !"待审核".equals(rec.get("shzt").toString())){
			HS.flushResponse(response, "审核失败！该记录审核状态并非待审核！");
			return;
		}
		if (rec.get("cid")==null || StringUtils.isEmpty(rec.get("cid").toString())){
			HS.flushResponse(response, "审核失败！该记录cid字段无效！");
			return;
		}
		if (rec.get("shjd")==null || StringUtils.isEmpty(rec.get("shjd").toString()) || !Pattern.compile("^[1-9]\\d*").matcher(rec.get("shjd").toString()).matches()){//正整数，从1开始
			HS.flushResponse(response, "审核失败！该记录shjd字段无效！");
			return;
		}
		
		String cid = rec.get("cid").toString();
		List<Map<String, Object>> r = _e.r("select s.* from tjpcms_lanmu t left join tjpcms_liucheng s on s.id=t.lcid where t.id="+cid);
		if (CollectionUtils.isEmpty(r) || r.size()!=1 || r.get(0)==null) {
			HS.flushResponse(response, "审核失败！该记录所在栏目无效！");
			return;
		}
		Map<String, Object> map = r.get(0);
		Object jsids = map.get("jsids");
		if ((jsids==null || StringUtils.isEmpty(jsids.toString())) && map.get("mc")!=null && !"自定义".equals(map.get("mc"))){
			HS.flushResponse(response, "审核失败！该记录所属流程角色序列为空！");
			return;
		}
		
		//校验shjd
		if (map.get("mc")!=null && "自定义".equals(map.get("mc"))){
			//自行在钩子中处理
			String ret="-1";
			if (StringUtils.isNotEmpty(o.getAev().getHkzdy_shenhe())){
				ret = (String)HS.execHook(o.getAev().getHkzdy_shenhe(), request, o, null, rec);
			}
			HS.flushResponse(response, ret);
		}else{
			String[] split = jsids.toString().split("→");
			Integer shjd = Integer.valueOf(rec.get("shjd").toString());
			if (!(shjd>=1 && shjd<=split.length)){
				HS.flushResponse(response, "审核失败！该记录shjd值异常！");
				return;
			}
			if (!HT.getJsid(request).equals(split[shjd-1])){
				HS.flushResponse(response, "审核失败！该记录当前并不需要角色"+HT.getJsid(request)+"审核！");
				return;
			}

			Map<String, String[]> e = new HashMap<String, String[]>(request.getParameterMap());
			if ("退回修改".equals(shzt) || "审核不通过".equals(shzt)){
				e.put("shjd", new String[]{"0"});//其实不设置也行就是了
			}else if (shjd < split.length){//继续流转到下一个阶段审核
				e.put("shzt", new String[]{"待审核"});//其实不设置也行就是了
				e.put("shjd", new String[]{String.valueOf(shjd+1)});
			}
			HS.flushResponse(response, HS.ppbc(_e,o.getTb(), request,e));
		}
		
	}
	
	//获取当前角色拥有的后台菜单
	//这里我发现一个问题，我是可以根据动态3级节点的表名把动态节点的数据都查出来塞到对应的2级菜单下面去，但是如果该动态节点的某列数据也是需要join查询的，这个就坑了呀
	//我想，目前只能说在代码一级做if判断，强行查出来了，这个还真有点麻烦，除非就是动态节点的数据是从动态sql里查出来的，而不是像现在在菜单管理里只给表名，但是这样还是到了代码一级
	//这个。。还真不是那么完善的，只能说希望动态表这种情况比较少，动态表里字段还需要join的情况更少了。如果有，只能在这里if判断，强行查出来，当然了，其实这里也牵扯到index_ad中点击菜单节点的逻辑
	//2018-06-04：突然想起来表单提交sql不太好，那我可以用视图啊，那不就想怎么弄怎么弄了吗，所以我后来又测试了视图的写法v_lanmu，没问题，所以这里强行查lanmu可以作古了
	@RequestMapping(value = "aj_get_htcdtree")
	public void aj_get_htcdtree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime=System.currentTimeMillis();//记录开始时间  
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("ret", 0);
		
		//把当前角色所拥有的菜单先全部查出来
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		String s = "select t.cdid id,m.name,m.lj, t.px ___px, "
				+" case when pId is not null then cast(m.pId as char) else mid(substring_index(cdid,'-',2),2) end pId "
				+" from tjpcms_qx_caidan t left join tjpcms_caidan m on m.id=t.cdid ";
		s += " where t.jsid= '"+HT.getJsid(request)+"' order by t.px";
		r = _e.r(s);

		//筛选出一级和二级菜单，并把每个二级菜单的子树查出来，并且如果是动态3级子树还要把动态子树的所有属性查出来塞进去，这样前台才能正确点击
		Map<String, Map<String, Object>> allNodes = new HashMap<String, Map<String, Object> >();//后面在塞动态节点时要用到，就是根据节点ID直接找到该节点的属性，不用线性查找了
		List<Map<String, Object>> yjcd = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < r.size(); i++) {
			String rid = r.get(i).get("id").toString();
			allNodes.put(rid, r.get(i));
			if (rid.startsWith("-")){
				String[] split = rid.split("-");
				allNodes.put(split[2], r.get(i));
			}
			if (CL.TREE_ROOT_ID.toString().equals(r.get(i).get("pId").toString())){
				r.get(i).put("___used", "1");//最后再次遍历时，知道这个已经用过了
				yjcd.add(r.get(i));
			}
		}
		m.put("yjcd", yjcd);//当前角色的一级菜单

		List<List<Map<String, Object>>> ejcd = new ArrayList<List<Map<String, Object>>>();
		for (int i = 0; i < yjcd.size(); i++) {
			String yjcdid = yjcd.get(i).get("id").toString();
			List<Map<String, Object>> tpp = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < r.size(); j++) {
				if (yjcdid.equals(r.get(j).get("pId").toString())){
					r.get(j).put("___used", "2");
					tpp.add(r.get(j));
				}
			}
			ejcd.add(tpp);
		}
		m.put("ejcd", ejcd);//当前角色的所有二级菜单，列表的每一项对应一个一级菜单
		
		Map<String, Object> mpdt3 = new HashMap<String, Object>();//存动态3级菜单节点id及其子节点序列
		for (int i = 0; i < r.size(); i++) {
			String ndid = r.get(i).get("id").toString();
			if (r.get(i).get("___used")!=null || CL.TREE_ROOT_ID.toString().equals(ndid)){
				continue;
			}

			//待处理的节点还有两类，一类是静态节点，还有一类是动态的，对于动态的需要去对应的表里把全部数据列查出来
			if (ndid.startsWith("-")){
				String pid = r.get(i).get("pId").toString();
				final String[] split = ndid.split("-");
				if (mpdt3.get(split[2])!=null){
					((List<String>)mpdt3.get(split[2])).add(split[3]);
				}else{
					mpdt3.put(split[2], new ArrayList<String>(){{add(split[3]);}});
				}
			}else{//是静态的，要找到该节点是在哪个二级菜单下面的
				htcdtree_insjtej(r.get(i), r, yjcd, ejcd, allNodes);
			}
		}
		if (mpdt3.size()>0) {
			
			List<Map<String, Object>> r2 = _e.r("select id, lj from tjpcms_caidan where lj is not null and dt=1 and id in"+HS.map2instr((Map<String, Object>)mpdt3));
			if (CollectionUtils.isEmpty(r2) || r2.size()!=mpdt3.size()) {
				throw new Exception("我从权限数据片段去反推完整的菜单数据，进入到这里说明很可能数据被污染了，请不要手动操作数据库，有问题与作者联系");
			}else{
				
				for (Map<String, Object> map : r2) {
					String[] split = ((String)map.get("lj")).split("\\s+");
					String bm = split[0];//表名
					String idmc = split[1];
					String pIdmc = split[2];
					String namemc = split[3];
					String ljmc = split[4] ;
					String sqlstr = "select t."+namemc+" name, t."+ljmc+" lj ,t.* ";
					//强行join查出tjpcms_lanmu表的lx列。后来发现可以用视图来join字段，也就免去了在代码里写死的尴尬
					sqlstr +=" ,t.id ___cid ";
/*					if ("tjpcms_lanmu".equals(bm)){
					}*/
					sqlstr += " from "+bm+" t ";
/*					if ("tjpcms_lanmu".equals(bm)){
						sqlstr += " left join tjpcms_zdx s on t.lx=s.zdxmc left join tjpcms_zdb d on s.pId=d.id ";
					}*/
					sqlstr += " where 1=1  ";
/*					if ("tjpcms_lanmu".equals(bm)){
						sqlstr += " and d.py='lanmuleixing' ";
					}*/
					sqlstr+=" and t."+idmc+" in  "+HS.list2instr((List<String>)mpdt3.get(map.get("id").toString()));
					List<Map<String, Object>> dtndlst = _e.r(sqlstr);
					htcdtree_insdtej(dtndlst, ejcd, idmc,pIdmc, map.get("id").toString(), allNodes, yjcd);//将这些动态的节点插入到正确的二级菜单下面
				}
			}
		}
		
		//最后还要排序一下，不然出来的菜单次序不对的，次序是根据你在list中的先后次序来的
		for(int i=0; i<ejcd.size(); i++){
			List<Map<String, Object>> list = ejcd.get(i);
			for (Map<String, Object> map : list) {
				if (map.get("sub") !=null){
					List<Map<String, Object>> st = (List<Map<String, Object>>)map.get("sub");
					Collections.sort(st, new Comparator<Map<String, Object>>() {
			            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			                Integer name1 = Integer.valueOf(o1.get("___px").toString()) ;//name1是从你list里面拿出来的一个 
			                Integer name2 = Integer.valueOf(o2.get("___px").toString()) ; //name1是从你list里面拿出来的第二个name
			                return name1.compareTo(name2);
			            }
			        });
				}
			}
		}

		HS.flushResponse(response, JSONObject.fromObject(m));
		
		long endTime=System.currentTimeMillis();//记录结束时间 
		System.out.println("endTime-startTime="+(endTime-startTime));
	}
	
	private void htcdtree_insdtej(List<Map<String, Object>> dtndlst, List<List<Map<String, Object>>> ejcd, String idmc,  String pIdmc, String nd3id, Map<String, Map<String, Object>> allNodes, List<Map<String, Object>> yjcd){
		String nd3pId = String.valueOf(allNodes.get(nd3id).get("pId"));
		for (Map<String, Object> e : dtndlst) {
			String dtndid = String.valueOf(e.get(idmc));
			e.put("id", "-"+nd3pId+"-"+nd3id+"-"+dtndid);
			e.put("pId", CL.TREE_ROOT_ID.toString().equals(dtndid)?nd3pId:"-"+nd3pId+"-"+nd3id+"-"+e.get(pIdmc));
			e.put("url", "");//ztree默认有这个字段就会打开新链接页
			e.put("___px", allNodes.get(e.get("id")).get("___px"));//根据这个字段来排序，保证与权限管理及菜单管理中的次序一致
			//再将合并后的节点插入到指定的二级菜单下
			String yjcdid = allNodes.get(nd3pId).get("pId").toString();
			int yjidx=0;
			for (; yjidx < yjcd.size(); yjidx++) {
				if (yjcdid.equals(yjcd.get(yjidx).get("id").toString())){
					break;
				}
			}
			if (yjidx>= yjcd.size()){
				logger.error("未找到父1级节点？这是不存在地");
				break;
			}else{
				List<Map<String, Object>> list = ejcd.get(yjidx);//一级菜单下的2级菜单列表
				for (Map<String, Object> map : list) {
					if (nd3pId.equals(map.get("id").toString())){//找到了这个二级节点，将填充后的完整节点数据插入
						Object sub = map.get("sub");
						if (sub!=null){
							List<Map<String, Object>> st = (List<Map<String, Object>>)sub;
							st.add(e);
						}else{
							List<Map<String, Object>> st = new ArrayList<Map<String, Object>>();
							st.add(e);
							map.put("sub", st);
						}
						break;
					}
				}
			}
		}
	}

	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String strHex = Integer.toHexString(bytes[i]);
			if (strHex.length() > 3) {
				sb.append(strHex.substring(6));
			} else {
				if (strHex.length() < 2) {
					sb.append("0" + strHex);
				} else {
					sb.append(strHex);
				}
			}
		}
		return sb.toString();
	}
	
	//对于给定的节点，其必然是3级或者更低级别的，且是静态的节点，要将其插入到ejcd的合适位置
	//其实有更高效的办法，用map来存储r，但是无所谓了，数据量小，现在服务器计算能力强，这种算法差距的区别其实被埋没了
	private void htcdtree_insjtej(Map<String, Object> node, List<Map<String, Object>> r, List<Map<String, Object>> yjcd, List<List<Map<String, Object>>> ejcd,Map<String, Map<String, Object>> allNodes) {
		final String id = node.get("id").toString();
		final String pId = node.get("pId").toString();
		String pid1 = pId;
		String pid2 = id;
		boolean found = false;//找到1级祖先节点了
		while(true){
			int yj = 0;
			for (; yj < yjcd.size(); yj++) {
				if (yjcd.get(yj).get("id").toString().equals(pid1)){//当前的pid已经到一级的了
					found = true;
					break;
				}
			}
			if (found){
/*				try {    
			        KeyGenerator kg = KeyGenerator.getInstance("AES");    
			        kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
			        SecretKey sk = kg.generateKey(); 
			        byte[] b = sk.getEncoded();
			        String s = byteToHexString(b);
			    } catch (NoSuchAlgorithmException e) {    
			        e.printStackTrace();
			    }*/
				
				for (Map<String, Object> map : ejcd.get(yj)) {
					if (pid2.equals(map.get("id").toString())){//找到2级祖先节点了
						Object sub = map.get("sub");
						if (sub!=null){
							List<Map<String, Object>> st = (List<Map<String, Object>>)sub;
							st.add(node);//这个就是用ztree的simpleData的好处，不需要自己组织树结构，只要有id和pId就行，以列表形式给ztree，ztree来组织出树结构
						}else{
							List<Map<String, Object>> st = new ArrayList<Map<String, Object>>();
							st.add(node);
							map.put("sub", st);
						}
					}
				}
				break;
			}else{
				Map<String, Object> map = allNodes.get(pid1);//找到该节点
				if (map==null) {
					logger.error("理论上不会进入这里的，除非数据库的数据被手动修改过了");
					break;
				}
				pid2 = pid1;
				pid1 = map.get("pId").toString();
			}
		}
	}
//=====================================================================================
//	自定义ajax结束
//=====================================================================================
	
	
	//修改头像
	@RequestMapping(value = "zcyh_xgtx")
	public String zcyh_xgtx(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e, request, "tjpcms_usr","修改头像");
		o.getAev().setTip("注：管理员和第三方登录的用户无法修改头像！");
		o.getAev().setNoBtns(HT.isCjgly(request) || HT.isDsfUsrpt(request));
		o.getAev().setHook_befadgx("com.tjpcms.common.Hook.befCmnUsrTxnc");
		o.getAev().setHook_aftadgx("com.tjpcms.common.Hook.aftCmnUsrTxnc");

		//aev的配置
		String aev[][]= {
			{"头像","tx",Aevzd.PIC.toString(),"required"},
			{"id",Aevzd.HIDDEN.toString(),"随便写一个，因为befCmnUsrTxnc里会重新塞一下，但是不能不写，因为不写默认就是新增"}
		};
		HS.setAev(request, o,aev);
		request.setAttribute("obj",  HS.objw(_e, o.getTb(), "id='"+HT.getUid(request)+"'"));
		
		return "adm/aev";
	}

	//修改昵称
	//adddedit里是根据是否有id来判断是新增还是更新，并且这个判断是在执行钩子函数之前的，所以，这里是个什么问题呢
	//如果不配id，那就会被认为是新增
	@RequestMapping(value = "zcyh_xgnc")
	public String zcyh_xgnc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e, request, "tjpcms_usr","修改昵称");
		o.getAev().setTip("注：后台管理员以及第三方登录的用户无法修改昵称！");
		String uid = HT.getUid(request);
		boolean cantModi = HT.isCjgly(request) || HT.isDsfUsrpt(request);
		o.getAev().setNoBtns(cantModi);//第三方登录的用户不可以编辑昵称
		o.getAev().setHook_befadgx("com.tjpcms.common.Hook.befCmnUsrTxnc");
		o.getAev().setHook_aftadgx("com.tjpcms.common.Hook.aftCmnUsrTxnc");
		o.getAev().setJs_aft_bc("parent.sxnc(nc, 	 tx)");//刷新昵称，如果你后台里显示的是用户的昵称，可以自行刷新一下，这里我显示的是id了，所以其实也不需要刷新了，这是为了保证如果nc是存session的，有接口可以刷新

		//aev的配置
		String aev[][]= {
			{"昵称","nc",!cantModi?Aevzd.INPUT.toString():Aevzd.TEXT.toString(),"required length#<=15:长度必须小于15  length#>=2:长度必须大于2"},
			{"id",Aevzd.HIDDEN.toString(),"随便写一个，因为befCmnUsrTxnc里会重新塞一下"}
		};
		HS.setAev(request, o,aev);
		request.setAttribute("obj",  HS.objw(_e, o.getTb(), "id='"+uid+"'"));
		
		return "adm/aev";
	}

	//修改密码
	@RequestMapping(value = "zcyh_xgmm")
	public String zcyh_xgmm(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "tjpcms_usr","修改密码");
		o.getAev().setZdybc("aj_zcyh_xgmm");//得返回JSON格式
		o.getAev().setTip("注：后台管理员以及第三方登录的用户无法修改密码！");
		o.getAev().setNoBtns(HT.isCjgly(request) || HT.isDsfUsrpt(request));
		o.getAev().setHook_befadgx("com.tjpcms.common.Hook.befCmnUsrTxnc");
	
		//aev的配置
		String aev[][]= {
				{"旧密码","old",Aevzd.PASSWORD.toString(),"required"},
				{"新密码(6-12位)","new1",Aevzd.PASSWORD.toString(),"required regexp#^[a-zA-Z0-9]{6,12}$:请输入6-12位的密码  not_have_kg:密码不能含有空格"},
				{"确认新密码","new2",Aevzd.PASSWORD.toString(),"required repeat#new1:两次输入的密码不一致 "}
		};
		HS.setAev(request, o,aev);
		
		return "adm/aev";
	}

	//自定义了aev的保存
	@RequestMapping(value = "aj_zcyh_xgmm")
	public void aj_zcyh_xgmm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Usrpt so = HT.getUsrpt(request);
		String old = request.getParameter("old");
		String new1 = request.getParameter("new1");
		String new2 = request.getParameter("new2");
		Map<String, Object> qtjson = new HashMap<String, Object>();
		qtjson.put("ret", "-1");
		if (so==null || StringUtils.isEmpty(old) || StringUtils.isEmpty(new1) || StringUtils.isEmpty(new2) || !new1.equals(new2)
			|| Pattern.compile("\\s+").matcher(new1).find() || new1.length()<6 || new1.length()>12) {
			HS.flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}
		
		//这里加上对是否第三方用户的判断，如果是，那就不能修改密码，当然其实修改了也无所谓，因为确实也有这么一个密码字段，但是第三方登录的用户根本用不着，所以你就是强行修改了也没啥，我只是没有在这里阻止你而已
		//然后befCmnUsrTxnc是进不去的，因为zcyh_xgmm配的自定义保存，所以没有钩子的，要在这里自行校验
		//后台管理员也不需要修改面，因为后台管理员的密码是写死的，不能修改，或者在nedcjglycontroller里修改也行，这里首先不能修改，也无法修改，因为管理员的账号不存数据库，可以改为加密存文件或者dll这种

		Map<String, Object> obj = HS.objw(_e, "tjpcms_usr", "id='"+so.getId()+"'");
		if (obj!=null){
			String uid = (String)obj.get("id");
			Mademd5 mad=new Mademd5();
			String iptold = mad.toMd5(mad.toMd5(old), uid);
			if (!((String)obj.get("mm")).equals(iptold)) {
				qtjson.put("ret", "旧密码不正确！");
				HS.flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}

			Map<String, String[]> e = new HashMap<String, String[]>();
			e.put("id", new String[]{uid});
			e.put("mm", new String[]{mad.toMd5(mad.toMd5(new1), uid)});
			qtjson.put("ret", HS.ppbc(_e, "tjpcms_usr", request,e));
			HS.flushResponse(response, JSONObject.fromObject(qtjson));
			request.getSession().setAttribute("ses_ht_tip","密码修改成功");//自己写个提示
			return;
		}

		HS.flushResponse(response, JSONObject.fromObject(qtjson));
	}
	
	
	@RequestMapping(value={"/{provinceId}_{levelId}","test333"})
    public void test( @PathVariable String provinceId, HttpServletRequest request) throws IllegalArgumentException, IllegalAccessException {
	       
	       
    }

	//弹幕列表
	@RequestMapping(value = "danmuliebiao")
	public String danmuliebiao(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_danmu", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		o.getAev().setNoaev();
		o.getR().setPerPage(50);
		o.getD().setBatdel();
		//o.getR().setJubu();

		String cxs[][]= {{"文字","wz"},{"ip","ip"}, {"日期","rq"}};
		String hds[][]= {{"文字","wz"},{"ip","ip"}, {"图片","tu", "pic:无"}, {"日期","","ext:rq:::tail:19"}};
		HS.setList(request, o,hds, cxs);//查询的配置

		return "adm/list";
	}
	
	//这个主要就是测试自定义审核流程了，企业申请表
	@RequestMapping(value = "qiyeshenqingbiao")
	public String qiyeshenqingbiao(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_test_sqb", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		String cxs[][]= {{"名称","qymc"}};
		String hds[][]= {{"id","id"}, {"名称","qymc"}, {"介绍","js"}, {"日期","","ext:rq:::tail:19"}};
		HS.setList(request, o,hds, cxs);//查询的配置

		String aev[][]= {
				{"企业名称","qymc","required"},
				{"介绍","js","required", Aevzd.INPUT.toString()},
				{"备注","bz"}
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}
	
	//这个上上一个有什么区别呢，因为这个栏目配置的流程是自定义，所以需要你自己去配置审核相关的查询，字段，按钮等等。而非自定义（非无审核）的那些流程，tjpcms系统会自动为其加上查询区，表头区以及按钮区的功能
	@RequestMapping(value = "sqbzdy")
	public String sqbzdy(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Crud o = new Crud(_e,request, "t_test_sqb", "");
		if(StringUtils.isEmpty(o.getCid())){return "adm/welcome";}
		
		//很显然这块也是需自定义，自己加上去的，当然也是从系统代码里copy的
		o.getR().setJs_aft_sx("fn_cmnsh_aftsx()");
		o.getR().setExtjoinstr("left join tjpcms_lanmu x on x.id = t.cid left join tjpcms_liucheng y on y.id=x.lcid");
		
		String cxs[][]= {{"名称","qymc"}, {"审核状态","shzt","zdb:wzshzt","def:eq:全部"},{"作者uid","uid","op:=,!=,like"}, {"作者昵称","zznc"}};
		//配置下属角色，当然最好得有下属角色才配置
		String list2instr = HS.list2instr(HS.getZisunNodes(HT.getJsid(request), o.getMp().r("select id,pId from tjpcms_juese")));
		String sqlxsjs = "select id,pId,mc name from tjpcms_juese where id in"+list2instr;
		List<Map<String, Object>> r = o.getMp().r(sqlxsjs);
		if (CollectionUtils.isNotEmpty(r)) cxs= (String[][])ArrayUtils.add(cxs, new String[]{"下属角色","xsjs","tree:"+sqlxsjs});
		
		//配置是否待我审核，对于企业申请表这个流程来说，我当然是希望流程的发起是企业，而不能是其他角色，那么就只有企业不需要有待我审核了
		int cnt = _e.cnt("select count(*) from tjpcms_juese where pId="+HT.getJsid(request));
		if (cnt>0){
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"是否待我审核","sfdwsh","zdb:shifou"});
		}
		
		String hds[][]= {{"id","id"},{"名称","qymc"}, {"介绍","js"}, {"日期","","ext:rq:::tail:19"}};
		//继续自定义的配置表头区
		Integer insidx = 2;
		hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"作者uid（昵称）", "uid","ext:zznc"});
		hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"作者昵称","zznc","sql:select nc zznc from tjpcms_usr  where id=t.uid","style:display:none"});
		hds= (String[][])ArrayUtils.add(hds, insidx++, new String[]{"审核状态（意见）","shzt","ext:shyj:（:）"});
		hds= (String[][])ArrayUtils.add(hds, insidx++, new String[]{"审核阶段","jieduan", 
			"sql:case when shzt='待审核' and shjd=2 then '待区审核' when shzt='待审核' and shjd=3 then '待市审核' when shzt='待审核' and shjd=4 then '待省审核' when shzt='审核通过' and shjd=4 then '完结' else '待提交' end  "});
		hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"下属角色","xsjs","sql:select id from tjpcms_juese where id=(select jsid from tjpcms_usr where id=t.uid )", "style:display:none"});
		hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"是否待我审核","sfdwsh", "sql:case when shzt='待审核' and t.shjd="+calcLevelByJsid(HT.getJsid(request))+" then 1 else 0 end", "style:display:none"});
		
		String czs[][]={{"text:case:sfdwsh=0:hidden#case:sfdwsh=1:val:审核","js:fn_cmn_sh(this,id,shzt):listczq.js","idx:1"}};
		
		HS.setList(request, o,hds, cxs, czs);//查询的配置
		
		//aev设置
		o.getAev().setHkzdy_shenhe("zdysqb_shenhe").setHook_befadgx("zdysqb_befadgx");
		String aev[][]= {
				{"企业名称","qymc","required"},
				{"介绍","js","required", Aevzd.INPUT.toString()},
				{"备注","bz"}
		};
		HS.setAev(request, o,aev);
		
		return "adm/list";
	}
	
	//根据角色id来计算节点所在的层次
	private int calcLevelByJsid(String jsid){
		
		int cc=1;
		while (true){
			List<Map<String, Object>> r = _e.r("select * from tjpcms_juese where pId="+jsid);
			if (CollectionUtils.isNotEmpty(r)) {
				cc++;
				jsid = r.get(0).get("id").toString();
			}else{
				break;
			}
		}
		
		return cc;
	}
}













