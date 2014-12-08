package pl.edu.wszib.msmolen.mt.common.auth;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 3583951098001140007L;

	private final int id;
	private final String name;
	private final char[] password;
	private final UserType userType;

	public User(int pmId, String pmName, char[] pmPassword, UserType pmUserType)
	{
		id = pmId;
		name = pmName;
		password = pmPassword;
		userType = pmUserType;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public char[] getPassword()
	{
		return password;
	}

	public UserType getUserType()
	{
		return userType;
	}
}
