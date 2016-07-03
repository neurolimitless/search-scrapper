package scrapper;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

public class BasicScrapper extends Scrapper {

    private final int TIME_OUT = 10000;

    public BasicScrapper loadPage(String url) {
        try {
            System.out.println(this + " connecting.");
            page = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIME_OUT).get();
            System.out.println(this + " page is parsed.");
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return this;
    }


    public static void main(String[] args) {
        Scrapper scrapper = new BasicScrapper();
        scrapper.loadPage("https://www.youtube.com/results?sp=SFDqAwA%253D&q=Links");
        List<String> list = scrapper.getLinksFromPage();
        for (String s : list) {
            System.out.println(s);
        }
    }

}
