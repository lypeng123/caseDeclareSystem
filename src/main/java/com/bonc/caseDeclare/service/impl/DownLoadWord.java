package com.bonc.caseDeclare.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bonc.util.HttpUtils;

/**
 * 企业申报-文件下载模块
 * @author haixia.shi
 * @date 2017年9月8日
 *
 */
@Service
public class DownLoadWord {

	private Logger logger = Logger.getLogger(DownLoadWord.class);

	/**
	 * 下载产品word模板
	 * @param response
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	public static String downLoadProductWord(HttpServletResponse response, HttpServletRequest request)
			throws ParseException, UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		// 模板名称
		String filename = "大数据优秀产品申报书.doc";
		String sep = File.separator;
		// 模板存放地址
		String path = request.getSession().getServletContext().getRealPath(sep) + sep + "dataDownLoad" + sep;
		// 模板真正地址
		String filepath = path + filename;
		try {
			// 调用ExportExcel的文件下载方法
			downAttachment(filepath, filename, request, response);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	/**
	 * 下载方案word模板
	 * @param response
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	public static String downLoadPlanWord(HttpServletResponse response, HttpServletRequest request)
			throws ParseException, UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		// 模板名称
		String filename = "大数据优秀解决方案申报书.doc";
		String sep = File.separator;
		// 模板存放地址
		String path = request.getSession().getServletContext().getRealPath(sep) + sep + "dataDownLoad" + sep;
		// 模板真正地址
		String filepath = path + filename;
		try {
			// 调用ExportExcel的文件下载方法
			downAttachment(filepath, filename, request, response);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	public static void downAttachment(String path, String fileName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		 boolean isMSIE = HttpUtils.isMSBrowser(request);  
		  if (isMSIE) {  
               fileName = URLEncoder.encode(fileName, "UTF-8");  
         } else {  
               fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");  
         } 
		
		InputStream inputStream = null;
		OutputStream os = null;


		response.setCharacterEncoding("GBK");
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

		try {
			// 下载文件
			 path = URLDecoder.decode(path, "UTF-8");
			inputStream = new FileInputStream(path);
			
			os = response.getOutputStream();

			byte[] b = new byte[4096];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
				inputStream.close();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	private static void deleteFile(String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}

	}
}
