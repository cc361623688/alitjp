/**
 * tjpcms - 最懂你的cms
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 */

package com.tjpcms.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tjpcms.cfg.XT;
import com.tjpcms.common.Crud.Crud;
import com.tjpcms.common.Crud.Retrieve;
import com.tjpcms.common.Crud.Tree;
import com.tjpcms.spring.mapper.EntMapper;


//函数
public class HS {

	
	//================================================================================================================================
	//	系统crud功能相关开始
	//================================================================================================================================
	//这个是渲染aev页面之前，先将数据查出来
	//此处不再使用隐藏域来存id，而是专门弄了个字段rec_id存储
	public static ModelAndView aev(HttpServletRequest request, EntMapper entMapper) throws Exception{
		String lx = request.getParameter("t");
		String id = request.getParameter("id");
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		if ((!"0".equals(lx) && !"1".equals(lx) && !"2".equals(lx)) || o==null || (!"0".equals(lx) && StringUtils.isEmpty(id))){
			return new ModelAndView("adm/welcome", null);
		}

		//如果aev已经配置过不需要呢，还进来不是有问题吗，果断拦截
		if (!o.getAev().isNeeda() && "0".equals(lx) || !o.getAev().isNeede() && "1".equals(lx) || !o.getAev().isNeedv() && "2".equals(lx)){
			throw new Exception("异常错误！配置了无需aev页面，却视图打开aev页面！");
		}
		
		String[] arr = {"新增","编辑","查看"};
		String bread = o.getBread();
		int idxeq = bread.indexOf("aevH=\'");
		int idxblk = bread.indexOf("\'", idxeq+6);
		int idxpz = bread.lastIndexOf("-");
		if (idxeq!=-1 && idxblk!=-1){
			bread = bread.substring(idxeq+6, idxblk);
		}else if (idxpz!=-1){
			bread = bread.substring(idxpz+1, bread.length());
		}else{
			bread = bread.substring(bread.indexOf("：")+1, bread.length());
		}

		o.getAev().setTitle(arr[Integer.valueOf(lx)]+bread);
		request.setAttribute("lx", lx);
		Map<String, Object> curele = null;
		if (!"0".equals(lx)){//编辑或者查看
			for(Map<String, Object>e:o.getR().getTbs()){
				if (e.get("id").toString().equals(id)){//在表体里找到这条数据，但数据有可能不全，如申请入会的情况
					curele = e;
					break;
				}
			}
			
			if (curele==null){
				throw new Exception("异常错误！欲编辑或查看的数据不在列表中！");
			}
		}

		//此处先执行aev包含的自定义的函数，即给用户一个可以在查出数据后修改此数据的机会
		execHook(o.getAev().getDynaev(), request, o, null, curele);
		
		
		//对于正式超管，专门为其增加直接设置审核状态的设置项
		//为什么在编辑里专门添加呢，因为这属于特殊权限，别的角色是不可以的，也需要与列表页的审核按钮相区别
		if (HT.isZsCjgly(request) 
			&& "1".equals(lx) //是编辑
			&& !"未提交".equals(curele.get("shzt").toString())//不是未提交的
			&& !HT.getUid(request).equals(curele.get("uid").toString())//不是自己的
			&& curele.get("cid")!=null) {//是来自栏目的请求
			
			//这里不能根据o.getR().getHook_aftcx()来判断栏目有有效的审核流程，因为查询的时候可能有多个栏目，其中有一个有审核流程即满足，但编辑时只可能是一个栏目，必须以该栏目来重新查询
			String cid = curele.get("cid").toString();
			List<Map<String, Object>> r = o.getMp().r("select * from tjpcms_lanmu t left join tjpcms_liucheng s on s.id=t.lcid where t.id="+cid);
			if (CollectionUtils.isNotEmpty(r) && r.size()==1 && r.get(0)!=null) {
				Object mc = r.get(0).get("mc");
				if (mc!=null  && !"无审核".equals(mc.toString()) && !"自定义".equals(mc.toString()) && r.get(0).get("jsids")!=null){//未配置审核流程或者审核流程为【无审核】
					Map<String, Object> zdshzt = new HashMap<String, Object>();
					zdshzt.put("title", "审核状态");
					zdshzt.put("zdm", "shzt");
					zdshzt.put("type", Aevzd.SELECT.toString());
					zdshzt.put("selops", fnsel("sel", o.getMp(), "审核通过,审核不通过,退回修改", ""));
					zdshzt.put("event", "onchange='___fn_zszg_selcg()'");
					o.getAev().getZds().add(zdshzt);
					Map<String, Object> zdshyj = new HashMap<String, Object>();
					zdshyj.put("title", "审核意见");
					zdshyj.put("zdm", "shyj");
					zdshyj.put("type", Aevzd.INPUT.toString());
					o.getAev().getZds().add(zdshyj);
					//如果是退回修改或者是审核不通过，需要给出理由，审核意见必填，这里配前台的，后台的校验在addedit里
					o.getAev().getExtjs().add("extaevjs.js?s=___zscgshzt");
					o.getAev().setJs_bef_zdyjy("___cmn_zscg_extsh()");
					pushUniuqeExtjs(o, false);//防止和之前的配置重复，重复引入js，虽没有问题，但不够精确
				}
			}
		}


		request.setAttribute("obj", curele);
		request.setAttribute("o", o);
		if (StringUtils.isNotEmpty(o.getR().getTree().getTb())) request.setAttribute("pId", request.getParameter("pId"));
		if ("1".equals(lx)){//编辑
			String hdnpara = o.getAev().getHdnpara();
			o.getAev().setHdnpara(hdnpara+"<input type='hidden' name='id' value='"+id+"'></input>");
		}

		return new ModelAndView("adm/aev", null);
	}
	
	//为表加上审核相关的四个字段
	//返回true代表成功
	public static boolean tbaddshzd(EntMapper _e, List<Map<String, Object>> tblZiduan, String tb, boolean addwhennull) throws Exception{
		if (tblZiduan==null || CollectionUtils.isEmpty(tblZiduan)) tblZiduan = _e.getTblZiduan(tb, CL.DB);
		boolean hasshzt = zdInDbcol("shzt", tblZiduan);
		boolean hasshjd = zdInDbcol("shjd", tblZiduan);
		boolean hasshyj = zdInDbcol("shyj", tblZiduan);
		boolean hasuid = zdInDbcol("uid", tblZiduan);
		String straddshzt = " add shzt  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '未提交' COMMENT '审核状态（未提交、待审核、审核通过、审核不通过、退回修改）' ,";
		String straddshjd = " add shjd int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核阶段（默认从0开始）' ,";
		String straddshyj = " add shyj varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见，可以为空，但退回修改时必须要有' ,";
		String stradduid = " add uid varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对应tjpcms_usr中的id，记录该条记录是由谁创建的' ,";
		
		if (hasshzt && hasshjd && hasshyj){
			if (!hasuid) {
				return _e.upd("alter table "+tb+stradduid.substring(0,stradduid.length()-1))> 0;
			}
		}else if (!hasshzt && !hasshjd && !hasshyj){
			if (addwhennull) {//如果3个字段都为空，是否需要增加
				String altstr=straddshzt+straddshjd+straddshyj;
				if (!hasuid) altstr += stradduid;
				return  _e.upd("alter table "+tb+altstr.substring(0,altstr.length()-1)) >0;
			}
		}else{//只有部分字段，如果随意操作数据库可能会出现这种情况，那就得修正一下
			String altstr="";
			if (!hasshzt) altstr+= straddshzt;
			if (!hasshjd) altstr+=straddshjd;
			if (!hasshyj) altstr+=straddshyj;
			if (!hasuid) altstr+=stradduid;
			return  _e.upd("alter table "+tb+altstr.substring(0,altstr.length()-1)) >0;
		}

		return true;
	}
	
