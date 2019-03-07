package com.tjpcms.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.ndktools.javamd5.Mademd5;
import com.tjpcms.common.Crud.Crud;
import com.tjpcms.spring.model.Usrpt;
import com.tjpcms.utils.NetworkUtil;


//para1:request, para2:o, para3:pa
public class Hook {
	
	//保存字典项中的栏目名称时，要把栏目表中lx为此名称的栏目也一并更新了
	public String lmmcBaocunbef(Object para1, Object para2, Object para3, Object beiyong){
		HttpServletRequest request = (HttpServletRequest)para1;
		Crud o = (Crud)para2;
		
		Map<String, Object> obj = HS.obj(o.getMp(), "tjpcms_zdx", request.getParameter("id"));
		if (obj !=null){
			String oldmc = (String)obj.get("zdxmc");
			String newmc = request.getParameter("zdxmc");
			if (StringUtils.isNotEmpty(oldmc) && StringUtils.isNotEmpty(newmc)){
				if (!oldmc.equals(newmc)) {//字典项有了新的名称，那就需要
					o.getMp().upd("update tjpcms_lanmu set lx='"+newmc+"' where lx='"+oldmc+"'");
				}
				return "0";//名称相同就不需要做额外的更新操作了
			}
		}

		return "-1";
	}

	//这个是删除栏目类型中的某个字典项时，该栏目不能已被栏目列表中某个栏目引用
	public String befDelZdx(Object para1, Object para2, Object para3, Object beiyong){
		HttpServletRequest request = (HttpServletRequest)para1;
		Crud o = (Crud)para2;

		String arr[] = request.getParameter("ids").split(",");
		String s = "select count(*) from tjpcms_lanmu t where t.lx in (select zdxmc from tjpcms_zdx t1 left join tjpcms_zdb t2 on t1.pId=t2.id where t2.py='lanmuleixing' ";
		s += " and t1.id in"+HS.arr2instr(arr)+" )";
		int cnt = o.getMp().cnt(s);

		return cnt>0?"删除失败！请先在【栏目列表】中将使用了该类型的 "+cnt+" 个栏目"+(cnt>1?"全部":"")+"删除，才能删除该类型！":"0";
	}

	//普通用户修改头像前执行这个，防止第三方登录的用户修改头像
	public String befCmnUsrTxnc(Object para1, Object para2, Object para3, Object beiyong){
		HttpServletRequest request = (HttpServletRequest)para1;

		if (HT.isCjgly(request) || HT.isDsfUsrpt(request)){
			return "管理员以及第三方登录的用户无法修改密码、头像和昵称";
		}

		Map<String, String[]> pa = (Map<String, String[]>)para3;
		if (pa !=null && pa.get("nc") !=null){
			String nc = pa.get("nc")[0];
			if (StringUtils.isEmpty(nc) || Pattern.compile("\\s+").matcher(nc).find() || nc.length() >15 || nc.length()<2) {
				return "昵称长度范围需为2-15位，不能含有空格";
			}else{
				pa.put("id", new String[]{HT.getUid(request)});
				return "0";
			}
		}else if (pa !=null && pa.get("tx") !=null){
			String tx = pa.get("tx")[0];
			if (StringUtils.isEmpty(tx)) {
				return "请上传头像！";
			}else{
				pa.put("id", new String[]{HT.getUid(request)});
				return "0";
			}
		}

		return "-1";
	}

	//用户修改自己的头像昵称后，session要更新一下
	public Map<String,Object> aftCmnUsrTxnc(Object para1, Object para2, Object para3, Object beiyong){
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("ret", "-1");
		HttpServletRequest request = (HttpServletRequest)para1;
		Crud o= (Crud)para2;
		Usrpt so = HT.getUsrpt(request);
		Map<String, Object> obj = o.getMp().obj("select nc,tx from tjpcms_usr where id='"+so.getId()+"'");//也可以不查出来，自己拼，这样就是方便点，不怕业务方便有修改
		if (obj!=null){
			so.setNc((String)obj.get("nc"));
			so.setTx((String)obj.get("tx"));
			request.getSession().setAttribute(CL.ses_usrpt, so);
			rtn.put("ret", "0");
			rtn.put("tx", so.getTx());
			rtn.put("nc", so.getNc());
		}

		return rtn;
	}
	
