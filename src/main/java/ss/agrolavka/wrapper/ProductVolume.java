/*
 * Copyright (C) 2023 alex
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

import ss.agrolavka.constants.VolumeUnit;

/**
 * Product volume
 * @author alex
 */
public class ProductVolume implements Comparable<ProductVolume>{
    /** Price. */
    private Double price;
    /** Amount. */
    private Double amount;
    /** Unit. */
    private VolumeUnit unit;

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
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }
    /**
     * @param amount the volume to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    /**
     * @return the unit
     */
    public VolumeUnit getUnit() {
        return unit;
    }
    /**
     * @param unit the unit to set
     */
    public void setUnit(VolumeUnit unit) {
        this.unit = unit;
    }

    @Override
    public int compareTo(ProductVolume pv) {
        if (getPrice() > pv.getPrice()) {
            return 1;
        } else if (getPrice() < pv.getPrice()) {
            return -1;
        } else {
            return 0;
        }
    }
}
