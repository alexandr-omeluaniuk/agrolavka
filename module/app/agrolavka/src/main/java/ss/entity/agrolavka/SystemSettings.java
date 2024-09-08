package ss.entity.agrolavka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ss.entity.security.EntityAudit;

@Entity
@Table(name = "system_settings")
public class SystemSettings extends EntityAudit {

    @Column(name = "delivery_conditions", length = 65535, nullable = false)
    private String deliveryConditions;

    @Column(name = "delivery_order", length = 65535, nullable = false)
    private String deliveryOrder;

    @Column(name = "delivery_payment_details", length = 65535, nullable = false)
    private String deliveryPaymentDetails;

    @Column(name = "show_all_product_variants")
    private boolean showAllProductVariants;

    public String getDeliveryConditions() {
        return deliveryConditions;
    }

    public void setDeliveryConditions(String deliveryConditions) {
        this.deliveryConditions = deliveryConditions;
    }

    public String getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(String deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getDeliveryPaymentDetails() {
        return deliveryPaymentDetails;
    }

    public void setDeliveryPaymentDetails(String deliveryPaymentDetails) {
        this.deliveryPaymentDetails = deliveryPaymentDetails;
    }

    public boolean isShowAllProductVariants() {
        return showAllProductVariants;
    }

    public void setShowAllProductVariants(boolean showAllProductVariants) {
        this.showAllProductVariants = showAllProductVariants;
    }
}
