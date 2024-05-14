package ss.agrolavka.test.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.test.web.servlet.ResultActions;
import ss.agrolavka.constants.JspValue;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract class BasePageControllerTest extends AbstractAgrolavkaMvcTest {

    protected static final String USER_PHONE = "+375 (29) 66644-55";
    
    
    protected ResultActions call(final String url, final String ...attributes) throws Exception {
        final var commonAttributes = new String[] {
            JspValue.SHOPS,
            JspValue.CART,
            JspValue.TOTAL_DECIMAL,
            JspValue.TOTAL_INTEGER,
            JspValue.DOMAIN,
            JspValue.PURCHASE_HISTORY
        };
        return mockMvc.perform(get(url).cookie(new Cookie(SiteConstants.PHONE_COOKIE, USER_PHONE)))
            .andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists(
            commonAttributes
        )).andExpect(model().attributeExists(
            attributes
        )).andExpect(model().size(attributes.length + commonAttributes.length));
    }
}
