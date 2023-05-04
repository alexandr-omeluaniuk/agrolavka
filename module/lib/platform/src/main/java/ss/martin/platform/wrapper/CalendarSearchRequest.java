/*
 * The MIT License
 *
 * Copyright 2020 ss.
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

import java.util.Date;
import java.util.Set;
import ss.entity.martin.CalendarEvent;

/**
 * Search request wrapper for calendar.
 * @author ss
 */
public class CalendarSearchRequest {
    /** Date from. */
    private Date from;
    /** Date to. */
    private Date to;
    /** Event classes. */
    private Set<Class<? extends CalendarEvent>> classes;
    /**
     * @return the from
     */
    public Date getFrom() {
        return from;
    }
    /**
     * @param from the from to set
     */
    public void setFrom(Date from) {
        this.from = from;
    }
    /**
     * @return the to
     */
    public Date getTo() {
        return to;
    }
    /**
     * @param to the to to set
     */
    public void setTo(Date to) {
        this.to = to;
    }
    /**
     * @return the classes
     */
    public Set<Class<? extends CalendarEvent>> getClasses() {
        return classes;
    }
    /**
     * @param classes the classes to set
     */
    public void setClasses(Set<Class<? extends CalendarEvent>> classes) {
        this.classes = classes;
    }
}
