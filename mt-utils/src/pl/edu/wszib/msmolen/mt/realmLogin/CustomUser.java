package pl.edu.wszib.msmolen.mt.realmLogin;

import java.util.List;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.auth.UserType;
import pl.edu.wszib.msmolen.mt.display.MenuItem;

public class CustomUser implements CustomPrincipal
{
	private final String mName;
	private final User mUserObject;
	private final List<MenuItem> mMenuItems;
	private final List<CustomRole> mRoles;

	protected CustomUser(int pmId, String pmName, String pmPassword, List<MenuItem> pmMenuItems, List<CustomRole> pmRoles, UserType pmUserType)
	{
		mName = pmName;
		mUserObject = new User(pmId, pmName, pmPassword.toCharArray(), pmUserType, null);
		mMenuItems = pmMenuItems;
		mRoles = pmRoles;
	}

	@Override
	public String getName()
	{
		return mName;
	}

	@Override
	public User getSimpleUserObject()
	{
		return mUserObject;
	}

	@Override
	public List<MenuItem> getAvailableMenuItems()
	{
		return mMenuItems;
	}

	@Override
	public List<CustomRole> getRoles()
	{
		return mRoles;
	}

	@Override
	public boolean hasRole(String pmRole)
	{
		return true;
	}

}
