package com.tjpcms.cfg;

import org.apache.commons.lang.ArrayUtils;

import com.tjpcms.spring.mapper.EntMapper;


//系统配置
//这里的值都可以随便改，理论上应该可以提高系统的安全性，因为如果你都是用默认的，我又是完全开源的，那这些默认配置很可能就是别人攻击你的入口啊。
//网上遍地都是织梦的漏洞，白帽网站各种提交，什么闲来无事看看源码，发现一漏洞拿shell，什么默认后台地址啦，默认重置密码地址啦这类，我去。
//所以，我觉得这个应该还有些作用，哪怕只是一点点
public class XT {
	public static final String nedcjgly="wjxxyg";//这个是只有登录了超级管理员才能访问到的链接部分，主要就是权限管理那一块了，目前两个账号，test只能看不能动，还有就是正式的超级管理员，需要在代码里设置，数据库不存（我就瞎写一个）

	public static final String htgllj="wzsbnyg";//后台管理链接（我再随便弄一个）

	public static final String dlhtlj="zshtglddllj";//为安全起见，可以更改默认的登录后台的路径（这是后台管理的登录链接）

	public static final String dlhtlj_test="htgl_test";//现网上用这个来登录后台管理的测试账号
	
	
	//部分系统表并不适合于自动填充nrtbl，nrtbl字段主要作用是栏目内容静态化（url3和nrtbl都不为空时可以栏目内容静态化），其次还有就是前台阅读文章时，有一个阅读次数cs的增加（当然其实前台我并不需要去考虑，因为系统只关乎后台而已）
	//这里tjpcms_fwb略有点需要注意一下，富文本类型当然是可以静态化的，比如作者简介这种，但它的静态化是栏目静态化而不是栏目内容静态化，也就是说比如作者简介是从栏目表中的url_d字段来静态化的，也就不需要nrtbl字段
	//注意这里用到了tjpcms_开头即为系统表的逻辑
	//dhwb我考虑了一下，还是决定不使用静态化和审核了，因为这玩意只是想填一段文字而已，这还要审核不合理啊，如果的确是需要审核，可以自行创建一个自定义的栏目类型
	public static boolean lmneed_nrtbl(String tb){

		String[] systbl={
			"tjpcms_tywz",
			//"tjpcms_dhwb",
			"tjpcms_pic"};
		return !tb.startsWith("tjpcms_") || ArrayUtils.contains(systbl, tb);
	}

	//只有部分系统表需要cid字段，其他系统表因为只是为了完成系统功能，而并不是存储数据的（cid字段的作用就是为了区分某条数据是来自哪个栏目），因此并不需要有这个区分
	//当然如果你又觉得哪个表需要区分了，可以在数组里加上
	//非系统表的话，要看表里是否有数据，有数据则我不自动加cid字段，没有则自动加
	public static boolean lmneed_cid(String tb, EntMapper mp){
		String[] systbl={
			"tjpcms_tywz",
			"tjpcms_fwb",
			"tjpcms_dhwb",
			"tjpcms_pic"};

		return tb.startsWith("tjpcms_") ? ArrayUtils.contains(systbl, tb) : mp.cnt("select count(*) from "+tb)<=0;
	}
	
}
