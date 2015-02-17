package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pl.edu.wszib.msmolen.mt.client.process.CheckOrdersProcess;
import pl.edu.wszib.msmolen.mt.client.process.LogoutProcess;
import pl.edu.wszib.msmolen.mt.client.process.UpdateDriverLocationProcess;
import pl.edu.wszib.msmolen.mt.client.utils.Location;
import pl.edu.wszib.msmolen.mt.client.utils.UserManager;
import pl.edu.wszib.msmolen.mt.common.utils.Orders;
import pl.edu.wszib.msmolen.mt.common.utils.Pair;

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

	private final JPanel mOrderPanel;

	private final JLabel mWaitingTitle;
	private final JLabel mWaitingText;

	private final JLabel mDestinationTitle;
	private final JLabel mDestinationText;

	private final JLabel mMapLabel;

	private final JScrollPane mScrollMapPane;

	private final ActionListener listener;

	private Timer mCheckOrdersTimer;

	private Orders mCurrentOrder;

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
		mNothingToDoLabel.setSize(getWidth(), getHeight());
		mNothingToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mNothingToDoLabel.setLocation(5, 5);
		mNothingToDoLabel.setOpaque(true);
		mNothingToDoLabel.setVisible(false);
		this.add(mNothingToDoLabel);

		mOrderPanel = new JPanel(null);
		mOrderPanel.setSize(getWidth() - 2, getHeight() - 31);
		mOrderPanel.setLocation(1, 30);
		mOrderPanel.setVisible(true);
		this.add(mOrderPanel);

		mWaitingTitle = new JLabel("Miejsce oczekiwania:");
		mWaitingTitle.setSize(130, 20);
		mWaitingTitle.setLocation(5, 0);
		mOrderPanel.add(mWaitingTitle);

		mWaitingText = new JLabel("");
		mWaitingText.setSize(getWidth() - 10, 20);
		mWaitingText.setLocation(5, 20);
		mOrderPanel.add(mWaitingText);

		mDestinationTitle = new JLabel(" Miejsce docelowe:");
		mDestinationTitle.setSize(130, 20);
		mDestinationTitle.setLocation(5, 40);
		mOrderPanel.add(mDestinationTitle);

		mDestinationText = new JLabel("");
		mDestinationText.setSize(getWidth() - 10, 20);
		mDestinationText.setLocation(5, 60);
		mOrderPanel.add(mDestinationText);

		mMapLabel = new JLabel()
		{

			private static final long serialVersionUID = 8016282349933470517L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				if (mCurrentOrder != null)
				{
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(3));
					g2.setColor(Color.RED);

					for (int i = 1; i < mCurrentOrder.getPath().size(); i++)
					{
						Pair<Double> start = mCurrentOrder.getPath().get(i - 1);
						Pair<Double> end = mCurrentOrder.getPath().get(i);
						g2.drawLine(
								Location.calculateX(start.getSecond()),
								Location.calculateY(start.getFirst()),
								Location.calculateX(end.getSecond()),
								Location.calculateY(end.getFirst())
								);
					}
				}
			}
		};

		mScrollMapPane = new JScrollPane(mMapLabel);
		mScrollMapPane.setSize(getWidth() - 10, getHeight() - 120);
		mScrollMapPane.setLocation(5, 80);
		mOrderPanel.add(mScrollMapPane);

	}

	/**
	 * Wyswietla labelke "Nie ma nowych zgloszen"
	 */
	public void showNothingToDoLabel()
	{
		mNothingToDoLabel.setVisible(true);
		mCurrentOrder = null;
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
				new CheckOrdersProcess(DriverPanel.this).process();
			}
		}, 0, 10000);
	}

	/**
	 * Zatrzymuje timer
	 */
	public void stopTimer()
	{
		mCheckOrdersTimer.cancel();
	}

	/**
	 * Wyswietla mape w panelu
	 * 
	 * @param pmImageFile
	 */
	public void loadMapOntoPanel(byte[] pmData, Orders pmOrders)
	{
		mNothingToDoLabel.setVisible(false);

		mMapLabel.setIcon(new ImageIcon(pmData));
		mCurrentOrder = pmOrders;
		mMapLabel.repaint();
		if (pmOrders != null && pmOrders.getPath() != null)
		{
			mScrollMapPane.getVerticalScrollBar().setValue(Location.calculateY(pmOrders.getPath().get(0).getFirst()) - mScrollMapPane.getHeight() / 2);
			mScrollMapPane.getHorizontalScrollBar().setValue(Location.calculateX(pmOrders.getPath().get(0).getSecond()) - mScrollMapPane.getWidth() / 2);

			startRideTimer();
		}
	}

	private void startRideTimer()
	{
		Timer lvTimer = new Timer();
		lvTimer.scheduleAtFixedRate(new TimerTask()
		{
			int mNextIndex = 1;

			Pair<Double> mCurrent = mCurrentOrder.getPath().get(0);

			@Override
			public void run()
			{
				Pair<Double> lvNext = mCurrentOrder.getPath().get(mNextIndex);

				double lvVerticalDistance = lvNext.getFirst() - mCurrent.getFirst();
				double lvHorizontalDistance = lvNext.getSecond() - mCurrent.getSecond();

				int lvVMetres = Location.deltaLatitudeToMetres(lvVerticalDistance);
				int lvHMetres = Location.deltaLongitudeToMetres(lvHorizontalDistance);

				double lvDistance = Math.sqrt(lvVMetres * lvVMetres + lvHMetres * lvHMetres);
				if (lvDistance < 20)
				{
					mCurrent = mCurrentOrder.getPath().get(mNextIndex);

					if (mNextIndex + 1 < mCurrentOrder.getPath().size())
					{
						mNextIndex++;
					}
					else
					{
						this.cancel();
					}
				}
				else
				{
					double lvFactor = 20.0 / lvDistance;

					double lvNewLat = mCurrent.getFirst() + lvVerticalDistance * lvFactor;
					double lvNewLon = mCurrent.getSecond() + lvHorizontalDistance * lvFactor;

					mCurrent = new Pair<Double>(lvNewLat, lvNewLon);

					mScrollMapPane.getVerticalScrollBar().setValue(Location.calculateY(lvNewLat) - mScrollMapPane.getHeight() / 2);
					mScrollMapPane.getHorizontalScrollBar().setValue(Location.calculateX(lvNewLon) - mScrollMapPane.getWidth() / 2);
				}

				new UpdateDriverLocationProcess(mCurrent.getFirst(), mCurrent.getSecond()).process();

			}
		}, 6 * 1000, 6 * 1000);
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

				new LogoutProcess(UserManager.getInstance().getUser().getName()).process();
				mParent.moveToBack(mPanel);
			}
		}
	}

}
