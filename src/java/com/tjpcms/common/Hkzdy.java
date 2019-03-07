package com.tjpcms.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tjpcms.common.Crud.Crud;



//para1:request, para2:o, para3:pa
public class Hkzdy {
	
		
	//企业（1）→区（2）→市（3）→省（4）
	public String zdysqb_shenhe(Object para1, Object para2, Object para3, Object beiyong) throws Exception{
		HttpServletRequest request = (HttpServletRequest)para1;
		Crud o = (Crud)para2;
		Map<String, Object> rec = (Map<String, Object>)beiyong;

		String shzt = request.getParameter("shzt");
		Map<String, String[]> e = new HashMap<String, String[]>(request.getParameterMap());
		if ("退回修改".equals(shzt) || "审核不通过".equals(shzt)){
			e.put("shjd", new String[]{"0"});//其实不设置也行就是了
		}else{//审核通过
			Integer shjd = Integer.valueOf(rec.get("shjd").toString());
			if (shjd<4){
				e.put("shzt", new String[]{"待审核"});//其实不设置也行就是了
				e.put("shjd", new String[]{String.valueOf(shjd+1)});
			}
		}

		return HS.ppbc(o.getMp(),o.getTb(), request,e);
	}

	
	
	
	
	
	
}















