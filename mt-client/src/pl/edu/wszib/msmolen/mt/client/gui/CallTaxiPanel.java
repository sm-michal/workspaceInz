package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.wszib.msmolen.mt.client.process.LogoutProcess;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;
import pl.edu.wszib.msmolen.mt.common.auth.User;

public class CallTaxiPanel extends JPanel
{

	private static final long serialVersionUID = -3366954965931444343L;

	private final JLayeredPane mParent;

	private final JButton mBackButton;

	private final JTextField mPassengersCount;
	private final JCheckBox mLuggageTaxi;
	private final JCheckBox mCombiTaxiCheckBox;

	private final JButton mCallTaxiButton;
	private final JButton mCheckPriceButton;
	private final JButton mCheckTimeButton;

	private final ActionListener listener;

	public CallTaxiPanel(JLayeredPane pmParent)
	{
		mParent = pmParent;

		this.setLayout(null);
		this.setSize(mParent.getWidth() - 15, mParent.getHeight() - 45);
		this.setLocation(5, 5);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		listener = new ButtonActionListener(this);

		mBackButton = new JButton(UserManager.getInstance().getUser() != null ? "Wyloguj" : "< Wstecz");
		mBackButton.setSize(100, 30);
		mBackButton.setLocation(getWidth() - 100, 0);
		mBackButton.addActionListener(listener);
		this.add(mBackButton);

		mPassengersCount = new JTextField("Liczba pasa¿erów");
		mPassengersCount.setSize(200, 35);
		mPassengersCount.setLocation(5, 40);
		mPassengersCount.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent arg0)
			{
				try
				{
					Integer.parseInt(mPassengersCount.getText());
				}
				catch (Exception e)
				{
					mPassengersCount.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if ("".equals(mPassengersCount.getText().trim()))
					mPassengersCount.setText("Liczba pasa¿erów");
			}
		});
		this.add(mPassengersCount);

		mCombiTaxiCheckBox = new JCheckBox("Du¿y baga¿nik");
		mCombiTaxiCheckBox.setSize(200, 20);
		mCombiTaxiCheckBox.setLocation(5, 80);
		this.add(mCombiTaxiCheckBox);

		mLuggageTaxi = new JCheckBox("Taxi baga¿owe");
		mLuggageTaxi.setSize(200, 20);
		mLuggageTaxi.setLocation(5, 105);
		this.add(mLuggageTaxi);

		mCallTaxiButton = new JButton("Zamów taksówkê");
		mCallTaxiButton.setSize(200, 30);
		mCallTaxiButton.setLocation(5, 140);
		mCallTaxiButton.addActionListener(listener);
		this.add(mCallTaxiButton);

		mCheckPriceButton = new JButton("SprawdŸ cenê");
		mCheckPriceButton.setSize(200, 30);
		mCheckPriceButton.setLocation(5, 175);
		mCheckPriceButton.addActionListener(listener);
		this.add(mCheckPriceButton);

		mCheckTimeButton = new JButton("SprawdŸ czas oczekiwania");
		mCheckTimeButton.setSize(200, 30);
		mCheckTimeButton.setLocation(5, 210);
		mCheckTimeButton.addActionListener(listener);
		this.add(mCheckTimeButton);

	}

	private class ButtonActionListener implements ActionListener
	{
		private final JPanel mPanel;

		public ButtonActionListener(JPanel pmPanel)
		{
			mPanel = pmPanel;
		}

		@Override
		public void actionPerformed(ActionEvent evt)
		{
			if (mBackButton.equals(evt.getSource()))
			{
				User lvUser = UserManager.getInstance().getUser();
				if (lvUser != null)
				{
					new LogoutProcess((StartWindow) mParent.getRootPane().getParent(), UserManager.getInstance().getUser().getName()).process();
				}

				mParent.moveToBack(mPanel);
			}

			else if (mCallTaxiButton.equals(evt.getSource()))
			{
				final ChooseLocationDialog lvDialog = new ChooseLocationDialog((StartWindow) mParent.getRootPane().getParent());
				lvDialog.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosed(WindowEvent e)
					{

						if (lvDialog.getCoordinates() != null)
						{
							System.out.println(lvDialog.getCoordinates()[0] + " " + lvDialog.getCoordinates()[1]);
						}
					}
				});

				lvDialog.setVisible(true);
			}
		}
	}

}
