/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function () {
    "use strict";

    document.querySelector('body').addEventListener('click', function (evt) {
        
    });
    
    document.addEventListener('scroll', function () {
        const topNavbar = document.querySelector('.arg-top-navbar');
        if (window.scrollY > 80) {
            topNavbar.style.opacity = 0;
            topNavbar.style.height = 0;
        } else {
            topNavbar.style.opacity = 1;
            topNavbar.style.height = '41px';
        }
    });
    
    // ===================================================== LISTENERS ====================================================================
})();

