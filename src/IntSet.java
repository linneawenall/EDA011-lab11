import java.util.ArrayList;

/** Beskriver en mängd av heltal. Varje heltal kan ingå högst en gång i mängden. */
public class IntSet {
	// Talen i mängden lagras i en ArrayList. En effektivare (och enklare) lösning
	// fås genom att använda en prioritetskö (klassen PriorityQueue). Sådana kan du
	// lära dig om i fördjupningskursen.
	
	private ArrayList<Integer> nbrs;

	/** Skapar en tom mängd av heltal. */
	public IntSet() {
		nbrs = new ArrayList<Integer>();
	}

	/** Lägger till n till denna mängd. Om n redan ingår händer inget. */
	public void add(int n) {
		if (! nbrs.contains(n)) {
			nbrs.add(n);
		}
	}

	/** Lägger till alla heltal från mängden s till denna mängd. */
	public void addAll(IntSet other) {
		for (int n : other.nbrs) {
			add(n);
		}
	}

	/** Tar reda på om denna mängd innehåller talet n. */
	public boolean contains(int n) {
		return nbrs.contains(n);
	}

	/** Tar reda på det minsta talet i denna mängd, eller 0 om mängden är tom. */
	public int least() {
		int min = Integer.MAX_VALUE;
		for (int n : nbrs) {
			if (n < min) {
				min = n;
			}
		}
		return min == Integer.MAX_VALUE ? 0 : min;
	}
}
