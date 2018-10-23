package com.bonc.caseDeclare.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bonc.caseDeclare.service.impl.DownLoadWord;
import com.bonc.caseDeclare.service.impl.UpLoadWord;
import com.bonc.caseDeclare.service.impl.WordToHtml;
import com.bonc.util.JsonResult;

/**
 * 企业申报-文件上传、下载模块
 * @author haixia.shi
 * @date 2017年9月8日
 *
 */

@RestController
@RequestMapping("/load")
public class LoadDataController {
	
	private Logger logger = Logger.getLogger(LoadDataController.class);
	
	@Autowired
	private UpLoadWord uploadWord;
	
	@Autowired
	private WordToHtml wordToHtml;

	/**
	 * 下载产品申报书
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/downLoadProduct")
	public JsonResult<Object> downLoadProduct(HttpServletResponse response, HttpServletRequest request) {
		Object result = "";
		try {
			result = DownLoadWord.downLoadProductWord(response, request);

		} catch (UnsupportedEncodingException | ParseException e) {
			e.printStackTrace();
		}
		return new JsonResult<Object>(result);
	}

	/**
	 * 下载方案申报书 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/downLoadPlan")
	public JsonResult<Object> downLoadPlan(HttpServletResponse response, HttpServletRequest request) {
		Object result = "";
		try {
			result = DownLoadWord.downLoadPlanWord(response, request);

		} catch (UnsupportedEncodingException | ParseException e) {
			e.printStackTrace();
		}
		return new JsonResult<Object>(result);
	}
	
	/**
	 * 上传产品文件
	 * @param file	文件
	 * @param request	请求参数
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	@RequestMapping("/upLoadProductWord")
	public JsonResult<Object> upLoadProductWord(@RequestParam("fileUpload") MultipartFile file, HttpServletRequest request) throws TransformerException, ParserConfigurationException {
		return uploadWord.upLoadProductWord(file, request);
	}
	
	/**
	 * 上传方案文件
	 * @param file	文件
	 * @param request	请求参数
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	@RequestMapping("/upLoadPlanWord")
	public JsonResult<Object> upLoadPlanWord(@RequestParam("fileUpload") MultipartFile file, HttpServletRequest request) throws TransformerException, ParserConfigurationException {
		return uploadWord.upLoadPlanWord(file, request);
	}
	
	/**
	 * 获取上传产品文件
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectProductFile")
	public JsonResult<Object> selectProductFileNmae(HttpServletResponse response, HttpServletRequest request) {
		String declare_id = request.getParameter("declare_id");
		if (declare_id == null || declare_id.equals("")) {
			logger.info("获取产品文件数据：参数有误");
			return new JsonResult<>("参数有误！");
		}
		
		Object selectProductFileNmae = uploadWord.selectProductFile(declare_id);

		return new JsonResult<>(selectProductFileNmae);
	}
	
	/**
	 * 获取上传方案文件
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectPlanFile")
	public JsonResult<Object> selectPlanFileNmae(HttpServletResponse response, HttpServletRequest request) {
		String declare_id = request.getParameter("declare_id");
		if (declare_id == null || declare_id.equals("")) {
			logger.info("获取方案文件数据：参数有误");
			return new JsonResult<>("参数有误！");
		}
		
		Object selectPlanFileNmae = uploadWord.selectPlanFile(declare_id);

		return new JsonResult<>(selectPlanFileNmae);
	}
	
	/**
	 * 领域下拉框数据
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectField")
	public JsonResult<Object> selectField(HttpServletResponse response, HttpServletRequest request) {
		
		Object selectField = uploadWord.selectField();

		return new JsonResult<>(selectField);
	}
	
	/**
	 * 删除产品文件
	 * @param response	文件
	 * @param request	请求参数
	 * @return
	 */
	@RequestMapping("/deleteProductFile")
	public JsonResult<Object> deleteProductFile(HttpServletResponse response, HttpServletRequest request) {
		return uploadWord.deleteProductFile(response, request);
	}
	
	/**
	 * 删除方案文件
	 * @param response	文件
	 * @param request	请求参数
	 * @return
	 */
	@RequestMapping("/deletePlanFile")
	public JsonResult<Object> deletePlanFile(HttpServletResponse response, HttpServletRequest request) {
		return uploadWord.deletePlanFile(response, request);
	}
	
	
	@RequestMapping("/showHtml")
	public JsonResult<Object> showHtml(HttpServletResponse response, HttpServletRequest request) {
		String show = uploadWord.showHtml(request, response);
		return new JsonResult<>(JsonResult.SUCCESS,show);
	}
	
	
//	/**
//	 * 上传产品文件
//	 * @param file	文件
//	 * @param request	请求参数
//	 * @return
//	 */
//	@RequestMapping("/wordToHtml")
//	public JsonResult<Object> wordToHtml(HttpServletResponse response, HttpServletRequest request) {
//		String file_path = request.getParameter("file_path");
//		try {
//			return wordToHtml.wordToHtml(file_path);
//		} catch (TransformerException e) {
//			e.printStackTrace();
//			return new JsonResult<>(JsonResult.ERROR,"可能为重命名文档，无法解析文档");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return new JsonResult<>(JsonResult.ERROR,"可能为重命名文档，无法解析文档");
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//			return new JsonResult<>(JsonResult.ERROR,"可能为重命名文档，无法解析文档");
//		}
//	}
	
}
