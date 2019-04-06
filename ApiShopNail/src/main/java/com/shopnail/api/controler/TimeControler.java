package com.shopnail.api.controler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.HTTP;
import org.json.JSONObject;

import com.shopnail.api.utils.Utils;
import com.sun.javafx.scene.layout.region.Margins.Converter;

public class TimeControler {
	PreparedStatement stmt = null;
	ResultSet rs = null;

	JSONObject jsonObject = new JSONObject();
	JSONObject jsonSuccess = new JSONObject();
	
	public String checkTimeStaff(Connection con, String jb) throws SQLException {
		String isCheck = "false";
		JSONObject jsonRequest = new JSONObject(jb.toString());
		System.out.println("Vao day: jsonRequest == " + jb.toString());
		
		String timeWork = jsonRequest.getString("timeWork").replace(":", "");
		timeWork = jsonRequest.getString("timeWork").replace("AM", "");
		timeWork = jsonRequest.getString("timeWork").replace("PM", "");
		timeWork = jsonRequest.getString("timeWork").replace(":", "");
		
		System.out.println("Vao day: timeWork == " + timeWork.toString());
		
		ResultSet rs = null;
		String sql = "SELECT * FROM nail.dtb_time ";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
		while (rs.next()) {
			int itimeWork = Integer.parseInt(timeWork);
			
			int iStart = Integer.parseInt(rs.getString("start").replaceAll(":", "").replaceAll("AM", ""));
			if(rs.getString("start").contains("PM")) {
				iStart += 12;
				if(iStart == 24) {
					iStart = 0;
				}
			}
			int iEnd = Integer.parseInt(rs.getString("end").replaceAll(":", "").replaceAll("PM", ""));
			if(rs.getString("end").contains("PM")) {
				iEnd = iEnd +  1200;
				if(iEnd == 24) {
					iEnd = 0;
				}
			}
			
			System.out.println("Vao day: iStart == " + iStart);
			
			System.out.println("Vao day: iEnd == " + iEnd);
			
			if(itimeWork >= iStart && itimeWork <= iEnd) {
				isCheck = "true";
				jsonSuccess.put("code", "200");
				jsonSuccess.put("message", "This time ok.(This only text for test so do not display on app)");
			}
			else {
				isCheck = "false";
				jsonSuccess.put("code", "405");
				jsonSuccess.put("message", "Error, not exists time in system");
			}
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		
		System.out.println("Vao day: jsonObject == " + jsonObject);
		
		return jsonObject.toString();
	}
	
	
	public String getTime(Connection con) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_time";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" + "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"time\":[" ;
		while (rs.next()) {
			isCheck = "true";
			count ++;
			if(count == 1) {
				result += "{";
			}
			else {
				result += ",{";
			}
			result += "\"id\":" + rs.getString("id")  + ",";
			result += "\"start\":" + "\""+ rs.getString("start") + "\",";
			result += "\"end\":" + "\""+ rs.getString("end") + "\",";
			result += "\"created_at\":" + "\""+ rs.getString("created_at") + "\",";
			result += "\"updated_at\":" + "\""+ rs.getString("updated_at") + "\"";
			result += "}";
		}
		result += "]";
		result += "}";
		if(isCheck.equals("false")) {
			result = "{";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"message\":" + "\""+  "Get time false please try a gain"+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
}
