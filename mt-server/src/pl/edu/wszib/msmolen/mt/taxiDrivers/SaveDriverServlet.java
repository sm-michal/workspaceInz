package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SaveDriverServlet
 */
@WebServlet("/saveDriver")
public class SaveDriverServlet extends HttpServlet
{
	private static final long serialVersionUID = 6738775092671924035L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		TaxiDriverOperation lvOperation = TaxiDriverOperation.getByText(request.getParameter("operation"));

		switch (TaxiDriverOperation.getByText(request.getParameter("action")))
		{
		case SAVE:
			validate(request, lvOperation);
			break;
		default:

		}

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/taxiDrivers");
		lvDispatcher.forward(request, response);
	}

	private void validate(HttpServletRequest pmRequest, TaxiDriverOperation pmOperation)
	{
		if (pmOperation == TaxiDriverOperation.READ)
			return;

		String lvPassword = pmRequest.getParameter("password");
		String lvRPassword = pmRequest.getParameter("repeatPassword");
		if (!lvPassword.equals(lvRPassword))
		{

		}

	}

}
