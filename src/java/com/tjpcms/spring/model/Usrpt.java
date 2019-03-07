package com.tjpcms.spring.model;


public class Usrpt {
	String id;
	String nc;
	String tx;
	String jsid;
	
	public Usrpt(String id,String nc, String tx, String jsid) {
		this.id=id;
		this.nc=nc;
		this.tx=tx;
		this.jsid=jsid;
	}
	public Usrpt() {

	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
}
