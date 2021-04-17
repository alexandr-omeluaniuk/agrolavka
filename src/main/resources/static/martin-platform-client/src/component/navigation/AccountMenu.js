import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import Icon from '@material-ui/core/Icon';
import DataService from '../../service/DataService';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import { NavLink } from "react-router-dom";
import AppURLs from '../../conf/app-urls';
import { common } from '../../module/common/module';
import { SharedDataService } from './../../service/SharedDataService';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    navLink: {
        textDecoration: 'none',
        color: 'inherit'
    },
    avatar: {
        backgroundColor: theme.palette.primary.main
    },
    content: {
        paddingTop: 0,
        paddingBottom: `${theme.spacing(1)}px !important`
    },
    divider: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1)
    }
}));

function AccountMenu (props) {
    const classes = useStyles();
    const { t } = useTranslation();
    const { onItemClick, permissions } = props;
    const [applications, setApplications] = React.useState(null);
    // ===================================================== HOOKS ========================================================================
    useEffect(() => {
        if (!applications) {
            setApplications([]);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [applications]);
    // ===================================================== RENDERING ====================================================================
    if (!applications) {
        return null;
    }
    const avatar = (
        <Avatar className={classes.avatar}>
            <Icon>account_circle</Icon>
        </Avatar>
    );
    return (
            <Card>
                <CardHeader title={permissions.fullname} subheader={'TODO: Role'} avatar={avatar}/>
                <CardContent className={classes.content}>
                    <List component="nav">
                        <Divider variant="middle" className={classes.divider}/>
                        {common.getItems().map((item, idx) => {
                            const label = t(`component.account_menu.${item.getId()}`);
                            return (
                                <NavLink to={AppURLs.app + common.getPath() + item.getPath()} className={classes.navLink} key={idx}
                                    onClick={(e) => onItemClick(label, item.getIcon())}>
                                    <ListItem button>
                                        <ListItemIcon>
                                            <Icon>{item.getIcon()}</Icon>
                                        </ListItemIcon>
                                        <ListItemText primary={label}/>
                                    </ListItem>
                                </NavLink>
                            );
                        })}
                        <Divider variant="middle" className={classes.divider}/>
                        <ListItem button onClick={(e) => {
                            if ('serviceWorker' in navigator) {
                                navigator.serviceWorker.ready.then((registration) => {
                                    registration.update();
                                    SharedDataService.showNotification(t('component.account_menu.update_success'), '', 'success');
                                }).catch((error) => {
                                    console.error(error.message);
                                });
                                onItemClick();
                            }
                            //serviceWorkerRegistration.update();
                        }}>
                            <ListItemIcon>
                                <Icon>update</Icon>
                            </ListItemIcon>
                            <ListItemText primary={t('component.account_menu.update')}/>
                        </ListItem>
                        <ListItem button onClick={(e) => {
                            dataService.logout().then(() => {
                                window.location.href = AppURLs.welcome;
                            });
                        }}>
                            <ListItemIcon>
                                <Icon>power_settings_new</Icon>
                            </ListItemIcon>
                            <ListItemText primary={t('component.account_menu.logout')}/>
                        </ListItem>
                    </List>
                </CardContent>
            </Card>
    );
}

export default AccountMenu;