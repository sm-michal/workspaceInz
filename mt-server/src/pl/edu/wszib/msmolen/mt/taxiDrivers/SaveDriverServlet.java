package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
				if (!validate(request, lvOperation) || !save(request, lvOperation))
				{
					refreshFormData(request);
					request.setAttribute(TaxiDriverOperation.class.getSimpleName(), lvOperation);
					request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversForm.jsp");
				}
				else
				{
					request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversList.jsp");
				}
			}
			break;
		default:
			request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversList.jsp");

		}

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
		lvDispatcher.forward(request, response);
	}

	/**
	 * 
	 * @param pmRequest
	 * @param pmOperation
	 * @return
	 */
	private boolean validate(HttpServletRequest pmRequest, TaxiDriverOperation pmOperation)
	{
		List<String> lvErrors = new ArrayList<String>();

		String lvName = pmRequest.getParameter("name");
		if (lvName == null || "".equals(lvName.trim()))
		{
			lvErrors.add("Imię nie może być puste.");
		}

		String lvSurname = pmRequest.getParameter("surname");
		if (lvSurname == null || "".equals(lvSurname.trim()))
		{
			lvErrors.add("Nazwisko nie może być puste.");
		}

		String lvLogin = pmRequest.getParameter("login");
		if (lvLogin == null || "".equals(lvLogin.trim()))
		{
			lvErrors.add("Login nie może być pusty.");
		}

		String lvPassword = pmRequest.getParameter("password");
		String lvRPassword = pmRequest.getParameter("repeatPassword");
		if (!lvPassword.equals(lvRPassword))
		{
			lvErrors.add("Hasło i powtórzenie hasła muszą być zgodne.");
		}
		else if (pmOperation == TaxiDriverOperation.EMPLOY && "".equals(lvPassword.trim()))
		{
			lvErrors.add("Hasło nie może być puste.");
		}

		if (!lvErrors.isEmpty())
		{
			pmRequest.setAttribute(Exception.class.getSimpleName(), lvErrors);
			return false;
		}
		else
			return true;
	}

	/**
	 * 
	 * @param pmRequest
	 * @param pmOperation
	 * @return
	 */
	private boolean save(HttpServletRequest pmRequest, TaxiDriverOperation pmOperation)
	{
		try
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

			lvBean.load();

			return true;
		}
		catch (Exception e)
		{
			pmRequest.setAttribute(Exception.class.getSimpleName(), Arrays.asList(e.getMessage()));
			return false;
		}
	}

	private void refreshFormData(HttpServletRequest pmRequest)
	{
		String lvIdStr = pmRequest.getParameter("driver_id");
		String lvName = pmRequest.getParameter("name");
		String lvSurname = pmRequest.getParameter("surname");
		String lvUserIdStr = pmRequest.getParameter("user_id");
		String lvLogin = pmRequest.getParameter("login");
		int lvId = -1;
		int lvUserId = -1;
		if (lvIdStr != null && !"".equals(lvIdStr))
			lvId = Integer.parseInt(lvIdStr);
		if (lvUserIdStr != null && !"".equals(lvUserIdStr))
			lvUserId = Integer.parseInt(lvUserIdStr);

		TaxiDriver lvDriver = new TaxiDriver(lvId, lvName, lvSurname, new User(lvUserId, lvLogin, new char[0]));
		pmRequest.setAttribute(TaxiDriver.class.getSimpleName(), lvDriver);
	}

}
