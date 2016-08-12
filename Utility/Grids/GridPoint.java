package Utility.Grids;

import java.util.Scanner;

public abstract class GridPoint implements Cloneable, Comparable<GridPoint>
{
	protected TessalationType type;
	protected int dimension;

	protected GridPoint(TessalationType type, int dimension)
	{
		this.type = type;
		this.dimension = dimension;
	}

	public int dimension()
	{
		return dimension;
	}

	public TessalationType type()
	{
		return type;
	}

	public GridPoint clone()
	{
		GridPoint newClone;
		try
		{
			newClone = (GridPoint)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException("GridPoint's super is Object, so should not have thrown CloneNotSupportedException");
		}
		newClone.type = this.type;
		newClone.dimension = this.dimension;
		return newClone;
	}

//	public boolean equals(Object that); // Need to override this
//	public int hashCode(); // Need to override this
	public abstract boolean isAdjacent(GridPoint that, boolean cornersCount);
//	public abstract GridEdge[] adjacentEdges();
	public abstract GridPoint[] adjacentPoints(boolean cornersCount);
	public abstract GridPoint[] nextPointOnLine(GridPoint[] line); // Must be point which directly follows the last point in line
	public abstract int manhattanDistance(GridPoint that);
	public abstract boolean orthogonalLinePossible(GridPoint that);
	public abstract GridPoint promptForPoint(Scanner input);
}