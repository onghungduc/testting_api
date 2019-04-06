package com.shopnail.api.controler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductControler {
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public String getProductByCate(Connection con, int parentId) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_product ";
		stmt = con.prepareStatement(sql);
		if(parentId > 0) {
			sql = "SELECT * FROM dtb_product where parent =  ?";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, parentId);
		}
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" +  "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"data\":[" ;
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
			result += "\"image\":" + "\""+ rs.getString("image") + "\""  + ",";
			result += "\"parent\":" + "\""+ rs.getString("parent") + "\""  + ",";
			result += "\"desc\":" + "\""+ rs.getString("desc") + "\""  + ",";
			result += "\"price\":" + "\""+ rs.getString("price") + "\""  + ",";
			result += "\"is_new\":" + "\""+ rs.getString("is_new") + "\""  + ",";
			result += "\"is_hot\":" + "\""+ rs.getString("is_hot") + "\""  + ",";
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
	public String getAllCategory(Connection con) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_product";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		result += "\"status\":" +"true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"data\":[" ;
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
			result += "\"name\":" + "\""+ rs.getString("name") + "\"";
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
