/*
 * The MIT License
 *
 * Copyright 2021 alex.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.spring.config;

/**
 * URL configuration.
 * @author alex
 */
public class URLConfiguration {
    /** Login URL. */
    private String login;
    /** Logout URL. */
    private String logout;
    /** Login page URL. */
    private String loginPage;
    /** Protected REST API URL. */
    private String protectedRest;
    /** Public REST API URL. */
    private String publicRest;
    /** Registration verification URL. */
    private String registrationVerification;
    /** Views URL. */
    private String views;
    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }
    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * @return the logout
     */
    public String getLogout() {
        return logout;
    }
    /**
     * @param logout the logout to set
     */
    public void setLogout(String logout) {
        this.logout = logout;
    }
    /**
     * @return the protectedRest
     */
    public String getProtectedRest() {
        return protectedRest;
    }
    /**
     * @param protectedRest the protectedRest to set
     */
    public void setProtectedRest(String protectedRest) {
        this.protectedRest = protectedRest;
    }
    /**
     * @return the publicRest
     */
    public String getPublicRest() {
        return publicRest;
    }
    /**
     * @param publicRest the publicRest to set
     */
    public void setPublicRest(String publicRest) {
        this.publicRest = publicRest;
    }
    /**
     * @return the registrationVerification
     */
    public String getRegistrationVerification() {
        return registrationVerification;
    }
    /**
     * @param registrationVerification the registrationVerification to set
     */
    public void setRegistrationVerification(String registrationVerification) {
        this.registrationVerification = registrationVerification;
    }
    /**
     * @return the context
     */
    public String getViews() {
        return views;
    }
    /**
     * @param context the context to set
     */
    public void setViews(String context) {
        this.views = context;
    }
    /**
     * @return the loginPage
     */
    public String getLoginPage() {
        return loginPage;
    }
    /**
     * @param loginPage the loginPage to set
     */
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