	//栏目那边通用的新增保存功能
	//如果是暂存，目前就不走钩子了，其实应该走钩子，防止进行一些数据的修正，但是走钩子的话需要自行判断是否是暂存，先这样吧
	public static void addedit(HttpServletRequest request, HttpServletResponse response, EntMapper entMapper) throws Exception{
		Map<String,Object> qtjson = new HashMap<String, Object>();
		qtjson.put("ret", "-1");
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		if (o==null) {
			qtjson.put("ret", "操作失败！请查看登录是否已过期，建议保存重要信息后再刷新页面！");
			flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}

		//只判断id，id必须是唯一主键。其他情况可以走ppbc
		List<Map<String, Object>> listZiduan = getListZiduan(o);
		List<String> zjFromDb = getZjFromDb(listZiduan);//如果没设置的话，从表的主键里读
		if (CollectionUtils.isEmpty(zjFromDb) || zjFromDb.size()!=1 || !"id".equalsIgnoreCase(zjFromDb.get(0))){
			qtjson.put("ret", "操作失败！请检查"+o.getTb()+"是否正确配置了主键（id必须是唯一主键）");
			flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}

		//校验暂存，和前台有暂存功能的逻辑保持一致
		final String ___zc = request.getParameter("___zc");
		final boolean iszc = StringUtils.isNotEmpty(___zc);//是否是暂存，如果是暂存不需要进入钩子
		if (iszc && !o.getAev().isCanZc()){//非栏目时校验一下暂存
			qtjson.put("ret", "操作失败！参数（___zc）异常，请联系管理员！");
			flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}
		
		//先针对shzt,shjd,shyj,uid这四个字段做一个校验和修正
		final String cid = request.getParameter("cid");
		if (!tbaddshzd(o.getMp(), listZiduan, o.getTb(), StringUtils.isNotEmpty(cid))){//cid不为空，代表是栏目内容里提交过来的，则需要增加这几个字段的，因为栏目可以修改流程啊
			qtjson.put("ret", "操作失败！未能成功添加shzt,shjd,shyj,uid字段，请联系管理员！");
			flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}
		listZiduan = HS.getListZiduan(o);//重新获取一下，防止在钩子里被改变过了

		//这里还要校验cid的权限，因为前台是把cid作为隐藏域放到表单里提交过来，那当然可以随便改了之后提交到后台了，如果后台不校验权限，显然还是有漏洞的嘛，只不过这个版本（5.0）我先不搞了
		//1.检验一下，如果带了cid参数，但是根本查不到这个栏目，那是有问题的
		List<Map<String, Object>> listcid = null;
		if (StringUtils.isNotEmpty(cid)) {
			listcid = o.getMp().r("select s.mc lcmc,s.jsids from tjpcms_lanmu t left join tjpcms_liucheng s on s.id=t.lcid where t.id='"+cid+"' ");
			if (CollectionUtils.isEmpty(listcid) || listcid.size()!=1 || listcid.get(0)==null) {
				qtjson.put("ret", "操作失败！参数（cid）异常，请联系管理员！");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			
			//再校验一个：如果jsids为空，必须是无审核或者自定义，不能是其他的
			if (listcid.get(0).get("lcmc")!=null && !"无审核".equals(listcid.get(0).get("lcmc").toString()) && !"自定义".equals(listcid.get(0).get("lcmc").toString())
				&& (listcid.get(0).get("jsids")==null || StringUtils.isEmpty(listcid.get(0).get("jsids").toString()))) {
				qtjson.put("ret", "操作失败！只有【无审核】及【自定义】两个流程才可以没有角色序列！");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
		}
		
		boolean isadd = StringUtils.isEmpty(request.getParameter("id"));//就是这个方式来判断是新增还是编辑
		Map<String, String[]> pa = new HashMap<String, String[]>(request.getParameterMap());

		//这边要给用户留一个修改提交参数的机会，因为这边可能会有业务上的需要，比如上次一个网友说要修改价格什么的，但是又不能在前端把业务逻辑暴露出来，那就在这边的钩子里修改
		//新增前钩子
		if (!iszc && isadd && StringUtils.isNotEmpty(o.getAev().getHook_befad())) {
			Object rtn = execHook(o.getAev().getHook_befad(), request, o, pa, null);//如果有更新前钩子，执行一下
			if (rtn==null) {
				qtjson.put("ret", "-1");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof String &&  !"0".equals(rtn.toString())){
				qtjson.put("ret",  rtn.toString());
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof Map){
				Map<String,Object> mp = (Map<String,Object>)rtn;
				if (!"0".equals((String)mp.get("ret"))){
					qtjson.put("ret", mp.get("ret"));
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}
			}
		}

		//更新前的钩子
		if (!iszc && !isadd && StringUtils.isNotEmpty(o.getAev().getHook_befgx())) {
			Object rtn = execHook(o.getAev().getHook_befgx(), request, o, pa, null);//如果有更新前钩子，执行一下
			if (rtn==null) {
				qtjson.put("ret", "-1");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof String &&  !"0".equals(rtn.toString())){
				qtjson.put("ret",  rtn.toString());
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof Map){
				Map<String,Object> mp = (Map<String,Object>)rtn;
				if (!"0".equals((String)mp.get("ret"))){
					qtjson.put("ret", mp.get("ret"));
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}
			}
		}

		//因为钩子中可能改变pa的值，所以这里取值时必须使用pa.get，不能再用request.getParameter了
		listZiduan = HS.getListZiduan(o);//重新获取一下，防止在钩子里被改变过了
		boolean shztInDbcol = zdInDbcol("shzt", listZiduan);
		String uid = HT.getUid(request);
		pa.put("uid",  new String[]{uid});//记录是谁提交的记录，如果表中有uid字段则会自动匹配上生成sql
		String ex="";
		List<Map<String, Object>> ruplst = new ArrayList<Map<String, Object>>();
		if (!isadd){
			ex += " and (id='"+pa.get("id")[0]+"') ";
			//如果表中有uid这个字段，并且是普通用户或者test超级管理员
			boolean uidInDbcol = zdInDbcol("uid", listZiduan);
			if ((HT.isUsrpt(request) || HT.isTestCjgly(request)) && uidInDbcol){
				ex +=" and (uid='"+HT.getUid(request)+"') ";
			}
			ruplst = o.getMp().r("select * from "+o.getTb()+" where 1=1 "+ex);
			if(CollectionUtils.isEmpty(ruplst) || ruplst.size()!=1 || ruplst.get(0)==null) {
				qtjson.put("ret", "操作失败！该记录无效或无权操作该记录");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}

			Object rupuid = ruplst.get(0).get("uid");
			Object rupshen = ruplst.get(0).get("shzt");
			if (uidInDbcol && (rupuid==null || StringUtils.isEmpty(rupuid.toString()))
				|| (rupshen==null || StringUtils.isEmpty(rupshen.toString())) && shztInDbcol) {//如果表里有uid，记录的uid为空，这不是有问题嘛。那肯定就是手动操作数据库的了
				qtjson.put("ret", "操作失败！该记录uid或shzt字段值异常为空！");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}

			//对于正式超管也增加了一个限制，即：未提交的他人记录不可编辑，人家还没提交呢，你就编辑了？这权限也太大了吧，维护人员DBA这种可以这么玩，超管也不行
			if (HT.isZsCjgly(request)){
				if (rupuid!=null && !rupuid.toString().equals(uid) && rupshen!=null && "未提交".equals(rupshen.toString())){
					qtjson.put("ret", "操作失败！他人尚未提交的记录不可编辑！");
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}
			}else{//针对审核进行一个后端校验：即在更新时，不是正式超管的用户，对于状态是未提交或退回修改的本人才可以编辑，否则不能编辑。呼应前台的逻辑(listczq.js中的fn_cmnsh_aftsx)
				if (rupuid!=null && !rupuid.toString().equals(uid) || rupshen!=null && !"未提交".equals(rupshen.toString()) && !"退回修改".equals(rupshen.toString())){
					qtjson.put("ret", "操作失败！只有【未提交】或【退回修改】的本人记录才可以编辑！");
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}
			}
		}
		
		//主要就是四个字段的逻辑，shzt，shjd，shyj，uid
		//如果是栏目保存，则根据栏目的审核流程自动加上审核状态
		//shjd默认是0（从1开始，1代表第一个阶段），uid就是用户登录id
		//如果不是栏目列表中的菜单，只是一个普通的菜单，也想要有流程，应该来说也不是不可以，当然我建议最简单的是把菜单放到栏目列表里，如果实在不能放，恐怕得需要自己按照字段来写审核了
		if (shztInDbcol){//表中有shzt字段，既可能是来自栏目的菜单，也可以是来自普通菜单但自行增加了审核的想关字段
			if (HT.isZsCjgly(request)){//正式超级管理员提交的，如果是编辑的话可能有shzt和shyj的字段
				if (isadd){
					pa.put("shzt",  new String[]{iszc?"未提交":"审核通过"});//实际上表中的shzt默认为未提交，但这里也写一下吧
				}else{//是更新
					Object obuid = ruplst.get(0).get("uid");
					if (obuid==null || StringUtils.isEmpty(obuid.toString())){//该记录没有uid字段
						qtjson.put("ret", "操作失败！"+o.getTb()+"表中有shzt字段，但异常没有uid字段，请确认是否人为修改过表字段或值！");
						flushResponse(response, JSONObject.fromObject(qtjson));
						return;
					}else if (obuid.toString().equals(uid)){//该记录是我的
						if (!iszc) pa.put("shzt",  new String[]{"审核通过"});//如果是暂存，不改变其审核状态
						pa.put("shyj", new String[]{""});
					}else{//该记录不是我的
						pa.put("uid", new String[]{obuid.toString()});//不能修改作者啊
						Object obshzt = ruplst.get(0).get("shzt");
						if (obshzt==null || StringUtils.isEmpty(obshzt.toString())){
							qtjson.put("ret", "操作失败！"+o.getTb()+"表中有shzt字段，但异常没有shzt的值，请确认是否人为修改过表字段或值！");
							flushResponse(response, JSONObject.fromObject(qtjson));
							return;
						}else if ("未提交".equals(obshzt.toString())){//如果是他人未提交的，是不能编辑的，与前台的逻辑呼应
							qtjson.put("ret", "操作失败！他人尚未提交的记录不可编辑！");
							flushResponse(response, JSONObject.fromObject(qtjson));
							return;
						}else if (zdInDbcol("cid", listZiduan)){//不是自己的，且状态不是未提交的，且是来自栏目的请求
							//校验一下必填
							String tjshzt = pa.get("shzt")[0];
							String tjshyj = pa.get("shyj")[0];
							if (StringUtils.isNotEmpty(tjshzt) && ("审核不通过".equals(tjshzt) || "退回修改".equals(tjshzt)) && StringUtils.isEmpty(tjshyj)){
								qtjson.put("ret", "操作失败！设置【"+tjshzt+"】时，请填写审核意见！");
								flushResponse(response, JSONObject.fromObject(qtjson));
								return;
							}
						}else{//不是来自栏目的，自行在钩子中处理
							
							
						}
					}
				}
			}else if (StringUtils.isEmpty(cid)){//test超管或者普通用户提交的，非栏目提交的
				
				//这里我不做任何操作了，由开发人员自行在前后钩子里操作
				
			}else{//test超管或者普通用户提交的，栏目提交的
				//再校验一下暂存，只有有有效的审核流程时，才有暂存，否则并不需要
				Map<String, Object> mplc = listcid.get(0);
				if (iszc && ((mplc.get("lcmc")==null  || StringUtils.isEmpty(mplc.get("lcmc").toString())
					|| "无审核".equals(mplc.get("lcmc").toString()) /*|| "自定义".equals(mplc.get("lcmc").toString())*/
					|| mplc.get("jsids")==null ||StringUtils.isEmpty(mplc.get("jsids").toString())) &&  !"自定义".equals(mplc.get("lcmc").toString()))){
					qtjson.put("ret", "操作失败！异常的暂存！");
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}
				
				//校验一下更新时的jsids
				if (!isadd && (mplc.get("jsids")==null || StringUtils.isEmpty(mplc.get("jsids").toString())) 
					&& "退回修改".equals(ruplst.get(0).get("shzt")) && (mplc.get("lcmc")!=null && !"自定义".equals(mplc.get("lcmc").toString()))){//!isadd时，shzt必不为空
					qtjson.put("ret", "操作失败！该记录对应的审核流程异常为空！");
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}

				//还剩下3种情况：1、新增的提交，2、未提交的更新，3、退回修改的更新
				if (mplc.get("lcmc")==null ||StringUtils.isEmpty(mplc.get("lcmc").toString()) ||  "无审核".equals( mplc.get("lcmc").toString())) {//栏目未配置流程或者栏目所属流程是无审核，则直接审核通过（退回修改不会走到这里）
					pa.put("shzt",  new String[]{iszc?"未提交":"审核通过"});//实际这里iszc不可能是true了，因为已经校验过暂存了
				}else if ("自定义".equals( mplc.get("lcmc").toString())){
					
					//这搞个钩子
					
				}else if (iszc){//是3种情况下的暂存
					if (isadd) pa.put("shzt",  new String[]{"未提交"});//未提交和退回修改时，暂存操作，审核状态不变
				}else{//按照流程的角色序列，以及当前用户的角色来设置shjd
					String[] jsids = mplc.get("jsids").toString().split("→");//必不为空
					Integer xlidx=0;
					for (;xlidx<jsids.length;xlidx++){
						if (HT.getJsid(request).equals(jsids[xlidx])) break;
					}
					if (xlidx<jsids.length){//在流程里找到了自己的角色
						if (xlidx == jsids.length-1){//我是流程的最后一个角色，那直接审核通过，如果流程中只有一个角色，也是进入这个判断
							pa.put("shzt",  new String[]{"审核通过"});
							pa.put("shjd",  new String[]{String.valueOf(jsids.length+1)});
						}else{//找到了，但不是流程的最后一个环节
							pa.put("shzt",  new String[]{"待审核"});
							pa.put("shjd",  new String[]{String.valueOf(xlidx+2)});//+1从程序索引到mysql的索引，再+1到下一流程
						}
					}else{//没找到，视为需要流程的第一个阶段角色审核，实际上一般来说，应当时流程中的第一个角色提交，由第二个角色审核
						pa.put("shzt",  new String[]{"待审核"});
						pa.put("shjd",  new String[]{"1"});//mysql中substring_index就是从1开始的
					}
					pa.put("shyj",  new String[]{""});//审核意见清空
				}
			}
		}//if (shztInDbcol){

		//执行新增或更新
		Map<String,Object> rtnval  = new HashMap<String, Object>();
		String rtnmsg = ppbc(o.getMp(), o.getTb(), request, pa, isadd, zjFromDb, ex,  listZiduan, rtnval);
		if (!"0".equals(rtnmsg)){
			flushResponse(response, JSONObject.fromObject(qtjson));
			return;
		}

		//更新后钩子
		if (!iszc && !isadd && StringUtils.isNotEmpty(o.getAev().getHook_aftgx())) {
			Object rtn = execHook(o.getAev().getHook_aftgx(), request, o, pa, null);//如果有更新后钩子，执行一下
			if (rtn==null) {
				qtjson.put("ret", "-1");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof String &&  !"0".equals(rtn.toString())){
				qtjson.put("ret",  rtn.toString());
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof Map){
				Map<String,Object> mp = (Map<String,Object>)rtn;
				if (!"0".equals((String)mp.get("ret"))){
					qtjson.put("ret", mp.get("ret"));
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}else if(StringUtils.isNotEmpty(o.getAev().getJs_aft_bc())){
					 String parseJsAftbc = parseJsAftbc(mp, o);
					 if (StringUtils.isNotEmpty(parseJsAftbc)) {
						 qtjson.put("js_aft_bc", parseJsAftbc);
					}
				}
			}
		}

		//新增后的钩子
		if (!iszc && isadd && StringUtils.isNotEmpty(o.getAev().getHook_aftad())) {
			Object rtn = execHook(o.getAev().getHook_aftad(), request, o, pa, null);
			if (rtn==null) {
				qtjson.put("ret", "-1");
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof String &&  !"0".equals(rtn.toString())){
				qtjson.put("ret",  rtn.toString());
				flushResponse(response, JSONObject.fromObject(qtjson));
				return;
			}
			if (rtn instanceof Map){
				Map<String,Object> mp = (Map<String,Object>)rtn;
				if (!"0".equals((String)mp.get("ret"))){
					qtjson.put("ret", mp.get("ret"));
					flushResponse(response, JSONObject.fromObject(qtjson));
					return;
				}else if(StringUtils.isNotEmpty(o.getAev().getJs_aft_bc())){
					 String parseJsAftbc = parseJsAftbc(mp, o);
					 if (StringUtils.isNotEmpty(parseJsAftbc)) {
						 qtjson.put("js_aft_bc", parseJsAftbc);
					}
				}
			}
		}

		String tip = isadd?"新增成功":"更新成功";
		if (!o.getR().isJubu() && !"2".equals(___zc)) request.getSession().setAttribute("ses_ht_tip", tip);//略有点侵入业务。如果有树，则局部刷新，则不用这种session提示
		qtjson.put("ret", "0");
		qtjson.put("tip", tip);
		if (isadd && "2".equals(___zc)) qtjson.put("zcnewid", rtnval.get("id"));
		flushResponse(response, JSONObject.fromObject(qtjson));
	}
	
	//2018-05-02
	//这里原来是只从表里取记录，看是否唯一，现在改成从setlist里取
	public static void unique(HttpServletRequest request, HttpServletResponse response, EntMapper entMapper){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("getdata", "false");//默认失败，即不唯一

		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		String id = request.getParameter("id");
		String lx = request.getParameter("lx");
		String val = request.getParameter("val");
		String zdm = request.getParameter("zdm");
		if (o==null ||StringUtils.isEmpty(val)||StringUtils.isEmpty(zdm) || !("0".equals(lx) || "1".equals(lx)) || ("1".equals(lx) && StringUtils.isEmpty(id))
			|| StringUtils.isEmpty(o.getR().getSql_c())){
			HS.flushResponse(response, JSONObject.fromObject(map));
			return;
		}

		String sql_c = o.getR().getSql_c()+ " and ( "+zdm+"='"+val+"' ) ";

		String filter = request.getParameter("___extfilter");
		if (StringUtils.isNotEmpty(filter)) {
			sql_c += " and  "+filter+" ";
		}
		if ("1".equals(lx)) {//编辑
			sql_c += " and  (id!='"+id+"') ";
		}
		map.put("getdata", o.getMp().cnt(sql_c)==0?"true":"false");
		flushResponse(response, JSONObject.fromObject(map));
	}
	
	//crud的通用删除
	public static void delete(HttpServletRequest request, HttpServletResponse response, EntMapper entMapper) throws Exception{
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		if (o==null){
			HS.flushResponse(response, "删除失败！请刷新查看登录是否过期后重试！");
			return;
		}

		//删除前钩子
		//先执行钩子，这样给一个先修正参数的机会，否则先检查参数的话，ids参数必须正确
		Map<String, String[]> pa = new HashMap<String, String[]>(request.getParameterMap());
		if (StringUtils.isNotEmpty(o.getD().getHook_befdel())) {
			Object ret = execHook(o.getD().getHook_befdel(), request, o, pa, null);
			if (ret==null || !"0".equals(ret.toString())){
				flushResponse(response,ret.toString());
				return;
			}
		}

		if (pa.get("ids")==null || pa.get("ids").length<=0 || StringUtils.isEmpty(pa.get("ids")[0]) || pa.get("ids")[0].split(",")==null || pa.get("ids")[0].split(",").length<=0){
			flushResponse(response,"删除失败！ids参数异常错误！");
			return;
		}

		String arr[] = pa.get("ids")[0].split(",");
		List<Map<String, Object>> r = o.getMp().r("select * from "+o.getTb()+" where id in "+arr2instr(arr));
		if (CollectionUtils.isEmpty(r) || r.size()!= arr.length){
			flushResponse(response,"删除失败！请检查ids参数是否正确有效、无重复！");
			return;
		}

		
		//正式超管：只有审核不通过的才能删除
		//非正式超管或普通用户：只有【审核不通过】的本人记录才可以删除
		List<Map<String, Object>> listZiduan = getListZiduan(o);
		boolean shztInDbcol = zdInDbcol("shzt", listZiduan);
		if (HT.isZsCjgly(request)){//正式超管
			if (shztInDbcol) {
				for (Map<String, Object> map : r) {
					Object obshzt = map.get("shzt");
					if (obshzt==null || !"审核不通过".equals(obshzt.toString())){
						flushResponse(response,"删除失败！只有【审核不通过】的记录才可以删除！");
						return;
					}
				}
			}
		}else{//其他用户
			if (shztInDbcol || zdInDbcol("uid", listZiduan)){//有审核状态字段或者uid字段都可以判断
				String uid = HT.getUid(request);
				for (Map<String, Object> map : r) {
					Object obuid = map.get("uid");
					if (obuid!=null && !obuid.toString().equals(uid)){
						flushResponse(response,"删除失败！无权删除他人记录！");
						return;
					}

					Object obshzt = map.get("shzt");
					if (obshzt!=null && !"审核不通过".equals(obshzt.toString())){
						flushResponse(response,"删除失败！只有【审核不通过】的记录才可以删除！");
						return;
					}
				}
			}
		}


		String batdel ="";
		if (StringUtils.isNotEmpty(o.getD().getZdysql())){
			batdel = HS.zdydel(o, arr, "");
		}else{
			batdel = HS.batdel(o.getMp(), o.getTb(), arr, "");
		}
		if (!"0".equals(batdel)){
			flushResponse(response,batdel);
			return;
		}

		//删除后钩子
		if (StringUtils.isNotEmpty(o.getD().getHook_aftdel())) {
			Object ret = execHook(o.getD().getHook_aftdel(), request, o, null, null);
			if (ret==null || !"0".equals(ret.toString())){
				flushResponse(response,"-1");
				return;
			}
		}
		
		if (!o.getR().isJubu()) request.getSession().setAttribute("ses_ht_tip", "删除成功！");
		
		HS.flushResponse(response, "0");
	}
	

	//这是上传图片到服务器的处理
	public static void ajaxupload(MultipartFile file, HttpServletRequest request, HttpServletResponse response, EntMapper entMapper) throws IllegalStateException, IOException{
		Map<String, Object> m = new HashMap<String, Object>();
		if (file==null){
			m.put("ret","请先选择文件！");
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}
		
		String uptag = request.getParameter("up_size_check_tag");
		if (!"pic".equals(uptag) && !"vdo".equals(uptag) ){
			m.put("ret","上传异常，请联系管理员！");
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}
		
		String oriName= file.getOriginalFilename();
	    String prefix=oriName.substring(oriName.lastIndexOf("."));//如  .png
		if ("pic".equals(uptag) && !".jpg".equalsIgnoreCase(prefix) && !".png".equalsIgnoreCase(prefix) 
			&&  !".gif".equalsIgnoreCase(prefix) && !".jpeg".equalsIgnoreCase(prefix)){
			m.put("ret","请选择jpg、jpeg、png或gif格式的图片！");
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}
		if ("vdo".equals(uptag) && !".mp4".equalsIgnoreCase(prefix) && !".flv".equalsIgnoreCase(prefix)){
			m.put("ret","请选择mp4或flv格式的视频！");
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}
		
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String YY = fmt.format(date).substring(0, 4);
		String MM = fmt.format(date).substring(4, 6);
		String path = request.getServletContext().getRealPath("")+File.separator+"upload"+File.separator+uptag+File.separator+YY+File.separator+MM.toString()+File.separator;
	    File mulu = new File(path);
		if (!mulu.exists()){
			mulu.mkdirs();
		}
		String uid = UUID.randomUUID().toString();
		String upSrc = fmt.format(date).substring(0,14)+uid.substring(3, 6)+fmt.format(date).substring(14,17)+uid.substring(9, 12)+uid.substring(19, 23)+prefix;
		File bcFile = new File(path+upSrc);  
		file.transferTo(bcFile);  

		m.put("ret","0");
		m.put("src",request.getContextPath()+"/upload/pic/"+YY+"/"+MM+"/"+upSrc);
		m.put("orimc", file.getOriginalFilename());
		m.put("size", String.format("%.2f MB",(double)bcFile.length()/1048576));

		HS.flushResponse(response, JSONObject.fromObject(m));
	}
	
	//table body的动态生成
	public static void tbs(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		Map<String, Object> m = new HashMap<String, Object>(){{put("ret","-1");}};
		if (o==null) {
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}

		Retrieve r = o.getR();
		createSql(o, cxqGetcxfilter(o, r.getCxs(), r.getDefs(), request), r.getExtselect(), request);
		
		//最终请求结果
		m.put("ret", "0");
		//数据项
		m.put("tabs", r.getTbs());
		m.put("zds", r.getZds());
		//分页项
		m.put("recTotal", r.getRecTotal());
		m.put("pgTotal", r.getPgTotal());
		m.put("perPage", r.getPerPage());
		m.put("curPage", r.getCurPage());
		
		//
		m.put("odbatdel",o.getD().isBatdel());
		
		//Thread.sleep(600);//测试的

		HS.flushResponse(response, JSONObject.fromObject(m));//ajax请求时，全部数据都是从本次请求中获得，跟后端就没关系了，此时是前端控制页面
	}

	//动态获取Retrieve中的tree
	public static void tree(HttpServletRequest request, HttpServletResponse response) throws InterruptedException{
		Crud o = (Crud)request.getSession(false).getAttribute(request.getParameter("u"));
		Map<String, Object> m = new HashMap<String, Object>(){{put("ret","-1");}};
		if (o==null) {
			HS.flushResponse(response, JSONObject.fromObject(m));
			return;
		}

		Tree tree = o.getR().getTree();
		m.put("ret", "0");
		m.put("tree", o.getMp().r("select t."+tree.getId()+" id,t."+tree.getpId()+" pId,t."+tree.getName()+" name,\'true\' open from "+o.getTb()+" t	where 1=1 order by t.px desc,t.gx desc,t.rq desc,t.id desc"));
		HS.flushResponse(response, JSONObject.fromObject(m));
	}
	//================================================================================================================================
	//	系统crud功能相关结束
	//================================================================================================================================

	
	
	public static void flushResponse(HttpServletResponse response, Object responseContent) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(ConvertUtils.convert(responseContent));
		} catch (IOException e) {
			
		} finally {
			writer.flush();
			writer.close();
		}
	}

	public static void setGlobal( HttpServletRequest reques) {
		reques.setAttribute("WZMC", CL.WZMC);
		reques.setAttribute("GGY", CL.GGY);
		reques.setAttribute("GGY2", CL.GGY2);
		reques.setAttribute("YUMING", CL.YUMING);
		reques.setAttribute("WANGZHI", CL. WANGZHI);
		reques.setAttribute("htgllj", XT.htgllj);
		reques.setAttribute("dlhtlj", XT.dlhtlj);
		reques.setAttribute("dlhtlj_test", XT.dlhtlj_test);
		reques.setAttribute("nedcjgly", XT.nedcjgly);
	}

	public static Object execHook(String hook, HttpServletRequest request, Crud o, Object pa, Object beiyong){
		if (StringUtils.isNotEmpty(hook) && request!=null && o!=null){
			int dtIndx = hook.lastIndexOf(".");
			if (dtIndx != -1) {
				String clsnm = hook.substring(0, dtIndx);
				String metd = hook.substring(dtIndx+1, hook.length());
				if (StringUtils.isNotEmpty(clsnm) && StringUtils.isNotEmpty(metd)) {
					try{
						Class<?> classT = Class.forName(clsnm);//获取对应类
						Method methodT = classT.getMethod(metd, Object.class, Object.class, Object.class, Object.class);
						Object obj = classT.newInstance();//创建类对象 
						return methodT.invoke(obj, (Object)request, (Object)o, (Object)pa, (Object)beiyong);
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}
				}
			}
		}

		return null;
	}

	private static String  parseJsAftbc(Map<String,Object> mp, Crud o){
		if (mp==null || o==null) return null;
		
		String s = o.getAev().getJs_aft_bc();
		if (StringUtils.isEmpty(s)) return null;
		
		int you = s.lastIndexOf(")");
		if (you==-1)  return null;
		int zuo = s.lastIndexOf("(", you);
		if (zuo==-1)  return null;
		String nr = s.substring(zuo+1, you);
		if (StringUtils.isEmpty(nr)) return null;
		String[] split = nr.split(",\\s*");
		if (split==null || split.length<=0) return null;
		String ti ="";//替换
		for (String kee : split) {
			String val = (String)mp.get(kee);
			if (StringUtils.isNotEmpty(val)){
				ti+="'"+val+"',";
			}
		}

		return s.substring(0,zuo)+"("+ti.substring(0,ti.length()-1)+")";
	}
	
	//这2个供外部调用
	public static String ppbc(EntMapper _e, String tb, HttpServletRequest request) throws Exception{
		boolean isadd = StringUtils.isEmpty(request.getParameter("id"));

		return ppbc(_e, tb, request,null, isadd, Arrays.asList("id")," and id='"+request.getParameter("id")+"'", null, null);
	}
	public static String ppbc(EntMapper _e, String tb, HttpServletRequest request, Map<String,String[]> pa) throws Exception{//这种情况是因为有可能不需要所有的字段，只用一部分
		if (pa==null || pa.size()<=0){
			throw new  Exception("如果传递了pa参数，那就使用pa来getParameter了");
		}
		
		boolean isadd = (pa.get("id")==null || StringUtils.isEmpty(pa.get("id")[0]));
		String wh=isadd?"":(" and id='"+pa.get("id")[0]+"'");
		
		return ppbc(_e, tb, request, pa, isadd, Arrays.asList("id"),wh, null, null);
	}

