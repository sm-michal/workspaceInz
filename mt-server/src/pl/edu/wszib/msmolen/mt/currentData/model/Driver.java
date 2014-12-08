package pl.edu.wszib.msmolen.mt.currentData.model;

public class Driver
{
	private final int driverId;
	private double[] coordinates;
	private DriverStatus status;
	private Integer clientId;

	public Driver(int pmDriverId, double[] pmCoordinates)
	{
		driverId = pmDriverId;
		coordinates = pmCoordinates;
	}

	public int getDriverId()
	{
		return driverId;
	}

	public double[] getCoordinates()
	{
		return coordinates;
	}

	public void setCoordinates(double[] pmCoordinates)
	{
		coordinates = pmCoordinates;
	}

	public DriverStatus getStatus()
	{
		return status;
	}

	public void setStatus(DriverStatus pmStatus)
	{
		status = pmStatus;
	}

	public Integer getClientId()
	{
		return clientId;
	}

	public void setClientId(Integer pmClientId)
	{
		clientId = pmClientId;
	}
}
