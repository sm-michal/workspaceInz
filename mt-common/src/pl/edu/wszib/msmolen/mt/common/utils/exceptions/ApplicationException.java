package pl.edu.wszib.msmolen.mt.common.utils.exceptions;

public class ApplicationException extends Exception
{
	static final long serialVersionUID = 8913287978899717863L;

	private final String mTitle;

	public ApplicationException(String pmTitle, String pmMessage)
	{
		super(pmMessage);
		mTitle = pmTitle;
	}

	public String getTitle()
	{
		return mTitle;
	}
}
