/* global parseFloat, fetch, mdb */

import { handleCartClickEvent, handleCartChangeEvent } from './modules/cart.js';
import { initScrollEvents} from './modules/scroll-events.js';
import { handleMenuEvent } from './modules/sidemenu.js';
import { handleProductCardEvent } from "./modules/product-card.js";

(function () {
    "use strict";

    initScrollEvents();
    const body = document.querySelector('body');
    
    body.addEventListener('click', function(evt) {
        if (preventModalEventOnDemand(evt)) {
            return;
        }
        handleMenuEvent(evt);
        handleProductCardEvent(evt);
        handleCartClickEvent(evt);
    }, true);
    
    body.addEventListener('change', function(evt) {
        handleCartChangeEvent(evt);
    }, true);
    
//    const navBar = document.querySelector('#agr-navbar');
//    document.querySelectorAll('.nav-link', navBar).forEach(navLink => {
//        navLink.addEventListener('click', function () {
//            if (navBar.classList.contains('show')) {
//                document.querySelector('button[data-mdb-target="#agr-navbar"]').click();
//            }
//        });
//    });
    
    /**
     * Fix for a bug with backdrop for photo-slider-modal in the product card.
     */
    const preventModalEventOnDemand = (evt) => {
        const modal = evt.target.closest('#agr-photo-modal');
        if (modal) {
            if (!evt.target.classList.contains('btn-close') 
                    && !evt.target.classList.contains('swiper-button-next')
                    && !evt.target.classList.contains('swiper-button-prev')) {
                evt.stopPropagation();
                return true;
            }
        }
    };
})();

