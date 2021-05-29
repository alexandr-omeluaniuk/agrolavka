/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import Typography from '@material-ui/core/Typography';

import Text from './components/Text';

class MenuPoint {
    constructor(icon, label, nestedMenu) {
        this.icon = icon;
        this.label = label;
        this.nestedMenu = nestedMenu;
        if (nestedMenu) {
            nestedMenu.forEach(m => {
                m.parent = this;
            });
        }
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

class MenuComponent extends MenuPoint {
    constructor(label, component) {
        super(null, label, null);
        this.component = component;
    }
}

class MenuAction extends MenuPoint {
    constructor(icon, label, action) {
        super(icon, label, null);
        this.action = action;
    }
}

const initialState = {
    mouseX: null,
    mouseY: null,
    initiator: null
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
    },
    itemIconBack: {
        marginRight: theme.spacing(2)
    }
}));

function HTMLEditorContextMenu(props) {
    const { state, setState } = props;
    const [menu, setMenu] = React.useState(null);
    const [prevMenu, setPrevMenu] = React.useState(null);
    const classes = useStyles();
    const { t } = useTranslation();
    // ---------------------------------------------------------- HOOKS -------------------------------------------------------------------
    useEffect(() => {
        let config = [];
        if (state.initiator && state.initiator.htmlEditorComponent) {
            config = [
                new MenuAction('edit', t('component.htmleditor.context_menu.action.edit'), onComponentEdit),
                new MenuAction('delete', t('component.htmleditor.context_menu.action.delete'), onComponentDelete)
            ];
        } else {
            config = [
                new MenuPoint('title', t('component.htmleditor.context_menu.headers'), [
                    new MenuComponent(t('component.htmleditor.context_menu.header.h1'), new Text('<h1>{text}</h1>')),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h2'), new Text('<h2>{text}</h2>')),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h3'), new Text('<h3>{text}</h3>')),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h4'), new Text('<h4>{text}</h4>')),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h5'), new Text('<h5>{text}</h5>')),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h6'), new Text('<h6>{text}</h6>'))
                ])
            ];
        }
        setMenu(config);
    }, [state]);
    // ---------------------------------------------------------- METHODS -----------------------------------------------------------------
    const onComponentEdit = () => {
        state.initiator.htmlEditorComponent.edit(state);
    };
    const onComponentDelete = () => {
        state.initiator.remove();
    };
    const onMenuPointClick = (menuPoint) => {
        if (menuPoint instanceof MenuComponent) {
            handleClose();
            menuPoint.component.create(state);
        } else if (menuPoint instanceof MenuAction) {
            console.log(menuPoint);
            handleClose();
            menuPoint.action(state);
        } else {
            setPrevMenu(menu);
            setMenu(menuPoint.getNestedMenu());
        }
    };
    const onMenuBack = (e) => {
        e.stopPropagation();
        setMenu(prevMenu);
    };
    const renderMenuPoint = (menuPoint, idx) => {
        return (
                <MenuItem key={idx} className={classes.item} onClick={(e) => onMenuPointClick(menuPoint)}>
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
        setMenu(null);
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
                {menu[0].parent ? (
                    <MenuItem key={-1} value={null} className={classes.item} onClick={(e) => onMenuBack(e)}>
                        <ListItemIcon classes={{root: classes.itemIcon}}>
                             <Icon>chevron_left</Icon>
                             <Typography variant="inherit">{t('component.htmleditor.context_menu.back')}</Typography>
                        </ListItemIcon>
                    </MenuItem>
                ) : null}
            </Menu>
    );
}

export default HTMLEditorContextMenu;
