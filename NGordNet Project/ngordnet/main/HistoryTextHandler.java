package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap start;

    public HistoryTextHandler(NGramMap map) {
        start = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String reply = "";

        for (String word : words) {
            TimeSeries wrdVals = new TimeSeries(start.weightHistory(word), startYear, endYear);
            reply += word + ": " + wrdVals.toString() + "\n";
        }
        return reply;
    }
}