	//返回"ret:0"表示执行正确，返回"ret:-1"表示执行失败，logger.error输出错误信息，ppbc：匹配保存
	@Transactional
	private static String ppbc(EntMapper _e, String tb, HttpServletRequest request , Map<String,String[]> pa, boolean isadd, List<String> objzjs, String wh, List<Map<String, Object>> listZiduan, Map<String,Object> rtnval) throws Exception{
		if (StringUtils.isEmpty(tb) ||CollectionUtils.isEmpty(objzjs)){
			return "-1";
		}
		
		if (pa==null || pa.size()<=0){//如果pa有值就用pa的
			pa = request.getParameterMap();
		}
		if (pa==null || pa.size()<=0){
			return "-1";
		}
		
		if (CollectionUtils.isEmpty(listZiduan)){
			listZiduan = _e.getTblZiduan(tb, CL.DB);
		}
		if (CollectionUtils.isEmpty(listZiduan)) {
			return "-1";
		}

		boolean hasGxZd = false, gxzdHasVal = false;
		boolean hasUrlZd =false;
		for (int i = 0;i<listZiduan.size();i++){
			String col_name = (String)listZiduan.get(i).get("COLUMN_NAME");
			if ("url".equalsIgnoreCase(col_name)) hasUrlZd = true;//表中有url字段
			else if ("gx".equalsIgnoreCase(col_name)) hasGxZd = true;//表中有gx字段
		}

		if (!isadd){//是更新
			String upd = "";
			for (Map.Entry<String,  String[]> e : pa.entrySet()) {//遍历提交的表单
				String k = e.getKey();
				for (int i = 0;i<listZiduan.size();i++){
					String col_name = (String)listZiduan.get(i).get("COLUMN_NAME");
					if (col_name.equalsIgnoreCase(k)){
						if (!objzjs.contains(k)){//不是主键字段
							String[] vlus = e.getValue();
							if (vlus.length==1){
								String v = e.getValue()[0];
								if ("on".equalsIgnoreCase(v)){
									v="1";
								}
								
								Object dtype = listZiduan.get(i).get("data_type");
								if (dtype==null) {
									throw new  Exception("怎么可能会发生某个数据库表的某个字段的data_type读不出来的情况呢？反正我是没遇到，如果遇到我怀疑是没看压缩包里的必读，我发誓我看过压缩包里的必读了");
								}
								if ("int".equalsIgnoreCase(dtype.toString()) && StringUtils.isEmpty(v)){//如果该字段是int类型，但是传递的是空字符串，则改为null
									upd += k+"=null,";
								}else{
									upd += k+"='"+v+"',";
								}
								if ("gx".equalsIgnoreCase(k))gxzdHasVal = true;//设置过了gx的值，下面就不再给其赋值了
							}else{
								upd += k+"='"+StringUtils.join(vlus, ",")+"',";
							}
						}
						listZiduan.remove(i);
						break;
					}
				}
			}
			if (!"".equals(upd)){
				String s = "update "+tb+" set "+upd.substring(0, upd.length()-1);
				if (hasGxZd && !gxzdHasVal) s+=" ,gx= '"+CL.fmt.format(new Date())+"'  ";
				s+=" where 1=1 "+wh;
				if (_e.upd(s)==1) return "0";
			}
		}else{//是add，如果是新增栏目内容，为该表加上url（前台只使用这个字段就可以了）,url_s和url_d三个字段
			String into = "", vls="", strCidVal = "";
			for (Map.Entry<String,  String[]> e : pa.entrySet()) {
				String k = e.getKey();
				for (int i = 0;i<listZiduan.size();i++){
					String col_name = (String)listZiduan.get(i).get("COLUMN_NAME");
					if (col_name.equalsIgnoreCase(k)){
						if (!objzjs.contains(k)){//不是主键字段
							String[] vlus = e.getValue();
							if (vlus.length==1){
								String v = vlus[0];
								into += k+",";
								if ("on".equalsIgnoreCase(v)){
									v="1";
								}
								if ("cid".equalsIgnoreCase(k)){
									strCidVal = v;
								}

								Object dtype = listZiduan.get(i).get("data_type");
								if (dtype==null) {
									throw new  Exception("怎么可能会发生某个数据库表的某个字段的data_type读不出来的情况呢？反正我是没遇到，如果遇到我怀疑是没看压缩包里的必读，我发誓我看过压缩包里的必读了");
								}
								if ("int".equalsIgnoreCase(dtype.toString()) && StringUtils.isEmpty(v)){//如果该字段是int类型，但是传递的是空字符串，则改为null
									vls += "null,";
								}else{
									vls += "'"+v+"',";
								}
								if ("gx".equalsIgnoreCase(k))gxzdHasVal = true;
							}else{//有多个
								into += k+",";
								vls += "'"+StringUtils.join(vlus, ",")+"',";
							}
						}else{//主键不是自增长的那种
							for (String zj : objzjs) {
								into += zj+",";
								vls += "'"+e.getValue()[0]+"',";
							}
						}
						listZiduan.remove(i);
						break;
					}
				}
			}
			if (!"".equals(into)){
				String strUrl3 = "";
				if (StringUtils.isNotEmpty(strCidVal)){//如果需要栏目内容静态化，提交的表单中有必须有cid字段
					List<Map<String, Object>> lstNrTbl = _e.r("select distinct id,url3 from tjpcms_lanmu where nrtbl='"+tb+"' ");
					if (CollectionUtils.isNotEmpty(lstNrTbl)){//要存的这张表内容是可以静态化的，不管栏目内容当前设置是否是只动态化显示，即使只动态显示，后面也可以改成静态显示的
						strUrl3 = getTbUrl3(lstNrTbl, strCidVal, _e);//根据表名和cid值算出url3字段的值，预备存入url_d字段，为静态化做好准备
						if (StringUtils.isNotEmpty(strUrl3) && !hasUrlZd) {
							if(_e.upd("alter table "+tb+" add url varchar(200), add url_s varchar(200),add url_d varchar(200)")<=0) {
								strUrl3 = "";
							}
						}
					}
				}
				String s = "insert into "+tb+"("+into.substring(0, into.length()-1);
				if (hasGxZd && !gxzdHasVal) {
					s+=",gx";
				}
				if (StringUtils.isNotEmpty(strUrl3)){
					s+=",url_d";//url在插入成功后填充，url_s在静态化的时候再填充
				}
				s+= ") values("+vls.substring(0, vls.length()-1);
				if (hasGxZd && !gxzdHasVal) {
					s+=",'"+CL.fmt.format(new Date())+"' ";
				}
				if (StringUtils.isNotEmpty(strUrl3)){
					s += ",'"+strUrl3+"' ";
				}
				s+=")";
				Map<String, Object> pmp= new HashMap<String, Object>();
				pmp.put("id", "");//用于存这次插入的记录的id
				pmp.put("sql", s);
				if (1==_e.ins(pmp)) {
					if (rtnval!=null)rtnval.put("id", pmp.get("id"));//把这个新增的id记录下来，暂存的时候可以用
					if (StringUtils.isNotEmpty(strUrl3) && StringUtils.isNotEmpty((String)pmp.get("id"))){//插入后更新url字段
						_e.upd("update "+tb+" set url='"+request.getContextPath()+"/"+strUrl3+".dhtml?id="+pmp.get("id")+"' where id="+pmp.get("id"));
					}
					 return "0";
				}
			}
		}

		return "-1";
	}

	private static String getTbUrl3(List<Map<String, Object>> lst, String strCidVal, EntMapper mp){
		List<Map<String, Object>> r = mp.r("select id,pId,url3 from tjpcms_lanmu");
		while (StringUtils.isNotEmpty(strCidVal)){
			for (Map<String, Object> e : lst) {
				if (e.get("id").toString().equals(strCidVal)){
					return (String)e.get("url3");
				}
			}
			int i=0;
			for (;i<r.size();i++) {
				if (strCidVal.equals(r.get(i).get("id").toString())){
					if (r.get(i).get("pId")==null)continue;
					strCidVal = r.get(i).get("pId").toString();//往上跳一级继续找
					break;
				}
			}
			if(i>=r.size()) break;
		}
		
		return "";
	}
	
	private static String batdel(EntMapper mp, String tb, String[] arr, String exw){
		
		if (mp==null || arr==null || arr.length<=0|| StringUtils.isEmpty(tb)){
			return "-1";
		}
		String s = "delete from "+tb+" where id in(";
		for(int i =0;i<arr.length;i++){
			s +="'"+arr[i]+"',";
		}

		int del = mp.del(s.substring(0,s.length()-1)+") "+exw);
		//System.out.println(del);
		return (del==arr.length)?"0":"删除失败，请查看是否具有权限！";
	}

	private static String zdydel(Crud o, String[] arr, String exw){
		if (o==null || o.getMp()==null || StringUtils.isEmpty(o.getTb()) || StringUtils.isEmpty(o.getD().getZdysql()) || arr==null || arr.length<=0){
			return "-1";
		}

		String zdy = o.getD().getZdysql();
		String s = zdy+" where 1=1 "+exw+" and id in( ";
		for(int i =0;i<arr.length;i++){
			s +="'"+arr[i]+"',";
		}
		if (zdy.trim().toLowerCase().startsWith("update")){
			return (o.getMp().upd(s.substring(0,s.length()-1)+")")==arr.length)?"0":"删除失败，请查看是否具有权限";
		}
		
		return (o.getMp().del(s.substring(0,s.length()-1)+")")==arr.length)?"0":"删除失败，请查看是否具有权限";
	}

	//根据sql直接查个数
	public static int cnt(EntMapper mp, String tb, String ex){
		if (mp==null || StringUtils.isEmpty(tb)){
			return -1;
		}

		String s ="select count(*) from "+tb+" where 1=1 "+(StringUtils.isNotEmpty(ex)?ex:"");

		return mp.cnt(s);
	}
	
	//crud中的retrieve，带pg
	public static List<Map<String,Object>> rpg(EntMapper mp, String tb, HttpServletRequest rq){
		String pg = rq.getParameter("pg");
		Integer npg = 0;
		try{
			npg = Integer.valueOf(pg);
		}catch(Exception e){
			npg = 1;
			pg = "1";
		}
		String c = "select count(*) from "+tb;
		Integer cnt = mp.cnt(c);
		if (!(npg>=1 && npg<=cnt)){
			return null;
		}

		String s = "select * from "+tb+" order by px desc,gx desc limit "+(npg-1)*CL.LIST_PP_DEF+","+CL.LIST_PP_DEF;

		return mp.r(s);
	}

	public static int vpx(EntMapper mp, String tb){
		String s = "select IFNULL(max(px)+1, 1) from "+tb;

		return mp.vpx(s);
	}

	//根据字典表得到select的option，可以逗号分隔的词组，zdb中查或者自定义sql查
	public static List<Map<String,Object>> fnsel(String a0lxm, EntMapper mp, String py, String exw){
		List<Map<String, Object>> sel= new ArrayList<Map<String, Object>>();

		if ("zdb".equals(a0lxm) && mp!=null && StringUtils.isNotEmpty(py)){
			String s = "select s.zdxmc txt,s.zdxval val,s.sys from tjpcms_zdb t left join tjpcms_zdx s on t.id = s.pId where (t.py = '"+py+"') ";
			if (StringUtils.isNotEmpty(exw)) {
				s+= " and ("+exw+")";
			}
			s+=" order by s.px desc ,s.gx desc";
			for (Map<String, Object> e : mp.r(s)) {
				sel.add(e);
			}
		}else if (("sel".equals(a0lxm) || "select".equals(a0lxm)) && mp!=null && StringUtils.isNotEmpty(py)){
			String[] split = py.split(":");
			if (split !=null && split.length>0 && split[0].trim().equalsIgnoreCase("sql")) {//这是直接执行sel:sql后续的sql代码
				if (split.length>1 && StringUtils.isNotEmpty(split[1])){
					List<Map<String, Object>> r = mp.r(split[1]);
					if (CollectionUtils.isNotEmpty(r)){
						if (r.get(0).keySet().size()==1){//sql里只查了一个字段
							for (Map<String, Object> m : r) {
								Map<String, Object> tpmp = new HashMap<String, Object>();
								for (String k : m.keySet()) {
									tpmp.put("val", m.get(k));
									tpmp.put("txt", m.get(k));
								}
								sel.add(tpmp);
							}
						}else{
							for (Map<String, Object> m : r) {
								Map<String, Object> tpmp = new HashMap<String, Object>();
								int jishu=0;
								for (String k : m.keySet()) {
									if (jishu++==0)tpmp.put("val", m.get(k)); else tpmp.put("txt", m.get(k));
								}
								sel.add(tpmp);
							}
						}
					}
				}
			}else{//这是把逗号分隔的词组转成select
				split = py.split(",\\s*");
				if (split !=null && split.length>0){
					for (String s : split) {
						Map<String, Object> tpmp = new HashMap<String, Object>();
						tpmp.put("val", s);
						tpmp.put("txt", s);
						sel.add(tpmp);
					}
				}
			}
		}

		return sel;
	}

	//hds[][]中当该字段类型为text:zdb时，从字典表中读出map
	//这儿，当时可能是java版本的原因，数字和字符串这儿有问题，所以按照类型来区分，现在发现不用区分也对了，难道真的是java版本升级的原因？
	public static Map<Object,Object> textzdbmap(EntMapper mp, String zdb){
		Map<Object, Object> ret= new HashMap<Object, Object>();

		if (mp!=null && StringUtils.isNotEmpty(zdb)){
			List<Map<Object, Object>> ro = mp.ro( "select  zdxmc txt, zdxval val from tjpcms_zdx t left join tjpcms_zdb s on t.pId = s.id where (s.py='"+zdb+"')");
			for (Map<Object, Object> e : ro) {
				String val = (String)e.get("val");
		        if(Pattern.compile("^-?\\d+$").matcher(val).matches()){//是整数
//		        	ret.put(Long.valueOf(val), e.get("txt"));
		        	ret.put(val, e.get("txt"));
		        }else{
		        	ret.put(val, e.get("txt"));
		        }
			}
		}

		return ret;
	}

	public static Map<String,Object> obj(EntMapper mp, String tb, String id){
		if (mp==null || StringUtils.isEmpty(tb) || StringUtils.isEmpty(id)){
			return null;
		}

		String s = "select * from "+tb+" where (id ="+id+")";

		return mp.obj(s);
	}

	public static Map<String,Object> objw(EntMapper mp, String tb, String w){
		if (mp==null || StringUtils.isEmpty(tb) || StringUtils.isEmpty(w)){
			return null;
		}

		String s = "select * from "+tb+" where 1=1 and "+w+" limit 0,1";

		return mp.obj(s);
	}

	//之前用的一个方便后台查数据的接口，现在已经被Crud替代了，不过前台还是可以用用的
	public static Map<String,Object> listpg(EntMapper mp, String tb, HttpServletRequest rq, String bread){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("recTotal",0);
		m.put("pgTotal",0);
		m.put("perPage",CL.LIST_PP_DEF);
		if (mp==null || StringUtils.isEmpty(tb) || rq==null){
			return m;
		}
		String pg = rq.getParameter("pg");
		Integer npg = 0;
		try{
			npg = Integer.valueOf(pg);
		}catch(Exception e){
			npg = 1;
			pg = "1";
		}
		Integer recTotal = mp.cnt("select count(*) from "+tb);
		Integer pgTotal = (int)Math.ceil(recTotal/(double)CL.LIST_PP_DEF);
		if (!(npg>=1 && npg<=recTotal)){
			return m;
		}

		String s = "select * from "+tb+" order by px desc,gx desc limit "+(npg-1)*CL.LIST_PP_DEF+","+CL.LIST_PP_DEF;
		m.put("list",mp.r(s));
		m.put("recTotal",recTotal);
		m.put("pgTotal",pgTotal);
		m.put("pg", pg);
		m.put("bread", bread);

		String msg = rq.getParameter("msg");
		if("0".equals(msg) || "1".equals(msg) || "2".equals(msg)){
			Integer idx = Integer.valueOf(msg);
			String arr[] = {"新增成功！", "删除成功！","编辑成功！"};
			m.put("msg", arr[idx]);
		}

		return m;
	}

	//把上面那个再改造一下
	public static Map<String,Object> listpgex(Map<String,Object> map, EntMapper _e, HttpServletRequest request, String tb, Integer pp, String zd, String wh){
		map.put("recTotal",0);
		map.put("pgTotal",0);
		map.put("perPage", pp);
		if (_e==null || StringUtils.isEmpty(tb) || request==null){
			return map;
		}
		String pg = request.getParameter("pg");
		Integer npg = 0;
		try{
			npg = Integer.valueOf(pg);
		}catch(Exception e){
			npg = 1;
			pg = "1";
		}
		if (pp==null || pp<=0)pp = CL.LIST_PP_DEF;
		Integer recTotal = _e.cnt("select count(*) from "+tb+" "+(StringUtils.isNotEmpty(wh)?("where "+wh):""));
		Integer pgTotal = (int)Math.ceil(recTotal/(double)pp);
		if (recTotal>0 && !(npg>=1 && npg<=recTotal)){
			return map;
		}

		String s = "select t.*"+(StringUtils.isNotEmpty(zd)?(","+zd):"")+" from "+tb+" t "+(StringUtils.isNotEmpty(wh)?("where "+wh):"")+" order by px desc,gx desc limit "+(npg-1)*pp+","+pp;
		map.put("lst",_e.r(s));
		map.put("recTotal",recTotal);
		map.put("pgTotal",pgTotal);
		map.put("pg", pg);
	
		return map;
	}
	
	//字段是否在数据库字段列中
	public static boolean zdInDbcol(String zdm, List<Map<String,Object>> listZiduan) throws Exception{
		if (StringUtils.isEmpty(zdm) || CollectionUtils.isEmpty(listZiduan)){
			throw new Exception("异常错误！zdInDbcol参数错误！");
		}

		for(Map<String,Object> e:listZiduan){
			String col_name = ((String)e.get("COLUMN_NAME"));
			if (col_name.equalsIgnoreCase(zdm)){
				return true;
			}
		}

		return false;
	}
	public static boolean zdInDbcol(String zdm, String bm, EntMapper _e) throws Exception{
		if (StringUtils.isEmpty(zdm) || StringUtils.isEmpty(bm) || _e==null){
			throw new Exception("异常错误！zdInDbcol参数错误！");
		}

		return zdInDbcol(zdm, _e.getTblZiduan(bm, CL.DB));
	}

