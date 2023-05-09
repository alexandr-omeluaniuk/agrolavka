/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.entity.martin.Subscription;
import ss.entity.security.SystemUser;
import ss.martin.core.constants.StandardRole;
import ss.martin.core.dao.CoreDao;
import ss.martin.platform.service.SubscriptionService;
import ss.martin.security.api.SystemUserService;

/**
 * Subscription service implementation.
 * @author ss
 */
@Service
class SubscriptionServiceImpl implements SubscriptionService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
    /** Core DAO. */
    @Autowired
    private CoreDao coreDAO;
    /** System user service. */
    @Autowired
    private SystemUserService systemUserService;
    @Override
    @Secured("ROLE_SUPER_ADMIN")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Subscription createSubscription(Subscription subscription) throws Exception {
        SystemUser subscriptionAdmin = new SystemUser();
        subscriptionAdmin.setActive(true);
        subscriptionAdmin.setStandardRole(StandardRole.ROLE_SUBSCRIPTION_ADMINISTRATOR);
        subscriptionAdmin.setLastname(subscription.getOrganizationName());
        subscriptionAdmin.setEmail(subscription.getSubscriptionAdminEmail());
        subscription = coreDAO.create(subscription);
        LOG.debug("new subscription created: " + subscription);
        subscriptionAdmin.setSubscription(subscription);
        systemUserService.startRegistration(subscriptionAdmin);
        LOG.debug("new subscription administrator created: " + subscriptionAdmin);
        return subscription;
    }
}
