/**
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * 2018-05-03：nedcjgly就是need超级管理员，也就是需要超级管理员登录后才能访问的链接，目前主要就是权限配置这一块的四个菜单了（角色管理、菜单管理、权限管理、用户管理），另外test账号只能看不能操作
 * 2018-06-27：改造一下角色管理和权限管理两个菜单，把原先的列表型的角色管理重新做成树形的，相应的权限管理也得做成树形的了，所以树形控件还真是很有作用的
 * 2018-06-28：NedCjglyController我设定为只有超级管理员可以访问，所以在菜单管理里是没有角色管理啊，菜单管理啊这几个菜单的。我认为非超级管理员并不需要这里的功能
 */
package com.tjpcms.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ndktools.javamd5.Mademd5;
import com.tjpcms.cfg.XT;
import com.tjpcms.common.Aevzd;
import com.tjpcms.common.CL;
import com.tjpcms.common.HS;
import com.tjpcms.common.HT;
import com.tjpcms.common.Crud.Crud;


@Controller
@RequestMapping("/"+XT.nedcjgly)
public class NedCjglyController extends TjpcmsController{
	@Autowired
    private RequestMappingHandlerMapping handlerMapping;

	//2018-04-17处理后台权限菜单
	@RequestMapping(value = "qxcd")
	public String qxcd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HS.setGlobal(request);//设置一些全局的参数到页面里
		return "qxcd";
	}

	//角色管理
	//其实这边删除时应该再慎重一点，，有个二次提示啊这种，当然还有数据有效性校验，不过也无所谓了，想弄都是可以的，有钩子，这是后台的，前台也可以弄，加自定义js或者ajax就行了，总之就是其实原来是怎么处理的，现在还是可以的
	//这边就是业务需求嘛，你自己再增加角色
	//这里有两个小问题：
	//1、czs这个配置，其实之前想做条件组合这种case的，但是弄了一半忙其他事了，这个有点类似于编译器了，就是可以用逻辑符号来组合出你想要的条件，而不是现在这种的简单的单字段<>!=，但还是有替代的办法，这种条件逻辑嘛说实在的配起来也挺绕人
	//    这里我想要的逻辑是，超级管理员和前台注册用户这两个角色是预置的，不能删除（删除按钮不是隐藏而是灰色，点击后提示为什么灰色），另外，如果有角色菜单或者用户角色，都是必须先删掉菜单和用户才能再删角色的
	//2、这里删除我做的比较细致了，细致在什么地方呢，就是前后台我都做了校验，这样当然是最标准的了，前台校验减轻服务器压力，后台校验保证无效数据不入库。但其实人的精力有限，或者说虫死猿烹，不是多么重要的系统没必要这么细致了
	//另外这里加入了两个extjs，其实可以放到一个里面，我这样做只是为了演示一下，可以放入多个extjs，而且还能带参数，带参数的好处就是可以在初始化时区分是谁引入了这个js了
	//czs这一块，演示了两种方法来实现根据具体的业务逻辑来控制按钮的样式和操作，但是这里按照case来配置操作项时，不能做条件逻辑，只能做单纯的=><!=<=>=这种
	//其实还有个小小小的问题，就是在tjpcms_liucheng中，jsids是以→分隔的，所以角色名称不可以再有这个符号了，不然逻辑上就出错了，就不管了吧，应该不会有带→的角色名
	//2018-07-02：开始修改成树形的角色，较之列表型更具普适性和实用性，最主要的设定就是父角色和子角色拥有同一栏目时，默认父角色可以看到和操作子角色的数据，当然为了应对特殊情况，这个逻辑是可配置的，普通逻辑通过权限管理来设置，特殊逻辑只能java代码了
	//什么叫特殊逻辑呢，比如我规定父可以看子，不能操作子，这叫正常逻辑，页面点击即可实现。但如果我规定，父只能看子近三天的数据，这个是无法页面操作的，强行页面实现就是侵入业务逻辑了，和tjpcms的设计理念不符。
	//删除角色后还得把角色的权限都删，qx_caidan和qx_fangfa，我没弄
	@RequestMapping(value = "juese")
	public ModelAndView juese(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e,request, "tjpcms_juese", "角色管理");
		o.getAev().setNeede(0).setHook_befad("jsglBefad").setHook_befgx("jsglBefgx");//不能新增系统预置的角色名，不能编辑任何角色名，可以编辑的话会有业务上的一致性问题的，所以直接不准编辑了吧，只准新增和删除
		o.getD().setHook_befdel("jsglBefdel").setZdyQrAft("fn_erci(ids)");//这个也是后台校验，以及删除的二次确认
		o.getR().getExtjs().add("extlistjs.js?s=juese");
		o.getR().getTree().setTb("tjpcms_caidan").setTitle("角色树").setTip("注：父子角色拥有相同栏目时，父角色默认具有子角色的数据查看和操作权限，也可以自定义权限。").setName("mc");//设置list页面tree树的相关数据
		o.getR().setJs_aft_sx("fn_juese_aftsx()");

		String cxs[][]= {{"角色名称","mc"},{"一级菜单数","cnt","op:>,<,>=,<=,=,!="},{"动态菜单数","dtcds","op:>,<,>=,<=,=,!="},{"角色用户数","jsyhs","op:>,<,>=,<=,=,!="}};
		String hds[][]= {{"角色id","id"},{"父id","pId"},{"角色名称","mc"},
				{"直接子角色数","zjss","sql:select count(*) from tjpcms_juese s where s.pId=t.id"},
				{"一级菜单数","cnt","sql:select count(*) from tjpcms_qx_caidan s where s.jsid=t.id and s.cdid in (select id from tjpcms_caidan x where x.pId="+CL.TREE_ROOT_ID+")"},
				{"动态菜单数","dtcds","sql:select count(*) from tjpcms_qx_caidan s where s.jsid=t.id and left(s.cdid,1)='-' and right(s.cdid,2)='-"+CL.TREE_ROOT_ID+"'"},
				{"角色用户数","jsyhs","sql:case when t.mc='超级管理员' then '1' else (select cast(count(*) as char) from tjpcms_usr s where s.jsid=t.id) end"},
				{"角色流程数","jslcs","sql:select count(*) from tjpcms_liucheng where find_in_set(t.mc,replace(jsids,'→',','))"},
				{"排序","px"},{"更新时间","gx"}};
		String czs[][]={
			//演示如何不做这个配置也可以按逻辑灰化按钮{"text:gray:编辑:case:mc=超级管理员#case:mc!=超级管理员:val:编辑","js:fn_cmn_edit(this,id,超级管理员为内置角色，不能编辑，不能删除！):listczq.js", "idx:1"},
			{"text:gray:删除:case:mc=超级管理员#case:mc!=超级管理员:val:删除","js:fn_jsgl_del(this,id):listczq.js"}//演示如何配置操作项来实现灰化按钮
		};
		HS.setList(request, o,hds, cxs, czs);//查询的配置

		String aev[][]= {
				{"角色名称","mc","required unique"},
				{"排序","px"}
		};
		HS.setAev(request, o, aev);//aev的配置

		return new ModelAndView("adm/list", null);
	}

	//菜单管理
	//这个其实类似栏目管理，就是说以树结构来管理三级菜单，第一第二级都是一层的，第三级是树形的
	//2018-08-02其实菜单管理基本上可以用o.r.tree来取代了，但还是略需要修改，还是算了吧。角色管理和菜单管理的页面布局和功能基本是一致的，但还是略微有一点区别
	@RequestMapping(value = "caidan")
	public String caidan(HttpServletRequest request, HttpServletResponse response){
		return "adm/caidan";
	}

	//菜单页面初始化，菜单管理和权限管理里都要用到这里，取菜单树
	//这里是个什么逻辑呢，有点小麻烦，就是说我需要把3级的子动态菜单树查出来并链接到对应的2级父节点上，替换掉该3级动态子节点，但是这里有两个难点
	//1、动态子菜单树的字段名称不一定是id,pId,name,lj，因为动态子菜单是单独维护的，比如栏目管理，或者我这里t_test_caidan_dt
	//2、动态子菜单树的节点id可能和tjpcms_caidan中的id重叠，这也是有问题的，问题产生的原因也是因为它们是分开维护的，不同的表，自然字段名可能一致而id值有重叠
	//所以，我在这里需要把动态树的数据重新组织一下，也就是在查询时，我将字段名改成标准的，id值改成负的，以与tjpcms_caidan中的id相区别，保证不会重叠
	//具体格式如：-188-194-139，含义是  【-和静态节点的id相区别】【188代表该动态子树的父节点是tjpcms_caidan中的id为188的节点】【194代表自身在原菜单树种的节点id为194】【139代表在子树中该节点的id为139】
	//最后发现还有个排序的问题，就是说某个2级菜单下有多个3级菜单，其中有部分是动态的，则生成权限树时，3级菜单的排序不能改变
	
	//如果是动态的三级菜单，我建议直接使用视图，否则一是关联字段无法读取，二是排序无法按照自定义的规则
	@RequestMapping(value = "getcdtree")
	public Map<String, Object> getcdtree(HttpServletRequest request, HttpServletResponse response, boolean noflush) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		String jsid = request.getParameter("jsid");
		if (StringUtils.isEmpty(jsid)) jsid = CL.TREE_ROOT_ID.toString();
		String chadt = request.getParameter("chadt");//查动态
		List<Map<String, Object>> cdTree = _e.getCdTree(jsid, chadt);

		//如果前台传递的参数dt为1，则查出所有的三级菜单（配置菜单这里），而像菜单管理这里就不需要查出所有的动态菜单，还要把该角色拥有的动态菜单打钩选中，而静态菜单在getCdTree中已经打钩了
		if ("1".equals(chadt)) {
			List<Map<String, Object>> lst3 = _e.r("select * from tjpcms_caidan t where t.pid in (select id from tjpcms_caidan s where s.pid in (select id from tjpcms_caidan x where x.pId="+
				CL.TREE_ROOT_ID+")) order by pId,px,gx desc");//查出所有层次在三级的菜单
			int cd3Indx = 0;//记录3级菜单列表的索引
			for (Map<String, Object> cd3 : lst3) {//遍历3级菜单
				String dt = (String)cd3.get("dt").toString();
				if ("1".equals(dt) && cd3.get("lj") != null && cd3.get("id")!=null && cd3.get("pId")!=null){//说明这个三级菜单是动态的，且lj字段是有值的，这样就可以查数据库了，id、pId也必须有值
					String strlj = cd3.get("lj").toString();
					String[] split = strlj.trim().split("\\s+");//格式是表名和四个字段名
					if (split==null || split.length!=5){
						throw new  Exception("我需要知道三级动态菜单的表名和四个字段名，不然无法查出来动态菜单树");
					}else{//这里没有直接存查询sql，一是不安全，二是不太好写，三是会被injFilter过滤掉（所以这个角度讲确实不适合直接存待执行的sql）
						String bm = split[0];
						String idmc = split[1];
						String pIdmc = split[2];
						int cnt = _e.cnt("select count(*) from "+bm+" where "+pIdmc+" is null and "+idmc+" ='"+CL.TREE_ROOT_ID.toString()+"'");//检查一下是否有某个节点不是根节点，但是节点ID和TREE_ROOT_ID一样的
						if (cnt!=1){
							throw new  Exception("动态树的根节点的id得是TREE_ROOT_ID("+CL.TREE_ROOT_ID+")，pId得是null");
						}
						
						String namemc = split[3];
						String ljmc = split[4] ;
						String pNodeId = cd3.get("pId").toString();//这颗子树的根节点
						String selfndId = cd3.get("id").toString();//这颗子树自身在主树中的节点id
						String s = "select id,pId,name,lj,"
								+"case when exists(select id from tjpcms_qx_caidan s where s.jsid='"+jsid+"' and s.cdid=t1.id) then 'true' else 'false' end checked"
								+" from(select case when "+pIdmc+" is null then concat('-"+pNodeId+"','-"+selfndId+"','-',cast("+CL.TREE_ROOT_ID+" as char)) "
								+" else concat('-"+pNodeId+"','-"+selfndId+"','-',cast("+idmc+" as char))"+" end "+"id,case when "+pIdmc+" is not null then concat('-"
								+pNodeId+"','-"+selfndId+"','-',cast("+pIdmc+" as char)) else "
								+pNodeId+" end pId,"+namemc+" name ,"+ljmc+" lj"+"  from "+bm+(HS.zdInDbcol("px", bm, _e)?" order by px":"")+")t1 where 1=1";
						List<Map<String, Object>> dtTree = _e.r(s);
						//树查出来了，还需要在tree1中找到合适的位置插入，保证和菜单管理中的排列次序是相同的
						if (cd3Indx==0){//这种就是把查出来的树放在最终树列表的最前面
							dtTree.addAll(cdTree);
							cdTree = dtTree;
						}else{
							Map<String, Object> last = lst3.get(cd3Indx-1);
							Object oprevid = last.get("id");//sibling previous
							if (oprevid!=null){
								for (int x=cdTree.size()-1;x>=0;x--){
									Map<String, Object> cur = cdTree.get(x);
									if (cur !=null){
										String oid = cur.get("id").toString();
										if (
											(!oid.startsWith("-") && oid.toString().equals(oprevid.toString()))
											|| (oid.startsWith("-") && oid.endsWith(CL.TREE_ROOT_ID.toString()) && oid.split("-")[2].equals(oprevid.toString()))
										){//找到兄弟节点
											cdTree.addAll(x+1, dtTree);//插入在该兄弟节点后面
											break;
										}
									}
								}
							}
						}
					}
				}
				cd3Indx++;
			}
		}
		m.put("all", cdTree);//直接查找出来的菜单树和查出来的三级菜单的动态部分合并起来
		
