package ss.agrolavka.constants;

/**
 * Agrolavka constants.
 * @author alex
 */
public final class SiteConstants {
    
    private SiteConstants() {
    }
    
    /** Columns. */
    public static final int SEARCH_RESULT_TILES_COLUMNS = 4;
    /** Rows. */
    public static final int SEARCH_RESULT_TILES_ROWS = 6;
    /** Cart session attribute. */
    public static final String CART_SESSION_ATTRIBUTE = "CART";
    /** Firebase topic: Agrolavka orders. */
    public static final String FIREBASE_TOPIC_ORDER_CREATED = "agrolavka_order_created";
    /** Image thumb size. */
    public static final int IMAGE_THUMB_SIZE = 306;
    /** Shop image thumb size. */
    public static final int IMAGE_SHOP_THUMB_SIZE = 459;
    /** Min order sum. */
    public static final int MIN_ORDER_SUM = 0;
    
    public static final String URL_PUBLIC = "/api/agrolavka/public";
    
    public static final String URL_PROTECTED = "/api/agrolavka/protected";
}
