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

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import ss.agrolavka.wrapper.ProductVolume;
import ss.entity.agrolavka.Discount;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;

/**
 * Price calculator.
 * @author alex
 */
public class PriceCalculator {
    
    private static final int MILLILITER_IN_LITER = 1000;
    
    public static Map<Double, Integer> breakQuantityByVolume(
        final Product product,
        final Optional<ProductVariant> variant,
        final Double quantity
    ) throws JsonProcessingException {
        final Map<Double, Integer> result = new TreeMap<>();
        if (product.getVolumes() != null) {
            int rest = toMilliliters(quantity);
            final List<ProductVolume> volumes = product.getProductVolumes();
            Collections.reverse(volumes);
            for (ProductVolume productVolume : volumes) {
                if (rest > 0) {
                    final int volumeInMilliliter = toMilliliters(productVolume.getAmount());
                    final int quantityInt = rest / volumeInMilliliter;
                    if (quantityInt >= 1) {
                        rest = rest - quantityInt * volumeInMilliliter;
                        result.put(productVolume.getPrice(), quantityInt);
                    }
                }
            }
        } else {
            final var basePrice = getBasePrice(product, variant.map(v -> Collections.singletonList(v)).orElse(Collections.emptyList()));
            final var shopPrice = getShopPrice(basePrice, product.getDiscount());
            result.put(shopPrice, quantity.intValue());
        }
        return result;
    }
    
    private static int toMilliliters(final double quantityInLiters) {
        return Double.valueOf(String.valueOf(quantityInLiters * MILLILITER_IN_LITER)).intValue();
    }
    
    public static Double getBasePrice(Product product, List<ProductVariant> variants) {
        return variants.isEmpty() ? product.getPrice() : variants.get(0).getPrice();
    }
    
    public static Double getShopPrice(Double basePrice, Discount discount) {
        if (discount != null) {
            return basePrice - (basePrice * discount.getDiscount() / 100);
        } else {
            return basePrice;
        }
    }
}
