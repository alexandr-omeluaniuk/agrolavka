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
import AccountMenu from './AccountMenu';
import Popover from '@material-ui/core/Popover';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { useTheme } from '@material-ui/core/styles';
import ToolbarDesktop from './ToolbarDesktop';
import ToolbarMobile from './ToolbarMobile';

const useStyles = makeStyles(theme => ({
    popover: {
        [theme.breakpoints.up("md")]: {
            minWidth: '400px'
        }
    }
}));

function AppToolbar(props) {
    const classes = useStyles();
    const { title, icon, open, setOpen, currentModule, permissions, setItemAttributes } = props;
    const [anchorElAccount, setAnchorElAccount] = React.useState(null);
    const isMobile = useMediaQuery(useTheme().breakpoints.down('sm'));
    // ---------------------------------------------------- HOOKS -------------------------------------------------------------------------
    const accountSettings = () => {
        const open = Boolean(anchorElAccount);
        return (
                <Popover open={open} anchorEl={anchorElAccount} onClose={() => { setAnchorElAccount(null); }} anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left'
                }} classes={{
                    paper: classes.popover
                }}>
                    <AccountMenu permissions={permissions} onItemClick={(label, icon) => {
                        setAnchorElAccount(null);
                        setItemAttributes(label, icon);
                    }}/>
                </Popover>
        );
    };
    const isOpen = currentModule && open;
    return (
            <React.Fragment>
                {isMobile ? 
                    <ToolbarMobile title={title} icon={icon} setOpen={setOpen} setAnchorElAccount={setAnchorElAccount}
                        currentModule={currentModule}/>
                    : <ToolbarDesktop title={title} icon={icon} open={isOpen} setOpen={setOpen} setAnchorElAccount={setAnchorElAccount}
                        currentModule={currentModule}/>}
                {accountSettings()}
            </React.Fragment>
    );
}

export default AppToolbar;
