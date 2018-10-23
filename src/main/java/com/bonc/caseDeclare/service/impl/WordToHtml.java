package com.bonc.caseDeclare.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.bonc.util.JsonResult;

/**
 *
 * @author haixia.shi
 * @date 2017年9月20日
 *
 */
@Service
public class WordToHtml {

	private static Logger logger = Logger.getLogger(WordToHtml.class);

	@Value("${html.product.url}")
	private String htmlProductUrl;
	
	/**
	 * 将word转换成html 支持 .doc and .docx
	 * 
	 * @param wordPath
	 *            word文件路径
	 * @param htmlPath
	 *            html存储路径
	 * @param htmlName
	 *            html名
	 * @throws TransformerException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public String convert2Html(String wordPath, String htmlPath)
			throws TransformerException, IOException, ParserConfigurationException {
		String substring = wordPath.substring(wordPath.lastIndexOf(".") + 1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		/**
		 * word2007和word2003的构建方式不同， 前者的构建方式是xml，后者的构建方式是dom树。
		 * 文件的后缀也不同，前者后缀为.docx，后者后缀为.doc 相应的，apache.poi提供了不同的实现类。
		 */
		if ("docx".equals(substring)) {
			// step 1 :  DOCX 转换为 XWPFDocument
			InputStream inputStream = new FileInputStream(new File(wordPath));
			XWPFDocument document;
			try{
				document = new XWPFDocument(inputStream);
			}catch(POIXMLException pe){
				return "可能为重命名文档，无法解析文档";
			} catch (FileNotFoundException fnfe) {
				return "未获取到文件";
			} catch (IOException ioe) {
				return "IO异常";
			} catch (NullPointerException np) {
				return "未获取到文件";
			}

			// step 2 : 准备  XHTML 选项
			final String imageUrl = "";

			XHTMLOptions options = XHTMLOptions.create();
			options.setExtractor(new FileImageExtractor(new File(htmlPath + imageUrl)));
			options.setIgnoreStylesIfUnused(false);
			options.setFragment(true);
			options.URIResolver(new IURIResolver() {
				// @Override 重写的方法
				public String resolve(String uri) {
					return imageUrl + uri;
				}
			});

			// step 3 : 转换  XWPFDocument 为  XHTML
			XHTMLConverter.getInstance().convert(document, out, options);
		} else {
			HWPFDocument wordDocument;
			try{
				wordDocument = new HWPFDocument(new FileInputStream(wordPath));
			}catch(OfficeXmlFileException oe){
				return "可能为重命名文档，无法解析文档";
			} catch (FileNotFoundException fnfe) {
				return "未获取到文件";
			} catch (IOException ioe) {
				return "IO异常";
			} catch (NullPointerException np) {
				return "未获取到文件";
			}
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
				public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
						float widthInches, float heightInches) {
					return suggestedName;
				}
			});
			wordToHtmlConverter.processDocument(wordDocument);

			// 保存图片
			List pics = wordDocument.getPicturesTable().getAllPictures();
			if (pics != null) {
				for (int i = 0; i < pics.size(); i++) {
					Picture pic = (Picture) pics.get(i);
					try {
						pic.writeImageContent(new FileOutputStream(htmlPath + pic.suggestFullFileName()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			Document htmlDocument = wordToHtmlConverter.getDocument();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance(); // 这个应该是转换成xml的
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		// //设置新的文件名称
		String dynFileName = sdf.format(new Date());
		String htmlName = dynFileName + ".html";

		out.close();
		return writeFile(new String(out.toByteArray()), htmlPath + htmlName, htmlName);
	}

	// 输出html文件
	public String writeFile(String content, String path, String htmlName) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			if (!file.exists()) {

			}
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(content);
//			JSONObject object = new JSONObject();
//			object.put("html_path", path);
			//test上html访问需要
//			return htmlName;
			return path;
			
			
		} catch (FileNotFoundException fnfe) {
			return "未获取到文件";
		} catch (IOException ioe) {
			return "IO异常";
		} catch (NullPointerException np) {
			return "未获取到文件";
		} catch (OfficeXmlFileException oe) {
			return "可能为重命名文档，无法解析文档";
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

//	public JsonResult<Object> wordToHtml(String wordPath)
//			throws TransformerException, IOException, ParserConfigurationException {
//		try {
//			return new JsonResult<>(JsonResult.SUCCESS,"ok",convert2Html(wordPath, htmlProductUrl));
//		} catch (NullPointerException e) {
//			return new JsonResult<>(JsonResult.ERROR,"可能为重命名文档，无法解析文档");
//		} catch (OfficeXmlFileException e) {
//			return new JsonResult<>(JsonResult.ERROR,"可能为重命名文档，无法解析文档");
//		}
//	}
}
