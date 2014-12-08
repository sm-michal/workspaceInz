package pl.edu.wszib.msmolen.mt.currentData.model;

import java.util.List;

public class JsonResponse
{
	private final String status;
	private final List<Driver> drivers;
	private final List<Client> clients;

	public JsonResponse(String pmStatus, List<Driver> pmDrivers, List<Client> pmClients)
	{
		status = pmStatus;
		drivers = pmDrivers;
		clients = pmClients;
	}

	public String getStatus()
	{
		return status;
	}

	public List<Driver> getDrivers()
	{
		return drivers;
	}

	public List<Client> getClients()
	{
		return clients;
	}
}
