import aoc.Day.IntDay;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
* <a href="https://adventofcode.com/2023/day/16">Day 16</a>
*/
public class Day16 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		return shoot(makeGrid(input), new Beam(0, 0, 0, 1));
	}

	@Override
	public int run2Int(List<String> input) {
		Pair<Character, Boolean>[][] grid = makeGrid(input);

		int max = Integer.MIN_VALUE;

		int len = input.size(), wid = input.get(0).length();
		for (int i = 0; i < len; i++) {
			int temp = shoot(grid, new Beam(i, 0, 0, 1));
			System.out.println(temp + " vs " + max);
			max = Math.max(max, temp);
			temp = shoot(grid, new Beam(i, wid - 1, 0, -1));
			System.out.println(temp + " vs " + max);
			max = Math.max(max, temp);
		}

		for (int i = 0; i < wid; i++) {
			int temp = shoot(grid, new Beam(0, i, 1, 0));
			System.out.println(temp + " vs " + max);
			max = Math.max(max, temp);
			temp = shoot(grid, new Beam(len - 1, i, -1, 0));
			System.out.println(temp + " vs " + max);
			max = Math.max(max, temp);
		}

		return max;
	}

	private Pair<Character, Boolean>[][] makeGrid(List<String> input) {
		int len = input.size(), wid = input.get(0).length();
		@SuppressWarnings("unchecked")
		Pair<Character, Boolean>[][] grid = new Pair[len][wid];
		for (int i = 0; i < len; i++) {
			String line = input.get(i);
			for (int j = 0; j < wid; j++) {
				grid[i][j] = Pair.of(line.charAt(j), false);
			}
		}
		return grid;
	}

	private int shoot(Pair<Character, Boolean>[][] grid, Beam start) {
		List<Beam> beams = new ArrayList<>();
		beams.add(start);
		int prevCount = -1;
		int count = 0;

		int timesSame = 0;

		while(!beams.isEmpty()) {
			for (int i = 0; i < beams.size(); i++) {
				Beam beam = beams.get(i);
				if (beam.r < 0 || beam.r >= grid.length || beam.c < 0 || beam.c >= grid[0].length) {
					beams.remove(i--);
					continue;
				}

				Pair<Character, Boolean> pair = grid[beam.r][beam.c];

				switch(pair.first()) {
					case '/' -> {
						int temp = beam.dr;
						beam.dr = -beam.dc;
						beam.dc = -temp;
					}
					case '\\' -> {
						int temp = beam.dr;
						beam.dr = beam.dc;
						beam.dc = temp;
					}
					case '|' -> {
						if(beam.dc != 0) {
							beams.add(new Beam(beam.r, beam.c, 1, 0));
							beam.dr = -1;
							beam.dc = 0;
						}
					}
					case '-' -> {
						if(beam.dr != 0) {
							beams.add(new Beam(beam.r, beam.c, 0, 1));
							beam.dr = 0;
							beam.dc = -1;
						}
					}
					case '.' -> {}
					default -> throw new IllegalStateException("Unexpected value: " + pair.first());
				}

				if(!pair.second()) {
					pair.second(true);
					count++;
				}
				beam.r += beam.dr;
				beam.c += beam.dc;
			}

			if(prevCount == count) {
				timesSame++;
				if(timesSame > 10) {
					break;
				}
			} else {
				timesSame = 0;
			}
			prevCount = count;
		}

		return count;
	}

	private static class Beam {
		private int r, c, dr, dc;

		private Beam(int r, int c, int dr, int dc) {
			this.r = r;
			this.c = c;
			this.dr = dr;
			this.dc = dc;
		}

		@Override
		public String toString() {
			return "Beam{" +
					"r=" + r +
					", c=" + c +
					", dr=" + dr +
					", dc=" + dc +
					'}';
		}
	}
}