	//这个接口是查询出列表数据后可以对其进行一些业务上的数据处理，如0-59改为不合格
	//这边只是测试一下这个功能，我这边的业务上来说没太大实际作用，其他人可能需要
	//这边我重新改了一下，如果是普通用户，非管理员的那种，我直接不在前台显示UId这个字段了，因为普通用户只能看到自己的数据，所以这个字段是多余的，当然
	//其实简单一点的做法是把uid的前三位去掉，只显示用户名，不带前缀
	//th:列表头数据，tb:列表数据，还有个zds：这个是除去多选，序号和操作3列的有效数据列
	//其实昵称也可以不要，其实也可以采取前端的方式来解决，还更简单点
	//2018-08-13：以前是普通用户只能看自己的，那就也不需要看uid这个字段了，现在引入了审核流程，普通用户也可能要审核他人的记录，那么uid就必须要保留了，所以这块就注释掉了，但这块还是有用的，说不定其他地方会有这个需求
	public List<Map<String, Object>> tywzAftCx(Object para1, Object para2, Object para3, Object beiyong){
		Crud o= (Crud)para2;
		
/*		HttpServletRequest request = (HttpServletRequest)para1;
		if (!Ht.isCjgly(request)){
			for (int i=0;i<o.getR().getThs().size();i++){
				Object object = o.getR().getThs().get(i).get("zdm");
				if (object!=null && "uid".equalsIgnoreCase(object.toString())){
					o.getR().getThs().remove(i);
					break;
				}
			}
			for (int i = 0; i < o.getR().getZds().size(); i++) {
				Object object = o.getR().getZds().get(i).get("zdm");
				if (object!=null && "uid".equalsIgnoreCase(object.toString())){
					o.getR().getZds().remove(i);
					break;
				}
			}
			for (int j = 0; j < o.getR().getTbs().size(); j++) {
				o.getR().getTbs().get(j).remove("uid");
			}
			//尼玛还要把查询区的也干掉
			for (int i = 0; i < o.getR().getCxs().size(); i++) {
				Map<String, Object> map = o.getR().getCxs().get(i);
				if (map.get("zdm")!=null && "uid".equalsIgnoreCase(map.get("zdm").toString())){
					o.getR().getCxs().remove(i);
					break;
				}
			}
		}*/
		
		return o.getR().getTbs();
	}

	//把流程管理里按jsid显示的流程改为按角色名称显示
	//那你为什么不直接存储角色名称呢？因为我觉得存id更容易减少bug一点，存名称我感觉可能会引起问题
	public List<Map<String, Object>> lcglAftCx(Object para1, Object para2, Object para3, Object beiyong){
		Crud o= (Crud)para2;
		HttpServletRequest request = (HttpServletRequest)para1;

		if (CollectionUtils.isNotEmpty(o.getR().getTbs())) {
			for (Map<String, Object> e: o.getR().getTbs()){
				Object jsids = e.get("jsids");
				if (jsids!=null){
					String[] split = jsids.toString().split("→");
					if (split!=null && split.length>0){//这个尴尬了，id必须逐个取出来查名称再组成→
						List<String> ret = new ArrayList<String>();
						for (int i = 0; i < split.length; i++) {
							ret.add(o.getMp().obj("select mc from tjpcms_juese where id="+split[i]).get("mc").toString());
						}
						e.put("jsmcs", StringUtils.join(ret,"→"));
					}
				}
			}
		}
		
		return o.getR().getTbs();
	}
	
