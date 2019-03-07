package com.tjpcms.interceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tjpcms.cfg.XT;
import com.tjpcms.common.HS;
import com.tjpcms.common.HT;
import com.tjpcms.spring.mapper.EntMapper;

public class QuanxianInterceptor  implements HandlerInterceptor {
	@Resource(name = "entMapper")
	private EntMapper _e;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if (HT.isCjgly(request) && !HT.isTestUsrpt(request)) return true;//正式超管不拦截
    	
    	String requestAction = request.getRequestURI().substring(request.getContextPath().length());
    	if (handler instanceof HandlerMethod && requestAction.startsWith("/"+XT.htgllj)) {
    		HandlerMethod hm = (HandlerMethod) handler;
    		if (_e.cnt("select count(*) from tjpcms_qx_fangfa t where t.jsid='"+HT.getJsid(request)+"' and ff='"+hm.getMethod().getName()+"'")<=0) {
    			HS.flushResponse(response, "无权访问！");
    			return false;
    		}else{
    			return true;
    		}
    	}

        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
 
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
