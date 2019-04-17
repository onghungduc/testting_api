package com.it.ou.testapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.TimeControler;
import com.shopnail.api.utils.FileUtils;

public class TimeTest {
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	
	TimeControler ctrl = new TimeControler();
	
	@Test
    public void testCheckTimeStaff() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/checkTimeStaff_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		
		File file1 = new File(classLoader.getResource("request/checkTimeStaff.txt").getFile());
		String jb = FileUtils.readSqlFile(file1.toString());
		try {
			String actual = ctrl.checkTimeStaff(con, jb);
			JSONAssert.assertEquals(expected, actual, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
