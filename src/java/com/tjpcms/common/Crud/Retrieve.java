package com.tjpcms.common.Crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.tjpcms.common.CL;


public class Retrieve {
	//==================================================================================================================================
	//暂时还没用到的，就是一些以前想到，但目前没用到，以后可能会实现的功能
	String zdySql;//查询列表的自定义sql语句，目前。。。没这功能，因为我还没遇到这个需求，但我觉得以后会遇到的，其实好像可以用视图或者临时表代
	boolean useIcon = false;//想在操作区用图标的，目前还没这功能
	//==================================================================================================================================

	
	boolean need = false;//只要调用了setList就把该字段设置为true，controller里不调用setList，则默认就是false，返回adm/aev就可以直接显示编辑页了，比如作者简介这种纯编辑的页面，不需要列表页
	
	
	//==================================================================================================================================
	//分页相关，一共查出多少条记录，每页多少条，一共多少页，当前是第几页
	//这边的分页以后我会自己写一个插件tjppage，替换掉现在的laypage
	Integer recTotal = 0;//记录总数
	private Integer perPage = CL.LIST_PP_DEF;//每页记录数，默认10
	Integer pgTotal = 0;//分页总数
	Integer curPage = 1;//当前第几页
	//==================================================================================================================================

	
	//==================================================================================================================================
	//查询区相关的参数
	List<Map<String,Object>> cxs = new ArrayList<Map<String,Object>>();//根据二维数组解析后的列表查询区的内容
	List<Map<String,Object>> defs = new ArrayList<Map<String,Object>>();//根据二位数组解析后的列表查询区的默认值配置
	//==================================================================================================================================
	
	
	//==================================================================================================================================
	//表头区和表体区
	List<Map<String,Object>> ths = new ArrayList<Map<String,Object>>();//表头
	List<Map<String,Object>> tbs = new ArrayList<Map<String,Object>>();//表体，记录有哪些数据
	List<Map<String,Object>> zds = new ArrayList<Map<String,Object>>();//
	String extselect = "";//最终拼出的sql中select部分里的内存查询
	//==================================================================================================================================
	
	
	//==================================================================================================================================
	//操作区
	List<Map<String,Object>> preczq = new ArrayList<Map<String,Object>>();//预解析操作区的结果
	
	//==================================================================================================================================
	
	
	String listloc;
	String exwhere="";
	
	String extjoinstr;//额外的join语句
	List<String> extjoinzdm = new ArrayList<String>();//额外的join字段名
	

	
	List<String> extjs = new ArrayList<String>();//列表查询区的内容
	List<String> extjsp = new ArrayList<String>();//还可以带额外的jsp，不能带参数，如需要参数用request.setAttribute
	
	List<Map<String,Object>> ans = new ArrayList<Map<String,Object>>();//按钮区
	
	String hook_aftcx;//查询出列表后，使用该hook对列表内容进行修正，比如将0-59分重新修正为不合格，而不显示具体分数这类的情况
	
	//查询时增加额外的字段，不在列表页的表头区显示出来，但这个字段存在
	List<String> extzdm=new ArrayList<String>();
	List<String> extzdstr =new ArrayList<String>();

	//把查list和count的两个sql存下来，limit后的list的结果是存在tbs里的，但是我需要从所有的list里的查（比如unique里），因为list里的字段是全部的
	String sql_r;//记录查出所有记录详情的的sql
	String sql_c;//记录查出所有记录总数量的sql，没有排序
	String sql_p;//记录查出的当前页记录的sql，按排序
	
	//排序其实要可以配置，以及可以在页面上点击的，目前没搞
	String odrby;//直接写就行，会添加在sql order by 的最前面，如果自己加了order by 会在set时被去掉

	
	
