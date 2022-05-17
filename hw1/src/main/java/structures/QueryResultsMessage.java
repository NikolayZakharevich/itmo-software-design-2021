package structures;

import java.util.List;

public class QueryResultsMessage {
    private final String searcherName;
    private final List<UrlWithTitle> responses;

    public QueryResultsMessage(String searcherName, List<UrlWithTitle> responses) {
        this.searcherName = searcherName;
        this.responses = responses;
    }

    public String getSearcherName() {
        return searcherName;
    }

    public List<UrlWithTitle> getResponses() {
        return responses;
    }

}
