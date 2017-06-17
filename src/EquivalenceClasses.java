/** Beskriver ekvivalensklasser av heltal. */
public class EquivalenceClasses {
	/** Skapar en ny lista över ekvivalensklasser. */
	public EquivalenceClasses() {
	}

	/** Anger att a och b ska tillhöra samma ekvivalensklass.
        - Om a och b båda redan ingår i olika ekvivalensklasser, så ska de två
          ekvivalensklasserna ersättas av en sammanslagen.
        - Om ett av talen ingår i en ekvivalensklass, men inte det andra,
          läggs ett tal till i en ekvivalensklass.
        - Om varken a eller b ingår i någon ekvivalensklass skapas en ny. */
	public void join(int a, int b) {
	}

	/** Tar reda på det minsta heltal i den ekvivalensklass där n ingår.
        Om n inte ingår i någon ekvivalensklass returneras n självt. */
	public int least(int n) {
		return n;
	}
}
