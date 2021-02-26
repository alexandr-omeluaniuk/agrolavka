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
import { makeStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import clsx from 'clsx';
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import { drawerWidth } from '../../conf/theme';
import Typography from "@material-ui/core/Typography";
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles(theme => ({
    title: {
        flex: 1,
        textAlign: 'right',
        marginRight: theme.spacing(2)
    },
    drawer: {
        height: '100%'
    },
    drawerPaper: {
        border: "none",
        position: "fixed",
        overflowY: 'hidden',
        top: "0",
        bottom: "0",
        left: "0",
        zIndex: "1",
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen
        }),
        [theme.breakpoints.up("md")]: {
          width: drawerWidth,
          position: "fixed",
          height: "100%",
          backgroundColor: '#ffffffb3'
        },
        // small display navBar
        [theme.breakpoints.down("sm")]: {
          width: drawerWidth,
          position: "fixed",
          display: "block",
          top: "0",
          height: "100vh",
          right: "0",
          left: "auto",
          zIndex: "1032",
          visibility: "visible",
          overflowY: "visible",
          borderTop: "none",
          textAlign: "left",
          paddingRight: "0px",
          paddingLeft: "0",
          transform: `translate3d(${drawerWidth}px, 0, 0)`
        }
    },
    drawerPaperDesktop: {
        boxShadow: '2px 0px 4px -1px rgba(0,0,0,0.2), 4px 0px 5px 0px rgba(0,0,0,0.14), 1px 0px 10px 0px rgba(0,0,0,0.12)',
        zIndex: 1202
    },
    brandContainer: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-start',
        padding: '0 8px',
        ...theme.mixins.toolbar
    },
    list: {
        overflowY: 'auto',
        maxHeight: '100%',
        '&::-webkit-scrollbar': {
            width: '0px'
        }
    }
}));

function SideNavBarDesktop(props) {
    const classes = useStyles();
    const { open, navItems, moduleId } = props;
    const { t } = useTranslation();
    let moduleTitle = (
        <Typography className={classes.title} variant={'caption'}>
            {t(`m_${moduleId}:title`)}
        </Typography>
    );
    return (
        <Drawer variant="persistent" className={classes.drawer}
            classes={{
                paper: clsx(classes.drawerPaper, classes.drawerPaperDesktop)
            }} open={open}>
            <div className={classes.brandContainer}>
                {moduleTitle}
            </div>
            <Divider variant="middle"/>
            <div className={classes.list}>
                <List>{navItems}</List>
            </div>
        </Drawer>
    );
}

export default SideNavBarDesktop;
