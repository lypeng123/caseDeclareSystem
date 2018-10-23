package com.bonc.caseDeclare.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bonc.caseDeclare.mapper.UploadMapper;
import com.bonc.util.JsonResult;

/**
 * 企业申报-文件上传模块
 * 
 * @author haixia.shi
 * @date 2017年9月10日
 *
 */
@Service
public class UpLoadWord {

	private static Logger logger = Logger.getLogger(UpLoadWord.class);

	/**
	 * 上传产品文件保存地址
	 */
	@Value("${upload.product.url}")
	private String uploadProductUrl;

	/**
	 * 上传方案文件保存地址
	 */
	@Value("${upload.plan.url}")
	private String uploadPlanUrl;

	/**
	 * 产品html存放地址
	 */
	@Value("${html.product.url}")
	private String htmlProductUrl;

	/**
	 * 方案html存放地址
	 */
	@Value("${html.plan.url}")
	private String htmlPlanUrl;
	
//	test上html访问地址
//	/**
//	 * 产品html访问地址
//	 */
//	@Value("${html.product.request.url}")
//	private String htmlProductRequestUrl;
//
//	/**
//	 * 方案html访问地址
//	 */
//	@Value("${html.plan.request.url}")
//	private String htmlPlanRequestUrl;

	@Autowired
	private UploadMapper uploadMapper;

	@Autowired
	private WordToHtml wordToHtml;

