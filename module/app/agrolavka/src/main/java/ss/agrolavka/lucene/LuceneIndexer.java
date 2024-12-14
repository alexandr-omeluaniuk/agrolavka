package ss.agrolavka.lucene;

import jakarta.annotation.PostConstruct;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.entity.agrolavka.Product;
import ss.martin.base.lang.ThrowingSupplier;
import ss.martin.core.dao.CoreDao;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LuceneIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexer.class);

    private static final int QUICK_SEARCH_PRODUCTS_MAX = 100;

    @Value("${lucene.index.dir:${user.home}/lucene}")
    private String indexDir;

    private IndexWriter writer = null;

    private IndexReader reader = null;

    @Autowired
    private CoreDao coreDao;

    private final Map<Character, Character> langMap = initLangMap();

    @PostConstruct
    private void init() {
        try {
            final var dirPath = Paths.get(indexDir);
            Boolean isFirstInit = false;
            if (!dirPath.toFile().exists()) {
                dirPath.toFile().mkdirs();
                isFirstInit = true;
            }
            Directory dir = FSDirectory.open(dirPath);
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            if (isFirstInit) {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }
            writer = new IndexWriter(dir, iwc);
            try {
                reader = DirectoryReader.open(dir);
            } catch (Exception ee) {
                LOG.warn("Init lucene reader fail", ee);
            }
            LOG.info("Lucene dir path [" + dirPath + "]");
        } catch (Exception e) {
            LOG.error("Lucene initialization failed", e);
        }
    }

    public void refreshIndex() {
        coreDao.getAll(Product.class).forEach(this::addToIndex);
    }

    public LuceneSearchResult search(String text) {
        if (reader == null) {
            return new LuceneSearchResult(new ArrayList<>(), "");
        }
        final var result1 = doSearch(text.toLowerCase());
        if (text.length() < 3) {
            return new LuceneSearchResult(result1, text);
        } else {
            final var lang2Text = inverseLanguage(text);
            final var result2 = doSearch(lang2Text);
            return result1.size() >= result2.size()
                ? new LuceneSearchResult(result1, text) : new LuceneSearchResult(result2, lang2Text);
        }
    }

    private List<Document> doSearch(String langText) {
        return ((ThrowingSupplier<List<Document>>) () -> {
            final var boolQueryBuilder = new BooleanQuery.Builder();
            Arrays.stream(langText.split(" ")).forEach(token -> {
                final var boolQueryBuilderInner = new BooleanQuery.Builder();
                final var fuzzyQuery = new FuzzyQuery(new Term("name", token));
                final var fuzzyQuery2 = new FuzzyQuery(new Term("name", token + "*"));
                boolQueryBuilderInner.add(
                    new BooleanClause(fuzzyQuery, BooleanClause.Occur.SHOULD)
                ).add(
                    new BooleanClause(fuzzyQuery2, BooleanClause.Occur.SHOULD)
                );
                boolQueryBuilder.add(
                    new BooleanClause(boolQueryBuilderInner.build(), BooleanClause.Occur.MUST)
                );
            });
            final var boolQuery = boolQueryBuilder.build();

            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs topDocs = searcher.search(boolQuery, QUICK_SEARCH_PRODUCTS_MAX);
            final var docs = Arrays.stream(topDocs.scoreDocs).toList().stream()
                .map(scoreDoc -> ((ThrowingSupplier<Document>) () -> {
                    return searcher.doc(scoreDoc.doc);
                }).get())
                .toList();
            return docs;
        }).get();
    }

    private void addToIndex(Product product) {
        try {
            final var term = new Term("id", product.getId().toString());
            final var doc = new Document();
            doc.add(new LongField("id", product.getId(), Field.Store.YES));
            doc.add(new TextField("name", product.getName(), Field.Store.NO));
            writer.updateDocument(term, doc);
            writer.commit();
            LOG.info("Product indexed [" + product.getId() +"]");
        } catch (Exception e) {
            LOG.error("Document can not be added to index", e);
        }
    }

    private String inverseLanguage(String origin) {
        final var charArr = origin.toLowerCase().chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        final var isEnglish = Stream.of(charArr).filter(langMap::containsKey).toList().size() > 2;
        if (isEnglish) {
            return Stream.of(charArr).map(ch -> langMap.getOrDefault(ch, ch).toString())
                .collect(Collectors.joining(""));
        } else {
            final var langMapInvert = langMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            return Stream.of(charArr).map(ch -> langMapInvert.getOrDefault(ch, ch).toString())
                .collect(Collectors.joining(""));
        }
    }

    private Map<Character, Character> initLangMap() {
        final var langMap = new HashMap<Character, Character>();
        langMap.put('q', 'й');
        langMap.put('w', 'ц');
        langMap.put('e', 'у');
        langMap.put('r', 'к');
        langMap.put('t', 'е');
        langMap.put('y', 'н');
        langMap.put('u', 'г');
        langMap.put('i', 'ш');
        langMap.put('o', 'щ');
        langMap.put('p', 'з');
        langMap.put('[', 'х');
        langMap.put(']', 'ъ');
        langMap.put('a', 'ф');
        langMap.put('s', 'ы');
        langMap.put('d', 'в');
        langMap.put('f', 'а');
        langMap.put('g', 'п');
        langMap.put('h', 'р');
        langMap.put('j', 'о');
        langMap.put('k', 'л');
        langMap.put('l', 'д');
        langMap.put(';', 'ж');
        langMap.put('\'', 'э');
        langMap.put('z', 'я');
        langMap.put('x', 'ч');
        langMap.put('c', 'с');
        langMap.put('v', 'м');
        langMap.put('b', 'и');
        langMap.put('n', 'т');
        langMap.put('m', 'ь');
        langMap.put(',', 'б');
        langMap.put('.', 'ю');
        langMap.put('/', '.');
        return langMap;
    }
}
