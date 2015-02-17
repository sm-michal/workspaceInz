package pl.edu.wszib.msmolen.mt.common.utils;

import java.io.Serializable;

public class Pair<T> implements Serializable
{
	private static final long serialVersionUID = 498813763710554730L;

	private final T first;
	private final T second;

	public Pair(T pmFirst, T pmSecond)
	{
		first = pmFirst;
		second = pmSecond;
	}

	public T getFirst()
	{
		return first;
	}

	public T getSecond()
	{
		return second;
	}
}
