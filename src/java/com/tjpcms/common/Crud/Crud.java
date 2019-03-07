package com.tjpcms.common.Crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tjpcms.cfg.XT;
import com.tjpcms.common.HS;
import com.tjpcms.common.HT;
import com.tjpcms.common.QX;
import com.tjpcms.spring.mapper.EntMapper;

public class Crud {
	public static final Integer SES_CRUD_TO=3600;
	
	Aev aev = new Aev();//add,edit,view
	Retrieve r = new Retrieve();
	Delete d = new Delete();
	
	EntMapper mp;
	String tb;
	String bread;
	
	List<Map<String,Object>>listtblzd = null;//可以手动写，临时表的时候只能这样
	String u;//uuid
	
	Object oses;//保存一些需要存session的信息
	
	String cid=null;//channel id，统一为只传递单个值。fulanmu_nr和fulanmu_zi都只传递pId
	String clx="";//channel lx，tjpcms_lanmu中lx字段的值
	String[] cchild = null;//如果是父栏目zi或父栏目nr，使用这个字段来记录直接子节点
	

	//暂未使用
	String dyhs;//记录调用Crud的函数，调试时可以输出该信息
	
	

	public String[] getCchild() {
		return cchild;
	}
	public void setCchild(String[] cchild) {
		this.cchild = cchild;
	}
	public Object getOses() {
		return oses;
	}
	public Crud setOses(Object oses) {
		this.oses = oses;
		
		return this;
	}
	public String getDyhs() {
		return dyhs;
	}

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}

	public List<Map<String, Object>> getListtblzd() {
		return listtblzd;
	}
	public Crud setListtblzd(List<Map<String, Object>> listtblzd) {
		this.listtblzd = listtblzd;
		return this;
	}
	public Aev getAev() {
		return aev;
	}
	public void setAev(Aev aev) {
		this.aev = aev;
	}
	
	//获得调用函数的名称
	//也是废弃了，改为uuid，防止session被伪造
	private String getDyfnName() {  
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
        StackTraceElement e = stacktrace[3];  
        return e.getClassName()+"."+e.getMethodName();  
    }
	
	//根据给定的id，找出该节点下所有类型不为fulanmu_nr或fulanmu_zi的子孙节点、
	//我还是设计为只查直接子栏目吧，否则有点麻烦，所以这个函数先废弃了，也许以后改设计了会再用
	private static List<String> getFlmnrChilds(String curnode, EntMapper mp){
		List<Map<String, Object>> alllm = mp.r("select id,pId,lx from tjpcms_lanmu");
		List<String> ret = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(alllm)){
			Map<String, Object> lmmp = new HashMap<String, Object>();
			for (int i=0;i<alllm.size();i++ ) {
				if (alllm.get(i).get("id")==null) continue;
				lmmp.put(alllm.get(i).get("id").toString(), alllm.get(i));//把所有节点都放到map里，这样就可以随时取出来看pId了
			}

			for (int i=0;i<alllm.size();i++ ) {
				Object id = alllm.get(i).get("id");
				Object pId = alllm.get(i).get("pId");
				Object lx = alllm.get(i).get("lx");
				if (id==null || pId==null || lx==null || lx.toString().equals("父栏目zi") || lx.toString().equals("父栏目nr")) continue;

				
			}
		}

		return ret;
	}
	
	
	//不需要了，不传___lmmc了，URLDecoder.decode(___lmmc,"UTF-8");
	//只有三种类型是会牵扯到直接子节点的：fulanmu_zi,fucaidan_zi,fulanmu_nr
	public Crud(EntMapper mp,HttpServletRequest request, String tb, String bread) throws Exception {
		if (mp==null || StringUtils.isEmpty(tb)) {
			throw new Exception("crud配置中的mp和tb参数不能为空！");
		}
		this.mp = mp;
		this.tb = tb;

		//针对fulanmu_nr做个后台校验，并且把栏目表中的nrtbl字段填起来
		Object lmmc =null;
		cid = request.getParameter("___cid");
		if (StringUtils.isNotEmpty(cid)){//说明是栏目内容里提交过来的
			List<Map<String, Object>> listlm = mp.r("select t.lx,t.name,s.jsids,s.mc lcmc from tjpcms_lanmu t left join tjpcms_liucheng s on t.lcid=s.id where t.id ='"+cid+"'");
			if (CollectionUtils.isEmpty(listlm) || listlm.size()!=1){
				throw new Exception("异常错误！cid参数有误！");
			}
			if (listlm.get(0)==null || listlm.get(0).get("lx")==null || listlm.get(0).get("name")==null){
				throw new Exception("异常错误！栏目"+cid+"的lx或name字段有误！");
			}
			lmmc =  listlm.get(0).get("name");

			//更新lanmu的nrtbl
			clx =  listlm.get(0).get("lx").toString();
			cchild = new String[]{cid};//默认值就就是自己的栏目id
			List<Map<String, Object>> listchild =null;
			if ("父栏目zi".equals(clx) || "父栏目nr".equals(clx)){//查出直接子节点
				listchild = mp.r("select * from tjpcms_lanmu t left join tjpcms_liucheng s on t.lcid=s.id where t.pId ='"+cid+"' and t.id in"+HS.arr2instr(QX.getValidLmids(mp, request)));//都是查出直接子栏目
				if ( "父栏目nr".equals(clx) && CollectionUtils.isNotEmpty(listchild) && listchild.size()>1){
					for (int i = 0; i < listchild.size(); i++) {
						if (listchild.get(i)==null || listchild.get(i).get("lx")==null || !listchild.get(i).get("lx").toString().equals(listchild.get(0).get("lx").toString())){
							throw new Exception("异常错误！对于父栏目nr的栏目类型，其直接子节点的类型必须全部相同！");
						}
					}
				}
				cchild = HS.list2arr(listchild, "id");
			}
			final String strcidin = HS.arr2instr(cchild);
			if (XT.lmneed_nrtbl(tb)) mp.upd("update tjpcms_lanmu set nrtbl='"+tb+"' where id in "+strcidin);//fulanmu_zi不能设置nrtbl，因为本来就没有啊

			//为tb加上cid字段
			List<Map<String, Object>> listZiduan = HS.getListZiduan(this);
			if (XT.lmneed_cid(tb, mp) && !HS.zdInDbcol("cid", listZiduan) && mp.upd("alter table "+tb+" add cid  int(11) NOT NULL COMMENT '栏目id' ") <= 0){//fulanmu_zi也不需要cid啊
				throw new Exception("异常错误！为"+tb+"表添加cid字段失败！");
			}

			//配置crud默认值
			String exw = "";
			if ("父栏目zi".equals(clx)){
				d.setNeed(false);
				aev.setNeede(false);
				aev.setNeeda(false);
				exw +="tt.id in"+strcidin;
			}else{//是栏目内容中的普通栏目或者是父栏目nr
			 	if (HS.zdInDbcol("cid",listZiduan)) exw +="tt.cid in"+strcidin;
				if (HS.zdInDbcol("delf",listZiduan)){
					exw +=" and delf=0 ";
					d.setBatdel(true).setZdysql("  update "+tb+" set delf=1");
				}
			}
			if (StringUtils.isNotEmpty(exw)) {
				r.setExwhere(exw);
			}
			
			//因为是栏目内容里的菜单，因此可能设置审核流程
			if (XT.lmneed_nrtbl(tb)){
				if (!HS.tbaddshzd(mp, null, tb, true)) throw new Exception("异常错误！栏目审核流程相关字段添加失败！");//对于系统表来说，只有nrtbl是存在的，才有审核的必要啊

				//和审核相关的预设置，等于controller里就不用再写了
				if ("父栏目nr".equals(clx)){//"父栏目zi".equals(clx)不会进入到这里
					//前面已经查过了，这里就不用再查了
				}else{
					listchild = mp.r("select * from tjpcms_lanmu t left join tjpcms_liucheng s on t.lcid=s.id where t.id in"+HS.arr2instr(cchild));
				}
				for (Map<String, Object> map : listchild) {
					if (map.get("mc")!=null  && !"无审核".equals(map.get("mc").toString()) && !"自定义".equals(map.get("mc").toString()) && map.get("jsids")!=null){
						r.setJs_aft_sx("fn_cmnsh_aftsx()");
						r.setExtjoinstr("left join tjpcms_lanmu x on x.id = t.cid left join tjpcms_liucheng y on y.id=x.lcid");
						r.getExtjoinzdm().add("y.mc ___shlcmc,y.jsids ___lcjsids");
						r.setHook_aftcx("___cmnsh_aftcx");//靠这个来识别栏目是否有审核流程
						break;
					}
				}
			}
		}

		//设置lmname
		if ("父栏目zi".equals(clx)){
			this.bread = "当前位置："+lmmc;
		}else if (StringUtils.isNotEmpty(bread)){
			if (bread.contains("<a")){
				this.bread = bread;
			}else{
				this.bread = "当前位置："+bread;
				if (lmmc!=null &&!bread.contains(lmmc.toString()))this.bread += " - "+lmmc;
			}
		}
		if (StringUtils.isEmpty(this.bread) && bread!=null && lmmc!=null){
			this.bread = "当前位置："+lmmc.toString();
		}
		
		//设置session
/*		Mademd5 mad = new Mademd5();
		dyhs = getDyfnName();*/
		
		//其实可以考虑把上一个u删掉，节省内存
		u=UUID.randomUUID().toString();//mad.toMd5(dyhs+mad.toMd5(tb)+bread);
		HttpSession ses = request.getSession();
		ses.setMaxInactiveInterval(SES_CRUD_TO);
		ses.setAttribute(u, this);
		request.setAttribute("o", this);
		request.setAttribute("___uid", HT.getUid(request));//把用户的uid带到前台页面
		request.setAttribute("___zscg", HT.isCjgly(request) && !HT.isTestCjgly(request));//是不是正式超管，非测试超管的那种
	}
	
	//去掉了这个，因为我发现有个问题，有这个函数的话，函数的调用又多了一层，导致getDyfnName里[4]才是谁调用了这个函数，正常应该是[3]。所以不能定义这个构造函数
