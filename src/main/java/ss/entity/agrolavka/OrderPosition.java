/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import ss.entity.martin.DataModel;
import ss.martin.platform.anno.security.FormField;

/**
 * Order position.
 * @author alex
 */
@Entity
@Table(name = "customer_order_position")
public class OrderPosition extends DataModel {
    // ========================================== FIELDS ==============================================================
    /** Order. */
    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    /** Product ID. */
    @NotNull
    @Column(name = "product_id")
    private Long productId;
    /** Product price. */
    @NotNull
    @FormField
    @Column(name = "price", nullable = false)
    private Double price;
    /** Quantity. */
    @NotNull
    @Min(0)
    @FormField
    @Column(name = "quantity_double", nullable = false)
    private Double quantity;
    /** Product. */
    @Transient
    private Product product;
    /** Temporary ID. Should be used until entity is not save in DB. */
    @Transient
    private String positionId;
    // ========================================== SET & GET ===========================================================
    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }
    /**
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.order = order;
    }
    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }
    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    /**
     * @return the quantity
     */
    public Double getQuantity() {
        return quantity;
    }
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }
    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
    /**
     * @return the tempId
     */
    public String getPositionId() {
        return positionId;
    }
    /**
     * @param tempId the tempId to set
     */
    public void setPositionId(String tempId) {
        this.positionId = tempId;
    }
    // ================================================================================================================
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductId() != null ? getProductId().hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderPosition)) {
            return false;
        }
        OrderPosition other = (OrderPosition) object;
        if ((this.getProductId() == null && other.getProductId() != null)
                || (this.getProductId() != null && !this.getProductId().equals(other.getProductId()))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "ss.entity.agrolavka.OrderPosition[ id=" + getId() + " ]";
    }
}
