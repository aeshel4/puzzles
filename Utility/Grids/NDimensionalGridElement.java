package Utility.Grids;

// package private
class NDimensionalGridElement extends NDimensionalGrid
{
	private Object value;

	public NDimensionalGridElement(Object defaultValue)
	{
		super();
		value = defaultValue;
	}

	public NDimensionalGridElement clone()
	{
		NDimensionalGridElement newClone = (NDimensionalGridElement)super.clone();
		newClone.value = this.value;
		return newClone;
	}

	public Object get(GridPoint p)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;

		if (point.dimension() == this.dimension)
		{
			return value;
		}
		else
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}
	}

	public boolean contains(GridPoint p)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;
		return (point.dimension() == this.dimension);
	}

	public void set(GridPoint p, Object value)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;

		if (point.dimension() == this.dimension)
		{
			this.value = value;
		}
		else
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}
	}
	
	public String toString()
	{
		return value.toString();
	}
}