	//修正一下用户管理里的第三方登录的来源
	public List<Map<String, Object>> yhglAftCx(Object para1, Object para2, Object para3, Object beiyong){
		Crud o= (Crud)para2;
		
		List<Map<String, Object>> tbs = o.getR().getTbs();
		for (Map<String, Object> map : tbs) {
			String juti = (String)map.get("juti");
			if (StringUtils.isNotEmpty(juti)) {
				map.put("juti", "（"+juti+"）");
			}
		}

		return tbs;
	}

	//用户管理：在新增保存之前把数据重新处理一下
	public String befadYhgl(Object para1, Object para2, Object para3, Object beiyong) throws IOException{
		Map<String, String[]> pa = (Map<String, String[]>)para3;//所有提交的参数在这个map里
		if (pa ==null || pa.get("yhm") ==null || StringUtils.isEmpty(pa.get("yhm")[0]) || StringUtils.isEmpty(pa.get("mm")[0])) return "-1";

		String id = "FP_"+pa.get("yhm")[0];
		pa.put("id", new String[]{id});//修正一下ID
		pa.put("ip", new String[]{NetworkUtil.getIpAddress((HttpServletRequest)para1)});//增加一下ip

		Mademd5 mad=new Mademd5();
		String mm = pa.get("mm")[0];
		pa.put("mm",  new String[]{mad.toMd5(mad.toMd5(mm), id)});

		return "0";
	}

	//角色管理，演示一下，如何在后台校验
	public String jsglBefad(Object para1, Object para2, Object para3, Object beiyong) throws IOException{
		Map<String, String[]> pa = (Map<String, String[]>)para3;//所有提交的参数在这个map里
		if (pa ==null || pa.get("mc") ==null || StringUtils.isEmpty(pa.get("mc")[0])) {
			return "-1";
		}
		if ("超级管理员".equals(pa.get("mc")[0]) || "前台注册用户".equals(pa.get("mc")[0])){
			return pa.get("mc")[0]+"为系统内置角色，不可新增、编辑或删除！";
		}

		return "0";
	}
	//角色管理的更新功能直接去掉了
	public String jsglBefgx(Object para1, Object para2, Object para3, Object beiyong) throws IOException{
		return "-1";
	}

	//角色管理，演示一下，如何在后台拦截住删除，在这里进行校验，通过了才删除，甚至可以自定义删除的url
	public String jsglBefdel(Object para1, Object para2, Object para3, Object beiyong){
		Map<String, String[]> pa = (Map<String, String[]>)para3;//所有提交的参数在这个map里
		if (pa ==null || pa.get("ids") ==null || StringUtils.isEmpty(pa.get("ids")[0])) {
			return "-1";
		}

		Crud o= (Crud)para2;
		Map<String, Object> obj = o.getMp().obj("select mc from tjpcms_juese where id="+pa.get("ids")[0]);
		if (obj==null || "超级管理员".equals(obj.get("mc")) || "前台注册用户".equals(obj.get("mc"))){
			return obj.get("mc")+"为系统内置角色，不可新增、编辑或删除！";
		}

		int cnt = o.getMp().cnt("select count(*) from tjpcms_qx_caidan s where s.jsid="+pa.get("ids")[0]+" and s.cdid in (select id from tjpcms_caidan x where x.pId="+CL.TREE_ROOT_ID+")");
		if (cnt>0){
			return "要删除该角色，请先删除该角色用有的菜单及拥有该角色的用户！";
		}
		cnt = o.getMp().cnt("select count(*) from tjpcms_usr s where s.jsid="+pa.get("ids")[0]);
		if (cnt>0){
			return "要删除该角色，请先删除该角色用有的菜单及拥有该角色的用户！";
		}
		
		//现在多了工作流，还要再校验是否已经被某个工作流引用了select * from tjpcms_liucheng where find_in_set('超级管理',replace(jsmcs,"→",","));
		//算了，前台弄吧

		return "0";
	}
	
