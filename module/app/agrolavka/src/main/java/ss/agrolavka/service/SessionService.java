package ss.agrolavka.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Order;

import java.util.ArrayList;

@Service
public class SessionService {

    @Autowired
    private CacheManager cacheManager;

    public void setPhoneCookie(HttpServletResponse response, String phoneNumber) {
        final var rawPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (rawPhoneNumber.length() >= 7) {
            final var cookie = createPhoneCookie(rawPhoneNumber.substring(rawPhoneNumber.length() - 7));
            response.addCookie(cookie);
        }
    }

    public Order getCurrentOrder(final HttpServletRequest request) {
        Order order = (Order) request.getSession(true).getAttribute(SiteConstants.CART_SESSION_ATTRIBUTE);
        if (order == null) {
            order = new Order();
            order.setPositions(new ArrayList<>());
            request.getSession().setAttribute(SiteConstants.CART_SESSION_ATTRIBUTE, order);
        }
        return order;
    }

    private Cookie createPhoneCookie(String phoneNumber) {
        final var purchaseHistoryCache = cacheManager.getCache(CacheKey.PURCHASE_HISTORY);
        if (purchaseHistoryCache != null) {
            purchaseHistoryCache.evict(phoneNumber);
        }
        final var phoneCookie = new Cookie(
            SiteConstants.PHONE_COOKIE,
            phoneNumber
        );
        phoneCookie.setMaxAge(60 * 60 * 24 * 365);
        phoneCookie.setHttpOnly(true);
        phoneCookie.setPath("/");
        return phoneCookie;
    }
}
