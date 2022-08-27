package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.models.Account;

import rev.orm.services.ORMServices;


public class WebAppController extends HttpServlet {
	
	ORMServices or = new ORMServices();
	Account ac = new Account();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String URI = request.getRequestURI();
		System.out.println(URI);
		
		PrintWriter printWriter = response.getWriter();
		
		printWriter.print("<h1>Hello World!<h1>");
		response.setStatus(218);
		response.setHeader("content-type", "text/html");
		
		
		
		System.out.println(or.retriveRowContentByColumn("Alice", 1, ac));
	}
	
}
