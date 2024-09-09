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

    @Column(name = "discount_about", length = 65535)
    private String discountAbout;

    @Column(name = "discount_participate", length = 65535)
    private String discountParticipate;

    @Column(name = "discount_size", length = 65535)
    private String discountSize;

    @Column(name = "registration_info", length = 65535)
    private String registrationInfo;

    @Column(name = "guarantee_info", length = 65535)
    private String guaranteeInfo;

    @Column(name = "return_info", length = 65535)
    private String returnInfo;

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getDiscountAbout() {
        return discountAbout;
    }

    public void setDiscountAbout(String discountAbout) {
        this.discountAbout = discountAbout;
    }

    public String getDiscountParticipate() {
        return discountParticipate;
    }

    public void setDiscountParticipate(String discountParticipate) {
        this.discountParticipate = discountParticipate;
    }

    public String getDiscountSize() {
        return discountSize;
    }

    public void setDiscountSize(String discountSize) {
        this.discountSize = discountSize;
    }

    public String getRegistrationInfo() {
        return registrationInfo;
    }

    public void setRegistrationInfo(String registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public String getGuaranteeInfo() {
        return guaranteeInfo;
    }

    public void setGuaranteeInfo(String guaranteeInfo) {
        this.guaranteeInfo = guaranteeInfo;
    }

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
