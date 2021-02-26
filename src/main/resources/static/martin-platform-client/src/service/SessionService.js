/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { Route, Redirect } from "react-router-dom";
import { modules } from '../conf/modules';
import AppURLs from '../conf/app-urls';
import { history } from '../index';

class SessionService {
    /**
     * Get current module.
     * @returns {applicationModules|currentModule}
     */
    static getCurrentModule = () => {
        const apps = modules().filter(m => m.isVisible());
        let currentModule = null;
        let internalAppUrl = window.location.pathname.replace(AppURLs.app, '');
        if (internalAppUrl) {
            let moduleUrl = '/' + internalAppUrl.split('/')[1];
            currentModule = apps.filter(m => {
                return m.path === moduleUrl;
            })[0];
        }
        return currentModule;
    }
    
    static getAllRoutes() {
        let routes = [];
        let rootURL = AppURLs.app;
        modules().forEach(module => {
            this._visitItemRoutes(module, routes, rootURL);
        });
        return routes;
    }
    
    static _visitItemRoutes = (item, routes, parentPath) => {
        if (item !== null) {
            let itemPath = parentPath + item.path;
            if (item.component) {
                routes.push(<Route path={itemPath} component={item.component} key={itemPath}/>);
            }
            if (item.items) {
                for (let i = 0; i < item.items.length; i++) {
                    this._visitItemRoutes(item.items[i], routes, itemPath);
                }
                routes.push(
                    <Route exact path={itemPath} key={itemPath}>
                        <Redirect to={itemPath + item.items[0].path}/>
                    </Route>
                );
            }
        }
    }
    
    
    
    
    
    
    /**
     * Get current module item. Determined by URL.
     * @returns {SessionService@call;_visitItem}
     */
    static currentItem = (authData) => {
        let module = this.currentModule(authData);
        let path = window.location.pathname.replace(AppURLs.app, '');
        if (module) {
            let currentItem = this._visitItem(module, path, '');
            if (currentItem && currentItem.visible) {
                return currentItem;
            } else {
                if (path !== '') {  // context URL detected (must be redirect)
                    //console.error('404 error: ' + path);
                    //window.location.href = AppURLs.errorPage + '/' + ERR_PAGE_NOT_FOUND;
                }
            }
        } else {
            history.push(AppURLs.app);    // redirect from context URL
        }
    }
    /**
     * Get module item full ID.
     * Uses for item label translate definition.
     * @param {string} itemId
     * @returns full item ID.
     */
    static getItemFullId = (itemId) => {
        let module = this.currentModule();
        let fullId = null;
        if (module) {
            fullId = this._visitItemId(module, '', itemId);
        }
        return fullId ? fullId : ('COMMON.' + itemId);
    }
    
    // ====================================================== PRIVATE =====================================================================
    static _visitItemId = (item, parentId, itemId) => {
        if (item !== null) {
            let subResult = (parentId ? parentId + '.' : '') + item.id;
            if (item.id === itemId) {
                return subResult;
            }
            if (item.items) {
                for (let i = 0; i < item.items.length; i++) {
                    let result = this._visitItemId(item.items[i], subResult, itemId);
                    if (result) {
                        return result;
                    }
                }
            }
        }
    }
    static _visitItem = (item, path, parentSubPath) => {
        if (item !== null) {
            let match;
            if (item.path.indexOf('/:') === -1) {   // for exact path
                match = parentSubPath + item.path === path;
            } else {    // for path templates
                let fullPath = (parentSubPath + item.path).trim().split('/');
                let fullPathSb = '';
                fullPath.filter(s => {
                    return s.length > 0;
                }).forEach(subPath => {
                    if (subPath.indexOf(':id') !== -1) {
                        //eslint-disable-next-line
                        fullPathSb += '\/[0-9]+';
                    } else {
                        //eslint-disable-next-line
                        fullPathSb += '\/' + subPath;
                    }
                });
                let regexp = new RegExp(fullPathSb);
                match = regexp.test(path);
            }
            if (match) {
                return item;
            }
            if (item.items) {
                for (let i = 0; i < item.items.length; i++) {
                    let result = this._visitItem(item.items[i], path, parentSubPath + item.path);
                    if (result) {
                        return result;
                    }
                }
            }
        }
    }
    
    static isAdmin = (authData) => {
        const mPermissions = authData.permissions.module_permissions;
        for (let moduleId in mPermissions) {
            let viewPermissions = mPermissions[moduleId].view_permissions;
            let usersPermission = viewPermissions.filter(v => {
                return v.view_id === 'users';
            });
            if (usersPermission.length > 0) {
                return true;
            }
        }
        return false;
    };
    static isOneViewAvailable = (authData) => {
        if (!authData || !authData.permissions || !authData.permissions.module_permissions) {
            return false;
        }
        const mPermissions = authData.permissions.module_permissions;
        let currentModule = SessionService.currentModule(authData);
        if (!currentModule) {
            return false;
        }
        let modulePermissions = mPermissions[currentModule.id];
        if (modulePermissions && modulePermissions.view_permissions.filter(vp => {
            return vp.view_id !== 'customerCallMonitor';
        }).length === 1) {
            return true;
        } else {
            return false;
        }
    };
}

export default SessionService;
