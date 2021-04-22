/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { createContext, useReducer } from 'react';
import { Route, Redirect } from "react-router-dom";
import { modules } from '../conf/modules';
import AppURLs from '../conf/app-urls';
import { history } from '../index';
import DataService from '../service/DataService';

const dataService = new DataService();

const initialAuthState = {
    permissions: null
};

export const AuthContext = createContext({
    ...initialAuthState
});

const reducer = (state, action) => {
    switch (action.type) {
        case 'PERMISSIONS':
        {
            const { permissions } = action.payload;
            return {
                ...state,
                permissions: permissions
            };
        }
        case 'LOGOUT':
        {
            return {
                ...state,
                permissions: null
            };
        }
        default:
        {
            return {...state};
        }
    }
};

export const AuthProvider = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialAuthState);
    // ------------------------------------------------------------- METHODS --------------------------------------------------------------
    const login = (data) => {
        dataService.login(data).then((authResponse) => {
            console.log(authResponse);
            history.push(AppURLs.app);
            updatePermissions();
        });
    };
    
    const logout = () => {
        dataService.logout().then(() => {
            window.location.href = AppURLs.welcome;
            dispatch({ type: 'LOGOUT', payload: {}});
        });
    };
    
    const updatePermissions = () => {
        dataService.get(`/platform/security/permissions`).then(permissions => {
            dispatch({ type: 'PERMISSIONS', payload: {permissions}});
        });
    };
    
    const getCurrentModule = () => {
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
    };
    
    const getAllRoutes = () => {
        let routes = [];
        let rootURL = AppURLs.app;
        modules().forEach(module => {
            _visitItemRoutes(module, routes, rootURL);
        });
        return routes;
    };
    
    const _visitItemRoutes = (item, routes, parentPath) => {
        if (item !== null) {
            let itemPath = parentPath + item.path;
            if (item.component) {
                routes.push(<Route path={itemPath} component={item.component} key={itemPath}/>);
            }
            if (item.items) {
                for (let i = 0; i < item.items.length; i++) {
                    _visitItemRoutes(item.items[i], routes, itemPath);
                }
                routes.push(
                    <Route exact path={itemPath} key={itemPath}>
                        <Redirect to={itemPath + item.items[0].path}/>
                    </Route>
                );
            }
        }
    };
    // ------------------------------------------------------------- RENDERING ------------------------------------------------------------
    return (
            <AuthContext.Provider value={{...state, getCurrentModule, getAllRoutes, login, logout}}>
                {children}
            </AuthContext.Provider>
    );
};
