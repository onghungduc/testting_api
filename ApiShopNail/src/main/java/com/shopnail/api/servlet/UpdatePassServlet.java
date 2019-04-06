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
 * Servlet implementation class UpdatePassServlet
 */
@WebServlet("/updatePass")
public class UpdatePassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ConnectionDB conDB  = new ConnectionDB();
	Connection con = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdatePassServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String reponseRestult = "";
		try {
			con = conDB.connect();
			UserControler ctrl = new UserControler();
			String id = request.getParameter("id");
			if(UserControler.checkAuthorization(con, request) == false)
				return;
			String password = request.getParameter("password");
			String password_new = request.getParameter("password_new");
			String password_confirmation = request.getParameter("password_confirmation");
			System.out.println("id" + id);
			System.out.println("password_new: " + password_new);
			System.out.println("password_confirmation: " + password_confirmation);
			reponseRestult = ctrl.reponseUpdatePassWord(con, password, password_new , password_confirmation, id);
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
