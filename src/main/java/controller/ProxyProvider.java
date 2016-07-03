package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProxyProvider {

    private static ProxyProvider proxyProvider;
    private File file;
    private List<String> proxies;

    private ProxyProvider(String fileName) {
        file = new File(fileName);
        proxies = new ArrayList<>();
        loadProxies();
    }

    private ProxyProvider() {
    }

    public static ProxyProvider getInstance() {
        if (proxyProvider == null) {
            proxyProvider = new ProxyProvider("proxy.txt");
        }
        return proxyProvider;
    }

    public List<String> getProxies(){
        return proxies;
    }

    private void loadProxies() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                proxies.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Missing proxy.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext() {
        return proxies.size() > 0;
    }

    public String getProxy() {
        String proxy = null;
        if (proxies.size() != 0) {
            proxy = proxies.get(proxies.size() - 1);
            proxies.remove(proxy);
        }
        return proxy;
    }
}
