package pl.edu.wszib.msmolen.mt.common.auth;

import java.io.Serializable;

public class Token implements Serializable
{

	private static final long serialVersionUID = -2226640922682070036L;

	private final String value;

	public Token(String pmValue)
	{
		value = pmValue;
	}

	public String getTokenValue()
	{
		return value;
	}
}
