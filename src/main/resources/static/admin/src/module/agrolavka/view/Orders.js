/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import Link from '@material-ui/core/Link';
import Badge from '@material-ui/core/Badge';
import Icon from '@material-ui/core/Icon';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import { requestFirebaseToken } from '../../../conf/firebase';
import AppURLs from '../../../conf/app-urls';
import { NavLink } from "react-router-dom";
import moment from 'moment';
import Price from '../component/Price';
import { ORDER_CREATED } from '../constants/NotificationTopics';
import useAuth from '../../../hooks/useAuth';
import useNotification from '../../../hooks/useNotification';
import useOrdersForPrint from '../hooks/useOrdersForPrint';
import Dropdown from '../../../component/form/input/Dropdown';
import { WAITING_FOR_APPROVAL, APPROVED, DELIVERY, CLOSED} from '../constants/OrderStatus';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    notificationsOn: {
        color: theme.palette.success.main
    },
    notificationsOff: {
        color: theme.palette.error.main
    },
    printAdd: {
        color: theme.palette.success.main
    },
    printRemove: {
        color: theme.palette.error.main
    }
}));

function Orders() {
    const { t } = useTranslation();
    const { permissions, updatePermissions } = useAuth();
    const { showNotification } = useNotification();
    const { addOrder, removeOrder, getOrders } = useOrdersForPrint();
    const classes = useStyles();
    const [tableConfig, setTableConfig] = React.useState(null);
    const [notificationsOn, setNotificationsOn] = React.useState(null);
    const [filterText, setFilterText] = React.useState('');
    const [filterStatus, setFilterStatus] = React.useState('');
    const [filterShowArchive, setFilterShowArchive] = React.useState(false);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const ordersFilter = () => {
        return (
                <Grid container spacing={2}>
                    <Grid item xs={12} md={6}>
                        <TextField value={filterText} label={t('m_agrolavka:order.search_text')} variant="outlined" fullWidth onChange={(e) => {
                            setFilterText(e.target.value);
                        }}/>
                    </Grid>
                    <Grid item xs={12} md={3}>
                        <Dropdown label={t('m_agrolavka:order.status')} value={filterStatus} name={'status'} fullWidth variant={'outlined'} options={[
                            { label: t('m_agrolavka:order.nostatus'), value: '' },
                            { label: t('m_agrolavka:order.statusConst.' + WAITING_FOR_APPROVAL), value: WAITING_FOR_APPROVAL },
                            { label: t('m_agrolavka:order.statusConst.' + APPROVED), value: APPROVED },
                            { label: t('m_agrolavka:order.statusConst.' + DELIVERY), value: DELIVERY },
                            { label: t('m_agrolavka:order.statusConst.' + CLOSED), value: CLOSED }
                        ]} onChange={(e) => {
                            setFilterStatus(e.target.value);
                        }}>
                        </Dropdown>
                    </Grid>
                    <Grid item xs={12} md={3}>
                        <FormControlLabel control={(
                            <Switch checked={filterShowArchive} onChange={(e) => {
                                setFilterShowArchive(e.target.checked);
                            }}/>
                        )} label={t('m_agrolavka:order.show_archive')}/>
                    </Grid>
                </Grid>
        );
    };
    const toolbarBefore = () => {
        const notificationButton = notificationsOn ? (
                <Tooltip title={t('m_agrolavka:orders.disable_notifications')} key="0">
                    <IconButton onClick={() => {
                        requestFirebaseToken().then(token => {
                            if (token) {
                                dataService.put(`/platform/firebase/topic/unsubscribe/${token}/${ORDER_CREATED}`).then(() => {
                                    updatePermissions(() => {
                                        showNotification(t('m_agrolavka:orders.disable_notifications_success'), '', 'success');
                                        setNotificationsOn(false);
                                    });
                                });
                            }
                        });
                    }}>
                        <Icon className={classes.notificationsOn}>notifications</Icon>
                    </IconButton>
                </Tooltip>
                ) : (
                <Tooltip title={t('m_agrolavka:orders.enable_notifications')} key="1">
                    <IconButton onClick={() => {
                        requestFirebaseToken().then(token => {
                            if (token) {
                                dataService.put(`/platform/firebase/topic/subscribe/${token}/${ORDER_CREATED}`).then(() => {
                                    updatePermissions(() => {
                                        showNotification(t('m_agrolavka:orders.enable_notifications_success'), '', 'success');
                                        setNotificationsOn(true);
                                    });
                                });
                            }
                        });
                    }}>
                        <Icon className={classes.notificationsOff}>notifications_off</Icon>
                    </IconButton>
                </Tooltip>
        );
        console.log(getOrders());
        const ordersForPrintButton = getOrders().length === 0 ? null : (
                <Tooltip title={t('m_agrolavka:orders.print')}>
                    <IconButton onClick={() => {
                        console.log("open dialog");
                    }}>
                        <Icon>print</Icon>
                    </IconButton>
                </Tooltip>
        );
        const buttons = [];
        buttons.push(notificationButton);
        return buttons;
    };
    
    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/order',
                null,
                null,
                '/agrolavka/protected/order'
        );
        apiUrl.addGetExtraParam('status', filterStatus);
        apiUrl.addGetExtraParam('text', filterText);
        apiUrl.addGetExtraParam('show_closed', filterShowArchive);
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.orders'), apiUrl, [
            new TableColumn('id', t('m_agrolavka:orders.order_number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                const navLink = <NavLink to={AppURLs.app + '/agrolavka/order/' + row.id} color="primary">{num}</NavLink>;
                return row.adminComment ? (
                    <Tooltip title={row.adminComment}>
                        <Badge variant="dot" color="primary" anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'left'
                        }}>
                            {navLink}
                        </Badge>
                    </Tooltip>
                ) : navLink;
            }).width('100px').setSortable(),
            new TableColumn('phone', t('m_agrolavka:orders.order_number'), (row) => {
                return <Link href={'tel:+375' + row.phone.replace(/\D/g,'')} color="primary">{row.phone}</Link>;
            }).width('170px'),
            new TableColumn('address', t('m_agrolavka:orders.address'), (row) => {
                const adr = row.address;
                const europost = row.europostLocationSnapshot;
                if (row.oneClick) {
                    return <i>{t('m_agrolavka:orders.one_click')}</i>;
                } else if (adr) {
                    return `${adr.postcode ? adr.postcode + ' ' : ''}${adr.city}, ${adr.street} д.${adr.house} ${adr.flat ? 'кв.' + adr.flat : ''}`;
                } else if (europost) {
                    return t('m_agrolavka:orders.delivery_europost');
                }
                return t('m_agrolavka:orders.delivery_self');
            }),
            new TableColumn('status', t('m_agrolavka:order.status'), (row) => {
                return t('m_agrolavka:order.statusConst.' + row.status);
            }).width('250px'),
            new TableColumn('created', t('m_agrolavka:order.created'), (row) => {
                return moment(row.created).locale('ru').format('DD.MM.yyyy HH:mm');
            }).width('160px').setSortable().alignment(ALIGN_RIGHT),
            new TableColumn('total', t('m_agrolavka:orders.order_total'), (row) => {
                let total = 0;
                row.positions.forEach(pos => {
                    total += pos.quantity * pos.price;
                });
                return <Price price={total} />;
            }).width('170px').alignment(ALIGN_RIGHT),
            new TableColumn('btn_print_add', '', (row) => {
                return (
                        <Tooltip title={t('m_agrolavka:orders.print_add')}>
                            <IconButton onClick={() => addOrder(row)}>
                                <Icon className={classes.printAdd}>playlist_add</Icon>
                            </IconButton>
                        </Tooltip>
                );
            }).width('40px').alignment(ALIGN_RIGHT).asAction(),
            new TableColumn('btn_print', '', (row) => {
                return (
                        <Tooltip title={t('m_agrolavka:orders.print')}>
                            <IconButton onClick={() => {
                                dataService.getFile('/api/agrolavka/protected/order/print/' + row.id, `Заказ №${row.id}.pdf`).then(resp => {
                                });
                            }}>
                                <Icon>print</Icon>
                            </IconButton>
                        </Tooltip>
                );
            }).width('40px').alignment(ALIGN_RIGHT).asAction()
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide()
        ])).setElevation(1).setFilter(ordersFilter()).setToolbarActionsBefore(toolbarBefore());
        setTableConfig(newTableConfig);
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (notificationsOn !== null) {
            updateTable();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [notificationsOn]);
    useEffect(() => {
        if (notificationsOn === null && permissions && permissions.userAgent) {
            setNotificationsOn(permissions.userAgent.notificationSubscriptions.filter(s => s.topic === ORDER_CREATED).length > 0);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [notificationsOn]);
    useEffect(() => {
        updateTable();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [filterStatus, filterText, filterShowArchive]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (tableConfig === null) {
        return null;
    }
    return (
            <DataTable tableConfig={tableConfig}/>
    );
}

export default Orders;

