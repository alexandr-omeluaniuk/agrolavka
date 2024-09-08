package ss.agrolavka.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.function.ThrowingSupplier;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.*;
import ss.entity.images.storage.EntityImage;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.core.dao.CoreDao;
import ss.martin.images.storage.api.ImagesStorageApi;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * My Sklad integration service implementation.
 * @author alex
 */
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
class MySkladIntegrationServiceImpl implements MySkladIntegrationService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MySkladIntegrationServiceImpl.class);
    
    private static final String URL_PRODUCT_GROUPS = "/entity/productfolder";
    private static final String URL_MODIFICATIONS = "/entity/variant";
    private static final String URL_PRODUCTS = "/entity/product?limit=%d&offset=%d";
    private static final String URL_PRODUCT_IMAGES = "/entity/product/%s/images";
    
    private static final String METHOD_GET = "GET";
    
    private static final String SITE_PRICE_TYPE = "Цена продажи";

    private static final Set<String> CHARACTERISTIC_NAMES_SKIP = new HashSet<>(
        Arrays.stream(new String[] {
            "на розлив",
            "развес"
        }).toList()
    );
    /** My sklad API endpoint. */
    @Value("${mysklad.api.url:https://api.moysklad.ru/api/remap/1.2}")
    private String rootUrl;
    /** Configuration. */
    @Autowired
    private AgrolavkaConfiguration configuration;
    /** Image service. */
    @Autowired
    private ImagesStorageApi imageService;
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** Authorization token. */
    private String token;
    
    @Override
    public List<ProductsGroup> getProductGroups() {
        String response = request(URL_PRODUCT_GROUPS, METHOD_GET, null);
        JSONObject json = new JSONObject(response);
        List<ProductsGroup> result = new ArrayList<>();
        if (json.has("rows")) {
            JSONArray rows = json.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject item = rows.getJSONObject(i);
                ProductsGroup productGroup = new ProductsGroup();
                productGroup.setExternalId(item.getString("id"));
                productGroup.setName(item.getString("name"));
                if (item.has("productFolder")) {
                    String link = item.getJSONObject("productFolder").getJSONObject("meta").getString("href");
                    productGroup.setParentId(link.substring(link.lastIndexOf("/") + 1));
                }
                if (item.has("description")) {
                    productGroup.setDescription(item.getString("description"));
                }
                LOG.debug(productGroup.toString());
                result.add(productGroup);
            }
        }
        LOG.debug("loaded product groups [" + result.size() + "]");
        return result;
    }
    
    @Override
    public List<Product> getProducts(int offset, int limit) {
        return ((ThrowingSupplier<List<Product>>) () -> {
            String response = request(String.format(URL_PRODUCTS, limit, offset), METHOD_GET, null);
            JSONObject json = new JSONObject(response);
            List<Product> result = new ArrayList<>();
            JSONArray rows = json.getJSONArray("rows");
            final var productGroupsMap = new HashMap<String, ProductsGroup>();
            List<ProductsGroup> groups = coreDAO.getAll(ProductsGroup.class);
            for (ProductsGroup group : groups) {
                productGroupsMap.put(group.getExternalId(), group);
            }
            for (int i = 0; i < rows.length(); i++) {
                JSONObject item = rows.getJSONObject(i);
                Product product = fromJSON(item, productGroupsMap);
                if (product.getGroup() != null) {
                    result.add(product);
                }
            }
            LOG.debug("loaded products [" + result.size() + "]");
            return result;
        }).get();
    }
    
    @Override
    public List<ProductVariant> getProductVariants() {
        final var response = request(URL_MODIFICATIONS, METHOD_GET, null);
        final var json = new JSONObject(response);
        final var result = new ArrayList<ProductVariant>();
        final var rows = json.getJSONArray("rows");
        for (int i = 0; i < rows.length(); i++) {
            final var item = rows.getJSONObject(i);
            if (item.has("product")) {
                final var variant = new ProductVariant();
                if (item.has("name")) {
                    variant.setExternalId(item.getString("id"));
                    variant.setName(item.getString("name"));
                    variant.setPrice(extractPriceValue(item));
                    final var productUrlParts = item.getJSONObject("product").getJSONObject("meta")
                        .getString("href").split("/");
                    variant.setParentId(productUrlParts[productUrlParts.length - 1]);
                    final var characteristics = item.getJSONArray("characteristics");
                    final var characteristicsNames = new ArrayList<String>();
                    boolean hidden = false;
                    for (int j = 0; j < characteristics.length(); j++) {
                        final var charObj = characteristics.getJSONObject(j);
                        if (CHARACTERISTIC_NAMES_SKIP.contains(charObj.getString("name"))) {
                            hidden = true;
                        }
                        characteristicsNames.add(charObj.getString("value"));
                    }
                    if (characteristicsNames.isEmpty()) {
                        continue;
                    }
                    variant.setHidden(hidden);
                    variant.setCharacteristics(characteristicsNames.stream().collect(Collectors.joining("::")));
                }
                result.add(variant);
            }
        }
        return result;
    }
    
    @Override
    public List<EntityImage> getProductImages(String productExternalId) {
        List<EntityImage> result = new ArrayList<>();
        String response = request(String.format(URL_PRODUCT_IMAGES, productExternalId), METHOD_GET, null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        JSONObject json = new JSONObject(response);
        if (json.has("rows")) {
            JSONArray rows = json.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject item = rows.getJSONObject(i);
                JSONObject meta = item.getJSONObject("meta");
                String href = meta.getString("downloadHref");
                EntityImage productImage = new EntityImage();
                productImage.setName(item.getString("filename"));
                productImage.setSize(item.getLong("size"));
                productImage.setData(requestData(href, "GET", headers));
                if (item.has("miniature")) {
                    JSONObject miniature = item.getJSONObject("miniature");
                    productImage.setType(miniature.getString("mediaType"));
                }
                result.add(productImage);
            }
        }
        return result;
    }
    
    @Override
    public void removeProductImages(Product product) {
        String response = request("/entity/product/" + product.getExternalId() + "/images", "GET", null);
        JSONObject json = new JSONObject(response);
        if (json.has("rows")) {
            JSONArray rows = json.getJSONArray("rows");
            if (rows.length() > 0) {
                request("/entity/product/" + product.getExternalId() + "/images/delete", "POST", rows.toString());
            }
        }
    }
    
    @Override
    public Product createProduct(Product product) {
        return ((ThrowingSupplier<Product>) () -> {
            String response = request(
                "/entity/product", "POST", 
                toMySkladJSON(product, getRetailPriceType()
            ).toString());
            return fromJSON(new JSONObject(response), null);
        }).get();
    }
    
    @Override
    public Product updateProduct(Product product) {
        return ((ThrowingSupplier<Product>) () -> {
            String response = request("/entity/product/" + product.getExternalId(), "PUT",
                    toMySkladJSON(product, getRetailPriceType()).toString());
            return fromJSON(new JSONObject(response), null);
        }).get();
    }
    
    @Override
    public void deleteProduct(Product product) {
        request("/entity/product/" + product.getExternalId(), "DELETE", null);
    }
    
    @Override
    public List<PriceType> getPriceTypes() {
        String response = request("/context/companysettings/pricetype", "GET", null);
        JSONArray arr = new JSONArray(response);
        List<PriceType> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject item = arr.getJSONObject(i);
            PriceType pt = new PriceType();
            pt.setExternalId(item.getString("id"));
            pt.setName(item.getString("name"));
            result.add(pt);
        }
        return result;
    }
    
    @Override
    public void attachImagesToProduct(Product product) {
        ((ThrowingRunnable) () -> {
            for (EntityImage image : product.getImages()) {
                JSONObject payload = new JSONObject();
                payload.put("filename", image.getName());
                payload.put("content", new String(Base64.getEncoder().encode(imageService.readImageFromDisk(image)), "UTF-8"));
                request("/entity/product/" + product.getExternalId() + "/images", "POST", payload.toString());
            }
        }).run();
    }
    
    @Override
    public String createProductsGroup(ProductsGroup group) {
        String response = request("/entity/productfolder", "POST", group.toMySkladJSON().toString());
        JSONObject json = new JSONObject(response);
        return json.getString("id");
    }
    
    @Override
    public void deleteProductsGroup(ProductsGroup group) {
        request("/entity/productfolder/" + group.getExternalId(), "DELETE", null);
    }
    
    @Override
    public void updateProductsGroup(ProductsGroup group) {
        request("/entity/productfolder/" + group.getExternalId(), "PUT", group.toMySkladJSON().toString());
    }
    
    @Override
    public Map<String, Product> getStock(int offset, int limit) throws Exception {
        String response = request("/report/stock/all?limit=" + limit + "&offset=" + offset, "GET", null);
        JSONObject json = new JSONObject(response);
        JSONArray rows = json.getJSONArray("rows");
        Map<String, Product> result = new HashMap<>();
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            Product product = new Product();
            product.setCode(row.getString("code"));
            product.setName(row.getString("name"));
            product.setQuantity(row.getDouble("quantity"));
            result.put(product.getCode(), product);
        }
        return result;
    }
    
    @Override
    public List<Discount> getDiscounts() {
        String response = request("/entity/discount", "GET", null);
        JSONObject json = new JSONObject(response);
        JSONArray rows = json.getJSONArray("rows");
        final var discounts = new ArrayList<Discount>();
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            if (row.getBoolean("active") && row.getBoolean("allAgents")
                    && !row.getBoolean("allProducts") && row.has("assortment") && row.has("discount")) {
                Discount discount = new Discount();
                discount.setExternalId(row.getString("id"));
                discount.setName(row.getString("name"));
                discount.setProducts(new ArrayList<>());
                discount.setDiscount(row.getDouble("discount"));
                JSONArray assortment = row.getJSONArray("assortment");
                for (int j = 0; j < assortment.length(); j++) {
                    JSONObject productData = assortment.getJSONObject(j).getJSONObject("meta");
                    Product product = new Product();
                    String link = productData.getString("href");
                    String productId = link.substring(link.lastIndexOf("/") + 1);
                    product.setExternalId(productId);
                    discount.getProducts().add(product);
                }
                discounts.add(discount);
            }
        }
        return discounts;
    }
    
    private JSONObject toMySkladJSON(Product product, PriceType priceType) {
        JSONObject json = new JSONObject();
        json.put("name", product.getName());
        json.put("article", product.getArticle());
        JSONArray salePrices = new JSONArray();
        JSONObject productPrice = new JSONObject();
        productPrice.put("value", Double.valueOf(product.getPrice() * 100).intValue());
        productPrice.put("priceType", toMySkladJSON(priceType));
        salePrices.put(productPrice);
        json.put("salePrices", salePrices);
//        JSONObject buyPriceJSON = new JSONObject();
//        buyPriceJSON.put("value", getBuyPrice() * 100);
//        json.put("buyPrice", buyPriceJSON);
        json.put("productFolder", product.getGroup().toMySkladJSONAsReference());
        json.put("description", product.getDescription() == null ? "" : product.getDescription());
        return json;
    }
    
    private PriceType getRetailPriceType() {
        return coreDAO.getAll(PriceType.class).stream()
            .filter(type -> SITE_PRICE_TYPE.equals(type.getName())).findFirst().orElseThrow();
    }
    
    private JSONObject toMySkladJSON(PriceType priceType) {
        JSONObject json = new JSONObject();
        JSONObject meta = new JSONObject();
        meta.put("href", "https://api.moysklad.ru/api/remap/1.2/context/companysettings/pricetype/"
                + priceType.getExternalId());
        meta.put("type", "pricetype");
        meta.put("mediaType", "application/json");
        json.put("meta", meta);
        return json;
    }
    
    /**
     * Request data from MySklad with predefined headers.
     * @param url URL.
     * @param method HTTP method.
     * @param payload payload.
     * @return response as string.
     * @throws Exception request error.
     */
    private String request(String url, String method, String payload) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            headers.put("Authorization", "Bearer " + token);
            return request(url, method, headers, payload);
        } catch (MySkladAuthenticationException authEx) {
            authentication();
            headers.put("Authorization", "Bearer " + token);
            return request(url, method, headers, payload);
        }
    }
    /**
     * Request data from MySklad.
     * @param url URL.
     * @param method HTTP method.
     * @param headers HTTP headers.
     * @param payload payload.
     * @return response as string.
     * @throws Exception error.
     */
    private String request(String url, String method, Map<String, String> headers, String payload) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("------------------------------ REQUEST TO MY SKLAD -----------------------------------------------");
            LOG.debug("url [" + url + "]");
            LOG.debug("method [" + method + "]");
            LOG.debug("payload [" + payload + "]");
        }
        return ((ThrowingSupplier<String>) () -> {
            final var maxRouteFails = 100;
            var attempt = 0;
            while (attempt < maxRouteFails) {
                try {
                    return unsafeRequest(url, method, headers, payload);
                } catch (NoRouteToHostException ex) {
                    LOG.info("No route to host[" + url + "], attempt [" + attempt + "]");
                    attempt++;
                }
            }
            throw new MySkladInternalErrorException("No route to host");
        }).get();
    }

    private String unsafeRequest(String url, String method, Map<String, String> headers, String payload)
        throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(rootUrl + url).openConnection();
        connection.setRequestMethod(method);
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(30)).intValue());
        for (String header : headers.keySet()) {
            connection.setRequestProperty(header, headers.get(header));
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (payload != null) {
            connection.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        }
        int responseCode = connection.getResponseCode();
        LOG.debug("response code [" + responseCode + "]");
        final var contentEncoding = connection.getHeaderField("Content-Encoding");
        LOG.debug("Content-Encoding: " + contentEncoding);
        String response = null;
        if (responseCode == 200 || responseCode == 201) {
            final var is = "gzip".equals(contentEncoding) ? new GZIPInputStream(connection.getInputStream()) : connection.getInputStream();
            response = inputStreamToString(is);
            LOG.debug("response: " + response);
            return response;
        } else if (responseCode == 401) {
            LOG.info("MySklad auth error [" + url + "]: " + inputStreamToString(connection.getErrorStream()));
            throw new MySkladAuthenticationException();
        } else {
            response = inputStreamToString(connection.getErrorStream());
            throw new MySkladInternalErrorException(response);
        }
    }
    /**
     * Request data.
     * @param url URL.
     * @param method HTTP method.
     * @param headers headers.
     * @return response as byte array.
     * @throws Exception request error. 
     */
    private byte[] requestData(String url, String method, Map<String, String> headers) {
        LOG.debug("------------------------------ DATA REQUEST ------------------------------------------------------");
        LOG.debug("url [" + url + "]");
        LOG.debug("method [" + method + "]");
        return ((ThrowingSupplier<byte[]>) () -> {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            connection.addRequestProperty("Accept-Encoding", "gzip");
            connection.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(30)).intValue());
            for (String header : headers.keySet()) {
                connection.setRequestProperty(header, headers.get(header));
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            int responseCode = connection.getResponseCode();
            LOG.debug("response code [" + responseCode + "]");
            final var contentEncoding = connection.getHeaderField("Content-Encoding");
            LOG.debug("Content-Encoding: " + contentEncoding);
            if (responseCode == 200 || responseCode == 201) {
                final var is = "gzip".equals(contentEncoding) ? new GZIPInputStream(connection.getInputStream()) : connection.getInputStream();
                return inputStreamToByteArray(is);
            } else {
                throw new IOException(inputStreamToString(connection.getErrorStream()));
            }
        }).get();
    }
    /**
     * Convert input stream to UTF-8 string.
     * @param is input stream.
     * @return UTF-8 string.
     * @throws Exception error.
     */
    private String inputStreamToString(InputStream is) throws Exception {
        int len;
        byte[] buff = new byte[1024 * 16];
        String result;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            is.close();
            result = new String(baos.toByteArray(), "UTF-8");
        }
        return result;
    }
    /**
     * Convert input stream to byte array.
     * @param is input stream.
     * @return byte array.
     * @throws Exception error.
     */
    private byte[] inputStreamToByteArray(InputStream is) throws Exception {
        int len;
        byte[] buff = new byte[1024 * 16];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        is.close();
        byte[] data = baos.toByteArray();
        baos.close();
        return data;
    }
    /**
     * Create product from JSON.
     * @param item json object.
     * @param productGroupsMap product groups map.
     * @return product model.
     */
    private Product fromJSON(JSONObject item, Map<String, ProductsGroup> productGroupsMap) throws Exception {
        Product product = new Product();
        product.setExternalId(item.getString("id"));
        product.setName(item.getString("name"));
        if (item.has("article")) {
            product.setArticle(item.getString("article"));
        }
        if (item.has("updated")) {
            String dateStr = item.getString("updated");
            product.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr.split("\\.")[0]));
        }
        if (item.has("code")) {
            product.setCode(item.getString("code"));
        }
        if (item.has("description")) {
            product.setDescription(item.getString("description"));
        }
        if (item.has("productFolder")) {
            String link = item.getJSONObject("productFolder").getJSONObject("meta").getString("href");
            String productGroupId = link.substring(link.lastIndexOf("/") + 1);
            if (productGroupsMap != null) {
                product.setGroup(productGroupsMap.get(productGroupId));
            }
        }
        product.setPrice(extractPriceValue(item));
        if (item.has("buyPrice")) {
            JSONObject buyPrice = item.getJSONObject("buyPrice");
            product.setBuyPrice(buyPrice.getDouble("value") / 100);
        }
        if (product.getBuyPrice() == null) {
            product.setBuyPrice(0d);
        }
        LOG.debug(product.toString());
        return product;
    }
    /**
     * Authentication.
     * @throws Exception error.
     */
    private void authentication() {
        ((ThrowingRunnable) () -> {
            Map<String, String> headers = new HashMap<>();
            String credentials = configuration.mySkladUsername() + ":" + configuration.mySkladPassword();
            credentials = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
            LOG.debug("credentials: " + credentials);
            headers.put("Authorization", "Basic " + credentials);
            String tokenResponse = request("/security/token", "POST", headers, null);
            LOG.debug("security token: " + tokenResponse);
            JSONObject json = new JSONObject(tokenResponse);
            token = json.getString("access_token");
            LOG.debug("new acquired token: " + token);
        }).run();
    }
    
    private Double extractPriceValue(JSONObject item) {
        if (item.has("salePrices")) {
            JSONArray prices = item.getJSONArray("salePrices");
            for (int j = 0; j < prices.length(); j++) {
                JSONObject price = prices.getJSONObject(j);
                if (SITE_PRICE_TYPE.equals(price.getJSONObject("priceType").getString("name"))) {
                    return price.getDouble("value") / 100;
                }
            }
        }
        return null;
    }
    
    /**
     * MySklad authentication error.
     */
    private static class MySkladAuthenticationException extends RuntimeException {
    }
    /**
     * MySklad internal error.
     */
    private static class MySkladInternalErrorException extends RuntimeException {
        /**
         * Constructor.
         * @param msg message.
         */
        public MySkladInternalErrorException(String msg) {
            super(msg);
        }
    }
}
