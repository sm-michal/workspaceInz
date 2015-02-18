package pl.edu.wszib.msmolen.mt.currentData.model;

public class Client
{
	private final int id;
	private final double[] coordinates;

	public Client(int pmId, double[] pmCoordinates)
	{
		id = pmId;
		coordinates = pmCoordinates;
	}

	public int getId()
	{
		return id;
	}

	public double[] getCoordinates()
	{
		return coordinates;
	}
}
