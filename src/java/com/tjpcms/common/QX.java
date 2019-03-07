package com.tjpcms.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import com.tjpcms.spring.mapper.EntMapper;


//权限的工具类
public class QX {
	
	//根绝角色获取有权限的cdid，不包括-开头的栏目id，只是cdid
	public static String[] getValidCdids(EntMapper _e, HttpServletRequest request){
		String[] ret = null;
		List<Map<String, Object>> r = _e.r("select cdid from tjpcms_qx_caidan where left(cdid,1)!='-' and jsid="+HT.getJsid(request));
		if (CollectionUtils.isNotEmpty(r) && r.size()>0) {
			ret = new String[r.size()];
			for (int i=0;i<r.size();i++) {
				ret[i] = r.get(i).get("cdid").toString();
			}
		}

		return ret;
	}
	
	//根绝角色获得有权限的lanmuid
	public static String[] getValidLmids(EntMapper _e, HttpServletRequest request){
		String[] ret = null;
		
		//根据request中的___mid得到栏目的cdid权限，此处的逻辑和菜单权限处的设计紧密相关。___mid已在securityFilter中校验过
		String ___mid = request.getParameter("___mid");
		String[] split = ___mid.split("-");
		List<Map<String, Object>> r = _e.r("select * from tjpcms_qx_caidan where cdid like  '-"+(split[1]+"-"+split[2])+"-%' and jsid="+HT.getJsid(request));
		if (CollectionUtils.isNotEmpty(r) && r.size()>0) {
			ret = new String[r.size()];
			for (int i=0;i<r.size();i++) {
				split = r.get(i).get("cdid").toString().split("-");
				ret[i] = split[3];
			}
		}
		
		return ret;
	}
	
	
	
}
