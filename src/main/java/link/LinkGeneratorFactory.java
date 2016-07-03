package link;

public class LinkGeneratorFactory {
    public static LinkGenerator createLinkGenerator(String searchEngine) {
        if (searchEngine.equals("Amazon")) return new AmazonLinkGenerator();
        else if (searchEngine.equals("Google")) return new GoogleLinkGenerator();
        else if (searchEngine.equals("Yahoo")) return new YahooLinkGenerator();
        else if (searchEngine.equals("Bing")) return new BingLinkGenerator();
        else if (searchEngine.equals("Youtube")) return new YoutubeLinkGenerator();
        else throw new IllegalArgumentException("No such search engine.");
    }
}
