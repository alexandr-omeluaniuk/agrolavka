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

export const initScrollEvents = () => {
    let previuosScrollPosition = null;
    document.addEventListener('scroll', function () {
        const topNavbar = document.querySelector('.arg-top-navbar');
        let currentScrollDirection = null;
        if (previuosScrollPosition !== null) {
            if (previuosScrollPosition > window.scrollY) {
                currentScrollDirection = 'UP';
            } else if (previuosScrollPosition < window.scrollY) {
                currentScrollDirection = 'DOWN';
            }
        }
        if (window.scrollY < 80 || currentScrollDirection === 'UP') {
            topNavbar.style.display = 'block';
            topNavbar.style.opacity = 1;
            topNavbar.style.height = '41px';
        } else if (currentScrollDirection === 'DOWN') {
            topNavbar.style.opacity = 0;
            topNavbar.style.height = 0;
            setTimeout(() => {
                if (topNavbar.style.height === 0 || topNavbar.style.height === '0px') {
                    topNavbar.style.display = 'none';
                }
            }, 300);
        }
        previuosScrollPosition = window.scrollY;
    });
};
