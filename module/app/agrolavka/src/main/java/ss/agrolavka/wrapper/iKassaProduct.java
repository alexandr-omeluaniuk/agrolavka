package ss.agrolavka.wrapper;

public record iKassaProduct(
    String article,
    String code,
    String id,
    Boolean isGTIN,
    String name,
    Integer price,
    Boolean priceHasTax,
    String type
) {
}
