import React, { useEffect, Suspense } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, Redirect } from "react-router-dom";
import reportWebVitals from './reportWebVitals';
import { createBrowserHistory } from "history";

import AppURLs from './conf/app-urls';
import Spinner from './component/util/Spinner';
import ErrorBoundary from './component/util/ErrorBoundary';
import Notification from './component/util/Notification';
import { createTheme } from './conf/theme';
import { SharedDataService } from './service/SharedDataService';
import './conf/i18next-config';

import App from './pages/App';
import Welcome from './pages/Welcome';
import FinishRegistration from './pages/FinishRegistration';

import { ThemeProvider } from '@material-ui/core/styles';
import 'fontsource-roboto';

import * as serviceWorkerRegistration from './serviceWorkerRegistration';

import firebase from "firebase/app";
import "firebase/analytics";
import "firebase/messaging";

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
    const [openNotification, setOpenNotification] = React.useState(false);
    const [notificationMessage, setNotificationMessage] = React.useState(null);
    const [notificationDetails, setNotificationDetails] = React.useState(null);
    const [notificationType, setNotificationType] = React.useState('info');
    const [notificationDuration, setNotificationDuration] = React.useState(6000);

    const showNotification = (msg, details, type, duration) => {
        setNotificationMessage(msg);
        setNotificationDetails(details);
        setNotificationType(type ? type : 'info');
        setOpenNotification(true);
        setNotificationDuration(duration ? duration : 6000);
    };
    
    useEffect(() => {
        if (!theme) {
            SharedDataService.showNotification = showNotification;
            setTheme(createTheme());
            changeTheme = (newtheme) => {
                setTheme(newtheme);
            };
            // Your web app's Firebase configuration
            // For Firebase JS SDK v7.20.0 and later, measurementId is optional
            var firebaseConfig = {
                apiKey: "AIzaSyCNsi-R0xLTquWz74PdTEUG9f2OtTHvbjk",
                authDomain: "agrolavka-2aecb.firebaseapp.com",
                projectId: "agrolavka-2aecb",
                storageBucket: "agrolavka-2aecb.appspot.com",
                messagingSenderId: "1028755576776",
                appId: "1:1028755576776:web:605e8cfe6046bb58c412fd",
                measurementId: "G-7TE8WCK7XF"
            };
            // Initialize Firebase
            firebase.initializeApp(firebaseConfig);
            firebase.analytics();
            const messaging = firebase.messaging();
            // Get registration token. Initially this makes a network call, once retrieved
            // subsequent calls to getToken will return from cache.
            messaging.getToken({ vapidKey: 'BAzvWieDywMujonB48CqlSTDcWmpO7J-eaOg_B3LSkT5rOiTBEj_9MAq0FUupJf2jEx_62mka304PorWUd8nFXk' }).then((currentToken) => {
                if (currentToken) {
                    console.log(currentToken);
                    // Send the token to your server and update the UI if necessary
                    // ...
                } else {
                    // Show permission request UI
                    console.log('No registration token available. Request permission to generate one.');
                    // ...
                }
            }).catch((err) => {
                console.log('An error occurred while retrieving token. ', err);
                // ...
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [theme]);

    let displayApp = theme ? true : false;
    return (
            <Suspense fallback={ < Spinner open = {true} / > }>
                {displayApp ? (
                    <ThemeProvider theme={theme}>
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
                        <Notification open={openNotification} setOpen={setOpenNotification} message={notificationMessage} 
                                details={notificationDetails} severity={notificationType} duration={notificationDuration}/>
                    </ThemeProvider>
                ) : <Spinner open={true}/>}
            </Suspense>
    );
}

ReactDOM.render(<Application/>, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://cra.link/PWA
serviceWorkerRegistration.register();

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
