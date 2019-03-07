package com.tjpcms.security;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.tjpcms.common.CL;

public class JiamiDbpwd extends PropertyPlaceholderConfigurer{

private boolean secutiry = false;

protected String resolvePlaceholder(String param, Properties props) {
	String pv = props.getProperty(param);
	
	//这是为坑爹的网友量身定制的
	if(StringUtils.isNotEmpty(param) && param.equalsIgnoreCase("jdbc.url")) {
		int idxxie = pv.lastIndexOf("/");
		if (idxxie!=-1){
			int idxwen = pv.lastIndexOf("?");
			if (idxwen==-1) idxwen = pv.length();
			if (idxxie+1 < idxwen){
				String db_alitjp= pv.substring(idxxie+1,idxwen);
				if (StringUtils.isNotEmpty(db_alitjp) && !db_alitjp.equals(CL.DB)){
					try {
						throw new Exception("你妹的你确定你看过压缩包里的必读了吗我嫩死你的心都有了你造吗");
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}
	}
	
	if (this.secutiry) {
		if (param.contains("password") ) {
			try{
				pv = EncryptUtil.decrypt(pv);
			}catch(Exception e){
				
			}
			
		}
	}

	return pv;
}

public boolean isSecutiry() {

	return secutiry;
}

public void setSecutiry(boolean secutiry) {

	this.secutiry = secutiry;

}

public static void main(String[] args) throws Exception {
	System.out.println(EncryptUtil.encrypt("q111"));
}

}
