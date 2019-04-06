package com.shopnail.api.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.NavigateStaffControler;
import com.shopnail.api.controler.OrderControler;
import com.shopnail.api.controler.UserControler;

/**
 * Servlet implementation class bookingServiceOnline
 */
@WebServlet("/addOrUpdateServiceChecking")
public class AddOrUpdateServiceChecking extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddOrUpdateServiceChecking() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(UserControler.checkAuthorization(con, request) == false)
			return;
		// TODO Auto-generated method stub
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */ }
		try {
			
			String reponseRestult = "";
			con = conDB.connect();
			
			NavigateStaffControler ctrl = new NavigateStaffControler();
			System.out.println("Line: " + jb.toString());
			try {
				reponseRestult = ctrl.addOrUpdateServiceChecking(con, jb.toString());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			StringBuffer result = new StringBuffer();
			result.append(reponseRestult);
			response.setHeader("Content-Type", "application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer;
			try {
				writer = response.getWriter();
				writer.print(result.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.getWriter().append("Served at: ").append(response.toString());
		} catch (JSONException e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
	}

}
