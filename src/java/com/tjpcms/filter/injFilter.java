package com.tjpcms.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//这个是防SQL注入和xss攻击的，我拿工具攻击过的，确实可防
public class injFilter implements Filter {
	private static final Log logger = LogFactory.getLog(injFilter.class);

	private static String ptns[] = {
		 "<script.*>.*<\\/script\\s*>", 
		 "onerror\\s*", 
		 "document\\s*.\\s*cookie", 
		 "document\\s*.\\s*write", 
		 "ascii\\s*\\(.*\\)",
		 "chr\\s*\\(.*\\)",
		 "alter\\s+",
		 "create\\s+",
		 "dbms_pipe\\s*.\\s*receive_message",
		 "truncate\\s+",
		 "drop\\s+",
		 "insert\\s+",
		 "update\\s+",
		 "delete\\s+",
		 "select\\s+",
		 "execute\\s+",
		 "waitfor\\s+",
		 "grant\\s+",
		 "lock\\s+table\\s+",
		 "cast\\s*\\(.*\\)",
		 "str\\s*\\(.*\\)",
		 "convert\\s*\\(.*\\)",
		 "floor\\s*\\(.*\\)",
		 "upper\\s*\\(.*\\)",
		 "lower\\s*\\(.*\\)",
		 "sleep\\s*\\(.*\\)",
		 "pg_sleep\\s*\\(.*\\)",
		 "union\\s+all\\s+",
		 "order\\s+by\\s+",
		 "group\\s+by\\s+",
		 "case\\s+when\\s+", 
		 "waitfor\\s+delay\\s+", 
		 "from\\s+information_schema\\s*",//information_schema后不一定有空格
		 "limit\\s+\\d\\s*,\\s*\\d+"
	};
	
	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

	//url-pattern为/*即匹配的是所有的url，/是restful风格的。*.html就只拦截html结尾的
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String contextPath = httpRequest.getContextPath();
		String requestAction = httpRequest.getRequestURI().substring(	contextPath.length());
		//String basePath = httpRequest.getScheme()+"://"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+httpRequest.getContextPath() + "/";

		Map paramMap = request.getParameterMap();
		Set keSet = paramMap.entrySet();
		for (Iterator itr = keSet.iterator(); itr.hasNext();) {
			Map.Entry me = (Map.Entry) itr.next();
			Object ov = me.getValue();
			String[] value =null;
			if (ov instanceof String[]) {
				value = (String[]) ov;
			} else {
				value = new String[1];
				value[0] = ov.toString();
			}
			for (int k = 0; k < value.length; k++) {
				if (StringUtils.isNotEmpty(value[k])) {
					int m = 0;
					for ( ;m < ptns.length; m++) {
						if (Pattern.compile(ptns[m]).matcher(value[k].toLowerCase()).find()){
							logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!injFilter,value[k]="+value[k]+"("+requestAction+")"+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							httpResponse.sendRedirect(contextPath+"/");
							return;
						}
					}
				}
			}
		}
	    
		filterChain.doFilter(request, response);
	}
	
	
	
}

