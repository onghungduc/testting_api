package com.shopnail.api.controler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shopnail.api.utils.Utils;
import sun.misc.BASE64Decoder;

@SuppressWarnings("unused")
public class UserControler {
	
	Utils u = new Utils();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	public static boolean checkAuthorization(Connection con,HttpServletRequest request){
		String authorization = request.getHeader("Authorization");
		String token = "Bearer " + "8fQClFOUfTIH6W6SBcy8beIGxtSrCPqzk9W0rFIvuHec9wP5f9S4qNLgYgkZ";
		System.out.println(authorization);
		if(authorization.length() > 0) {
			if(token.equals(authorization)) {
				System.out.println("Okey");
				return true;
			}
			else
				return false;
		}
		System.out.println(authorization);
		return true;
	}
	public String reponseCheckLogin(Connection con, String userName, String password) throws SQLException {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonSuccess = new JSONObject();
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_users where phone = ? and  password = ? and role = 1 ";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, userName);
		stmt.setString(2, u.digestMsg(password));
	    rs = stmt.executeQuery();
		while (rs.next()) {
			isCheck = "true";
			jsonSuccess.put("code", "200");
			jsonSuccess.put("token", rs.getString("_token"));
			jsonSuccess.put("id", rs.getInt("id"));
			jsonSuccess.put("username", rs.getString("username"));
			jsonSuccess.put("name", rs.getString("name"));
			jsonSuccess.put("email", rs.getString("email"));
			jsonSuccess.put("avatar", rs.getString("avatar"));
		    System.out.println("Json: "+jsonSuccess);
		}
		if(isCheck.equals("false")) {
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "Register false please try a gain");
		}
		jsonObject.put("status", isCheck);
		jsonObject.put("success", jsonSuccess);
		return jsonObject.toString();
	}
	
	public String changeAvatar(Connection con, String base64String, String userId) throws SQLException  {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonSuccess = new JSONObject();
		@SuppressWarnings("restriction")
		BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes;
		try {
			decodedBytes = decoder.decodeBuffer(base64String);
	        String uploadFile = "E:\\Java\\workspace\\Java\\ApiShopNail\\src\\main\\webapp\\images\\" + userId +".png";
			BufferedImage image;
			image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
			
			String sql = "update dtb_users set  avatar = ? where id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, "http://192.168.236.2:1003/ApiShopNail/images/" + userId+".png");
			stmt.setString(2, userId);
		    stmt.executeUpdate();
		    
			File f = new File(uploadFile);
			ImageIO.write(image, "png", f);
			
			System.out.println("Okey");
			jsonObject.put("status", "true");
			jsonSuccess.put("code", "200");
			jsonSuccess.put("message", "Updated !!!");
			
			jsonObject.put("success", jsonSuccess);
			
		} catch (IOException e1) {
			jsonObject.put("status", "false");
			jsonSuccess.put("code", "405");
			jsonSuccess.put("message", "Updated false!!!");
			e1.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	public String reponseClientLogin(Connection con, String phone) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_users where phone = ? and role = 2";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, phone);
	    rs = stmt.executeQuery();
		result = "{";
		while (rs.next()) {
			isCheck = "true";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":200" + ",";
			result += "\"user_id\":" + rs.getString("id")  + ",";
			result += "\"fullname\":" + "\""+ rs.getString("username") + "\"" + ",";
			result += "\"phone\":" + "\""+ rs.getString("phone") + "\"";
			result += "}";
		}
		if(isCheck.equals("false")) {
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"message\":" + "\""+  "Register false please try a gain"+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
	
	
	public String reponseforgotPassword(Connection con, String phone) throws SQLException {
		String result = "";
		String isCheck = "true";
		
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_users where phone = ? ";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, phone);
	    rs = stmt.executeQuery();
	    int count = 0;
		while (rs.next()) {
			count ++;
			sql = "update dtb_users set  password = ? where phone = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, u.digestMsg("123456"));
			stmt.setString(2, phone);
		    stmt.executeUpdate();
			result = "{";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":200" + ",";
			result += "\"message\":" + "\""+  "Your password is updated with 123456"+ "\" ";
			result += "}";
			result += "}";
		}
		if(count == 0) {
			result = "{";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"message\":" + "\""+  "Phone not exists"+ "\" ";
			result += "}";
			result += "}";
		}
		return result;
	}
	
	public String reponseUpdatePassWord(Connection con, String password, String password_new, String password_confirmation, String id) throws SQLException {
		String result = "";
		
		String isCheck = "false";
		String code = "405";
		String msg = "Error, password is not update";
		
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_users where id = ? and  password = ? ";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, id);
		stmt.setString(2, u.digestMsg(password));
	    rs = stmt.executeQuery();
	    
		while (rs.next()) {
			if(password_new.equals(password_confirmation)) {
				code = "200";
				isCheck = "true";
				String sql2 = "update dtb_users set password = ? where id = ?";
				stmt = con.prepareStatement(sql2);
				stmt.setString(1, u.digestMsg(password_new));
				stmt.setString(2, id);
			    stmt.execute();
			    msg = "Your password is updated";
			}
			else {
				code = "403";
				msg =  "Password is not equals with password_confirmation";
			}
		}
		result = "{";
		result += "\"status\":" +isCheck + ",";
		result += "\"success\":{";
		result += "\"code\":" + code +  ",";
		result += "\"message\":" + "\""+  msg + "\" ";
		result += "}";
		result += "}";
		return result;
	}
	
	public int checkPhoneExists(Connection con, String phone) throws SQLException {
		ResultSet rs = null;
		String sql = "SELECT * FROM dtb_users where phone = ? ";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, phone);
	    rs = stmt.executeQuery();
		while (rs.next()) {
			return rs.getInt("id");
		}
		return -1;
	}
	
	public int insertNewClient(Connection con, String name, String phone , String email) throws SQLException {
		
		if(checkPhoneExists(con, phone) > 0) {
			return checkPhoneExists(con, phone);
		}
		else {
			String sql = "insert into nail.dtb_users(username,name,phone, email, _token,password, role) values(?,?,?,?,?,?,?) ";
			try {
				stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, name);
				stmt.setString(2, name);
				stmt.setString(3, phone);
				stmt.setString(4, email);
				stmt.setString(5, u.digestMsg(name + phone));
				stmt.setString(6, u.digestMsg("123456"));
				stmt.setInt(7, 2);
			    stmt.executeUpdate();
			    rs = stmt.getGeneratedKeys();
			    if (rs.next()) {
			    	return rs.getInt(1);
			    }
			    return -1;
			}
			catch(SQLException ex) {
				return -1;
			}
		}
	}
	public String reponseNewClient(Connection con, String name, String phone, String email){
		
		String result = "";
		String isCheck = "false";
		int newId  = 0;
		ResultSet rs = null;
		result = "{";
		String sql = "insert into nail.dtb_users(username,name,phone, email, _token,password, role) values(?,?,?,?,?,?,?) ";
		try {
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, name);
			stmt.setString(3, phone);
			stmt.setString(4, email);
			stmt.setString(5, u.digestMsg(name + phone));
			stmt.setString(6, u.digestMsg("123456"));
			stmt.setInt(7, 2);
		    stmt.executeUpdate();
		    rs = stmt.getGeneratedKeys();
		    if (rs.next()) {
		    	newId = rs.getInt(1);
		    	System.out.println("Id: " + newId);
		    	isCheck = "true";
				result += "\"status\":" +isCheck + ",";
				result += "\"success\":{";
				result += "\"code\":200" + ",";
				result += "\"last_id\":" + newId  + ",";
				result += "\"name\":" + "\""+ name + "\"" + ",";
				result += "\"name\":" + "\""+ phone + "\"" + ",";
				result += "\"message\":" + "\""+  "New customer created."+ "\" ";
				result += "}";
		    }
		    else {
		    	result += "\"status\":" +isCheck + ",";
				result += "\"success\":{";
				result += "\"code\":405" + ",";
				result += "\"message\":" + "\""+  "Error."+ "\" ";
				result += "}";
		    }
		}
		catch(SQLException ex) {
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"message\":" + "\""+  "Error user name is exists."+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
	
	
	public String reponseGetUserById(Connection con, int userId) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM nail.dtb_users where id = ? ";
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, userId);
	    rs = stmt.executeQuery();
		result = "{";
		while (rs.next()) {
			isCheck = "true";
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":200" + ",";
			result += "\"id\":" + rs.getString("id")  + ",";
			result += "\"fullname\":" + "\""+ rs.getString("username") + "\"" + ",";
			result += "\"avatar\":" + "\"" +  rs.getString("avatar") + "\"";
			result += "}";
		}
		if(isCheck.equals("false")) {
			result += "\"status\":" +isCheck + ",";
			result += "\"success\":{";
			result += "\"code\":405" + ",";
			result += "\"msg\":" + "\""+  "User not exists"+ "\" ";
			result += "}";
		}
		result += "}";
		return result;
	}
	
	public String reponseGetAllStaff(Connection con) throws SQLException {
		String result = "";
		String isCheck = "false";
		ResultSet rs = null;
		String sql = "SELECT * FROM nail.dtb_users where role = ? ";
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, 1);
	    rs = stmt.executeQuery();
	    int count = 0;
		result = "{";
		isCheck = "true";
		result += "\"status\":" +  "true" + ",";
		result += "\"success\":{";
		result += "\"code\":200" + ",";
		result += "\"staff\":[" ;
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
