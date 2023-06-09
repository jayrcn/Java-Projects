package ngordnet.ngrams;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Collection;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    private HashMap<String, TimeSeries> allWords;
    private TimeSeries wordCnts;

    public NGramMap(String wordsFilename, String countsFilename) {
        allWords = new HashMap();
        wordCnts = new TimeSeries();
        String line1 = "";

        In in1 = new In(wordsFilename);
        while ((line1 = in1.readLine()) != null) {
            String[] row = line1.split("\\t");
            int yrVals = Integer.parseInt(row[1]);
            double cntVals = Double.parseDouble(row[2]);
            if (!allWords.containsKey(row[0])) {
                TimeSeries newWord = new TimeSeries();
                newWord.put(yrVals, cntVals);
                allWords.put(row[0], newWord);
            } else {
                allWords.get(row[0]).put(yrVals, cntVals);
            }
        }

        String line2 = "";
        In in2 = new In(countsFilename);
        while ((line2 = in2.readLine()) != null) {
            String[] row = line2.split(",");
            int yCountVal = Integer.parseInt(row[0]);
            double tCountVal = Double.parseDouble(row[1]);
            wordCnts.put(yCountVal, tCountVal);
        }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries cntHistoire = new TimeSeries();
        for (int keyVal : allWords.get(word).keySet()) {
            cntHistoire.put(keyVal, allWords.get(word).get(keyVal));
        }
        return cntHistoire;
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     * changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries cntHistoireLmt = new TimeSeries(allWords.get(word), startYear, endYear);
        return cntHistoireLmt;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries ttlCntHistoire = new TimeSeries();
        for (int key : wordCnts.keySet()) {
            ttlCntHistoire.put(key, wordCnts.get(key));
        }
        return ttlCntHistoire;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries wgtHistoire = allWords.get(word).dividedBy(wordCnts);
        return wgtHistoire;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wgtHistoire2 = new TimeSeries(allWords.get(word).dividedBy(wordCnts), startYear, endYear);
        return wgtHistoire2;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries begin = new TimeSeries();
        for (String word : words) {
            begin = begin.plus(this.weightHistory(word));
        }
        return begin;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries begin2 = new TimeSeries(new TimeSeries(), startYear, endYear);
        for (String word : words) {
            begin2 = begin2.plus(this.weightHistory(word, startYear, endYear));
        }
        return begin2;
    }

}
