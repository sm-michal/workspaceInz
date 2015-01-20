package pl.edu.wszib.msmolen.mt.client.utils;

public class ClientUtils
{
	public static String getTimeMessage(int pmTime)
	{
		if (pmTime == 1)
			return "1 minuta";
		else if (pmTime > 5 && pmTime < 21 || pmTime % 10 >= 5 || pmTime % 10 == 0)
			return pmTime + " minut";
		else if (pmTime % 10 > 1 && pmTime % 10 < 5)
			return pmTime + " minuty";

		return "";
	}
}
