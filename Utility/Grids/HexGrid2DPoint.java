package Utility.Grids;

import java.util.ArrayList;
import java.util.Scanner;

// Nearly everything in this class is package-private, as it is meant to be opaque
// to everything but HexGrid. The class itself is public so that users of HexGrid2D
// can pass it around.
public class HexGrid2DPoint extends GridPoint
{
	private int line;
	private int cell;

	public static void main(String[] args)
	{
/*
		HexGrid2DPoint[] line = new HexGrid2DPoint[2];
		line[0] = new HexGrid2DPoint(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		line[1] = new HexGrid2DPoint(2, 2);

		GridPoint[] adjPoints = line[0].adjacentPoints(false);
		for (int i = 0; i < adjPoints.length; ++i)
		{
			GridPoint[] secondLeveladjPoints = adjPoints[i].adjacentPoints(false);
			for (int j = 0; j < secondLeveladjPoints.length; ++j)
			{
//				System.out.println(secondLeveladjPoints[j] + ": " + line[0].manhattanDistance(secondLeveladjPoints[j]) + " " + secondLeveladjPoints[j].manhattanDistance(line[0]));
			}
//			System.out.println();
		}*/
		ArrayList<HexGrid2DPoint> forLine = new ArrayList<HexGrid2DPoint>();
		forLine.add(new HexGrid2DPoint(2, 2));
		forLine.add(new HexGrid2DPoint(3, 2));
//		forLine.add(new HexGrid2DPoint(2, 4));
		while (forLine.size() < 10)
		{
			GridPoint[] continued = forLine.get(0).nextPointOnLine(forLine.toArray(new HexGrid2DPoint[0]));
//			for (int i = 0; i < continued.length; ++i)
			{
				System.out.println(continued[0] + " " + forLine.get(0).manhattanDistance(continued[0]));
			}
			forLine.add((HexGrid2DPoint)continued[0]);
		}
	}

	public HexGrid2DPoint(int line, int cell)
	{
		super(TessalationType.HEXAGONAL, 2);
		setCell(cell);
		setLine(line);
	}

	private boolean parity() // when calculating adjacent, true goes positive, false goes negative
	{
		return getParity(line);
	}

	private static boolean getParity(int line)
	{
		return ((line % 2) != 0);
	}

	public int getLine()
	{
		return line;
	}

	private void setLine(int line)
	{
		this.line = line;
	}

	public int getCell()
	{
		return cell;
	}

	private void setCell(int cell)
	{
		this.cell = cell;
	}	

	public String toString()
	{
		return "[Line:" + line + " #" + cell + "]";
	}

	public GridPoint clone()
	{
		return new HexGrid2DPoint(line, cell);
	}

	public boolean equals(Object t)
	{
		HexGrid2DPoint that = (HexGrid2DPoint)t;
		return (this.line == that.line) && (this.cell == that.cell);
	}

	public int hashCode()
	{
		return 100000 * line + cell;
	}

	public boolean isAdjacent(GridPoint t, boolean cornersCount)
	{
		HexGrid2DPoint that = (HexGrid2DPoint)t;
		// cornersCount is meaningless for hex points
		int diffLine = this.line - that.line;
		int diffCell = this.cell - that.cell;
		if (diffLine == 0)
		{
			return ((diffCell == 1) || (diffCell == -1));
		}
		else if ((diffLine == -1) || (diffLine == 1))
		{
			if (diffCell == 0)
			{
				return true;
			}

			if (parity())
			{
				return (diffCell == 1);
			}
			else
			{
				return (diffCell == -1);
			}
		}
		else
		{
			return false;
		}
	}

	public GridPoint[] adjacentPoints(boolean cornersCount)
	{
		HexGrid2DPoint[] adjPoints = new HexGrid2DPoint[6];
		for (int i = 0; i < adjPoints.length; ++i)
		{
			adjPoints[i] = getAdjacency(i);
		}
		return adjPoints;
	}

	//  1 2
    // 0 * 3
    //  5 4
	private int getDirection(HexGrid2DPoint adj)
	{
		// Simple, most often used case, points are literally adjacent.
		for (int i = 0; i < 6; ++i)
		{
			if (adj.equals(this.getAdjacency(i)))
			{
				return i;
			}
		}

		if (!this.orthogonalLinePossible(adj))
		{
			// This doesn't make sense since there could be multiple directions for which direction the new point may lie on.
			//  1 2
		    // 0 * 3
		    //  5 4
			// As an example, 0 to 2 above could be either the * location (i.e. 3) or the 1 location.
			throw new RuntimeException(this + " is not orthogonal to " + adj + " meaning getAdjacency would be meaningless.");
		}

		int minManhattanDistanceOnOrthogonal = Integer.MAX_VALUE;
		int ret = -1;
		for (int i = 0; i < 6; ++i)
		{
			HexGrid2DPoint adjPoint = this.getAdjacency(i);
			if (adj.orthogonalLinePossible(adjPoint))
			{
				int manDist = adj.manhattanDistance(adjPoint);
				if (manDist < minManhattanDistanceOnOrthogonal)
				{
					minManhattanDistanceOnOrthogonal = manDist;
					ret = i;
				}
			}
		}

		if (ret == -1)
		{
			throw new RuntimeException("Didn't think this could happen.");
		}
		else
		{
			return ret;
		}
	}

