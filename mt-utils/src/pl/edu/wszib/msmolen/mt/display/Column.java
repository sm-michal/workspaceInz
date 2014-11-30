package pl.edu.wszib.msmolen.mt.display;

public class Column
{
	private final int ordinal;
	private final String displayName;
	private final String dbFieldName;
	private final boolean sortAvailable;
	private boolean sortEnabled;

	public Column(int pmOrdinal, String pmName, String pmDbFieldname, boolean pmSortAvailable, boolean pmSortEnabled)
	{
		ordinal = pmOrdinal;
		displayName = pmName;
		dbFieldName = pmDbFieldname;
		sortAvailable = pmSortAvailable;
		sortEnabled = pmSortEnabled;
	}

	public int getOrdinal()
	{
		return ordinal;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getDbFieldName()
	{
		return dbFieldName;
	}

	public boolean isSortAvailable()
	{
		return sortAvailable;
	}

	public boolean isSortEnabled()
	{
		return sortEnabled;
	}

	public void setSortEnabled(boolean sortEnabled)
	{
		this.sortEnabled = sortEnabled;
	}
}