/*	public Crud(EntMapper mp,HttpServletRequest rq, String tb) throws Exception {
		this(mp,rq,tb,"");
	}*/
	
	//其实一直考虑过，如果是自定义sql，怎么来创建crud呢，当然绝大多数情况下，有一个主表，再join，再有hook，自定义保存这种已经足够对付了
	//但实际业务中肯定会遇到特殊情况的，当然你可以不走crud这一套，那就什么情况都可以搞定
	//但其实我目前能想到的就已经有三种方法了
	//1、创建视图，等于就是自定义sql了，这样可以直接看，但是无法直接写，需要自定义保存
	//2、创建临时表，可以读写，但不能保存，我没用过，应该是可以用于一些临时性的计算（这个已经实现，可以读写保存，见）
	//3、就是这里我直接传一个String数组，反正能帮我展示出来就行了，保存也是要自定义的，但是因为不是走数据库查询的，很多crud的已有功能无法直接用上

	
	
	public Retrieve getR() {
		return r;
	}
	public String getClx() {
		return clx;
	}
	public void setClx(String clx) {
		this.clx = clx;
	}
	public void setR(Retrieve r) {
		this.r = r;
	}
	public Delete getD() {
		return d;
	}
	public void setD(Delete d) {
		this.d = d;
	}
	public EntMapper getMp() {
		return mp;
	}
	public void setMp(EntMapper mp) {
		this.mp = mp;
	}
	public String getTb() {
		return tb;
	}
	public void setTb(String tb) {
		this.tb = tb;
	}
	public String getBread() {
		return bread;
	}
	public void setBread(String bread) {
		this.bread = bread;
	}
}
