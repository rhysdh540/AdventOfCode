import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* <a href="https://adventofcode.com/2023/day/10">Day 10</a>
*/
public class Day10 extends Day.IntDay {
	@Override
	public int run1Int(List<String> input) {
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

		int x = start.x, y = start.y, prevX = x, prevY = y, length = 0;

		grid[y][x] = start = determineType(start, grid);
		// follow the path
		while(true) {
			PipePart part = grid[y][x];
			if(part.type == Type.EMPTY ||(part.equals(start) && length > 0)) {
				break;
			}
			length++;
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

		return length / 2;
	}

	@Override
	public int run2Int(List<String> input) {
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
		int x = start.x, y = start.y, prevX = x, prevY = y, length = 0;

		List<PipePart> loop = new ArrayList<>();
		loop.add(start);

		grid[y][x] = start = determineType(start, grid);

		// follow the path
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
				if(part.type == Type.EMPTY) {
					if(!hasPathToEdge(grid, part)) {
						grid[part.y][part.x] = new PipePart(Type.INSIDE, part.x, part.y);
						count++;
					}
				}
			}
		}

		for(PipePart[] row : grid) {
			System.out.println(Arrays.stream(row).map(it -> it.type.t + "").collect(Collectors.joining()));
		}

		return count;
	}

	private boolean hasPathToEdge(PipePart[][] grid, PipePart part) {
		List<Type> vertical = List.of(Type.VERTICAL, Type.NORTH_EAST, Type.NORTH_WEST);
		int count = 0;
		for(int x = part.x; x < grid[part.y].length; x++) {
			PipePart other = grid[part.y][x];
			if(vertical.contains(other.type)) {
				count++;
			}
		}
		return count % 2 == 0;
	}

	private PipePart determineType(PipePart start, PipePart[][] grid) {
		List<PipePart> adjacent = start.findAdjacent(grid);
		boolean north = false, south = false, east = false, west = false;
		for(PipePart adj : adjacent) {
			if(adj.y < start.y) {
				north = true;
			}
			if(adj.y > start.y) {
				south = true;
			}
			if(adj.x < start.x) {
				west = true;
			}
			if(adj.x > start.x) {
				east = true;
			}
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
