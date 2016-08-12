package Utility.Cards;

public class SetDeck
{
    private SetCard[][][][] cards;
    private boolean[] used;

    public SetDeck()
    {
        cards = new SetCard[3][3][3][3];

        for (int a = 0; a < cards.length; ++a) {
        for (int b = 0; b < cards[a].length; ++b) {
        for (int c = 0; c < cards[a][b].length; ++c) {
        for (int d = 0; d < cards[a][b][c].length; ++d) {
            cards[a][b][c][d] = new SetCard(a, b, c, d);
        }}}}

        used = new boolean[3 * 3 * 3 * 3];
        for (int i = 0; i < used.length; ++i)
        {
            used[i] = false;
        }
    }

    public boolean GetNewCard(int n)
    {
        if (!used[n])
        {
            used[n] = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void ReturnCard(int n)
    {
        if (!used[n])
        {
            throw new RuntimeException("Returning card that was never taken " + n);
        }
        else
        {
            used[n] = false;
        }
    }

    public SetCard GetCard(int n)
    {
        if (n < 0 || n > 80)
        {
            throw new RuntimeException("Bad index to set deck");
        }
        return cards[(n/1)%3][(n/3)%3][(n/9)%3][(n/27)%3];
    }
    
    public String toString()
    {
        String str = "";
        for (int i = 0; i < 81; ++i)
        {
            str += i + ": " + GetCard(i) + '\n';
        }
        return str;
    }
}