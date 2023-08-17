package ss.agrolavka.constants;

/**
 * Order status.
 * @author alex
 */
public enum OrderStatus {
    /** Waiting for approval. */
    WAITING_FOR_APPROVAL("В ожидании подтверждения"),
    /** Approved. */
    APPROVED("Подтвержден и принят в обработку"),
    /** Delivery. */
    DELIVERY("В процессе доставки"),
    /** Closed. */
    CLOSED("Закрыт");
    
    private final String label;
    
    private OrderStatus(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