	/**
	 * 上传产品文件
	 * 
	 * @param file
	 *            文件
	 * @param request
	 *            请求参数
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public JsonResult<Object> upLoadProductWord(MultipartFile file, HttpServletRequest request)
			throws TransformerException, ParserConfigurationException {

		if (file.isEmpty()) {
			logger.info("上传文件内容为空");
			return new JsonResult<>("上传文件内容为空");
		}
		if (file.getSize() > 31457280) {
			logger.info("文件太大");
			return new JsonResult<>("文件太大");
		}

		String declare_id = request.getParameter("declare_id");
		String company_name = request.getParameter("company_name");
		String property_id = request.getParameter("property_id");
		String user_id = request.getParameter("user_id");
		String user_name = request.getParameter("user_name");
		String area_id = request.getParameter("area_id");
		String quoted_state = request.getParameter("quoted_state");
		if (declare_id == null || declare_id.equals("") || company_name == null || company_name.equals("")
				|| property_id == null || property_id.equals("") || user_id == null || user_id.equals("")
				|| user_name == null || user_name.equals("") || area_id == null || area_id.equals("")
				|| quoted_state == null || quoted_state.equals("")) {
			logger.info("参数错误");
			return new JsonResult<>("参数错误");
		}

		List<Map<String, Object>> checkState = uploadMapper.checkState(declare_id);
		if (checkState != null && checkState.size() > 0) {
			logger.info("无法进行此项操作");
			return new JsonResult<>("无法进行此项操作");
		}

		// 获取原文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		if (!suffixName.equals(".doc") && !suffixName.equals(".docx")) {
			logger.info("文件格式有误");
			return new JsonResult<>("文件格式有误");
		}

		// 文件上传后的存放路径
		// String filePath =
		// request.getSession().getServletContext().getRealPath(uploadProductUrl);
		String filePath = uploadProductUrl;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		// //设置新的文件名称(无后缀)
		String dynFileName = sdf.format(new Date());

		// 解决中文问题，liunx下中文路径，图片显示问题
		// fileName = UUID.randomUUID() + suffixName;
		// 真正的文件路径：filePath + File.separator + dynFileName + suffixName
		File dest = new File(filePath + dynFileName + suffixName);
		// 检测是否存在目录
		// if (!dest.getParentFile().exists()) {
		// dest.getParentFile().mkdirs();
		// }

		try {
			// 上传文件到指定路径
			file.transferTo(dest);

			String html_path = wordToHtml.convert2Html(filePath + dynFileName + suffixName, htmlProductUrl);
			if (html_path.equals("可能为重命名文档，无法解析文档")) {
				return new JsonResult<>("可能为重命名文档，无法解析文档");
			} else if (html_path.equals("未获取到文件")) {
				return new JsonResult<>("未获取到文件");
			} else if (html_path.equals("IO异常")) {
				return new JsonResult<>("IO异常");
			}
			// 提交上传信息到数据库 【完成功能】
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("declare_id", declare_id);
			map.put("product_id", UUID.randomUUID().toString());
			map.put("old_file_name", fileName);
			map.put("file_name", dynFileName + suffixName);
			//test上html访问路径
//			map.put("html_path", htmlProductRequestUrl+html_path);
			map.put("html_path", html_path);
			map.put("file_path", filePath + dynFileName + suffixName);
			map.put("upload_time", new Date());
			map.put("state", "1");

			Integer maxCode = uploadMapper.selectMaxFileCode();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("declare_id", declare_id);
			map2.put("screen_id", UUID.randomUUID().toString());
			map2.put("file_code", "FC" + maxCode);
			map2.put("user_id", user_id);
			map2.put("user_name", user_name);
			map2.put("company_name", company_name);
			map2.put("property_id", property_id);
			map2.put("area_id", area_id);
			map2.put("quoted_state", quoted_state);
			map2.put("field_id", null);
			map2.put("old_file_name", fileName);
			map2.put("file_name", dynFileName + suffixName);
			map2.put("file_path", filePath + dynFileName + suffixName);
			//test上html访问路径
//			map2.put("html_path", htmlProductRequestUrl+html_path);
			map.put("html_path", html_path);
			map2.put("upload_time", new Date());
			map2.put("file_type", "0");
			map2.put("audit_state", "0");
			map2.put("selected_state", "0");

			boolean insertUploadProductInfo = uploadMapper.insertUploadProductInfo(map);
			logger.info("添加上传信息结果：" + insertUploadProductInfo);

			boolean insertScreenTable = uploadMapper.insertScreenTable(map2);
			logger.info("添加筛选信息结果：" + insertScreenTable);

			return new JsonResult<>(JsonResult.SUCCESS, "ok");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonResult<>("error");
	}

	/**
	 * 上传方案文件
	 * 
	 * @param file
	 *            文件
	 * @param request
	 *            请求参数
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public JsonResult<Object> upLoadPlanWord(MultipartFile file, HttpServletRequest request)
			throws TransformerException, ParserConfigurationException {
		if (file.isEmpty()) {
			logger.info("上传文件内容为空");
			return new JsonResult<>("上传文件内容为空");
		}
		if (file.getSize() > 31457280) {
			logger.info("文件太大");
			return new JsonResult<>("文件太大");
		}

		String field_id = request.getParameter("field_id");
		String declare_id = request.getParameter("declare_id");
		String company_name = request.getParameter("company_name");
		String property_id = request.getParameter("property_id");
		String user_id = request.getParameter("user_id");
		String user_name = request.getParameter("user_name");
		String area_id = request.getParameter("area_id");
		String quoted_state = request.getParameter("quoted_state");
		if (declare_id == null || declare_id == "" || field_id == null || field_id == "" || company_name == null
				|| company_name.equals("") || property_id == null || property_id.equals("") || user_id == null
				|| user_id.equals("") || user_name == null || user_name.equals("") || area_id == null
				|| area_id.equals("") || quoted_state == null || quoted_state.equals("")) {
			logger.info("参数错误");
			return new JsonResult<>("参数错误");
		}

		List<Map<String, Object>> checkState = uploadMapper.checkState(declare_id);
		if (checkState != null && checkState.size() > 0) {
			logger.info("无法进行此项操作");
			return new JsonResult<>("无法进行此项操作");
		}

		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		if (!suffixName.equals(".doc") && !suffixName.equals(".docx")) {
			logger.info("文件格式错误");
			return new JsonResult<>("文件格式有误");
		}

		// 文件上传后的路径
		// String filePath =
		// request.getSession().getServletContext().getRealPath(uploadPlanUrl);
		String filePath = uploadPlanUrl;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		// //设置新的文件名称
		String dynFileName = sdf.format(new Date());

		// 解决中文问题，liunx下中文路径，图片显示问题
		// fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + dynFileName + suffixName);
		// 检测是否存在目录
		// if (!dest.getParentFile().exists()) {
		// dest.getParentFile().mkdirs();
		// }
		try {
			file.transferTo(dest);

			String html_path = wordToHtml.convert2Html(filePath + dynFileName + suffixName, htmlPlanUrl);
			if (html_path.equals("可能为重命名文档，无法解析文档")) {
				return new JsonResult<>("可能为重命名文档，无法解析文档");
			} else if (html_path.equals("未获取到文件")) {
				return new JsonResult<>("未获取到文件");
			} else if (html_path.equals("IO异常")) {
				return new JsonResult<>("IO异常");
			}
			// 提交上传信息到数据库 【完成功能】
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("declare_id", declare_id);
			map.put("plan_id", UUID.randomUUID().toString());
			map.put("old_file_name", fileName);
			map.put("file_name", dynFileName + suffixName);
			map.put("field_id", field_id);
			map.put("file_path", filePath + dynFileName + suffixName);
			//test上html访问路径
//			map.put("html_path", htmlPlanRequestUrl+html_path);
			map.put("html_path", html_path);
			map.put("upload_time", new Date());
			map.put("state", "1");

			Integer maxCode = uploadMapper.selectMaxFileCode();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("declare_id", declare_id);
			map2.put("screen_id", UUID.randomUUID().toString());
			map2.put("file_code", "FC" + maxCode);
			map2.put("user_id", user_id);
			map2.put("user_name", user_name);
			map2.put("company_name", company_name);
			map2.put("property_id", property_id);
			map2.put("area_id", area_id);
			map2.put("quoted_state", quoted_state);
			map2.put("field_id", field_id);
			map2.put("old_file_name", fileName);
			map2.put("file_name", dynFileName + suffixName);
			map2.put("file_path", filePath + dynFileName + suffixName);
			//test上html访问路径
//			map2.put("html_path", htmlPlanRequestUrl+html_path);
			map2.put("html_path", html_path);
			map2.put("upload_time", new Date());
			map2.put("file_type", "1");
			map2.put("audit_state", "0");
			map2.put("selected_state", "0");

			boolean insertUploadPlanInfo = uploadMapper.insertUploadPlanInfo(map);
			logger.info("添加上传信息结果：" + insertUploadPlanInfo);

			boolean insertScreenTable = uploadMapper.insertScreenTable(map2);
			logger.info("添加筛选信息结果：" + insertScreenTable);

			return new JsonResult<>(JsonResult.SUCCESS, "ok");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonResult<>("error");
	}

	/**
	 * 获取上传产品文件
	 * 
	 * @param declare_id
	 * @return
	 */
	public Object selectProductFile(String declare_id) {
		List<Map<String, Object>> selectProductFile = uploadMapper.selectProductFile(declare_id);

		logger.info("已上传产品文件返回结果：" + selectProductFile);

		return selectProductFile;
	}

