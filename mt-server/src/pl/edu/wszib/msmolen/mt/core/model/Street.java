package pl.edu.wszib.msmolen.mt.core.model;

public class Street
{
	private final int startCrossingId;
	private final int endCrossingId;
	private final String name;
	private final double length;
	private final boolean oneWay;

	public Street(int pmStartId, int pmEndId, String pmName, Double pmLength, boolean pmOneWay)
	{
		startCrossingId = pmStartId;
		endCrossingId = pmEndId;
		name = pmName;
		length = pmLength;
		oneWay = pmOneWay;
	}

	public int getStartCrossingId()
	{
		return startCrossingId;
	}

	public int getEndCrossingId()
	{
		return endCrossingId;
	}

	public String getName()
	{
		return name;
	}

	public double getLength()
	{
		return length;
	}

	public boolean isOneWay()
	{
		return oneWay;
	}
}
