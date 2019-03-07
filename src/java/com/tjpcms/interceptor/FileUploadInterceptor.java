package com.tjpcms.interceptor;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tjpcms.common.HS;

public class FileUploadInterceptor  implements HandlerInterceptor {
	private long maxSize_vdo;
	private long maxSize_pic;
	 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(ServletFileUpload.isMultipartContent(request)) {
            String parameter = request.getParameter("up_size_check_tag");
            Map<String, Object> m = new HashMap<String, Object>();
            if ("pic".equals(parameter) || "vdo".equals(parameter) ){
            	ServletRequestContext ctx = new ServletRequestContext(request);
            	if ("pic".equals(parameter) && ctx.getContentLength()>maxSize_pic){
            		m.put("ret","图片大小不能超过3M！");
            		HS.flushResponse(response, JSONObject.fromObject(m));
            		return false;
            	}else if ("vdo".equals(parameter) && ctx.getContentLength()>maxSize_vdo){
            		m.put("ret","视频大小不能超过500M！");
            		HS.flushResponse(response, JSONObject.fromObject(m));
            		return false;
            	}
            }else{
            	m.put("ret","上传文件时要带up_size_check_tag参数！");
            	HS.flushResponse(response, JSONObject.fromObject(m));
            	return false;
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
 
    
    public long getMaxSize_vdo() {
		return maxSize_vdo;
	}

	public void setMaxSize_vdo(long maxSize_vdo) {
		this.maxSize_vdo = maxSize_vdo;
	}

	public void setMaxSize_pic(long maxSize_pic) {
    	this.maxSize_pic = maxSize_pic;
    }
}
