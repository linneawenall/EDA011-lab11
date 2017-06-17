public class GrayscaleImage {
	/** Skapar en gråskalebild med innehåll läst från filen filename. */
	public GrayscaleImage(String filename) {
	}

	/** Tar reda på bildens höjd (antalet pixelrader). */
	public int getHeight() {
		return 0;
	}

	/** Tar reda på bildens bredd (antalet pixlar på varje rad). */
	public int getWidth() {
		return 0;
	}

	/**
	 * Tar reda på pixelvärdet på rad r och kolumn c. Om (r,c) pekar utanför
	 * bilden returneras värdet på den närmsta pixeln.
	 */
	public int get(int r, int c) {
		return 0;
	}

	/**
	 * Tar reda på bildens histogram, det vill säga en vektor där element
	 * i anger hur många pixlar i bilden som har värdet i.
	 */
	public int[] histogram() {
		return null;
	}
}
