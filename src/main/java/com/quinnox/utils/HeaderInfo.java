package com.quinnox.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HeaderInfo {

	public HeaderInfo() {
		// TODO Auto-generated constructor stub
	}
		public static Map<String, String> getHeadersInfo(HttpServletRequest httpRequest) {

			Map<String, String> map = new HashMap<String, String>();
			Enumeration<?> headerNames = httpRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = httpRequest.getHeader(key);
				map.put(key, value);
			}
			return map;
		}
}
