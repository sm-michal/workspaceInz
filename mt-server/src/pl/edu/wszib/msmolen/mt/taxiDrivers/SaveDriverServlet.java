package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.edu.wszib.msmolen.mt.common.auth.User;

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
			if (lvOperation != TaxiDriverOperation.READ)
			{
				validate(request, lvOperation);
				if (!save(request, lvOperation))
				{
					request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversForm.jsp");
				}
			}
			break;
		default:
			request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversList.jsp");

		}

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
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

		String lvName = pmRequest.getParameter("name");
		if (lvName == null || "".equals(lvName.trim()))
		{

		}

		String lvSurname = pmRequest.getParameter("surname");
		if (lvSurname == null || "".equals(lvSurname.trim()))
		{

		}

		String lvLogin = pmRequest.getParameter("login");
		if (lvLogin == null || "".equals(lvLogin.trim()))
		{

		}
	}

	private boolean save(HttpServletRequest pmRequest, TaxiDriverOperation pmOperation)
	{
		TaxiDriversBean lvBean = (TaxiDriversBean) pmRequest.getSession().getAttribute(TaxiDriversBean.class.getSimpleName());

		String lvIdStr = pmRequest.getParameter("driver_id");
		String lvName = pmRequest.getParameter("name");
		String lvSurname = pmRequest.getParameter("surname");
		String lvUserIdStr = pmRequest.getParameter("user_id");
		String lvLogin = pmRequest.getParameter("login");
		String lvPassword = pmRequest.getParameter("password");
		int lvId = -1;
		int lvUserId = -1;
		if (lvIdStr != null && !"".equals(lvIdStr))
			lvId = Integer.parseInt(lvIdStr);
		if (lvUserIdStr != null && !"".equals(lvUserIdStr))
			lvUserId = Integer.parseInt(lvUserIdStr);

		TaxiDriver lvDriver = new TaxiDriver(lvId, lvName, lvSurname, new User(lvUserId, lvLogin, lvPassword.toCharArray()));

		if (pmOperation == TaxiDriverOperation.EMPLOY)
			lvBean.create(lvDriver);
		else if (pmOperation == TaxiDriverOperation.MODIFY)
			lvBean.update(lvDriver);

		return true;
	}

}
