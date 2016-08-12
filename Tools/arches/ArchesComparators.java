package Tools.arches;

import java.util.*;
import java.io.*;
import Utility.Words.*;
import Utility.Grids.*;

class ArchesWordComparatorAlphabetical implements Comparator<ArchesWord>
{
	private boolean ignoreValidityCheck;
	public ArchesWordComparatorAlphabetical(boolean ignoreValidityCheck)
	{
		this.ignoreValidityCheck = ignoreValidityCheck;
	}

	public int compare(ArchesWord w1, ArchesWord w2)
	{
		return w1.getWord(ignoreValidityCheck).compareTo(w2.getWord(ignoreValidityCheck));
	}
}

class ArchesWordComparatorLength implements Comparator<ArchesWord>
{
	private boolean ignoreValidityCheck;
	public ArchesWordComparatorLength(boolean ignoreValidityCheck)
	{
		this.ignoreValidityCheck = ignoreValidityCheck;
	}

	public int compare(ArchesWord w1, ArchesWord w2)
	{
		String s1 = w1.getWord(ignoreValidityCheck);
		String s2 = w2.getWord(ignoreValidityCheck);
		if (s1.length() == s2.length())
		{
			return s1.compareTo(s2);
		}
		else
		{
			return s1.length() - s2.length();
		}
	}
}

class ArchesWordComparatorGrid implements Comparator<ArchesWord>
{
	private boolean ignoreValidityCheck;
	public ArchesWordComparatorGrid(boolean ignoreValidityCheck)
	{
		this.ignoreValidityCheck = ignoreValidityCheck;
	}

	public int compare(ArchesWord w1, ArchesWord w2)
	{
		ArrayList<GridPoint> a1 = w1.getPoints(ignoreValidityCheck);
		ArrayList<GridPoint> a2 = w2.getPoints(ignoreValidityCheck);
		for (int p = 0; p < a1.size() && p < a2.size(); ++p)
		{
			int diff = a1.get(p).compareTo(a2.get(p));
			if (diff != 0)
			{
				return diff;
			}
		}
		return a1.size() - a2.size();
	}
}

class ArchesWordComparatorJumpGrid implements Comparator<ArchesWord>
{
	private boolean ignoreValidityCheck;
	public ArchesWordComparatorJumpGrid(boolean ignoreValidityCheck)
	{
		this.ignoreValidityCheck = ignoreValidityCheck;
	}

	public int compare(ArchesWord w1, ArchesWord w2)
	{
		ArrayList<GridPoint> pts1 = w1.getPoints(ignoreValidityCheck);
		ArrayList<GridPoint> pts2 = w2.getPoints(ignoreValidityCheck);
		int d1 = steadyDimension(pts1);
		int d2 = steadyDimension(pts2);
		if (d1 != d2)
		{
			return d1 - d2;
		}
		else if (d1 == -1)
		{
			return -1;
		}
		else if (d2 == -1)
		{
			return 1;
		}
		else
		{
			NDimensionalPoint p1 = (NDimensionalPoint)pts1.get(0);
			NDimensionalPoint p2 = (NDimensionalPoint)pts2.get(0);
			return p1.get(d1) - p2.get(d2);
		}
	}

	private int steadyDimension(ArrayList<GridPoint> points)
	{
		if (points.size() == 0)
		{
			return -1;
		}
		for (int d = 0; d < points.get(0).dimension(); ++d)
		{
			int val = Integer.MAX_VALUE;
			Iterator<GridPoint> pointIter = points.iterator();
			while (pointIter.hasNext())
			{
				NDimensionalPoint pt = (NDimensionalPoint)pointIter.next();
				if (val == Integer.MAX_VALUE)
				{
					val = pt.get(d);
				}
				else if (pt.get(d) != val)
				{
					val = Integer.MAX_VALUE;
					break;
				}
			}

			if (val != Integer.MAX_VALUE)
			{
				return d;
			}
		}
		throw new RuntimeException("There was no steady dimension.");
	}
}