package com.shopnail.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shopnail.api.connection.ConnectionDB;
import com.shopnail.api.controler.UserControler;
/**
 * Servlet implementation class GetByUserIdServlet
 */
@WebServlet(name = "GetByUserIdServlet", urlPatterns = {"/getUserById/*"})
public class GetByUserIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
    /**
     * Default constructor. 
     */
    public GetByUserIdServlet() {
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String reponseRestult = "";
		if(UserControler.checkAuthorization(con, request) == false)
			return;
		try {
			con = conDB.connect();
			UserControler ctrl = new  UserControler();
			int userId = retrieveUserid(request);
			reponseRestult = ctrl.reponseGetUserById(con, userId);
			
		} catch (SQLException e) {
			e.printStackTrace();
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
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static int retrieveUserid(HttpServletRequest req) {
	      String pathInfo = req.getPathInfo();
	      if (pathInfo.startsWith("/")) {
	          pathInfo = pathInfo.substring(1);
	      }
	      return Integer.parseInt(pathInfo);
	 }
}
