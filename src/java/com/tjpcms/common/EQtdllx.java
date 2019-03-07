package com.tjpcms.common;

//得是3位的，后面trjpcms_usr的sql里要截取，这是前台登录，这四类用户只能在前台登录页，后台登录页是登不上去的，同样后台登录页面前台注册用户也是登录不上去的，但所有的用户信息都存储在tjpcms_usr表里
public enum EQtdllx {
	GR_,//个人
	SN_,//新浪
	QQ_,//qq
	BD_//百度
}