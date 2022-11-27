/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.agrolavka.wrapper;

/**
 * Cart product.
 * @author alex
 */
public class CartProduct {
    /** Product ID. */
    private Long productId;
    /** Volume price. */
    private Double volumePrice;
    /** Quantity. */
    private Integer quantity;

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
     * @return the volumePrice
     */
    public Double getVolumePrice() {
        return volumePrice;
    }
    /**
     * @param volumePrice the volumePrice to set
     */
    public void setVolumePrice(Double volumePrice) {
        this.volumePrice = volumePrice;
    }
    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
