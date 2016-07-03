package scrapper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class Scrapper {

    protected final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
    protected Document page;

    public abstract Scrapper loadPage(String url);

    public List<String> getLinksFromPage() {
        List<String> listOfLinks = new ArrayList<>();
        if (page != null) {
            Elements links = page.getElementsByAttribute("href");
            for (Element link : links) {
                listOfLinks.add(link.attr("href"));
            }
        } else System.out.println(this + " page is null.");
        return listOfLinks;
    }
}
