import aoc.Day.IntDay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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
		char[][] grid = makeGrid(input);

		int len = input.size(), wid = input.get(0).length();

		List<CompletableFuture<Void>> futures = new ArrayList<>();

		IntStream.range(0, len).forEach(i -> {
			futures.add(CompletableFuture.runAsync(() -> {
				int run = shoot(grid, new Beam(i, 0, 0, 1));
				if(run > max.get()) {
					max.set(run);
					System.out.println(run);
				}
			}));
			futures.add(CompletableFuture.runAsync(() -> {
				int run = shoot(grid, new Beam(i, wid - 1, 0, -1));
				if(run > max.get()) {
					max.set(run);
					System.out.println(run);
				}
			}));
		});

		IntStream.range(0, wid).forEach(i -> {
			futures.add(CompletableFuture.runAsync(() -> {
				int run = shoot(grid, new Beam(0, i, 1, 0));
				if(run > max.get()) {
					max.set(run);
					System.out.println(run);
				}
			}));
			futures.add(CompletableFuture.runAsync(() -> {
				int run = shoot(grid, new Beam(len - 1, i, -1, 0));
				if(run > max.get()) {
					max.set(run);
					System.out.println(run);
				}
			}));
		});

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		return max.get();
	}

	final AtomicInteger max = new AtomicInteger(-1);
	final AtomicInteger count = new AtomicInteger(0);

	private char[][] makeGrid(List<String> input) {
		int len = input.size(), wid = input.get(0).length();
		char[][] grid = new char[len][wid];
		for(int i = 0; i < len; i++) {
			grid[i] = input.get(i).toCharArray();
		}
		return grid;
	}

	private int shoot(char[][] grid, Beam start) {
		List<Beam> beams = new ArrayList<>();
		beams.add(start);
		int prevCount = -1;
		int count = 0;

		boolean[][] visited = new boolean[grid.length][grid[0].length];

		int timesSame = 0;

		while(!beams.isEmpty()) {
			for (int i = 0; i < beams.size(); i++) {
				Beam beam = beams.get(i);
				if (beam.r < 0 || beam.r >= grid.length || beam.c < 0 || beam.c >= grid[0].length) {
					beams.remove(i--);
					continue;
				}

				char c = grid[beam.r][beam.c];

				switch(c) {
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
				}

				if(!visited[beam.r][beam.c]) {
					visited[beam.r][beam.c] = true;
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
