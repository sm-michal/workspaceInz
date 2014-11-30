package pl.edu.wszib.msmolen.mt.taxiDrivers;

/**
 * Enum z operacjami dotyczacymi taksowkarzy
 * 
 * @author msmolen
 * 
 */
public enum TaxiDriverOperation
{
	EMPLOY("Zatrudnij taksówkarza"),
	REFRESH("Odśwież"),
	READ("Przeglądaj"),
	MODIFY("Modyfikuj"),
	FIRE("Zwolnij taksówkarza"),
	SAVE("Zapisz"),
	CANCEL("Anuluj"),
	NO_VALUE("");

	private String text;

	private TaxiDriverOperation(String pmText)
	{
		text = pmText;
	}

	public String getText()
	{
		return text;
	}

	public static TaxiDriverOperation getByText(String pmText)
	{
		for (TaxiDriverOperation operation : values())
			if (operation.text.equals(pmText))
				return operation;

		return NO_VALUE;
	}
}
