/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import Typography from '@material-ui/core/Typography';

class MenuPoint {
    constructor(id, icon, label, nestedMenu) {
        this.icon = icon;
        this.label = label;
        this.nestedMenu = nestedMenu;
    }
    
    getId() {
        return this.id;
    }
    
    getLabel() {
        return this.label;
    }
    
    getNestedMenu() {
        return this.nestedMenu;
    }
    
    getIcon() {
        return this.icon;
    }
}

const initialState = {
    mouseX: null,
    mouseY: null
};

const useStyles = makeStyles(theme => ({
    item: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        minWidth: '250px'
    },
    itemContent: {
        display: 'flex',
        alignItems: 'center'
    },
    itemIcon: {
        minWidth: '40px'
    }
}));

function HTMLEditorContextMenu(props) {
    const { state, setState } = props;
    const [menu, setMenu] = React.useState(null);
    const classes = useStyles();
    const { t } = useTranslation();
    // ---------------------------------------------------------- HOOKS -------------------------------------------------------------------
    useEffect(() => {
        if (menu === null) {
            const config = [
                new MenuPoint(1, 'title', t('component.htmleditor.toolbar.headers'), [
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h1')),
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h2')),
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h3')),
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h4')),
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h5')),
                    new MenuPoint(11, null, t('component.htmleditor.toolbar.header.h6'))
                ])
            ];
            setMenu(config);
        }
    }, [menu, t]);
    // ---------------------------------------------------------- METHODS -----------------------------------------------------------------
    const renderMenuPoint = (menuPoint, idx) => {
        console.log(menuPoint);
        return (
                <MenuItem key={idx} value={menuPoint.getId()} className={classes.item}>
                    <div className={classes.itemContent}>
                        {menuPoint.getIcon() ? (
                            <ListItemIcon classes={{root: classes.itemIcon}}>
                                <Icon>{menuPoint.getIcon()}</Icon>
                            </ListItemIcon>
                        ) : null}
                        <Typography variant="inherit">{menuPoint.getLabel()}</Typography>
                    </div>
                    {menuPoint.getNestedMenu() ? (
                        <Icon>chevron_right</Icon>
                    ) : null}
                </MenuItem>
        );
    };
    const handleClose = () => {
        setState(initialState);
    };
    // ---------------------------------------------------------- RENDERING ---------------------------------------------------------------
    if (!menu) {
        return null;
    }
    return (
            <Menu keepMounted open={state.mouseY !== null} onClose={handleClose} anchorReference="anchorPosition"
                anchorPosition={
                    state.mouseY !== null && state.mouseX !== null ? { top: state.mouseY, left: state.mouseX } : undefined
                }>
                {menu.map((menuPoint, idx) => {
                    return renderMenuPoint(menuPoint, idx);
                })}
            </Menu>
    );
}

export default HTMLEditorContextMenu;
