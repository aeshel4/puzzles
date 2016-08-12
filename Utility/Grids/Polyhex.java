package Utility.Grids;

import java.util.Iterator;
import java.util.Arrays;

public class Polyhex extends Polyform
{
	public static void main(String[] args)
	{
		HexGrid2DPoint[] points = new HexGrid2DPoint[6];
		int p = 0;
		points[p++] = new HexGrid2DPoint(0, 1);
		points[p++] = new HexGrid2DPoint(1, 1);
		points[p++] = new HexGrid2DPoint(2, 2);
		points[p++] = new HexGrid2DPoint(2, 3);
		points[p++] = new HexGrid2DPoint(3, 1);
		points[p++] = new HexGrid2DPoint(4, 1);
/*
		points[p++] = new HexGrid2DPoint(0, 1);
		points[p++] = new HexGrid2DPoint(0, 2);
		points[p++] = new HexGrid2DPoint(1, 1);
		points[p++] = new HexGrid2DPoint(1, 3);
		points[p++] = new HexGrid2DPoint(2, 1);
		points[p++] = new HexGrid2DPoint(2, 2);
*/
		String[] values = {
			"A",
			"B",
			"F",
			"C",
			"E",
			"D",
		};

		Polyhex polyhex = new Polyhex(points, values, false);
		System.out.println("###");
		System.out.println(polyhex);
		System.out.println("###");
		polyhex.mirror(0);
		System.out.println(polyhex);
		System.out.println("###");
		polyhex.mirror(0);
		System.out.println(polyhex);
		System.out.println("###");

/*
		for (int i = 0; i < polyhex.numRotations(); ++i)
		{
			System.out.println(polyhex);
			System.out.println();
			polyhex.rotate();
		}
		polyhex.mirror(0);
		for (int i = 0; i < polyhex.numRotations(); ++i)
		{
			System.out.println(polyhex);
			System.out.println();
			polyhex.rotate();
		}*/
	}

	private boolean north_south;
	private HexGrid2DPoint center;
	private GridPoint[] centerRing;

	public Polyhex(HexGrid2DPoint[] points, boolean north_south)
	{
		super(TessalationType.HEXAGONAL, 2);
		this.north_south = north_south;
		initialize(points);
	}

	public Polyhex(HexGrid2DPoint[] points, Object[] values, boolean north_south)
	{
		super(TessalationType.HEXAGONAL, 2);
		this.north_south = north_south;
		initialize(points, values);
	}

	private void initialize(HexGrid2DPoint[] pts)
	{
		String[] values = new String[pts.length];
		Arrays.fill(values, "*");
		initialize(pts, values);
	}

	private void initialize(HexGrid2DPoint[] pts, Object[] values)
	{
		if (values.length != pts.length)
		{
			throw new RuntimeException("Values passed isn't the same length (" + values.length + ") as points passed (" + pts.length + ")");
		}

		if (pts.length == 0)
		{
			grid = null;
			center = null;
			centerRing = null;
		}
		else
		{
			int avgLine = 0;
			int avgCell = 0;

			for (HexGrid2DPoint p : pts)
			{
				avgLine += p.getLine();
				avgCell += p.getCell();
			}

			avgLine += pts.length - 1;
			avgCell += pts.length - 1;

			avgLine /= pts.length;
			avgCell /= pts.length;

			HexGrid2DPoint avgPoint = new HexGrid2DPoint(avgLine, avgCell);

			int furthestAway = Integer.MIN_VALUE;
			for (HexGrid2DPoint p : pts)
			{
				int distance = avgPoint.manhattanDistance(p);
				if (furthestAway < distance)
				{
					furthestAway = distance;
				}
			}

			grid = new HexGrid2D(furthestAway + 1, north_south, null);

			// Since most operations are going to require knowing these, save time and calculate them once.
			center = new HexGrid2DPoint(furthestAway, furthestAway);
			centerRing = center.adjacentPoints(false);
System.out.println(furthestAway + "; " + avgPoint + "; " + center);

			int lShift = avgPoint.getLine() - center.getLine();
			int cShift = avgPoint.getCell() - center.getCell();
			for (int p = 0; p < pts.length; ++p)
			{
				HexGrid2DPoint point = new HexGrid2DPoint(pts[p].getLine() - lShift, pts[p].getCell() - cShift);
				grid.set(point, values[p]);
			}
		}
	}

	public Polyhex clone()
	{
		Polyhex newClone = (Polyhex)super.clone();
		newClone.north_south = this.north_south;
		return newClone;
	}

	//AE could also implement mirroring over diagonals, if so, change numAxes to * 2
	public void mirror(int axis)
	{
		if (axis == 0)
		{
			mirrorOverMain();
			return;
		}

		HexGrid2D hGrid = (HexGrid2D)grid;
		HexGrid2D oldGrid = hGrid.clone();

		GridPoint[] origPoints = getAsPointsWhileClearing();

		for (GridPoint gridPoint : origPoints)
		{
			HexGrid2DPoint curPoint = (HexGrid2DPoint)gridPoint;
			Object value = oldGrid.get(curPoint);

			int origL = curPoint.getLine();
			int origC = curPoint.getCell();

			int newL = 0; //AE
			int newC = 0; //AE

			grid.set(new HexGrid2DPoint(newL, newC), value);
		}
	}

