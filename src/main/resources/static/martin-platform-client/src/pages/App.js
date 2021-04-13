import React, { useEffect } from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import { history } from '../index';
import { Router } from "react-router-dom";
import CssBaseline from '@material-ui/core/CssBaseline';
import AppToolbar from '../component/navigation/AppToolbar';
import SideNavBar from '../component/navigation/SideNavBar';
import MainContent from '../component/navigation/MainContent';
import SessionService from '../service/SessionService';
import DataService from '../service/DataService';
import { SharedDataService } from '../service/SharedDataService';
import { DESKTOP_MENU_OPEN } from '../conf/local-storage-keys';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import firebase from "firebase/app";
import "firebase/analytics";
import "firebase/messaging";

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex'
    }
}));

const dataService = new DataService();

function App() {
    const classes = useStyles();
    const { t } = useTranslation();
    let isMenuOpen = localStorage.getItem(DESKTOP_MENU_OPEN);
    const isMobile = useMediaQuery(useTheme().breakpoints.down('sm'));
    const [title, setTitle] = React.useState('');
    const [open, setOpen] = React.useState(false);
    const [icon, setIcon] = React.useState(null);
    const [routes, setRoutes] = React.useState(null);
    const [currentModule, setCurrentModule] = React.useState(null);
    const [permissions, setPermissions] = React.useState(null);
    // --------------------------------------------------------- HOOKS --------------------------------------------------------------------
    useEffect(() => {
        if (currentModule) {
            const item = currentModule.getCurrentItem();
            if (item) {
                setItemAttributes(t(currentModule.getLabelKey(item)), item.getIcon());
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentModule]);
    useEffect(() => {
        if (isMobile) {
            setOpen(false);
        } else {
            setOpen(isMenuOpen === 'true' ? true : false);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isMobile]);
    useEffect(() => {
        if (permissions === null) {
            dataService.get(`/platform/security/permissions`).then(permissions => {
                setPermissions(permissions);
                SharedDataService.permissions = permissions;
                setRoutes(SessionService.getAllRoutes());
                setCurrentModule(SessionService.getCurrentModule());
                history.listen(location => {
                    setCurrentModule(SessionService.getCurrentModule());
                });
                requestFirebaseToken(permissions);
            });
        }
        return () => {
            dataService.abort();
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [permissions]);
    // ------------------------------------------------------------ METHODS ---------------------------------------------------------------
    const setItemAttributes = (label, icon) => {
        setTitle(label);
        setIcon(icon);
        //setOpen(false);
        if (currentModule) {
            document.title = t(`m_${currentModule.getId()}:title`) + ' | ' + label;
        }
    };
    
    const requestFirebaseToken = (permissions) => {
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
        if (!permissions.hasFirebaseToken) {
            messaging.getToken({ vapidKey: 'BAzvWieDywMujonB48CqlSTDcWmpO7J-eaOg_B3LSkT5rOiTBEj_9MAq0FUupJf2jEx_62mka304PorWUd8nFXk' })
                    .then((currentToken) => {
                if (currentToken) {
                    console.log(currentToken);
                    // Send the token to your server and update the UI if necessary
                    dataService.put('/platform/security/firebase-token', currentToken).then(() => {
                        console.log('Firebase token saved...');
                    });
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
    };
    // ------------------------------------------------------------ RENDERING -------------------------------------------------------------
    if (!permissions) {
        return null;
    }
    return (
            <Router history={history}>
                <div className={classes.root}>
                    <CssBaseline />
                    <AppToolbar title={title} open={open} setOpen={setOpen} icon={icon} currentModule={currentModule}
                        permissions={permissions} setItemAttributes={setItemAttributes}/>
                    <SideNavBar open={open} currentModule={currentModule} setOpen={setOpen} onItemSelected={(label, icon) => {
                        setItemAttributes(label, icon);
                        if (isMobile) {
                            setOpen(false);
                        }
                    }}/>
                    {routes ? <MainContent routes={routes} open={open} currentModule={currentModule}/> : null}
                </div>
            </Router>
    );
}

export default App;
