import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class day_01
{
    public static BufferedReader reader;
    public static String input;
    public static char[] charInput;

    public static final Map<String, Integer> dict = new HashMap<>()
    {{
        put("1", 1);
        put("2", 2);
        put("3", 3);
        put("4", 4);
        put("5", 5);
        put("6", 6);
        put("7", 7);
        put("8", 8);
        put("9", 9);
        put("one", 1);
        put("two", 2);
        put("three", 3);
        put("four", 4);
        put("five", 5);
        put("six", 6);
        put("seven", 7);
        put("eight", 8);
        put("nine", 9);
    }};

    public static void main(String[] args)
    {
        System.out.println("Day 01 of Advent of Code 2023:\n");

        try {
            problem_1();
            problem_2();

            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void problem_1() throws Exception
    {
        System.out.println("Problem_01:");

        reader = new BufferedReader(new FileReader("./inputs/01.txt"));

        String coords = null;
        char tmp;
        int output = 0;

        while ((input = reader.readLine()) != null)
        {
            charInput = input.toCharArray();

            tmp = 0;
            for (char c: charInput)
            {
                if (c > 47 && c < 58)
                {
                    if (tmp == 0)
                    {
                        coords = String.valueOf(c);
                    }
                    tmp = c;
                }
            }
            coords = coords + tmp;

            output += Integer.parseInt(coords);
        }

        System.out.printf("The sum of all of the calibration values: %,d%n", output);
        System.out.println();
    }


    public static void problem_2 () throws Exception
    {
        System.out.println("Problem_02:");

        reader = new BufferedReader(new FileReader("./inputs/01.txt"));

        int output = 0;
        int tmp;

        while ((input = reader.readLine()) != null)
        {
            int firstIndex = Integer.MAX_VALUE, lastIndex = -1;
            int firstValue = 0, lastValue = 0;
            for (Map.Entry<String, Integer> entry: dict.entrySet())
            {
                if (input.contains(entry.getKey()))
                {
                    if ((tmp = input.indexOf(entry.getKey())) < firstIndex)
                    {
                        firstValue = entry.getValue();
                        firstIndex = tmp;
                    }
                    if ((tmp = input.lastIndexOf(entry.getKey())) > lastIndex)
                    {
                        lastValue = entry.getValue();
                        lastIndex = tmp;
                    }
                }
            }

            output += 10 * firstValue + lastValue;
        }

        System.out.printf("the sum of all of the calibration values: %,d%n", output);
        System.out.println();
    }
}