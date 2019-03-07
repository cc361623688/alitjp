package com.tjpcms.common.Crud;

import com.tjpcms.common.CL;


//这个就是在列表页list.jsp中显示出一个树形控件的区域，和右侧的列表区形成联动
public class Tree {
	//有点类似于菜单管理的里的是否动态的那几个设置，就是要给出表名或者视图名，如果是表名有可能需要要额外设置id,pId,mc,lj这四个字段，用视图的话可以强制把字段名字改了，当然也可以不改，不改就设置
	String tb="";
	String id = "id";
	String pId = "pId";
	String name = "name";
	String lj = "lj";
	String title="树";
	String tip = "";
	String width = "220";//树区域的默认宽度
	final Integer TREE_ROOT_ID = CL.TREE_ROOT_ID;
	
	//id，pId，name
	
	
	public String getTip() {
		return tip;
	}
	public Integer getTREE_ROOT_ID() {
		return TREE_ROOT_ID;
	}
	public Tree setTip(String tip) {
		this.tip = tip;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public Tree setTitle(String title) {
		this.title = title;
		return this;
	}
	public String getTb() {
		return tb;
	}
	public Tree setTb(String tb) {
		this.tb = tb;
		return this;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLj() {
		return lj;
	}
	public void setLj(String lj) {
		this.lj = lj;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
	
	
}
