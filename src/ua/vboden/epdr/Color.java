package ua.vboden.epdr;

public enum Color {
	GREEN("green"), RED("red"), YELLOW("yellow");

	private String nodeName;

	private Color(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

}