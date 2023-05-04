/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.wrapper;

/**
 * Order search request.
 * @author alex
 */
public class OrderSearchRequest {
    /** Page number. Starts with 1. */
    private Integer page;
    /** Page size. */
    private Integer pageSize;
    /** Order. */
    private String order;
    /** Order by. */
    private String orderBy;
    /** Search text. */
    private String text;
    /** Status. */
    private String status;
    /** Show closed orders. */
    private boolean showClosed = false;
    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }
    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }
    /**
     * @return the pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }
    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }
    /**
     * @param order the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }
    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }
    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the showClosed
     */
    public boolean isShowClosed() {
        return showClosed;
    }
    /**
     * @param showClosed the showClosed to set
     */
    public void setShowClosed(boolean showClosed) {
        this.showClosed = showClosed;
    }
}
