import aoc.Day.IntDay;
import util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* <a href="https://adventofcode.com/2023/day/10">Day 10</a>
*/
public class Day10 extends IntDay {
	@Override
	public int run1Int(List<String> input) {
		return getLoop(getGrid(input)).size() / 2;
	}

	@Override
	public int run2Int(List<String> input) {
		Pair<PipePart[][], PipePart> pair = getGrid(input);
		PipePart[][] grid = pair.first();
		Set<PipePart> loop = getLoop(pair);

		for(PipePart[] row : grid) {
			for(PipePart part : row) {
				if(!loop.contains(part)) {
					grid[part.y][part.x] = new PipePart(Type.EMPTY, part.x, part.y);
				}
			}
		}

		int count = 0;
		for(PipePart[] row : grid) {
			for(PipePart part : row) {
				if(!loop.contains(part) && !hasPathToEdge(grid, part)) {
					count++;
				}
			}
		}

		return count;
	}

	private Pair<PipePart[][], PipePart> getGrid(List<String> input) {
		PipePart[][] grid = new PipePart[input.size()][input.get(0).length()];
		PipePart start = null;
		for(int y = 0; y < input.size(); y++) {
			String line = input.get(y);
			for(int x = 0; x < line.length(); x++) {
				grid[y][x] = new PipePart(Type.of(line.charAt(x)), x, y);
				if(grid[y][x].type == Type.START) {
					start = grid[y][x];
				}
			}
		}

		assert start != null;
		grid[start.y][start.x] = start = determineType(start, grid);
		return Pair.of(grid, start);
	}

	private Set<PipePart> getLoop(Pair<PipePart[][], PipePart> pair) {
		PipePart[][] grid = pair.first();
		PipePart start = pair.second();
		int x = start.x, y = start.y, prevX = x, prevY = y, length = 0;

		Set<PipePart> loop = new HashSet<>(Set.of(start));

		grid[y][x] = start = determineType(start, grid);

		while(true) {
			PipePart part = grid[y][x];
			if(part.type == Type.EMPTY ||(part.equals(start) && length > 0)) {
				break;
			}
			length++;
			loop.add(part);
			List<PipePart> adjacent = part.findAdjacent(grid);
			PipePart next = adjacent.get(0);
			if(next.x == prevX && next.y == prevY) {
				next = adjacent.get(1);
			}

			prevX = x;
			prevY = y;
			x = next.x;
			y = next.y;
		}
		return loop;
	}

	private boolean hasPathToEdge(PipePart[][] grid, PipePart part) {
		int count = 0;
		for(int x = part.x; x < grid[part.y].length; x++) {
			PipePart other = grid[part.y][x];
			if(other.type.hasNorth()) {
				count++;
			}
		}
		return count % 2 == 0;
	}

	private PipePart determineType(PipePart start, PipePart[][] grid) {
		List<PipePart> adjacent = start.findAdjacent(grid);
		boolean north = false, south = false, east = false, west = false;
		for(PipePart adj : adjacent) {
			if(adj.y < start.y) north = true;
			if(adj.y > start.y) south = true;
			if(adj.x < start.x) west = true;
			if(adj.x > start.x) east = true;
		}

		if(north && south && !east && !west) {
			return new PipePart(Type.VERTICAL, start.x, start.y);
		} else if(!north && !south && east && west) {
			return new PipePart(Type.HORIZONTAL, start.x, start.y);
		} else if(north && !south && east && !west) {
			return new PipePart(Type.NORTH_EAST, start.x, start.y);
		} else if(north && !south && !east && west) {
			return new PipePart(Type.NORTH_WEST, start.x, start.y);
		} else if(!north && south && east && !west) {
			return new PipePart(Type.SOUTH_EAST, start.x, start.y);
		} else if(!north && south && !east && west) {
			return new PipePart(Type.SOUTH_WEST, start.x, start.y);
		}
		throw new AssertionError();
	}

	record PipePart(Type type, int x, int y) {
		public List<PipePart> findAdjacent(PipePart[][] grid) {
			List<PipePart> result = new ArrayList<>();

			PipePart north = y > 0 ? grid[y - 1][x] : null;
			if(north != null && type.hasNorth() && north.type.hasSouth()) {
				result.add(north);
			}

			PipePart south = y < grid.length - 1 ? grid[y + 1][x] : null;
			if(south != null && type.hasSouth() && south.type.hasNorth()) {
				result.add(south);
			}

			PipePart east = x < grid[y].length - 1 ? grid[y][x + 1] : null;
			if(east != null && type.hasEast() && east.type.hasWest()) {
				result.add(east);
			}

			PipePart west = x > 0 ? grid[y][x - 1] : null;
			if(west != null && type.hasWest() && west.type.hasEast()) {
				result.add(west);
			}

			return result;
		}

		// use custom equals and hashcode to speed up the hashset

		@Override
		public boolean equals(Object obj) {
			return obj instanceof PipePart other && other.x == x && other.y == y;
		}

		@Override
		public int hashCode() {
			return x * 31 + y;
		}
	}

	enum Type {
		START('S'),
		VERTICAL('|'),
		HORIZONTAL('-'),
		NORTH_EAST('L'),
		NORTH_WEST('J'),
		SOUTH_EAST('F'),
		SOUTH_WEST('7'),
		EMPTY('.'),
		INSIDE('I')
		;
		private final char t;
		Type(char t) {
			this.t = t;
		}

		public boolean hasNorth() {
			return this == VERTICAL || this == NORTH_EAST || this == NORTH_WEST || this == START;
		}

		public boolean hasSouth() {
			return this == VERTICAL || this == SOUTH_EAST || this == SOUTH_WEST || this == START;
		}

		public boolean hasEast() {
			return this == HORIZONTAL || this == NORTH_EAST || this == SOUTH_EAST || this == START;
		}

		public boolean hasWest() {
			return this == HORIZONTAL || this == NORTH_WEST || this == SOUTH_WEST || this == START;
		}

		static Type of(char t) {
			for(Type type : values()) {
				if(type.t == t) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unknown type: " + t);
		}
	}
}
