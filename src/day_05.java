
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class day_05
{
    public static BufferedReader reader;
    public static String input;
    public static Pattern seedPattern = Pattern.compile("seeds: (.*)");
    public static Pattern mapPattern = Pattern.compile("([0-9]+)( +)([0-9]+)( +)([0-9]+)");
    public static Matcher seedMatcher;
    public static Matcher mapMatcher;

    public static List<Long> seedList;

    // Attributes in order:
    // Seed, Soil, Fertilizer, Water, Light, Temperature, Humidity, Location
    public static List<List<Long>> seeds;

    public static class mapping
    {
        String srcName, tgtName;
        List<Long[]> ranges;


        public mapping(String source, String target)
        {
            srcName = source;
            tgtName = target;
            ranges = new ArrayList<>();
        }


        public void addValue(Long inputStart, Long outputStart, Long range)
        {
            ranges.add(new Long[] {inputStart, inputStart + range - 1, outputStart, outputStart + range - 1});
        }

        public void sortValues(String s)
        {
            if (s.equals("start"))
                ranges.sort((o1, o2) -> o1[0].compareTo(o2[0]));
            else
                ranges.sort((o1, o2) -> o1[2].compareTo(o2[2]));
        }

        // To be used on seeds List, E.g. Target List
        public mapping mapValues(mapping mapper)
        {
            long start = 0, end = 0;
            Long[] map;

            mapping target = new mapping(mapper.srcName, mapper.tgtName);
            mapping source = this;

            source.sortValues("");
            mapper.sortValues("start");

            for (Long[] src: this.ranges)
            {
                for (int i = 0; i < mapper.ranges.size(); i++)
                {
                    map = mapper.ranges.get(i);

                    if (src[2] >= map[0] && src[2] <= map[1])
                    {
                        start = map[2] + (src[2] - map[0]);

                        if (src[3] < map[1])
                        {
                            end = start + (src[3] - src[2]);
                            target.addValue(0L, start, end - start + 1);

                            break;
                        }
                        else
                        {
                            end = map[3];
                            target.addValue(0L, start, end - start + 1);

                            if (src[3] < mapper.ranges.get(i + 1)[0])
                            {
                                start = map[1] + 1;
                                end = src[3];

                                target.addValue(0L, start, end - start + 1);
                                break;
                            }
                            else if (mapper.ranges.get(i + 1)[0] == map[1] + 1)
                            {
                                start = mapper.ranges.get(i + 1)[0];
                                continue;
                            }
                            else
                            {
                                start = map[1] + 1;
                                end = mapper.ranges.get(i + 1)[0] - 1;

                                target.addValue(0L, start, end - start + 1);

                                start = mapper.ranges.get(i + 1)[0];
                                continue;
                            }
                        }
                    }

                    if (src[3] >= map[0])
                    {
                        end = map[2] + (src[3] - map[0]);

                        target.addValue(0L, start, end - start + 1);
                        break;
                    }
                }
            }

            return target;
        }


        public void printValues()
        {
            System.out.printf("Mapping %s to %s\n", srcName, tgtName);
            if (tgtName.equals("seeds"))
            {
                for (Long[] range: ranges)
                {
                    System.out.printf("Output: [%,d - %,d]\n", range[2], range[3]);
                }
            }
            else
            {
                for (Long[] range: ranges)
                {
                    System.out.printf("Input: [%,d - %,d], Output: [%,d - %,d]\n", range[0], range[1], range[2], range[3]);
                }
            }

            System.out.println();
        }
    }



    public static void main(String[] args)
    {
        System.out.println("Day 01 of Advent of Code 2023:\n");

        try {
            //problem_1();
            problem_2();

            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void problem_1() throws Exception
    {
        System.out.println("Problem_01:");

        //seedList = null;
        seeds = new ArrayList<>();
        int ac = 0; // Attributes counter - Defines which attribute we are currently setting up
        long src, tgt, range, attribute, minLocation = Long.MAX_VALUE;

        reader = new BufferedReader(new FileReader("./inputs/05.txt"));

        while ((input = reader.readLine()) != null)
        {
            if (input.contains("seeds:"))
            {
                seedMatcher = seedPattern.matcher(input);
                if (seedMatcher.find())
                {
                    seedList = Stream.of(seedMatcher.group(1).split("( +)")).map(Long::parseLong).collect(Collectors.toList());
                    seedList.sort(null); // Sort in natural order
                    for (Long seed: seedList)
                        seeds.add(new ArrayList<>() {{
                            add(seed);
                        }});
                }
            }

            if (input.contains("-"))
            {
                while ((input = reader.readLine()) != null && !input.isEmpty())
                {
                    mapMatcher = mapPattern.matcher(input);
                    if (mapMatcher.find())
                    {
                        tgt = Long.parseLong(mapMatcher.group(1));
                        src = Long.parseLong(mapMatcher.group(3));
                        range = Long.parseLong(mapMatcher.group(5));

                        for (List<Long> d: seeds)
                        {
                            attribute = d.get(ac);

                            if (attribute >= src && attribute <= src + range - 1)
                                d.add(tgt + attribute - src);
                        }
                    }
                }

                // In case value was not present in the mapping
                // We map it 1:1
                for (List<Long> d: seeds)
                    if (d.size() == ac + 1)
                        d.add(d.get(ac));

                ac++;
            }
        }

        for (List<Long> d: seeds)
            if (d.get(ac) < minLocation)
                minLocation = d.get(ac);

        System.out.printf("The lowest location number that corresponds to any of the initial seed numbers: %,d%n", minLocation);
        System.out.println();
    }


    public static void problem_2() throws Exception
    {
        System.out.println("Problem_02:");
        /*
        --Get values from input
        --Save seeds as ranges (Some structure - (containing start and end of range))
        --Create dictionary from the current mapping
        --Sort it by src
        Map src ranges to tgt ranges
        Store it as new src
        Repeat
         */

        int mapperListIterator = 0, seedMappingIterator = 0;
        long src, tgt, range;

        List<mapping> mapperList = new ArrayList<>();
        List<mapping> seedMapping = new ArrayList<>();

        reader = new BufferedReader(new FileReader("./inputs/05.txt"));

        while ((input = reader.readLine()) != null)
        {
            if (input.contains("seeds:"))
            {
                seedMapping.add(new mapping("", "seeds"));

                seedPattern = Pattern.compile("\\d+");
                seedMatcher = seedPattern.matcher(input);

                int i = 0;
                long prev = 0L;

                while (seedMatcher.find())
                {
                    if (i % 2 == 0)
                    {
                        prev = Long.parseLong(seedMatcher.group());
                        i++;
                    }
                    else
                    {
                        seedMapping.get(mapperListIterator).addValue(0L, prev, Long.valueOf(seedMatcher.group()));
                        i++;
                    }
                }

                seedMappingIterator++;
            }

            if (input.contains("-"))
            {
                Pattern attributePattern = Pattern.compile("(.*)-to-(.*) ");
                Matcher attributeMatcher = attributePattern.matcher(input);
                if (attributeMatcher.find())
                    mapperList.add(new mapping(attributeMatcher.group(1), attributeMatcher.group(2)));

                while ((input = reader.readLine()) != null && !input.isEmpty())
                {
                    mapMatcher = mapPattern.matcher(input);
                    if (mapMatcher.find())
                    {
                        tgt = Long.parseLong(mapMatcher.group(1));
                        src = Long.parseLong(mapMatcher.group(3));
                        range = Long.parseLong(mapMatcher.group(5));

                        mapperList.get(mapperListIterator).addValue(src, tgt, range);
                    }
                }

                mapperListIterator++;
            }
        }


        for (int i = 0; i < mapperListIterator; i++)
        {
            seedMapping.add(seedMapping.get(i).mapValues(mapperList.get(i)));
        }



        seedMapping.get(0).sortValues("");
        seedMapping.get(1).sortValues("");
        mapperList.get(0).sortValues("start");

        seedMapping.get(0).printValues();
        seedMapping.get(1).printValues();
        mapperList.get(0).printValues();

        /*
        for (mapping m: seedMapping)
        {
            m.sortValues("");
            m.printValues();
        }

         */
    }
}
