/**
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 */

package com.tjpcms.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.tjpcms.spring.model.Usrpt;


//后台包含两类用户：超级管理员、普通用户
//超级管理员分两类：test，非test
//普通用户分三类：前台注册的，后台超级管理员分配的，第三方登录的
//第三方登录的分三类：QQ，百度，新浪（微信登录没搞，要钱，等后面搞小程序时再申请）
//有点绕，其实很简单，这只是我设计的一个规则，因为我觉得这么分绝大多数业务情况都可以满足了
//所以，从大的分类来说：超管（正式，测试），普通用户
public class HT {

	public static void setCjgly(HttpServletRequest request, String name){
		if (StringUtils.isNotEmpty(name)) {
			clrUsr(request);
			request.getSession().setMaxInactiveInterval(CL.ses_timeout);
			request.getSession(false).setAttribute(CL.ses_cjgly, name);//这个是整个系统的管理员，所以应该叫超级管理员，以与分配的普通后台管理员相区别
		}
	}

	public static String getCjgly(HttpServletRequest request){
		
		return (String)request.getSession().getAttribute(CL.ses_cjgly);//这个是整个系统的管理员，所以应该叫超级管理员，以与分配的普通后台管理员相区别
	}

	public static boolean isCjgly(HttpServletRequest request){
		
		return StringUtils.isNotEmpty(getCjgly(request));
	}

	//这个要注意，如果不是test超级管理员，那既可能是正式超管，也可能是普通用户
	public static boolean isTestCjgly(HttpServletRequest request){

		return  "test".equals(getCjgly(request));
	}
	
	//正式超管，首先得是超管，其次不能是测试超管
	//如果不是正式超管，那就只能是普通用户，或者是测试超管
	public static boolean isZsCjgly(HttpServletRequest request){
		return  isCjgly(request) && !isTestCjgly(request);
	}
	
	public static void clrCjgly(HttpServletRequest request){
		request.getSession().removeAttribute(CL.ses_cjgly);
	}
	
	public static Usrpt getUsrpt(HttpServletRequest request){
		return (Usrpt)request.getSession().getAttribute(CL.ses_usrpt);
	}
	public static boolean isUsrpt(HttpServletRequest request){
		return getUsrpt(request) !=null;
	}
	public static boolean isTestUsrpt(HttpServletRequest request){
		Usrpt usrpt = getUsrpt(request);
		return usrpt!=null && "test".equals(usrpt.getId().substring(3, usrpt.getId().length()));
	}

	public static String getUid(HttpServletRequest request){
		Usrpt so = (Usrpt)getUsrpt(request);
		if (so != null){
			return so.getId();
		}
		
		return getCjgly(request);
	}

	public static String getJsid(HttpServletRequest request){
		Usrpt so = (Usrpt)getUsrpt(request);
		if (so != null){
			return so.getJsid();
		}

		return CL.TREE_ROOT_ID.toString();//超管在角色树中id为1
	}

	public static void setUsr(HttpServletRequest request, Usrpt so){
		if (request!=null && so !=null){
			clrCjgly(request);
			request.getSession().setMaxInactiveInterval(CL.SES_QT_TO);
			request.getSession().setAttribute(CL.ses_usrpt,so);
		}
	}

	public static void clrUsr(HttpServletRequest request){
		request.getSession().removeAttribute(CL.ses_usrpt);
	}
	
	public static void clearLogin(HttpServletRequest request){
		request.getSession().removeAttribute(CL.ses_cjgly);
		request.getSession().removeAttribute(CL.ses_usrpt);
	}
	
	public static boolean isDsfUsrpt(HttpServletRequest request){
		Usrpt so = (Usrpt)getUsrpt(request);
		if (so!=null){
			String lx = so.getId().substring(0, 3);
			return lx.equals(EQtdllx.BD_.toString()) || lx.equals(EQtdllx.SN_.toString()) || lx.equals(EQtdllx.QQ_.toString());
		}
		
		return  false;
	}
	
	
}
