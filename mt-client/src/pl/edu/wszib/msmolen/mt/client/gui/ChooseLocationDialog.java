package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import pl.edu.wszib.msmolen.mt.client.process.DownloadMapProcess;
import pl.edu.wszib.msmolen.mt.client.utils.Location;

public class ChooseLocationDialog extends JDialog
{
	private static final long serialVersionUID = 580967660301041077L;

	private final int WIDTH = 650;
	private final int HEIGHT = 680;

	private final JButton mCloseButton;
	private final JButton mConfirmButton;
	private final JLabel mMapLabel;

	private final ActionListener mActionListener = new ButtonActionListener();

	private Double mLattitude = null;
	private Double mLongitude = null;

	private Point mLocation = null;

	private StartWindow mMainWindow = null;

	public ChooseLocationDialog(StartWindow pmParent)
	{
		super(pmParent, ModalityType.APPLICATION_MODAL);

		mMainWindow = pmParent;

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) (screen.getWidth() - WIDTH) / 2, (int) (screen.getHeight() - HEIGHT) / 2);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);

		this.setUndecorated(true);

		mCloseButton = new JButton("Zamknij");
		mCloseButton.setSize(100, 20);
		mCloseButton.setLocation(5, 650);
		mCloseButton.addActionListener(mActionListener);
		this.add(mCloseButton);

		mConfirmButton = new JButton("PotwierdŸ wybór");
		mConfirmButton.setSize(150, 20);
		mConfirmButton.setLocation(490, 650);
		mConfirmButton.addActionListener(mActionListener);
		this.add(mConfirmButton);

		mMapLabel = new JLabel()
		{
			private static final long serialVersionUID = 1168076902144883045L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				if (mLocation != null)
				{
					g.drawPolygon(new int[] { mLocation.x, mLocation.x - 5, mLocation.x + 5 }, new int[] { mLocation.y, mLocation.y - 10, mLocation.y - 10 }, 3);
					g.setColor(Color.RED);
					g.fillPolygon(new int[] { mLocation.x, mLocation.x - 5, mLocation.x + 5 }, new int[] { mLocation.y, mLocation.y - 10, mLocation.y - 10 }, 3);
				}
			}
		};
		mMapLabel.setSize(640, 640);
		mMapLabel.setLocation(5, 5);
		mMapLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mMapLabel.addMouseListener(new MapListener());
		this.add(mMapLabel);

		loadMapOntoWindow();

	}

	public double[] getCoordinates()
	{
		return mLattitude == null || mLongitude == null ? null : new double[] { mLattitude, mLongitude };
	}

	public void loadMapOntoWindow()
	{
		new DownloadMapProcess(mMainWindow, mMapLabel).process();
	}

	private class ButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			if (mCloseButton.equals(evt.getSource()))
			{
				mLattitude = null;
				mLongitude = null;

				ChooseLocationDialog.this.dispose();
			}

			else if (mConfirmButton.equals(evt.getSource()))
			{
				ChooseLocationDialog.this.dispose();
			}
		}
	}

	private class MapListener extends MouseAdapter
	{
		@Override
		public void mouseReleased(MouseEvent e)
		{
			mLattitude = Location.calculateLongitude(e.getX());
			mLongitude = Location.calculateLattitude(e.getY());

			mLocation = new Point(e.getX(), e.getY());

			mMapLabel.repaint();
		}
	}

}
