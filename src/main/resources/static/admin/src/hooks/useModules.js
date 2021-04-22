/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { common } from '../module/common/module';
import { core } from '../module/core/module';
import { administrator } from '../module/admin/module';
import { agrolavka } from '../module/agrolavka/module';

function useModules(permissions) {
    const modules = [common, core, administrator, agrolavka];
    if (!permissions) {
        return [];
    } else {
        return modules.filter(m => {
            return m.isPermitted(permissions ? permissions.standardRole : '');
        });
    }
}

export default useModules;

