package link;

import java.util.ArrayList;
import java.util.List;

public class BingLinkGenerator implements LinkGenerator {
    @Override
    public List<String> generateLinks(String query, int lastPage) {
        return generateLinks(query, lastPage, 1);
    }

    @Override
    public List<String> generateLinks(String query, int lastPage, int firstPage) {
        List<String> links = new ArrayList<>();
        for (int i = firstPage; i <= lastPage; i++) {
            int first = i;
            if (first != 1) first = (first * 10) + 1;
            links.add("http://www.bing.com/search?q=" + query + "&first=" + first + "&FORM=PERE" + i);
        }
        return links;
    }
}
