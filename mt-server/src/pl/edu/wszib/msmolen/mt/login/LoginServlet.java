package pl.edu.wszib.msmolen.mt.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
       
	private static final long serialVersionUID = -344257502622424259L;

	private static final String LOGIN_PARAM = "user";
	private static final String PASSWORD_PARAM = "password";
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String user = request.getParameter(LOGIN_PARAM);
		String password = request.getParameter(PASSWORD_PARAM);
			
		if (request.getUserPrincipal() == null && user != null && password != null)
		{
//			request.login(user, "admin");
//			
//			response.sendRedirect("j_security_check?j_username="+user+"&j_password=admin");
//			getServletContext().getRequestDispatcher("/mt-server/j_security_check?j_username="+user+"&j_password=admin").forward(request, response);
//			return;
		}
		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
