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

import { addElementEvent } from './util-functions.js';

const navBackBtnClick = (evt, button) => {
    const lastCatalogLink = document.querySelector('#agr-last-catalog-link');
    if (lastCatalogLink) {
        lastCatalogLink.click();
    } else if (window.location.pathname === '/catalog') {
        window.location.href = '/';
    } else {
        window.location.href = '/catalog';
    }
};

export const handleNavigationEvent = (evt) => {
    addElementEvent(evt, ".agr-nav-back-btn", navBackBtnClick);
};
