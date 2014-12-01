package pl.edu.wszib.msmolen.mt.taxiDrivers;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TaxiDriversServlet
 */
@WebServlet("/taxiDrivers")
public class TaxiDriversServlet extends HttpServlet
{

	private static final long serialVersionUID = -9030784133604154076L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			TaxiDriversBean lvBean = getBeanFromSession(request);

			lvBean.load();

			request.getSession().setAttribute(TaxiDriversBean.class.getSimpleName(), lvBean);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			request.setAttribute(Exception.class.getSimpleName(), Arrays.asList(e.getMessage()));
		}
		redirectToList(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		TaxiDriversBean lvBean = getBeanFromSession(request);
		TaxiDriver lvSelected;

		switch (TaxiDriverOperation.getByText(request.getParameter("action")))
		{
		case FIRE:
			lvSelected = lvBean.getById(Integer.parseInt(request.getParameter("driver_id")));

			break;
		case READ:
		case MODIFY:
			lvSelected = lvBean.getById(Integer.parseInt(request.getParameter("driver_id")));
			request.setAttribute(TaxiDriver.class.getSimpleName(), lvSelected);
		case EMPLOY:
			request.setAttribute("operation", request.getParameter("action"));
			redirectToForm(request, response);
			return;
		case REFRESH:
			doGet(request, response);
			return;
		default:
			break;
		}

		redirectToList(request, response);
	}

	/**
	 * Przekierowuje do listy taksowkarzy
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversList.jsp");

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
		lvDispatcher.forward(request, response);
	}

	/**
	 * Przekierowuje do formularza danych taksowkarza
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("includePage", "jsp/taxiDrivers/taxiDriversForm.jsp");

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
		lvDispatcher.forward(request, response);
	}

	private TaxiDriversBean getBeanFromSession(HttpServletRequest request)
	{
		TaxiDriversBean lvBean = (TaxiDriversBean) request.getSession().getAttribute(TaxiDriversBean.class.getSimpleName());
		if (lvBean == null)
			lvBean = new TaxiDriversBean();

		return lvBean;
	}
}
