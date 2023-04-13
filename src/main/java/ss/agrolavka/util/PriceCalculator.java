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
package ss.agrolavka.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;
import ss.entity.agrolavka.Product;

/**
 * Price calculator.
 * @author alex
 */
public class PriceCalculator {
    
    private static final int MILLILITER_IN_LITER = 1000;
    
    public static Map<Double, Double> breakQuantityByVolume(final Product product, final Double quantity) {
        final Map<Double, Double> result = new TreeMap<>();
        if (product.getVolumes() != null) {
            final Map<Double, String> volumePrices = product.getVolumePricesWithOrder(true);
            int rest = toMilliliters(quantity);
            for (Map.Entry<Double, String> entry : volumePrices.entrySet()) {
                if (rest > 0) {
                    final String volumeStr = entry.getValue().replace("л", "").replace(",", ".");
                    final int volumeInMilliliter = toMilliliters(Double.parseDouble(volumeStr));
                    final int quantityInt = rest / volumeInMilliliter;
                    if (quantityInt >= 1) {
                        rest = rest - quantityInt * volumeInMilliliter;
                        result.put(entry.getKey(), new BigDecimal(quantityInt).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    }
                }
            }
        } else {
            result.put(product.getDiscountPrice(), quantity);
        }
        return result;
    }
    
    private static int toMilliliters(final double quantityInLiters) {
        return Double.valueOf(String.valueOf(quantityInLiters * MILLILITER_IN_LITER)).intValue();
    }
}
