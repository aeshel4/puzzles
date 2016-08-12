package Utility.Grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

import java.io.*;

public class HexGrid2D extends GridTessalation
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(args[0]));
		String[] delims = new String[2];
		delims[0] = "\n";
		delims[1] = " ";
		HexGrid2D g = new HexGrid2D(sc, delims, false, ".");
//System.out.println(g.hexShaped + " " + g.north_south + " " + g.topLeftIsCorner + " " + g.grid.length  + " " + g.grid[0].length);
		System.out.println(g);
	}

	private static void runLine(HexGrid2D g, ArrayList<GridPoint> line, GridPoint nextPoint)
	{
		g.set(nextPoint, String.valueOf(line.get(0).manhattanDistance(nextPoint)));
		line.add(nextPoint);

		GridPoint[] nextInLine = nextPoint.nextPointOnLine(line.toArray(new HexGrid2DPoint[0]));
		for (int i = 0; i < nextInLine.length; ++i)
		{
			if (g.contains(nextInLine[i]))
			{
				runLine(g, line, nextInLine[i]);
			}
		}

		line.remove(nextPoint);
	}

	public static void main2(String[] args)
	{
		int SIZE = Integer.parseInt(args[0]);
		HexGrid2D g = new HexGrid2D(SIZE, false, "*");
		// HexGrid2DPoint p = new HexGrid2DPoint(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		HexGrid2DPoint p = new HexGrid2DPoint(SIZE-1, SIZE-1);
		GridPoint[] adjs = p.adjacentPoints(false);
		g.set(p, "#");
		ArrayList<GridPoint> line = new ArrayList<GridPoint>();
		line.add(p);
		for (int a = 0; a < adjs.length; ++a)
		{
		// if (a != 1) continue;
		// runLine(g, line, adjs[a]);
		// g.set(adjs[a], String.valueOf('.'));
		// line.add(adjs[a]);
		}

		for (int i = 0; i < g.grid.length; ++i)
		{
			// System.out.println(g.grid[i].length + " " + g.numBlankObjectsAtStartOfLine(i));
		}
		// HexGrid2D g = new HexGrid2D(Integer.parseInt(args[0]), true, "*");
		Iterator<GridPoint> iter = g.iterator();
		char ch = 'A';
		while (iter.hasNext())
		{
			// g.set(iter.next(), ch++);
			GridPoint pt = iter.next();
			g.set(pt, p.manhattanDistance(pt));
		}
		System.out.println(g);
/*		for (int a = 0; a < g.grid.length; ++a)
		{
			for (int b = 0; b < g.grid.length; ++b)
			{
				HexGrid2DPoint p = new HexGrid2DPoint(a, b);
				if (g.contains(p))
				{
					System.out.print("#");
				}
				else
				{
					System.out.print(".");
				}
			}
			System.out.println();
		}*/
	}

	private Object[][] grid;
	private ArrayList<HexGrid2DPoint> nonPoints;

	// See the various toString* for what these look like
	private boolean hexShaped; // otherwise rectangle-ish shaped
	private boolean north_south; // if true cells can form a straight line north/south
	// topLeftIsCorner doesn't matter if hexShaped
	// topLeftIsCorner doesn't matter if ~half of 'rows' are longer than other ~half by 1
	//                                //AE the above line is not currently supported anyway
	private boolean topLeftIsCorner;

	// Hex-shaped grid
	public HexGrid2D(int n, boolean north_south, Object defaultValue)
	{
		super(TessalationType.HEXAGONAL, 2);
		this.nonPoints = new ArrayList<HexGrid2DPoint>();
		this.hexShaped = true;
		this.topLeftIsCorner = true; // To stop things from skipping some
		this.north_south = north_south;

		int m = 2 * n - 1; // this is the longest line
		grid = new Object[m][];
		int usableLen = n;
		int change = 1;
		for (int l = 0; l < grid.length; ++l)
		{
			int blankLen = numBlankObjectsAtStartOfLine(l);
			grid[l] = new Object[blankLen + usableLen];

			int c;
			for (c = 0; c < blankLen; ++c)
			{
				nonPoints.add(new HexGrid2DPoint(l, c));
			}

			for (; c < grid[l].length; ++c)
			{
				grid[l][c] = defaultValue;
			}

			usableLen += change;
			if (usableLen == m)
			{
				change *= -1;
			}
		}
	}

	// Rectangular grid
	public HexGrid2D(int x, int y, boolean north_south, boolean topLeftIsCorner, Object defaultValue)
	{
		super(TessalationType.HEXAGONAL, 2);
		this.nonPoints = new ArrayList<HexGrid2DPoint>();
		this.hexShaped = false;
		this.north_south = north_south;
		int line, cell;
		if (north_south)
		{
			line = x;
			cell = y;
		}
		else
		{
			line = y;
			cell = x;
		}
		this.topLeftIsCorner = topLeftIsCorner;

		grid = new Object[line][];
		for (int l = 0; l < grid.length; ++l)
		{
			int addTo = (topLeftIsCorner || (l % 2 != 0)) ? 0 : 1;
			grid[l] = new Object[cell + addTo];

			int c;
			for (c = 0; c < addTo; ++c)
			{
				nonPoints.add(new HexGrid2DPoint(l, c));
			}

			for (; c < grid[l].length; ++c)
			{
				grid[l][c] = defaultValue;
			}
		}
	}

	// Rectangular grid with given elements
	public HexGrid2D(Object[][] objs, boolean north_south, boolean topLeftIsCorner)
	{
		super(TessalationType.HEXAGONAL, 2);
		this.nonPoints = new ArrayList<HexGrid2DPoint>();
		this.hexShaped = false;
		this.north_south = north_south;
		this.topLeftIsCorner = topLeftIsCorner;

		int line, cell;
		if (north_south)
		{
			line = objs.length;
			cell = objs[0].length;
		}
		else
		{
			line = objs[0].length;
			cell = objs.length;
		}

		grid = new Object[line][];
		for (int l = 0; l < grid.length; ++l)
		{
			int addTo = (topLeftIsCorner || (l % 2 != 0)) ? 0 : 1;
			grid[l] = new Object[cell + addTo];

			int c;
			for (c = 0; c < addTo; ++c)
			{
				nonPoints.add(new HexGrid2DPoint(l, c));
			}

			for (; c < grid[l].length; ++c)
			{
				int x, y;
				if (north_south)
				{
					x = c - addTo;
					y = l;
				}
				else
				{
					x = l;
					y = c - addTo;
				}
				grid[l][c] = objs[x][y];
			}
		}
	}

	public HexGrid2D(Scanner fileScanner, String[] delimiters, boolean north_south, String skipChar)
	{
		super(TessalationType.HEXAGONAL, 2);
		String fileContents = "";
		while (fileScanner.hasNextLine())
		{
			fileContents += fileScanner.nextLine() + "\n";
		}
		initializeWithFileText(fileContents, delimiters, north_south, skipChar);
	}

	//AE Can't currently initialize:
	//      north_south && !topLeftIsCorner
	//      north_south && hexShaped
	private void initializeWithFileText(String fileText, String[] delimiters, boolean north_south, String skipChar)
	{
		if (delimiters.length != 2)
		{
			throw new RuntimeException("Trying to construct a 2D hex grid with the wrong number of delimiters.");
		}

		String[][] strGrid;

		String[] rows = fileText.split(delimiters[0]);
		strGrid = new String[rows.length][];
		for (int r = 0; r < rows.length; ++r)
		{
			String[] cols = rows[r].split(delimiters[1]);
			int start = cols[0].isEmpty() ? 1 : 0;
			strGrid[r] = new String[cols.length - start];
			for (int c = start; c < cols.length; ++c)
			{
				if (cols[c].equals(skipChar))
				{
					strGrid[r][c - start] = null;
				}
				else
				{
					strGrid[r][c - start] = cols[c];
				}
			}
		}

		this.nonPoints = new ArrayList<HexGrid2DPoint>();
		this.north_south = north_south;
		this.hexShaped = strGrid[0][1] == null;
		this.topLeftIsCorner = hexShaped || (strGrid[0][0] != null);

		if (north_south)
		{
			if (hexShaped)
			{
				throw new UnsupportedOperationException("Unfortunately can't handle a north_south hexShaped grid right now.");
			}
			else if (!topLeftIsCorner)
			{
				throw new UnsupportedOperationException("Unfortunately can't handle a north_south !topLeftIsCorner grid right now.");
			}
		}

		if (north_south)
		{
			grid = Utility.Math.Math.Transpose(strGrid);
		}
		else
		{
			grid = strGrid;
		}

		// Since it's not possible to stick nulls in file text, it's safe to consider those for nonPoints
		for (int l = 0; l < grid.length; ++l)
		{
			for (int c = 0; c < grid[l].length; ++c)
			{
				if (grid[l][c] == null)
				{
					nonPoints.add(new HexGrid2DPoint(l, c));
				}
			}
		}
	}

