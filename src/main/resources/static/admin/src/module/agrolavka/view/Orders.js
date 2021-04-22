/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import Link from '@material-ui/core/Link';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import { requestFirebaseToken } from '../../../conf/firebase';
import AppURLs from '../../../conf/app-urls';
import { NavLink } from "react-router-dom";
import moment from 'moment';
import Price from '../component/Price';
import { ORDER_CREATED } from '../constants/NotificationTopics';
import useAuth from '../../../hooks/useAuth';
import useNotification from '../../../hooks/useNotification';

let dataService = new DataService();

function Orders() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    const { permissions } = useAuth();
    const { showNotification } = useNotification();
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const toolbarBefore = () => {
        const notificationsOn = permissions && permissions.userAgent
                && permissions.userAgent.notificationSubscriptions.filter(s => s.topic === ORDER_CREATED).length > 0;
        return notificationsOn ? (
                <Tooltip title={t('m_agrolavka:orders.disable_notifications')}>
                    <IconButton onClick={() => {
                        requestFirebaseToken().then(token => {
                            if (token) {
                                dataService.put(`/platform/firebase/topic/unsubscribe/${token}/${ORDER_CREATED}`).then(() => {
                                    showNotification(t('m_agrolavka:orders.disable_notifications_success'), '', 'success');
                                });
                            }
                        });
                    }}>
                        <Icon color="primary">notifications_off</Icon>
                    </IconButton>
                </Tooltip>
                ) : (
                <Tooltip title={t('m_agrolavka:orders.enable_notifications')}>
                    <IconButton onClick={() => {
                        requestFirebaseToken().then(token => {
                            if (token) {
                                dataService.put(`/platform/firebase/topic/subscribe/${token}/${ORDER_CREATED}`).then(() => {
                                    showNotification(t('m_agrolavka:orders.enable_notifications_success'), '', 'success');
                                });
                            }
                        });
                    }}>
                        <Icon color="primary">notifications</Icon>
                    </IconButton>
                </Tooltip>
        );
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (tableConfig !== null) {
            return;
        }
        const apiUrl = new ApiURL(
                '/platform/entity/ss.entity.agrolavka.Order',
                null,
                null,
                '/agrolavka/protected/order'
        );
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.orders'), apiUrl, [
            new TableColumn('id', t('m_agrolavka:orders.order_number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                return <NavLink to={AppURLs.app + '/agrolavka/order/' + row.id} color="primary">{num}</NavLink>;
            }).width('100px').setSortable(),
            new TableColumn('phone', t('m_agrolavka:orders.order_number'), (row) => {
                return <Link href={'tel:+375' + row.phone.replace(/\D/g,'')} color="primary">{row.phone}</Link>;
            }).width('170px'),
            new TableColumn('address', t('m_agrolavka:orders.address'), (row) => {
                const adr = row.address;
                if (adr) {
                    return `${adr.postcode ? adr.postcode + ' ' : ''}${adr.city}, ${adr.street} д.${adr.house} ${adr.flat ? 'кв.' + adr.flat : ''}`;
                }
                return 'Самовывоз';
            }),
            new TableColumn('status', t('m_agrolavka:order.status'), (row) => {
                return t('m_agrolavka:order.statusConst.' + row.status);
            }),
            new TableColumn('created', t('m_agrolavka:order.created'), (row) => {
                return moment(row.created).locale('ru').format('DD.MM.yyyy HH:mm');
            }).width('160px').setSortable().alignment(ALIGN_RIGHT),
            new TableColumn('total', t('m_agrolavka:orders.order_total'), (row) => {
                let total = 0;
                row.positions.forEach(pos => {
                    total += pos.quantity * pos.price;
                });
                return <Price price={total} />;
            }).width('170px').alignment(ALIGN_RIGHT)
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide()
        ])).setElevation(1).setToolbarActionsBefore(toolbarBefore());
        setTableConfig(newTableConfig);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [tableConfig]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (tableConfig === null) {
        return null;
    }
    return (
            <DataTable tableConfig={tableConfig}/>
    );
}

export default Orders;

