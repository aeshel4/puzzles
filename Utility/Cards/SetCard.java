package Utility.Cards;

enum Tri
{
    FIRST,  // red,    diamond,  solid,  single
    SECOND, // green,  squiggle, stripe, double
    THIRD   // purple, oval,     clear,  triple
}

public class SetCard
{
    private Tri[] tris;

    public SetCard(int a, int b, int c, int d)
    {
        this(CastToTri(a), CastToTri(b), CastToTri(c), CastToTri(d));
    }

    public SetCard(Tri a, Tri b, Tri c, Tri d)
    {
        tris = new Tri[4];
        tris[0] = a;
        tris[1] = b;
        tris[2] = c;
        tris[3] = d;
    }

    private static Tri CastToTri(int i)
    {
        switch (i)
        {
            case 0: return Tri.FIRST;
            case 1: return Tri.SECOND;
            case 2: return Tri.THIRD;
            default: throw new RuntimeException("Bad Tri");
        }
    }

    private static boolean TriIsSet(Tri t1, Tri t2, Tri t3)
    {
        return (t1 == t2 && t2 == t3) ||
               (t1 != t2 && t2 != t3 && t1 != t3);
    }

    public static boolean IsSet(SetCard s1, SetCard s2, SetCard s3)
    {
        for (int i = 0; i < s1.tris.length; ++i)
        {
            if (!TriIsSet(s1.tris[i], s2.tris[i], s3.tris[i]))
            {
                return false;
            }
        }
        return true;
    }

    public String toString()
    {
        String str = "";
        switch (tris[0])
        {
            case FIRST: str += 'R'; break;
            case SECOND: str += 'G'; break;
            case THIRD: str += 'P'; break;
        }
        switch (tris[1])
        {
            case FIRST: str += '^'; break;
            case SECOND: str += '~'; break;
            case THIRD: str += '0'; break;
        }
        switch (tris[2])
        {
            case FIRST: str += '#'; break;
            case SECOND: str += '='; break;
            case THIRD: str += '.'; break;
        }
        switch (tris[3])
        {
            case FIRST: str += '1'; break;
            case SECOND: str += '2'; break;
            case THIRD: str += '3'; break;
        }

        return str;
    }

    public boolean equals(SetCard that, int t1, int t2)
    {
        return (this.tris[t1] == that.tris[t1]) &&
               (this.tris[t2] == that.tris[t2]);
    }
}