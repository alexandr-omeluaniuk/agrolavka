/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import DataTable from '../../../component/datatable/DataTable';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT } from '../../../util/model/TableConfig';
import Icon from '@material-ui/core/Icon';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';

const useStyles = makeStyles(theme => ({
    active: {
        color: theme.palette.success.main
    },
    inactive: {
        color: theme.palette.grey.main
    }
}));

function Subscriptions() {
    const classes = useStyles();
    const { t } = useTranslation();
    const config = new TableConfig(t('m_core:core.subscriptions'), '/platform/entity/Subscription', [
        new TableColumn('organizationName', t('m_core:organization_name')).setSortable(),
        new TableColumn('subscriptionAdminEmail', t('m_core:subscription_admin_email')).setSortable(),
        new TableColumn('started', t('m_core:subscription_start_date')).width('150px').alignment(ALIGN_RIGHT).setSortable(),
        new TableColumn('expirationDate', t('m_core:subscription_expiration_date')).width('170px').alignment(ALIGN_RIGHT).setSortable(),
        new TableColumn('active', t('m_core:subscription_active'), (row) => {
            return <Icon className={row.active ? classes.active : classes.inactive}>check_circle</Icon>;
        }).width('40px').alignment(ALIGN_RIGHT)
    ], new FormConfig([
        new FormField('id', TYPES.ID).hide(),
        new FormField('organizationName', TYPES.TEXTFIELD, t('m_core:organization_name')).setGrid({xs: 10}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ]),
        new FormField('active', TYPES.BOOLEAN, t('m_core:subscription_active')).setGrid({xs: 2}),
        new FormField('started', TYPES.DATE, t('m_core:subscription_start_date')).setGrid({xs: 6}).validation([
            new Validator(VALIDATORS.REQUIRED)
        ]),
        new FormField('expirationDate', TYPES.DATE, t('m_core:subscription_expiration_date')).setGrid({xs: 6}).validation([
            new Validator(VALIDATORS.REQUIRED)
        ]),
        new FormField('subscriptionAdminEmail', TYPES.TEXTFIELD, t('m_core:subscription_admin_email')).setGrid({xs: 12}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.EMAIL),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ])
    ]));
    return (
        <DataTable tableConfig={config}/>
    );
}

export default Subscriptions;
