package ua.vboden.epdr;

public enum Color {
	GREEN(0), RED(1), YELLOW(2);

	private int nodePosition;

	private Color(int nodePosition) {
		this.nodePosition = nodePosition;
	}

	public int getNodePosition() {
		return nodePosition;
	}

}
