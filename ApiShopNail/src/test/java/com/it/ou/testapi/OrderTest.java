package com.it.ou.testapi;

import java.io.File;
import java.sql.Connection;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.OrderControler;
import com.shopnail.api.utils.FileUtils;

public class OrderTest {
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	
	OrderControler ctrl = new OrderControler();
	
	@Test
    public void testGetCustomerInfo() {
		con = conDB.connect();
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getCustomerInfo_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		
		File file1 = new File(classLoader.getResource("request/getCustomerInfo.txt").getFile());
		String jb = FileUtils.readSqlFile(file1.toString());
		String actual = ctrl.getCustomerInfo(con, jb);
		
		JSONAssert.assertEquals(expected, actual, true);
	}
	
	@Test
    public void testGetCustomerTimeOrder() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getCustomerTimeOrder_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		File file1 = new File(classLoader.getResource("request/getCustomerTimeOrder.txt").getFile());
		String jb = FileUtils.readSqlFile(file1.toString());
		String actual = ctrl.getCustomerTimeOrder(con, jb);
		JSONAssert.assertEquals(expected, actual, true);
	}
	
	@Test
    public void testGetMyCustomer() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/getMyCustomer_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		File file1 = new File(classLoader.getResource("request/getMyCustomer.txt").getFile());
		String jb = FileUtils.readSqlFile(file1.toString());
		String actual = ctrl.getMyCustomer(con, jb);
		JSONAssert.assertEquals(expected, actual, true);
	}
	
	@Test
    public void testHistoryClientByStaff_True() {
		con = conDB.connect();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("result/historyClientByStaff_True.txt").getFile());
		String expected = FileUtils.readSqlFile(file.toString());
		File file1 = new File(classLoader.getResource("request/historyClientByStaff.txt").getFile());
		String jb = FileUtils.readSqlFile(file1.toString());
		String actual = ctrl.historyClientByStaff(con, jb);
		JSONAssert.assertEquals(expected, actual, true);
	}
}
