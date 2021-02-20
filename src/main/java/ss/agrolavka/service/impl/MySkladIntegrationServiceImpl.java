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
import ss.agrolavka.dao.CoreDAO;
import ss.agrolavka.model.Product;
import ss.agrolavka.model.ProductImage;
import ss.agrolavka.model.ProductsGroup;
import ss.agrolavka.service.MySkladIntegrationService;

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
    public void authentication() throws Exception {
        Map<String, String> headers = new HashMap<>();
        String credentials = configuration.getMySkladUsername() + ":" + configuration.getMySkladPassword();
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
        LOG.debug("credentials: " + credentials);
        headers.put("Authorization", "Basic " + credentials);
        String tokenResponse = request("/security/token", "POST", headers);
        LOG.debug("security token: " + tokenResponse);
        JSONObject json = new JSONObject(tokenResponse);
        token = json.getString("access_token");
        LOG.debug("new acquired token: " + token);
    }
    @Override
    public List<ProductsGroup> getProductGroups() throws Exception {
        String response = request("/entity/productfolder", "GET");
        JSONObject json = new JSONObject(response);
        List<ProductsGroup> result = new ArrayList<>();
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
        LOG.debug("loaded product groups [" + result.size() + "]");
        return result;
    }
    @Override
    public List<Product> getProducts() throws Exception {
        String response = request("/entity/product", "GET");
        JSONObject json = new JSONObject(response);
        List<Product> result = new ArrayList<>();
        JSONArray rows = json.getJSONArray("rows");
        Map<String, ProductsGroup> productGroupsMap = new HashMap();
        for (ProductsGroup group : coreDAO.getAll(ProductsGroup.class)) {
            productGroupsMap.put(group.getExternalId(), group);
        }
        for (int i = 0; i < rows.length(); i++) {
            JSONObject item = rows.getJSONObject(i);
            Product product = new Product();
            product.setExternalId(item.getString("id"));
            product.setName(item.getString("name"));
            if (item.has("productFolder")) {
                String link = item.getJSONObject("productFolder").getJSONObject("meta").getString("href");
                String productGroupId = link.substring(link.lastIndexOf("/") + 1);
                product.setGroup(productGroupsMap.get(productGroupId));
            }
            if (item.has("salePrices")) {
                JSONArray prices = item.getJSONArray("salePrices");
                for (int j = 0; j < prices.length(); j++) {
                    JSONObject price = prices.getJSONObject(j);
                    product.setPrice(price.getDouble("value") / 100);
                }
            }
            LOG.debug(product.toString());
            result.add(product);
        }
        LOG.debug("loaded products [" + result.size() + "]");
        return result;
    }
    @Override
    public List<ProductImage> getProductImages(String productExternalId) throws Exception {
        List<ProductImage> result = new ArrayList<>();
        String response = request("/entity/product/" + productExternalId + "/images", "GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        JSONObject json = new JSONObject(response);
        JSONArray rows = json.getJSONArray("rows");
        for (int i = 0; i < rows.length(); i++) {
            JSONObject item = rows.getJSONObject(i);
            JSONObject meta = item.getJSONObject("meta");
            String href = meta.getString("downloadHref");
            ProductImage productImage = new ProductImage();
            productImage.setFilename(item.getString("filename"));
            productImage.setImageSize(item.getLong("size"));
            productImage.setImageData(requestData(href, "GET", headers));
            result.add(productImage);
        }
        return result;
    }
    // ============================================= PRIVATE ==========================================================
    /**
     * Request data from MySklad with predefined headers.
     * @param url URL.
     * @param method HTTP method.
     * @return response as string.
     * @throws Exception request error.
     */
    private String request(String url, String method) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return request(url, method, headers);
    }
    /**
     * Request data from MySklad.
     * @param url URL.
     * @param method HTTP method.
     * @param headers HTTP headers.
     * @return response as string.
     * @throws Exception error.
     */
    private String request(String url, String method, Map<String, String> headers) throws Exception {
        LOG.debug("------------------------------ REQUEST TO MY SKLAD -----------------------------------------------");
        LOG.debug("url [" + url + "]");
        LOG.debug("method [" + method + "]");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(API_ENDPOINT + url).openConnection();
        connection.setRequestMethod(method);
        connection.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(30)).intValue());
        for (String header : headers.keySet()) {
            connection.setRequestProperty(header, headers.get(header));
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        int responseCode = connection.getResponseCode();
        LOG.debug("response code [" + responseCode + "]");
        String response;
        if (responseCode == 200 || responseCode == 201) {
            response = inputStreamToString(connection.getInputStream());
        } else {
            response = inputStreamToString(connection.getErrorStream());
        }
        LOG.debug("response: " + response);
        return response;
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
        String response;
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
        StringBuilder sb = new StringBuilder();
        while ((len = is.read(buff)) != -1) {
            sb.append(new String(buff, 0, len, "UTF-8"));
        }
        is.close();
        return sb.toString();
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
        ByteArrayOutputStream sb = new ByteArrayOutputStream();
        while ((len = is.read(buff)) != -1) {
            sb.write(buff, 0, len);
        }
        is.close();
        byte[] data = sb.toByteArray();
        sb.close();
        return data;
    }
}
