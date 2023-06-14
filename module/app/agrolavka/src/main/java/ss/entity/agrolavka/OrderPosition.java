package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ss.agrolavka.wrapper.ProductVolume;
import ss.entity.martin.DataModel;
import ss.martin.core.anno.Updatable;

/**
 * Order position.
 * @author alex
 */
@Entity
@Table(name = "customer_order_position")
public class OrderPosition extends DataModel {
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
    @Updatable
    @Column(name = "price", nullable = false)
    private Double price;
    /** Quantity. */
    @NotNull
    @Min(1)
    @Updatable
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    /** Product. */
    @Transient
    private Product product;
    /** Temporary ID. Should be used until entity is not save in DB. */
    @Transient
    private String positionId;
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public String getPositionId() {
        return positionId;
    }
    
    public void setPositionId(String tempId) {
        this.positionId = tempId;
    }
    
    @JsonIgnore
    public String getQuantityLabel() throws JsonProcessingException {
        if (getProduct() != null && getProduct().getVolumes() != null) {
            final Optional<ProductVolume> volume = getProduct().getProductVolumes().stream()
                    .filter(item -> item.getPrice().equals(getPrice())).findFirst();
            return volume.isEmpty() ? "" : String.format("за %s", volume.get().getVolumeLabel());
        } else {
            return "за 1 ед";
        }
    }
    
    public String getSubtotalLabel() throws JsonProcessingException {
        if (getProduct() != null && getProduct().getVolumes() != null) {
            final Optional<ProductVolume> volume = getProduct().getProductVolumes().stream()
                    .filter(item -> item.getPrice().equals(getPrice())).findFirst();
            return volume.isEmpty() ? "" : String.format("за %s", volume.get().getVolumeLabelForQuantity(quantity));
        } else {
            return String.format("за %s ед", getQuantity());
        }
    }
    
    public String getProductName() throws JsonProcessingException {
        String name = "";
        if (getProduct() != null) {
            if (getProduct().getVolumes() != null) {
                final Optional<ProductVolume> volume = getProduct().getProductVolumes().stream()
                    .filter(item -> item.getPrice().equals(getPrice())).findFirst();
                name = getProduct().getName() + (volume.isEmpty() ? "" : (" (" + volume.get().getVolumeLabel() + ")"));
            } else {
                name = getProduct().getName();
            }
        }
        return name;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductId() != null ? getProductId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderPosition)) {
            return false;
        }
        OrderPosition other = (OrderPosition) object;
        return !((this.getProductId() == null && other.getProductId() != null)
            || (this.getProductId() != null && !this.getProductId().equals(other.getProductId())));
    }
    
    @Override
    public String toString() {
        return "ss.entity.agrolavka.OrderPosition[ id=" + getId() + " ]";
    }
}
