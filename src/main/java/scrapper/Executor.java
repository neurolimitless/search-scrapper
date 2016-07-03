package scrapper;

import controller.ProxyProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Executor {

    private String domain;
    private ScrapperMode scrapperMode;
    private ExecutorService executorService;
    private List<String> scrappingResult;

    public Executor(ScrapperMode scrapperMode) {
        this.scrapperMode = scrapperMode;
        this.executorService = Executors.newFixedThreadPool(6);
    }

    public Executor(ScrapperMode scrapperMode, int threads) {
        this.scrapperMode = scrapperMode;
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    private List<Callable<List<String>>> createTasks(List<String> links) throws MalformedURLException {
        List<Callable<List<String>>> tasks = new ArrayList<>();
        domain = new URL(links.get(0)).getHost();
        for (String link : links) {
            System.out.println(link);
            if (scrapperMode == ScrapperMode.BASIC) {
                Scrapper scrapper = new BasicScrapper();
                tasks.add(createScrapperTask(link, scrapper));
            } else if (scrapperMode == ScrapperMode.PROXY) {
                ProxyProvider proxyProvider = ProxyProvider.getInstance();
                if (proxyProvider.hasNext()) {
                    String[] proxy = proxyProvider.getProxy().split(":");
                    Scrapper scrapper = new ProxyScrapper(proxy[0], Integer.parseInt(proxy[1]));
                    tasks.add(createScrapperTask(link, scrapper));
                }
            }
        }
        return tasks;
    }

    private void parseResults(List<Future<List<String>>> results) throws ExecutionException, InterruptedException {
        for (Future<List<String>> result : results) {
            List<String> receivedLinks = result.get();
            for (String receivedLink : receivedLinks) {
                String formattedLink = formatLink(receivedLink.intern());
                if (!formattedLink.equals("")) scrappingResult.add(formattedLink);
            }
        }
    }

    private String formatLink(String link) {
        if (link.equals("#")) return "";
        else if (link.substring(0, 2).equals("//")) return "";
        else if (link.length()>10 && link.substring(0, 10).equals("javascript")) return "";
        else if (link.substring(0, 1).equals("/")) return "http://" + domain + link;
        else return link;
    }

    public List<String> parseLinks(List<String> links) {
        scrappingResult = new ArrayList<>();
        System.out.println("Selected mode " + scrapperMode);
        try {
            parseResults(executorService.invokeAll(createTasks(links)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return scrappingResult;
    }

    private Callable<List<String>> createScrapperTask(String url, Scrapper scrapper) {
        return () -> scrapper.loadPage(url).getLinksFromPage();
    }
}
