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
import Icon from '@material-ui/core/Icon';
import Typography from '@material-ui/core/Typography';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';

const useStyles = makeStyles(theme => ({
    container: {
        display: 'flex',
        alignItems: 'flex-end'
    },
    title2: {
        marginLeft: theme.spacing(1)
    },
    title: {
        [theme.breakpoints.down("sm")]: {
            color: theme.palette.getContrastText(theme.palette.secondary.main)
        },
        [theme.breakpoints.up("md")]: {
            marginLeft: theme.spacing(1),
            color: theme.palette.getContrastText(theme.palette.secondary.main)
        }
    },
    icon: {
        [theme.breakpoints.up("md")]: {
            color: theme.palette.getContrastText(theme.palette.secondary.main)
        }
    }
}));

function InlineTabHeader(props) {
    const { icon, title } = props;
    const classes = useStyles();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    let nevBar = !isMobile ? (
            <div className={classes.container}>
                <Icon>{icon}</Icon> 
                <span className={classes.title2}>{title}</span>
            </div>
    ) : (
        <Typography className={classes.title} variant="subtitle2">{title}</Typography>
    );
    return nevBar;
}

export default InlineTabHeader;