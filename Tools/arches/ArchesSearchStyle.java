package Tools.arches;

import java.util.*;
import java.io.*;
import Utility.Words.*;
import Utility.Grids.*;

enum ARCHES_SEARCH_STYLE
{
	CLASSIC,
	BOGGLE,
	KNIGHT,
	JUMP;

	static ARCHES_SEARCH_STYLE getFromString(String s)
	{
		if (s.equalsIgnoreCase("CLASSIC"))
		{
			return ARCHES_SEARCH_STYLE.CLASSIC;
		}
		else if (s.equalsIgnoreCase("BOGGLE"))
		{
			return ARCHES_SEARCH_STYLE.BOGGLE;
		}
		else if (s.equalsIgnoreCase("KNIGHT"))
		{
			return ARCHES_SEARCH_STYLE.KNIGHT;
		}
		else if (s.equalsIgnoreCase("JUMP"))
		{
			return ARCHES_SEARCH_STYLE.JUMP;
		}
		else
		{
			throw new RuntimeException("Don't know the ARCHES_SEARCH_STYLE: " + s);
		}
	}
}

interface ArchesSearchStyle
{
	public GridPoint[] getValidNextPoints(ArchesWord archesWord);
}

class ArchesSearchStyle_Normal implements ArchesSearchStyle
{
	private boolean boggle;
	private boolean cardinalOnly;

	public ArchesSearchStyle_Normal(ARCHES_SEARCH_STYLE style, boolean cardinalOnly)
	{
		this.boggle = (style == ARCHES_SEARCH_STYLE.BOGGLE);
		this.cardinalOnly = cardinalOnly;
	}

	public GridPoint[] getValidNextPoints(ArchesWord archesWord)
	{
		ArrayList<GridPoint> existingPoints = archesWord.getPoints(false);
		int lastPointLoc = existingPoints.size() - 1;
		if (boggle || (existingPoints.size() == 1))
		{
			GridPoint lastPoint = existingPoints.get(lastPointLoc);
			return lastPoint.adjacentPoints(!cardinalOnly);
		}
		else
		{
			// nextPointOnLine is really supposed to act like static, but abstract static aren't
			// allowed in Java, so pick a point to call it on.
			// For HexGrids, cardinalOnly has a special meaning requiring a special call
			GridPoint[] points = existingPoints.get(0).nextPointOnLine(existingPoints.toArray(new GridPoint[0]));
			if ((points.length == 3) && cardinalOnly && (points[0].type() == TessalationType.HEXAGONAL))
			{
				GridPoint[] straightPoint = new GridPoint[1];
				for (int i = 0; i < points.length; ++i)
				{
					if (HexGrid2DPoint.lineIsStraight((HexGrid2DPoint)existingPoints.get(lastPointLoc - 1), 
													  (HexGrid2DPoint)existingPoints.get(lastPointLoc), 
													  (HexGrid2DPoint)points[i]))
					{
						straightPoint[0] = points[i];
					}
				}
				return straightPoint;
			}
			else
			{
				return points;
			}
		}
	}
}

class ArchesSearchStyle_Knight implements ArchesSearchStyle
{
	public GridPoint[] getValidNextPoints(ArchesWord archesWord)
	{
		ArrayList<GridPoint> existingPoints = archesWord.getPoints(false);
		NDimensionalPoint lastPoint = (NDimensionalPoint)(existingPoints.get(existingPoints.size() - 1));

		// Pick a long dimension, then a different short dimension. Long/short can be -1/+1
		int dim = lastPoint.dimension();
		NDimensionalPoint[] points = new NDimensionalPoint[dim * (dim - 1) * 2 * 2];
		int pointIdx = 0;

		for (int dl = 0; dl < dim; ++dl)
		{
			for (int ds = 0; ds < dim; ++ds)
			{
				if (dl == ds) {continue;}

				for (int pl = -1; pl <= 1; pl += 2)
				{
					for (int ps = -1; ps <= 1; ps += 2)
					{
						points[pointIdx] = (NDimensionalPoint)lastPoint.clone();
						points[pointIdx].shift(dl, pl * 2);
						points[pointIdx].shift(ds, ps * 1);
						++pointIdx;
					}
				}
			}
		}
		return points;
	}
}

// Currently doesn't bother with searchParams and hard-code assumes orthogonal (and forward only)
class ArchesSearchStyle_Jump implements ArchesSearchStyle
{
	private GridTessalation grid;
	public ArchesSearchStyle_Jump(GridTessalation grid)
	{
		this.grid = grid;
	}

	public GridPoint[] getValidNextPoints(ArchesWord archesWord)
	{
		ArrayList<GridPoint> existingPoints = archesWord.getPoints(false);
		GridPoint lastPoint = existingPoints.get(existingPoints.size() - 1);

		ArrayList<GridPoint> validPoints = new ArrayList<GridPoint>();
		if (existingPoints.size() == 1)
		{
			Iterator<GridPoint> pointIter = grid.iterator();
			while (pointIter.hasNext())
			{
				GridPoint potentialPoint = pointIter.next();
				if (potentialPoint.equals(lastPoint))
				{
					continue;
				}
				
				if (lastPoint.orthogonalLinePossible(potentialPoint))
				{
					// Only move forward.
					if (lastPoint.compareTo(potentialPoint) < 0)
					{
						validPoints.add(potentialPoint);
					}
				}
			}
		}
		else
		{
			ArrayList<GridPoint> allPoints = new ArrayList<GridPoint>();
			allPoints.addAll(existingPoints);
			while (true)
			{
				// nextPointOnLine is really supposed to act like static, but abstract static aren't
				// allowed in Java, so pick a point to call it on.
				// We assume cardinalOnly and for HexGrids this has a special meaning requiring a special call
				GridPoint[] nextPoint = lastPoint.nextPointOnLine(allPoints.toArray(new GridPoint[0]));
				if ((nextPoint.length == 3) && (nextPoint[0].type() == TessalationType.HEXAGONAL))
				{
					GridPoint[] straightPoint = new GridPoint[1];
					for (int i = 0; i < nextPoint.length; ++i)
					{
						if (HexGrid2DPoint.lineIsStraight((HexGrid2DPoint)existingPoints.get(0), 
														  (HexGrid2DPoint)lastPoint, 
														  (HexGrid2DPoint)nextPoint[i]))
						{
							nextPoint[0] = nextPoint[i];
							break;
						}
					}
				}

				if (!grid.contains(nextPoint[0]))
				{
					break;
				}

				allPoints.add(nextPoint[0]);
				validPoints.add(nextPoint[0]);
			}
		}

		return validPoints.toArray(new GridPoint[0]);
	}
}