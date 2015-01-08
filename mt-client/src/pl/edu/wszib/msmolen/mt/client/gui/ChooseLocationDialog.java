package pl.edu.wszib.msmolen.mt.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pl.edu.wszib.msmolen.mt.client.utils.Location;

public class ChooseLocationDialog extends JDialog
{
	private static final long serialVersionUID = 580967660301041077L;

	private final int WIDTH = 650;
	private final int HEIGHT = 520;

	private final JButton mCloseButton;
	private final JButton mConfirmButton;
	private final JLabel mMapLabel;

	private final ActionListener mActionListener = new ButtonActionListener();

	private Double mLattitude = null;
	private Double mLongitude = null;

	public ChooseLocationDialog(JFrame pmParent)
	{
		super(pmParent, ModalityType.APPLICATION_MODAL);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) (screen.getWidth() - WIDTH) / 2, (int) (screen.getHeight() - HEIGHT) / 2);
		this.setSize(650, 520);
		this.setLayout(null);

		this.setUndecorated(true);

		mCloseButton = new JButton("Zamknij");
		mCloseButton.setSize(100, 20);
		mCloseButton.setLocation(5, 490);
		mCloseButton.addActionListener(mActionListener);
		this.add(mCloseButton);

		mConfirmButton = new JButton("PotwierdŸ wybór");
		mConfirmButton.setSize(150, 20);
		mConfirmButton.setLocation(490, 490);
		mConfirmButton.addActionListener(mActionListener);
		this.add(mConfirmButton);

		mMapLabel = new JLabel();
		mMapLabel.setSize(640, 480);
		mMapLabel.setLocation(5, 5);
		mMapLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mMapLabel.addMouseListener(new MapListener());
		this.add(mMapLabel);

	}

	public double[] getCoordinates()
	{
		return mLattitude == null || mLongitude == null ? null : new double[] { mLattitude, mLongitude };
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
		}
	}

}