/*		for (int i = 0; i < cdTree.size(); i++) {
			System.out.println(cdTree.get(i).get("id")+", "+cdTree.get(i).get("name")+cdTree.get(i).get("pId"));
		}*/

		if (!noflush){
			String selid = request.getParameter("selid");
			if (StringUtils.isEmpty(selid)){
				selid = CL.TREE_ROOT_ID.toString();
			}
			m.put("dyc", _e.getCdList(Integer.valueOf(selid),0,CL.LIST_PP_DEF,CL.TREE_ROOT_ID));
			int recTotal = _e.cntCdList(Integer.valueOf(selid));
			m.put("recTotal", recTotal);
			m.put("pgTotal", (int)Math.ceil(recTotal/(double)CL.LIST_PP_DEF));
			m.put("pg", 1);
			m.put("perPage",CL.LIST_PP_DEF);
			HS.flushResponse(response, JSONObject.fromObject(m));
		}

		return m;
	}

	//根据pid和页数来查询菜单列表
	@RequestMapping(value = "getcdlistpg")
	public void getcdlistpg(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
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
			int recTotal = _e.cntCdList(npid);
			int pgTotal = (int)Math.ceil(recTotal/(double)CL.LIST_PP_DEF);
			if (!(npg>=1&& npg<=pgTotal)){
				m.put("ret","-1");
				m.put("recTotal",0);
				m.put("pgTotal",0);
			}else{
				m.put("ret","0");
				m.put("pg",pg);
				m.put("recTotal",recTotal);
				m.put("pgTotal",pgTotal);
				m.put("dyc", _e.getCdList(npid,(npg-1)*CL.LIST_PP_DEF,CL.LIST_PP_DEF, CL.TREE_ROOT_ID));
			}
			m.put("perPage",CL.LIST_PP_DEF);
		}
		HS.flushResponse(response, JSONObject.fromObject(m));
	}

	//获取菜单列表的最大排序，以自动填充编辑页的排序字段
	@RequestMapping(value = "getcdmaxpx")
	public void getcdmaxpx(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		HS.flushResponse(response, HS.vpx(_e, "tjpcms_caidan"));
	}

	//其实从这几个函数就能看出来，随便一个crud都特别麻烦，要写很多重复的东西，即使已经做了函数封装
	@RequestMapping(value = "cdaddedit")
	public void cdaddedit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (HT.isTestCjgly(request)){
			HS.flushResponse(response, "-1");
			return;
		}
		
		HS.flushResponse(response, HS.ppbc(_e, "tjpcms_caidan", request));
	}

	//编辑菜单时，先用同步的ajax从数据库查出来该菜单
	@RequestMapping(value = "getCdById")
	public void getCdById(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, ServletException, IOException {
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			Map<String, Object> obj = _e.obj("select t.* from tjpcms_caidan t where id='"+id+"'");
			HS.flushResponse(response, JSONObject.fromObject(obj));
			return;
		}

		HS.flushResponse(response, "-1");
	}

	//删除某个菜单
	@RequestMapping(value = "delCaidan")
	public void delCaidan(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		if (HT.isTestCjgly(request)){
			HS.flushResponse(response, "-1");
			return;
		}
		
		String id = request.getParameter("id");
		if (StringUtils.isEmpty(id) || _e.cnt("select count(*) from tjpcms_caidan where pId='"+id+"'")>0) {//有子菜单的不能删除
			HS.flushResponse(response, "-1");
			return;
		}
		
		//如果有用户拥有该菜单的权限，也不能删除
		if (StringUtils.isEmpty(id) || _e.cnt("select count(*) from tjpcms_caidan where pId='"+id+"'")>0) {
			HS.flushResponse(response, "");
			return;
		}

		HS.flushResponse(response, _e.del("delete from tjpcms_caidan where id='"+id+"'")==1?"0":"-1");
	}

	//角色和菜单都有了，就要有角色菜单了，也就是权限管理了
	//界面和角色管理基本一致，只是可以多设置权限，包括菜单和方法两个权限
	//设置权限这块，就不做成可配置的了，那样会侵入业务过多，这里就是自行前端解决
	@RequestMapping(value = "quanxian")
	public ModelAndView quanxian(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Crud o = new Crud(_e,request, "tjpcms_juese", "角色管理");
		o.getAev().setNeede(0).setHook_befad("jsglBefad").setHook_befgx("jsglBefgx").setNoaev();//不能新增系统预置的角色名，不能编辑任何角色名，可以编辑的话会有业务上的一致性问题的，所以直接不准编辑了吧，只准新增和删除
		o.getD().setHook_befdel("jsglBefdel").setNeed(false);//这个也是后台校验，以及删除的二次确认
		o.getR().getTree().setTb("tjpcms_caidan").setTitle("角色树").setTip("注：父子角色拥有相同栏目时，父角色默认具有子角色的数据查看和操作权限，也可以自定义权限。").setName("mc");//设置list页面tree树的相关数据
		o.getR().setJs_aft_layout("fn_qxgl_aftlo()").getExtjsp().add("extqxgl.jsp");//灰化按钮的，修正布局，引入extjs
		//o.getR().setPerPage(1);

		String hds[][]= {{"角色id","id"},{"父id","pId"},{"角色名称","mc"},
				{"直接子角色数","zjss","sql:select count(*) from tjpcms_juese s where s.pId=t.id"},
				{"角色用户数","jsyhs","sql:case when t.mc='超级管理员' then '1' else (select cast(count(*) as char) from tjpcms_usr s where s.jsid=t.id) end"},
				{"排序","px"},{"更新时间","gx"}};
		HS.setList(request, o,hds);//查询的配置

		return new ModelAndView("adm/list", null);
	}

	//动态获取某个角色的权限列表
	@RequestMapping(value = "aj_qxgl_getqx")
	public void aj_qxgl_getqx(HttpServletRequest request, HttpServletResponse response,   String cdlist) throws Exception {
		
		String jsid = request.getParameter("jsid");
		Map<String, Object> m = new HashMap<String, Object>(){{put("ret","0");}};
		if (StringUtils.isEmpty(jsid)) jsid = CL.TREE_ROOT_ID.toString();
		Map<String, Object> obj = _e.obj("select id,mc from tjpcms_juese where id = '"+jsid+"'");
		m.put("jsid", obj.get("id"));
		m.put("jsm", obj.get("mc"));
		
		//把权限的菜单列表查出来
		 m.put("cds", getcdtree(request, response, true));
		
		//这里把htglcontroller里所有的方法查出来，以设置权限，这是我目前想到的最好的权限粒度，方法级
		List<Map<String, Object>> r = _e.r("select ff from tjpcms_qx_fangfa where jsid='"+jsid+"'");
		List<Map<String, String>> ffs = new ArrayList<Map<String, String>>();
		Map<RequestMappingInfo, HandlerMethod>  map =  this.handlerMapping.getHandlerMethods();
        for(RequestMappingInfo info : map.keySet()){ 
        	PatternsRequestCondition pc = info.getPatternsCondition();
        	final String ptn = "[/"+XT.htgllj;
        	if (pc!=null && pc.toString().startsWith(ptn)){//是后台管理的controller，目前只配置这个权限，其他的如果需要也可以配
        		Map<String, String> e = new HashMap<String, String>();
        		e.put("ffm", map.get(info).getMethod().getName());
        		e.put("uri", pc.toString().substring(pc.toString().indexOf(ptn)+ptn.length()+1, pc.toString().length()-1));
        		for (Map<String, Object> map2 : r) {
					if (map2.get("ff").toString().equals(e.get("ffm"))){
						e.put("sel", "1");
						break;
					}
				}
        		ffs.add(e);
        	}
        }
        m.put("ffs", ffs);

		HS.flushResponse(response, JSONObject.fromObject(m));
	}


	//保存权限
	//博客1、木叶之荣 JSON数组形式字符串转换为Map数组（转为其他的Bean的话，请参考自行变形）https://blog.csdn.net/zknxx/article/details/52281699
	//博客2、ruoxuan25的博客 ajax从JSP传递对象数组到后台 https://blog.csdn.net/ruoxuan25/article/details/77530928
	//这里遇到个技术上的小问题，就是我想把对象数组传递到后台，当然其实不需要，因为我只要传一个id就行了，也就不需要传对象了，如果是只传id数组，前台的写法不一样，因为没有对象嘛
	//而我这里还是用对象来处理，因为我觉得这样可以包容只传id的那种方式，也就是说，如果我可以传对象数组回来的话，那当我想只传id的话，只要设置对象的属性只有一个id就行了
	//博客2中的方法其实已经可以解决我的问题了，但是他那个方法里需要做一个pojo类，我连pojo类都不想要，就像我这个系统里mybatis也没有做pojo类一样
	//所以我又找到博客1，他用到了一个fastjson，其实，问题是很明确的，就是我向后台传回了一个json格式的字符串，我想把这个字符串转成一个list，然后的list的类型我不想用bean或者
	//叫pojo类，我想用list<map>，所以就得用解析json的jar包啊，net.sf.json.JSONArray的功能我怎么感觉比较有限啊，下次还是直接用fastjson吧
	//之所以不想用bean，是因为mybatis里我就基本没用model，所以我也不想因为这个小问题就弄一个model。如果是用model来处理net.sf.json.JSONArray就可以解决掉了
	@RequestMapping(value = "bcquanxian")
	@ResponseBody
	@Transactional
	public void bcquanxian(HttpServletRequest request, HttpServletResponse response,   String cdlist) throws InterruptedException {
		
		//检查角色是否为空及是否存在和唯一
		String jsid = request.getParameter("jsid");
		if (StringUtils.isEmpty(jsid) || HT.isTestCjgly(request)){
			HS.flushResponse(response, "-1");
			return;
		}
		int cnt = _e.cnt("select count(*) from tjpcms_juese where id='"+jsid+"'");
		if (cnt !=1){
			HS.flushResponse(response, "请检查该角色名是否存在且唯一");
			return;
		}

		_e.del("delete from tjpcms_qx_caidan where jsid="+jsid);
		List<Map<String,String>> cds = (List<Map<String,String>>)com.alibaba.fastjson.JSONArray.parse(cdlist);
		if (CollectionUtils.isNotEmpty(cds)) {
			int px = 0;
	        for(Map<String,String> mapList : cds){//遍历传回到后台的菜单
	            for (Object entry : ((Map)mapList).entrySet()){
	               Object v = ((Map.Entry)entry).getValue();
	               if (v != null){
	            	   String cdid = v.toString();
	            	   _e.add("insert into tjpcms_qx_caidan(jsid,cdid,px,gx) values('"+jsid+"','"+cdid+"','"+(px++)+"','"+CL.fmt.format(new Date())+"') ");
	               }
	            }
	        } 
		}

		//保存方法
		String fflist = request.getParameter("fflist");
		_e.del("delete from tjpcms_qx_fangfa where jsid="+jsid);
		if (StringUtils.isNotEmpty(fflist)){
			String[] split = fflist.split(",");
			if (split!=null){
				for (int i = 0; i < split.length; i++) {
					_e.add("insert into tjpcms_qx_fangfa(jsid,ff,gx) values('"+jsid+"','"+split[i].trim()+"','"+CL.fmt.format(new Date())+"') ");
				}
			}
		}
		

		HS.flushResponse(response, "0");
	}

	//角色，菜单，权限都有了，最后就是用户角色了
	//这里要小展现一下tjpcms的能力，我这里有这样一个需求，就是我tjpcms_usr表里的id字段，是varchar类型，并不是最常用的int类型，这个id兼顾了三个功能
	//1、它是作为表的主键的
	//2、字段值开头是以类型GR，QQ，SN之类开头的，也就是说前2个字母代表了这个用户的注册类型，当然了，我现在又需要可以分配一些用户到指定的角色了，那这里准确的说就不能叫注册类型了，应该叫来源类型
	//3、来源类型后的内容才是用户的用户名
	//这样设计呢，有历史原因，正常的话应当是id为int自增长类型，再设两个字段，一个存角色，一个存来源，就是说这个用户首先他是什么角色，其次这个角色是怎么来的，是自己注册的，还是第三方登录的，还是分配的等等
	//那既然已经设计成这样了，特别是涉及到了第三方登录，自然不好轻易修改这个设计，那就在这个基础上来实现我所需要的功能
	//第一个：就是我需要查询和查看用户名，我当然不能直接把id显示出来，我要截取一下，这里就有三种方法可以实现(1)sql(2)extzd(3)hook_aftcx，其实都不难，用过一次就知道了，以后我会在视频里具体讲一下
	//第二个：查询区和列表区把角色都显示出来，并且是显示文字。稍微麻烦点的是查询区的角色，不是来源于字典表的，就不可以写sel:zdb了，这里增加了一个写法sel:sql
	//第三个：我要根据来源来查用户，但是我并没有来源这个字段，甚至没有来源这个字典表。字典表来源：(1)sel(2)zdb，根据来源查用户。查询来源：还是(1)sql(2)extzd(3)hook_aftcx
	//			另外，如果是第三方登录的话，我在来源后增加（...）来填写其具体是哪个第三方，如果需要也可以在查询区实现下拉框的联动查询
	@RequestMapping(value = "yonghu")
	public ModelAndView yonghu(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e,request, "tjpcms_usr", "用户管理");
		o.getR().getExtzdm().add("juti");
		o.getR().getExtzdstr().add("case when left(id,3)='QQ_' then 'QQ' when left(id,3)='SN_' then '新浪' when left(id,3)='BD_' then '百度' else '' end");
		o.getR().setHook_aftcx("com.tjpcms.common.Hook.yhglAftCx");//把来源修正一下
		o.getR().getExtjs().add("extlistjs.js");//这个是为了二次确认删除
		o.getD().setZdyQrAft("fn_erci(ids)");
		o.getD().setBatdel(true);
		//o.getR().setJubu();

		String cxs[][]= {{"用户名","yhm"},
				{"角色","jsid","tree:select id,pId,mc name from tjpcms_juese where mc!='超级管理员' "},//新的配置写法，原则就是能省则省，所以直接tree:xxxxxx
				{"来源","ly","sel:后台分配,  	前台注册,第三方登录"},{"昵称","nc"},{"更新时间","gx"}};
		String hds[][]= {
			{"用户名","yhm","sql:select mid(id,4) from tjpcms_usr where id=t.id"},
			{"角色","js","sql:select s.mc from tjpcms_juese s where s.id=t.jsid"},
			{"来源","ly","sql:case when left(id,3)='GR_' then '前台注册' when left(id,3)='FP_' then '后台分配' else '第三方登录' end","ext:juti::"},//juti的值就是getExtzdstr查出来的
			{"昵称","nc"},{"头像","tx","pic:无"},{"更新时间","gx"}};
		String czs[][]={{"text:重置密码","js:fn_yhgl_czmm(id,this)"}};
		HS.setList(request, o,hds, cxs,czs);//查询的配置

		//新增用户，即后台分配的用户
		//查看和编辑时，不需要有密码和确认密码，编辑时不能编辑用户名
		o.getAev().setTip("注：为用户设置某个角色后，该用户将拥有指定角色的全部菜单权限");
		o.getAev().setHook_befad("befadYhgl");//新增之前增加ip字段及id字段
		//其实这里还应该有一个befgx，就是在正式更新前要把表单里密码和角色值去掉，因为在extaevjs里有前台的这段逻辑，但前台是不可靠的
		o.getAev().getExtjs().add("extaevjs.js?s=yhgl");//带一个参数yonghu，这样即使用的都是同一个extjs，也可以区分了，就是$(function(){})这种初始化代码里做些额外操作，不能使得其他包含了该extjs的文件也做这些额外操作
		String aev[][]= {
				{"用户名","yhm","required unique"},
				{"密码","mm","required  regexp#^[a-zA-Z0-9]{6,12}$:请输入6-12位的密码  not_have_kg:密码不能含有空格",Aevzd.PASSWORD.toString(), "phd:请输入6-12位的密码（不能含有空格）"},
				{"确认密码","qrmm","required repeat#mm:两次输入的密码不一致",Aevzd.PASSWORD.toString(),"phd:请再次重复输入密码（不能含有空格）"},
				{"角色","jsid",Aevzd.TREE.toString(),"required:请选择角色！"," sQl 	: select id,pId,mc name from tjpcms_juese where mc!='超级管理员' and mc!='前台注册用户'"},//不必严格sql:，可以有空白符和大小写混杂
				{"昵称","nc", "length#<=21:长度必须小于21（汉字长度为3）  length#>=6:长度必须大于6（汉字长度为3）"},
				{"头像","tx",Aevzd.PIC.toString(),"\\"+CL.DEF_TX}//配置一个默认头像
		};
		HS.setAev(request, o,aev);//aev的配置

		return new ModelAndView("adm/list", null);
	}

	//用户管理重置密码，默认就是重置为123456
	@RequestMapping(value = "aj_yhgl_czmm")
	public void aj_yhgl_czmm(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		
		if (HT.isTestCjgly(request)){
			HS.flushResponse(response, "-1");
			return;
		}

		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			Mademd5 mad=new Mademd5();
			int upd = _e.upd("update tjpcms_usr set mm='"+mad.toMd5(mad.toMd5("123456"), id)+"' where id='"+id+"'");
			HS.flushResponse(response, upd==1?"0":"-1");
			return ;
		}

		HS.flushResponse(response, "-1");
	}
	
	//审核流程的配置
	//这里其实有一个什么点呢，就是从我个人的用户体验而言，在列表页如果需要展示其他页面的内容，在内容不巨多的情况下，弹出层会比页面跳转更友好一点，遮罩层的存在明确提示了用户那个
	//列表页已经被覆盖了，现在你的注意力应该集中在弹出层了，但是用户依然可以看到列表页的部分内容，甚至可以移动弹出层，就会有一种掌控了这个页面的感觉。等关闭了提示层后，立刻就可以回到
	//列表页，就会比较明确，等于你引导了用户的注意力。当然肯定也并不是所有的情况都是弹出层更好，我能想到的比如数据量非常大的情况下，因为弹出层毕竟区域小，这时可能就不合适了。
	//但是如果弹出层覆盖了弹出层，我感觉还是有点乱了吧，可能一个弹出层就差不多了，再多了感觉会让我有点抓狂
	@RequestMapping(value = "liucheng")
	public ModelAndView liucheng(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Crud o = new Crud(_e,request, "tjpcms_liucheng", "流程管理");
		o.getAev().setNeede(false);
		o.getD().setBatdel();
		o.getR().setJs_aft_sx("fn_lc_aftsx()").setHook_aftcx("lcglAftCx").setJubu();//

		String cxs[][]= {{"流程名称","mc"}};
		String hds[][]= {{"id","id"},{"流程名称","mc"},
			{"流程总数","lczs","sql:select floor(ifnull((length(jsids)-length(replace(jsids,'→','')))/length('→')+1,0)) from tjpcms_liucheng s where s.id=t.id","style:width:5em"},
			{"角色流程id","jsids"}, {"角色流程名称","jsmcs"}, 
			{"流程栏目数","lclms","sql:select count(*) from tjpcms_lanmu s where s.lcid=t.id"},
			{"备注","bz"}, {"排序","px"}};
		//String czs[][]={{"text:配置流程","href:pzlc.dhtml?mc=&id="}};
		String czs[][]={{"text:gray:配置角色:case:lclms>0#case:lclms=0:val:配置角色","js:fn_pzjuese(id,mc,lclms):listczq.js"}};
		HS.setList(request, o,hds, cxs,czs);//查询的配置

		String aev[][]= {
			{"流程名称","mc","required unique"},
			{"排序","px"},
			{"备注","bz",Aevzd.TEXTAREA.toString()},
		};
		HS.setAev(request, o,aev);//aev的配置

		return new ModelAndView("adm/list", null);
	}

	//这里我故意搞了个临时表，因为tjpcms_liucheng这个表里的jsids字段是逗号分隔的，而不是存在子表里的，所以就不能像zdn，zdx那样了
	//实际上这里如果已经有栏目采用了该流程了，就不可以进来配置角色了，但是流程这个东西业务性太强，所以我就没必要搞得那么细了，因为都是无用功，流程的需求是没办法用页面点击输入这种来定义出来的
	//定义了临时表，还得设置listtblzd，我去。。稍微有点麻烦，我感觉都可以直接弄正式表了，用完再删了
	//这里页面改成用局部刷新
	//和普通的crud区别就是多了个自定义下拉框在按钮区，另外排序是在列表页修改的
	@RequestMapping(value = "pzlc")
	public ModelAndView pzlc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String lcid = request.getParameter("id");//流程ID
		if (StringUtils.isEmpty(lcid)) {
			return null;
		}
		Map<String, Object> obj = _e.obj("select * from tjpcms_liucheng where id="+lcid);
		if (obj==null) {
			return null;
		}

		Object object = obj.get("jsids");
		String[] jss = null;
		if (object!=null){
			jss = object.toString().split("→");
		}
		//创建临时表，如果不存在才需要创建
		String s = "create temporary table if not exists tmp_lcjs(";
		s += " id int(11) unsigned not null auto_increment, ";
		s += " jsid varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , ";
		s += " px  int(11) UNSIGNED NOT NULL DEFAULT 1, ";
		s += " PRIMARY KEY (id))";
		_e.r(s);
		_e.del("delete from tmp_lcjs");
		if (jss!=null && jss.length>0){
			for (int i = 0; i < jss.length; i++) {
				if (StringUtils.isNotEmpty(jss[i])) {
					_e.add("insert into tmp_lcjs(jsid,px) values('"+jss[i]+"',"+(i+1)+")");
				}
			}
		}
		List<Map<String, Object>> lsttbZd = new ArrayList<Map<String, Object>>();//因为crud里要用到lsttbZd
		Map<String, Object> e1 = new HashMap<String, Object>();
		e1.put("COLUMN_NAME", "id");
		e1.put("extra", "auto_increment");
		e1.put("data_type", "int");
		e1.put("COLUMN_KEY", "PRI");
		lsttbZd.add(e1);
		Map<String, Object> e2 = new HashMap<String, Object>();
		e2.put("COLUMN_NAME", "jsid");
		e2.put("data_type", "varchar");
		e2.put("CHARACTER_MAXIMUM_LENGTH", "200");
		lsttbZd.add(e2);
		Map<String, Object> e3 = new HashMap<String, Object>();
		e3.put("COLUMN_NAME", "px");
		e3.put("data_type", "int");
		lsttbZd.add(e3);
		
		//上面就是创建临时表，这样就可以用crud这一套了，就方便了
		Crud o = new Crud(_e,request, "tmp_lcjs", null);
		o.setOses(lcid).setListtblzd(lsttbZd).getR().setPerPage(20).setJubu().setOdrby("order   by px asc").setJs_aft_sx("fn_pzlc_jsaftsx()").setInc_ztree();//当然其实asc可以不写    存session，删除流程角色的时候要用到lcid
		o.getAev().setNoaev();
		o.getD().setHook_aftdel("aftdelPzlc").setBatdel(true);
		//排序可以直接在列上面改，写到listHds吧
		request.setAttribute("lcid", lcid);
		request.setAttribute("srcfrom", "pzlc");
		request.setAttribute("jstree", JSONArray.fromObject(_e.r("select id,pId,mc name from tjpcms_juese t order by t.px desc,t.gx desc,t.rq desc,t.id desc")));//把系统的角色查出来

		String cxs[][]= {{"角色名称","js"}};
		String hds[][]= {{"角色id","jsid"},{"角色名称","jsmc","sql:select mc from tjpcms_juese s where s.id=t.jsid"}, {"排序","px"}};
		String ans[][]= {{"新增","js:fn_pzlc_xinzeng(this,"+lcid+"):listanq.jsp", "idx:0"}};
		HS.setList(request, o,hds, cxs,null, ans);
	
		return new ModelAndView("adm/list", null);
	}
	
	//说实在的，很多来不及细细研究，比如这个鬼@Transactional，具体到代码级别是做了什么，不清楚，有没有用，不清楚，怎么测试，不太会
	//比如那个display:table-cell，垂直居中的神器，但是原理完全没搞清
	@RequestMapping(value = "aj_pzlc_addjs")
	@Transactional
	public void aj_pzlc_addjs(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {

		String id = request.getParameter("id");
		String lcid = request.getParameter("lcid");
		if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(lcid)) {
			Map<String, Object> obj1 = _e.obj("select mc from tjpcms_juese where (id="+id+")");
			if (obj1!=null){
				int vpx = HS.vpx(_e,"tmp_lcjs");//临时表无法reopen，只能先查出来
				int add = _e.add("insert into tmp_lcjs(jsid,px) values('"+id+"', "+vpx+")");
				if (add==1){
					int upd = _e.upd("update tjpcms_liucheng set jsids= (select group_concat(jsid separator '→') jsids from tmp_lcjs order by px limit 1) where (id="+lcid+")");
					HS.flushResponse(response, (upd==1?"0":"-1"));
				}
			}
		}

		HS.flushResponse(response, "-1");
	}
	
	//配置流程 - 修改排序
	@RequestMapping(value = "aj_pzlc_xgpx")
	public void aj_pzlc_xgpx(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		
		String id = request.getParameter("id");
		String lcid = request.getParameter("lcid");
		String px = request.getParameter("px");
		if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(lcid) && StringUtils.isNotEmpty(px)) {
			int upd = _e.upd("update tmp_lcjs set px="+px+" where id="+id);
			if (upd==1){
				upd = _e.upd("update tjpcms_liucheng set jsids= (select group_concat(jsid separator '→') jsids from (select * from tmp_lcjs order by px)tt  limit 1) where (id="+lcid+")");
				HS.flushResponse(response, (upd==1?"0":"-1"));
			}
		}
		
		HS.flushResponse(response, "-1");
	}
	
	
	
	
}





