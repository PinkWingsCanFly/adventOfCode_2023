
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class day_04
{
    public static BufferedReader reader;
    public static String input;
    public static Pattern pattern = Pattern.compile("Card( +)([0-9]+):( +)(.*) \\|( +)([0-9].*)");
    String sth = "\"Old Value\":\"([a-zA-Z !@#$%^&*_+-={}\\|:',<.>/?]*)\"";
    public static Matcher matcher;
    public static int sum;



    public static void main(String[] args)
    {
        System.out.println("Day 04 of Advent of Code 2023:\n");

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
        int cardNumber;
        List<Integer> winningNumbers, elfoNumbers;

        reader = new BufferedReader(new FileReader("./inputs/04.txt"));

        while ((input = reader.readLine()) != null)
        {
            matcher = pattern.matcher(input);
            if (matcher.find())
            {
                // Split each row of numbers and stream the into an Integer List
                cardNumber = Integer.parseInt(matcher.group(2));
                winningNumbers = Stream.of(matcher.group(4).split("( +)")).map(Integer::parseInt).collect(Collectors.toList());
                elfoNumbers = Stream.of(matcher.group(6).split("( +)")).map(Integer::parseInt).collect(Collectors.toList());

                /*
                System.out.println("Card number: " + cardNumber);
                System.out.println("Winning numbers: " + winningNumbers);
                System.out.println("Elf's numbers: " + elfoNumbers);
                 */

                elfoNumbers.retainAll(winningNumbers);
                sum += (int) Math.pow(2, elfoNumbers.size() - 1);

                /*
                System.out.println("Elf's winning numbers: " + elfoNumbers);
                System.out.println("Card points: " + (int) Math.pow(2, elfoNumbers.size() - 1));
                System.out.println();
                 */
            }
        }

        System.out.printf("The sum of the IDs of possible games: %,d%n", sum);
        System.out.println();
    }


    public static void problem_2 () throws Exception
    {
        System.out.println("Problem_02:");

        sum = 0;
        int cardNumber, cardsSize = 0;
        int[] cards;
        List<Integer> winningNumbers, elfoNumbers;

        // Find the total number of Cards
        reader = new BufferedReader(new FileReader("./inputs/04.txt"));
        while (reader.readLine() != null) cardsSize++;
        cards = new int[cardsSize];
        Arrays.fill(cards, 1);

        reader = new BufferedReader(new FileReader("./inputs/04.txt"));

        while ((input = reader.readLine()) != null)
        {
            matcher = pattern.matcher(input);
            if (matcher.find())
            {
                // Split each row of numbers and stream the into an Integer List
                cardNumber = Integer.parseInt(matcher.group(2));
                winningNumbers = Stream.of(matcher.group(4).split("( +)")).map(Integer::parseInt).collect(Collectors.toList());
                elfoNumbers = Stream.of(matcher.group(6).split("( +)")).map(Integer::parseInt).collect(Collectors.toList());

                /*
                System.out.println("Card number: " + cardNumber);
                System.out.println("Winning numbers: " + winningNumbers);
                System.out.println("Elf's numbers: " + elfoNumbers);
                 */

                elfoNumbers.retainAll(winningNumbers);
                for (int i = cardNumber; i < cardNumber + elfoNumbers.size() && i < cardsSize; i++)
                {
                    cards[i] += cards[cardNumber - 1];
                }

                /*
                System.out.println("Elf's winning numbers: " + elfoNumbers);
                System.out.println("Card points: " + elfoNumbers.size());
                System.out.println(Arrays.toString(cards));
                System.out.println();
                 */
            }
        }

        System.out.printf("The sum of all the scratchcards: %,d%n", Arrays.stream(cards).sum());
        System.out.println();
    }
}
