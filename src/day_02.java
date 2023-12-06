
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day_02
{
    public static BufferedReader reader;
    public static String input;

    public static Pattern pattern = Pattern.compile("Game (.*): (.*)");
    public static Matcher matcher;
    public static Pattern[] dict = new Pattern[]
            {
                    Pattern.compile("(.*) red"),
                    Pattern.compile("(.*) green"),
                    Pattern.compile("(.*) blue")
            };
    public static Matcher dictMatcher;
    public static int[] limits;
    public static String[] games;
    public static List<Integer> days;



    public static void main(String[] args)
    {
        System.out.println("Day 02 of Advent of Code 2023:\n");

        try {
            problem_1();
            problem_2();

            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void problem_1 () throws Exception
    {
        System.out.println("Problem_01:");

        limits = new int[]
                {
                        12,
                        13,
                        14
                };
        days = new ArrayList<>();
        int day;

        reader = new BufferedReader(new FileReader("./inputs/02.txt"));

        while ((input = reader.readLine()) != null)
        {
            matcher = pattern.matcher(input);
            if (matcher.find())
            {
                games = matcher.group(2).split(", |; ");
                day = Integer.parseInt(matcher.group(1));
                days.add(day);

                for (String game: games)
                {
                    for (int i = 0; i < dict.length; i++)
                    {
                        dictMatcher = dict[i].matcher(game);
                        if (dictMatcher.find() && Integer.parseInt(dictMatcher.group(1)) > limits[i])
                        {
                            days.remove(Integer.valueOf(day));
                            break;
                        }
                    }
                }
            }
        }

        System.out.printf("The sum of the IDs of possible games: %,d%n", days.stream().reduce(0, Integer::sum));
        System.out.println();
    }


    public static void problem_2 () throws Exception
    {
        System.out.println("Problem_02:");

        // Default values are 0
        limits = new int[3];
        int powers = 0, power = 1;

        reader = new BufferedReader(new FileReader("./inputs/02.txt"));

        while ((input = reader.readLine()) != null)
        {
            matcher = pattern.matcher(input);
            if (matcher.find())
            {
                games = matcher.group(2).split(", |; ");

                for (String cubes: games)
                {
                    for (int i = 0; i < dict.length; i++)
                    {
                        dictMatcher = dict[i].matcher(cubes);
                        if (dictMatcher.find() && Integer.parseInt(dictMatcher.group(1)) > limits[i])
                        {
                            limits[i] = Integer.parseInt(dictMatcher.group(1));
                        }
                    }
                }

                for (int limit: limits)
                {
                    power *= limit;
                }
                powers += power;
            }

            power = 1;
            Arrays.fill(limits, 0);
        }

        System.out.printf("The sum of the powers of minimum necessary cubes: %,d%n", powers);
        System.out.println();
    }
}
