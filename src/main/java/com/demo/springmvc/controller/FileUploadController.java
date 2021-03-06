package com.demo.springmvc.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.demo.springmvc.util.Log4jUtil;

@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public ModelAndView test(@RequestParam MultipartFile[] myfiles, ModelAndView modelAndView, HttpServletRequest request) throws IOException {
		// 如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
		// 如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
		// 并且上传多个文件时，前台表单中的所有<input
		// type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				Log4jUtil.error(this, "文件未上传");
			} else {
				Log4jUtil.debug(this, "文件长度: " + myfile.getSize());
				Log4jUtil.debug(this, "文件类型: " + myfile.getContentType());
				Log4jUtil.debug(this, "文件名称: " + myfile.getName());
				Log4jUtil.debug(this, "文件原名: " + myfile.getOriginalFilename());
				Log4jUtil.debug(this, "========================================");
				// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
				String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
				// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
				FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));
			}
		}
		modelAndView.addObject("message", "上传成功.");
		modelAndView.setViewName("fileUpload");
		return modelAndView;

	}
}