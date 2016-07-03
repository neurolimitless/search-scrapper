package link;

import java.util.ArrayList;
import java.util.List;

public class GoogleLinkGenerator implements LinkGenerator {
    public List<String> generateLinks(String query, int lastPage) {
        return generateLinks(query, lastPage, 1);
    }

    public List<String> generateLinks(String query, int lastPage, int firstPage) {
        List<String> links = new ArrayList<>();
        for (int i = firstPage * 10; i <= lastPage * 10; i += 10) {
            links.add("http://google.com/search?q=" + query + "&start=" + i);
        }
        return links;
    }
}
