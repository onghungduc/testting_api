package com.shopnail.api.controler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceTypeControler {
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public String getServiceType(Connection con) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_service_type";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" +  "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"serviceType\":[" ;
		while (rs.next()) {
			count ++;
			
			isCheck = "true";
			
			if(count == 1) {
				result += "{";
			}
			else {
				result += ",{";
			}
			result += "\"id\":" + rs.getString("id")  + ",";
			result += "\"name\":" + "\""+ rs.getString("name") + "\""  + ",";
			result += "\"created_at\":" + "\""+ rs.getString("created_at") + "\""  + ",";
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
			result += "\"message\":" + "\""+  "Get false please try a gain"+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
	
	
	public String getAllService(Connection con) throws SQLException {
		PreparedStatement stmt2 = null;
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT id, name, price FROM nail.dtb_product  order by id ";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" +  "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"product\":[" ;
		while (rs.next()) {
			count ++;
			isCheck = "true";
			if(count == 1) {
				result += "{";
			}
			else {
				result += ",{";
			}
			result += "\"cateName\":" + "\""+ rs.getString("name") + "\""  + ",";
			result += "\"cateChild\":[";
			
			//Than body
			ResultSet rs2 = null;
			String sql2 = "SELECT id, name, price FROM nail.dtb_product where parent = ? order by id ";
			stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, rs.getString("id"));
		    rs2 = stmt2.executeQuery();
		    int count2 = 0;
			while (rs2.next()) {
				count2 ++;
				isCheck = "true";
				if(count2 == 1) {
					result += "{";
				}
				else {
					result += ",{";
				}
				result += "\"productId\":" + "\""+ rs2.getString("id") + "\""  + ",";
				result += "\"productName\":" + "\""+ rs2.getString("name") + "\""  + ",";
				result += "\"productPrice\":" + "\""+ rs2.getString("price") + "\"" ;
				result += "}";
			}
			result += "]";
			//=========================
			result += "}";
		}
		result += "]";
		result += "}";
		if(isCheck.equals("false")) {
			result = "{";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"message\":" + "\""+  "Get false please try a gain"+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
}
