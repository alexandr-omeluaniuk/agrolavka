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
