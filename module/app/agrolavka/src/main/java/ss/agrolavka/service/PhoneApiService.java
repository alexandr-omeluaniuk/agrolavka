package ss.agrolavka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ss.agrolavka.wrapper.vats.CompletedCall;

@Service
public class PhoneApiService {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneApiService.class);

    private static final String ROOT_URL = "https://api.moysklad.ru/api/phone/1.0";

    @Autowired
    private RestTemplate restTemplate;

    public void createCall(CompletedCall call, String secretString) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Lognex-Phone-Auth-Token", secretString);
        headers.add("Content-Type", "application/json");
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        HttpEntity<CompletedCall> entity = new HttpEntity<>(call, headers);
        final var response = restTemplate.exchange(
            ROOT_URL + "/call",
            HttpMethod.POST,
            entity,
            String.class
        ).getBody();
        LOG.info("PHONE API RESPONSE: " + response);
    }
}