	private void mirrorOverMain()
	{
		HexGrid2D hGrid = (HexGrid2D)grid;
		HexGrid2D oldGrid = hGrid.clone();

		GridPoint[] origPoints = getAsPointsWhileClearing();

		int highestLine = Integer.MIN_VALUE;
		for (GridPoint gridPoint : origPoints)
		{
			HexGrid2DPoint curPoint = (HexGrid2DPoint)gridPoint;
			int l = curPoint.getLine();
			if (l > highestLine)
			{
				highestLine = l;
			}
		}

		if (highestLine % 2 == 1)
		{
			highestLine += 1;
		}

		for (GridPoint gridPoint : origPoints)
		{
			HexGrid2DPoint curPoint = (HexGrid2DPoint)gridPoint;
			Object value = oldGrid.get(curPoint);
			int origL = curPoint.getLine();
			int newL = 2 * center.getLine() - origL;
			grid.set(new HexGrid2DPoint(newL, curPoint.getCell()), value);
		}
	}

	public int numAxes()
	{
		return 3;
	}

/*
LINES
  0 0 0
 1 1 1 1
2 2 2 2 2
 3 3 3 3
  4 4 4

CELLS
  1 2 3
 0 1 2 3
0 1 2 3 4
 0 1 2 3
  1 2 3


LINES
   0 0 0 0
  1 1 1 1 1
 2 2 2 2 2 2
3 3 3 3 3 3 3
 4 4 4 4 4 4
  5 5 5 5 5
   6 6 6 6

CELLS
  1 2 3
 0 1 2 3
0 1 2 3 4
 0 1 2 3
  1 2 3


  B C D
 A     E

    A B
       C
        D
       E

       A
        B
       C
    E D

   A B * @
  * @ C @ *
 * * @ D * *
@ @ @ E @ @ @
 * * @ @ * *
  * @ * @ *
   @ * * @

   @ * * A
  * @ * @ B
 * * @ @ C *
@ @ @ E D @ @
 * * @ @ * *
  * @ * @ *
   @ * * @


// Equivalent to HexGrid2DPoint adjacencies
    1 1 1 1 2
   0 1 1 1 2 2
  0 0 1 1 2 2 2
 0 0 0 1 2 2 2 2
0 0 0 0 6 3 3 3 3
 5 5 5 5 4 3 3 3
  5 5 5 4 4 3 3
   5 5 4 4 4 3
    5 4 4 4 4
*/
	private int getSector(HexGrid2DPoint point)
	{
		if (point.equals(center))
		{
			return 6;
		}

		int minManhattanDistance = Integer.MAX_VALUE;
		int numMinRepeated = 0;
		int[] manhattanDistances = new int[centerRing.length];
		for (int i = 0; i < centerRing.length; ++i)
		{
			manhattanDistances[i] = point.manhattanDistance(centerRing[i]);
			if (manhattanDistances[i] < minManhattanDistance)
			{
				minManhattanDistance = manhattanDistances[i];
				numMinRepeated = 1;
			}
			else if (manhattanDistances[i] == minManhattanDistance)
			{
				++numMinRepeated;
			}
		}

		if (numMinRepeated == 1)
		{
			for (int i = 0; i < manhattanDistances.length; ++i)
			{
				if (manhattanDistances[i] == minManhattanDistance)
				{
					return i;
				}
			}
			throw new RuntimeException("Couldn't find the manhattan distance we just specified!");
		}
		else if (numMinRepeated == 2)
		{
			int minIndex = -1;
			for (int i = 0; i < manhattanDistances.length; ++i)
			{
				if (manhattanDistances[i] == minManhattanDistance)
				{
					if (minIndex == -1)
					{
						minIndex = i;
					}
					else
					{
						if (minIndex == ((6 + i - 1) % 6))
						{
							return minIndex;
						}
						else if (i == ((6 + minIndex - 1) % 6))
						{
							return i;
						}
						else
						{
							throw new RuntimeException("The two indices of minManhattanDistance are not one apart! " + minIndex + " and " + i);
						}
					}
				}
			}
			throw new RuntimeException("Couldn't find the 2 manhattan distances we just specified!");
		}
		else
		{
			throw new RuntimeException("Should not have " + numMinRepeated + " minManhattanDistances repeated.");
		}
	}

	public void rotate()
	{
		HexGrid2D hGrid = (HexGrid2D)grid;
		HexGrid2D oldGrid = hGrid.clone();

		GridPoint[] origPoints = getAsPointsWhileClearing();

		for (GridPoint gridPoint : origPoints)
		{
			HexGrid2DPoint curPoint = (HexGrid2DPoint)gridPoint;
			Object value = oldGrid.get(curPoint);

			int origL = curPoint.getLine();
			int origC = curPoint.getCell();

			int newL = 0; //AE
			int newC = 0; //AE

			grid.set(new HexGrid2DPoint(newL, newC), value);
		}
	}

	public int numRotations()
	{
		//AE This could be optimized for symmetrical polyominoes.
		// If so rotate needs to be updated as well.
		return 6;
	}
}