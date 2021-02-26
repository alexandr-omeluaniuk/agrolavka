/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { common } from '../module/common/module';
import { core } from '../module/core/module';
import { administrator } from '../module/admin/module';
import { SharedDataService } from '../service/SharedDataService';

/**
 * Modules metadata.
 * 'id' values must be unique inside module.
 * @type Array
 */
export const modules = function () {
    const modules = [common, core, administrator];
    const permissions = SharedDataService.getPermissions();
    return modules.filter(m => {
        return m.isPermitted(permissions.standardRole);
    });
};