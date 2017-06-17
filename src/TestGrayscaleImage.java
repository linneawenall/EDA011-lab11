public class TestGrayscaleImage {
	
	/** Skriv ut en rad som berättar om testfallet gick bra. */
	public static void check(boolean success, String description) {
		String line = String.format("%-30s%s", description + ":", success ? "fungerar" : "fungerar INTE");
		System.out.println(line);
	}
	
	/** Kontrollerar att klassen GrayscaleImage fungerar med en känd bild. */
	public static void main(String[] args) {
		GrayscaleImage img = new GrayscaleImage("blodkroppar.png");

		check(img.getWidth() == 306, "getWidth");
		check(img.getHeight() == 120, "getHeight");
		
		check(img.get(0, 5) == 255, "get(0, 5)");
		check(img.get(25, 50) == 239, "get(25, 50)");
		if (img.get(0, 5) != 255 && img.get(25, 50) != 239) {
			if (img.get(50, 25) == 239) {
				System.out.println("Har du möjligen förväxlat rad- och kolonnindex?");
			}
			return; // inte lönt att fortsätta om inte detta stämmer
		}
		
		check(img.get(10, -1) == img.get(10, 0), "pixlar vänster om bilden");
		check(img.get(20, img.getWidth()) == img.get(20, img.getWidth() - 1), "pixlar höger om bilden");
		
		check(img.get(-1, 10) == img.get(0, 10), "pixlar ovanför bilden");
		check(img.get(img.getHeight(), 10) == img.get(img.getHeight() - 1, 10), "pixlar nedanför bilden");

		check(img.get(-1, -1) == img.get(0, 0), "utanför övre vänstra hörnet");
		check(img.get(-1, img.getWidth()) == img.get(0, img.getWidth() - 1), "utanför övre högra hörnet");
		check(img.get(img.getHeight(), -1) == img.get(img.getHeight() - 1, 0), "utanför nedre vänstra hörnet");
		check(img.get(img.getHeight(), img.getWidth()) == img.get(img.getHeight() - 1, img.getWidth() - 1), "utanför nedre högra hörnet");
		
		int[] hist = img.histogram(); 
		check(hist != null && hist.length == 256, "histogrammets storlek");
		check(hist[157] == 59, "histogram (stickprov)");
	}
}
