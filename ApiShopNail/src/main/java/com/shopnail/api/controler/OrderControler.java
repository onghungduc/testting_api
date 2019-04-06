package com.shopnail.api.controler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shopnail.api.utils.Utils;

public class OrderControler {
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	JSONObject jsonObject = new JSONObject();
	JSONObject jsonSuccess = new JSONObject();
	
	ArrayList<String> list = new ArrayList<String>();
	
	public String getCustomerInfo(Connection con, String jb) {
		list.clear();
		JSONObject jsonClients = null;
		JSONArray jsonArray = new JSONArray();
		
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb.toString());
		
		String staffId = jsonRequest.get("staffId").toString();
		String dateOrder = (jsonRequest.getString("date"));
		
		String sql = "select distinct a.id, a.name, a.phone\r\n" + 
				"from dtb_users a inner join dtb_order b\r\n" + 
				"	on a.id = b.user_id\r\n" + 
				"where date_order = ? and b.staff_id = ? and b.status <> ? ";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, dateOrder);
			stmt.setString(2, staffId);
			stmt.setString(3, "2");
		    rs = stmt.executeQuery();
		    System.out.println("Vao day: sql == " + sql);
		    System.out.println("Vao day: executeQuery ==" + dateOrder + " == staffid " + staffId);
			while (rs.next()) {
				System.out.println("Vao day: dateOrder ==" + dateOrder + " == staffid " + staffId);
				isCheck = "true";
				jsonClients = new JSONObject();
				jsonClients.put("clientId", rs.getInt("id"));
				jsonClients.put("clientName", rs.getString("name"));
				jsonClients.put("clientPhone", rs.getString("phone"));
				String sql2 = "select distinct a.id, a.name, a.phone, b.id order_id\r\n" + 
						"from dtb_users a inner join dtb_order b\r\n" + 
						"	on a.id = b.user_id\r\n" + 
						"where date_order = ? and b.user_id = ?  and b.status <> ? ";
				stmt = con.prepareStatement(sql2);
			    System.out.println("Vao day: sql2 == " + sql2);
			    System.out.println("Vao day: executeQuery2 ==" + dateOrder + " == user_id " + rs.getInt("id"));
				stmt.setString(1, dateOrder);
				stmt.setInt(2, rs.getInt("id"));
				stmt.setString(3, "2");
			    ResultSet rs2 = stmt.executeQuery();
			    list.clear();
				while (rs2.next()) {
				    list.add(rs2.getString("order_id"));
				    System.out.println("Order list: " + list);
				}
				jsonArray.put(jsonClients);
				jsonClients.put("clientOrderId", new JSONArray(list));
			}
			jsonSuccess.put("code", "200");
			jsonSuccess.put("clients", jsonArray);
		}
		catch(SQLException ex) {
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "getCustomerInfo fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		System.out.println("jsonObject: " + jsonObject);
		return jsonObject.toString();
	}
	
	public String getMyCustomer(Connection con, String jb) {
		JSONObject jsonCustomers = null;
		JSONObject jsonOrders = new JSONObject();
		JSONArray jsonArrayOrders = new JSONArray();
		JSONObject jsonProduct = null;
		JSONArray jsonArrayProduct = new JSONArray();
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb);
 		int orderId = (jsonRequest.getInt("orderId"));
 		System.out.println("orderId: " + orderId);
		String sql = "SELECT a.id userid, b.date_order , a.name as fullname, a.phone,\r\n" + 
				"b.id orderId, b.extra orderExtra, b.comment orderComment\r\n" + 
				"FROM nail.dtb_users a \r\n" + 
				"	inner join nail.dtb_order b on  a.id = b.user_id\r\n" + 
				"where b.id = ? and b.status <> ?";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, orderId);
			stmt.setInt(2, 2);
		    rs = stmt.executeQuery();
		    
			while (rs.next()) {
				System.out.println("vao day : rs.next() " + orderId);
				isCheck = "true";
				jsonCustomers = new JSONObject();
				jsonCustomers.put("userId", rs.getInt("userid"));
				jsonCustomers.put("fullname", rs.getString("fullname"));
				jsonCustomers.put("phone", rs.getString("phone"));
				jsonCustomers.put("date", rs.getString("date_order"));
				
				System.out.println("rs.getString(\"date_order\"): " + rs.getString("date_order"));
				
				
				String sql2 = "SELECT a.product_id, b.name product_name, a.price product_price, a.status\r\n" + 
						"FROM nail.dtb_order_detail a  \r\n" + 
						"	inner join nail.dtb_product b on  a.product_id= b.id\r\n" + 
						"Where a.order_id = ? ";
				stmt = con.prepareStatement(sql2);
				stmt.setInt(1, orderId);
			    ResultSet rs2 = stmt.executeQuery();
				while (rs2.next()) {
					jsonProduct = new JSONObject();
					
					System.out.println("rs.getString(\"productId\"): " + rs2.getString("product_id"));
					
					jsonProduct.put("productId",  rs2.getString("product_id"));
					jsonProduct.put("productName", rs2.getString("product_name"));
					jsonProduct.put("productPrice",  rs2.getString("product_price"));
					jsonProduct.put("status", rs2.getString("status"));
					jsonArrayProduct.put(jsonProduct);
				}
				jsonOrders.put("orderId", rs.getString("orderId"));
				jsonOrders.put("orderExtra", rs.getString("orderExtra"));
				jsonOrders.put("orderComment", rs.getString("orderComment"));
				jsonOrders.put("orderProducts", jsonArrayProduct);
				jsonArrayOrders.put(jsonOrders);
				
				jsonCustomers.put("orders", jsonArrayOrders);
			}
			jsonSuccess.put("code", "200");
			jsonSuccess.put("customers", jsonCustomers);
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "getCustomerInfo fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		System.out.println("jsonObject: " + jsonObject);
		return jsonObject.toString();
	}

	public String historyClientByStaff(Connection con, String jb) {
		
		JSONArray jsonArrayProduct = new JSONArray();
		JSONArray jsonArrayProducts = new JSONArray();
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		
		JSONObject jsonProducts = null;
		JSONObject jsonProduct = null;
		String isCheck = "true";
		
		JSONArray ja = new JSONArray(jb.toString());
        int n = ja.length();
        System.out.println("count: == " + n);
        for (int i = 0; i < n; i++) {
        	System.out.println("get i: " + ja.getString(i));
        	String sql = " SELECT * FROM nail.dtb_order where id = ?  and status <> ?";
    		try {
    			stmt = con.prepareStatement(sql);
    			stmt.setString(1, ja.getString(i));
    			stmt.setString(2, "2");
    		    rs = stmt.executeQuery();
    		    jsonProducts = new JSONObject();
    			while (rs.next()) {
    				jsonArrayProduct = new JSONArray();
    				int order_id = rs.getInt("id");
    				System.out.println("order_id: " + order_id);
    				jsonProducts.put("orderId", rs.getInt("id"));
    				jsonProducts.put("extraMoney", rs.getString("extra"));
    			    isCheck = "true";
    			    String sql2 = " SELECT a.product_Id, b.name productName, a.price FROM nail.dtb_order_detail a inner join dtb_product  b on a.product_id = b.id  where a.order_id = ?  ";
    			    stmt2 = con.prepareStatement(sql2);
    				stmt2.setInt(1, order_id);
    				rs2 = stmt2.executeQuery();
    				while (rs2.next()) {
    					jsonProduct = new JSONObject();
    					System.out.println("productId: " + rs2.getInt("product_Id"));
    					jsonProduct.put("productId", rs2.getInt("product_Id"));
    					jsonProduct.put("productName", rs2.getString("productName"));
    					jsonProduct.put("productPrice", rs2.getString("price"));
    					jsonArrayProduct.put(jsonProduct);
    				}
    				jsonProducts.put("product", jsonArrayProduct);
    			}
    			jsonArrayProducts.put(jsonProducts);
    			jsonSuccess.put("products", jsonArrayProducts);
    			jsonSuccess.put("code", "200");
        }
		catch(SQLException ex) {
			ex.printStackTrace();
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "historyClientByStaff fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
        }
        System.out.println("jsonObject" + jsonObject.toString());
        return jsonObject.toString();
	}
	
	public String updateStatusService(Connection con, String jb) {
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb);
 		int orderId = (jsonRequest.getInt("orderId"));
		JSONArray arr = jsonRequest.getJSONArray("products");
		for (int i = 0; i< arr.length(); i++){
		    System.out.println("productId: " + arr.getJSONObject(i).getString("productId"));
		    System.out.println("status: " + arr.getJSONObject(i).getInt("status"));
		    
			String sql = "update nail.dtb_order_detail set status = ? where  order_id = ? and product_id = ?";
			try {
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, arr.getJSONObject(i).getInt("status"));
				stmt.setInt(2, orderId);
				stmt.setString(3, arr.getJSONObject(i).getString("productId"));
			    stmt.executeUpdate();
			    isCheck = "true";
			}
			catch(SQLException ex) {
				ex.printStackTrace();
			}
        }
		
		JSONObject jsonCustomers = null;
		JSONObject jsonOrders = new JSONObject();
		JSONArray jsonArrayOrders = new JSONArray();
		JSONObject jsonProduct = null;
		JSONArray jsonArrayProduct = new JSONArray();
		
 		System.out.println("orderId: " + orderId);
		String sql = "SELECT a.id userid, b.date_order , a.name as fullname, a.phone,\r\n" + 
				"b.id orderId, b.extra orderExtra, b.comment orderComment\r\n" + 
				"FROM nail.dtb_users a \r\n" + 
				"	inner join nail.dtb_order b on  a.id = b.user_id\r\n" + 
				"where b.id = ? ";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, orderId);
		    rs = stmt.executeQuery();
		    
			while (rs.next()) {
				System.out.println("vao day : rs.next() " + orderId);
				isCheck = "true";
				jsonCustomers = new JSONObject();
				jsonCustomers.put("userId", rs.getInt("userid"));
				jsonCustomers.put("fullname", rs.getString("fullname"));
				jsonCustomers.put("phone", rs.getString("phone"));
				jsonCustomers.put("date", rs.getString("date_order"));
				
				System.out.println("rs.getString(\"date_order\"): " + rs.getString("date_order"));
				
				
				String sql2 = "SELECT a.product_id, b.name product_name, a.price product_price, a.status\r\n" + 
						"FROM nail.dtb_order_detail a  \r\n" + 
						"	inner join nail.dtb_product b on  a.product_id= b.id\r\n" + 
						"Where a.order_id = ? ";
				stmt = con.prepareStatement(sql2);
				stmt.setInt(1, orderId);
			    ResultSet rs2 = stmt.executeQuery();
				while (rs2.next()) {
					jsonProduct = new JSONObject();
					
					System.out.println("rs.getString(\"productId\"): " + rs2.getString("product_id"));
					
					jsonProduct.put("productId",  rs2.getString("product_id"));
					jsonProduct.put("productName", rs2.getString("product_name"));
					jsonProduct.put("productPrice",  rs2.getString("product_price"));
					jsonProduct.put("status", rs2.getString("status"));
					jsonArrayProduct.put(jsonProduct);
				}
				jsonOrders.put("orderId", rs.getString("orderId"));
				jsonOrders.put("orderExtra", rs.getString("orderExtra"));
				jsonOrders.put("orderComment", rs.getString("orderComment"));
				jsonOrders.put("orderProducts", jsonArrayProduct);
				jsonArrayOrders.put(jsonOrders);
				
				jsonCustomers.put("orders", jsonArrayOrders);
			}
			jsonSuccess.put("code", "200");
			jsonSuccess.put("customers", jsonCustomers);
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "getCustomerInfo fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		System.out.println("jsonObject: " + jsonObject);
		return jsonObject.toString();
	}
	
	public String bookingServiceOnline(Connection con, String jb) throws SQLException {
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb);
		UserControler ctrl = new UserControler();
		
		//{"fullname":"ducoh","phone":"999-999-9999","email":"onghungduccntt@gmail.com",
		//"values":[{"dateOrder":"2019-02-17","note":"tees","staffId":2,
		//"product":{"productId":1,"price":"10","timeOrder":"13:43"}}
		//]}
		String fullname = (jsonRequest.getString("fullname"));
 		String phone = (jsonRequest.getString("phone"));
 		String email = (jsonRequest.getString("email"));
 		
 		int userId = ctrl.insertNewClient(con, fullname, phone, email);
 		System.out.println("userId okey: " + userId);
		JSONArray arrValues = jsonRequest.getJSONArray("values");
		for (int i = 0; i< arrValues.length(); i++){
		    System.out.println("dateOrder: " + arrValues.getJSONObject(i).getString("dateOrder"));
		    System.out.println("note: " + arrValues.getJSONObject(i).getString("note"));
		    System.out.println("staffId: " + arrValues.getJSONObject(i).getInt("staffId"));
		    String arrProduct = arrValues.getJSONObject(i).get("product").toString();
			String sql = "insert into nail.dtb_order(date_order,extra,user_id, staff_id, comment) values(?,?,?,?,?) ";
			try {
				System.out.println("vao day: " + userId);
				stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, arrValues.getJSONObject(i).getString("dateOrder"));
				stmt.setInt(2, 0);
				stmt.setInt(3, userId);
				stmt.setInt(4,  arrValues.getJSONObject(i).getInt("staffId"));
				stmt.setString(5, arrValues.getJSONObject(i).getString("note"));
			    stmt.executeUpdate();
			    rs = stmt.getGeneratedKeys();
			    if (rs.next()) {
			    	int orderId = rs.getInt(1);
			    	JSONObject jsProduct = new JSONObject(arrProduct);
			    	System.out.println("da co orderId: " + orderId);
			    	System.out.println("productId: " + jsProduct.getInt("productId"));
					System.out.println("price: " + jsProduct.getString("price"));
					System.out.println("timeOrder: " + jsProduct.getString("timeOrder"));
			    	String sql2 = "insert into nail.dtb_order_detail(order_id, product_id,price, time_order) values(?,?,?,?) ";
		            stmt = con.prepareStatement(sql2);
		            stmt.setInt(1, orderId);
					stmt.setInt(2, jsProduct.getInt("productId"));
					stmt.setString(3, jsProduct.getString("price"));
				    stmt.setString(4, jsProduct.getString("timeOrder"));
				    stmt.executeUpdate();
			    }
			    isCheck = "true";
				jsonSuccess.put("code", "200");
				jsonSuccess.put("message", "Your order has been successful.");
			}
			catch(SQLException ex) {
				ex.printStackTrace();
				isCheck = "false";
				jsonSuccess.put("code", "405");
				jsonSuccess.put("message", "Your order has been fail.");
			}
		    System.out.println("arrProduct: " + arrProduct);
        }
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		System.out.println("jsonObject: " + jsonObject);
		return jsonObject.toString();
	}
	
	public String getCustomerTimeOrder(Connection con, String jb) {
		
		JSONArray jsonArrayTime = new JSONArray();
		JSONArray jsonArrayTimes = new JSONArray();
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		
		JSONObject jsonTimes = null;
		String isCheck = "true";
		
		JSONArray ja = new JSONArray(jb.toString());
        int n = ja.length();
        System.out.println("count: == " + n);
        for (int i = 0; i < n; i++) {
        	System.out.println("get i: " + ja.getString(i));
        	String sql = " SELECT * FROM nail.dtb_order where id = ?  and status <> ?";
    		try {
    			stmt = con.prepareStatement(sql);
    			stmt.setString(1, ja.getString(i));
    			stmt.setString(2, "2");
    		    rs = stmt.executeQuery();
    			while (rs.next()) {
    				jsonArrayTime = new JSONArray();
    				jsonTimes = new JSONObject();
    				int order_id = rs.getInt("id");
    				System.out.println("order_id: " + order_id);
    				jsonTimes.put("orderId", rs.getString("id"));
    			    isCheck = "true";
    			    String sql2 = " SELECT distinct a.time_order FROM nail.dtb_order_detail a where a.order_id = ?  ";
    			    stmt2 = con.prepareStatement(sql2);
    				stmt2.setInt(1, order_id);
    				rs2 = stmt2.executeQuery();
    				while (rs2.next()) {
    					jsonArrayTime.put(rs2.getString("time_order"));
    				}
    				jsonTimes.put("time", jsonArrayTime);
    			}
    			jsonArrayTimes.put(jsonTimes);
    			jsonSuccess.put("time", jsonArrayTimes);
    			jsonSuccess.put("code", "200");
        }
		catch(SQLException ex) {
			ex.printStackTrace();
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "historyClientByStaff fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
        }
        System.out.println("jsonObject" + jsonObject.toString());
        return jsonObject.toString();
	}
	
	
	public String updateExtraByOrder(Connection con, String jb) {
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb.toString());
		int orderId = jsonRequest.getInt("orderId");
		int extra = jsonRequest.getInt("extra");
		System.out.println("orderid" + orderId);
		
		String sql = "update nail.dtb_order set extra = ? where  id = ?";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, extra);
			stmt.setInt(2, orderId);
		    stmt.executeUpdate();
		    isCheck = "true";
			jsonSuccess.put("code", "200");
			jsonSuccess.put("message", "Service update successfully.");
		}
		catch(SQLException ex) {
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "Service update fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		return jsonObject.toString();
	}
	
	public static java.sql.Time getCurrentJavaSqlTime() {
	    java.util.Date date = new java.util.Date();
	    return new java.sql.Time(date.getTime());
	  }
	
	public String insertOrder(Connection con, String jb) {
		String isCheck = "true";
		JSONObject jsonRequest = new JSONObject(jb.toString());
		int orderId = -1;
		String userId = jsonRequest.getString("userId");
		String staffId = jsonRequest.getString("staffId");
		int extra = jsonRequest.getInt("extra");
		System.out.println("staffId: " + staffId);
		
		String dateOrder = (jsonRequest.getString("dateOrder"));
		String sql = "insert into nail.dtb_order(date_order,extra,user_id, staff_id) values(?,?,?,?) ";
		try {
			System.out.println("vao day: " + staffId);
			
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, dateOrder);
			stmt.setInt(2, extra);
			stmt.setString(3, userId);
			stmt.setString(4, staffId);
		    stmt.executeUpdate();
		    rs = stmt.getGeneratedKeys();
		    if (rs.next()) {
		    	orderId = rs.getInt(1);
		    	System.out.println("da co orderId: " + orderId);
				JSONArray arr = jsonRequest.getJSONArray("values");
		        for (int i = 0; i < arr.length(); i++) {
		            JSONObject jo = arr.getJSONObject(i);
		            int productId = jo.getInt("productId");
		            String price = jo.getString("price");
		            System.out.println("get Order: \"productId\":" + productId);
		            System.out.println("get Order: \"price\":" + price);
		            java.sql.Time time = getCurrentJavaSqlTime();
				    System.out.println("time=" + time);
		            String sql2 = "insert into nail.dtb_order_detail(order_id, product_id,price, time_order) values(?,?,?,?) ";
		            stmt = con.prepareStatement(sql2);
		            stmt.setInt(1, orderId);
					stmt.setInt(2, productId);
					stmt.setString(3, price);
				    stmt.setTime(4, time);
				    stmt.executeUpdate();
		        }
		    }
		    isCheck = "true";
			jsonSuccess.put("code", "200");
			jsonSuccess.put("message", "Your order has been successful.");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			isCheck = "false";
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "Your order has been fail.");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		return jsonObject.toString();
	}
}
