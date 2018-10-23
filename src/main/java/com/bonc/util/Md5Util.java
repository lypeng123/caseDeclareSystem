package com.bonc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * md5加密工具
 * @author zhijie.ma
 * @date 2017年9月8日
 *
 */
public class Md5Util {

	private static final String salt = "magic.bonc.com.cn";
	
	/**
	 * md5进行加密
	 * @param pwd
	 * @return
	 */
	public static String encoding(String pwd){
		String newpwd =  "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(salt.getBytes());
			Encoder encoder = Base64.getEncoder();
			newpwd  =  encoder.encodeToString(md5.digest(pwd.getBytes())).replaceAll("[+]", "");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return newpwd;
	}
	
}
