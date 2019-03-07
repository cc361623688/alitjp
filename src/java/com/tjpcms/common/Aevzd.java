package com.tjpcms.common;


//之所以这样搞，是因为我有些疑虑，感觉虽然Aevzd在配置项中是大写，但还是有何其他配置项混淆重叠的可能性，毕竟像INPUT,TREE这种单词也不算很少见，所以我还是改成了___开头，这样重复的可能性应该就几乎没有了
public enum Aevzd {
	INPUT("___INPUT"),
	PIC("___PIC"),
	RICH("___RICH"),
	HIDDEN("___HIDDEN"),
	TEXT("___TEXT"),
	SELECT("___SELECT"),
	TEXTAREA("___TEXTAREA"),
	PASSWORD("___PASSWORD"),
	ZDB("___ZDB"),//ZDB类型就是先显示字典表让你选择，再根据选的字段表查出对应的字典项，最终的结果是字典项，配了这个类型，表里要有zdb这个字段
	RADIO("___RADIO"),
	CHECKBOX("___CHECKBOX"),//和radio都是写zdb:
	DATE("___DATE"),
	VDO("___VDO"),
	TREE("___TREE");//这个tree是不带复选框的，只能单选。给列表型数据，3个字段id,pId,name。示例是nedcj...中的用户管理
	
	private final String val;
	private Aevzd(String val) {
	     this.val = val;
	}
	public String toString() {
		return val;
	}
	
	
	//IPV4
	//IPV6
	//SEL_PG
	//GJC
	//TREE
	//TREE_CHK
	//FJ
	//FJ_LIST
	//PIC_LIST
	//VDO_LIST
	//PIC_ZH
	//FJ_ZH
	//VDO_ZH
	//MEDIA
	//SEL2
	//SEL3
	//SEL_CX
	//所有能想到的
}