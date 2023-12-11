package util;

public class Coordinate extends Couple<Integer> {
	public Coordinate(int first, int second) {
		super(first, second);
	}

	public int x() {
		return first;
	}

	public int y() {
		return second;
	}

	public int getX() {
		return first;
	}

	public int getY() {
		return second;
	}

	public void x(int x) {
		setFirst(x);
	}

	public void y(int y) {
		setSecond(y);
	}
}