	Tree tree = new Tree();//2018-06-26 增加一个左侧的tree，最终页面类似于栏目列表
	String js_aft_treeclk;//统一使用js_aft(bef)_xxx的格式，代表是js的相关钩子
	
	
	String js_aft_layout="";//after layout，如果自行修改了list页面布局，留这个接口来修正布局
	
	
	//==================================================================================================================================
	//局部刷新区域
	//2018-07-03，还是决定加入查询区异步加载的功能，虽然绝大多数的情况下整页刷新就可以搞定了，但的确也存在这样的需求，即数据区需要单独异步局部刷新，以提升体验，比如：流程管理里关闭弹出层后要刷新父页面，子页面新增和更改排序，类似栏目列表页面
	//整页刷新其实也能实现这些功能，但体验上略差，最主要的就是会闪一下，因为是整页刷新的，但是局部刷新会存在失败的可能，这倒也是比整页刷新不足的地方
	//但我考虑后还是觉得不能放弃原有的全局刷新，虽然这样维护时要维护两份代码，即列表数据区的html需由同样的逻辑，后端模板语法(jsp)和前端模板语言(arttemplate)各自实现一下
	boolean jubu = false;//列表区（数据区）是否要设置为局部刷新，我非常建议默认为false
	String js_aft_sx="";//局部刷新成功后需要执行的额外js代码
	//==================================================================================================================================
	
	boolean inc_ztree = false;//list.jsp中是否引入ztree的js和css
	
	
	
	public boolean isInc_ztree() {
		return inc_ztree;
	}

	public void setInc_ztree(boolean inc_ztree) {
		this.inc_ztree = inc_ztree;
	}
	public void setInc_ztree() {
		this.inc_ztree = true;
	}


	public String getJs_aft_sx() {
		return js_aft_sx;
	}

	public Retrieve setJs_aft_sx(String js_aft_sx) {
		this.js_aft_sx = js_aft_sx;
		return this;
	}

	public String getJs_aft_treeclk() {
		return js_aft_treeclk;
	}

	public void setJs_aft_treeclk(String js_aft_treeclk) {
		this.js_aft_treeclk = js_aft_treeclk;
	}

	public String getJs_aft_layout() {
		return js_aft_layout;
	}

	public Retrieve setJs_aft_layout(String js_aft_layout) {
		this.js_aft_layout = js_aft_layout;
		
		return this;
	}


	public List<Map<String, Object>> getPreczq() {
		return preczq;
	}

	public void setPreczq(List<Map<String, Object>> preczq) {
		this.preczq = preczq;
	}

	public boolean isJubu() {
		return jubu;
	}

	public void setJubu(boolean jubu) {
		this.jubu = jubu;
	}
	public Retrieve setJubu() {
		this.jubu = true;
		return this;
	}

	public Tree getTree() {
		if (tree!=null && StringUtils.isNotEmpty(tree.tb)) setJubu();
		return tree;
	}

	public void setTree(Tree tree) {
		setJubu();//用到了setTree就设置为局部刷新，因为列表区左侧有树的话，只能局部刷新。全局也不是做不出来，但是太麻烦体验也不好。
		this.tree = tree;
	}

	public String getOdrby() {
		return odrby;
	}

