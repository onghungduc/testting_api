package com.shopnail.api.utils;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class Utils {
	
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public static Date formatDate(Object strDate){
		Date date = null;
		if(null == strDate) return date;
		try {
			String start_dt = strDate.toString();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 			
			date = (Date)formatter.parse(start_dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public String digestMsg(String message) {
		String data = "";
		String private_key = "" ; 
		try {
			private_key =  "private_key_14101992";
			final String msg = String.format("%s|%s", message, private_key);
			byte[] hash = MessageDigest.getInstance("SHA-512").digest(msg.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				hexString.append(Integer.toHexString((hash[i] & 0x000000FF) | 0xFFFFFF00).substring(6));
			}
			data = hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("data: " + data);
		return data;
	}
	
	public String genCheckBox(java.sql.Connection con) throws SQLException  {
		
		String result = "";
		String isCheck = "true";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_setting";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
		while (rs.next()) {
			result = "{";
				result += "\"status\":" +isCheck + ",";
				result += "\"success\":{";
				result += "\"code\":200" + ",";
				result += "\"setting\":{";
				result += "\"id\":" + rs.getString("id")  + ",";
				result += "\"service_number\":" + "\""+ rs.getString("service_number") + "\"" + ",";
				result += "\"wax\":" + "\""+ rs.getString("wax") + "\"" + ",";
				result += "\"bonus\":" + "\""+ rs.getString("bonus") + "\"" + ",";
				result += "\"created_at\":" + "\""+ rs.getString("created_at") + "\"" + ",";
				result += "\"updated_at\":" + "\""+ rs.getString("updated_at") + "\"" ;
				result += "}";
				result += "}";
			result += "}";
		}
		return result;
	}
	public static LocalTime localTime() {
		System.out.println(java.time.LocalTime.now());
		return java.time.LocalTime.now();
	}
	public static void main(String[] args) {
		Utils u = new Utils();
		u.digestMsg("matkhau123");
		
		System.out.println(java.time.LocalTime.now());
	}
}
