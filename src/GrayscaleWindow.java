import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class GrayscaleWindow {
	private ImageFrame frame;
	private final GrayscaleImage image;

	private LinkedBlockingQueue<Integer> events = new LinkedBlockingQueue<Integer>();

	/**
	 * Skapar ett nytt fönster för en gråskalebild. Fönstret får titeln title
	 * och visar bilden image. Bilden visas i skalan 2:1.
	 */
	public GrayscaleWindow(GrayscaleImage image, String title) {
		this(image, title, 2);
	}

	/**
	 * Skapar ett nytt fönster för en gråskalebild. Fönstret får titeln title
	 * och visar bilden image med förstoringen magnification.
	 */
	public GrayscaleWindow(GrayscaleImage image, String title, int magnification) {
		frame = new ImageFrame(image.getWidth(), image.getHeight(), title,
				magnification);
		this.image = image;
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		update();
	}

	/**
	 * Uppdatera fönstret. Det är nödvändigt att anropa denna metod om bilden
	 * ändrats sedan fönstret skapades.
	 */
	public void update() {
		frame.bitmapPanel.putPixels(image);
		frame.histogramPanel.repaint();
	}

	/**
	 * Lägg till en knapp med texten "label", som när den klickas leder till att
	 * värdet "command" returnernas av getCommand.
	 */
	@SuppressWarnings("serial")
	public void addButton(String label, final int command) {
		frame.addButton(new JButton(new AbstractAction(label) {
			public void actionPerformed(ActionEvent e) {
				events.offer(command);
			}
		}));
	}

	/**
	 * Väntar tills användaren klickar antingen i histogrammet eller på en
	 * knapp. Om hen klickat i histogrammet returneras det pixelvärde (0..255)
	 * som klickats på. Annars returneras det värde som förknippats med knappen,
	 * då den lades till med metoden addButton ovan.
	 */
	public int getCommand() {
		try {
			return events.take();
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	private class ImageFrame extends JFrame implements MouseMotionListener,
			MouseListener {
		private static final long serialVersionUID = 1;
		private static final int NBR_LEVELS = 256;
		private BitmapPanel bitmapPanel;
		private HistogramPanel histogramPanel;
		private StatusBar statusBar;

		private final Box buttonBox;
		private int buttonIndex = 0;

		private ImageFrame(int width, int height, String title,
				int magnification) {
			super(title);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Box vbox = Box.createVerticalBox();
			buttonBox = Box.createHorizontalBox();
			buttonBox.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			buttonBox.add(statusBar = new StatusBar(width * magnification));

			vbox.add(bitmapPanel = new BitmapPanel(width, height, magnification));
			vbox.add(histogramPanel = new HistogramPanel(width, magnification));
			vbox.add(buttonBox);
			add(vbox);

			bitmapPanel.addMouseMotionListener(this);
			bitmapPanel.addMouseListener(this);
			histogramPanel.addMouseMotionListener(this);
			histogramPanel.addMouseListener(this);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		private void addButton(JButton b) {
			buttonBox.add(b, buttonIndex++);
			pack();
		}

		private class BitmapPanel extends JPanel {
			private static final long serialVersionUID = 1;
			private final Color[] grayValues;
			private BufferedImage pixels;
			private int scaledWidth, scaledHeight;
			private int magnification;

			private BitmapPanel(int width, int height, int magnification) {
				scaledWidth = width * magnification;
				scaledHeight = height * magnification;
				this.magnification = magnification;
				pixels = new BufferedImage(width, height,
						BufferedImage.TYPE_BYTE_GRAY);
				grayValues = new Color[NBR_LEVELS];
				for (int i = 0; i < NBR_LEVELS; i++) {
					float v = i / (float) (NBR_LEVELS - 1);
					grayValues[i] = new Color(v, v, v);
				}
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
				setPreferredSize(new Dimension(scaledWidth, scaledHeight));
				setMinimumSize(getPreferredSize());
				setMaximumSize(getPreferredSize());
				setSize(getPreferredSize());
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(pixels, 0, 0, scaledWidth, scaledHeight, this);
			}

			private final void putPixels(GrayscaleImage image) {
				Graphics g = pixels.getGraphics();
				int height = image.getHeight();
				int width = image.getWidth();
				for (int h = 0; h < height; ++h) {
					for (int w = 0; w < width; ++w) {
						int v = image.get(h, w);
						v = (v < 0) ? 0
								: (v >= NBR_LEVELS ? NBR_LEVELS - 1 : v);
						g.setColor(grayValues[v]);
						g.fillRect(w, h, 1, 1);
					}
				}
				repaint();
			}

			private final int getPixel(int x, int y) {
				return image.get(y / magnification, x / magnification);
			}
		}

		private class HistogramPanel extends JPanel {
			private static final long serialVersionUID = 1;
			private int scaledWidth, scaledHeight;
			private int barWidth;
			private int leftOffset;

			private static final int HISTOGRAM_HEIGHT = 150;

			private HistogramPanel(int width, int magnification) {
				scaledWidth = width * magnification;
				scaledHeight = HISTOGRAM_HEIGHT;
				barWidth = scaledWidth / NBR_LEVELS;
				leftOffset = (scaledWidth % NBR_LEVELS) / 2;
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
				setPreferredSize(new Dimension(scaledWidth, scaledHeight));
				setMinimumSize(getPreferredSize());
				setMaximumSize(getPreferredSize());
				setSize(getPreferredSize());
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int[] histogram = null;
				try {
					histogram = image.histogram();
				} catch (Throwable t) {
					g.drawString("histogram() gav fel: " + t, 10,
							scaledHeight / 2);
					t.printStackTrace();
					return;
				}
				if (histogram == null) {
					g.drawString("histogram() == null", 10, scaledHeight / 2);
					return;
				} else if (histogram.length != NBR_LEVELS) {
					g.drawString("histogramvektorn har fel längd ("
							+ histogram.length + ")", 10, scaledHeight / 2);
					return;
				}
				double maxHeight = 1;
				int maxValue = 0;
				for (int v : histogram) {
					if (v > 0) {
						maxHeight = Math.max(Math.log(v), maxHeight);
						maxValue = Math.max(v, maxValue);
					}
				}
				g.setColor(Color.WHITE);
				g.fillRect(leftOffset, 0, barWidth * NBR_LEVELS,
						HISTOGRAM_HEIGHT);
				g.setColor(Color.BLACK);
				for (int i = 0; i < NBR_LEVELS; i++) {
					int barHeight = (int) (HISTOGRAM_HEIGHT * Math.log(histogram[i]) / maxHeight);
					g.fillRect(leftOffset + i * barWidth, HISTOGRAM_HEIGHT
							- barHeight, barWidth, barHeight);
				}
			}

			/**
			 * Tar reda på det index i histogrammet som pekas ut av den givna
			 * x-koordinaten, eller < 0 om x inte faller inom histogrammet.
			 */
			private int barIndex(int x) {
				int idx = (x - leftOffset) / barWidth;
				if (idx < 0 || idx >= NBR_LEVELS) {
					return -1;
				}
				return idx;
			}
		}

		private class StatusBar extends JPanel {
			private static final long serialVersionUID = 1L;
			private static final int NO_COLOR = -1;

			private final JLabel label = new JLabel(" ");
			private final Color defaultBackground;

			private StatusBar(int width) {
				defaultBackground = getBackground();
				add(label);
			}

			private void setMessage(String s, int color) {
				label.setForeground(color < NBR_LEVELS / 2 ? Color.WHITE
						: Color.BLACK);
				label.setText(s);
				setBackground(new Color(color, color, color));
			}

			private void hideMessage() {
				label.setText(" ");
				setBackground(defaultBackground);
			}
		}

		// ---------------------------------------------------- MOUSE LISTENERS

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Object source = e.getSource();
			int x = e.getX();
			int y = e.getY();
			if (source == bitmapPanel) {
				int c = bitmapPanel.getPixel(x, y);
				statusBar.setMessage("get(" + y + "," + x + ") = " + c, c);
			} else if (source == histogramPanel) {
				try {
					int idx = histogramPanel.barIndex(x);
					int[] histogram = image.histogram();
					if (idx >= 0 || histogram == null) {
						statusBar.setMessage("histogram[" + idx + "] = "
								+ histogram[idx], idx);
					} else {
						statusBar.hideMessage();
					}
				} catch (Throwable t) {
					statusBar.setMessage("kan inte läsa histogram",
							StatusBar.NO_COLOR);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source == histogramPanel) {
				int bar = histogramPanel.barIndex(e.getX());
				events.add(bar);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			statusBar.hideMessage();
		}
	}
}
