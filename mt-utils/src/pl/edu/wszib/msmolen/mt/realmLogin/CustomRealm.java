package pl.edu.wszib.msmolen.mt.realmLogin;

import java.security.Principal;

import org.apache.catalina.Wrapper;
import org.apache.catalina.realm.RealmBase;

public class CustomRealm extends RealmBase
{
	private final UserLoginDAO userDao = new UserLoginDAO();

	@Override
	protected String getName()
	{
		return this.getClass().getName();
	}

	@Override
	protected String getPassword(String pmUserName)
	{
		return userDao.getUserPassword(pmUserName);
	}

	@Override
	protected Principal getPrincipal(String pmUserName)
	{
		return userDao.getUser(pmUserName);
	}

	@Override
	public Principal authenticate(String pmUserName, String pmCredentials)
	{
		boolean lvIsPasswordOk;

		String lvUserPassword = getPassword(pmUserName);
		if (lvUserPassword == null)
		{
			lvIsPasswordOk = false;
		}
		else if (hasMessageDigest())
		{
			lvIsPasswordOk = lvUserPassword.equalsIgnoreCase(digest(pmCredentials));
		}
		else
		{
			lvIsPasswordOk = lvUserPassword.equals(pmCredentials);
		}

		if (!lvIsPasswordOk)
		{
			return null;
		}
		else
		{
			return getPrincipal(pmUserName);
		}
	}

	@Override
	public boolean hasRole(Wrapper arg0, Principal arg1, String arg2)
	{
		// TODO Auto-generated method stub
		return true;
	}

}
