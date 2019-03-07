package com.tjpcms.common.Crud;

import org.apache.commons.lang3.StringUtils;


public class Delete {
	boolean need = true;
	boolean batdel = false;//批量删除
	String zdysql;//删除的自定义sql
	
	String hook_befdel;//删除之前的钩子
	String hook_aftdel;//删除之后的钩子
	
	String zdyQrAft;//在确认删除后，可以执行一段js，可以比如再次确认
	

	public String getZdyQrAft() {
		return zdyQrAft;
	}
	public Delete setZdyQrAft(String zdyQrAft) {
		this.zdyQrAft = zdyQrAft;
		
		return this;
	}
	public String getHook_befdel() {
		return hook_befdel;
	}
	public Delete setHook_befdel(String hook_befdel) {
		if (StringUtils.isNotEmpty(hook_befdel)) {
			hook_befdel = hook_befdel.trim();
			if (!hook_befdel.contains(".")){
				hook_befdel = "com.tjpcms.common.Hook."+hook_befdel;
			}
		}
		
		this.hook_befdel = hook_befdel;
		
		return this;
	}
	public String getHook_aftdel() {
		return hook_aftdel;
	}
	public Delete setHook_aftdel(String hook_aftdel) {
		if (StringUtils.isNotEmpty(hook_aftdel)) {
			hook_aftdel = hook_aftdel.trim();
			if (!hook_aftdel.contains(".")){
				hook_aftdel = "com.tjpcms.common.Hook."+hook_aftdel;
			}
		}
		
		this.hook_aftdel = hook_aftdel;
		return this;
	}
	public String getZdysql() {
		return zdysql;
	}
	public void setZdysql(String zdysql) {
		this.zdysql = zdysql;
	}
	public boolean isNeed() {
		return need;
	}
	public void setNeed(boolean need) {
		this.need = need;
	}
	public Delete setNeed(Integer need) {
		this.need = !(need==0);
		
		return this;
	}
	public boolean isBatdel() {
		return batdel;
	}
	public Delete setBatdel(boolean batdel) {
		this.batdel = batdel;
		return this;
	}
	public Delete setBatdel() {
		this.batdel = true;
		return this;
	}
}
