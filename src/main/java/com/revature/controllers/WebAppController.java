package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Account;

import rev.orm.services.ORMServices;


public class WebAppController extends HttpServlet {
	
	private ORMServices or = new ORMServices();
	private Account ac = new Account();
	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String URI = request.getRequestURI();
		
		String[] urlSections = URI.split("/");
		
		if (urlSections.length == 3) {
			List<Object> listAccount = or.retriveAll(ac);
			String json = objectMapper.writeValueAsString(listAccount);

			PrintWriter printWriter = response.getWriter();

			printWriter.print(json);

			response.setStatus(200);

			response.setContentType("application/json");
			
		}else if(urlSections.length==4) {
			try {
				String userName = String.valueOf(urlSections[3]);
				
				Object accountUsername = or.retriveRowContentByColumn(userName, 3, ac);
				
				PrintWriter printWriter = response.getWriter();
				
				String json = objectMapper.writeValueAsString(accountUsername);
				
				printWriter.print(json);
				response.setStatus(200);
				response.setContentType("application/json");
		
			}catch(NumberFormatException e) {
				response.setStatus(404);
				return;
			}
		}else {
			response.setStatus(404);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = request.getReader();

		String line = reader.readLine();
		if(line != null) {
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}

			String json = new String(sb);
			
			Account addAccount = objectMapper.readValue(json, Account.class);
			
			or.insertNewObject(addAccount);
			
			response.setStatus(201);			
		}else {
			response.setStatus(404);
		}

	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = request.getReader();

		String line = reader.readLine();
		
		String URI = request.getRequestURI();
		String[] urlSections = URI.split("/");
		if(urlSections.length==4) {

			String userName = String.valueOf(urlSections[3]);
			Account tempAccountUsername = (Account) or.retriveRowContentByColumn(userName, 3, ac);
			if(tempAccountUsername != null) {
				
				while (line != null) {
					sb.append(line);
					line = reader.readLine();
				}

				String json = new String(sb);
				Account updatedAccount = objectMapper.readValue(json, Account.class);
				
				//Compares the "User" that is being updated to the JSON marshaling object "UpdatedAccount" fields 
				//Allows for multiple fields to be updated at the same time
				for(int i = 1; i < 5;) {
					if(tempAccountUsername.getAccountName() != updatedAccount.getAccountName()) {
						or.updateRowContentByColumn(updatedAccount.getAccountName(), i, tempAccountUsername.getAccountName(), i, ac);
					}
					i++;
					if(tempAccountUsername.getAccountLastName() != updatedAccount.getAccountLastName()) {
						or.updateRowContentByColumn(updatedAccount.getAccountLastName(), i, tempAccountUsername.getAccountLastName(), i, ac);
					}
					i++;
					if(tempAccountUsername.getUserName() != updatedAccount.getUserName()) {
						or.updateRowContentByColumn(updatedAccount.getUserName(), i, tempAccountUsername.getUserName(), i, ac);
					}
					i++;
					if(tempAccountUsername.getUserEmail() != updatedAccount.getUserEmail()) {
						or.updateRowContentByColumn(updatedAccount.getUserEmail(), i, tempAccountUsername.getUserEmail(), i, ac);
					}
					i++;
					if(tempAccountUsername.getZipcode() != updatedAccount.getZipcode()) {
						or.updateRowContentByColumn(String.valueOf(updatedAccount.getZipcode()), i, String.valueOf(tempAccountUsername.getZipcode()), i, ac);
					}
				}
				response.setStatus(200);
			}else {
				response.setStatus(404);
			}
		}else {
			response.setStatus(404);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String URI = request.getRequestURI();
		String[] urlSections = URI.split("/");
		if(urlSections.length==4) {
			String userName = String.valueOf(urlSections[3]);
			Account deleteAccountByUsername = (Account) or.retriveRowContentByColumn(userName, 3, ac);	
			or.deleteRowContentByColumn(userName, 3, deleteAccountByUsername);
			response.setStatus(200);
		}else {
			response.setStatus(404);
		}
		
	}
	
}
