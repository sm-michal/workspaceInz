package pl.edu.wszib.msmolen.mt.client;

import javax.swing.SwingUtilities;

import pl.edu.wszib.msmolen.mt.client.gui.StartWindow;
import pl.edu.wszib.msmolen.mt.client.utils.ClientUtils;
import pl.edu.wszib.msmolen.mt.client.utils.SSLUtils;
import pl.edu.wszib.msmolen.mt.client.utils.TokenManager;

public class Main
{

	public static void main(String[] args)
	{
		SSLUtils.zezwalajNaPolaczeniaSsl();

		try
		{
			ClientUtils.initializeWorkDirs();

			TokenManager.getInstance().obtainToken();

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					StartWindow.startGUI();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
