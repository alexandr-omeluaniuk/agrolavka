package ss.agrolavka.constants;

/**
 * Site URLs.
 * @author alex
 */
public final class SiteUrls {
    
    private SiteUrls() {}
    
    public static final String URL_PUBLIC = "/api/agrolavka/public";
    public static final String URL_PROTECTED = "/api/agrolavka/protected";
    
    public static final String PAGE_HOME = "/";
    public static final String PAGE_CATALOG_ROOT = "/catalog";
    public static final String PAGE_CATALOG = PAGE_CATALOG_ROOT + "/**";
}
