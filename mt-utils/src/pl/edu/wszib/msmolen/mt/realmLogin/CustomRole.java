package pl.edu.wszib.msmolen.mt.realmLogin;

import org.apache.catalina.Role;
import org.apache.catalina.UserDatabase;

public class CustomRole implements Role
{
	private final int mId;
	private final String mName;
	private final String mDescription;

	public CustomRole(int pmId, String pmName, String pmDescription)
	{
		mId = pmId;
		mName = pmName;
		mDescription = pmDescription;
	}

	public int getId()
	{
		return mId;
	}

	@Override
	public String getName()
	{
		return mName;
	}

	@Override
	public String getDescription()
	{
		return mDescription;
	}

	@Override
	public String getRolename()
	{
		return mName;
	}

	@Override
	public UserDatabase getUserDatabase()
	{
		return null;
	}

	@Override
	public void setDescription(String arg0)
	{
		throw new IllegalAccessError();
	}

	@Override
	public void setRolename(String arg0)
	{
		throw new IllegalAccessError();
	}

}
