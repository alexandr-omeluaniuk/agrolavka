package ss.entity.agrolavka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ss.agrolavka.wrapper.ProductVolume;
import ss.entity.martin.DataModel;
import ss.martin.base.lang.ThrowingSupplier;

import java.util.Optional;

/**
 * Order position.
 * @author alex
 */
@Entity
@Table(name = "customer_order_position")
public class OrderPosition extends DataModel implements Comparable<OrderPosition> {
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
    @Column(name = "price", nullable = false)
    private Double price;
    /** Quantity. */
    @NotNull
    @Min(1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    /** Variant ID. */
    @Column(name = "variant_id")
    private String variantId;
    /** Product. */
    @Transient
    private Product product;
    @Transient
    private ProductVariant variant;
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
    
    public String getProductName() {
        return ((ThrowingSupplier<String>) () -> {
            String name = "";
            if (getProduct() != null) {
                if (getVariant() != null) {
                    name = getVariant().getName() + " " + getVariant().getCharacteristics();
                } else if (getProduct().getVolumes() != null) {
                    final Optional<ProductVolume> volume = getProduct().getProductVolumes().stream()
                        .filter(item -> item.getPrice().equals(getPrice())).findFirst();
                    name = getProduct().getName() + (volume.isEmpty() ? "" : (" (" + volume.get().getVolumeLabel() + ")"));
                } else {
                    name = getProduct().getName();
                }
            }
            return name;
        }).get();
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
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

    @Override
    public int compareTo(final OrderPosition o) {
        return getProductName().compareTo(o.getProductName());
    }
}
