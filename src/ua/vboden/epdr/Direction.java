package ua.vboden.epdr;

public enum Direction {

	N(0), W(90), S(180), E(270);

	private int degress;

	private Direction(int degress) {
		this.degress = degress;
	}

	public int getDegress() {
		return degress;
	}

}