	//  1 2
    // 0 * 3
    //  5 4
	private HexGrid2DPoint getAdjacency(int adj)
	{
		int shift = parity() ? 1 : -1;
		int shift1 = Math.min(0, shift);
		int shift2 = Math.max(0, shift);

		switch (adj)
		{
			case 0: return new HexGrid2DPoint(line,     cell - 1);
			case 1: return new HexGrid2DPoint(line - 1, cell + shift1);
			case 2: return new HexGrid2DPoint(line - 1, cell + shift2);
			case 3: return new HexGrid2DPoint(line,     cell + 1);
			case 4: return new HexGrid2DPoint(line + 1, cell + shift2);
			case 5: return new HexGrid2DPoint(line + 1, cell + shift1);
			default: throw new RuntimeException("Trying to get adjacency > 5");
		}
	}

	public static boolean lineIsStraight(HexGrid2DPoint p0, HexGrid2DPoint p1, HexGrid2DPoint p2)
	{
		// The same relationship between p1/p0 as p2/p1 means the line is straight.
		return p1.getDirection(p0) == p2.getDirection(p1);
	}

	private static HexGrid2DPoint[] getAll3NextPoints(HexGrid2DPoint p1, HexGrid2DPoint p2)
	{
		HexGrid2DPoint[] pts = new HexGrid2DPoint[3];
		int curAdj = p2.getDirection(p1);
		for (int i = 0; i < pts.length; ++i)
		{
			// Lines are continued to all 3 points next to the center (p2) that are
			// NOT one adjacent to the last (p1). So add 2 and go around the circle
			pts[i] = p2.getAdjacency((curAdj + 2 + i) % 6);
		}
		return pts;
	}

	private static HexGrid2DPoint getNextPoint(HexGrid2DPoint p0, HexGrid2DPoint p1, HexGrid2DPoint p2)
	{
		// p3 (the next point) has the same relationship to p2 as p1 does to p0
		int adj = p0.getDirection(p1);
		return p2.getAdjacency(adj);
	}

	public GridPoint[] nextPointOnLine(GridPoint[] line)
	{
		return nextPointOnLineStatic(line);
	}

	private static HexGrid2DPoint[] nextPointOnLineStatic(GridPoint[] line)
	{
		if (line.length < 2)
		{
			return null;
		}

		HexGrid2DPoint p1 = (HexGrid2DPoint)line[line.length - 2];
		HexGrid2DPoint p2 = (HexGrid2DPoint)line[line.length - 1];

		if (line.length == 2)
		{
			return getAll3NextPoints(p1, p2);
		}
		else
		{
			HexGrid2DPoint p0 = (HexGrid2DPoint)line[line.length - 3];
			HexGrid2DPoint[] asArray = new HexGrid2DPoint[1];
			asArray[0] = getNextPoint(p0, p1, p2);
			return asArray;
		}
	}

	public int compareTo(GridPoint t)
	{
		HexGrid2DPoint that = (HexGrid2DPoint)t;
		if (this.line == that.line)
		{
			return this.cell - that.cell;
		}
		else
		{
			return this.line - that.line;
		}
	}

	public int manhattanDistance(GridPoint t)
	{
		HexGrid2DPoint that = (HexGrid2DPoint)t;
		int lMin, lMax, cMin, cMax;
		if (this.line < that.line)
		{
			lMin = this.line;
			cMin = this.cell;
			lMax = that.line;
			cMax = that.cell;
		}
		else
		{
			lMin = that.line;
			cMin = that.cell;
			lMax = this.line;
			cMax = this.cell;
		}

		int lineDiff = lMax - lMin;
		while (lMin != lMax)
		{
			if (!getParity(lMax) && (cMax > cMin))
			{
				--cMax;
			}
			else if (getParity(lMax) && (cMax < cMin))
			{
				++cMax;
			}
			--lMax;
		}

		return lineDiff + Math.abs(cMax - cMin);
	}

	public boolean orthogonalLinePossible(GridPoint t)
	{
		HexGrid2DPoint that = (HexGrid2DPoint)t;
		if (this.line == that.line)
		{
			return true;
		}
		else
		{
			// Could write new code to do this 'faster' but
			// reusing manhattanDistance is cleaner and simpler
			return Math.abs(this.line - that.line) == this.manhattanDistance(that);
		}
	}

	private int promptNumber(Scanner input, String strToDisplay)
	{
		System.out.print(strToDisplay + ": ");
		do
		{
			try
			{
				return Integer.parseInt(input.nextLine());
			}
			catch (NumberFormatException e)
			{
				System.out.print("Invalid choice, please try again: ");
			}
		} while (true);
	}

	// This would be static if it could be
	public GridPoint promptForPoint(Scanner input)
	{
		int promptLine = promptNumber(input, "Which line");
		int promptCell = promptNumber(input, "Which cell");;
		return new HexGrid2DPoint(promptLine, promptCell);
	}
}