	public Retrieve setOdrby(String odrby) {
		odrby = odrby.trim().replaceAll("order\\s*by", " ");
			
		String[] split = odrby.split(",");
		String ret="";
		if(split!=null && split.length>0){
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].trim();
				if (!split[i].startsWith("tt.")){
					ret += "tt."+split[i]+",";
				}
			}
		}
		
		this.odrby = " "+ret.substring(0, ret.length()-1)+" ";
		
		return this;
	}

	public List<String> getExtzdm() {
		return extzdm;
	}

	public List<String> getExtzdstr() {
		return extzdstr;
	}

	public String getHook_aftcx() {
		return hook_aftcx;
	}
	public Retrieve setHook_aftcx(String hook_aftcx) {
		this.hook_aftcx = hook_aftcx;
		
		if (StringUtils.isNotEmpty(hook_aftcx)) {
			hook_aftcx = hook_aftcx.trim();
			if (!hook_aftcx.contains(".")){
				hook_aftcx = "com.tjpcms.common.Hook."+hook_aftcx;
			}
		}

		this.hook_aftcx = hook_aftcx;
		
		return this;
	}
	public List<Map<String, Object>> getAns() {
		return ans;
	}
	public void setAns(List<Map<String, Object>> ans) {
		this.ans = ans;
	}
	public List<String> getExtjsp() {
		return extjsp;
	}

	public boolean isUseIcon() {
		return useIcon;
	}
	public void setUseIcon(boolean useIcon) {
		this.useIcon = useIcon;
	}
	public List<String> getExtjs() {
		return extjs;
	}

	public void setExtjs(List<String> extjs) {
		this.extjs = extjs;
	}

	public void setExtjsp(List<String> extjsp) {
		this.extjsp = extjsp;
	}

	public boolean isNeed() {
		return need;
	}
	public void setNeed(boolean need) {
		this.need = need;
	}
	public String getExtjoinstr() {
		return extjoinstr;
	}
	public void setExtjoinstr(String extjoinstr) {
		this.extjoinstr = extjoinstr;
	}
	public List<String> getExtjoinzdm() {
		return extjoinzdm;
	}
	public void setExtjoinzdm(List<String> extjoinzdm) {
		this.extjoinzdm = extjoinzdm;
	}
	public List<Map<String, Object>> getCxs() {
		return cxs;
	}
	public void setCxs(List<Map<String, Object>> cxs) {
		this.cxs = cxs;
	}

	public List<Map<String, Object>> getDefs() {
		return defs;
	}

	public void setDefs(List<Map<String, Object>> defs) {
		this.defs = defs;
	}

	public String getExwhere() {
		return exwhere;
	}

	private boolean iskongbai(char c){
		return Pattern.compile("\\s+").matcher(String.valueOf(c)).matches();
	}

	private boolean canfoundt(String s, int i){
		
		if (StringUtils.isEmpty(s) || !(i>=0 && i<s.length())) return false;
		
		while(i-1>=0 && s.charAt(i-1)!='t') i--;;
		
		
		return i-1>=0;
	}

	//这边为where中的字段加上t.   防止字段重名
	//这边如果要做字符串的大括号解析略有点麻烦，还是改成
	public Retrieve setExwhere(String exwhere) {
		
/*		List<Integer> bsfidx = new ArrayList<Integer>();
		if (StringUtils.isNotEmpty(exwhere)){//这边自动加上t.，不然组sql时可能会字段名重复
			int eqidx = -1;
			do{
				eqidx = exwhere.indexOf("=", ++eqidx);
				if (eqidx!=-1){
					int inneridx = eqidx;
					do{
						inneridx--;
					}while(inneridx>=0 && iskongbai(exwhere.charAt(inneridx)));//退出循环的条件是eqidx<0或者不是空白符了
					if (inneridx>=0){
						do{
							inneridx--;
						}while(inneridx>=0 && !iskongbai(exwhere.charAt(inneridx)));
						if (inneridx+1<exwhere.length() && exwhere.charAt(inneridx+1)=='t' 
							&& inneridx+2<exwhere.length() && exwhere.charAt(inneridx+2)=='.'){//写了t.的话就不加了
							
						}else{//还要看是不是类似     	t  		.   id  = 10			t	.id=10 	这种带空白符的写法
							int bswz = inneridx+1;
							boolean mhfound = exwhere.charAt(bswz)=='.';
							do{inneridx--;}while(inneridx>=0 && iskongbai(exwhere.charAt(inneridx)));
							if(inneridx>=0) {
								if (exwhere.charAt(inneridx)=='t' && mhfound){	//说明是	t	.id的情况
									
								}else if (exwhere.charAt(inneridx)=='.' && !mhfound && canfoundt(exwhere, inneridx)){//说明是	t	.	id的情况
									
								}else{
									bsfidx.add(bswz);//此处索引为标识符第一个字符
								}
							}else{
								bsfidx.add(bswz);//此处索引为标识符第一个字符
							}
						}
					}
				}
			}while(eqidx!=-1);
		}
		if (CollectionUtils.isNotEmpty(bsfidx)) {
			String copy = new String(exwhere);
			exwhere="";
			for (int i = 0; i < bsfidx.size(); i++) {
				int st = (i==0?0:bsfidx.get(i));
				int ed = (i==bsfidx.size()-1?copy.length():bsfidx.get(i+1));
				exwhere += copy.substring(st,bsfidx.get(i))+"t."+ copy.substring(bsfidx.get(i),ed);
			}
		}*/

		if (StringUtils.isNotEmpty(exwhere.trim())) {
			exwhere = exwhere.trim();
			if (exwhere.startsWith("and") && exwhere.length()>=4 && Pattern.compile("\\s").matcher(exwhere.substring(3,4)).matches()
				|| exwhere.startsWith("or") && exwhere.length()>=3 && Pattern.compile("\\s").matcher(exwhere.substring(2,3)).matches()){//and或or开头，后面接着空白符
				this.exwhere += exwhere;
			}else{
				this.exwhere += " and "+exwhere;//默认是and
			}
			
/*			String[] split = exwhere.split("=");
			if (split.length<=1){
				this.exwhere += " and ("+exwhere+") ";
			}else{
				String zdz = split[1];
				if (zdz.indexOf("'")==-1){
					zdz = "'"+zdz+"'";
				}
				this.exwhere += " and ("+split[0]+"="+zdz+") ";
			}*/
		}
		
		return this;
	}
	public String getListloc() {
		return listloc;
	}
	public void setListloc(String listloc) {
		this.listloc = listloc;
	}
	public Integer getRecTotal() {
		return recTotal;
	}
	public void setRecTotal(Integer recTotal) {
		this.recTotal = recTotal;
	}
	public Integer getPgTotal() {
		return pgTotal;
	}
	public void setPgTotal(Integer pgTotal) {
		this.pgTotal = pgTotal;
	}
	public List<Map<String, Object>> getThs() {
		return ths;
	}
	public void setThs(List<Map<String, Object>> ths) {
		this.ths = ths;
	}
	public List<Map<String, Object>> getTbs() {
		return tbs;
	}
	public void setTbs(List<Map<String, Object>> tbs) {
		this.tbs = tbs;
	}

	public Integer getPerPage() {
		return perPage;
	}
	public Retrieve  setPerPage(Integer perPage) {
		if (perPage==null){
			perPage=CL.LIST_PP_DEF;
		}else {
			if (perPage>CL.LIST_PP_MAX)perPage = CL.LIST_PP_MAX;
			if (perPage<=0)perPage = CL.LIST_PP_DEF;
		}
		
		this.perPage = perPage;
		
		return this;
	}

	public String getZdySql() {
		return zdySql;
	}
	public void setZdySql(String zdySql) {
		this.zdySql = zdySql;
	}
	public String getSql_r() {
		return sql_r;
	}
	public void setSql_r(String sql_r) {
		this.sql_r = sql_r;
	}
	public String getSql_c() {
		return sql_c;
	}
	public void setSql_c(String sql_c) {
		this.sql_c = sql_c;
	}


	public String getSql_p() {
		return sql_p;
	}

	public void setSql_p(String sql_p) {
		this.sql_p = sql_p;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public List<Map<String, Object>> getZds() {
		return zds;
	}
	public void setZds(List<Map<String, Object>> zds) {
		this.zds = zds;
	}
	
	
	
	public String getExtselect() {
		return extselect;
	}

	public void setExtselect(String extselect) {
		this.extselect = extselect;
	}

	public static void main(String[] args) throws Exception {
		Retrieve re = new Retrieve();
		
		String t[] = {
				" pId  		= 1 and t.delf 			=	 0	 ",
				" t.pId  		= 1 and t.delf 			=	 0	 ",
				"	 pId  		= 1 and t.delf 			=	 0	",
				"    	 t	. 		pId  		= 1 and t.delf 			=	 0	 ",
				" pId  		= 1 and 		delf 			=	 0	 ",
				" pId  		= 1 and t		.delf 			=	 0	 ",
				" pId  		= 1 and t.		delf 			=	 0	 ",
				"t				.pId  		= 1 and t.				delf 			=	 0	 ",
				" t.pId  		= 1 and t.				delf 			=	 0	 ",
				"t.pId  		= 1 and t.				delf 			=	 0	 ",
				" t				pId  		= 1 and t			.			delf 			=	 0	 ",
				".				pId  		= 1 and t			.			delf 			=	 0	 ",
				"",
				"t.",
				"				pId=1",
				"pId=1",
				".pId=1",
				".t pId=1",
				".  t pId=1",
		};
		for (int i = 0; i < t.length; i++) {
			re.setExwhere(t[i]);
			System.out.println(re.getExwhere());
		}
		
	}
	
}
