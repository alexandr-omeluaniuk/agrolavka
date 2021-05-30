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
import { Text, H1, H2, H3, H4, H5, H6, P, SPAN } from './components/Text';

const TYPE_CONTEXTMENU = 'CONTEXTMENU';
const TYPE_TEXT = 'TEXT';

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
    const { state, setState, saveHTML } = props;
    const [menu, setMenu] = React.useState(null);
    const [prevMenu, setPrevMenu] = React.useState(null);
    const classes = useStyles();
    const { t } = useTranslation();
    // ---------------------------------------------------------- HOOKS -------------------------------------------------------------------
    useEffect(() => {
        let config = null;
        if (state.type === TYPE_CONTEXTMENU) {
            config = [
                new MenuAction('delete', t('component.htmleditor.context_menu.action.delete'), onComponentDelete)
            ];
        } else if (state.type === TYPE_TEXT) {
            config = [
                new MenuPoint('format_list_numbered', t('component.htmleditor.context_menu.headers'), [
                    new MenuComponent(t('component.htmleditor.context_menu.header.h1'), new Text(H1)),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h2'), new Text(H2)),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h3'), new Text(H3)),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h4'), new Text(H4)),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h5'), new Text(H5)),
                    new MenuComponent(t('component.htmleditor.context_menu.header.h6'), new Text(H6))
                ]),
                new MenuPoint('title', t('component.htmleditor.context_menu.texts'), [
                    new MenuComponent(t('component.htmleditor.context_menu.text.p'), new Text(P)),
                    new MenuComponent(t('component.htmleditor.context_menu.text.span'), new Text(SPAN))
                ])
            ];
        }
        setMenu(config);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [state]);
    // ---------------------------------------------------------- METHODS -----------------------------------------------------------------
    const onComponentDelete = () => {
        state.initiator.remove();
        saveHTML();
    };
    const onMenuPointClick = (menuPoint) => {
        if (menuPoint instanceof MenuComponent) {
            handleClose();
            menuPoint.component.action(state);
        } else if (menuPoint instanceof MenuAction) {
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
