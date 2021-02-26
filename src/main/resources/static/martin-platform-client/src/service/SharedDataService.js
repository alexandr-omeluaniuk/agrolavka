/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AppURLs from '../conf/app-urls';

export class SharedDataService {
    /** User permissions. */
    static permissions;
    /** Show notification function. */
    static showNotification;
    
    static getPermissions() {
        if (!SharedDataService.permissions) {
            window.location.href = AppURLs.welcome;
        } else {
            return SharedDataService.permissions;
        }
    }
}
