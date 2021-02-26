/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import { fade } from '@material-ui/core/styles/colorManipulator';
import { NavLink } from "react-router-dom";
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Tooltip from '@material-ui/core/Tooltip';
import Icon from '@material-ui/core/Icon';
import AppURLs from '../../conf/app-urls';
import List from '@material-ui/core/List';
import Collapse from '@material-ui/core/Collapse';

const useStyles = makeStyles(theme => ({
    navLink: {
        textDecoration: 'none',
        color: 'inherit'
    },
    itemLink: {
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        width: 'auto',
        borderRadius: '3px',
        marginBottom: theme.spacing(1)
    },
    itemLinkNotRoot: {
        paddingTop: theme.spacing(0),
        paddingBottom: theme.spacing(0)
    },
    itemIcon: {
        minWidth: '30px'
    },
    itemIconSelected: {
        color: theme.palette.getContrastText(theme.palette.text.primary) + ' !important',
        minWidth: '30px'
    },
    selected: {
        backgroundColor: fade(theme.palette.grey[50], 1) + ' !important',
        color: theme.palette.getContrastText(theme.palette.grey[50]) + ' !important'
    },
    innerList: {
        paddingBottom: 0,
        paddingTop: 0
    }
}));

function NavItem(props) {
    const { path, label, icon, onItemSelected, children, itemId } = props;
    let savedOpenState = localStorage.getItem('navItem_' + itemId);
    const isOpenDefault = savedOpenState === 'true' ? true : false;
    const classes = useStyles();
    const theme = useTheme();
    const [open, setOpen] = React.useState(isOpenDefault);
    let shiftMultiplier = path.split('/').length - AppURLs.app.split('/').length - 1;
    const isSelected = window.location.pathname === path;
    const expandButton = children ?
            <Icon fontSize="small" style={{
                    marginRight: `${theme.spacing(1)}px`,
                    marginLeft: `${-12}px`
                }}>
                {open ? 'expand_less' : 'expand_more'}
            </Icon> : null;
    const listIcon = (
        <Tooltip title={label}>
            <ListItemIcon className={classes.itemIcon}>
                <Icon className={isSelected ? classes.selected : null}>{icon}</Icon>
            </ListItemIcon>
        </Tooltip>
    );
    const listItem = (
        <ListItem button selected={isSelected} 
                className={classes.itemLink} classes={{
                    selected: classes.selected
                }} onClick={() => {
                    if (children) {
                        let nValue = !open;
                        setOpen(nValue);
                        localStorage.setItem('navItem_' + itemId, nValue);
                    }
                }} style={{marginLeft: `${shiftMultiplier * theme.spacing(2)}px`}}>
            <ListItemText primary={label}/>
            {children ? expandButton : listIcon}
        </ListItem>
    );
    return (
            <React.Fragment>
                {!children ? (
                    <NavLink to={path} className={classes.navLink} onClick={(e) => {
                        onItemSelected(label, icon);
                    }}>
                        {listItem}
                    </NavLink>
                ) : listItem}
                {children ? (
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <List className={classes.innerList}>
                            {children}
                        </List>
                    </Collapse>
                ) : null}
            </React.Fragment>
    );
}

export default NavItem;