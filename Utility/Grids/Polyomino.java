package Utility.Grids;

import java.util.Iterator;
import java.util.Arrays;

public class Polyomino extends Polyform
{
	public static void main(String[] args)
	{
		int[][] pts = {
			{0, 2},
			{1, 2},
			{2, 2},
			{2, 3},
		};
		String[] values = {
			"A",
			"B",
			"C",
			"D"
		};
		Polyomino polyomino = new Polyomino(pts, values);

		for (int i = 0; i < polyomino.numRotations(); ++i)
		{
			System.out.println(polyomino);
			System.out.println();
			polyomino.rotate();
		}
		polyomino.mirror(1);
		for (int i = 0; i < polyomino.numRotations(); ++i)
		{
			System.out.println(polyomino);
			System.out.println();
			polyomino.rotate();
		}
	}

	public Polyomino(int[][] pts)
	{
		super(TessalationType.RECTANGULAR, 2);
		initialize(pts);
	}

	public Polyomino(NDimensionalPoint[] points)
	{
		super(TessalationType.RECTANGULAR, 2);
		int[][] pts = convertNDimPtsToIntArray(points);
		initialize(pts);
	}

	public Polyomino(int[][] pts, Object[] values)
	{
		super(TessalationType.RECTANGULAR, 2);
		initialize(pts, values);
	}

	public Polyomino(NDimensionalPoint[] points, Object[] values)
	{
		super(TessalationType.RECTANGULAR, 2);
		int[][] pts = convertNDimPtsToIntArray(points);
		initialize(pts, values);
	}

	private static int[][] convertNDimPtsToIntArray(NDimensionalPoint[] points)
	{
		int[][] pts = new int[points.length][2];
		for (int p = 0; p < points.length; ++p)
		{
			for (int d = 0; d < pts[p].length; ++d)
			{
				pts[p][d] = points[p].get(d);
			}
		}
		return pts;
	}

	private void initialize(int[][] pts)
	{
		String[] values = new String[pts.length];
		Arrays.fill(values, "*");
		initialize(pts, values);
	}

	private void initialize(int[][] pts, Object[] values)
	{
		if (values.length != pts.length)
		{
			throw new RuntimeException("Values passed isn't the same length (" + values.length + ") as points passed (" + pts.length + ")");
		}

		int[] min = {Integer.MAX_VALUE, Integer.MAX_VALUE};
		int[] max = {Integer.MIN_VALUE, Integer.MIN_VALUE};

		for (int p = 0; p < pts.length; ++p)
		{
			if (pts[p].length != 2)
			{
				throw new RuntimeException("Trying to create Polyomino with non 2-dimensional points");
			}

			for (int d = 0; d < pts[p].length; ++d)
			{
				if (pts[p][d] < min[d])
				{
					min[d] = pts[p][d];
				}
				if (pts[p][d] > max[d])
				{
					max[d] = pts[p][d];
				}
			}
		}

		if (pts.length == 0)
		{
			grid = null;
		}
		else
		{
			int[] size = new int[min.length];
			for (int i = 0; i < size.length; ++i)
			{
				size[i] = max[i] - min[i] + 1;
			}

			int maxDimIndex = Utility.Math.Math.MaxIndex(size);
			Arrays.fill(size, size[maxDimIndex]);
			grid = new NDimensionalGrid(size, null);

			for (int p = 0; p < pts.length; ++p)
			{
				for (int d = 0; d < size.length; ++d)
				{
					pts[p][d] -= min[d];
				}
				grid.set(new NDimensionalPoint(pts[p]), values[p]);
			}
		}
	}

	public Polyomino clone()
	{
		Polyomino newClone = (Polyomino)super.clone();
		return newClone;
	}

	//AE could also implement mirroring over diagonals, if so, change numAxes to * 2
	public void mirror(int axis)
	{
		NDimensionalGrid ndGrid = (NDimensionalGrid)grid;
		NDimensionalGrid oldGrid = ndGrid.clone();
		int dimensionToMirror = axis;

		GridPoint[] origPoints = getAsPointsWhileClearing();
		int dimensionToMirrorSize = ndGrid.size(new NDimensionalPoint(2), dimensionToMirror);
		for (int p = 0; p < origPoints.length; ++p)
		{
			NDimensionalPoint curPoint = (NDimensionalPoint)origPoints[p];
			Object value = oldGrid.get(curPoint);
			int originalValue = curPoint.get(dimensionToMirror);
			curPoint.set(dimensionToMirror, dimensionToMirrorSize - 1 - originalValue);
			grid.set(curPoint, value);
		}
	}

	public int numAxes()
	{
		return super.dimension;
	}

	public void rotate()
	{
		NDimensionalGrid ndGrid = (NDimensionalGrid)grid;
		NDimensionalGrid oldGrid = ndGrid.clone();

		GridPoint[] origPoints = getAsPointsWhileClearing();
		int xSize = ndGrid.size(new NDimensionalPoint(2), 0);
		int ySize = ndGrid.size(new NDimensionalPoint(2), 1);
		for (int p = 0; p < origPoints.length; ++p)
		{
			NDimensionalPoint curPoint = (NDimensionalPoint)origPoints[p];
			Object value = oldGrid.get(curPoint);
			int origX = curPoint.get(0);
			int origY = curPoint.get(1);
			curPoint.set(0, origY);
			curPoint.set(1, xSize - 1 - origX);
			grid.set(curPoint, value);
		}
	}

	public int numRotations()
	{
		//AE This could be optimized for symmetrical polyominoes.
		// If so rotate needs to be updated as well.
		return 4;
	}
}