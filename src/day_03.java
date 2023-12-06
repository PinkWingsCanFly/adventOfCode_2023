
import java.io.BufferedReader;
import java.io.FileReader;


public class day_03
{
    public static BufferedReader reader;
    public static Object[] input;
    public static char[][] charInput;
    public static char filler = '.';
    public static char gear = '*';
    public static int sum;



    public static void main(String[] args)
    {
        System.out.println("Day 03 of Advent of Code 2023:\n");

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

        sum = 0;
        int x, y;
        char c;

        reader = new BufferedReader(new FileReader("./inputs/03.txt"));
        input = reader.lines().toArray();
        x = input[0].toString().length();
        y = input.length;
        charInput = new char[y][x];

        for (int i = 0; i < input.length; i++)
        {
            charInput[i] = input[i].toString().toCharArray();
        }

        for (int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                c = charInput[i][j];

                if (c > 47 && c < 58)
                {
                    j = scanProblem_1(charInput, i, j, y, x);
                }
            }
        }

        System.out.printf("The sum of all of the part numbers in the engine schematic: %,d%n", sum);
        System.out.println();
    }


    public static void problem_2 () throws Exception
    {
        System.out.println("Problem_02:");

        sum = 0;
        int x, y;

        reader = new BufferedReader(new FileReader("./inputs/03.txt"));
        input = reader.lines().toArray();
        x = input[0].toString().length();
        y = input.length;
        charInput = new char[y][x];

        for (int i = 0; i < input.length; i++)
        {
            charInput[i] = input[i].toString().toCharArray();
        }

        for (int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                if (charInput[i][j] == gear)
                {
                    scanProblem_2(charInput, i, j, y, x);
                }
            }
        }

        System.out.printf("The sum of all of the gear ratios in the engine schematic: %,d%n", sum);
        System.out.println();
    }



    public static int scanProblem_1(char[][] c, int y, int x, int height, int length)
    {
        int numberLength = 0;

        // Check the length of the number
        while (c[y][x + numberLength] > 47 && c[y][x + numberLength] < 58)
            if (x + numberLength + 1 == length)
            {
                numberLength++;
                break;
            }
            else numberLength++;
        numberLength--;

        // Check its surroundings
        for (int i = y - 1; i <= y + 1; i++)
        {
            if (i < 0 || i > height - 1)
                continue;

            for (int j = x - 1; j <= x + 1 + numberLength; j++)
            {
                if (j < 0 || j > length - 1)
                    continue;

                if (i == y && (j >= x && j <= x + numberLength))
                    continue;

                // If something was found in the surrounding
                // Get the number and move past it
                if (c[i][j] != filler)
                {
                    for (int k = 0; k <= numberLength; k++)
                    {
                        sum += (int) (Math.pow(10, numberLength - k) * Character.getNumericValue(c[y][x + k]));
                    }
                    return x + numberLength;
                }
            }
        }

        // If nothing was found in the surrounding
        // clear the number from the board and move past it
        for (int i = 0; i <= numberLength; i++)
        {
            c[y][x + i] = filler;
        }
        return x + numberLength;
    }


    public static void scanProblem_2(char[][] c, int y, int x, int height, int length)
    {
        int left = 1, right = 1;
        int leftIndex, rightIndex;
        boolean leftStop = true, rightStop = true;
        int number = 0, numberCounter = 0, gearProduct = 1;

        for (int i = y - 1; i <= y + 1; i++)
        {
            if (i < 0 || i > height - 1)
                continue;

            for (int j = x - 1; j <= x + 1; j++)
            {
                if (j < 0 || j > length - 1)
                    continue;

                // Number was found in the gear's surrounding
                if (c[i][j] > 47 && c[i][j] < 58)
                {
                    number = 0;
                    leftIndex = j;
                    rightIndex = j;

                    // Scan for the number coordinates
                    while (leftStop || rightStop)
                    {
                        if (j - left >= 0)
                        {
                            if (c[i][j - left] > 47 && c[i][j - left] < 58)
                            {
                                leftIndex = j - left;
                                left++;
                            }
                            else
                                leftStop = false;
                        }
                        else
                            leftStop = false;

                        if (j + right < length)
                        {
                            if (c[i][j + right] > 47 && c[i][j + right] < 58)
                            {
                                rightIndex = j + right;
                                right++;
                            }
                            else
                                rightStop = false;
                        }
                        else
                            rightStop = false;
                    }

                    // Get the number
                    for (int k = leftIndex; k <= rightIndex; k++)
                    {
                        number += (int) (Math.pow(10, rightIndex - k) * Character.getNumericValue(c[i][k]));
                    }
                    numberCounter++;
                    gearProduct *= number;

                    left = 1; right = 1;
                    leftStop = true; rightStop = true;

                    // Move past this number
                    if (rightIndex > x + 1)
                    {
                        j = x + 1;
                    }
                    else
                        j = rightIndex;
                }
            }
        }

        // If this gear have only 2 adjacent numbers
        // Add it to the sum
        if (numberCounter == 2)
        {
            sum += gearProduct;
        }
    }
}
