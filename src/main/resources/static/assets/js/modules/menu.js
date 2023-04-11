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

export { handleEvent } from './utils.js';

const openMobileMenu = (evt, element) => {
    evt.stopPropagation();
    const sidebar = document.querySelector('.agr-menu-sidebar');
    const body = document.querySelector('body');
    const backdrop = document.querySelector('.agr-backdrop');
    backdrop.classList.add('open-it');
    backdrop.classList.remove('close-it');
    body.classList.add('agr-body-freeze');
    sidebar.classList.remove('close-it');
    sidebar.classList.add('open-it');
};

const closeMobileMenu = (evt, element) => {
    evt.stopPropagation();
    const sidebar = document.querySelector('.agr-menu-sidebar');
    const body = document.querySelector('body');
    const backdrop = document.querySelector('.agr-backdrop');
    backdrop.classList.remove('open-it');
    backdrop.classList.add('close-it');
    body.classList.remove('agr-body-freeze');
    sidebar.classList.remove('open-it');
    sidebar.classList.add('close-it');
    setTimeout(() => backdrop.classList.remove('close-it'), 350);
};

export const handleMenuEvent = (evt) => {
    handleEvent(evt, ".agr-mobile-menu-link", closeMobileMenu);
    handleEvent(evt, ".agr-mobile-menu-btn", openMobileMenu);
    handleEvent(evt, ".agr-desktop-menu-btn", openMobileMenu);
    handleEvent(evt, ".agr-mobile-menu-close-btn", closeMobileMenu);
    handleEvent(evt, ".agr-backdrop", closeMobileMenu);
};