	//字段是否在数据库字段列中，或者在hds中，在的话就可以从list中取数据。
	//这个函数只在解析ext文字的时候用到，就是要确定某个ext的字段是不是需要从tabs里去取
	private static String zdInHdsOrDbOrJoin(String[][] hds, String zdm, Crud o) throws Exception{
		if (StringUtils.isEmpty(zdm) || o==null) {
			return "";
		}

		List<Map<String,Object>> listZiduan = getListZiduan(o);
		if (CollectionUtils.isEmpty(listZiduan)){
			return "";
		}

		String plusv = "0";
		if (zdm.contains("+")){
			String[] split = zdm.split("[+]");
			zdm = split[0].trim();
			plusv = split[1].trim();
			if (StringUtils.isEmpty(plusv)) plusv="0";
		}
		
		if (zdInDbcol(zdm, listZiduan)){
			return plusv;
		}
		
		List<String> exjoinzdm = o.getR().getExtjoinzdm();//left join tjpcms_zdx s on t.lx=s.zdxmc     exjoinzdm.add("s.zdxpy py");       o.getR().getExtjoinzdm().add("y.mc shlcmc,y.jsids ___lcjsids");
		if (CollectionUtils.isNotEmpty(exjoinzdm)) {
			for (String s : exjoinzdm) {
				String[] split1 = s.trim().split(",");//先按逗号分隔
				if (split1!=null && split1.length>0){
					for (int x=0;x<split1.length;x++){
						String[] split2 = split1[x].trim().split("\\s+");//再按空白符号分割
						if (split2.length>=2){//有空白符
							if (zdm.equals(split2[1].trim()))return plusv;
						}else{//单独的
							split2 = s.trim().split(".");
							if (zdm.equals(split2[split2.length-1].trim()))return plusv;
						}
					}
				}
			}
		}
		
		//这里又要增加extzd了，这是第三部分了，也是一个字段的来源
		List<String>lstzds = o.getR().getExtzdm();
		if (CollectionUtils.isNotEmpty(lstzds)) {
			for (String s : lstzds) {
				if (s.equalsIgnoreCase(zdm)){
					return plusv;
				}
			}
		}
		
		if (hds==null){
			return "";
		}
		for (int i = 0; i < hds.length; i++) {
			if (hds[i]!=null && hds[i].length>1 && hds[i][1].trim().equalsIgnoreCase(zdm)) return plusv;
		}

		return "";
	}

