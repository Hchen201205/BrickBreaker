import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.IllegalFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ScoreRecorder is the class implementing I/O. It has a responsibility both as:
 * 1. a reader, which will read the scores and the score keeper of each of the scores to a 
 * Map<Integer, List<String>> data structure. The reason why we need List<String> instead of 
 * Set<String> is because we want the name to be listed in alphabetical order. Also, we have a 
 * ranking that is a List of integer based on the keyset of the map. Ranking is sorted in descending
 * order
 * 2. a writer, which will take in a map and will write it into the file.
 * @author garychen
 *
 */
public class ScoreRecorder {

    // The format for each of the record, which is in the format of "int string" for a line
    // The format allows at most four digit of number because the maximum score possible in this 
    // game is 40 * 20 * 10 = 8000
    public static final String RECORD_REGEX = "^[0-9]{1,4}\\s" + GameCourt.USERNAME_REGEX + "$";
    private String filePath; // the path of the file in string
    private BufferedReader br;
    // An integer list representing the ranking of all the scores from largest to the smallest
    private List<Integer> ranking;
    // records contains the scores as the key and a list of user name sorted in alphabetical order 
    // as the entry
    private Map<Integer, List<String>> records;
    
    /**
     * The constructor will take in the path of the file storing and will generate a bufferedreader
     * that will read all the scores
     * @param filePath
     */
    public ScoreRecorder(String filePath) {
        this.filePath = filePath;
        ranking = new LinkedList<Integer>();
        records = new TreeMap<Integer, List<String>>();
        try {
            br = new BufferedReader(new FileReader(filePath));
            readScores();
        } catch (NullPointerException | FileNotFoundException e) {
            System.err.println("Failed to read file");
        }
    }
    
    /**
     * A helper method which will read all the scores into both ranking and records
     */
    private void readScores() {
        // Indicator variable
        String next;
        try {
            next = br.readLine();
            while (next != null) {
                parseToRecord(next); // Parse into an acceptable format
                next = br.readLine();
            }
            br.close();
            ranking.addAll(records.keySet()); // Add all the integers into ranking
            // Sort the ranking from highest to lowest
            Collections.sort(ranking, Collections.reverseOrder());
        } catch (IOException e) {
            System.err.println("Unable to read the file");
        }

    }

    /**
     * A helper method which will parse the record, which is given as a string, to the map
     * If the format of the record is not valid,the helper method will abandone the record.
     * @param line
     */
    private void parseToRecord(String line) {
        // Check if the format is correct. It requires both following a Record_REGEX and to check if
        // the string only has int
        if (line.matches(RECORD_REGEX) && !line.matches("-\\d+")) { 
            String[] sl = line.split(" "); // split the string into supposedly array of length 2
            try {
                int score = Integer.parseInt(sl[0]);
                if (score < 8000) { // If the score is over the limit, which is not possible
                    if (records.containsKey(score)) {
                        List<String> l = records.get(score);
                        l.add(sl[1]);
                        Collections.sort(l); // Sort the name in alphabetical order
                    } else {
                        List<String> l = new LinkedList<String>();
                        l.add(sl[1]);
                        records.put(score, l); // Create and store a new list with only one element
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Wrong format");
            }
        }
    }
    
    /**
     * a getter method that will return a copy of the records
     * @return
     */
    public Map<Integer, List<String>> getRecords() {
        Map<Integer, List<String>> result = new TreeMap<Integer, List<String>>();
        for (Integer i: records.keySet()) {
            result.put((int) i, new LinkedList<String>(records.get(i)));
        }
        result.putAll(records);
        return result;
    }
    
    /**
     * A getter method that will return a copy of the ranking in sorted order
     * @return
     */
    public List<Integer> gerRanking() {
        return new LinkedList<Integer>(ranking);
    }
    
    /**
     * A method that takes in a records like data structure and will write it into the file in the
     * form "int string"
     * @param r is a data structure like the records defined above
     */
    public void writeRecords(Map<Integer, List<String>> r) {
        File file = Paths.get(filePath).toFile();
        try {
            // Overwrite the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (Integer i : r.keySet()) {
                if (i != null) {
                    for (String s : r.get(i)) {
                        if (s != null) {
                            bw.write(String.format("%d %s\n", i, s)); // Write in specific format
                        }
                    }
                }
            }
            bw.close();
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (IllegalFormatException e) {
            System.err.println("Wrong format");
        } catch (NullPointerException e) {
            System.err.println("Map is null");
        }
    }
}