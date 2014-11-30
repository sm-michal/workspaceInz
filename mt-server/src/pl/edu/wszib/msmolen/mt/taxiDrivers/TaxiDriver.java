package pl.edu.wszib.msmolen.mt.taxiDrivers;

import pl.edu.wszib.msmolen.mt.common.auth.User;

public class TaxiDriver
{
	private int id;
	private String name;
	private String surname;
	private User user;

	public TaxiDriver(int pmId, String pmName, String pmSurname, User pmUser)
	{
		id = pmId;
		name = pmName;
		surname = pmSurname;
		user = pmUser;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int pmId)
	{
		this.id = pmId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String pmName)
	{
		this.name = pmName;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String pmSurname)
	{
		this.surname = pmSurname;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User pmUser)
	{
		this.user = pmUser;
	}

}
