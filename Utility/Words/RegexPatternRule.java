package Utility.Words;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPatternRule implements RuleForMatching
{
    private Pattern pattern;
    private Matcher matcher;

    public RegexPatternRule(String myStylePattern)
    {
        for (int i = 0; i < myStylePattern.length(); ++i)
        {
            char ch = myStylePattern.charAt(i);
            if (!(Character.isLetterOrDigit(ch) || ch == '.'))
            {
                throw new RuntimeException("String should only contain alphanumeric or '.'  " + ch);
            }
        }
        String convertedPattern = myStylePattern.toUpperCase();

        char ch; int j = 1;
        for (int i = 0; i < convertedPattern.length(); ++i)
        {
            ch = convertedPattern.charAt(i);
            if (Character.isDigit(ch))
            {
                convertedPattern = convertedPattern.substring(0, i) + "(.)" + convertedPattern.substring(i+1);
                convertedPattern = convertedPattern.replace(String.valueOf(ch), "\\" + String.valueOf(j));
                ++j;
            }
        }
        pattern = Pattern.compile(convertedPattern);
    }

    public RegexPatternRule(Pattern pattern)
    {
        this.pattern = pattern;
    }

    public boolean IsAMatch(String word)
    {
        matcher = pattern.matcher(word);
        return matcher.matches();
    }
}
