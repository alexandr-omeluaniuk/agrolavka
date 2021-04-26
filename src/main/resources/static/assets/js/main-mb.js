/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function () {
    "use strict";

    document.querySelector('body').addEventListener('click', function (evt) {
        const navbarToggleButton = evt.target.closest('.navbar-toggler');
        if (navbarToggleButton) {
            navbarToggleButtonClick(navbarToggleButton);
        }
    });
    
    // ===================================================== LISTENERS ====================================================================
    function navbarToggleButtonClick(button) {
        const expanded = button.getAttribute('aria-expanded') === 'true';
        const intro = document.querySelector('#introCarousel');
        if (intro) {
            intro.setAttribute('data-expanded', expanded);
        }
        const navbar = document.querySelector('.navbar');
        navbar.setAttribute('data-expanded', expanded);
    }
})();

