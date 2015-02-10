package pl.edu.wszib.msmolen.mt.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.wszib.msmolen.mt.core.model.Crossing;
import pl.edu.wszib.msmolen.mt.core.model.Street;
import pl.edu.wszib.msmolen.mt.db.DbUtils;

public class PathFinder
{
	private List<Crossing> crossings;

	private List<Street> streets;

	private final Map<String, List<Integer>> pathsMap = new HashMap<String, List<Integer>>();

	private Integer[][] pathsLengthsMatrix;

	private int matrixSize;

	private static PathFinder instance;

	private static PathFinder getInstance()
	{
		if (instance == null)
			instance = new PathFinder();

		return instance;
	}

	private PathFinder()
	{
		initializeData();
		calculatePaths();
	}

	private void initializeData()
	{
		Connection lvConnection = null;
		try
		{
			lvConnection = DbUtils.getConnection();
			crossings = loadCrossings(lvConnection);
			streets = loadStreets(lvConnection);

			matrixSize = crossings.size();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.close(lvConnection);
		}
	}

	private List<Crossing> loadCrossings(Connection pmConnection) throws Exception
	{
		List<Crossing> lvCrossings = new ArrayList<Crossing>();

		Statement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = pmConnection.createStatement();
			lvResult = lvStmt.executeQuery("SELECT ID, POLOZENIE_Y, POLOZENIE_X, SASIEDNIE_IDS FROM MT_DANE_SKRZYZOWANIA");
			while (lvResult.next())
				lvCrossings.add(new Crossing(lvResult.getInt(1), lvResult.getDouble(2), lvResult.getDouble(3), lvResult.getString(4)));
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}

		return lvCrossings;
	}

	private List<Street> loadStreets(Connection pmConnection) throws Exception
	{
		List<Street> lvStreets = new ArrayList<Street>();

		Statement lvStmt = null;
		ResultSet lvResult = null;
		try
		{
			lvStmt = pmConnection.createStatement();
			lvResult = lvStmt.executeQuery("SELECT SKRZYZOWANIE_START, SKRZYZOWANIE_STOP, NAZWA, DLUGOSC, JEDNOKIERUNKOWA FROM MT_DANE_ULICE");
			while (lvResult.next())
				lvStreets.add(new Street(lvResult.getInt(1), lvResult.getInt(2), lvResult.getString(3), lvResult.getDouble(4), lvResult.getBoolean(5)));
		}
		finally
		{
			DbUtils.close(lvResult, lvStmt);
		}

		return lvStreets;
	}

	private void calculatePaths()
	{
		Integer[][] matrixD = createEdgesMatrix();
		Integer[][] matrixS = createNeighborhoodMatrix();

		pathsLengthsMatrix = createFinalMatrix(matrixD, matrixS);

		for (int i = 0; i < matrixSize; i++)
			for (int j = 0; j < matrixSize; j++)
				if (i != j)
				{
					List<Integer> lvPath = new ArrayList<Integer>();
					lvPath.add(i);
					showPath(i, j, matrixS, lvPath);
					lvPath.add(j);
					pathsMap.put(i + "_" + j, lvPath);
				}
	}

	private Integer[][] createEdgesMatrix()
	{
		Integer[][] lvMatrix = new Integer[matrixSize][matrixSize];

		for (Crossing crossing : crossings)
		{
			Arrays.fill(lvMatrix[crossing.getId()], Integer.MAX_VALUE / 2);
			lvMatrix[crossing.getId()][crossing.getId()] = 0;

			for (Integer neighbor : crossing.getNeighbors())
			{
				for (Street street : streets)
				{
					if (street.getStartCrossingId() == crossing.getId() &&
							street.getEndCrossingId() == neighbor ||
							street.getEndCrossingId() == crossing.getId() &&
							street.getStartCrossingId() == neighbor && !street.isOneWay())
					{
						lvMatrix[crossing.getId()][neighbor] = (int) street.getLength() * 1000;
						break;
					}
				}
			}
		}

		return lvMatrix;
	}

	private Integer[][] createNeighborhoodMatrix()
	{
		Integer[][] lvMatrix = new Integer[matrixSize][matrixSize];
		for (Crossing crossing : crossings)
		{
			for (Integer neighbor : crossing.getNeighbors())
				lvMatrix[crossing.getId()][neighbor] = crossing.getId();
		}
		return lvMatrix;
	}

	private Integer[][] createFinalMatrix(Integer[][] pmStartMatrix, Integer[][] pmNeighborhoodMatrix)
	{
		Integer[][] lvStartMatrix = pmStartMatrix.clone();
		Integer[][] lvFinalMatrix = new Integer[matrixSize][matrixSize];

		for (int k = 0; k < matrixSize; k++)
		{
			for (int i = 0; i < matrixSize; i++)
				for (int j = 0; j < matrixSize; j++)
				{
					if (lvStartMatrix[i][j] > lvStartMatrix[i][k] + lvStartMatrix[k][j])
					{
						lvFinalMatrix[i][j] = lvStartMatrix[i][k] + lvStartMatrix[k][j];
						pmNeighborhoodMatrix[i][j] = k;
					}
					else
					{
						lvFinalMatrix[i][j] = lvStartMatrix[i][j];
					}
				}

			lvStartMatrix = lvFinalMatrix.clone();
		}

		return lvFinalMatrix;
	}

	private void showPath(Integer pmStart, Integer pmEnd, Integer[][] pmNeghborhoodMatrix, List<Integer> pmPath)
	{
		Integer k = pmNeghborhoodMatrix[pmStart][pmEnd];
		if (k != null)
		{
			if (!k.equals(pmEnd))
				showPath(pmStart, k, pmNeghborhoodMatrix, pmPath);

			if (pmPath.size() > 0 && !pmPath.get(pmPath.size() - 1).equals(k))
				pmPath.add(k);

			if (!k.equals(pmStart))
				showPath(k, pmEnd, pmNeghborhoodMatrix, pmPath);
		}
	}

	public static int getRideTime(Integer pmStart, Integer pmEnd)
	{
		return getInstance().pathsLengthsMatrix[pmStart][pmEnd];
	}

	public static List<Integer> getPath(Integer pmStart, Integer pmEnd)
	{
		return getInstance().pathsMap.get(pmStart + "_" + pmEnd);
	}
}
