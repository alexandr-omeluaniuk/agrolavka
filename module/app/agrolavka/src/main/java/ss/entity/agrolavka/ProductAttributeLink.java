package ss.entity.agrolavka;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ss.entity.security.EntityAudit;

@Entity
@Table(name = "product_attribute_links")
public class ProductAttributeLink extends EntityAudit {

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_attribute_item_id")
    private ProductAttributeItem attributeItem;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProductAttributeItem getAttributeItem() {
        return attributeItem;
    }

    public void setAttributeItem(ProductAttributeItem attributeItem) {
        this.attributeItem = attributeItem;
    }
}
