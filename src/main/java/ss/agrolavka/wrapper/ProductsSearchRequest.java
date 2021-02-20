/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.wrapper;

/**
 * Product search request.
 * @author alex
 */
public class ProductsSearchRequest {
    /** Products group ID. Optional. */
    private Long groupId;
    /** Page number. Starts with 1. */
    private Integer page;
    /** Page size. */
    private Integer pageSize;
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
}
