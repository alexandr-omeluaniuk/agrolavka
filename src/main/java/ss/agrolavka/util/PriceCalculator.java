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
import ss.entity.agrolavka.Product;

/**
 * Price calculator.
 * @author alex
 */
public class PriceCalculator {
    
    public static Double caluclatePrice(final Product product, final Double quantity) {
        if (product.getVolumes() != null) {
            final Map<Double, String> volumePrices = product.getVolumePricesWithOrder(true);
            double rest = quantity;
            double sum = 0;
            for (Map.Entry<Double, String> entry : volumePrices.entrySet()) {
                if (rest > 0) {
                    final double volume = Double.parseDouble(entry.getValue().replace("л", "").replace(",", "."));
                    final double ceil = Math.floor(rest / volume);
                    rest = rest - ceil * volume;
                    sum += ceil * entry.getKey();
                }
            }
            return new BigDecimal(sum / quantity).doubleValue();
        } else {
            return product.getDiscountPrice();
        }
    }
}
