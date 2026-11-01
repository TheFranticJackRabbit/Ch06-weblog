/**
 * Read web server data and analyse hourly access patterns.
 *
 * Chapter 7 exercises 7.12, 7.14, 7.15, 7.16 and 7.18,
 * completed 2026.11.01.
 *
 *   7.12 - a second constructor that takes the name of the log file.
 *   7.14 - numberOfAccesses(): the total number of accesses in the log.
 *   7.15 - busiestHour():      the hour with the most accesses.
 *   7.16 - quietestHour():     the hour with the fewest accesses.
 *   7.18 - busiestTwoHour():   the first hour of the busiest two-hour
 *                              period, including the one that wraps
 *                              around midnight (11pm to 1am).
 *
 * All of the new methods read their answers out of the hourCounts
 * array, so analyzeHourlyData() must be called first to fill it in.
 *
 * @author David J. Barnes and Michael Kölling.
 * @author Ashton Harbison
 * @version 2026.11.01
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     * This version reads the default log file (weblog.txt).
     */
    public LogAnalyzer()
    {
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }

    /**
     * Create an object to analyze hourly web accesses from a
     * particular log file instead of the default one. (Exercise 7.12)
     * The file must be in the BlueJ project folder so the reader
     * can find it.
     * @param filename The name of the log file to analyze,
     *                 for example "weblog.txt".
     */
    public LogAnalyzer(String filename)
    {
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // LogfileReader already has a constructor that takes a
        // file name, so we just pass the name along to it.
        reader = new LogfileReader(filename);
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }

    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }

    /**
     * Return the total number of accesses recorded in the log file.
     * Every access was counted into exactly one slot of hourCounts
     * by analyzeHourlyData, so adding up all 24 slots gives the total.
     * (Exercise 7.14)
     * @return The total number of accesses.
     */
    public int numberOfAccesses()
    {
        int total = 0;
        // Add the count for each hour of the day onto the total.
        for(int hour = 0; hour < hourCounts.length; hour++) {
            total = total + hourCounts[hour];
        }
        return total;
    }

    /**
     * Return the busiest hour of the day: the hour with the most
     * accesses. If two or more hours tie for the most accesses,
     * the earliest of them is returned. (Exercise 7.15)
     * @return The busiest hour, from 0 (midnight) to 23 (11pm).
     */
    public int busiestHour()
    {
        // Start by assuming hour 0 is the busiest, then look at
        // every other hour to see if one of them beats it.
        int busiest = 0;
        for(int hour = 1; hour < hourCounts.length; hour++) {
            if(hourCounts[hour] > hourCounts[busiest]) {
                busiest = hour;       // found an hour with more accesses
            }
        }
        return busiest;
    }

    /**
     * Return the quietest hour of the day: the hour with the fewest
     * accesses. If two or more hours tie for the fewest accesses,
     * the earliest of them is returned. (Exercise 7.16)
     * @return The quietest hour, from 0 (midnight) to 23 (11pm).
     */
    public int quietestHour()
    {
        // Same idea as busiestHour, but we keep the SMALLER count.
        int quietest = 0;
        for(int hour = 1; hour < hourCounts.length; hour++) {
            if(hourCounts[hour] < hourCounts[quietest]) {
                quietest = hour;      // found an hour with fewer accesses
            }
        }
        return quietest;
    }

    /**
     * Return the first hour of the busiest two-hour period of the day.
     * There are 24 periods to check: 0-2 (midnight to 2am), 1-3 (1am
     * to 3am), ... 22-0 (10pm to midnight) and 23-1 (11pm to 1am,
     * which wraps around midnight). If several periods tie, the
     * earliest is returned. (Exercise 7.18)
     * @return The first hour (0 to 23) of the busiest two-hour period.
     */
    public int busiestTwoHour()
    {
        // Start by assuming the period that begins at hour 0
        // (hours 0 and 1 together) is the busiest.
        int busiestStart = 0;
        int busiestTotal = hourCounts[0] + hourCounts[1];

        for(int hour = 1; hour < hourCounts.length; hour++) {
            // The hour that follows this one. The % (remainder)
            // operator makes 23 + 1 roll over to 0, the same way
            // NumberDisplay rolled a clock over from 23 to 0, so
            // the last period pairs 11pm with midnight.
            int nextHour = (hour + 1) % hourCounts.length;
            int total = hourCounts[hour] + hourCounts[nextHour];
            if(total > busiestTotal) {
                busiestTotal = total;
                busiestStart = hour;  // found a busier two-hour period
            }
        }
        return busiestStart;
    }
}
