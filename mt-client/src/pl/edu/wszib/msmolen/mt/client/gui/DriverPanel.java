package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pl.edu.wszib.msmolen.mt.client.process.CheckOrdersProcess;
import pl.edu.wszib.msmolen.mt.client.process.LogoutProcess;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;

/**
 * Panel taksowkarza. Wyswietla informacje o przychodzacych zgloszeniach.
 * Umozliwia taksowkarzowi zgloszenie zabrania i dowiezenia klienta, zgloszenie
 * braku klienta lub innego problemu.
 * 
 * @author msmolen
 * 
 */
public class DriverPanel extends JPanel
{
	private static final long serialVersionUID = 5434030190855063909L;

	private final JLayeredPane mParent;

	private final JButton mBackButton;
	private final JLabel mNothingToDoLabel;

	private final ActionListener listener;

	private Timer mCheckOrdersTimer;

	public DriverPanel(JLayeredPane pmParent)
	{
		mParent = pmParent;

		this.setLayout(null);

		this.setSize(mParent.getWidth() - 15, mParent.getHeight() - 45);
		this.setLocation(5, 5);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		listener = new ButtonActionListener(this);

		mBackButton = new JButton("Wyloguj");
		mBackButton.setSize(100, 30);
		mBackButton.setLocation(getWidth() - 100, 0);
		mBackButton.addActionListener(listener);
		this.add(mBackButton);

		mNothingToDoLabel = new JLabel("Nie ma nowych zg³oszeñ");
		mNothingToDoLabel.setSize(getWidth(), 30);
		mNothingToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mNothingToDoLabel.setLocation(5, 150);
		mNothingToDoLabel.setVisible(false);
		this.add(mNothingToDoLabel);

	}

	/**
	 * Wyswietla labelke "Nie ma nowych zgloszen"
	 */
	public void showNothingToDoLabel()
	{
		mNothingToDoLabel.setVisible(true);
	}

	/**
	 * Uruchamia timer odpytujacy serwer o zgloszenia
	 */
	public void startTimer()
	{
		mCheckOrdersTimer = new Timer();
		mCheckOrdersTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				new CheckOrdersProcess((StartWindow) mParent.getRootPane().getParent(), DriverPanel.this).process();
			}
		}, 0, 10000);
	}

	public void stopTimer()
	{
		mCheckOrdersTimer.cancel();
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
				stopTimer();

				new LogoutProcess((StartWindow) mParent.getRootPane().getParent(), UserManager.getInstance().getUser().getName()).process();
				mParent.moveToBack(mPanel);
			}
		}
	}

}
