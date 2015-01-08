package pl.edu.wszib.msmolen.mt.client.utils;

public class Location
{
	public static final double LATTITUDE = 50.061781;
	public static final double LONGITUDE = 19.937242;

	private static final int ZOOM = 15;

	private static final double POWER_OF_2 = Math.pow(2, 7 + ZOOM);

	private static final int MAP_CENTER_X = 320;
	private static final int MAP_CENTER_Y = 240;

	private static final double GLOBE_OFFSET = Math.pow(2, 28);
	private static final double GLOBE_RADIUS = Math.pow(2, 28) / Math.PI;

	private static final double A = Math.pow(2, 28);
	private static final double B = A / Math.PI;
	private static final double C = 0.017453292519943;
	private static final double P = Math.PI / 180;

	/**
	 * Oblicza dlugosc geograficzna na podstawie punktu
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmX
	 * @return dlugosc geograficzna
	 */
	public static double calculateLongitude(int pmX)
	{
		return (pmX - MAP_CENTER_X) * 180 / POWER_OF_2 + LONGITUDE;
	}

	/**
	 * Oblicza szerokosc geograficzna na podstawie punktu
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmY
	 * @return szerokosc geograficzna
	 */
	public static double calculateLattitude(int pmY)
	{
		double lvStartY = lattitudeToY(LATTITUDE);
		double lvEndY = lvStartY + (pmY - MAP_CENTER_Y) * Math.pow(2, 28 - 7 - ZOOM);

		return Math.asin(Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) /
				(Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) + 1) -
				1 / (Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) + 1)
				) / C;
	}

	private static double lattitudeToY(double pmLattitude)
	{
		return Math.round(GLOBE_OFFSET - GLOBE_RADIUS *
				Math.log((1 + Math.sin(pmLattitude * P)) /
						(1 - Math.sin(pmLattitude * P))) / 2);
	}
}
