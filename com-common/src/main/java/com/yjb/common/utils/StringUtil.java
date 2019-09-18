package com.yjb.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String joinString(List<String> array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.get(i).toString();
				if (temp != null && temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1)
				result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	public static String joinString(String[] array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				String temp = array[i];
				if (temp != null && temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1)
				result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	public static List<String> mkList(String str, String regx) {
		List<String> list = new ArrayList<String>();
		String[] array = str.split(regx);
		for (String value : array) {
			list.add(value);
		}
		return list;
	}

	public static String getKeyValue(String str, String regx, String key) {

		String[] params = str.split(regx);
		for (String param : params) {
			String[] values = param.split("=");
			if (key.equals(values[0].trim())) {
				return values[1].trim();
			}
		}
		return str;
	}

	public static String areaText(String ptext, String etext, String searchWord, int wlength, boolean merge) {
		if (!etext.contains(searchWord))
			return null;
		String wordText = etext;
		int start = etext.indexOf(searchWord);
		int end = start;
		if (merge) // true 不合并多个关键字
			end = etext.lastIndexOf(searchWord);
		String startStr = etext.substring(0, start);
		String endStr = etext.substring(end + searchWord.length());
		if (ptext != null && !ptext.trim().equals("")) { // 父文本不为空时
			if (startStr.length() < wlength) { // 从父文本中剪切响应补充关键字左边文本
				int diff = wlength - startStr.length();
				int newStart = ptext.indexOf(etext) - diff;
				if (newStart < 0)
					newStart = 0;
				startStr = ptext.substring(newStart, (newStart + wlength) > ptext.length() ? ptext.length()
						: (newStart + wlength));
			} else {
				startStr = startStr.substring(startStr.length() - wlength);
			}
			if (endStr.length() < wlength) { // 从父文本中剪切响应补充关键字右边文本
				int subIndex = ptext.indexOf(etext);
				int diff = wlength - endStr.length();
				int newEnd = subIndex + etext.length() + diff;
				if (newEnd > ptext.length())
					newEnd = ptext.length();
				endStr = ptext.substring(subIndex + etext.length() - endStr.length(), newEnd);
			} else {
				endStr = endStr.substring(0, wlength);
			}
			wordText = startStr + etext.substring(start, end + searchWord.length()) + endStr;
		} else {
			if (startStr.length() > wlength) // 剪切 多余的长度文本
				startStr = startStr.substring(startStr.length() - wlength);
			if (endStr.length() > wlength)
				endStr = endStr.substring(0, wlength);
			wordText = startStr + etext.substring(start, end + searchWord.length()) + endStr;
		}
		return wordText;
	}

	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String nvl(Object obj, String other) {
		if (obj == null)
			return other;
		else
			return obj.toString();
	}

	/**
	 * 替换所有变量值
	 * 
	 * @param str
	 *            输入的整个String
	 * @param src
	 *            被替换的string
	 * @param replacement
	 *            替换后的值
	 * @return
	 */
	public static String replaceAll(String str, String src, String replacement) {
		Pattern p = Pattern.compile(src);
		Matcher m = p.matcher(str);
		String result = m.replaceAll(Matcher.quoteReplacement(replacement));
		return result;
	}
	

	public static List<String> distinct(String[] str) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			if (!list.contains(str[i]))
				list.add(str[i]);
		}
		return list;
	}

	public static String encode(String str) {
		return new String(new Base64().encode(str.getBytes()));
	}
	
	public static void main(String[] args) {
		//System.out.println(new String(new Base64().encode("智行者".getBytes())));
		String aString = "会计/金融/银行/保险 ";
		String bString = " aaaa ";
		System.out.println(StringUtils.trimToNull(aString));
		System.out.println(StringUtils.trimToNull(bString));
		
	}

}
