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
package ss.agrolavka.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.wrapper.OneClickOrderWrapper;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.EuropostLocation;
import ss.entity.agrolavka.EuropostLocationSnapshot;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.FirebaseClient;
import ss.martin.platform.wrapper.PushNotification;

/**
 * Order service implementation.
 * @author alex
 */
@Service
class OrderServiceImpl implements OrderService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Firebase client. */
    @Autowired
    private FirebaseClient firebaseClient;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Order createOrder(final Order order, final OrderDetailsWrapper orderDetails) throws Exception {
        LOG.info("Create new order");
        final Double total = order.getPositions().stream().mapToDouble(e -> e.getPrice() * e.getQuantity())
                .reduce(0, (a, b) -> a + b);
        LOG.info("Order total [" + total + "]");
        LOG.info("Order phone [" + orderDetails.getPhone() + "]");
        order.setPhone(orderDetails.getPhone());
        order.setComment(orderDetails.getComment());
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        if (orderDetails.getCity() != null && !orderDetails.getCity().isBlank()) {
            order.setAddress(getOrderAddress(orderDetails));
            LOG.info("Order delivery type - post");
        } else if (orderDetails.getEuropostLocationId() != null) {
            order.setEuropostLocationSnapshot(getOrderEuropostLocation(orderDetails));
            LOG.info("Order delivery type - europost");
        } else {
            LOG.info("Order delivery type - self");
        }
        final Order savedOrder = coreDAO.create(order);
        sendNotification(savedOrder, total);
        return savedOrder;
    }
    
    @Override
    public Order createOneClickOrder(final OneClickOrderWrapper orderDetails) throws Exception {
        final Product product = coreDAO.findById(orderDetails.getProductId(), Product.class);
        final OrderPosition position = new OrderPosition();
        position.setQuantity(1);
        position.setProduct(product);
        position.setProductId(orderDetails.getProductId());
        position.setPrice(product.getDiscountPrice());
        final Double total = position.getQuantity() * position.getPrice();
        final Order order = new Order();
        order.setPhone(orderDetails.getPhone());
        order.setCreated(new Date());
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        order.setPositions(new HashSet<>(Collections.singletonList(position)));
        position.setOrder(order);
        final Order savedOrder = coreDAO.create(order);
        sendNotification(savedOrder, total);
        return order;
    }
    
    private Address getOrderAddress(final OrderDetailsWrapper orderDetails) {
        final Address address = new Address();
        address.setRegion(orderDetails.getRegion());
        address.setDistrict(orderDetails.getDistrict());
        address.setCity(orderDetails.getCity());
        address.setHouse(orderDetails.getHouse());
        address.setStreet(orderDetails.getStreet());
        address.setPostcode(orderDetails.getPostcode());
        address.setFlat(orderDetails.getFlat());
        address.setFirstname(orderDetails.getFirstname());
        address.setLastname(orderDetails.getLastname());
        address.setMiddlename(orderDetails.getMiddlename());
        return address;
    }
    
    private EuropostLocationSnapshot getOrderEuropostLocation(final OrderDetailsWrapper orderDetails) throws Exception {
        final EuropostLocationSnapshot snapshot = new EuropostLocationSnapshot();
        final EuropostLocation location =
                coreDAO.findById(orderDetails.getEuropostLocationId(), EuropostLocation.class);
        snapshot.setAddress(location.getAddress());
        snapshot.setAltId(location.getAltId());
        snapshot.setCity(location.getCity());
        snapshot.setExternalId(location.getExternalId());
        snapshot.setIsNew(location.getIsNew());
        snapshot.setLatitude(location.getLatitude());
        snapshot.setLongitude(location.getLongitude());
        snapshot.setNote(location.getNote());
        snapshot.setWarehouseId(location.getWarehouseId());
        snapshot.setWorkingHours(location.getWorkingHours());
        return snapshot;
    }
    
    private void sendNotification(final Order order, final Double total) throws Exception {
        final PushNotification notification = new PushNotification();
        notification.setTitle("Поступил новый заказ");
        notification.setBody("Потенциальная сумма заказа - " + String.format("%.2f", total)
                + " BYN. Номер заказа: " + order.getId());
        notification.setTtlInSeconds(String.valueOf(TimeUnit.DAYS.toSeconds(1)));
        notification.setIcon("https://agrolavka.by/favicon.svg");
        notification.setClickAction("https://agrolavka.by/admin/app/agrolavka/order/" + order.getId());
        notification.setClickActionLabel("Открыть");
        firebaseClient.sendTopicNotification(notification, SiteConstants.FIREBASE_TOPIC_ORDER_CREATED);
        LOG.info("Order created: notification sent");
    }
}
