/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.service.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ss.agrolavka.AgrolavkaConfiguration;
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
    /** Authorization token. */
    private String token;
    /**
     * Initialization.
     */
    @PostConstruct
    protected void init() {
        acquireToken();
    }
    // ============================================= PRIVATE ==========================================================
    /**
     * Get new security token.
     * @return new security token.
     * @throws Exception 
     */
    private void acquireToken() {
        try {
            Map<String, String> headers = new HashMap<>();
            String credentials = configuration.getMySkladUsername() + ":" + configuration.getMySkladPassword();
            credentials = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
            LOG.debug("credentials: " + credentials);
            headers.put("Authorization", "Basic " + credentials);
            String tokenResponse = request("/security/token", "POST", headers);
            LOG.info("security token: " + tokenResponse);
            JSONObject json = new JSONObject(tokenResponse);
            token = json.getString("access_token");
            LOG.debug("new acquired token: " + token);
        } catch (Exception ex) {
            LOG.error("Can't perform MySklad authorization!", ex);
        }
    }
    
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
}
