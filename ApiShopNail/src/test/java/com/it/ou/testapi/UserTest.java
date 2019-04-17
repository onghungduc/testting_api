package com.it.ou.testapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.UserControler;
import com.shopnail.api.utils.FileUtils;

public class UserTest {
	
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	
	UserControler ctrl = new UserControler();
	
	@Test
    public void testLoginPass() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/loginPass.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		String userName = "111-222-3333";
		String password ="123456";
		try {
			String actual = ctrl.reponseCheckLogin(con, userName, password);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testLoginFail1() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/loginFail.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		String userName = "111-222-3333";
		String password ="12345678";
		try {
			String actual = ctrl.reponseCheckLogin(con, userName, password);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testLoginFail2() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/loginPass.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		String userName = "111-222-3333";
		String password ="12345678";
		try {
			String actual = ctrl.reponseCheckLogin(con, userName, password);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testForgotPassTrue() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/forgotPass_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		String phone = "111-222-3333";
		try {
			String actual = ctrl.reponseforgotPassword(con, phone);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testForgotPassFlase() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/forgotPass_False.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		String phone = "111-222-3332";
		try {
			String actual = ctrl.reponseforgotPassword(con, phone);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
