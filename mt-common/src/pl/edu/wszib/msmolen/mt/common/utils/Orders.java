package pl.edu.wszib.msmolen.mt.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author msmolen
 * 
 */
public class Orders implements Serializable
{

	private static final long serialVersionUID = -6011197636286019896L;

	private final int id;
	private final String startPlace;
	private final double startLatitude;
	private final double startLongitude;
	private final List<Pair<Double>> path;

	public Orders(int pmId, String pmStartPlace, double pmStartLat, double pmStartLong, List<Pair<Double>> pmPath)
	{
		id = pmId;
		startPlace = pmStartPlace;
		startLatitude = pmStartLat;
		startLongitude = pmStartLong;
		path = pmPath;
	}

	public int getId()
	{
		return id;
	}

	public String getStartPlace()
	{
		return startPlace;
	}

	public double getStartLatitude()
	{
		return startLatitude;
	}

	public double getStartLongitude()
	{
		return startLongitude;
	}

	public List<Pair<Double>> getPath()
	{
		return path;
	}

}
