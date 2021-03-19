/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.PriceType;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;

/**
 * My Sklad integration service implementation.
 * @author alex
 */
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
class MySkladIntegrationServiceImpl implements MySkladIntegrationService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(MySkladIntegrationServiceImpl.class);
    /** My sklad API endpoint. */
    private static final String API_ENDPOINT = "https://online.moysklad.ru/api/remap/1.2";
    /** Configuration. */
    @Autowired
    private AgrolavkaConfiguration configuration;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Authorization token. */
    private String token;
    @Override
    public List<ProductsGroup> getProductGroups() throws Exception {
        String response = request("/entity/productfolder", "GET", null);
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
                LOG.debug(productGroup.toString());
                result.add(productGroup);
            }
        }
        LOG.debug("loaded product groups [" + result.size() + "]");
        return result;
    }
    @Override
    public List<Product> getProducts(int offset, int limit) throws Exception {
        String response = request("/entity/product?limit=" + limit + "&offset=" + offset, "GET", null);
        JSONObject json = new JSONObject(response);
        List<Product> result = new ArrayList<>();
        JSONArray rows = json.getJSONArray("rows");
        Map<String, ProductsGroup> productGroupsMap = new HashMap();
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
    }
    @Override
    public List<EntityImage> getProductImages(String productExternalId) throws Exception {
        List<EntityImage> result = new ArrayList<>();
        String response = request("/entity/product/" + productExternalId + "/images", "GET", null);
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
    public void removeProductImages(Product product) throws Exception {
        String response = request("/entity/product/" + product.getExternalId() + "/images", "GET", null);
        JSONObject json = new JSONObject(response);
        if (json.has("rows")) {
            JSONArray rows = json.getJSONArray("rows");
            request("/entity/product/" + product.getExternalId() + "/images/delete", "POST", rows.toString());
        }
    }
    @Override
    public Product createProduct(Product product) throws Exception {
        List<PriceType> priceTypes = coreDAO.getAll(PriceType.class);
        String response = request("/entity/product", "POST", product.toMySkladJSON(priceTypes.get(0)).toString());
        return fromJSON(new JSONObject(response), null);
    }
    @Override
    public Product updateProduct(Product product) throws Exception {
        List<PriceType> priceTypes = coreDAO.getAll(PriceType.class);
        String response = request("/entity/product/" + product.getExternalId(), "PUT",
                product.toMySkladJSON(priceTypes.get(0)).toString());
        return fromJSON(new JSONObject(response), null);
    }
    @Override
    public void deleteProduct(Product product) throws Exception {
        request("/entity/product/" + product.getExternalId(), "DELETE", null);
    }
    @Override
    public List<PriceType> getPriceTypes() throws Exception {
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
    public void attachImagesToProduct(Product product) throws Exception {
        for (EntityImage image : product.getImages()) {
            JSONObject payload = new JSONObject();
            payload.put("filename", image.getName());
            payload.put("content", new String(Base64.getEncoder().encode(image.getData()), "UTF-8"));
            request("/entity/product/" + product.getExternalId() + "/images", "POST", payload.toString());
        }
    }
    @Override
    public String createProductsGroup(ProductsGroup group) throws Exception {
        String response = request("/entity/productfolder", "POST", group.toMySkladJSON().toString());
        JSONObject json = new JSONObject(response);
        return json.getString("id");
    }
    @Override
    public void deleteProductsGroup(ProductsGroup group) throws Exception {
        request("/entity/productfolder/" + group.getExternalId(), "DELETE", null);
    }
    @Override
    public void updateProductsGroup(ProductsGroup group) throws Exception {
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
    // ============================================= PRIVATE ==========================================================
    /**
     * Request data from MySklad with predefined headers.
     * @param url URL.
     * @param method HTTP method.
     * @param payload payload.
     * @return response as string.
     * @throws Exception request error.
     */
    private String request(String url, String method, String payload) throws Exception {
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
    private String request(String url, String method, Map<String, String> headers, String payload) throws Exception {
        LOG.debug("------------------------------ REQUEST TO MY SKLAD -----------------------------------------------");
        LOG.debug("url [" + url + "]");
        LOG.debug("method [" + method + "]");
        LOG.debug("payload [" + payload + "]");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(API_ENDPOINT + url).openConnection();
        connection.setRequestMethod(method);
        connection.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(30)).intValue());
        for (String header : headers.keySet()) {
            connection.setRequestProperty(header, headers.get(header));
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (payload != null) {
            connection.getOutputStream().write(payload.getBytes("UTF-8"));
        }
        int responseCode = connection.getResponseCode();
        LOG.debug("response code [" + responseCode + "]");
        String response = null;
        if (responseCode == 200 || responseCode == 201) {
            response = inputStreamToString(connection.getInputStream());
            LOG.debug("response: " + response);
            return response;
        } else if (responseCode == 401) {
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
    private byte[] requestData(String url, String method, Map<String, String> headers) throws Exception {
        LOG.debug("------------------------------ DATA REQUEST ------------------------------------------------------");
        LOG.debug("url [" + url + "]");
        LOG.debug("method [" + method + "]");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(30)).intValue());
        for (String header : headers.keySet()) {
            connection.setRequestProperty(header, headers.get(header));
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        int responseCode = connection.getResponseCode();
        LOG.debug("response code [" + responseCode + "]");
        if (responseCode == 200 || responseCode == 201) {
            return inputStreamToByteArray(connection.getInputStream());
        } else {
            throw new IOException(inputStreamToString(connection.getErrorStream()));
        }
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
    private Product fromJSON(JSONObject item, Map<String, ProductsGroup> productGroupsMap) {
        Product product = new Product();
        product.setExternalId(item.getString("id"));
        product.setName(item.getString("name"));
        if (item.has("article")) {
            product.setArticle(item.getString("article"));
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
        if (item.has("salePrices")) {
            JSONArray prices = item.getJSONArray("salePrices");
            for (int j = 0; j < prices.length(); j++) {
                JSONObject price = prices.getJSONObject(j);
                product.setPrice(price.getDouble("value") / 100);
            }
        }
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
    private void authentication() throws Exception {
        Map<String, String> headers = new HashMap<>();
        String credentials = configuration.getMySkladUsername() + ":" + configuration.getMySkladPassword();
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
        LOG.debug("credentials: " + credentials);
        headers.put("Authorization", "Basic " + credentials);
        String tokenResponse = request("/security/token", "POST", headers, null);
        LOG.debug("security token: " + tokenResponse);
        JSONObject json = new JSONObject(tokenResponse);
        token = json.getString("access_token");
        LOG.debug("new acquired token: " + token);
    }
    /**
     * MySklad authentication error.
     */
    private static class MySkladAuthenticationException extends Exception {
    }
    /**
     * MySklad internal error.
     */
    private static class MySkladInternalErrorException extends Exception {
        /**
         * Constructor.
         * @param msg message.
         */
        public MySkladInternalErrorException(String msg) {
            super(msg);
        }
    }
}
