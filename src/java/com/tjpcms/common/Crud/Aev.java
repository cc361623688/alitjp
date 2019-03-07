package com.tjpcms.common.Crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class Aev {
	String title;
	String hdnpara;
	List<Map<String,Object>> zds = new ArrayList<Map<String,Object>>();
	
	boolean needa = true;
	boolean neede = true;
	boolean needv = true;
	String zdybc;//自定义新增、编辑页面的保存url，即自定义保存，注意要回返回JSON格式，可参考Hanshu中addedit或cmnusr中aj_xgmm的写法。现在引入了___zc参数，代表是否是暂存
	List<String> extjs = new ArrayList<String>();//额外的js，这个是可以额外引入的js文件，可以带参数
	List<String> extjsp = new ArrayList<String>();//还可以带额外的jsp，也可以带参数了
	
	String dynaev;//动态设置aev，如申请入会（单位或个人申请表不同）的情况，也是个hook，只不过比较早，我当时还没把这些钩子统一命名为hook开头
	String hook_befgx;//保存时切入一个钩子，在更新之前执行
	String hook_aftgx;//保存时切入一个钩子，在更新之后执行
	
	String hook_befad;//保存时切入一个钩子，在新增之前执行，返回"0"表示正确，其他表示错误，会中断后续的保存
	String hook_aftad;
	
	String tip;//页面提示信息

	boolean noBtns = false;//可以设置编辑页面直接不要按钮，当然后面aev页面的按钮也要做成可以自定义的
	
	String js_aft_bc;//设置在后台保存之后前台需要执行的额外js代码
	
	//aev.jsp中fn_bc中系统保存校验之前执行用户自定义的校验，这样无法用正则的校验就可以在这里写js来校验了，数组是为了防止有多个校验函数在不同的时机加入，只要在页面渲染前加入都会去执行校验
	List<String> js_aft_zdyjy = new ArrayList<String>();
	List<String> js_bef_zdyjy = new ArrayList<String>();
	
	boolean canZc = false;//能否暂存，注意这个不是设置项，而是计算项。设置aev时，根据逻辑计算出来的

	
	
	//审核流程为自定义时，如果你还需要系统自带的审核弹出层前端及相关前后台逻辑，这里给个钩子来自定义最终的shzt和shjd字段值，由你自行的逻辑来设置
	String hkzdy_shenhe;
	
	
	
	public String getHkzdy_shenhe() {
		return hkzdy_shenhe;
	}

	public Aev setHkzdy_shenhe(String hkzdy_shenhe) {
		
		if (StringUtils.isNotEmpty(hkzdy_shenhe)) {
			hkzdy_shenhe = hkzdy_shenhe.trim();
			if (!hkzdy_shenhe.contains(".")){
				hkzdy_shenhe = "com.tjpcms.common.Hkzdy."+hkzdy_shenhe;
			}
		}

		this.hkzdy_shenhe = hkzdy_shenhe;
		
		return this;
	}

	public boolean isCanZc() {
		return canZc;
	}

	public void setCanZc(boolean canZc) {
		this.canZc = canZc;
	}

	public List<String> getExtjsp() {
		return extjsp;
	}

	public void setExtjsp(List<String> extjsp) {
		this.extjsp = extjsp;
	}

	public void setExtjs(List<String> extjs) {
		this.extjs = extjs;
	}

	public List<String> getExtjs() {
		return extjs;
	}

	public boolean isNoBtns() {
		return noBtns;
	}

	public void setNoBtns(boolean noBtns) {
		this.noBtns = noBtns;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getHook_befad() {
		return hook_befad;
	}

	public Aev setHook_befad(String hook_befad) {
		if (StringUtils.isNotEmpty(hook_befad)) {
			hook_befad = hook_befad.trim();
			if (!hook_befad.contains(".")){
				hook_befad = "com.tjpcms.common.Hook."+hook_befad;
			}
		}

		this.hook_befad = hook_befad;
		
		return this;
	}

	public String getHook_aftad() {
		return hook_aftad;
	}

	public Aev setHook_aftad(String hook_aftad) {
		if (StringUtils.isNotEmpty(hook_aftad)) {
			hook_aftad = hook_aftad.trim();
			if (!hook_aftad.contains(".")){
				hook_aftad = "com.tjpcms.common.Hook."+hook_aftad;
			}
		}

		this.hook_aftad = hook_aftad;
		
		return this;
	}

	public String getHook_aftgx() {
		return hook_aftgx;
	}

	public Aev setHook_aftgx(String hook_aftgx) {
		if (StringUtils.isNotEmpty(hook_aftgx)) {
			hook_aftgx = hook_aftgx.trim();
			if (!hook_aftgx.contains(".")){
				hook_aftgx = "com.tjpcms.common.Hook."+hook_aftgx;
			}
		}

		this.hook_aftgx = hook_aftgx;
		
		return this;
	}
	
	public void setHook_aftadgx(String hook_atf) {
		setHook_aftad(hook_atf);
		setHook_aftgx(hook_atf);
	}
	public void setHook_befadgx(String hook_bef) {
		setHook_befad(hook_bef);
		setHook_befgx(hook_bef);
	}

	public String getDynaev() {
		return dynaev;
	}

	public void setDynaev(String dynaev) {
		this.dynaev = dynaev;
	}

	public String getHook_befgx() {
		return hook_befgx;
	}

	public Aev setHook_befgx(String hook_befgx) {
		
		if (StringUtils.isNotEmpty(hook_befgx)) {
			hook_befgx = hook_befgx.trim();
			if (!hook_befgx.contains(".")){
				hook_befgx = "com.tjpcms.common.Hook."+hook_befgx;
			}
		}

		this.hook_befgx = hook_befgx;
		
		return this;
	}

	public String getHdnpara() {
		return hdnpara;
	}

	public void setHdnpara(String hdnpara) {
		this.hdnpara = hdnpara;
	}

	public boolean isNeeda() {
		return needa;
	}

	public void setNoaev() {
		setNeeda(false);
		setNeede(false);
		setNeedv(false);
	}
	public Aev setNeeda(boolean needa) {
		this.needa = needa;
		
		return this;
	}
	public Aev setNeeda(int p) {
		this.needa = (p!=0);
		
		return this;
	}

	public boolean isNeede() {
		return neede;
	}

	public Aev setNeede(boolean neede) {
		this.neede = neede;
		
		return this;
	}
	public Aev setNeede( ) {
		this.neede = false;
		
		return this;
	}
	public Aev setNeede( Integer v) {
		this.neede = !(v==0);
		
		return this;
	}

	public boolean isNeedv() {
		return needv;
	}

	public void setNeedv(boolean needv) {
		this.needv = needv;
	}
	
	public List<Map<String, Object>> getZds() {
		return zds;
	}

	public void setZds(List<Map<String, Object>> zds) {
		this.zds = zds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZdybc() {
		return zdybc;
	}

	public void setZdybc(String zdybc) {
		this.zdybc = zdybc;
	}

	public String getJs_aft_bc() {
		return js_aft_bc;
	}

	public void setJs_aft_bc(String js_aft_bc) {
		this.js_aft_bc = js_aft_bc;
	}

	public List<String> getJs_aft_zdyjy() {
		return js_aft_zdyjy;
	}

	public Aev setJs_aft_zdyjy(String js_aft_zdyjy) {
		this.js_aft_zdyjy.add(js_aft_zdyjy);
		
		return this;
	}

	public List<String> getJs_bef_zdyjy() {
		return js_bef_zdyjy;
	}

	public Aev setJs_bef_zdyjy(String js_bef_zdyjy) {
		this.js_bef_zdyjy.add(js_bef_zdyjy);
		
		return this;
	}


}
