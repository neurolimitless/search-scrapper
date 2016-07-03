package controller;

import link.LinkGenerator;
import link.LinkGeneratorFactory;
import scrapper.Executor;
import scrapper.ScrapperMode;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

public class Controller {

    private static Controller controller;
    private ProxyProvider provider;

    protected Controller() {
        provider = ProxyProvider.getInstance();
    }

    public static Controller getInstance() {
        if (controller == null) controller = new Controller();
        return controller;
    }

    public int getCountOfProxies() {
        return provider.getProxies().size();
    }

    public void shuffleProxies() {
        Collections.shuffle(provider.getProxies());
    }

    public List<String> launch(String query, String engine, String pages, boolean proxy) {
        Executor executor;
        List<String> links;
        if (proxy) executor = new Executor(ScrapperMode.PROXY);
        else executor = new Executor(ScrapperMode.BASIC);
        LinkGenerator linkGenerator = LinkGeneratorFactory.createLinkGenerator(engine);
        int[] pagesCount = parsePageCount(pages);
        if (pagesCount[1] == 0) links = linkGenerator.generateLinks(query, pagesCount[0]);
        else links = linkGenerator.generateLinks(query, pagesCount[1], pagesCount[0]);
        return executor.parseLinks(links);
    }

    private int[] parsePageCount(String text) {
        int[] result = new int[2];
        if (text.contains("-")) {
            String[] parsedCount = text.split("-");
            for (String s : parsedCount) {
                System.out.println(s);
            }
            result[0] = Integer.parseInt(parsedCount[0]);
            result[1] = Integer.parseInt(parsedCount[1]);
            if (result[1] < result[0]) throw new IllegalArgumentException("First number must be less than second.");
        } else result[0] = Integer.parseInt(text);
        return result;
    }

    public void saveToFile(List<String> links, File file, boolean asTxt) {
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file, true)) {
                for (String link : links) {
                    if (asTxt) fileWriter.write(link + "\n");
                    else fileWriter.write(link+",\n");
                }
            } catch (Exception e) {
                System.out.println("Saving unsuccessful");
            }
        }
    }
}
