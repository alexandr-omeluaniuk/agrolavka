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
        if (window.scrollY > 40) {
            topNavbar.classList.remove('show');
        } else {
            topNavbar.classList.add('show');
        }
    });
    
    // ===================================================== LISTENERS ====================================================================
})();

