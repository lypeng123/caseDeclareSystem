package com.bonc.util;

import java.util.Random;

/**
*	密码生成器	<br>
*	6位数字+2位字母
* @author zhijie.ma
* @date 2017年9月10日
* 
*/
public class GeneratorPassword {

	/**
	 * 6位数字+2为字母密码
	 * @param numLength	几位数字
	 * @param strLength 几位字母
	 * @return
	 */
	public static String getPassword(int numLength,int strLength) {
		String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String num="0123456789";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<numLength;i++){
			char ch=num.charAt(new Random().nextInt(num.length()));
			sb.append(ch);
		}
		for(int i=0;i<strLength;i++) {
			char ch=str.charAt(new Random().nextInt(str.length()));
			sb.append(ch);
		}
		return sb.toString();
		
	}
	
}
