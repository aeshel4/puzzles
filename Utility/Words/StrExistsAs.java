package Utility.Words;

public enum StrExistsAs
{
        // Prefix, Word,  Fail
    FAIL   (false, false, true),
    NONE   (false, false, false),
    PREFIX (true,  false, false),
    WORD   (false, true,  false),
    BOTH   (true,  true,  false);

    private boolean pre;
    private boolean wrd;
    private boolean fail;

    private StrExistsAs(boolean pre, boolean wrd, boolean fail)
    {
        this.pre = pre;
        this.wrd = wrd;
        this.fail = fail;
    }

    public boolean isPrefix()
    {
        if (fail) {throw new RuntimeException("Search failed, shouldn't use this check.");}
        return pre;
    }
    public boolean isWord()
    {
        if (fail) {throw new RuntimeException("Search failed, shouldn't use this check.");}
        return wrd;
    }

    public static StrExistsAs Construct(boolean pre, boolean wrd, boolean fail)
    {
        if (fail) {return StrExistsAs.FAIL;}
        else if (pre)
        {
            if (wrd) {return StrExistsAs.BOTH;}
            else {return StrExistsAs.PREFIX;}
        }
        else
        {
            if (wrd) {return StrExistsAs.WORD;}
            else {return StrExistsAs.NONE;}
        }
    }
}