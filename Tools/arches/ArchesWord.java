package Tools.arches;

import java.util.*;
import java.io.*;
import Utility.Words.*;
import Utility.Grids.*;

class ArchesWord
{
	private ArrayList<GridPoint> points;
	private String word;
	private boolean valid;

	public ArchesWord()
	{
		points = new ArrayList<GridPoint>();
		word = "";
		valid = true;
	}

	public ArchesWord(ArchesWord that) // copy constructor
	{
		this.points = new ArrayList<GridPoint>(that.points);
		this.word = that.word;
		this.valid = that.valid;
	}

	public void add(GridPoint point, String ch)
	{
		if (point != null)
		{
			points.add(point);
		}
		word += ch;
	}

	public boolean isPointUsed(GridPoint point)
	{
		return points.contains(point);
	}

	public boolean overlaps(ArchesWord that)
	{
		ArrayList<GridPoint> pts1 = this.getPoints(false);
		ArrayList<GridPoint> pts2 = that.getPoints(false);
		for (int p1 = 0; p1 < pts1.size(); ++p1)
		{
			for (int p2 = 0; p2 < pts2.size(); ++p2)
			{
				if (pts1.get(p1).equals(pts2.get(p2)))
				{
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<GridPoint> getPoints(boolean ignoreValidityCheck)
	{
		return (ignoreValidityCheck || valid) ? points : new ArrayList<GridPoint>(0);
	}

	public String getWord(boolean ignoreValidityCheck)
	{
		return (ignoreValidityCheck || valid) ? word : "";
	}

	public String toString()
	{
		String str = "";
		if (!valid)
		{
			str += " **Invalid** ";
		}

		str += word + ": ";
		for (int p = 0; p < points.size(); ++p)
		{
			str += points.get(p).toString();
			if (p != points.size() - 1)
			{
				str +=  "   ";
			}
		}
		return str;
	}

	public void setValidity(boolean valid)
	{
		this.valid = valid;
	}

	public boolean isValid()
	{
		return valid;
	}

	public boolean equals(Object o)
	{
		ArchesWord that = (ArchesWord)o;

		return this.word.equals(that.word) &&
				this.points.equals(that.points);
	}

	public int hashCode()
	{
		return word.hashCode() * points.hashCode();
	}
}