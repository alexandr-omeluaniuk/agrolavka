import React, { useEffect, Suspense } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, Redirect } from "react-router-dom";
import reportWebVitals from './reportWebVitals';
import { createBrowserHistory } from "history";

import AppURLs from './conf/app-urls';
import Spinner from './component/util/Spinner';
import ErrorBoundary from './component/util/ErrorBoundary';
import { createTheme } from './conf/theme';
import { AuthProvider } from './context/AuthContext';
import { NotificationProvider } from './context/NotificationContext';
import './conf/i18next-config';
import 'moment/locale/ru';
import withClearCache from "./clear-cache";

import App from './pages/App';
import Welcome from './pages/Welcome';
import FinishRegistration from './pages/FinishRegistration';

import { ThemeProvider } from '@material-ui/core/styles';
import 'fontsource-roboto';

import * as serviceWorkerRegistration from './serviceWorkerRegistration';

export const history = createBrowserHistory();

export var changeTheme;

const indexRoutes = [{
        path: AppURLs.welcome,
        component: Welcome
    }, {
        path: AppURLs.registration,
        component: FinishRegistration
    }, {
        path: AppURLs.app,
        component: App
    }];

function Application() {
    const [theme, setTheme] = React.useState(null);
    
    useEffect(() => {
        if (!theme) {
            setTheme(createTheme());
            changeTheme = (newtheme) => {
                setTheme(newtheme);
            };
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [theme]);

    let displayApp = theme ? true : false;
    return (
            <Suspense fallback={ < Spinner open = {true} / > }>
                {displayApp ? (
                    <ThemeProvider theme={theme}>
                        <NotificationProvider>
                            <AuthProvider>
                                <ErrorBoundary>
                                    <Router history={history}>
                                        <Switch>
                                            {indexRoutes.map((prop, key) => {
                                                return <Route path={prop.path} component={prop.component} key={key} />;
                                            })}
                                            <Route exact path={AppURLs.context} key={'index-root'}>
                                                <Redirect to={AppURLs.app}/>
                                            </Route>
                                        </Switch>
                                    </Router>
                                </ErrorBoundary>
                            </AuthProvider>
                        </NotificationProvider>
                    </ThemeProvider>
                ) : <Spinner open={true}/>}
            </Suspense>
    );
}

const ClearCacheComponent = withClearCache(Application);

ReactDOM.render(<ClearCacheComponent/>, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://cra.link/PWA
serviceWorkerRegistration.register();

// Any other custom service worker logic can go here.
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/firebase-messaging-sw.js');
}

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