	//列表区表头解析
	//0：title
	//1：字段名
	//2-7：顺序不必固定，可以是sql(比较常用)，style，pic，ext（文字修饰），text（可以将字段值由数字转成对应的汉字，如0-否，1-是这种），href（可以点击跳转到新页面）
	//normal和text类型有什么区别？normal是从查询结果里取的值，text可以是固定值文字或者是字典表转换后的的数据
	private static  void parseBtq(HttpServletRequest request, Crud o,  String[][] hds, String[][] czs) throws Exception{
		if (hds==null || hds.length<=0 || o==null){
			return ;
		}

		//为有审核流程的栏目自动配下面几个列
		if ("com.tjpcms.common.Hook.___cmnsh_aftcx".equals(o.getR().getHook_aftcx())){
			int insidx =hds.length;
			for (int i=0;i<hds.length;i++) {
				if (hds[i]!=null && hds[i].length>=2){
					if ("px".equals(hds[i][1])) {insidx = i;break;}
					else if ("gx".equals(hds[i][1])) {insidx = i;break;}
				}
			}
			hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"作者uid（昵称）", "uid","ext:zznc"});
			hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"作者昵称","zznc","sql:select nc zznc from tjpcms_usr  where id=t.uid","style:display:none"});
			hds= (String[][])ArrayUtils.add(hds, insidx++, new String[]{"审核状态（意见）","shzt","ext:shyj:（:）"});
			hds= (String[][])ArrayUtils.add(hds,  insidx++, new String[]{"审核流程(阶段)","___shlcmc"});
			hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"下属角色","xsjs","sql:select id from tjpcms_juese where id=(select jsid from tjpcms_usr where id=t.uid )", "style:display:none"});
			hds= (String[][])ArrayUtils.add(hds, insidx++,  new String[]{"是否待我审核","sfdwsh", "sql:case when shzt='待审核' and substring_index(substring_index(y.jsids, '→', shjd), '→', -1) = '"+HT.getJsid(request)+"' then '1' else '0' end", "style:display:none"});
		}

		
		List<Map<String,Object>> ths = o.getR().getThs();
		List<Map<String,Object>> zds = o.getR().getZds();
		if (o.getD().isBatdel()){
			Map<String,Object> e = new HashMap<String,Object>();
			e.put("title", "<input  type='checkbox'/>");
			e.put("notip", true);
			e.put("style", "width:3em");
			ths.add(e);
		}
		{
			Map<String,Object> e = new HashMap<String,Object>();
			e.put("style", "width:5em;padding-left:1px;padding-right:1px;");
			e.put("title", "序号");
			ths.add(e);
		}

		String extselect = "";
		for (int i=0;i<hds.length;i++) {//遍历hds
			Map<String,Object> e = new HashMap<String,Object>();
			String head = hds[i][0];
			if ("操作".equals(head)) continue;//配置hds时，不需要自行写操作列

			String zdm = hds[i][1];//字段名
			e.put("title", head);
			e.put("zdm", zdm);
			e.put("empcoltip", "");//
			e.put("zdType", "normal");//最普通的就是列表字段
			for (int j = 2; j <= 7; j++) {//第2,3,4,5,6,7字段位置可以不固定，根据具体内容来判断来判断类型
				if (!(hds[i].length>=j+1 && StringUtils.isNotEmpty(hds[i][j]))) continue;
				
				String cont = hds[i][j].trim();
				String tparr[] = cont.split(":");
				if (cont.startsWith("sql")){//说明是自定义字段
					e.put("issql", true);
					if (tparr.length > 1) extselect+=" ("+tparr[1]+")"+zdm+",";//select  (..sql..)zdm ,...... from tb t
				}else if (cont.startsWith("zdb")){
					e.put("zdType", "text");
					e.put("textmap", textzdbmap(o.getMp(), tparr[1]));
					e.put("textmpzd", tparr.length>=3?tparr[2]:zdm);
				}else if (cont.startsWith("style")){//是style字段
					int indexOf = cont.indexOf("=");
					if(indexOf==-1) indexOf = cont.indexOf(":");
					if (indexOf+1 < cont.length()) {
						String sub = cont.substring(indexOf+1, cont.length());
						e.put("style", sub.replaceAll("\\s*", ""));
						if (e.get("style").toString().indexOf("display:none")!=-1) e.put("display", "none");
					}
				}else if (cont.startsWith("pic")){//该字段是图片类型
					e.put("zdType", "pic");
					if (tparr.length > 1) e.put("empcoltip",tparr[1]);
				}else if (cont.startsWith("ext")){//该字段要额外添加文字
					e.put("exts", "(");//默认的追加文字
					e.put("exttail", true);//head还是tail，就是说追加的文字是放在正式文字的前后还是后面，默认追加在后面
					if (cont.endsWith("::")){//这种情况就是值追加内容，不写左右符号
						e.put("exte", "");e.put("exts", "");
					}else if (tparr.length==2){//这种情况就是左右符号用默认的，就是英文的左右括号(内容...)
						e.put("exte", ")");
					}else if (tparr.length==3) {
						e.put("exts", tparr[2]);
					}else if (tparr.length==4) {
						e.put("exte", tparr[3]);e.put("exts", tparr[2]);
					}else if (tparr.length>=5) {
						e.put("exte", tparr[3]);
						e.put("exts", tparr[2]);
						e.put("exttail", "tail".equals(tparr[4]));
						if (tparr.length>=6){e.put("extsub", tparr[5]);}//这个配置用来截取字符串的前多少位
					}
					if (tparr.length > 1) {
						e.put("extc", tparr[1]);//内容
						String inret = zdInHdsOrDbOrJoin(hds, tparr[1], o);
						e.put("extdbzd", StringUtils.isNotEmpty(inret));
						if (StringUtils.isNotEmpty(inret) && !"0".equals(inret)) {
							e.put("extc", tparr[1].split("[+]")[0]);//内容
							e.put("extp", inret);//plus的值，比如shjd+1这种
						}
					}
				}else if (cont.startsWith("text")){//不从查询sql里根据字段取值了，可以是val、zdb
					e.put("zdType", "text");
					if (tparr.length>=3){
						if ("val".equals( tparr[1])){
							e.put("textval", tparr[2]);
						}else if ("zdb".equals( tparr[1])){
							e.put("textmap", textzdbmap(o.getMp(), tparr[2]));
							e.put("textmpzd", tparr.length>=4?tparr[3]:zdm);
						}
					}
				}else if (cont.startsWith("href")){//该字段可以点击
					parseHdsHref(tparr, e);
				}else{//剩下的这个是字段值为空时的提示文字
					e.put("empcoltip", hds[i][j]);
				}
			}
			
			//这个table的宽度实在是不智能，难道我只能用js来调？css应该可以啊
			if (StringUtils.isEmpty((String)e.get("style"))){
				if ("id".equals(zdm) || "px".equals(zdm) || "pId".equalsIgnoreCase(zdm)) e.put("style", "width:5em;padding-left:1px;padding-right:1px;");
				else if ("gx".equals(zdm) || "rq".equals(zdm)) e.put("style", "width:10em;");
			}
			zds.add(e);
			if(e.size()>0) ths.add(e);
		}

		//如果点击是的fulanmu_nr，则额外增加一个栏目列，以区分内容是哪个栏目的
		if ("父栏目nr".equals(o.getClx())){
			Map<String,Object> e = new HashMap<String,Object>();
			e.put("title", "栏目");
			ths.add(e);
			List<Map<String, Object>> cxs = o.getR().getCxs();
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> selopslst = (List<Map<String,Object>>)cxs.get(cxs.size()-1).get("selops");
			Map<String,String> txtmp = new HashMap<String,String>();
			for (Map<String,Object> esel:selopslst){
				txtmp.put(esel.get("val").toString(), (String)esel.get("txt"));
			}
			e.put("textmap", txtmp);
			e.put("textmpzd", "cid");//根绝cid直接把栏目名称查出来
			e.put("zdType", "text");
			zds.add(e);
		}
		//如果有操作列，则增加上该列
		if (/*o.getAev().isNeeda() ||*/ o.getAev().isNeede() || o.getAev().isNeedv() || o.getD().isNeed() || (czs!=null && czs.length>0)){
			Map<String,Object> e = new HashMap<String,Object>();
			e.put("title", "操作");
			ths.add(e);
		}

		o.getR().setExtselect(extselect);
	}
	
	private static void parseHdsHref(String tparr[], Map<String,Object> e ){
		if (tparr!=null && tparr.length>=2 && tparr.length<=3){
			String sHf="";
			boolean tgblank = false;
			for(int i=1;i<=(tparr.length-1);i++){
				if ("_blank".equalsIgnoreCase(tparr[i])){
					tgblank = true;
				}else if (StringUtils.isNotEmpty(tparr[i])){
					sHf = tparr[i];
				}
			}
			if (StringUtils.isEmpty(sHf)){
				return;
			}

			int dhIdx = sHf.indexOf(".dhtml");
			if (dhIdx!=-1 && !sHf.startsWith(".dhtml")){
				int whIdx = sHf.indexOf("?",dhIdx);
				if(whIdx==-1) {
					e.put("href", sHf);
					e.put("tgblank", tgblank);
				}else{//有参数   xqhd_detail.dhtml?id=${pId}
					List<Map<String, Object>> hrefmp = new ArrayList<Map<String, Object>>();
					int eqIdx = sHf.indexOf("=",whIdx+1);
					while (eqIdx!=-1){
						int zdSt = sHf.lastIndexOf("&",eqIdx-1);
						if (zdSt==-1 || zdSt<whIdx)zdSt = sHf.lastIndexOf("?",eqIdx-1);
						if (zdSt!=-1 && zdSt<eqIdx){
							String zdm = sHf.substring(zdSt+1, eqIdx).trim();
							if (StringUtils.isNotEmpty(zdm)) {
								Map<String, Object> eZd = new HashMap<String, Object>();
								eZd.put("zdm", zdm);
								int adIdx = sHf.indexOf("&", eqIdx+1);
								if (adIdx==-1) adIdx = sHf.length();
								if (eqIdx+1<adIdx){
									String szdz = sHf.substring(eqIdx+1, adIdx).trim();
									if (StringUtils.isNotEmpty(szdz)) {
										if (szdz.startsWith("$") && szdz.endsWith("}")){
											int lkhidx = szdz.indexOf("{");
											if (lkhidx != -1) {
												String tplzdm = szdz.substring(lkhidx+1,szdz.length()-1).trim();
												eZd.put("tplzdm", tplzdm);
											}
										}else{
											eZd.put("zdz", szdz);
										}
									}
								}
								hrefmp.add(eZd);
							}
						}
						eqIdx = sHf.indexOf("=",eqIdx+1);
					}
					if (CollectionUtils.isNotEmpty(hrefmp)) {
						e.put("href", sHf.substring(0, dhIdx+".dhtml".length()));
						e.put("hrefmp", hrefmp);
						e.put("tgblank", tgblank);
					}
				}
			}
		}
	}
	
	//表单字段是否在栏目配置中
	private static int formZdInLmpz(String name, List<Map<String,Object>> lstZd, int[] lmpzidx){
		if (StringUtils.isEmpty(name) || CollectionUtils.isEmpty(lstZd)){
			return -1;
		}

		if (name.endsWith("___kssj") || name.endsWith("___jssj")){
			for(int i=0;i<lstZd.size();i++){
				Map<String, Object> m = lstZd.get(i);
				if ("sj".equals(m.get("lx").toString())){//该字段是否是时间类型
					String zdm = (String)m.get("zdm");
					String cls = (String)m.get("cls");
					String postfix = name.endsWith("___kssj")?"___kssj":"___jssj";
					String gvzd= name.substring(0,name.indexOf(postfix));
					if (zdm.equalsIgnoreCase(gvzd) && postfix.contains(cls)){//时间类型的话，不仅字段名要相同，开始结束的类型也要相同索引才对
						lmpzidx[0] = i;
						return i;
					}
				}
			}
		}else{
			for(int i=0;i<lstZd.size();i++){
				if (name.equalsIgnoreCase((String)lstZd.get(i).get("zdm"))){
					lmpzidx[0] = i;
					return i;
				}
			}
		}

		return -1;
	}

	//根据操作字符串生成listOp
	private static List<Map<String,Object>> getCxqOps(String opstr){
		List<Map<String,Object>> ops = new ArrayList<Map<String,Object>>();
		if (StringUtils.isEmpty(opstr)){
			Map<String,Object> one = new HashMap<String,Object>();
			one.put("val", "like");
			one.put("text", "包含");
			ops.add(one);
		}else{
			String[] arr = opstr.split(",");
			for(int x=0;x<arr.length;x++) {
				Map<String,Object> one = new HashMap<String,Object>();
				one.put("val", "eq".equalsIgnoreCase(arr[x])?"=":arr[x]);
				String[][] seltxt = {{"eq","like",">","<",">=","<=","=","==","!=","<>"},{"等于","包含","大于","小于","大于等于","小于等于","等于","等于","不等于","不等于"}};
				for (int i = 0; i < seltxt[0].length; i++) {
					if (arr[x].equalsIgnoreCase(seltxt[0][i])){
						one.put("text", seltxt[1][i]);
						break;
					}
				}
				
				ops.add(one);
			}
		}
		
		return ops;
	}

	//查询区单个字段长度的计算
	private static int calcCzqWzWidth(List<Map<String,Object>> listCx){
		int cxq_wzWidth = -1;
		int FZISE=16;
		for (int i = 0; i < listCx.size(); i++) {
			int cxw = ((String)listCx.get(i).get("wz")).length()*FZISE;
			if (cxw>cxq_wzWidth) cxq_wzWidth = cxw;
			if (cxq_wzWidth<=FZISE*2)  cxq_wzWidth*=1.2;//两个字的时候因为还要放个：，就有点挤了，微调一下
		}

		return cxq_wzWidth;
	}

	//如果查询页面某字段有初始值，那和查询区提交的表单字段合并一下
	private static Map<String, String[]> unionParaMap(List<Map<String,Object>> listDef, Map<String, String[]> pmp){
		Map<String, String[]> retmp = new HashMap<String, String[]>(pmp);
		if (CollectionUtils.isNotEmpty(listDef) && pmp !=null) {
			for (Map<String, Object> e : listDef) {
				if (pmp.get((String)e.get("zdm")) == null){//不能是已经存在的，存在说明页面查询区做了选择，要覆盖掉默认值
					retmp.put((String)e.get("zdm"),   new String[]{(String)e.get("val")});
					retmp.put(e.get("zdm")+"___op",  new String[]{(String)e.get("op")});
				}
			}
		}

		return retmp;
	}
	
	private static boolean isStrKaishi(String str){
		return StringUtils.isNotEmpty(str) && (str.trim().equals("ks") || str.trim().equals("kaishi") || str.trim().equals("start") || str.trim().equals("k"));
	}
	
	//解析查询区时间类型的def值
	private static void parseCxqSjDef(List<Map<String,Object>> listDef, String[] line, Map<String,Object> ks , Map<String,Object> js, HttpServletRequest rq){
		if (line==null || ks==null || js==null){
			return;
		}
		
		//如果是点了页面中的查询或者翻页，或者右键TAB项刷新，都不需要出现默认项的框框
		if (line.length>=3 && Pattern.compile("^\\s*def\\s*:", Pattern.CASE_INSENSITIVE).matcher(line[2]).find()){//该字段设置了有默认值，如def:ks:eq:2017-02-14:js:eq:xxxx
			String zdm=line[1];
			String pzVal = line[2].substring(line[2].indexOf(":")+1);
			String arr[] = pzVal.split(":");
			if (arr.length==2){//可以省略是开始还是结束，默认是开始
				if (StringUtils.isEmpty(rq.getParameter(zdm+"___kssj"))) ks.put("hasDef", 1);
				Map<String, Object> edef = new HashMap<String, Object>();
				edef.put("zdm", zdm+"___kssj");//时间的字段名要加后缀___kssj, ___jssj
				edef.put("val", arr[1].trim());
				edef.put("op", "eq".equalsIgnoreCase(arr[0])?"=":"like");
				listDef.add(edef);
			}else if (arr.length==3){//配置了一个日期的默认值
				if (isStrKaishi(arr[0]) && StringUtils.isEmpty(rq.getParameter(zdm+"___kssj"))) {ks.put("hasDef", 1);}
				else if (!isStrKaishi(arr[0]) && StringUtils.isEmpty(rq.getParameter(zdm+"___jssj"))) {js.put("hasDef", 1);}
				Map<String, Object> edef = new HashMap<String, Object>();
				edef.put("zdm", zdm+(isStrKaishi(arr[0])?"___kssj":"___jssj"));
				edef.put("val", arr[2].trim());
				edef.put("op", "eq".equals(arr[1])?"=":"like");
				listDef.add(edef);
			}else if (arr.length==6){//配置了两个日期的默认值
				if (StringUtils.isEmpty(rq.getParameter(zdm+"___kssj")) && StringUtils.isEmpty(rq.getParameter(zdm+"___jssj"))) {ks.put("hasDef", 1);js.put("hasDef", 1);}
				for (int i=0;i<2;i++){
					boolean isSt = isStrKaishi(arr[i*3]);
					Map<String, Object> edef = new HashMap<String, Object>();
					edef.put("zdm", zdm+(isSt?"___kssj":"___jssj"));
					edef.put("val", arr[i*3+2].trim());
					edef.put("op", "eq".equals(arr[i*3+1])?"=":"like");
					listDef.add(edef);
				}
			}
		}
	}
	
	//列表页查询区配置解析（时间类型要也可以加def）。通过解析得到了下面四个结果，但是这里要改造一下，因为要适应局部刷新的情况
	//map.put("cxq", listCx)
	//map.put("cxfilter", cxfilter)
	//rq.setAttribute("cxq_haveShijian", true)
	//rq.setAttribute("cxq_wzWidth", calcCzqWzWidth(listCx))
	//lx和zdb(sel,select)是不能同时存在的，zdb就是lx嘛
	private static void parseCxq(HttpServletRequest request, Crud o, String[][] cxs){
		if (o==null || o.getMp()==null || StringUtils.isEmpty(o.getTb()) ||  cxs==null || cxs.length<=0){
			return ;
		}
		
		//如果是有树，或者引入了ztree，则pId要配置到查询区中，否则无法查询pId的
		//为什么cxs要配一个{"","pId","lx:hidden"}呢，hanshu.cxqGetcxfilter里也讲了，如果是想通过字段名称一致的方式来查询数据的，还是必须要配到cxs里，以避免无心的匹配
		if (StringUtils.isNotEmpty(o.getR().getTree().getTb()) || o.getR().isInc_ztree()) {
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"","pId","lx:hidden"});//如果也有字段不在查询区，但是需要能查到的，类似这样写（等于自行维护一个查询字段，比如用户管理中的pId）
		}
		
		//再自动加上审核相关的查询项，如果栏目没有审核，则不需要加
		//一共5个：shzt,uid,zznc,xsjs,sfdwsh
		if ("com.tjpcms.common.Hook.___cmnsh_aftcx".equals(o.getR().getHook_aftcx())){
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"审核状态","shzt","zdb:wzshzt","def:eq:全部"});
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"作者uid","uid","op:eq,!=,like"});
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"作者昵称","zznc"});
			String list2instr = HS.list2instr(HS.getZisunNodes(HT.getJsid(request), o.getMp().r("select id,pId from tjpcms_juese")));
			String sqlxsjs = "select id,pId,mc name from tjpcms_juese where id in"+list2instr;
			List<Map<String, Object>> r = o.getMp().r(sqlxsjs);
			if (CollectionUtils.isNotEmpty(r)) cxs= (String[][])ArrayUtils.add(cxs, new String[]{"下属角色","xsjs","tree:"+sqlxsjs});
			cxs= (String[][])ArrayUtils.add(cxs, new String[]{"是否待我审核","sfdwsh","zdb:shifou"});
		}

		//查询区内容及排版
		List<Map<String,Object>> listCx = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listDef= new ArrayList<Map<String,Object>>();//记录哪些字段有默认值
		for (int i=0;i<cxs.length;i++) {
			String wz = cxs[i][0];//文字
			String zdm = cxs[i][1];//字段名
			if("gx".equalsIgnoreCase(zdm)) {//更新字段，自动拆成两个
				Map<String,Object> ks =  new HashMap<String,Object>();
				ks.put("wz", "开始"+wz);
				ks.put("zdm", "gx");
				ks.put("lx", "sj");//时间类型
				ks.put("cls", "kssj");//时间类型
				listCx.add(ks);
				Map<String,Object> js =  new HashMap<String,Object>();
				js.put("wz", "结束"+wz);
				js.put("zdm", "gx");
				js.put("lx", "sj");
				js.put("cls", "jssj");
				listCx.add(js);
				request.setAttribute("cxq_haveShijian", true);//查询区有时间字段
				parseCxqSjDef(listDef, cxs[i], ks, js, request);
			}else if ("rq".equalsIgnoreCase(zdm)){//查的是记录产生的日期
				Map<String,Object> ks =  new HashMap<String,Object>();
				ks.put("wz", wz+"开始");
				ks.put("zdm", "rq");
				ks.put("lx", "sj");//时间类型
				ks.put("cls", "kssj");//时间类型
				listCx.add(ks);
				Map<String,Object> js =  new HashMap<String,Object>();
				js.put("wz", wz+"结束");
				js.put("zdm", "rq");
				js.put("lx", "sj");
				js.put("cls", "jssj");
				listCx.add(js);
				request.setAttribute("cxq_haveShijian", true);//查询区有时间字段
				parseCxqSjDef(listDef, cxs[i], ks, js, request);
			}else{//普通字段
				Map<String,Object> e =  new HashMap<String,Object>();
				e.put("wz", wz);
				e.put("zdm", zdm);
				e.put("lx","input");//默认是input
				e.put("ops", getCxqOps(null));//默认操作符(like)
				for(int m=2;m<4;m++){//从第三个开始，前两个默认文字和字段名
					if (!(cxs[i].length>=m+1 && StringUtils.isNotEmpty(cxs[i][m]))) continue;
					String cont = cxs[i][m].trim();
					String arr[] = cont.split(":");
					if (arr.length<2) continue;
						
					String a0lxm = arr[0].trim().toLowerCase();
					if ("zdb".equals(a0lxm) || "sel".equals(a0lxm)||"select".equals(a0lxm)){
						e.put("lx", "sel");//zdb也是sel类型
						e.put("selops", fnsel(a0lxm, o.getMp(), cont.substring(cont.indexOf(':')+1), ""));
					}else if ("op".equals(a0lxm)){
						e.put("ops", getCxqOps(arr[1]));//默认操作符
					}else if ("def".equals(a0lxm)){//该字段设置了有默认值，如def:eq:待审核
						if (arr.length>=3){
							e.put("val", arr[2]);
							if (StringUtils.isEmpty(request.getParameter(zdm))) e.put("hasDef", 1);//只有初始进入页面时，需要这个默认值提示框
							Map<String, Object> edef = new HashMap<String, Object>();
							edef.put("zdm", zdm);
							edef.put("val", arr[2]);
							edef.put("op", "eq".equals(arr[1])?"=":"like");
							listDef.add(edef);
						}
					}else if ("lx".equals(a0lxm)){
						if ("sj".equalsIgnoreCase((String)arr[1].trim())){
							e = null;//不再添加e了，添加ks和js了
							Map<String,Object> ks =  new HashMap<String,Object>();
							ks.put("wz", wz+"开始");
							ks.put("zdm", zdm);
							ks.put("lx", "sj");//时间类型
							ks.put("cls", "kssj");//时间类型
							listCx.add(ks);
							Map<String,Object> js =  new HashMap<String,Object>();
							js.put("wz", wz+"结束");
							js.put("zdm", zdm);
							js.put("lx", "sj");
							js.put("cls", "jssj");
							listCx.add(js);
							request.setAttribute("cxq_haveShijian", true);//查询区有时间字段
							parseCxqSjDef(listDef, cxs[i], ks, js, request);
						}else if ("hidden".equalsIgnoreCase((String)arr[1].trim()) || "hdn".equalsIgnoreCase((String)arr[1].trim())){
							e.put("lx", "hidden");
						}
					}else if ("tree".equals(a0lxm)){
						e.put("lx", "tree");
						e.put("treelist", JSONArray.fromObject(o.getMp().r(arr[1])));
					}
				}//for(int m=2;m<4;m++)
				if (e!=null) listCx.add(e);
			}
		}
		//这种情况（点击了栏目树的fulanmu_nr）就需要可以选栏目，额外增加这里的判断，额外又多了一个查询条件
		if ("父栏目nr".equals(o.getClx())){
			Map<String,Object> e =  new HashMap<String,Object>();
			e.put("wz", "栏目");
			e.put("zdm", "cid");
			e.put("lx","sel");
			e.put("selops", o.getMp().r("select id val,name txt from tjpcms_lanmu where id in"+HS.arr2instr(o.getCchild())));
			listCx.add(e);
		}
		request.setAttribute("cxq_wzWidth", calcCzqWzWidth(listCx));
		o.getR().setCxs(listCx);
		o.getR().setDefs(listDef);
	}
	
	//把这边构建查询区sql过滤条件的代码单独撇出来，这样无论是整页刷新还是单页刷新都可以调这个函数了
	//这边最主要是request的不错，最终的过滤条件结果也是不同的
	//注释掉的那个elseif是发现，如果提交过来的链接中有非查询区的字段，按照这个逻辑就会拼一段查询条件，但有可能这个字段并不是我想查询的，只是为了传递数据的，比如zdb中查看字典项，zdx.dhtml?id=xx，这个id是字典表id，我只是想
	//传递给zdx，让其知道pId是谁，但如果按照没注释掉代码的逻辑，就会变成查询zdx中id=xx的记录了，这个是不对的，所以我还是打算弄一个cxq的hidden
	private static String cxqGetcxfilter(Crud o, List<Map<String,Object>> listCx, List<Map<String,Object>> listDef, HttpServletRequest request){
		String cxfilter="";

		Map<String, String[]> pa = unionParaMap(listDef, request.getParameterMap());//把表单提交的字段值和设置的字段默认值合并到新的pa里，下面就遍历pa来拼sql了
		if (pa!=null && pa.size()>0){
			List<Map<String,String>> fmZds = new ArrayList<Map<String,String>>();//记录提交过来的字段
			Map<String,String> fmopmp = new HashMap<String,String>();
			for (Map.Entry<String,  String[]> e : pa.entrySet()) {//先把操作符弄出来
				String[] value = e.getValue();
				if (value !=null && value.length>0 && StringUtils.isNotEmpty(value[0])){
					String name = e.getKey();
					if (name.endsWith("___op") && !name.startsWith("___op")){//有操作符
						fmopmp.put(name.substring(0,name.indexOf("___op")), " "+value[0]+" ");
					}
				}
			}

			for (Map.Entry<String,  String[]> e : pa.entrySet()) {//遍历提交的表单项
				String[] value = e.getValue();
				if (value !=null && value.length>0 && StringUtils.isNotEmpty(value[0])){
					String zdm = e.getKey();
					int[] lmpzidx =new int[1];
					if (zdm.endsWith("___kssj")){//开始日期字段
						if (formZdInLmpz(zdm, listCx,lmpzidx)!=-1){
							cxfilter += " and  left("+zdm.substring(0,zdm.indexOf("___kssj"))+",10) >= '"+value[0]+"' ";
							listCx.get(lmpzidx[0]).put("val", value[0]);
						}
					}else if (zdm.endsWith("___jssj")){//结束日期字段
						if (formZdInLmpz(zdm, listCx, lmpzidx)!=-1){
							cxfilter += " and  left("+ zdm.substring(0,zdm.indexOf("___jssj"))+",10) <= '"+value[0]+"' ";
							listCx.get(lmpzidx[0]).put("val", value[0]);
						}
					}else if (zdm.endsWith("___op") && !zdm.startsWith("___op")){//有操作符
						continue;
					}else if (formZdInLmpz(zdm, listCx, lmpzidx)!=-1){//是查询区配置的字段
						Map<String,String> onezd = new HashMap<String,String>();
						onezd.put("name", zdm);
						onezd.put("value", value[0]);
						onezd.put("lx", (String)listCx.get(lmpzidx[0]).get("lx"));
						fmZds.add(onezd);
						listCx.get(lmpzidx[0]).put("val", value[0]);
						if(fmopmp.get(zdm)!=null) listCx.get(lmpzidx[0]).put("op", fmopmp.get(zdm).trim());
						
						//针对是tree类型查询的情况做一个特殊处理，没办法，因为tree在操作时，显示的是中文，但是存储时存储的是id
						//采用初始化插件时，把文字查出来设置val的方式，因此val_wz就不需要设置了
						if ("tree".equalsIgnoreCase(onezd.get("lx"))) {//是查询区表单字段inputtree
							//listCx.get(lmpzidx[0]).put("val_wz", pa.get("___cxq_fp_tree_"+zdm)[0]);
						}
					}/*else if (zdInDbcol(zdm, getListZiduan(o))){//字段不在配置的查询区里，当然也可以查询了，比如查pId，就直接等于吧。先用zdInDbcol吧，实际上应该是tabs里的字段加上db里的字段才是最全的
						cxfilter += " and "+zdm+"="+value[0]+" ";
					}*/
				}
			}
			for(Map<String,String> e:fmZds){//fmZds此时可以直接从fmopmp取操作符了，如果没有默认like
				String op = " like ";
				String name = e.get("name");
				String value = e.get("value");
				String lx = e.get("lx");
				if (StringUtils.isNotEmpty(fmopmp.get(name))) op = fmopmp.get(name);
				else if ("sel".equalsIgnoreCase((String)e.get("lx")) || "tree".equalsIgnoreCase((String)e.get("lx"))) op = "=";
				if (op.contains("like")){
					value = " '%"+value+"%' ";
					cxfilter += " and ("+name+op+value+") ";
				}else if (  !("sel".equalsIgnoreCase(lx) && ("___-1".equals(value) || "全部".equals(value)))  ){
					value= " '"+value+"' "; 
					cxfilter += " and ("+name+op+value;
					if (op.contains(">") || op.contains("<"))cxfilter+="+0 ";
					cxfilter+=" ) ";
				}
			}
		}
		
		return cxfilter;
	}
	
	//先按照字符串中不能有空格来，href可以传递带空格的参数吗？
	private static String[] getCzqHref(String href, Map<String, Object> onerec){
		
/*		if (StringUtils.isNotEmpty(href) && onerec !=null){
			int whIdx = href.indexOf("?");
			if (whIdx!=-1){
				int eqIdx = whIdx+1;
				do{
					eqIdx = href.indexOf("=", eqIdx);
					if (){
						
					}
				}while(eqIdx!=-1);
			}
		}*/

		String retarr[]= new String[2];
		String[] split = href.split(":");
		for (int i = 0; i < split.length; i++) {
			if ("_blank".equalsIgnoreCase(split[i])){
				retarr[1] = "_blank";
			}else if (StringUtils.isNotEmpty(split[i])){
				href = split[i];
			}
		}
		retarr[0]=href;

		//../sqrh.dhtml
		List<Map<String, Object>> posLst = new ArrayList<Map<String, Object>>();
		int whIdx = href.indexOf("?");
		if (whIdx!=-1){
			int eqIdx = whIdx+1;
			do{
				eqIdx = href.indexOf("=&", eqIdx);
				if (eqIdx !=-1){
					int adlast = href.lastIndexOf("&", eqIdx-1);
					if (adlast==-1){adlast = href.lastIndexOf("?", eqIdx-1);}
					if (adlast!=-1){
						Map<String, Object> e = new HashMap<String, Object>();
						e.put("pos", eqIdx+1);//等于号下一位置
						e.put("zdm", href.substring(adlast+1, eqIdx));
						posLst.add(e);
					}
					eqIdx+=2;
				}
			}while(eqIdx!=-1);
			//再加一个结尾是=的情况
			if (href.endsWith("=")){
				Map<String, Object> e = new HashMap<String, Object>();
				e.put("pos", href.length());
				int adlast = href.lastIndexOf("&", href.length()-2);
				if (adlast==-1){adlast = href.lastIndexOf("?", href.length()-2);}
				e.put("zdm", href.substring(adlast+1, href.length()-1));
				posLst.add(e);
			}
		}

		if (CollectionUtils.isNotEmpty(posLst)) {
			retarr[0]="";
			String copy = new String(href);
			for (int i = 0; i < posLst.size(); i++) {
				Integer pos = (Integer)posLst.get(i).get("pos");
				String zdm = (String)posLst.get(i).get("zdm");
				int st = (i==0?0:pos);
				int ed = (i==posLst.size()-1?copy.length():(Integer)posLst.get(i+1).get("pos"));
				retarr[0] += copy.substring(st,pos)+onerec.get(zdm)+ copy.substring(pos,ed);
			}
		}
		
		return retarr;
	}
	
	private static String getCzqJs(Crud o, String js, Map<String, Object> onerec) {
		String ret="";
		int idx1 = js.indexOf("(");
		int idx2 = js.indexOf(")");
		if (idx1!=-1 && idx2!=-1){
			if (idx1+1==idx2){//没有参数的情况
				ret = js.substring(0,idx2+1);
			}else{
				String paras = js.substring(idx1+1,idx2);
				if (StringUtils.isNotEmpty(paras)){
					ret = js.substring(0,idx1+1);
					String[] split = paras.split(",");//js函数的括号内参数
					List<Map<String,Object>> listZiduan = getListZiduan(o);
					for (int i = 0; i < split.length; i++) {
						String para = split[i].trim();
						if ("this".equals(para) || para.startsWith("'") && para.endsWith("'")){
							ret += para+",";
						}else if (!para.startsWith("\\") &&onerec.get(para)!=null){
							ret += "'"+onerec.get(para)+"',";
						}else{
							ret += "'"+para+"',";
						}
					}
					ret =ret.substring(0,ret.length()-1)+")";
				}
			}
		}
		
		return StringUtils.isEmpty(ret)?js:ret;
	}
	
	//解析表体的操作区(这块的规则稍微有点复杂，配的时候估计会容易错)
	//0-3：text, idx, js, href
	//因为执行这个函数前已经预解析过了，结果放到了o.getR().getPreczq()里，所以这里就直接用
	private static void parseCzq(Crud o){
		if (o==null || o.getR()==null || CollectionUtils.isEmpty(o.getR().getTbs()) || CollectionUtils.isEmpty(o.getR().getPreczq())){
			return;
		}

		//遍历查询出来的记录，每条记录的操作可能都不一样
		for (Map<String, Object> onerec : o.getR().getTbs()) {//遍历list页面查出来的记录
			List<Map<String, Object>> lstczq = new ArrayList<Map<String, Object>>();//记录该条记录在操作区的所有信息，列表长度即为操作区按钮个数
			for (Map<String,Object> epre:o.getR().getPreczq()) {//遍历所有操作，判断当前记录可以增加几个操作按钮，几个什么样的操作
				Map<String,Object> eczq = new HashMap<String,Object>(epre);
				String cz_text = (String)eczq.get("text");
				if (eczq.size()<=0 || StringUtils.isEmpty(cz_text)) continue;//如果文字是""或null，则等于不需要这个操作按钮了

				eczq.put("czx","");
				String cz_js = (String)eczq.get("js");
				String cz_href = (String)eczq.get("href");
				if (cz_text.contains("case:")){//有条件选择，要根据这条记录的实际情况填充内容
					String[] split = cz_text.split("#");
					for (int j = 0; j < split.length; j++) {
						String[] splitmh = split[j].split(":");//case:sh=未审核:val:同意#case:sh=未审核:val:不同意
						String strcase= null;
						String strText = null;
						for (int k = 0; k < splitmh.length; k++) {
							if ("case".equals(splitmh[k])){
								if (k+1<splitmh.length) strcase = splitmh[k+1];
							}else if ("val".equals(splitmh[k]) || "gray".equals(splitmh[k]) || "grey".equals(splitmh[k])){
								if (k+1<splitmh.length) strText = splitmh[k]+":"+splitmh[k+1];
							}
						}
						if (StringUtils.isNotEmpty(strcase) && StringUtils.isNotEmpty(strText) && recMeetCase(onerec, strcase)) {
							if (StringUtils.isNotEmpty(cz_href)){//写了href
								zzczxhref(onerec, eczq, strText);
							}else if (StringUtils.isNotEmpty(cz_js)){//写了js，用onclick来响应
								zzczxjs(o, onerec, eczq, strText);
							}
						}//splitmh
					}
				}else{//没有条件选择，也就是除非操作项名称有冲突，否则该操作要么走href，要么走js
					String astr="<a ";
					String[] hrwzarr = cz_text.split("val:");//就是说可以写text:val:文字，也可以写text:文字
					String hrwz=hrwzarr[hrwzarr.length-1];
					if (StringUtils.isNotEmpty(cz_href)){//写了href
						String[] czqHref = getCzqHref(cz_href, onerec);
						if (czqHref!=null && StringUtils.isNotEmpty(czqHref[0])){
							astr+=" href='"+czqHref[0]+"' ";
							if (StringUtils.isNotEmpty(czqHref[1])) {
								astr+=" target='_blank' ";//此处可以搞一个_tab啊，类似于未读消息那样的
							}
							astr+=" title='"+hrwz+"' >";
							astr += hrwz+"</a>";
							eczq.put("czx", eczq.get("czx")+" "+astr);
							eczq.put("cztext", hrwz);//这个是在查重时要用到的，就是根据按钮文字要查重，后面覆盖前面的
						}
					}else if (StringUtils.isNotEmpty(cz_js)){//写了js，用onclick来响应
						String czqJs = getCzqJs(o, cz_js, onerec);
						if (StringUtils.isNotEmpty(czqJs)){
							astr+=" href='javascript:' title='"+hrwz+"' onclick=\""+czqJs+"\">";
							astr += hrwz+"</a>";
							eczq.put("czx", eczq.get("czx")+" "+astr);
							eczq.put("cztext", hrwz);
						}
					}
				}
				if (StringUtils.isNotEmpty((String)eczq.get("czx"))) lstczq.add(eczq);
			}//遍历每个操作项
			onerec.put("lstczq", sortOps(uniqueOps(lstczq)));//对lstczq查一下重复，后面覆盖前面的，最后排序
		}//遍历当前页数据
	}

	//解析一下extjs的字符串，如果是有效的js或者jsp，加入到结果中返回，类似于xxx.js?para=1&para2=2这种
	private static List<Map<String, Object>> parseExtjs(Object[] jsoarr){
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		if (jsoarr != null){
			for (int xx = 0;xx<jsoarr.length;xx++){
				String tpp = (String)jsoarr[xx];
				if (StringUtils.isEmpty(tpp)) continue;
					
				tpp = tpp.replaceAll(" ", "");
				if (tpp.endsWith(".js") || tpp.trim().endsWith(".jsp")){
					Map<String, Object> e = new HashMap<String, Object>();
					e.put("wjm", tpp);
					ret.add(e);
				}else{//看是不是js带参数的情况
					String[] split = tpp.split("[?]");
					if (split!=null && split.length==2 && (split[0].trim().endsWith(".js"))){
						Map<String, Object> e = new HashMap<String, Object>();
						e.put("wjm", split[0].trim());
						ret.add(e);
						String[] split2 = split[1].trim().split("&");
						if (split2!=null && split2.length >0){
							Map<String,String> mppas = new HashMap<String,String>();
							for (int i = 0; i < split2.length; i++) {
								String[] split3 = split2[i].split("=");
								if (split3!=null && split3.length==2){
									mppas.put(split3[0], split3[1]);
								}
							}
							e.put("para", mppas);
						}
					}
				}
			}
		}

		return ret;
	}
	
	//合并js参数，即类似xxx.js?para=1&para2=2这种
	private static String hbJsCanshu(String wjm, Map<String,String> oldd, Map<String,String> neww){
		final String fzhuancun = "___v";
		wjm += "?"+fzhuancun+"="+String.valueOf(Math.random());//这个是防止缓存
		if (oldd !=null){
			for (Map.Entry<String,  String> e : oldd.entrySet()) {
				if (fzhuancun.equals(e.getKey())) continue;
				wjm += "&"+e.getKey()+"="+e.getValue();
			}
		}
		if (neww !=null){
			for (Map.Entry<String,  String> e : neww.entrySet()) {
				if (fzhuancun.equals(e.getKey())) continue;
				wjm += "&"+e.getKey()+"="+e.getValue();
			}
		}

		return wjm;
	}

	//2018-05-04 青年节
	//将crud配置的或直接引入的js进行查重和添加随机参数的处理，增加默认参数是为了防止缓存
	//isList是指，来自list的push请求还是来自aev的push请求，要加入到不同的extjs的ArrayList里
	//返回值是指jsoarr中是否至少有一个有效的js或者jsp，即是否至少引入了一个js或者jsp文件
	//extjsp比extjs的优势在于可以直接引用'${xxx}'，extjs就不行，当然js不用后台编译速度上应该是优势了，就是写法不方便了，综合来说其实还是用extjsp方便
	private static void pushUniuqeExtjs(Crud o, boolean isList){
		if (o==null){
			return;
		}

		//再把对应的原有数组再解析一下
		List<Map<String, Object>> lstJb = new ArrayList<Map<String, Object>>();//jb：脚本
		List<String> lsOld= isList ? o.getR().getExtjs(): o.getAev().getExtjs();
		if (CollectionUtils.isNotEmpty(lsOld)) {
			lstJb = parseExtjs(lsOld.toArray());
		}

		//分别合并js和jsp两个结果
		Map<String, Object> weiyi = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(lstJb)){
			List<String> retJb = new ArrayList<String>();
			for (Map<String, Object> map : lstJb) {
				String wjm = (String)map.get("wjm");//文件名，必须唯一
				if (wjm.endsWith("js")){
					if (weiyi.get(wjm)!=null){//该文件名已经存在，合并一下参数
						for (String ts : retJb) {
							if (ts.contains(wjm)){
								retJb.remove(ts);
								break;
							}
						}
						wjm = hbJsCanshu(wjm, (Map<String,String>)weiyi.get("para"), (Map<String,String>)map.get("para"));
					}else{
						weiyi.put(wjm, map);//存储它，代表已经存在了
						wjm = hbJsCanshu(wjm, null, (Map<String,String>)map.get("para"));
					}
					retJb.add(wjm);
				}
			}
			if (isList){
				o.getR().setExtjs(retJb);
			}else{
				o.getAev().setExtjs(retJb);
			}
		}

		lsOld= isList ? o.getR().getExtjsp() : o.getAev().getExtjsp();
		List<Map<String, Object>> lstJsp =  new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(lsOld)) {
			lstJsp = parseExtjs(lsOld.toArray());
		}
		if (CollectionUtils.isNotEmpty(lstJsp)){
			List<String> retJsp = new ArrayList<String>();
			weiyi.clear();
			for (Map<String, Object> map : lstJsp) {
				String wjm = (String)map.get("wjm");//文件名，必须唯一
				if (wjm.endsWith("jsp")){
					if (weiyi.get(wjm)==null){//不存在则加入
						retJsp.add(wjm);
						weiyi.put(wjm, "1");//存储它，代表已经存在了
					}
				}
			}
			if (isList){
				o.getR().setExtjsp(retJsp);
			}else{
				o.getAev().setExtjsp(retJsp);
			}
		}
	}

	private static List<Map<String, Object>> uniqueOps(List<Map<String, Object>> lstczq){
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(lstczq)) {
			for (int i=0;i<lstczq.size();i++){
				for (int j=i+1;j<lstczq.size();j++){
					String c1 = (String)lstczq.get(i).get("cztext");
					String c2 = (String)lstczq.get(j).get("cztext");
					if (c1.equals(c2)){
						lstczq.get(i).put("delf", 1);
						break;
					}
				}
			}
			for (Map<String, Object> e:lstczq){
				if (e.get("delf")==null){
					ret.add(e);
				}
			}
		}

		return ret;
	}
	
	private static List<Map<String, Object>> sortOps(List<Map<String, Object>> lstczq){
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tail = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> stmp = new HashMap<Integer, Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(lstczq)) {
			for (Map<String, Object> e : lstczq) {
				Integer idx = (Integer)e.get("idx");
				if (idx!=null && idx>=0 && idx<lstczq.size()){
					stmp.put((Integer)e.get("idx"), e);
				}else{
					tail.add(e);
				}
			}
			for (int i=0;i<lstczq.size();i++){
				if (stmp.get(i)!=null) {
					ret.add(stmp.get(i));
				}else if (CollectionUtils.isNotEmpty(tail)){
					ret.add(tail.get(0));
					tail.remove(0);
				}
			}
		}

		return ret;
	}

	private static void zzczxhref(Map<String, Object> onerec, Map<String, Object> elst, String strText){
		if (onerec==null || elst==null || StringUtils.isEmpty(strText)){
			return;
		}

		String cz_href = (String)elst.get("href");
		String[] czqHref = getCzqHref(cz_href, onerec);
		if (czqHref!=null && StringUtils.isNotEmpty(czqHref[0])){
			String czx="";
			String[] split = strText.split(":");
			if (split.length>1){//val:删除
				czx+= "<a ";
				czx+=" href='"+czqHref[0]+"' ";
				if (StringUtils.isNotEmpty(czqHref[1])) {
					czx+=" target='_blank' ";
				}
				if ("gray".equalsIgnoreCase(split[0]) || "grey".equalsIgnoreCase(split[0])) czx+=" class='czxgray' title='点击查看按钮灰化原因' ";
				czx += " >"+split[1]+"</a>";
			}
			if (StringUtils.isNotEmpty(czx)) {
				elst.put("czx", elst.get("czx")+" "+czx);
				elst.put("cztext", split[1]);
			}
		}
	}

	//组装操作项js
	private static void zzczxjs(Crud o, Map<String, Object> onerec, Map<String, Object> elst, String strText){
		if (o==null || onerec==null || elst==null || StringUtils.isEmpty(strText)){
			return;
		}

		String[] cz_js = ((String)elst.get("js")).split(":");//fn_delrhsq(this,id,sh):listczq.js,xxx.js?pa=1 ,  yy.jsp
		if (cz_js!=null){
			String czqJs = getCzqJs(o, cz_js[0], onerec);
			if (StringUtils.isNotEmpty(czqJs)){
				String czx="";
				String[] split = strText.split(":");//eg. val:删除
				if (split.length>1){//val:删除
					czx+="<a ";
					if ("gray".equalsIgnoreCase(split[0]) || "grey".equalsIgnoreCase(split[0])) czx+=" class='czxgray' title='点击查看按钮灰化原因' ";
					czx+=" href='javascript:' onclick=\""+czqJs+"\" >"+split[1]+"</a>";
				}
				if (StringUtils.isNotEmpty(czx)) {
					elst.put("czx", elst.get("czx")+" "+czx);
					elst.put("cztext", split[1]);
				}
			}
		}
	}

	//要不要弄个解析czq字符串呢，有点麻烦的，而且操作区的配置不够简洁和灵活
	private static List<Map<String, Object>> partiCaseStr(String strcase){
		return null;
	}
	
	//判断某条记录是否符合case，符合的话就要增加该操作项
	//这个函数的功能略弱，而且配置操作这块我自己都觉得不甚合理，写的很麻烦
	private static boolean recMeetCase(Map<String, Object> onerec, String strcase){
		if (onerec==null || StringUtils.isEmpty(strcase)){
			return false;
		}

		//先把case字符partiCaseStr串拆解成N个判断块，单个块就不包含外围的大括号了
		//List<Map<String, Object>> partiCaseStr = partiCaseStr(strcase);
		
		String[] opparr = {">=", "<=","==","!=","=",">","<"};
		for (int i = 0; i < opparr.length; i++) {
			int iop = strcase.indexOf(opparr[i]);
			if (iop != -1){
				String zdm = strcase.substring(0, iop);
				String val = strcase.substring(iop+opparr[i].length(), strcase.length());
				if (StringUtils.isNotEmpty(zdm) && StringUtils.isNotEmpty(val)) {
					Object zdobj = onerec.get(zdm);
					if (zdobj ==null) return false;
					double dbvzd = 0;
					if ( zdobj instanceof Integer){//字段值是数值类型
						dbvzd = (Integer)zdobj;
					}else if (zdobj instanceof Long){
						dbvzd = (Long)zdobj;
					}else if (zdobj instanceof Double){
						dbvzd = (Double)zdobj;
					}else if (zdobj instanceof Float){
						dbvzd = (Float)zdobj;
					}else if (zdobj instanceof String){
						if (Pattern.compile("^(\\d*\\.)?\\d+$").matcher(zdobj.toString()).matches()){
							dbvzd = Double.valueOf(zdobj.toString());
						}else{
							return (("==".equals(opparr[i]) || "=".equals(opparr[i])) && val.equalsIgnoreCase(String.valueOf(zdobj.toString()))
								|| ("!=".equals(opparr[i]) && !val.equalsIgnoreCase(String.valueOf(zdobj.toString()))));
						}
					}else{
						return false;
					}
					
					if (Pattern.compile("^(\\d*\\.)?\\d+$").matcher(val).matches()){
						double n1 = dbvzd;
						double n2 = Double.valueOf(val).doubleValue();
						if (">=".equals(opparr[i])) return n1 >= n2;
						else if ("<=".equals(opparr[i])) return  n1 <= n2;
						else if (">".equals(opparr[i])) return   n1 > n2;
						else if ("<".equals(opparr[i])) return  n1 < n2;
						else if ("==".equals(opparr[i]) || "=".equals(opparr[i])) return  n1 == n2;
						else if ("!=".equals(opparr[i])) return  n1 != n2;
					} else {
						return ((("==".equals(opparr[i]) || "=".equals(opparr[i])) && val.equalsIgnoreCase(String.valueOf(dbvzd)))
									|| ("!=".equals(opparr[i])  && !val.equalsIgnoreCase(String.valueOf(dbvzd))));
					}
				}
				break;
			}
			
		}
		
		return false;
	}
	
	//解析按钮区的内容
	private static void parseAnq(Crud o, String[][] v){
		if (o==null){
			return;
		}
		
		//这里根据配置加上新增、批量删，和自定义的使用同样的规则，如果某条记录计算结束有重复名称，后面的覆盖前面的
		//新增和批量删这两个是带图标的
		String[][] arrh = new String[3][];
		int count=0;
		if (o.getAev().isNeeda()) {
			arrh[count++] = new String[]{"新增",StringUtils.isNotEmpty(o.getR().getTree().getTb())?"js:___fn_add()":"js:location.href='aev.dhtml?t=0&u="+o.getU()+"'", "clazz:btn_add"};
		}
		if (o.getD().isBatdel()) {
			arrh[count++] = new String[]{"批量删", "js:___fn_batdel()", "clazz:btn_recycle"};
		}
		int cntWx = arrh.length-count;
		for (int i = 0; i < cntWx; i++) {
			arrh = (String[][]) ArrayUtils.remove(arrh, arrh.length-1);
		}
		v = (String[][])ArrayUtils.addAll(arrh, v);
		if (v==null || v.length<=0) return;
		
		List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
		for (int i=0; i< v.length;i++){
			Map<String, Object> elst= new HashMap<String, Object>();//存储的是该操作项的相关属性
			if (v[i]==null || StringUtils.isEmpty(v[i][0])) continue;
			elst.put("cztext", v[i][0]);//行内第一个默认是操作名称
			elst.put("clazz","btn_cmn");
			for (int j = 1; j <= 5; j++) {//后5个字段位置可以不固定，js、style、idx,id,clazz
				if (v[i].length>=j+1 && StringUtils.isNotEmpty(v[i][j])){
					String trim = v[i][j].trim();
					if (Pattern.compile("^\\s*js\\s*:", Pattern.CASE_INSENSITIVE).matcher(trim).find()){//类似js:fn_op_yidu(0):listanq.jsp,xxx.js , yyy.jsp, zzz.js
						String[] split = trim.split(":");
						for (int k=0;k<split.length;k++){
							String astp = split[k].trim();
							String[] jsarr = astp.split("\\s*,");
							List<Map<String, Object>> parseRet = parseExtjs(jsarr);
							if (CollectionUtils.isNotEmpty(parseRet)){
								for (int yy = 0; yy < jsarr.length; yy++) {
									o.getR().getExtjs().add(jsarr[yy]);//先加进去，后面会进行查重和修正
									o.getR().getExtjsp().add(jsarr[yy]);
								}
							}else{
								elst.put("fn", astp);
							}
						}
					}else if (Pattern.compile("^\\s*style\\s*:", Pattern.CASE_INSENSITIVE).matcher(trim).find()){
						elst.put("style", trim.substring(trim.indexOf(":")+1, trim.length()));
					}else if (Pattern.compile("^\\s*idx\\s*:", Pattern.CASE_INSENSITIVE).matcher(trim).find()){
						String[] split = trim.split(":");
						if (split.length>=2){
							Integer vidx = -1;
							try{vidx  =Integer.valueOf(split[1].trim());}catch(Exception exc){vidx = -1;}
							if (vidx !=-1) elst.put("idx", vidx);
						}
					}else if (Pattern.compile("^\\s*id\\s*:", Pattern.CASE_INSENSITIVE).matcher(trim).find()){//给按钮加一个id
						String[] split = trim.split(":");
						if (split.length>=2){
							elst.put("id", split[1].trim());
						}
					}else if (Pattern.compile("^\\s*clazz\\s*:", Pattern.CASE_INSENSITIVE).matcher(trim).find()){
						String[] split = trim.split(":");
						if (split.length>=2){
							elst.put("clazz", elst.get("clazz")+" "+split[1].trim());
						}
					}
				}
			}
			if (elst.size()>=2){
				if ("新增".equals( v[i][0]) && elst.get("clazz")!=null && !elst.get("clazz").toString().contains("btn_add")) elst.put("clazz", elst.get("clazz")+" btn_add ");
				if ("批量删".equals( v[i][0]) && elst.get("clazz")!=null && !elst.get("clazz").toString().contains("btn_recycle")) elst.put("clazz", elst.get("clazz")+" btn_recycle ");
				lst.add(elst);
			}
		}
		
		o.getR().setAns(sortOps(uniqueOps(lst)));//先做唯一，再排序
	}
	
	public static void setList(HttpServletRequest request, Crud o, String[][] hds) throws Exception{//只有表，没有查询区、操作区和按钮区
		setList(request, o,hds,null, null, null);
	}
	public static void setList(HttpServletRequest request, Crud o, String[][] hds, String[][] cxs) throws Exception{//只有表和查询区，没有操作区和按钮区
		setList(request, o,hds,cxs, null, null);
	}
	public static void setList(HttpServletRequest request, Crud o, String[][] hds, String[][] cxs, String[][] czs) throws Exception{//有表、查询区和操作区，没有按钮区
		setList(request, o,hds,cxs, czs, null);
	}

	//解析aev的字符串数组，title和zdm
	//还是和list一样，前两个设置是标题和字段名
	//2-6，有4个可以配置，次序不限，分别是校验（前台），字段类型，js的一些事件处理，初始化参数，placeholder
	public static void setAev(HttpServletRequest request, Crud o, String[][] v) throws Exception{
		//添加了1=1 -- 2019-03-07 修改
		if (o==null || v==null || v.length<=0||o.getMp()==null || StringUtils.isEmpty(o.getTb())){
			return;
		}

		//把数据库中定义的字段长度取出来
		List<Map<String,Object>> listZiduan = getListZiduan(o);
		if (CollectionUtils.isEmpty(listZiduan)){
			throw new Exception("请先为【"+o.getTb()+"】设置表字段！");
		}

		List<Map<String,Object>> zds = new ArrayList<Map<String,Object>>();//存储解析出来的字段信息
		if ("父栏目nr".equals(o.getClx())){
			Map<String,Object> e = new HashMap<String,Object>();
			e.put("title", "栏目");
			e.put("zdm", "cid");
			e.put("type", Aevzd.SELECT.toString());
			e.put("required", true);
			List<Map<String, Object>> cxs = o.getR().getCxs();
			if (cxs.size()>0)e.put("selops", cxs.get(cxs.size()-1).get("selops"));
			zds.add(e);
		}

		boolean aevarrHasHdnCid = false;//记录数组里是否有cid这个hidden类型
		String hdnpara = "";
		for (int i=0;i<v.length;i++) {
			if ("cid".equals(v[i][0])) aevarrHasHdnCid=true;
			if (Aevzd.HIDDEN.toString().equals(v[i][0]) || Aevzd.HIDDEN.toString().equals(v[i][1])){//是hidden类型
				if (v[i].length>=3 && StringUtils.isNotEmpty(v[i][2])) {
					if ("父栏目nr".equals(o.getClx()) && "cid".equals(v[i][0])){
						//这种情况是不需要加这个hidden域的
					}else{
						hdnpara+="<input type='hidden' name='"+(Aevzd.HIDDEN.toString().equals(v[i][0])?v[i][1]:v[i][0])+"' value='"+v[i][2]+"'>"+"</input>";
					}
				}
			}else if (v[i].length>=3 && (Aevzd.TEXT.toString().equals(v[i][1]) || Aevzd.TEXT.toString().equals(v[i][2]))){//是纯文本，可以在位置1,2
				String tytext = Aevzd.TEXT.toString();
				Map<String,Object> e = new HashMap<String,Object>();
				e.put("type", tytext);
				e.put("title", v[i][0]);//中文title必须是第一个
				String col3 = tytext.equals(v[i][1])?v[i][2]:v[i][1];
				if (zdInDbcol(col3, listZiduan)){
					e.put("zdm", col3);
				}else{
					e.put("val", col3);
				}
				zds.add(e);
			}else if ("px".equals(v[i][1])){//px类型默认必填正整数
				if (zdInDbcol("px", listZiduan)){
					Map<String,Object> e = new HashMap<String,Object>();
					e.put("title", v[i][0]);
					e.put("zdm", v[i][1].toLowerCase());
					e.put("val", vpx(o.getMp(), o.getTb()));
					e.put("type", Aevzd.INPUT.toString());
					if (StringUtils.isEmpty((String)e.get("validate"))){//排序默认是必填、正整数
						e.put("required", true);
						e.put("validate", "required:::排序的值不能为空 plusinteger:::排序请输入正整数");
					}
					putMaxLen(e, listZiduan);
					zds.add(e);
				}
			}else{
				Map<String,Object> e = new HashMap<String,Object>();
				e.put("title", v[i][0]);
				e.put("zdm", v[i][1].toLowerCase());
				e.put("required", false);
				e.put("validate", "");
				//先把字段类型查出来
				for (int j = 2; j <= 6; j++) {
					if (v[i].length>=j+1 && StringUtils.isNotEmpty(v[i][j]) && isAevZdType(v[i][j])){
						e.put("type", v[i][j].toUpperCase());
					}
				}
				String aevzdlx = (String)e.get("type");
				if (e.get("type")==null){
					e.put("type", Aevzd.INPUT.toString());//默认是INPUT
				}

				for (int j = 2; j <= 6; j++) {//后5个字段可以随意排，这里根据实际的内容来判断是什么类型的，增加了配置aev的灵活性
					if (v[i].length>=j+1 && StringUtils.isNotEmpty(v[i][j])){
						if (isAevZdType(v[i][j])) continue;
						
						if (Pattern.compile("^\\s*zdb\\s*:", Pattern.CASE_INSENSITIVE).matcher(v[i][j]).find()){//zDb前缀有无都可以，大小写也不分
							if (aevzdlx.equals(Aevzd.SELECT.toString())){//
								e.put("selops", fnsel("zdb", o.getMp(), v[i][j].split(":")[1].trim(), ""));
							}else if (aevzdlx.equals(Aevzd.CHECKBOX.toString()) || aevzdlx.equals(Aevzd.RADIO.toString())){//这是写了zdb前缀的情况，也可以不写，循环结束后处理不写的情况
								String py = v[i][j].split(":")[1].trim();
								Map<String, Object> obj = o.getMp().obj("select GROUP_CONCAT(s.zdxmc) val from tjpcms_zdb t left join tjpcms_zdx s on t.id = s.pId where (t.py = '"+py
										+"') order by s.px asc ,s.gx desc");
								if (obj !=null){
									e.put("val",obj.get("val"));
								}
							}
						}else if (isValidateStr(v[i][0], v[i][j], e)){
							
						}else if (isExtjsStr(o, v[i][j], e)){
							
						}else if (Pattern.compile("^\\s*phd\\s*:", Pattern.CASE_INSENSITIVE).matcher(v[i][j]).find()){//placeholder
							e.put("phd", v[i][j].split(":")[1].trim());
						}else{
							if (v[i][j].startsWith("\\")){//初始化参数
								v[i][j]  =v[i][j].substring(1,v[i][j].length());
							}
							e.put("val",v[i][j]);
						}
					}
				}
				
				String zdtype = (String)e.get("type");
				if (Aevzd.SELECT.toString().equals(zdtype)){//是SELECT类型，如果还没查出selops，则执行查找
					if (e.get("selops")==null){
						String selval =  (String)e.get("val");
						if (StringUtils.isNotEmpty(selval)) {
							String[] selslt = selval.split(":");
							if (selslt!=null && (selslt.length==1||selslt.length==2)){
								if (selslt.length==1){
									e.put("selops", fnsel("zdb", o.getMp(), (String)e.get("val"), ""));
								}else{
									selslt = selval.split(":");
									if (selslt!=null && selslt.length>=2 && selslt[0].trim().equalsIgnoreCase("sql") && StringUtils.isNotEmpty(selslt[1].trim())){
										e.put("selops", fnsel("sel", o.getMp(), selval, ""));
									}
								}
							}
						}
					}
				}else if (Aevzd.TREE.toString().equals(zdtype)){//是单选树
					String selval =  (String)e.get("val");
					if (StringUtils.isNotEmpty(selval)) {
						String[] selslt = selval.split(":");
						if (selslt!=null && (selslt.length==1||selslt.length==2)){
							e.put("treelist",  JSONArray.fromObject(o.getMp().r(selslt[selslt.length-1])));
						}
					}
					if (e.get("phd")==null)e.put("phd", "请选择树节点");
				}else if (Aevzd.RADIO.toString().equals(zdtype) || Aevzd.CHECKBOX.toString().equals(zdtype)){
					if (e.get("val")!=null){
						String py = ((String)e.get("val")).trim();
						Map<String, Object> obj = o.getMp().obj("select GROUP_CONCAT(s.zdxmc) val from tjpcms_zdb t left join tjpcms_zdx s on t.id = s.pId where (t.py = '"+py
								+"') order by s.px asc ,s.gx desc");
						if (obj !=null){
							e.put("val",obj.get("val"));
						}
					}
				}else if (Aevzd.ZDB.toString().equalsIgnoreCase((String)e.get("type"))){//这个不是用某个zdb的内容做出select，而是把字典表中的所有字典表作为select显示出来
					e.put("zdbops",o.getMp().r("select id val,mc txt from tjpcms_zdb"));
					e.put("zdbHaveZdy", "zdy".equals((String)e.get("val")));
				}
				putMaxLen(e,listZiduan);
				zds.add(e);
			}
		}

		if (StringUtils.isNotEmpty(o.getCid()) && !"父栏目nr".equals(o.getClx()) && !aevarrHasHdnCid){//只有一个子栏目，就不需要选栏目了，直接设置成隐藏域cid
			hdnpara+="<input type='hidden' name='cid' value='"+o.getCid()+"'>"+"</input>";
		}
		o.getAev().setHdnpara(hdnpara);
		o.getAev().setZds(zds);

		String ses_ht_tip = (String)request.getSession(false).getAttribute("ses_ht_tip");
		if (StringUtils.isNotEmpty(ses_ht_tip)){
			request.setAttribute("ses_ht_tip", ses_ht_tip);
			request.getSession(false).removeAttribute("ses_ht_tip");
		}else if (!o.getR().isNeed() && !o.getAev().isNoBtns()){//只有编辑页
			request.setAttribute("ses_ht_tip", "点击【编辑】按钮后，方可编辑内容");
		}

		//计算canZc（是否需要暂存)，只有满足 【表中有shzt字段，且审核流程不是无审核不是自定义也不是空】的条件 才需要暂存（这个是后台暂存，当然前台暂存我也没搞就是了）
		//还是把自定义加进去吧，不然我还得在aev页面自己搞提交和暂存按钮，也太难搞了
		if (StringUtils.isNotEmpty(o.getCid()) && zdInDbcol("shzt", listZiduan)){
			List<Map<String, Object>> r = o.getMp().r("select s.* from tjpcms_lanmu t left join tjpcms_liucheng s on s.id=t.lcid where t.id="+o.getCid()+"");
			if (CollectionUtils.isNotEmpty(r) && r.size()==1 && r.get(0)!=null && r.get(0).get("mc")!=null && StringUtils.isNotEmpty(r.get(0).get("mc").toString())){
				String mc = r.get(0).get("mc").toString();
				if (!"无审核".equals(mc) && r.get(0).get("jsids")!=null && StringUtils.isNotEmpty(r.get(0).get("jsids").toString()) || "自定义".equals(mc)){
					o.getAev().setCanZc(true);
				}
			}
		}
		
		//extjs和extjsp去重和修复，放在最后，防止前面的代码里又有修改extjs的地方
		pushUniuqeExtjs(o, false);
	}
	
	//之所以把这个封装起来，也是为了局部刷新时能直接调用
	//全部刷新时，o.getRq就是本次请求的request，局部刷新时，必须用ajax请求的request不能再用o.getRq了
	//这边比较麻烦的就是现在不用t.*了，导致必须精确一下具体是查找哪些字段，字段的组成有
	//1. extselect ，就是issql=1的内嵌字段
	//2. Extzdm，主要就是hds和aev里都没配但是又需要用到的表字段，如wdxx里的yd字段
	//3. Extjoinzdm，这个是join后的其他表字段
	//4. hds和aev的zds
	//5. cid,gx,rq,delf,px,id这几个系统用字段
	//2018-08-10，还是改成，如果是后台刷新使用t.*，如果是前台刷新具体到每个字段
	private static void createSql(Crud o, String cxfilter, String extselect, HttpServletRequest request) throws Exception{
		Retrieve r = o.getR();
		
		String s = " from(select "+extselect+" ";//这个是内部查询字段比如select (....内部查询...) xx, zd1,zd2 from tb
		if (CollectionUtils.isNotEmpty(r.getExtzdm())) {//增加extzd，即作为查询结果但不显示在表头区的字段，这个的例子比如用户管理中的juti字段
			List<String> extzdm = r.getExtzdm();
			for (int iZd = 0;iZd<extzdm.size();iZd++) {
				String zdstr = "";
				if (iZd<r.getExtzdstr().size()) zdstr = r.getExtzdstr().get(iZd);//比如我只想加一个字段，那我就可以只填充Extzdm
				s+=" "+zdstr+" "+extzdm.get(iZd)+" , ";
			}
		}
		if (CollectionUtils.isNotEmpty(r.getExtjoinzdm())) for (String eexjstr: r.getExtjoinzdm()) s += eexjstr+",";
		
		//2018-08-10，还是改成，如果是后台刷新使用t.*，如果是前台刷新具体到每个字段
		List<Map<String, Object>> listZiduan = getListZiduan(o);
		if (!r.isJubu()){
			s+="t.*,";
		}else{
			//这里因为有了局部刷新，所以其实不好再直接使用t.*了，因为这样会把sql查出来的所有数据暴露到前台
			//原先是只有全部刷新时，因为是后台拼出来的html，所以无所谓了，用t.*会比较方便，但是既然现在全局和局部大家公用这个函数，必须要精确到到底需要哪些字段，只把需要的字段查出来
			boolean hascid = false,hasgx = false,hasrq = false,hasdelf = false, haspx = false, hasId = false;
			List<Map<String, Object>> zds = new ArrayList<Map<String, Object>>(o.getR().getZds());
			zds.addAll(o.getAev().getZds());
			Map<String, Object> weiyi = new HashMap<String, Object>();
			for (Map<String,Object> e:zds) {
				Object zdm = e.get("zdm");
				if (zdm ==null || StringUtils.isEmpty(zdm.toString())) continue;
				if (weiyi.get(zdm.toString())!=null) continue;
				else weiyi.put(zdm.toString(), 1);
				if (e.get("issql")==null && zdInDbcol(zdm.toString(), listZiduan)){
					if (zdm.toString().equalsIgnoreCase("rq")) s += " date_format(t.rq, '%Y-%m-%d %H:%i:%S')rq, ";
					else s += " t."+zdm.toString()+", ";
				}
				
				Object zdextc = e.get("extc");//hds[]中ext字段也要查出来
				if (zdextc!=null && StringUtils.isNotEmpty(zdextc.toString()) && weiyi.get(zdextc.toString())==null){
					weiyi.put(zdextc.toString(), 1);
					if (zdInDbcol(zdextc.toString(), listZiduan)) s += " t."+zdextc.toString()+", ";
				}
				
				if (!hascid && "cid".equalsIgnoreCase(zdm.toString())) hascid = true;
				if (!hasgx && "gx".equalsIgnoreCase(zdm.toString())) hasgx = true;
				if (!hasrq && "rq".equalsIgnoreCase(zdm.toString())) hasrq = true;
				if (!hasdelf && "delf".equalsIgnoreCase(zdm.toString())) hasdelf = true;
				if (!haspx && "px".equalsIgnoreCase(zdm.toString())) haspx = true;
				if (!hasId && "id".equalsIgnoreCase(zdm.toString())) hasId = true;
			}

			//再判断一下系统要使用的几个默认字段在不在
			if (!hascid && zdInDbcol("cid", listZiduan)) s += " t.cid, ";
			if (!hasgx && zdInDbcol("gx", listZiduan)) s += " t.gx, ";
			if (!hasrq && zdInDbcol("rq", listZiduan)) s += " date_format(t.rq, '%Y-%m-%d %H:%i:%S')rq, ";
			if (!hasdelf && zdInDbcol("delf", listZiduan)) s += " t.delf, ";
			if (!haspx && zdInDbcol("px", listZiduan)) s += " t.px, ";
			if (!hasId && zdInDbcol("id", listZiduan)) s += " t.id, ";
		}
		
		s += "  1 from "+o.getTb()+" t ";//1是防止前面都没有字段，作用类似where 1=1  (and...and...)，有了1=1and就可以随意加了
		if (StringUtils.isNotEmpty(r.getExtjoinstr())) s += r.getExtjoinstr();
		s += ")tt where 1=1 ";
		
		
		//===================================================
		//根据权限过滤，这个要重写的，并且还要加上hook，允许自定义过滤
		//如果表中有uid这个字段，并且是普通用户，只能看自己的数据
		//这里的查看数据，还根据流程和角色来，当然超管是都能看（测试非测试都能看），但是非超管的话，数据是3个来源
		//1、自己添加的本人数据，uid字段记录这条记录是谁产生的
		//2、下属角色的数据，下属角色要根据uid去usr表里找到jsid，判断是否在下属角色列表里
		//3、审核流程到了自己这里的数据，既然有审核，那就必须要知道是谁把数据传递过来的，那还是要有uid
		boolean uidInDbcol = zdInDbcol("uid", listZiduan);
		if (HT.isUsrpt(request) && uidInDbcol){//所以uid在这里判断了
			String uid = HT.getUid(request);
			String usrptJsid = HT.getJsid(request);
			
			//本人数据
			s +=" and (uid='"+uid+"' ";

			//下属角色的数据
			boolean shztIndb = zdInDbcol("shzt", listZiduan);
			List<String>zisun = getZisunNodes(usrptJsid, o.getMp().r("select id,pId from tjpcms_juese"));
			if (zisun!=null && zisun.size()>0){
				s += " or (exists (select * from tjpcms_usr ___x where ___x.id =tt.uid and ___x.jsid in "+list2instr(zisun)+") "+(shztIndb?"and shzt!='未提交'":"")+") ";
			}
			
			//审核流程已经到了我这里的，需要我审核的，shzt必然是【待审核】
			if (shztIndb && zdInDbcol("shjd", listZiduan) && "com.tjpcms.common.Hook.___cmnsh_aftcx".equals(o.getR().getHook_aftcx())) {
				//s += " or (shzt='待审核' and substring_index(substring_index(___lcjsids, '→', shjd), '→', -1) = '"+usrptJsid+"') ";//因为这个语句已经是在最外层，所以不能用y.jsids。内层语句可以用
				s+=" or sfdwsh='1' ";
			}
			
			s += " ) ";
		}
		//===================================================
		
		
		if (StringUtils.isNotEmpty(r.getExwhere())) s += " "+r.getExwhere()+" ";
		s += " "+cxfilter+" ";
		r.setSql_c("select count(*) "+s);
		r.setSql_r("select * "+s);

		Integer curPage = 1;//默认就是第一页
		r.setPgTotal(0);
		r.setCurPage(curPage);
		r.setRecTotal(o.getMp().cnt(r.getSql_c()));
		System.out.println("========================================"+r.getSql_c());
		try{curPage = Integer.valueOf(request.getParameter("___pg"));}catch(Exception e){curPage = 1;}//异常还是到第一页
		if (r.getRecTotal()>0){
			r.setSql_p(r.getSql_r()+orderbyAndLimit(o, curPage));
			r.setTbs(o.getMp().r(r.getSql_p()));//表体区，下面的parseCzq要用到
			if (StringUtils.isNotEmpty(r.getHook_aftcx())) {
				r.setTbs((List<Map<String, Object>>)execHook(r.getHook_aftcx(), request, o, null, null));
			}
			parseCzq(o);//操作区，就是列表区的最后一列。对每一条记录专门计算出其应该有的菜单
		}else{//如果没有查出来数据也要经过hook，可能会做一些不涉及数据的操作的
			r.setTbs(new ArrayList<Map<String, Object>>());
			if (StringUtils.isNotEmpty(r.getHook_aftcx())) {
				r.setTbs((List<Map<String, Object>>)execHook(r.getHook_aftcx(), request, o, null, null));
			}
		}
	}
	
	//广度遍历子孙节点。因为我想返回一个结果集，用递归好像不行吧。递归就是把结果集作为参数传递，其实差不多了，不同的写法
	public static List<String> getZisunNodes(String fuid, List<Map<String,Object>> r){
		List<String> ret = new ArrayList<String>();
		
		if (StringUtils.isNotEmpty(fuid) && CollectionUtils.isNotEmpty(r)) {
			Queue<String> q = new LinkedList<String>();
			q.add(fuid);
			while (!q.isEmpty()){
				String head = q.remove();
				for (Map<String,Object> map : r) {
					Object pId = map.get("pId");
					if (pId!=null && head.equals(pId.toString()) && map.get("id")!=null){
						String np = map.get("id").toString();
						ret.add(np);
						q.add(np);
					}
				}
			}
		}
		
		return ret;
	}
	
	//预解析操作区czq
	//0-3：text, idx, js, href
	private static void preParseCzq(Crud o, String[][] v){
		//这里根据配置加上查看、编辑、删除，和自定义的使用同样的规则，如果某条记录计算结束有重复名称，后面的覆盖前面的
		String[][] arrh = new String[3][];
		int count=0;
		if (o.getAev().isNeedv()) {
			arrh[count++] = new String[]{"查看",StringUtils.isEmpty(o.getR().getTree().getTb())?"href:aev.dhtml?id=&t=2&u="+o.getU():"js:___fn_view(id)"};
		}
		if (o.getAev().isNeede()) {
			arrh[count++] = new String[]{"text:编辑","href:aev.dhtml?t=1&u="+o.getU()+"&id="};
		}
		if (o.getD().isNeed()) {
			arrh[count++] = new String[]{"text:删除","js:___fn_del(this,id)"};
		}
		int cntWx = arrh.length-count;
		for (int i = 0; i < cntWx; i++) {
			arrh= (String[][]) ArrayUtils.remove(arrh, arrh.length-1);//空的要去掉，不然合并后还有空的
		}
		v = (String[][])ArrayUtils.addAll(arrh, v);
		
		
		//对于有审核的，需要加上审核按钮
		if ("com.tjpcms.common.Hook.___cmnsh_aftcx".equals(o.getR().getHook_aftcx())){
			String shzrr[][]={{"text:case:sfdwsh=0:hidden#case:sfdwsh=1:val:审核","js:fn_cmn_sh(this,id,shzt):listczq.js","idx:1"}};
			v = (String[][])ArrayUtils.addAll(shzrr, v);
		}

		
		if (v==null || v.length<=0) return;

		//遍历查询出来的记录，每条记录的操作可能都不一样
		for (int i=0;i<v.length;i++) {//遍历所有操作项
			if (v[i]==null) continue;
			Map<String, Object> elst= new HashMap<String, Object>();//存储的是该操作项的相关属性
			for (int j = 0; j <= 3; j++) {//字段位置可以不固定
				if (v[i].length>=j+1 && StringUtils.isNotEmpty(v[i][j])){
					String vij = v[i][j].trim();
					int idxMh = vij.indexOf(":");
					if (idxMh!=-1){
						String prefix = vij.substring(0,idxMh).trim();
						String cont = vij.substring(idxMh+1,vij.length()).trim();
						if ("idx".equals(prefix)){
							Integer idx = -1;
							try{idx  =Integer.valueOf(cont);}catch(Exception exc){idx = -1;}
							if (idx !=-1) elst.put("idx", idx);
						}else if (StringUtils.isNotEmpty(cont) && StringUtils.isNotEmpty(prefix)){
							elst.put(prefix, cont);
						}
					}else if (StringUtils.isNotEmpty(vij)){//如果没有冒号，默认是text
						elst.put("text", vij);
					}
				}
			}
			o.getR().getPreczq().add(elst);

			//如果有js的话要塞到r.extjs里
			if (elst.size() > 0 && elst.get("js") != null) {
				String[] split = ((String)elst.get("js")).split(":");//可以是js:fn_cmn_del():listczq.js,11.js,22.js这种，就是同时引入多个js
				if (split!=null && split.length>=2){
					String[] splt = split[1].split(",");//这么写就必须保证是先写fn_...(...)再写....js，我认为你一定是写在split[1]
					if (splt != null){
						for (int yy = 0; yy < splt.length; yy++) {
							o.getR().getExtjs().add(splt[yy]);
							o.getR().getExtjsp().add(splt[yy]);
						}
					}
				}
			}
		}//for (int i=0;i<v.length;i++)
	}
	
	//list页面表头和表体的内容
	//2018-05-04：之前是按钮区和操作区可能会引入额外的js，还有就是用户调o.getR().getExtjs().add直接加，这个有两处修改
	//					一是额外的js可以带参数了(这样js里就可以读出来，也就可以区分是谁引入了这个js了，例子见用户管理)
	//					二是如果用户自行通过o.getR().getExtjs().add来引入额外的js，需要在这个函数里去除掉重复的，并且要加上math.random参数（防止js文件缓存）
	//就是需要把整页刷新和局部刷新公共的部分剥离出来，可以单独调用，共享
	public static void setList(HttpServletRequest request, Crud o, String[][] hds, String[][] cxs, String[][] czs, String[][] ans) throws Exception {
		//调用了该函数就设置需要列表页为true
		o.getR().setNeed(true);


		//检查参数
		if (o==null || hds==null || hds.length<=0||o.getMp()==null || StringUtils.isEmpty(o.getTb())){
			return;
		}

		
		//设置提交表单和aev页面返回的列表页url
		//其实还有个什么问题呢，就是跳到其他页面后再返回，是不是需要保存住跳转前的查询条件，但这个目前不是特别重要了，要实现也不是很难，但是有这样的需求吗，没有我现在好像用的也挺好
		Retrieve r = o.getR();
		String wzurl = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/')+1,request.getRequestURI().length());//完整url
		r.setListloc(wzurl+(StringUtils.isNotEmpty(request.getQueryString())?("?"+request.getQueryString()):""));//list location
		

		//解析查询区，这里就要重新考虑是否局部刷新的情况，如果是局部刷新，则查询区要显示出来，但功能需要等到数据库加载完成后再响应
		 parseCxq(request, o, cxs);

		 
		 //解析表头区
		parseBtq(request, o, hds, czs);
		 
		
		//解析按钮区
		parseAnq(o, ans);
		
		
		//预解析操作区
		//这个略有点坑，因为解析操作区的话，必须数据已经查出来了，但是局部刷新的情况的话，数据还没查出来，而pushUniuqeExtjs里又必须要用到czq中设置的extjs和extjsp，关键是jsp，无法由ajax动态加载
		//所以这里只好先满足pushUniuqeExtjs
		preParseCzq(o, czs);
		

		//根据解析的结果把sql拼接出来。如果是局部刷新的，则这个函数不用执行，等待ajax请求时再调用该函数拼sql
		if (!r.isJubu()){
			createSql(o, cxqGetcxfilter(o, r.getCxs(), r.getDefs(), request), r.getExtselect(), request);
		}

		
		//后台列表页初始化完成后的提示，为了应对比如aev页面保存成功后在列表页要有提示，这种情况
		String ses_ht_tip = (String)request.getSession(false).getAttribute("ses_ht_tip");
		if (StringUtils.isNotEmpty(ses_ht_tip)) {
			request.setAttribute("ses_ht_tip", ses_ht_tip);
			request.getSession(false).removeAttribute("ses_ht_tip");
		}

		
		//保证extjs和extjsp不会重复，并且会为extjs加上random参数防止缓存，放在最后执行一下就行了
		pushUniuqeExtjs(o, true);
	}

	private static  String orderbyAndLimit(Crud o, Integer curPage) throws Exception{
		String mowei = ""; 
		if (o!=null){
			o.getR().setPgTotal((int)Math.ceil(o.getR().getRecTotal()/(double)o.getR().getPerPage()));
			if (!(curPage>=1 && curPage<=o.getR().getPgTotal())) {curPage=1;}//
			o.getR().setCurPage(curPage);
			if (StringUtils.isNotEmpty(o.getR().getOdrby())) mowei += " "+o.getR().getOdrby()+", ";
			List<Map<String, Object>> listZiduan = getListZiduan(o);
			if (zdInDbcol("px", listZiduan)) mowei+=" tt.px desc, ";
			if (zdInDbcol("gx", listZiduan)) mowei+=" tt.gx desc, ";
			if (zdInDbcol("rq", listZiduan)) mowei+=" tt.rq desc, ";
			if (zdInDbcol("id", listZiduan)) mowei+=" tt.id desc, ";
			if (StringUtils.isNotEmpty(mowei)) {
				mowei = " order by "+mowei.trim().substring(0, mowei.trim().length()-1)+" ";
			}
			
			mowei += " limit "+(curPage-1)*o.getR().getPerPage()+","+o.getR().getPerPage();
		}
		
		return mowei;
	}
	
	//都通过这个函数来取表字段，减少数据库操作，但那个ecache是怎么用来着的
	public static List<Map<String,Object>> getListZiduan(Crud o){
/*		List<Map<String,Object>> listZiduan = null;
		if (CollectionUtils.isEmpty(o.getLsttbZd())){//没查过就去库里查一下再存起来
			listZiduan = o.getMp().getTblZiduan(o.getTb(), CL.DB);
			o.setLsttbZd(new ArrayList<Map<String,Object>>(listZiduan));//复制一份，因为下面的代码会删listZiduan
		}else{
			listZiduan = new ArrayList<Map<String,Object>>(o.getLsttbZd());
		}
		
		return listZiduan;*/

		//我觉得还不是能用缓存，因为两次操作之间是有可能改变表结构的，必须每次都查出最新的
		if (o!=null && CollectionUtils.isNotEmpty(o.getListtblzd())) return new ArrayList<Map<String,Object>>(o.getListtblzd());

		return o.getMp().getTblZiduan(o.getTb(), CL.DB);
	}

	//判断该字符串是否是Aevzdtype类型。
	private static boolean  isAevZdType(String v){
		boolean valid = false;
		for(Aevzd e:Aevzd.values()){
			if (e.toString().equalsIgnoreCase(v)){
				return true;
			}
		}

		return valid;
	}
	
	//判断是不是校验字符串，如果是对其做补充
	private static boolean  isValidateStr(String title, String v, Map<String,Object> mp){
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(v) || v.startsWith("\\")
			||Pattern.compile("^\\s*sql\\s*:", Pattern.CASE_INSENSITIVE).matcher(v).find()
			||Pattern.compile("^\\s*phd\\s*:", Pattern.CASE_INSENSITIVE).matcher(v).find()){
			return false;
		}
		String arr[] = v.trim().split("\\s+");
		if (arr==null || arr.length<=0){
			return false;
		}

		boolean isVdt = false;
		String vdtstr = "";
		for (int i = 0; i < arr.length; i++) {
			int mhidx  = arr[i].indexOf(":");
			if (arr[i].startsWith("required")){//如果是需要内容为required，写成\required
				isVdt = true;
				if (mhidx==-1 ){//没写冒号
					vdtstr +=" required:::"+title+"的值不能为空";
					mp.put("required", true);
				}else if (mhidx+1<arr[i].length()){
					vdtstr +=" required:::"+arr[i].substring(mhidx+1, arr[i].length());
					mp.put("required", true);
				}
			}else if (arr[i].startsWith("unique")){//该字段唯一，可以带筛选参数
				isVdt = true;
				if (mhidx==-1 ){//没写冒号
					vdtstr += " "+ arr[i]+":::"+title+"的值已经存在，请检查";
					mp.put("unique", true);
				}else if (mhidx+1<arr[i].length()){
					vdtstr += " "+arr[i].substring(0, mhidx)+":::"+arr[i].substring(mhidx+1, arr[i].length());
					mp.put("unique", true);
				}
			}else if (mhidx!=-1 && mhidx-1>=0 && mhidx+1<arr[i].length()){
				isVdt = true;
				vdtstr += " "+arr[i].substring(0,mhidx)+":::"+arr[i].substring(mhidx+1, arr[i].length());
			}
		}
		if (isVdt)mp.put("validate", vdtstr.trim());

		return isVdt;
	}
	
	//判断是不是补充js字符串
	private static boolean  isExtjsStr(Crud o, String v, Map<String,Object> e){
		if (o ==null || StringUtils.isEmpty(v) || v.startsWith("\\") || !v.contains("=")
			||Pattern.compile("^\\s*sql\\s*:", Pattern.CASE_INSENSITIVE).matcher(v).find()
			||Pattern.compile("^\\s*phd\\s*:", Pattern.CASE_INSENSITIVE).matcher(v).find()){
			return false;
		}

		String arr[] = v.trim().split("=");
		if (arr==null || arr.length<2){//至少得有两项（事件名和对应处理js），可以不写引入js文件的值，类似onfocus=fn_py1(this)=pinyin.js
			return false;
		}

		e.put("event", " "+arr[0]+"='"+arr[1]+"' ");
		if(arr.length>=3) {
			boolean found=false;
			List<String> extjs = o.getAev().getExtjs();
			for(String s:extjs){
				if (s.equalsIgnoreCase(arr[2])){
					found=true;
					break;
				}
			}
			if (!found){
				extjs.add(arr[2]);
				o.getAev().setExtjs(extjs);
			}
		}

		return true;
	}

	//这里是根据数据库中字段设置的长度来直接设置前台input这些的maxlength，等于maxlength的设置也替你省掉了
	private static void putMaxLen(Map<String,Object> e, List<Map<String,Object>> listZiduan ) throws Exception{
		String zdm = ((String)e.get("zdm")).toLowerCase();
		String zdtp = ((String)e.get("type")).toUpperCase();
		if (Aevzd.INPUT.toString().equals(zdtp) 
				|| Aevzd.RICH.toString().equals(zdtp) 
				|| Aevzd.TEXTAREA.toString().equals(zdtp)
				|| Aevzd.PASSWORD.toString().equals(zdtp)
				|| Aevzd.ZDB.toString().equals(zdtp)){
			for (int j = 0;j<listZiduan.size();j++){
				String col_name = ((String)listZiduan.get(j).get("COLUMN_NAME")).toLowerCase();
				if (col_name.equals(zdm)){
					Object datyle = listZiduan.get(j).get("DATA_TYPE");
					//Object datyle = listZiduan.get(j).get("data_type");
					if (datyle==null) {	
						//break;
						throw new  Exception("怎么可能会发生某个数据库表的某个字段的data_type读不出来的情况呢？反正我是没遇到，如果遇到我怀疑是没看压缩包里的必读，我发誓我看过压缩包里的必读了");
					}
					if ("varchar".equalsIgnoreCase(datyle.toString()) || "mediumtext".equalsIgnoreCase(datyle.toString())){
						e.put("maxlen", listZiduan.get(j).get("CHARACTER_MAXIMUM_LENGTH"));//反正我本地不需要除以2，3这种，1比1
					}else if ("int".equalsIgnoreCase(datyle.toString())){
						e.put("maxlen", 9);
					}
					break;
				}
			}
		}
	}

	//从查出的数据库表字段信息里得到主键字段
	private static List<String> getZjFromDb(List<Map<String,Object>> listZiduan) throws Exception{
		List<String> retLst = new ArrayList<String>();
		for (Map<String, Object> map : listZiduan) {
			if ("PRI".equalsIgnoreCase((String)map.get("COLUMN_KEY"))){
				retLst.add((String)map.get("COLUMN_NAME"));
			}
		}

		if (CollectionUtils.isEmpty(retLst)){
			throw new  Exception("getZjFromDb---表没设置主键的话aev编辑和查看的时候无法正常工作，因为没法查是哪条记录");
		}

		return retLst;
	}

	//组装bread
	public static String zzBread(String txt,String href, String aevH){
		String ret="";
		if (StringUtils.isNotEmpty(txt) || StringUtils.isNotEmpty(href)){
			ret += "<div aevH='"+aevH+"'><a href='"+href+"'  class='icon-fanhui fanhui'> "+txt+"</a></div>";
		}
		
		return ret;
	}
	
	public static String arr2instr(Object[] arr){
		String ret=" ('') ";

		if (arr!=null && arr.length>0){
			ret = " ( ";
			for (int i = 0; i < arr.length; i++) {
				ret += "  '"+arr[i].toString()+"' ,";
			}
			ret = ret.substring(0,ret.length()-1)+" ) ";
		}
		
		return ret;
	}
	
	public static String map2instr(Map<String, Object> map){
		String ret="";
		
		if (map!=null && map.size()>0){
			String[] arr = new String[map.size()];
			int i=0;
			for (Map.Entry<String,  Object> e : map.entrySet()) {
				arr[i++] = e.getKey();
			}
			return arr2instr(arr);
		}

		return ret;
	}
	
	public static String list2instr(List<String> list){
		return arr2instr(list.toArray());
	}
	
	public static String list2instr(List<Map<String, Object>> list, String kee){
		return arr2instr(list2arr(list, kee));
	}
	
	public static String[] list2arr(List<Map<String, Object>> list, String kee){
		String[] ret =null;
		
		if (CollectionUtils.isNotEmpty(list) && StringUtils.isNotEmpty(kee)){
			ret = new String[list.size()];
			int i=0;
			for (Map<String, Object> map : list) {
				if (map.get(kee)!=null){
					ret[i++] = map.get(kee).toString();
				}
			}
		}

		return ret;
	}
	
	public static void getStrCidIn(String[] ret,String id, List<Map<String, Object>> alllm){
		for (int i=0;i<alllm.size();i++ ) {
			Object pId = alllm.get(i).get("pId");
			if (pId!=null && id.equals(pId.toString())){
				String innerlmid = alllm.get(i).get("id").toString();
				ret[0] += innerlmid+",";
				getStrCidIn(ret, innerlmid, alllm);
			}
		}
	}
	
	
	
}