	/**
	 * 获取上传方案文件
	 * 
	 * @param declare_id
	 * @return
	 */
	public Object selectPlanFile(String declare_id) {
		List<Map<String, Object>> selectPlanFile = uploadMapper.selectPlanFile(declare_id);

		logger.info("已上传方案产品返回结果：" + selectPlanFile);

		return selectPlanFile;
	}

	/**
	 * 领域下拉框数据
	 * 
	 * @return
	 */
	public Object selectField() {
		List<Map<String, Object>> selectField = uploadMapper.selectField();

		logger.info("领域数据返回结果：" + selectField);

		return selectField;
	}

	/**
	 * 销毁产品文件
	 * 
	 * @param filepath
	 *            文件路径
	 */
	public JsonResult<Object> deleteProductFile(HttpServletResponse response, HttpServletRequest request) {
		String declare_id = request.getParameter("declare_id");
		String file_path = request.getParameter("file_path");
		String html_path = request.getParameter("html_path");
		String company_name = request.getParameter("company_name");
		String file_name = request.getParameter("file_name");
		String product_id = request.getParameter("product_id");
		if (file_path == null || file_path.equals("") || html_path == null || html_path.equals("") || product_id == null
				|| product_id.equals("") || company_name == null || company_name.equals("") || file_name == null
				|| file_name.equals("") || declare_id == null || declare_id.equals("")) {
			logger.info("删除产品文件：参数有误");
			return new JsonResult<>("参数有误");
		}

		List<Map<String, Object>> checkState = uploadMapper.checkState(declare_id);
		if (checkState == null || checkState.size() == 0) {
			logger.info("无法进行此项操作");
			return new JsonResult<>("无法进行此项操作");
		}

		File file = new File(file_path);
		File file2 = new File(html_path);
		if (file.exists()) {
			file.delete();
			if (file2.exists()) {
				file2.delete();
				boolean deleteProductWord = uploadMapper.deleteProductWord(product_id);
				if (deleteProductWord == false) {
					return new JsonResult<>("文件删除失败");
				}
				boolean deleteScreenTable = uploadMapper.deleteScreenTable(company_name, file_name);
				if (deleteScreenTable == false) {
					return new JsonResult<>("筛选信息删除失败，请联系管理员删除");
				}
				return new JsonResult<>(JsonResult.SUCCESS, "已删除");
			}
		}
		return new JsonResult<>("删除失败,未找到相应文件");

	}

