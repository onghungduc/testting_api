package com.shopnail.api.controler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NavigateStaffControler {
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	JSONObject jsonObject = new JSONObject();
	JSONObject jsonSuccess = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	
	private void checkExistsServiceByStaff(Connection con,int staffId, int orderNumber, int type, int value) throws SQLException {
		String sql = "SELECT * FROM dtb_navigate_staff where type = ? and staff_id = ? and order_number = ?";
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, type);
		stmt.setInt(2, staffId);
		stmt.setInt(3, orderNumber);
	    rs = stmt.executeQuery();
	    while (rs.next()) {
	    	
	    	String sql2 = "update dtb_navigate_staff set type where type = ? and staff_id = ? and order_number = ?";
	    	switch(type) {
	    		case 0: 
	    			sql2 = "update dtb_navigate_staff set is_enable = ?, service_id = ? where type = ? and staff_id = ? and order_number = ?";
	    			break;
	    		case 1: 
	    			sql2 = "update dtb_navigate_staff set is_enable = ?, wax = ? where type = ? and staff_id = ? and order_number = ?";
	    			break;
	    		case 3: 
	    			sql2 = "update dtb_navigate_staff set is_enable = ?, bonus = ? where type = ? and staff_id = ? and order_number = ?";
	    			break;
	    	}
			stmt = con.prepareStatement(sql2);
			stmt.setInt(1, value);
			stmt.setInt(2, value);
			stmt.setInt(3, orderNumber);
		    rs = stmt.executeQuery();
	    }
	}
	public String addOrUpdateServiceChecking(Connection con, String jb) throws SQLException {
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb);
		UserControler ctrl = new UserControler();
		//{"staffId":"1","services":[{"type":2,"order":1,"value":1},{"type":1,"order":1,"value":1},{"type":1,"order":2,"value":1}]}
		String staffId = (jsonRequest.getString("staffId"));
 		System.out.println("staffId okey: " + staffId);
		JSONArray arrValues = jsonRequest.getJSONArray("services");
		System.out.println("arrValues okey: " + arrValues);
		for (int i = 0; i< arrValues.length(); i++){
		    System.out.println("type: " + arrValues.getJSONObject(i).getInt("type"));
		    System.out.println("order: " + arrValues.getJSONObject(i).getInt("order"));
		    System.out.println("value: " + arrValues.getJSONObject(i).getInt("value"));
        }
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		System.out.println("jsonObject: " + jsonObject);
		return jsonObject.toString();
	}
	
	public String getProductByCate(Connection con) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_navigate_staff";
		stmt = con.prepareStatement(sql);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" +  "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"navigates\":[" ;
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
			result += "\"staff_id\":" + "\""+ rs.getString("staff_id") + "\""  + ",";
			result += "\"is_enable\":" + "\""+ rs.getString("is_enable") + "\""  + ",";
			result += "\"service_id\":" + "\""+ rs.getString("service_id") + "\""  + ",";
			result += "\"bonus\":" + "\""+ rs.getString("bonus") + "\""  + ",";
			result += "\"order_number\":" + "\""+ rs.getString("order_number") + "\""  + ",";
			result += "\"type\":" + "\""+ rs.getString("type") + "\""  + ",";
			result += "\"date\":" + "\""+ rs.getString("date") + "\"" ;
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
