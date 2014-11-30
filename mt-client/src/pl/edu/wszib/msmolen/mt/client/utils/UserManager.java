package pl.edu.wszib.msmolen.mt.client.utils;

import pl.edu.wszib.msmolen.mt.common.auth.User;

public class UserManager
{
	private static UserManager instance;

	private User mUser;

	public static UserManager getInstance()
	{
		if (instance == null)
			instance = new UserManager();

		return instance;
	}

	public User getUser()
	{
		return mUser;
	}

	public void setUser(User mUser)
	{
		this.mUser = mUser;
	}
}
