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
package ss.martin.platform.wrapper;

/**
 * Push notification.
 * @author alex
 */
public class PushNotification {
    /** Title. */
    private String title;
    /** Body. */
    private String body;
    /** Icon. */
    private String icon;
    /** Click action. */
    private String clickAction;
    /** Click action label. */
    private String clickActionLabel;
    /** Time to live (in seconds). */
    private String ttlInSeconds;
    /** Arbitrary data. */
    private String data;
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }
    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }
    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }
    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     * @return the clickAction
     */
    public String getClickAction() {
        return clickAction;
    }
    /**
     * @param clickAction the clickAction to set
     */
    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }
    /**
     * @return the ttlInSeconds
     */
    public String getTtlInSeconds() {
        return ttlInSeconds;
    }
    /**
     * @param ttlInSeconds the ttlInSeconds to set
     */
    public void setTtlInSeconds(String ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }
    /**
     * @return the clickActionLabel
     */
    public String getClickActionLabel() {
        return clickActionLabel;
    }
    /**
     * @param clickActionLabel the clickActionLabel to set
     */
    public void setClickActionLabel(String clickActionLabel) {
        this.clickActionLabel = clickActionLabel;
    }
    /**
     * @return the data
     */
    public String getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }
}
