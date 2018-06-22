package com.afkghouri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class Login
 */

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HashMap<String, Connection> login_connections = new HashMap<>(); 
	
	ThreadLocal<Integer> count = new ThreadLocal<>();

	/**
	 * Default constructor. 
	 */
	public LoginServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(count.get() == null)
			count.set(0);
		count.set(count.get()+1);
		System.out.println("thread local count: "+count.get());
		
		String hidden_email = request.getParameter("hidden_email"); 
		
		if(hidden_email != null)
			try {
				Connection conn = login_connections.remove(hidden_email); 
				//System.out.println(login_connections.size());
				if(conn !=null)
					conn.close();  // connection released
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		request.getRequestDispatcher("views/loginform.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// get login post form parameters
		String email = request.getParameter("email"), password = request.getParameter("password");
		// set login post form parameters for other JSP 
		request.setAttribute("email", email); 
		request.setAttribute("password", password);

		//int  value = compareFormFieldsValuesWithHardcodeValues(email, password);
		int  value = compareFormFieldsValuesWithDataBase(email, password);

		switch(value){
		case 1:
			goToJSP(request, response, "views/success.jsp");
			break;
		case 0:
			goToJSP(request, response, "views/failure.jsp");
			break;
		case 500:
			response.getWriter().println("connection pool overflowed...");
			break;
		}

	}

	protected int compareFormFieldsValuesWithHardcodeValues(String email,String password){
		if(email.equals("afkghouri@gmail.com") && password.equals("1"))
			return 1;
		return 0;
	}

	protected int compareFormFieldsValuesWithDataBase(String email,String password){

		try {
			Connection con = getConnectionFromPool(); 

			Statement stmt = null;  // statement/query execute
			ResultSet rs = null;    // output show

			stmt =  con.createStatement();
			stmt.execute("use logindata");
			stmt =  con.createStatement();
			rs=stmt.executeQuery("select * from login where email='"+email+"'"+" and password='"+password+"'");


			if(rs.next()){
				login_connections.put(email, con); // connection preserved
				return 1; 
			}else
				con.close(); // connection released if login failed
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch(NullPointerException ex){ 
			return 500;
		} 
		return 0;
	}

	protected Connection getConnectionFromPool() throws ClassNotFoundException, SQLException{
		return DBConnection.getInstance().getConnectionFromPool(); 
	}

	protected void goToJSP(HttpServletRequest request, HttpServletResponse response,String path) throws ServletException, IOException{
		request.getRequestDispatcher(path).forward(request, response);
	}




}
