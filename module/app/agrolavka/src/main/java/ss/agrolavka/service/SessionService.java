package ss.agrolavka.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.SiteConstants;
import ss.entity.agrolavka.Order;

import java.util.ArrayList;

@Service
public class SessionService {

    public void setPhoneCookie(HttpServletResponse response, String phoneNumber) {
        final var phoneCookie = new Cookie(SiteConstants.PHONE_COOKIE, phoneNumber);
        phoneCookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(phoneCookie);
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
}
