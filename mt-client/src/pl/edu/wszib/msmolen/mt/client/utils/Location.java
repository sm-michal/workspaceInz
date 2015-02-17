package pl.edu.wszib.msmolen.mt.client.utils;

public class Location
{
	public static final double LATTITUDE = 50.061781;
	public static final double LONGITUDE = 19.937242;

	/** Stale do konwersji stopni na kilometry, wyznaczone doswiadczalnie **/
	private static final double LAT_TO_KILOMETRES = 0.0089619;
	private static final double LON_TO_KILOMETRES = 0.0137931;

	private static final int ZOOM = 15;

	private static final double POWER_OF_2 = Math.pow(2, 7 + ZOOM);

	private static final int MAP_CENTER_X = 320;
	private static final int MAP_CENTER_Y = 320;

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
	public static double calculateLatitude(int pmY)
	{
		double lvStartY = lattitudeToY(LATTITUDE);
		double lvEndY = lvStartY + (pmY - MAP_CENTER_Y) * Math.pow(2, 28 - 7 - ZOOM);

		return Math.asin(Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) /
				(Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) + 1) -
				1 / (Math.pow(Math.E, (2 * A / B - 2 * lvEndY / B)) + 1)
				) / C;
	}

	/**
	 * Oblicza wspolrzedna X punktu na podstawie dlugosci geograficznej
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmLongitude
	 * @return wspolrzedna X
	 */
	public static int calculateX(double pmLongitude)
	{
		int lvX = longitudeToX(pmLongitude);
		int lvStartX = longitudeToX(LONGITUDE);
		return (int) (MAP_CENTER_X + (lvX - lvStartX) / Math.pow(2, 28 - 7 - ZOOM));
	}

	/**
	 * Oblicza wspolrzedna Y na podstawie szerokosci geograficznej
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmLattitude
	 * @return
	 */
	public static int calculateY(double pmLattitude)
	{
		int lvY = (int) lattitudeToY(pmLattitude);
		int lvStartY = (int) lattitudeToY(LATTITUDE);

		return (int) (MAP_CENTER_Y + (lvY - lvStartY) / Math.pow(2, 28 - 7 - ZOOM));
	}

	/**
	 * Oblicza wspolrzedna Y na podstawie szerokosci geograficznej.
	 * Zwraca bezwzgledny wynik w odniesieniu do poczatku ukladu, przy maksymalnym powiekszeniu
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmLattitude
	 * @return wpolrzedna Y
	 */
	private static double lattitudeToY(double pmLattitude)
	{
		return Math.round(GLOBE_OFFSET - GLOBE_RADIUS *
				Math.log((1 + Math.sin(pmLattitude * P)) /
						(1 - Math.sin(pmLattitude * P))) / 2);
	}

	/**
	 * Oblicza wspolrzedna Y na podstawie dlugosci geograficznej.
	 * Zwraca bezwzgledny wynik w odniesieniu do poczatku ukladu, przy maksymalnym powiekszeniu.
	 * 
	 * @see http://stackoverflow.com/questions/2651099/convert-long-lat-to-pixel-x-y-on-a-given-picture
	 * 
	 * @param pmLongitude
	 * @return wpolrzedna X
	 */
	private static int longitudeToX(double pmLongitude)
	{
		return (int) Math.round(GLOBE_OFFSET + GLOBE_RADIUS * pmLongitude * P);
	}

	/**
	 * Przelicza odleglosc w stopniach szerokosci geograficznej na metry
	 * 
	 * @param pmDLat
	 * @return
	 */
	public static int deltaLatitudeToMetres(double pmDLat)
	{
		return (int) (pmDLat / LAT_TO_KILOMETRES * 1000);
	}

	/**
	 * Przelicza odleglosc w stopniach dlugosci geograficznej na metry
	 * 
	 * @param pmDLon
	 * @return
	 */
	public static int deltaLongitudeToMetres(double pmDLon)
	{
		return (int) (pmDLon / LON_TO_KILOMETRES * 1000);
	}
}
