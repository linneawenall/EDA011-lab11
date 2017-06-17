public class TestEquivalenceClasses {

	private EquivalenceClasses eq;

	/** Skapar en ny testuppställning. */
	public TestEquivalenceClasses() {
		eq = new EquivalenceClasses();
	}
	
	/** Kontrollerar att a och b tillhör samma ekvivalensklass. */
	private void checkEquivalent(int a, int b) {
		System.out.print(a + " och " + b + " ekvivalenta:        ");
		int aLeast = eq.least(a);
		int bLeast = eq.least(b);
		if (aLeast == bLeast) {
			System.out.println("fungerar");
		} else {
			System.out.println("fungerar INTE [least(" + a + ")=" + aLeast + ", least(" + b + ")=" + bLeast + "]");
		}
	}

	/** Kontrollerar att a och b INTE tillhör samma ekvivalensklass. */
	private void checkNotEquivalent(int a, int b) {
		System.out.print(a + " och " + b + " inte ekvivalenta:   ");
		int aLeast = eq.least(a);
		int bLeast = eq.least(b);
		if (aLeast != bLeast) {
			System.out.println("fungerar");
		} else {
			System.out.println("fungerar INTE [least(" + a + ")=" + aLeast + ", least(" + b + ")=" + bLeast + "]");
		}
	}
	
	/**
	 * Använd ett EquivalenceClasses-objekt, skapa ekvivalensklasser och
	 * kontrollera att de fungerar.
	 */
	public void runTest() {
		
		// Test 1: skapa tre olika ekvivalensklasser
		//         {1,3}  {7,8}  {5,9}

		System.out.println("*** Test 1");
		eq.join(1, 3);
		eq.join(7, 8);
		eq.join(5, 9);
		
		checkEquivalent(1, 3);
		checkEquivalent(7, 8);
		checkEquivalent(5, 9);
		
		checkNotEquivalent(1, 2);
		checkNotEquivalent(2, 5);
		checkNotEquivalent(4, 5);
		checkNotEquivalent(7, 9);
		
		// Test 2: utöka en av ekvivalensklasserna
		//         {1,3}  {2,7,8}  {5,9}

		System.out.println("*** Test 2");
		eq.join(2, 7);

		checkEquivalent(2, 7);
		checkEquivalent(7, 8);
		checkEquivalent(2, 8);

		checkEquivalent(1, 3);
		checkEquivalent(5, 9);

		checkNotEquivalent(1, 2);
		checkNotEquivalent(2, 5);
		checkNotEquivalent(4, 5);
		checkNotEquivalent(7, 9);
		
		// Test 3: slå ihop två av ekvivalensklasserna
		//         {1,3,5,9}  {2,7,8}

		System.out.println("*** Test 3");
		eq.join(1, 5);

		checkEquivalent(1, 3);
		checkEquivalent(1, 5);
		checkEquivalent(1, 9);
		checkEquivalent(3, 5);
		checkEquivalent(3, 9);
		checkEquivalent(5, 9);

		checkEquivalent(2, 7);
		checkEquivalent(7, 8);
		checkEquivalent(2, 8);

		checkNotEquivalent(1, 2);
		checkNotEquivalent(2, 5);
		checkNotEquivalent(4, 5);
		checkNotEquivalent(7, 9);
		
		// Test 4: slå ihop ekvivalensklasserna igen,
		//         kolla att ekvivalenserna finns kvar
		System.out.println("*** Test 4");
		eq.join(1, 5);

		checkEquivalent(1, 3);
		checkEquivalent(1, 5);
		checkEquivalent(1, 9);
		checkEquivalent(3, 5);
		checkEquivalent(3, 9);
		checkEquivalent(5, 9);

		checkEquivalent(2, 7);
		checkEquivalent(7, 8);
		checkEquivalent(2, 8);

		checkNotEquivalent(1, 2);
		checkNotEquivalent(2, 5);
		checkNotEquivalent(4, 5);
		checkNotEquivalent(7, 9);
		
		// Test 5: slå ihop ett och samma tal i en ekvivalensklass,
		//         kolla att ekvivalenserna finns kvar
		System.out.println("*** Test 5");
		eq.join(3, 3);

		checkEquivalent(1, 3);
		checkEquivalent(1, 5);
		checkEquivalent(1, 9);
		checkEquivalent(3, 5);
		checkEquivalent(3, 9);
		checkEquivalent(5, 9);

		checkEquivalent(2, 7);
		checkEquivalent(7, 8);
		checkEquivalent(2, 8);

		checkNotEquivalent(1, 2);
		checkNotEquivalent(2, 5);
		checkNotEquivalent(4, 5);
		checkNotEquivalent(7, 9);
	}

	public static void main(String[] args) {
		TestEquivalenceClasses tester = new TestEquivalenceClasses();
		tester.runTest();
	}

}
