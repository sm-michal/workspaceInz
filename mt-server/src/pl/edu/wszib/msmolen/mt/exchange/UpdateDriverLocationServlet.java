package pl.edu.wszib.msmolen.mt.exchange;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateDriverLocationServlet
 */
@WebServlet("/exchange/updateDriver")
public class UpdateDriverLocationServlet extends BasicExchangeServlet implements Servlet
{

	private static final long serialVersionUID = -4791181774164560920L;

	@Override
	protected void processRequest(HttpServletResponse pmResponse, Object[] pmObjects) throws Exception
	{
		// TODO Auto-generated method stub

	}

}