//	private void initializeWithObjectArray(Object[][] objs, boolean north_south, boolean topLeftIsCorner) //AE

	public HexGrid2D clone()
	{
		HexGrid2D newClone = (HexGrid2D)super.clone();
		newClone.hexShaped = this.hexShaped;
		newClone.north_south = this.north_south;
		newClone.topLeftIsCorner = this.topLeftIsCorner;
		newClone.grid = new Object[this.grid.length][];
		for (int i = 0; i < grid.length; ++i)
		{
			newClone.grid[i] = Arrays.copyOf(this.grid[i], this.grid[i].length);
		}
		newClone.nonPoints = new ArrayList<HexGrid2DPoint>(this.nonPoints);

		return newClone;
	}

	// (if hexShaped && !north_south)
	//    * * * *
	//   * * * * *
	//  * * * * * *
	// * * * * * * *
	//  * * * * * *
	//   * * * * *
	//    * * * *
	private String toStringEastWestHex(GridPrinter printer)
	{
		String str = "";
		for (int l = 0; l < grid.length; ++l)
		{
			int blankObjects = numBlankObjectsAtStartOfLine(l);
			str += Utility.Utility.Spaces(grid.length - (grid[l].length - blankObjects));
			for (int c = blankObjects; c < grid[l].length; ++c)
			{
				str += printer.value(this, new HexGrid2DPoint(l, c));
				if (c != grid[l].length - 1)
				{
					str += ' ';
				}
			}
			str += '\n';
		}
		return str;
	}

	// (if hexShaped && north_south)
	//           *
	//        *     *
	//     *     *     *
	//  *     *     *     *
	//     *     *     *
	//  *     *     *     *
	//     *     *     *
	//  *     *     *     *
	//     *     *     *
	//  *     *     *     *
	//     *     *     *
	//        *     *
	//           *
	//
	private String toStringNorthSouthHex(GridPrinter printer)
	{
		String str = "";
		int m = grid.length; // corresponds to number of elements in longest line (and therefore halfway point)
		int n = (m + 1) / 2; // number of elements in a side
		int totalRows = 2 * m - 1;
		for (int row = 0; row < totalRows; ++row)
		{
			int diffFromCenter = Math.min(row, totalRows - row - 1);
			int firstCol = n - 1 - diffFromCenter;
			int lastCol = n - 1 + diffFromCenter;
			for (int col = 0; col < grid.length; ++col)
			{
				if (col % 2 == (row + n) % 2)
				{
					str += ' ';
				}
				else if (col < firstCol || col > lastCol)
				{
					str += ' ';
				}
				else
				{
					// distFromCenter also corresponds to row of first element
					int distFromCenter = (n - 1) - Math.min(col, 2 * (n - 1) - col);
					int cell = (row - distFromCenter) / 2 + numBlankObjectsAtStartOfLine(col);
					str += printer.value(this, new HexGrid2DPoint(col, cell));
				}
				str += "  ";
			}
			str += '\n';
		}
		return str;
	}


	// (if !north_south && topLeftIsCorner)
	// * * * *
	//  * * * *
	// * * * *
	//  * * * *
	//
	// (if !north_south && !topLeftIsCorner)
	//  * * * *
	// * * * *
	//  * * * *
	// * * * *
	private String toStringEastWestRect(GridPrinter printer)
	{
		String str = "";
		for (int l = 0; l < grid.length; ++l)
		{
			// int c = 0;
			// if (topLeftIsCorner ^ (l % 2 != 0))
			// {
			// 	str += ' ';
			// }
			if (l % 2 == 1)
			{
				str += ' ';
			}
			for (int c = 0; c < grid[l].length; ++c)
			{
				if (nonPoints.contains(new HexGrid2DPoint(l, c)))
				{
					str += ' ';
				}
				else
				{
					str += printer.value(this, new HexGrid2DPoint(l, c));
				}

				if (c != grid[l].length - 1)
				{
					str += ' ';
				}
			}
			str += '\n';
		}
		return str;
	}

	// (if north_south && topLeftIsCorner)
	// *     *     *     *   
	//    *     *     *     *
	// *     *     *     *   
	//    *     *     *     *
	// *     *     *     *   
	//    *     *     *     *
	// *     *     *     *   
	//    *     *     *     *
	private String toStringNorthSouthRect(GridPrinter printer)
	{
		String str = "";
		int totalRows = (grid[0].length - (topLeftIsCorner ? 0 : 1)) * 2;
		for (int row = 0; row < totalRows; ++row)
		{
			for (int col = 0; col < grid.length; ++col)
			{
				if ((col % 2 == row % 2) ^ topLeftIsCorner)
				{
					str += ' ';
				}
				else
				{
					int cell = (row + (!topLeftIsCorner ? 1 : 0)) / 2;
					str += printer.value(this, new HexGrid2DPoint(col, cell));
				}
				str += "  ";
			}
			str += '\n';
		}
		return str;
	}

	public String toString(GridPrinter printer)
	{
		if (hexShaped)
		{
			if (north_south)
			{
				return toStringNorthSouthHex(printer);
			}
			else
			{
				return toStringEastWestHex(printer);
			}
		}
		else
		{
			if (north_south)
			{
				return toStringNorthSouthRect(printer);
			}
			else
			{
				return toStringEastWestRect(printer);
			}
		}
	}

	public Object get(GridPoint p)
	{
		HexGrid2DPoint point = (HexGrid2DPoint)p;
		if (this.contains(point))
		{
			return grid[point.getLine()][point.getCell()];
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException(point.toString() + " is not within this grid");
		}
	}

	public void set(GridPoint p, Object o)
	{
		HexGrid2DPoint point = (HexGrid2DPoint)p;
		if (this.contains(point))
		{
			grid[point.getLine()][point.getCell()] = o;
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException(point.toString() + " is not within this grid");
		}
	}

	public boolean contains(GridPoint p)
	{
		HexGrid2DPoint point = (HexGrid2DPoint)p;
		if (nonPoints.contains(point))
		{
			return false;
		}
		else
		{
			int l = point.getLine();
			int c = point.getCell();
			if ((l < 0) || (c < 0))
			{
				return false;
			}
			else if (l >= grid.length)
			{
				return false;
			}
			else if (c >= grid[l].length)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}

	public Iterator<GridPoint> iterator()
	{
		return new HexGrid2DIterator(this);
	}

	public void shiftWithin(GridPoint point)
	{
		//AE Could implement this for rectangular versions satisfying certain constraints.
		throw new UnsupportedOperationException("Shifting within is weird for most hex grids.");
	}

	// package private so iterator can use it
	private int numBlankObjectsAtStartOfLine(int l)
	{
		if (!hexShaped)
		{
			throw new UnsupportedOperationException("numBlankObjectsAtStartOfLine is only valid in hex-shaped grids");
		}

		int m = grid.length;
		int n = (m + 1) / 2;
		return (Math.abs(n - l - 1) + (n + 1) % 2) / 2;
	}

	class HexGrid2DIterator implements Iterator<GridPoint>
	{
		private HexGrid2D grid;
		private int curLine;
		private int curCell;

		// Package private so you have to get this from the grid
		HexGrid2DIterator(HexGrid2D grid)
		{
			this.grid = grid;
			curLine = -1;
			curCell = -1;
		}

		private void advanceNextLine()
		{
			++curLine;
			if (grid.hexShaped)
			{
				curCell = numBlankObjectsAtStartOfLine(curLine);
			}
			else
			{
				curCell = (!grid.topLeftIsCorner && (curLine % 2 == 0)) ? 1 : 0;
			}
		}

		private void advanceNextCell()
		{
			if ((curCell == -1) ||
				(++curCell == grid.grid[curLine].length))
			{
				advanceNextLine();
			}
		}

		public boolean hasNext()
		{
			if (curLine < grid.grid.length - 1)
			{
				// Still have another line to go through
				return true;
			}
			else if (curLine == grid.grid.length)
			{
				// Have no more lines
				return false;
			}
			else
			{
				// On the last line, check the cell
				return curCell < (grid.grid[curLine].length - 1);
			}
		}

		public HexGrid2DPoint next()
		{
			// Explicit check because this iterator just blindly advances cells/lines
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}

			advanceNextCell();
			return new HexGrid2DPoint(curLine, curCell);
		}
	}
}