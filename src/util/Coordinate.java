package util;

import util.Couple.ImmutableCouple;

public class Coordinate extends ImmutableCouple<Integer> {
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
}
