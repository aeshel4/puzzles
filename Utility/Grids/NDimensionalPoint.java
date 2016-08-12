package Utility.Grids;

import java.util.Scanner;
import java.util.Arrays;

public class NDimensionalPoint extends GridPoint
{
	private int[] point;

	public static final NDimensionalPoint NULL_POINT = new NDimensionalPoint(0);

	public NDimensionalPoint(int numDimensions)
	{
		super(TessalationType.RECTANGULAR, numDimensions);

		point = new int[numDimensions];
		for (int d = 0; d < point.length; ++d)
		{
			point[d] = 0;
		}
	}

	public NDimensionalPoint(int[] p)
	{
		super(TessalationType.RECTANGULAR, p.length);

		point = new int[p.length];
		for (int d = 0; d < point.length; ++d)
		{
			point[d] = p[d];
		}
	}

	public GridPoint clone()
	{
		NDimensionalPoint newClone = (NDimensionalPoint)super.clone();
		newClone.point = Arrays.copyOf(this.point, this.point.length);
		return newClone;
	}

	public boolean equals(Object t)
	{
		NDimensionalPoint that = (NDimensionalPoint)t;

		if (this.point.length != that.point.length)
		{
			return false;
		}
		
		for (int d = 0; d < point.length; ++d)
		{
			if (this.point[d] != that.point[d])
			{
				return false;
			}
		}
		return true;
	}

	public int hashCode()
	{
		int h = 0;
		for (int d = 0, m = 1; d < point.length; ++d, m *= 100)
		{
			h += point[d] * m;
		}
		return h;
	}

	public boolean isAdjacent(GridPoint t, boolean cornersCount)
	{
		NDimensionalPoint that = (NDimensionalPoint)t;
		if (this.dimension() != that.dimension())
		{
			return false;
		}

		int countEqualDimensions = 0;
		for (int d = 0; d < point.length; ++d)
		{
			int diff = this.point[d] - that.point[d];
			if (diff < -1 || diff > 1)
			{
				return false;
			}
			if (diff == 0)
			{
				++countEqualDimensions;
			}
		}

		if (countEqualDimensions == this.dimension())
		{
			// These are equal points
			return false;
		}
		else
		{
			// Would have early returned if far away
			return cornersCount || (countEqualDimensions == 1);
		}
	}

	public GridPoint[] adjacentPoints(boolean cornersCount)
	{
		NDimensionalPoint[] points;
		if (cornersCount)
		{
			// The number of possible points corresponds to either going down, going up or staying
			// in each dimension independently of each other; that is 3^d.
			int numPossible = (int)(Math.pow(3, this.dimension()));
			points = new NDimensionalPoint[numPossible - 1];

			// Skip 0, since that would correspond to this point (staying in each dimension)
			for (int i = 1; i < numPossible; ++i)
			{
				points[i - 1] = (NDimensionalPoint)this.clone();
				for (int d = 0; d < this.dimension(); ++d)
				{
					// Translate 2 to -1 so that the skipped 0 translates to all 0's
					int shiftAmount = Utility.Math.Math.GetNthDigitInBaseB(i, d, 3);
					if (shiftAmount == 2) {shiftAmount = -1;}

					points[i - 1].shift(d, shiftAmount);
				}
			}
		}
		else
		{
			points = new NDimensionalPoint[2 * this.dimension()];
			for (int d = 0; d < this.dimension(); ++d)
			{
				points[2 * d + 0] = (NDimensionalPoint)(this.clone());
				points[2 * d + 1] = (NDimensionalPoint)(this.clone());
				points[2 * d + 0].shift(d, 1);
				points[2 * d + 1].shift(d, -1);
			}
		}

		return points;
	}

