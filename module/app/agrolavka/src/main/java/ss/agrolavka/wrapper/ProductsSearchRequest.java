/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.wrapper;

import java.util.Set;

/**
 * Product search request.
 * @author alex
 */
public class ProductsSearchRequest {
    /** Products group ID. Optional. */
    private Long groupId;
    private Set<Long> productIds;
    /** Page number. Starts with 1. */
    private Integer page;
    /** Page size. */
    private Integer pageSize;
    /** Search text. */
    private String text;
    /** Order. */
    private String order;
    /** Order by. */
    private String orderBy;
    /** Code. */
    private String code;
    /** If true, search products with quantity > 0, otherwise all. */
    private boolean available = false;
    /** With discounts only. */
    private boolean withDiscounts = false;
    /** Includes hidden. */
    private boolean includesHidden = false;

    private boolean invisible = false;
    // =================================================== SET & GET ==================================================
    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return groupId;
    }
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
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
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }
    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    /**
     * @return the withDiscounts
     */
    public boolean isWithDiscounts() {
        return withDiscounts;
    }
    /**
     * @param withDiscounts the withDiscounts to set
     */
    public void setWithDiscounts(boolean withDiscounts) {
        this.withDiscounts = withDiscounts;
    }
    /**
     * @return the includesHidden
     */
    public boolean isIncludesHidden() {
        return includesHidden;
    }
    /**
     * @param includesHidden the includesHidden to set
     */
    public void setIncludesHidden(boolean includesHidden) {
        this.includesHidden = includesHidden;
    }

    public Set<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
}