	//临时表不支持下面的写法，reopen了，只能先查出tmp_lcjs中的数据量，再update
	//update tjpcms_liucheng set jsmcs= (select case when (select count(*) from tmp_lcjs)>0 then group_concat(js separator '→') else null end jsmcs from tmp_lcjs order by px limit 1) where xxx
	public String aftdelPzlc(Object para1, Object para2, Object para3, Object beiyong){
		Crud o = (Crud)para2;

		String lcid  = (String)o.getOses();
		int cnt = o.getMp().cnt("select count(*) from tmp_lcjs");
		String jsids = (cnt==0?" null ":"(select group_concat(jsid separator '→')  from tmp_lcjs order by px limit 1)");
		int upd = o.getMp().upd("update tjpcms_liucheng set jsids= "+jsids+" where (id="+lcid+")");
		
		return (upd==1?"0":"-1");
	}
	
	//这里有个比较坑爹的问题
	//本来tjpcms_liucheng这个表里，我是存的jsmcs，即类似部门员工→部门主任这种格式的数据，但我后来觉得存名称还是不好，因为tjpcms_juese表中当然有可能删掉某个角色再重新起个名称，那这样不是有点逻辑隐患吗
	//所以我还是改成了id来存储，即136→87这种格式。但是这样就带来了显示的问题，我在页面上看流程时，肯定要看名称啊。问题是sql里还不好直接把id翻译成名称，只能在代码里查了
	public List<Map<String, Object>> ___cmnsh_aftcx(Object para1, Object para2, Object para3, Object beiyong){
		Crud o= (Crud)para2;
		HttpServletRequest request = (HttpServletRequest)para1;
		
		if (CollectionUtils.isNotEmpty(o.getR().getTbs())) {
			Map<String, Object> mpmcs = new HashMap<String, Object>();
			for (Map<String, Object> e: o.getR().getTbs()){
				//修正一下审核流程(阶段)，主要是把shjd数字加上去，这样可以看清楚审核流程流转到哪个阶段了
				Object shzt = e.get("shzt");
				if (shzt!=null && "待审核".equals(shzt.toString()) && e.get("shjd")!=null){
					e.put("___shlcmc", e.get("___shlcmc")+"("+e.get("shjd")+")");
				}else if (shzt!=null &&  "审核通过".equals(shzt.toString())){
					e.put("___shlcmc", e.get("___shlcmc")+"(完结)");
				}

				Object jsids = e.get("___lcjsids");
				if (jsids==null || StringUtils.isEmpty(jsids.toString())) continue;
					
				if (mpmcs.get(jsids.toString())==null){
					String[] split = jsids.toString().split("→");
					if (split!=null && split.length>0){
						List<String> ret = new ArrayList<String>();
						for (int i = 0; i < split.length; i++) {
							ret.add(o.getMp().obj("select concat(mc,'(',"+(i+1)+",')')mc from tjpcms_juese where id="+split[i]).get("mc").toString());
						}
						mpmcs.put(jsids.toString(), StringUtils.join(ret,"→"));
						e.put("___title____shlcmc", mpmcs.get(jsids.toString()));
					}
				}else{
					e.put("___title____shlcmc", mpmcs.get(jsids.toString()));
				}
			}
		}
		
		return o.getR().getTbs();
	}
	
	
	
	
	//演示自定义审核流程的第一种：区市省三级审核
	public String zdysqb_befadgx(Object para1, Object para2, Object para3, Object beiyong){
		HttpServletRequest request = (HttpServletRequest)para1;

		final String ___zc = request.getParameter("___zc");
		final boolean iszc = StringUtils.isNotEmpty(___zc);//是否是暂存，如果是暂存不需要进入钩子
		
		Map<String, String[]> pa = (Map<String, String[]>)para3;
		if (!iszc){
			pa.put("shzt", new String[]{"待审核"});
			pa.put("shjd", new String[]{"2"});//企业（1）→区（2）→市（3）→省（4）
		}
		
		return "0";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}















