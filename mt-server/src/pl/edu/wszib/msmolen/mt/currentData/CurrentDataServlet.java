package pl.edu.wszib.msmolen.mt.currentData;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CurrentDataServlet
 */
@WebServlet("/currentData")
public class CurrentDataServlet extends HttpServlet
{
	private static final long serialVersionUID = -3013015017373787872L;

	/**
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("includePage", "jsp/overview/overview.jsp");

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
		lvDispatcher.forward(request, response);
	}

	/**
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

}
