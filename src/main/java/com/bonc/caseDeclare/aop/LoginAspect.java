package com.bonc.caseDeclare.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bonc.util.JsonResult;
import com.bonc.util.SendEmailUtils;

/**
 * AOP
 * 
 * @author zhijie.ma
 * @date 2017年9月8日
 *
 */
@Aspect
@Component
public class LoginAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoginAspect.class);

	/**
	 * 案例申报登陆首页
	 */
//	@Value("${loginHtml}")
//	private String loginHtml;
	
	/**
	 * 专家允许访问接口列表
	 */
	@Value("${allow.expert}")
	private String[] allowExpert;
	
	/**
	 * 企业允许访问接口列表
	 */
	@Value("${allow.enterprise}")
	private String[] allowEnterprise;
	
	/**
	 * 管理员允许访问接口列表
	 */
	@Value("${allow.administrators}")
	private String[] allowAdministrators;

//	private final String[] excludeUrls = { "login.html", "login/graphic", ".js", ".css", "login/login", ".png", ".jpg",
//			"test" };

	@Around("execution(* com.bonc.caseDeclare.controller.*.*(..)) && !execution(* com.bonc.caseDeclare.controller.LoginController.*(..))")
	public JsonResult<Object> interceptor(ProceedingJoinPoint pjp) {
		Object obj = null;
		try {
			
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes sra = (ServletRequestAttributes) ra;
			HttpServletRequest req = sra.getRequest();
			HttpServletResponse response = sra.getResponse();
			req.setCharacterEncoding("utf-8");
			String path = req.getRequestURI();
			HttpSession session = req.getSession();

			Map<String, Object> map = (Map<String, Object>) session.getAttribute("declare_user");
			if (map == null || map.isEmpty()) {
				JsonResult<Object> jsonResult = new JsonResult<>(JsonResult.UNLOGIN, "未登录或登陆失效");
				logger.info(jsonResult.toString());
//				response.sendRedirect(loginHtml);
				return jsonResult;
			}
			String type = (String) map.get("type");
			String state = (String) map.get("state");
			String user_name = (String) map.get("user_name");
			logger.info("用户：【"+user_name+"】"+",身份为:【"+type+"】,正在访问:"+path);
			
			if(state.equals("2")) {
				logger.info("用户未完成注册，请完成注册再进行登陆！");
				return new JsonResult<>("用户未完成注册，请完成注册再进行登陆！");
			}else if(state.equals("0")) {
				logger.info("该用户被停用");
				return new JsonResult<>("该用户被停用");
			}
			
			boolean result = false;
			if (type.equals("3")) {
				//企业
				for (String string : allowEnterprise) {
					if (path.indexOf(string) != -1) {
						result = true;
						break;
					}
				}
				
				if(!result) {
					logger.info("访问结果：用户：【"+user_name+"】"+",身份为:【"+type+"】,无权访问");
					return new JsonResult<>("您无权访问");
				}

			} else if (type.equals("2")) {
				//专家
				for (String string : allowExpert) {
					if (path.indexOf(string) != -1) {
						result = true;
						break;
					}
				}
				if(!result) {
					logger.info("访问结果：用户：【"+user_name+"】"+",身份为:【"+type+"】,无权访问");
					return new JsonResult<>("您无权访问");
				}
				

			}else if(type.equals("1")) {
				//管理员 
				for (String string : allowAdministrators) {
					if (path.indexOf(string) != -1) {
						result = true;
						break;
					}
				}
				if(!result) {
					logger.info("访问结果：用户：【"+user_name+"】"+",身份为:【"+type+"】,无权访问");
					return new JsonResult<>("您无权访问");
				}
				
			}else {
				logger.info(user_name+":您的身份不明，请联系管理员");
				return new JsonResult<>("您的身份不明，请联系管理员");
			}

			obj = pjp.proceed();
			return (JsonResult<Object>) obj;
		} catch (Throwable e) {
			e.printStackTrace();
			SendEmailUtils.sendErrorEmail(e.getMessage());;
			return new JsonResult<>(e);
		}
	}
}
