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
import { Switch } from "react-router-dom";
import Copyright from '../Copyright';
import Container from '@material-ui/core/Container';
import Box from '@material-ui/core/Box';
import { drawerWidth } from '../../conf/theme';
import ErrorBoundary from '../util/ErrorBoundary';
import AppURLs from '../../conf/app-urls';
import { Route, Redirect } from "react-router-dom";


const useStyles = makeStyles(theme => ({
    content: {
        flexGrow: 1,
        height: '100vh',
        overflow: 'auto',
        '&::-webkit-scrollbar': {
            width: '0px'
        },
        [theme.breakpoints.up("md")]: {
            marginLeft: drawerWidth,
            transition: theme.transitions.create('margin', {
                easing: theme.transitions.easing.easeOut,
                duration: theme.transitions.duration.enteringScreen
            })
        },
        "&:after": {
            position: "absolute",
            top: 0,
            bottom: 0,
            left: 0,
            right: 0,
            zIndex: "-1",
            content: '""',
            display: "block",
            opacity: `1`,
            //backgroundImage: 'url(' + SessionService.moduleBackground() + ')',
            backgroundSize: "cover",
            backgroundPosition: "center center"
        }
    },
    appBarSpacer: theme.mixins.toolbar,
    container: {
        paddingTop: theme.spacing(3),
        paddingBottom: theme.spacing(3),
        [theme.breakpoints.down("md")]: {
            paddingTop: theme.spacing(1),
            paddingBottom: theme.spacing(1)
        },
        maxWidth: 'none'
    },
    fullWidthContent: {
        marginLeft: '0px !important;',
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen
        })
    }
}));

function MainContent(props) {
    const classes = useStyles();
    const { routes, open, currentModule } = props;
    if (!routes) {
        return null;
    }
    const contentStyle = clsx({
        [classes.content]: true,
        [classes.fullWidthContent]: !(currentModule && open)
    });
    return (
        <main className={contentStyle}>
            <div className={classes.appBarSpacer} />
            <Container maxWidth="lg" className={classes.container}>
                <ErrorBoundary>
                    <Switch>
                        {routes}
                        <Route exact path={AppURLs.app} key={'root'}>
                            <Redirect to={AppURLs.app + '/common/applications'}/>
                        </Route>
                    </Switch>
                </ErrorBoundary>
                <Box pt={4}>
                    <Copyright />
                </Box>
            </Container>
        </main>
    );
}

export default MainContent;