/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function () {
    "use strict";

    document.querySelector('body').addEventListener('click', function (evt) {
        
    });
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
            topNavbar.style.opacity = 1;
            topNavbar.style.height = '41px';
        } else if (currentScrollDirection === 'DOWN') {
            topNavbar.style.opacity = 0;
            topNavbar.style.height = 0;
        }
        previuosScrollPosition = window.scrollY;
    });
    
    // ===================================================== LISTENERS ====================================================================
})();

