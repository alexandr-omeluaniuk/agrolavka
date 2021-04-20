/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Applications from './view/Applications';
import Settings from './view/Settings';
import UserProfile from './view/UserProfile';
import { Module } from '../../util/model/Module';
import { ModuleView } from '../../util/model/ModuleView';

export const common = new Module('common', '/common', [
    new ModuleView('applications', '/applications', 'widgets', Applications),
    new ModuleView('profile', '/profile', 'account_box', UserProfile),
    new ModuleView('settings', '/settings', 'settings', Settings)
]).setInvisible();

