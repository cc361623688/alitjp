package com.tjpcms.common;

import java.text.SimpleDateFormat;

public class CL {
	
	
	public static final String ROOT="alitjp";//网站根目录，其实没怎么用到，如果你想换个名称，需要你全工程搜索alitjp全部替换成自己的
	public static final String DB="db_alitjp";//网站数据库名，换名称的话jdbc.properties要一并改一下

	public static final String WZMC="tjpcms";//网站名称
	public static final String GGY="tjpcms - 最懂你的cms";//广告语
	public static final String GGY2="国产Java类cms良心之作";//广告语
	public static final String YUMING="www.tjpcms.com";//域名
	public static final String WANGZHI="http://www.tjpcms.com";//网址
	public static final String META_DES="tjpcms是一套基于java的轻量级cms解决方案，全部代码开源（<a href=\\'/alitjp/zntt_detail.dhtml?id=404\\' target=\\'blank\\'>开源许可证协议及版权声明</a>）、可免费商用。其独有的实时配置增删改查的功能，是其区别于同类cms的最大特点，也是最大优势，极大的减少了重复劳动，同时又极大的增加了cms的灵活性。tjpcms信奉以约定替代配置，最大程度减少对业务层代码的入侵性。懂jsp即可实现快速建站、权限工作流设置、静态化等，学习成本极低。文档齐全，持续更新，完备的 <a target=\\'_blank\\' href=\\'/alitjp/spjc.dhtml\\'>视频教程</a> 为你提供全方位的提升，欢迎下载使用！";

	public static final int ses_timeout= 7200;//session2小时失效
	public static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");

	public static final String SES_QT_TIP="ses_qt_tip";
	public static final Integer SES_QT_TO=3600;
	
	
	public static final String DEF_TX = "http://www.tjpcms.com/alitjp/img/logo.png";
	
	public static final String ses_cjgly= "ses_cjgly";//注意，这里的管理员是针对系统而言的，所以，准确的讲，应该叫超级管理员，也就是仅次于系统维护者和开发者的，由超级管理员分配的用户当然其实也可以是管理员，比如管理某些栏目
	public static final String ses_usrpt= "ses_usrpt";//除了管理员之外都是普通用户
	
	
	//MySQL里AUTO_INCREMENT表里插入0值的问题：https://blog.csdn.net/nova_pegasus/article/details/6538984
	//我一开始设置的是1，后来我觉得1不够特殊，我又改成了0 ，但是竟然遇到导出数据库后再导入时，insert into ...(id,...) values('0',...)0是无法导入的，会变成AUTO_INCREMENT+1，导致出错，所以我又改回了1。0出错是因为AUTO_INCREMENT最少1。
	public static final Integer TREE_ROOT_ID=1;//自己建树的，根节点要设置为这个
	
	
	//后台列表页per_page的max值，没有这个逻辑的话可能会出现什么情况呢，比如我在前台给你设置了每页10，20，50，100条数据，然后你改成1000提交到后台，那就返回1000条数据给你了，这样就有点不太好，我并不禁止你看你权限下所有的数据，但是要受我控制啊
	public static final Integer LIST_PP_MAX =200;//list页面最多一页200条数据
	public static final Integer LIST_PP_DEF =15;//list页面默认一页10条记录
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
