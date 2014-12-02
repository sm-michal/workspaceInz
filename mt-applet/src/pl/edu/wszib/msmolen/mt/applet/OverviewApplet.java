package pl.edu.wszib.msmolen.mt.applet;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class OverviewApplet extends JApplet
{

	private static final long serialVersionUID = 132590882965871981L;

	@Override
	public void init()
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					createGUI();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void createGUI()
	{
		add(new JLabel("Hello world"));
	}
}
