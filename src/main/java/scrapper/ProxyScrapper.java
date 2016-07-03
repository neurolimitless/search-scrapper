package scrapper;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ProxyScrapper extends Scrapper {

    private String proxyIp;
    private int proxyPort;

    public ProxyScrapper(String proxyIp, int proxyPort) {
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
    }

    private URLConnection createProxyConnection(String url) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection(proxy);
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlConnection;
    }

    public ProxyScrapper loadPage(String url) {
        System.out.println(this + " connecting via proxy: " + proxyIp + ":" + proxyPort);
        URLConnection connection = createProxyConnection(url);
        System.out.println(this + " connecting.");
        page = Jsoup.parse(String.valueOf(readPage(connection)));
        System.out.println(this + " page is parsed.");
        return this;
    }

    private String readPage(URLConnection urlConnection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line = null;
            StringBuilder tmp = new StringBuilder();
            System.out.println(this + " parsing page.");
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
            return tmp.toString();
        } catch (Exception e) {
            if (e.getMessage().equals("Connection reset"))
                System.out.println("Connection reset. Proxy: " + proxyIp + ":" + proxyPort);
            System.out.println(e.getLocalizedMessage());
            return "";
        }
    }

}
