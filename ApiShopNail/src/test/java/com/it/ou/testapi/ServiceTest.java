package com.it.ou.testapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.ServiceTypeControler;
import com.shopnail.api.utils.FileUtils;

public class ServiceTest {
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	
	ServiceTypeControler ctrl = new ServiceTypeControler();
	
	@Test
    public void testGetProductByCate() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getAllService_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		try {
			String actual = ctrl.getAllService(con);
			JSONAssert.assertEquals(expected, actual, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
