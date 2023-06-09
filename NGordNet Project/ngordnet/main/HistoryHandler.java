package ngordnet.main;

import ngordnet.ngrams.TimeSeries;
import ngordnet.ngrams.NGramMap;
import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.List;
import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap start;

    public HistoryHandler(NGramMap map) {
        start = map;
    }

    public String handle(NgordnetQuery q) {
        List<String> wordList = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        ArrayList<TimeSeries> lts = new ArrayList<>();
        for (String word : wordList) {
            lts.add(start.weightHistory(word, startYear, endYear));
        }
        XYChart chart = Plotter.generateTimeSeriesChart(wordList, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);
        return encodedImage;
    }
}
