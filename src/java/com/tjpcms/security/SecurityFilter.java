
package com.tjpcms.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tjpcms.cfg.XT;
import com.tjpcms.common.Aevzd;
import com.tjpcms.common.CL;
import com.tjpcms.common.HS;
import com.tjpcms.common.HT;
import com.tjpcms.spring.mapper.EntMapper;
import com.tjpcms.wrapper.JsidWrapper;

public class SecurityFilter implements Filter {
	private static final Log logger = LogFactory.getLog(SecurityFilter.class);
	private static String fenyeptn = "___[0-9]*[1-9][0-9]*";
	
	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

	private String getJthStr(HttpServletRequest httpRequest , String py){
		if (StringUtils.isEmpty(py) || httpRequest==null) {
			return null;
		}

		//这边检查静态化（filter中不好注入entMapper，要使用WebApplicationContext的方式）
		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(httpRequest.getSession().getServletContext());
		if (ac != null){
			EntMapper mp = (EntMapper) ac.getBean("entMapper");
			Map<String, Object> cfg = mp.obj("select val from tjpcms_cfg where py='"+py.toUpperCase()+"' ");
			if (cfg !=null){
				return (String)cfg.get("val");
			}
		}

		return null;
	}

	//url-pattern为/*即匹配的是所有的url，/是restful风格的。*.html就只拦截html结尾的
	//这块的逻辑要改动，我等弄完其他的来写一个注释
	//有网友表示群主这个过滤器是干嘛的，实际上很简单，两大一小，登录判断、静态化外加解决一个第三方登录时第一次登录会失败的问题
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)) {  
			filterChain.doFilter(request, response);  
			return;  
		}  
		// wrap response to remove URL encoding  
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletResponseWrapper jswrapper = new JsidWrapper(httpResponse);
		
		String contextPath = httpRequest.getContextPath();
		String requestAction = httpRequest.getRequestURI().substring(	contextPath.length());
		//String basePath = httpRequest.getScheme()+"://"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+httpRequest.getContextPath() + "/";

		if (requestAction.equals("/error.html")){
			filterChain.doFilter(request, jswrapper);
			return;
		}
		
		//这边其实有个我一直很疑惑的问题，为什么tomcat部署域名之后，访问www.tjpcms.com和www.tjpcms.com/alitjp都是到首页，尼玛！
		//我觉得肯定有可以配置的地方，使得只能通过www.tjpcms.com访问到服务器，因为如果两个都能访问会存在登录session失效的问题，非常坑爹
		//但是初步找找没有找到如何配置，因此精力顾及不到，暂时以强制跳转来实现。而且这个地方内部跳和外部跳是否都可以，有什么区别，也不清楚
		String serverName = httpRequest.getServerName();
		if (!"localhost".equals(serverName) && !"127.0.0.1".equals(serverName) && !serverName.startsWith("192.168.") 
				&& ("/"+CL.ROOT).equals(contextPath)){
			String queryString = httpRequest.getQueryString();
			httpResponse.sendRedirect(requestAction+(StringUtils.isNotEmpty(queryString)?"?"+queryString:""));
			return;
		}
		
		// clear session if session id in URL  
		if (httpRequest.isRequestedSessionIdFromURL()) {  
			HttpSession session = httpRequest.getSession();  
           	if (session != null) session.invalidate();  
		}  
 
		
		
		
		
		//这里针对htglcontroller里的请求做一个参数校验，即如果是菜单则的链接则必须有___mid，且如果是栏目菜单，还必须有___cid
		//否则此链接是有问题的，可能是伪造的请求
		//相反的，如果不是菜单栏目的链接，却有这样的参数，也是有问题的，因为我是___开头的，不可能那么巧的
		//1、某些url必须要有这两个参数的一个或全部两个，剩下的url完全不需要，一个都不需要
		//2、___mid和___cid的权限校验(因为多个栏目可以是同一个栏目类型，也就共享了一张表，通过cid来区分权限，但是cid是写到前台并提交到后台来的，所以必须要校验提交过来的cid，防止被篡改过，mid也是前台提交的，要校验)
		//3、方法的权限是由人工判断和设置的，这没有办法，只能这样，能做到页面设置已经不错了。之所以方法必须是人工，根本原因在于授权的菜单里可能会发起url请求，而这个请求是否是我授权菜单下的，我无从知晓
		
		
		
		
		
		
		
		
		
		boolean succ = false;
		if (requestAction.endsWith(".dhtml") || requestAction.endsWith("/")){//拦截的是动态页面
			if (requestAction.startsWith("/"+XT.nedcjgly+"/")) {//链接带nedcjgly的，要检查管超级理员登录情况
				succ = true;
				String cjgly = HT.getCjgly(httpRequest);
				if (StringUtils.isEmpty(cjgly)) {
					httpResponse.sendRedirect(contextPath+"/"+XT.dlhtlj+".dhtml");
					return;
				}
				if ("test".equalsIgnoreCase(cjgly)){
					if (requestAction.contains("addedit") || requestAction.contains("delete") || requestAction.contains("delLanmu") ||  requestAction.contains("aj_cmn_sh")){
						Map<String,Object> qtjson = new HashMap<String, Object>();
						qtjson.put("ret", "抱歉，测试账户不可以改动数据");
						HS.flushResponse(httpResponse, JSONObject.fromObject(qtjson));
						return;
					}
				}
			}else if (requestAction.startsWith("/"+XT.htgllj+"/")){//是后台管理的相关链接，要检查是否登录了，可以是超级管理员也可以是普通用户
				if (HT.isCjgly(httpRequest) || HT.isUsrpt(httpRequest)){
					succ = true;
					if (HT.isTestUsrpt(httpRequest) || HT.isTestCjgly(httpRequest)){
						if (requestAction.contains("addedit") || requestAction.contains("delete") || requestAction.contains("delLanmu") ||  requestAction.contains("aj_cmn_sh")){
							Map<String,Object> qtjson = new HashMap<String, Object>();
							qtjson.put("ret", "抱歉，测试账户不可以改动数据");
							HS.flushResponse(httpResponse, JSONObject.fromObject(qtjson));
							return;
						}
					}
				}else{//既没登录超级管理员也没登录usrpt
					httpResponse.sendRedirect(contextPath+"/"+XT.dlhtlj+".dhtml");
					return;
				}
			}else{//非管理员的链接自行检查登录情况
				succ = true;
				if (requestAction.equals("/") || requestAction.equals("/index.dhtml")){//访问的是动态首页
					succ = true;
					if (StringUtils.isEmpty(request.getParameter("___opjth"))){//是静态化功能访问首页动态链接
						String jthStr = getJthStr(httpRequest, "syjth");
						if ("静态化".equals(jthStr)) {//设置了首页静态化，但是访问了动态的首页链接
							httpResponse.sendRedirect("static/index.html");
							return;
						}else if (!"动态化".equals(jthStr)){
							succ = false;
						}
					}
				}else{
					WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(httpRequest.getSession().getServletContext());
					if (ac == null){
						succ = false;
					}else{
						EntMapper mp = (EntMapper)ac.getBean("entMapper");
						Map<String, Object> lmobj = mp.obj("select url_d,donly2 from tjpcms_lanmu where url_d is not null and url_d !='' and concat('/',url_d,'.dhtml')='"+requestAction+"'");
						if (lmobj!=null){//说明是栏目的链接，继续检查是否需要跳转到对应的静态链
							succ = true;
							String jthStr = getJthStr(httpRequest, "lmjth");
							if ("静态化".equals(jthStr)) {//设置了栏目静态化，但是访问了动态的栏目链接，且该栏目没有设置“只动态显示”
								if ("否".equals(lmobj.get("donly2"))){
									httpResponse.sendRedirect("static/lanmu/"+lmobj.get("url_d")+".html");
									return;
								}
							}else if (!"动态化".equals(jthStr)){
								succ = false;
							}
						}else{
							succ = true;
							Map<String, Object> nrobj = mp.obj("select distinct url3,donly3,id from tjpcms_lanmu where url3 is not null and url3 !='' and concat('/',url3,'.dhtml')='"+requestAction+"'");
							if (nrobj!=null){//说明是栏目内容的动态链接
								String lmnrjth = getJthStr(httpRequest, "lmnrjth");
								if ("静态化".equals(lmnrjth)) {//设置了栏目内容静态化，但是访问了动态的栏目内容链接，且该栏目内容没有设置“只动态显示”
									if ("否".equals(nrobj.get("donly3"))){
										httpResponse.sendRedirect("static/neirong/"+nrobj.get("url3")+"/"+request.getParameter("id")+".html");
										return;
									}
								}else if (!"动态化".equals(lmnrjth)){
									succ = false;
								}
							}
						}
					}
				}
			}
		}else if (requestAction.endsWith(".html")){//拦截的是静态页面
			if (requestAction.equals("/static/index.html")){//访问静态首页
				succ = true;
				String jthStr = getJthStr(httpRequest, "syjth");
				if ("动态化".equals(jthStr)) {//设置了首页动态化，但是访问了静态的首页链接
					httpResponse.sendRedirect("../");
					return;
				}else if (!"静态化".equals(jthStr)){
					succ = false;
				}
			}else{//不是访问静态首页
				WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(httpRequest.getSession().getServletContext());
				if (ac == null){
					succ = false;
				}else{
					EntMapper mp = (EntMapper)ac.getBean("entMapper");
					String actionOri = requestAction;
					requestAction = requestAction.replaceAll(fenyeptn, "");
					Map<String, Object> lmobj = mp.obj("select url_s,url_d,donly2 from tjpcms_lanmu where url_s is not null and url_s !='' and url_s='/"+CL.ROOT+requestAction+"'");
					if (lmobj!=null){//说明是栏目的链接，继续检查是否需要跳转到对应的动态链
						succ = true;
						String lmjth = getJthStr(httpRequest, "lmjth");
						String parapg = request.getParameter("pg");
						boolean vldPg = true;
						if (StringUtils.isNotEmpty(parapg)) try{Integer.valueOf(parapg);}catch(Exception e){vldPg = false;}
						if (!"动态化".equals(lmjth) && !"静态化".equals(lmjth) || !vldPg){
							succ = false;
						}else if (StringUtils.isNotEmpty(parapg) && "否".equals(lmobj.get("donly2")) && "静态化".equals(lmjth)) {
							String dynurl = requestAction.substring(requestAction.lastIndexOf("/")+1,requestAction.lastIndexOf(".html"));
							if (nedGxFenye(mp, parapg, dynurl, request)){//需要重新生成该分页
								final ByteArrayOutputStream os = new ByteArrayOutputStream();  
								final ServletOutputStream stream = new ServletOutputStream() {  
							        public void write(byte[] data, int offset, int length) {    os.write(data, offset, length);  }  
							        public void write(int b) throws IOException {    os.write(b);   }  
							    };
							    final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));  
							    HttpServletResponse rep = new HttpServletResponseWrapper(httpResponse) {  
							        public ServletOutputStream getOutputStream() {    return stream;   }
							        public PrintWriter getWriter() {   return pw;   }
							    };
							    
								File mulu = new File(request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"lanmu"+File.separator);
								if (!mulu.exists()){
									mulu.mkdirs();
								}
								
								RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/"+dynurl+".dhtml?___opjth=1&___url_d="+dynurl);
								rd.include(request, rep);
								pw.flush();
								FileOutputStream fos = new FileOutputStream(request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"lanmu"+File.separator+dynurl+"___"+parapg+".html");
								os.writeTo(fos);
							}
							
							//判断需要不需要跳转
							if (NedRedirect(parapg, actionOri)){//需要跳转
								httpResponse.sendRedirect(contextPath+"/static/lanmu/"+dynurl+("1".equals(parapg)?"":"___"+parapg)+".html?pg="+parapg);
								return;
							}
						}else if ("是".equals(lmobj.get("donly2")) || "动态化".equals(lmjth)){//什么情况下要跳到该静态栏目对应的动态链接：1、该栏目只动态访问，2、栏目非只动态访问但是设置了栏目动态化
							httpResponse.sendRedirect(contextPath+"/"+lmobj.get("url_d")+".dhtml");
							return;
						}
					}else{//判断是不是栏目内容的链接
						succ=true;
						int idxlast = requestAction.lastIndexOf("/");
						int idxHtm= requestAction.lastIndexOf(".html");
						if (idxlast==-1 || idxHtm==-1 || idxlast>=idxHtm-1 || StringUtils.isEmpty(requestAction.substring(idxlast+1, idxHtm))){
							succ = false;
						}else{
							String recid = requestAction.substring(idxlast+1, idxHtm);
							Map<String, Object> nrobj = mp.obj("select distinct url3,donly3,id from tjpcms_lanmu where url3 is not null and url3 !='' and concat('/static/neirong/',url3,"
																				+"'/"+recid+".html')='"+requestAction+"'");
							if (nrobj!=null){//说明是栏目内容的静态链接
								String lmnrjth = getJthStr(httpRequest, "lmnrjth");
								if (!"动态化".equals(lmnrjth) && !"静态化".equals(lmnrjth)){
									succ = false;
								}else if ("是".equals(nrobj.get("donly3")) || "动态化".equals(lmnrjth)){//什么情况下要跳到该静态链接对应的动态链接：1、该栏目只动态访问，2、非动态访问但是设置了动态化
									httpResponse.sendRedirect(contextPath+"/"+nrobj.get("url3")+".dhtml?id="+recid);
									return;
								}
							}
						}
					}
				}
			}
		}

		if(succ) {
			try{
				filterChain.doFilter(request, jswrapper);
			}
			catch(Exception e){
				e.printStackTrace();
				logger.error("！！！！！！！！！！！！！出 错 啦，自己看打印，我也没那么牛逼所有的错误都在应用层面帮你们弹出提示对吧！！！！！！！！！！！！requestAction="+e.toString());
				//wait，我发现还是可以稍微提示一下下的
				String s = contextPath+"/error.html";
				String emsg = ExceptionUtils.getRootCauseMessage(e);
				emsg=emsg.replaceAll("\'", "");
				emsg=emsg.replaceAll("\\s+", "~~~");
				if (e.getCause()!=null) s+="?c="+URLEncoder.encode(URLEncoder.encode(emsg, "UTF-8"), "UTF-8");

				//System.out.println(s);
				httpResponse.sendRedirect(s);
				HS.flushResponse(httpResponse, s);
				return;
			}
		}else {
			httpResponse.sendRedirect(contextPath + "/error.html");
		}
	}
	
	//判断是否需要更新分页的html文件
	private boolean nedGxFenye(EntMapper mp ,String pg, String dynurl, ServletRequest request){
		String filePath = request.getServletContext().getRealPath("")+File.separator+"static"+File.separator+"lanmu"+File.separator+dynurl+"___"+pg+".html";
		File file=new File(filePath);  
		if(!file.exists()) {
			return true;
		}
		
		//文件最后修改的时间
		long lastModified = file.lastModified();
		Calendar wjsj = Calendar.getInstance();  
		wjsj.setTimeInMillis(lastModified); 
		
		//上一次栏目静态的时间
		Map<String, Object> obj = mp.obj("select gx from tjpcms_cfg where PY='LMJTH'");
		if (obj ==null || obj.get("gx")==null){
			logger.error("nedGxFenye,get LMJTH error");
			return true;	
		}
		Calendar lmjthsj = Calendar.getInstance();
		Date parse = null;
		try{
			parse = CL.fmt.parse((String)obj.get("gx"));
		}catch(Exception e){
			parse = null;
		}
		if (parse==null){
			logger.error("nedGxFenye,parse LMJTH error");
			return true;
		}

		lmjthsj.setTime(parse);
		return wjsj.before(lmjthsj);
	}
	
	
	private boolean NedRedirect(String pg, String acori){
		
		Pattern pattern = Pattern.compile(fenyeptn);
		Matcher matcher = pattern.matcher(acori);
		if(matcher.find()){//链接中有___d
			String group = matcher.group(0);
			String sz = group.substring(group.indexOf("___")+"___".length(), group.length());
			if (!sz.equals(pg)){
				return true;
			}
		}else if (!pg.equals("1")){
			return true;
		}

		return false;
	}
}
