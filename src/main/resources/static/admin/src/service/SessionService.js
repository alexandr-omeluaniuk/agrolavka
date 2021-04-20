/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { Route, Redirect } from "react-router-dom";
import { modules } from '../conf/modules';
import AppURLs from '../conf/app-urls';

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
}

export default SessionService;
