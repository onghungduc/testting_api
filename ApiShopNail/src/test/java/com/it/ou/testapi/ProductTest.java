package com.it.ou.testapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.ProductControler;
import com.shopnail.api.utils.FileUtils;

public class ProductTest {
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	
	ProductControler ctrl = new ProductControler();
	
	@Test
    public void testGetProductByCate() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getProductByCate_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		int parentId = 11;
		try {
			String actual = ctrl.getProductByCate(con, parentId);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testGetAllCate() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getAllCategory_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		try {
			String actual = ctrl.getAllCategory(con);
			JSONAssert.assertEquals(expected, actual, true);
			//JSONAssert
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