	// This would be static if it could be
	public GridPoint[] nextPointOnLine(GridPoint[] line)
	{
		if (line.length < 2)
		{
			return null;
		}

		// For rectangular points, a line is determined by 2 points, so grab most recent two
		NDimensionalPoint p1 = (NDimensionalPoint)line[line.length - 2];
		NDimensionalPoint p2 = (NDimensionalPoint)line[line.length - 1];

		// There is only one possibility for the next point given these two previous.
		NDimensionalPoint[] next = new NDimensionalPoint[1];
		next[0] = p1.nextPointOnLine(p2);
		return next;
	}

	private NDimensionalPoint nextPointOnLine(NDimensionalPoint that)
	{
		if (this.dimension() != that.dimension())
		{
			throw new RuntimeException("Trying to continue line with points of differing dimensions.");
		}

		NDimensionalPoint newPoint = (NDimensionalPoint)that.clone();
		for (int d = 0; d < newPoint.dimension(); ++d)
		{
			// Since we only want the next point on this line, the actual diff doesn't matter, just
			// positive, negative or zero
			int diff = that.get(d) - this.get(d);
			if (diff > 0) {diff = 1;}
			else if (diff < 0) {diff = -1;}

			newPoint.shift(d, diff);
		}
		return newPoint;
	}

	public int manhattanDistance(GridPoint t)
	{
		NDimensionalPoint that = (NDimensionalPoint)t;
		if (this.dimension() != that.dimension())
		{
			throw new RuntimeException("Trying to continue line with points of differing dimensions.");
		}

		int distance = 0;
		for (int d = 0; d < dimension(); ++d)
		{
			distance += Math.abs(this.compare(that, d));
		}
		return distance;
	}

	public boolean orthogonalLinePossible(GridPoint t)
	{
		NDimensionalPoint that = (NDimensionalPoint)t;
		if (this.dimension() != that.dimension())
		{
			return false;
		}

		boolean oneDimensionDifferent = false;
		for (int d = 0; d < point.length; ++d)
		{
			if (this.point[d] != that.point[d])
			{
				if (oneDimensionDifferent)
				{
					// Now two dimensions are different
					return false;
				}
				oneDimensionDifferent = true;
			}
		}

		// If we finished the loop either same point or only one dimension
		// different, either way, return orthogonalLinePossible
		return true;
	}

	// This would be static if it could be
	public GridPoint promptForPoint(Scanner input)
	{
		NDimensionalPoint prompt = new NDimensionalPoint(point.length);
		for (int d = 0; d < point.length; ++d)
		{
			System.out.print("Dimension " + (d+1) + ": ");
			do
			{
				try
				{
					prompt.point[d] = Integer.parseInt(input.nextLine());
					break;
				}
				catch (NumberFormatException e)
				{
					System.out.print("Invalid choice, please try again: ");
				}
			} while (true);
		}
		return prompt;
	}

	public String toString()
	{
		String str = "(";
		for (int d = 0; d < point.length; ++d)
		{
			str += point[d];
			if (d != point.length - 1)
			{
				str += ", ";
			}
		}
		str += ")";
		return str;
	}

	public NDimensionalPoint trimFirstDimension()
	{
		return new NDimensionalPoint(Utility.Utility.RemoveFirstElementOfIntArray(this.point));
	}

	public void shift(int dimension, int amount)
	{
		this.point[dimension] += amount;
	}

	public void zero(int dimension)
	{
		set(dimension, 0);
	}

	public boolean advance(int dimension, int cap)
	{
		if (point[dimension] < cap - 1)
		{
			point[dimension]++;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void set(int dimension, int value)
	{
		point[dimension] = value;
	}

	public int get(int dimension)
	{
		return point[dimension];
	}

	public int compareTo(GridPoint t)
	{
		NDimensionalPoint that = (NDimensionalPoint)t;

		if (this.point.length != that.point.length)
		{
			return this.point.length - that.point.length;
		}

		for (int d = 0; d < point.length; ++d)
		{
			int diff = this.compare(that, d);
			if (diff != 0)
			{
				return diff;
			}
		}

		return 0;
	}

	public int compare(NDimensionalPoint that, int dimension)
	{
		return (this.get(dimension) - that.get(dimension));
	}
}