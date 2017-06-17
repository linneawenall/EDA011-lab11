import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Läser in en bildfil med givet filnamn, omvandlar den till gråskala och
 * returnerar en matris av heltalsvärden i intervallet 0..255. Matrisens
 * dimensioner överensstämmer med bildens.
 */
public class ImageReader {
	public static int[][] readImage(String filename) {
		try {
			Raster filedata = ImageIO.read(new File(filename)).getRaster();
			int[][] pixels = new int[filedata.getHeight()][filedata.getWidth()];
			final int nbrBands = filedata.getNumBands();
			for (int i = 0; i < filedata.getHeight(); i++) {
				for (int j = 0; j < filedata.getWidth(); j++) {
					int s = 0;
					for (int k = 0; k < nbrBands; k++) {
						s += filedata.getSample(j, i, k);
					}
					pixels[i][j] = s / nbrBands;
				}
			}

			return pixels;
		} catch (IOException e) {
			throw new Error(e);
		}
	}
}
