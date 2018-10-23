package com.bonc.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author haixia.shi
 * @date 2017年9月8日
 *
 */
public class HttpUtils {

	private static String[] IEBrowserSignals = { "MSIE", "Trident", "Edge" };

	public static boolean isMSBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		for (String signal : IEBrowserSignals) {
			if (userAgent.contains(signal))
				return true;
		}
		return false;
	}
}
