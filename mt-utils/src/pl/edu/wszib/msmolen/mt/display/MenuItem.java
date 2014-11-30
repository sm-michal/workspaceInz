package pl.edu.wszib.msmolen.mt.display;

public class MenuItem
{
	private final int id;
	private final String text;
	private final String url;

	public MenuItem(int pmId, String pmText, String pmUrl)
	{
		id = pmId;
		text = pmText;
		url = pmUrl;
	}

	public int getId()
	{
		return id;
	}

	public String getText()
	{
		return text;
	}

	public String getUrl()
	{
		return url;
	}
}
