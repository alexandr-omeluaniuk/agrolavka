/* 
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import React from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import { drawerWidth } from '../../conf/theme';
import { useTranslation } from 'react-i18next';
import AppBar from '@material-ui/core/AppBar';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Toolbar from '@material-ui/core/Toolbar';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';
import { DESKTOP_MENU_OPEN } from '../../conf/local-storage-keys'; 

const useStyles = makeStyles(theme => ({
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen
        })
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen
        })
    },
    menuButton: {
        marginRight: theme.spacing(2)
    },
    toolbar: {
        borderRadius: 0
    },
    title: {
        flexGrow: 1
    },
    accountText: {
        fontWeight: 'bold',
        textTransform: 'none'
    },
    icon: {
        marginRight: theme.spacing(1)
    }
}));

function ToolbarDesktop(props) {
    const classes = useStyles();
    const { title, icon, open, setOpen, setAnchorElAccount, currentModule } = props;
    const { t } = useTranslation();
    // ---------------------------------------------------- RENDER ------------------------------------------------------------------------
    const desktopToolbarStyle = clsx({
        [classes.appBar]: true,
        [classes.appBarShift]: open
    });
    return (
            <AppBar position="absolute" className={desktopToolbarStyle}>
                <Toolbar className={classes.toolbar}>
                    {currentModule ? (
                        <Tooltip title={open ? t('component.menu.close') : t('component.menu.open')}>
                            <IconButton edge="start" color="inherit" className={classes.menuButton} onClick={() => {
                                let nValue = !open;
                                setOpen(nValue);
                                localStorage.setItem(DESKTOP_MENU_OPEN, nValue);
                            }}>
                                <Icon>{open ? 'menu_open' : 'menu'}</Icon>
                            </IconButton>
                        </Tooltip>
                    ) : null}
                    <Icon className={classes.icon}>{icon}</Icon>
                    <Typography variant="h6" className={classes.title}>
                        {title}
                    </Typography>
                    <Tooltip title={t('component.toolbar.account')}>
                        <IconButton color="inherit" onClick={(e) => {setAnchorElAccount(e.currentTarget);}}>
                            <Icon>account_circle</Icon>
                        </IconButton>
                    </Tooltip>
                    
                </Toolbar>
            </AppBar>
    );
}

export default ToolbarDesktop;
