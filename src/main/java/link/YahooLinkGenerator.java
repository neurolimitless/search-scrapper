package link;

import java.util.ArrayList;
import java.util.List;

public class YahooLinkGenerator implements LinkGenerator {
    @Override
    public List<String> generateLinks(String query, int lastPage) {
        return generateLinks(query, lastPage, 1);
    }

    @Override
    public List<String> generateLinks(String query, int lastPage, int firstPage) {
        List<String> links = new ArrayList<>();
        for (int i = firstPage - 1; i < lastPage; i++) {
            int b = i;
            if (b != 0) b = b * 10 + 1;
            else b++;
            links.add("https://search.yahoo.com/search;?p=" + query + "&b=" + b + "&pz=10");
        }
        return links;
    }
}
