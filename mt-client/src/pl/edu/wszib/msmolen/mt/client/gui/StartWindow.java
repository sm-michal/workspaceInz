package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.edu.wszib.msmolen.mt.client.process.LoginProcess;
import pl.edu.wszib.msmolen.mt.client.process.RegisterProcess;
import pl.edu.wszib.msmolen.mt.client.utils.TokenManager;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;
import pl.edu.wszib.msmolen.mt.common.auth.User;
import pl.edu.wszib.msmolen.mt.common.auth.UserType;

public class StartWindow extends JFrame
{

	private static final long serialVersionUID = -1610499838469467227L;

	private final JLayeredPane mLayeredPanel;

	private final JPanel mCallTaxiPanel;
	private final JPanel mDriverPanel;

	private final JPanel mMainPanel;

	private final JTextField mLoginField;
	private final JPasswordField mPassword;
	private final JCheckBox mRememberMeCheck;
	private final JButton mLoginButton;
	private final JButton mRegisterButton;

	private final JLabel mOrLabel;

	private final JButton mCallTaxiButton;

	private final ActionListener listener = new ButtonListener();

	private final int WIDTH = 240;
	private final int HEIGHT = 400;

	private static StartWindow instance = null;

	public static StartWindow getInstance()
	{
		if (instance == null)
			instance = new StartWindow();

		return instance;
	}

	public static void startGUI()
	{
		getInstance().setVisible(true);
	}

	private StartWindow()
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) (screen.getWidth() - WIDTH) / 2, (int) (screen.getHeight() - HEIGHT) / 2);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setLayout(null);
		this.setTitle("MOBIL-TAXI");
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent evt)
			{
				TokenManager.getInstance().disposeToken();
				System.exit(0);
			}
		});

		mMainPanel = new JPanel(null);
		mMainPanel.setSize(WIDTH - 15, HEIGHT - 45);
		mMainPanel.setLocation(5, 5);
		mMainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		mLoginField = new JTextField("Login");
		mLoginField.setSize(200, 30);
		mLoginField.setLocation(10, 10);
		mMainPanel.add(mLoginField);

		mPassword = new JPasswordField("Has³o");
		mPassword.setSize(200, 30);
		mPassword.setLocation(10, 50);
		mMainPanel.add(mPassword);

		mRememberMeCheck = new JCheckBox("Zapamiêtaj mnie");
		mRememberMeCheck.setSize(200, 30);
		mRememberMeCheck.setLocation(10, 80);
		mMainPanel.add(mRememberMeCheck);

		mLoginButton = new JButton("Zaloguj");
		mLoginButton.setSize(90, 40);
		mLoginButton.setLocation(10, 130);
		mLoginButton.addActionListener(listener);
		mMainPanel.add(mLoginButton);

		mRegisterButton = new JButton("Rejestracja");
		mRegisterButton.setSize(100, 40);
		mRegisterButton.setLocation(110, 130);
		mRegisterButton.addActionListener(listener);
		mMainPanel.add(mRegisterButton);

		mOrLabel = new JLabel("lub", JLabel.CENTER);
		mOrLabel.setSize(200, 40);
		mOrLabel.setLocation(10, 180);
		mMainPanel.add(mOrLabel);

		mCallTaxiButton = new JButton("<html><center>Zamów taksówkê<br>bez logowania...</center></html>");
		mCallTaxiButton.setSize(200, 100);
		mCallTaxiButton.setLocation(10, 230);
		mCallTaxiButton.addActionListener(listener);
		mMainPanel.add(mCallTaxiButton);

		mLayeredPanel = new JLayeredPane();
		mLayeredPanel.setLayout(null);
		mLayeredPanel.setSize(WIDTH, HEIGHT);
		mLayeredPanel.setLocation(0, 0);

		mCallTaxiPanel = new CallTaxiPanel(mLayeredPanel);
		mDriverPanel = new DriverPanel(mLayeredPanel);

		mLayeredPanel.add(mMainPanel, new Integer(10));
		mLayeredPanel.add(mCallTaxiPanel, new Integer(0));
		mLayeredPanel.add(mDriverPanel, new Integer(0));

		this.add(mLayeredPanel);
	}

	/**
	 * Wyswietla komunikat bledu
	 * 
	 * @param pmTitle
	 * @param pmMessage
	 */
	public void displayErrorMessage(String pmTitle, String pmMessage)
	{
		JOptionPane.showMessageDialog(this, pmMessage, pmTitle, JOptionPane.WARNING_MESSAGE);
	}

	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			if (mLoginButton.equals(evt.getSource()))
			{
				new LoginProcess(mLoginField.getText(), mPassword.getPassword()).process();
				showProperWindow(UserManager.getInstance().getUser());
			}
			else if (mRegisterButton.equals(evt.getSource()))
			{
				new RegisterProcess(mLoginField.getText(), mPassword.getPassword()).process();
				showProperWindow(UserManager.getInstance().getUser());
			}
			else if (mCallTaxiButton.equals(evt.getSource()))
			{
				mLayeredPanel.setLayer(mCallTaxiPanel, 10, 0);
			}
		}

		/**
		 * W zaleznosci od uzytkownika wyswietla panel klienta lub taksowkarza
		 * 
		 * @param pmUser
		 */
		private void showProperWindow(User pmUser)
		{
			if (pmUser != null)
			{
				if (pmUser.getUserType() == UserType.CLIENT)
					mLayeredPanel.setLayer(mCallTaxiPanel, 10, 0);
				else if (pmUser.getUserType() == UserType.DRIVER)
				{
					mLayeredPanel.setLayer(mDriverPanel, 10, 0);

					((DriverPanel) mDriverPanel).startTimer();
				}
			}
		}
	}

}
