import React, { useEffect } from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import { history } from '../index';
import { Router } from "react-router-dom";
import CssBaseline from '@material-ui/core/CssBaseline';
import AppToolbar from '../component/navigation/AppToolbar';
import SideNavBar from '../component/navigation/SideNavBar';
import MainContent from '../component/navigation/MainContent';
import useAuth from '../hooks/useAuth';
import { DESKTOP_MENU_OPEN } from '../conf/local-storage-keys';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { ToolbarContext } from '../context/ToolbarContext';

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex'
    }
}));

function App() {
    const classes = useStyles();
    const { t } = useTranslation();
    const { getCurrentModule, getAllRoutes, permissions } = useAuth();
    let isMenuOpen = localStorage.getItem(DESKTOP_MENU_OPEN);
    const isMobile = useMediaQuery(useTheme().breakpoints.down('sm'));
    const [title, setTitle] = React.useState('');
    const [open, setOpen] = React.useState(false);
    const [icon, setIcon] = React.useState(null);
    const [routes, setRoutes] = React.useState(null);
    const [currentModule, setCurrentModule] = React.useState(null);
    // --------------------------------------------------------- HOOKS --------------------------------------------------------------------
    useEffect(() => {
        if (currentModule) {
            const item = currentModule.getCurrentItem();
            if (item && item.id) {
                setItemAttributes(t(currentModule.getLabelKey(item)), item.getIcon());
            }
        } else {
            setTitle(t('component.account_menu.applications'));
            setIcon('widgets');
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
        if (permissions !== null && routes === null) {
            setRoutes(getAllRoutes());
            setCurrentModule(getCurrentModule());
            history.listen(location => {
                setCurrentModule(getCurrentModule());
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [permissions, routes]);
    // ------------------------------------------------------------ METHODS ---------------------------------------------------------------
    const setItemAttributes = (label, icon) => {
        setTitle(label);
        setIcon(icon);
        if (currentModule) {
            document.title = t(`m_${currentModule.getId()}:title`) + ' | ' + label;
        }
    };
    // ------------------------------------------------------------ RENDERING -------------------------------------------------------------
    if (!permissions) {
        return null;
    }
    return (
            <ToolbarContext.Provider value={{title: title, setTitle: setTitle, icon: icon, setIcon: setIcon}}>
                <Router history={history}>
                    <div className={classes.root}>
                        <CssBaseline />
                        <AppToolbar open={open} setOpen={setOpen} currentModule={currentModule}
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
            </ToolbarContext.Provider>
    );
}

export default App;
