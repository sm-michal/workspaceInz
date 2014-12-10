package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import pl.edu.wszib.msmolen.mt.client.process.LogoutProcess;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;

public class DriverPanel extends JPanel
{
	private static final long serialVersionUID = 5434030190855063909L;

	private final JLayeredPane mParent;

	private final JButton mBackButton;
	private final ActionListener listener;

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
				new LogoutProcess((StartWindow) mParent.getRootPane().getParent(), UserManager.getInstance().getUser().getName()).process();
				mParent.moveToBack(mPanel);
			}
		}
	}

}
