package ss.agrolavka.lucene;

import org.apache.lucene.document.Document;

import java.util.List;

public class LuceneSearchResult {

    public String term;

    public List<Document> documents;

    public LuceneSearchResult(List<Document> documents, String term) {
        this.term = term;
        this.documents = documents;
    }
}
