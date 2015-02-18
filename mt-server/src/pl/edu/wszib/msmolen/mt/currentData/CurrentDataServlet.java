package pl.edu.wszib.msmolen.mt.currentData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import pl.edu.wszib.msmolen.mt.currentData.model.Client;
import pl.edu.wszib.msmolen.mt.currentData.model.Driver;
import pl.edu.wszib.msmolen.mt.currentData.model.JsonResponse;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

/**
 * Servlet obslugujacy strone z podgladem aktualnej sytuacji
 */
@WebServlet("/currentData")
public class CurrentDataServlet extends HttpServlet
{
	private static final long serialVersionUID = -3013015017373787872L;

	/**
	 * Przekierowuje do strony z mapa
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("includePage", "jsp/overview/overview.jsp");

		RequestDispatcher lvDispatcher = getServletContext().getRequestDispatcher("/");
		lvDispatcher.forward(request, response);
	}

	/**
	 * Odbiera zadanie ajax z js odpowiedzialnego za aktualizacje mapy i wysyla
	 * odpowiedz w formacie JSON
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().println(new Gson().toJson(getData()));
	}

	private JsonResponse getData()
	{
		List<Driver> lvDrivers = new ArrayList<Driver>();
		List<Client> lvClients = new ArrayList<Client>();

		Connection lvConnection = null;
		try
		{
			lvConnection = DbUtils.getConnection();

			lvDrivers = getDrivers(lvConnection);
			lvClients = getClients(lvConnection);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new JsonResponse("ERROR", new ArrayList<Driver>(), new ArrayList<Client>());
		}
		finally
		{
			DbUtils.close(lvConnection);
		}
		return new JsonResponse("OK", lvDrivers, lvClients);
	}

	private List<Driver> getDrivers(Connection pmConnection) throws Exception
	{
		List<Driver> lvDrivers = new ArrayList<Driver>();
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = pmConnection.prepareStatement("SELECT ID, POLOZENIE_Y, POLOZENIE_X FROM MT_TAKSOWKARZE WHERE CZY_AKTYWNY = ?");
			lvStmt.setBoolean(1, true);
			lvResult = lvStmt.executeQuery();
			while (lvResult.next())
			{
				lvDrivers.add(new Driver(lvResult.getInt(1), new double[] { lvResult.getDouble(2), lvResult.getDouble(3) }));
			}

		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}
		return lvDrivers;
	}

	private List<Client> getClients(Connection pmConnection) throws Exception
	{
		List<Client> lvClients = new ArrayList<Client>();
		PreparedStatement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = pmConnection.prepareStatement("SELECT ID, START_Y, START_X FROM MT_ZAMOWIENIA WHERE STATUS IN ('1','2')");
			lvResult = lvStmt.executeQuery();
			while (lvResult.next())
			{
				lvClients.add(new Client(lvResult.getInt(1), new double[] { lvResult.getDouble(2), lvResult.getDouble(3) }));
			}
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}
		return lvClients;
	}

}
