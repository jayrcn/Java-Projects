package ngordnet.ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int keyVal : ts.keySet()) {
            if (startYear <= keyVal && keyVal <= endYear) {
                this.put(keyVal, ts.get(keyVal));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> yearList = new ArrayList<>();
        for (int keyVal : this.keySet()) {
            yearList.add(keyVal);
        }
        return yearList;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> dataList = new ArrayList<>();
        for (int keyVal : this.keySet()) {
            dataList.add(this.get(keyVal));
        }
        return dataList;
    }

    /**
     * Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries total = new TimeSeries();
        ArrayList<Integer> totalYears = new ArrayList<>();
        for (int keyVal : this.keySet()) {
            totalYears.add(keyVal);
        }
        for (int keyVal2 : ts.keySet()) {
            if (!totalYears.contains(keyVal2)) {
                totalYears.add(keyVal2);
            }
        }
        for (int keyVal3 : totalYears) {
            if (this.keySet().contains(keyVal3) && ts.keySet().contains(keyVal3)) {
                total.put(keyVal3, this.get(keyVal3) + ts.get(keyVal3));
            } else if (this.keySet().contains(keyVal3) && !ts.keySet().contains(keyVal3)) {
                total.put(keyVal3, this.get(keyVal3));
            } else if (!this.keySet().contains(keyVal3) && ts.keySet().contains(keyVal3)) {
                total.put(keyVal3, ts.get(keyVal3));
            }
        }
        return total;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
     * throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
     * Should return a new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries total = new TimeSeries();
        ArrayList<Integer> totalYears = new ArrayList<>();
        for (int keyVal : this.keySet()) {
            totalYears.add(keyVal);
        }
        for (int keyVal2 : ts.keySet()) {
            if (!totalYears.contains(keyVal2)) {
                totalYears.add(keyVal2);
            }
        }
        for (int keyVal3 : totalYears) {
            if (this.keySet().contains(keyVal3) && ts.keySet().contains(keyVal3)) {
                total.put(keyVal3, this.get(keyVal3) / ts.get(keyVal3));
            } else if (this.keySet().contains(keyVal3) && !ts.keySet().contains(keyVal3)) {
                throw new IllegalArgumentException("TS missing value");
            }

        }
        return total;
    }
}
