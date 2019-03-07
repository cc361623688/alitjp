package com.tjpcms.spring.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tjpcms.common.HS;
import com.tjpcms.spring.mapper.EntMapper;

public class TjpcmsController {
	
	@Resource(name = "entMapper")
	protected EntMapper _e;

	@RequestMapping(value = "aev")
	public ModelAndView aev(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return HS.aev(request, _e);
	}

	//后台通用的新增编辑处理，通过crud获取表信息，通过request获取提交参数信息，进行匹配，生成新增或保存的sql语句并执行
	@RequestMapping(value = "addedit")
	public void addedit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HS.addedit(request, response, _e);
	}

	//新增和编辑时检测字段值是否唯一
	@RequestMapping(value = "unique")
	public void unique(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
		HS.unique(request, response, _e);
	}

	@RequestMapping(value = "delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HS.delete(request, response, _e);
	}

	@RequestMapping(value = "ajaxupload")
	public void ajaxupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws  Exception  {
		HS.ajaxupload(file,request,response,_e);
	}
	
	@RequestMapping(value = "tbs")
	public void tbs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HS.tbs(request, response);
	}
	
	@RequestMapping(value = "tree")
	public void tree(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
		HS.tree(request, response);
	}
}
