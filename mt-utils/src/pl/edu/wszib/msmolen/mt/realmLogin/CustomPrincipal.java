package pl.edu.wszib.msmolen.mt.realmLogin;

import java.security.Principal;
import java.util.List;

import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.display.MenuItem;

public interface CustomPrincipal extends Principal
{
	User getSimpleUserObject();

	List<MenuItem> getAvailableMenuItems();

	List<CustomRole> getRoles();

	boolean hasRole(String pmRole);

}
