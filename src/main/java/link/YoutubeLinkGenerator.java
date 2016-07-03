package link;

import java.util.ArrayList;
import java.util.List;

public class YoutubeLinkGenerator implements LinkGenerator {
    @Override
    public List<String> generateLinks(String query, int lastPage) {
        return generateLinks(query, lastPage, 1);
    }

    @Override
    public List<String> generateLinks(String query, int lastPage, int firstPage) {
        List<String> links = new ArrayList<>();
        for (int i = 1; i <= lastPage; i++) {
            links.add("https://www.youtube.com/results?q=" + query + "&p=" + i);
        }
        return links;
    }
}
