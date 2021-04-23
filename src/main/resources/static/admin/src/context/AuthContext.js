/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { createContext, useReducer, useEffect } from 'react';
import { Route, Redirect } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import useModules from '../hooks/useModules';
import useNotification from '../hooks/useNotification';
import AppURLs from '../conf/app-urls';
import { history } from '../index';
import DataService from '../service/DataService';

DataService.jwt = localStorage.getItem('access_token') ? localStorage.getItem('access_token') : null;

const dataService = new DataService();

const initialAuthState = {
    permissions: null,
    initialization: false
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
                permissions: permissions,
                initialization: true
            };
        }
        case 'LOGOUT':
        {
            return {
                ...state,
                permissions: null,
                initialization: true
            };
        }
        case 'INITIALIZATION':
        {
            return {
                ...state,
                initialization: true
            };
        }
        default:
        {
            return {...state};
        }
    }
};

export const AuthProvider = ({ children }) => {
    const { t } = useTranslation();
    const [state, dispatch] = useReducer(reducer, initialAuthState);
    const modules = useModules(state.permissions);
    const { showNotification } = useNotification();
    // ------------------------------------------------------------- METHODS --------------------------------------------------------------
    const login = (data) => {
        dataService.login(data).then((authResponse) => {
            console.log(authResponse);
            if (authResponse) {
                DataService.jwt = authResponse.jwt;
                localStorage.setItem('access_token', authResponse.jwt);
                updatePermissions(() => {
                    history.push(AppURLs.app);
                });
            } else {
                showNotification(t('component.welcome.login_fail'), '', 'warning');
            }
        });
    };
    
    const logout = () => {
        dataService.logout().then(() => {
            window.location.href = AppURLs.welcome;
            dispatch({ type: 'LOGOUT', payload: {}});
        });
    };
    
    const updatePermissions = (callback) => {
        dataService.get(`/platform/security/permissions`).then(permissions => {
            dispatch({ type: 'PERMISSIONS', payload: {permissions}});
            if (callback) {
                callback(permissions);
            }
        });
    };
    
    const getCurrentModule = () => {
        const apps = modules.filter(m => m.isVisible());
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
        modules.forEach(module => {
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
    // ------------------------------------------------------------- HOOKS ----------------------------------------------------------------
    useEffect(() => {
        if (state.permissions === null && !state.initialization) {
            dispatch({ type: 'INITIALIZATION', payload: {}});
            updatePermissions();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [state.permissions, state.initialization]);
    // ------------------------------------------------------------- RENDERING ------------------------------------------------------------
    return (
            <AuthContext.Provider value={{...state, getCurrentModule, getAllRoutes, login, logout, updatePermissions}}>
                {children}
            </AuthContext.Provider>
    );
};
