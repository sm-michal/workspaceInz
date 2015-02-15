package pl.edu.wszib.msmolen.mt.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Crossing
{
	private final Pattern splitPattern = Pattern.compile(",");

	private final int id;
	private final double lattitude;
	private final double longitude;
	private final Set<Integer> neighbors;

	public Crossing(int pmId, double pmLattitude, double pmLongitude, String pmNeighbors)
	{
		id = pmId;
		lattitude = pmLattitude;
		longitude = pmLongitude;
		neighbors = createSetFromString(pmNeighbors);
	}

	public int getId()
	{
		return id;
	}

	public double getLattitude()
	{
		return lattitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public Set<Integer> getNeighbors()
	{
		return neighbors;
	}

	private Set<Integer> createSetFromString(String pmString)
	{
		Set<Integer> lvResult = new HashSet<Integer>();

		String[] lvArray = splitPattern.split(pmString);
		for (String element : lvArray)
			lvResult.add(Integer.parseInt(element));

		return lvResult;
	}
}