	/**
	 * 销毁方案文件
	 * 
	 * @param filepath
	 *            文件路径
	 */
	public JsonResult<Object> deletePlanFile(HttpServletResponse response, HttpServletRequest request) {
		String declare_id = request.getParameter("declare_id");
		String filepath = request.getParameter("file_path");
		String html_path = request.getParameter("html_path");
		String company_name = request.getParameter("company_name");
		String file_name = request.getParameter("file_name");
		String plan_id = request.getParameter("plan_id");
		if (filepath == null || filepath.equals("") || plan_id == null || plan_id.equals("") || company_name == null
				|| company_name.equals("") || file_name == null || file_name.equals("") || declare_id == null
				|| declare_id.equals("")) {
			logger.info("删除方案文件：参数有误");
			return new JsonResult<>("参数有误");
		}

		List<Map<String, Object>> checkState = uploadMapper.checkState(declare_id);
		if (checkState == null || checkState.size() == 0) {
			logger.info("无法进行此项操作");
			return new JsonResult<>("无法进行此项操作");
		}

		File file = new File(filepath);
		File file2 = new File(html_path);
		if (file.exists()) {
			file.delete();
			if (file2.exists()) {
				file2.delete();
				boolean deletePlanWord = uploadMapper.deletePlanWord(plan_id);
				if (deletePlanWord == false) {
					return new JsonResult<>("文件删除失败");
				}
				boolean deleteScreenTable = uploadMapper.deleteScreenTable(company_name, file_name);
				if (deleteScreenTable == false) {
					return new JsonResult<>("筛选信息删除失败，请联系管理员删除");
				}
				return new JsonResult<>(JsonResult.SUCCESS, "已删除");
			}
		}
		return new JsonResult<>("删除失败,未找到相应文件");
	}

	public String showHtml(HttpServletRequest request, HttpServletResponse response) {

		String html_path = request.getParameter("html_path");
		System.out.println(html_path);
		try {
			URL url = new URL("file:///" + html_path);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			OutputStream os = null;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.err.println("成功");
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					System.out.println(str);
					return str;
				}
				bufr.close();
			} else {
				System.err.println("失败");
				return "失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
