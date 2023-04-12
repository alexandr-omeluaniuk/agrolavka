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
import java.util.Map;
import java.util.TreeMap;
import ss.entity.agrolavka.Product;

/**
 * Price calculator.
 * @author alex
 */
public class PriceCalculator {
    
    public static Map<Double, Double> breakQuantityByVolume(final Product product, final Double quantity) {
        final Map<Double, Double> result = new TreeMap<>();
        if (product.getVolumes() != null) {
            final Map<Double, String> volumePrices = product.getVolumePricesWithOrder(true);
            double rest = quantity;
            for (Map.Entry<Double, String> entry : volumePrices.entrySet()) {
                if (rest > 0) {
                    final double volume = Double.parseDouble(entry.getValue().replace("л", "").replace(",", "."));
                    final double subQuantity = Math.floor(rest / volume);
                    rest = rest - subQuantity * volume;
                    result.put(entry.getKey(), subQuantity);
                }
            }
        } else {
            result.put(product.getDiscountPrice(), quantity);
        }
        return result;
    }
}
