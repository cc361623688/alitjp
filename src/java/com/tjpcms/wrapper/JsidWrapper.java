package com.tjpcms.wrapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

//这边就是不加那个jsessionid了，不然识别不了会报错 
//jsessionid的困扰：http://2277259257.iteye.com/blog/2236878
//首次访问服务器的时候会有这个问题，很坑，基本每天只出现一次，好几天了才发现为什么，还不知道这样能不能解决呢，还得再等明天后天试
public class JsidWrapper extends HttpServletResponseWrapper{
	public JsidWrapper(HttpServletResponse response) {
		super(response);
	}
	
    @Override  
    public String encodeRedirectUrl(String url) {  
        return url;
    }

    @Override  
    public String encodeRedirectURL(String url) {  
        return url;
    }  

    @Override  
    public String encodeUrl(String url) {  
        return url;
    }  

    @Override  
    public String encodeURL(String url) {  
        return url;
    }
}
