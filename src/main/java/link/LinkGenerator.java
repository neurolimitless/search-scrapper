package link;

import java.util.List;

public interface LinkGenerator {
    List<String> generateLinks(String query, int lastPage);

    List<String> generateLinks(String query, int lastPage, int firstPage);
}
