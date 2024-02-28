/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import DataTable from '../../../component/datatable/DataTable';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT } from '../../../util/model/TableConfig';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import AppURLs from '../../../conf/app-urls';

const useStyles = makeStyles(theme => ({
    active: {
        color: theme.palette.success.main
    },
    inactive: {
        color: theme.palette.grey.main
    }
}));

function Users() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    useEffect(() => {
        if (tableConfig === null) {
            setTableConfig(new TableConfig(t('m_administrator:administrator.users'), '/platform/security/users', [
                new TableColumn('fullname', t('m_administrator:users.fullname'), (row) => {
                    return `${row.firstname ? `${row.firstname} ` : ''}${row.lastname}`;
                }).setSortable().width('300px'),
                new TableColumn('email', t('m_administrator:users.email')).setSortable(),
                new TableColumn('status', t('m_administrator:users.status'), (row) => {
                    return t(`m_administrator:users.status_const.${row.status}`);
                }).setSortable().width('250px'),
                new TableColumn('active', t('m_administrator:users.active'), (row) => {
                    return <Icon className={row.active ? classes.active : classes.inactive}>check_circle</Icon>;
                }).width('40px').alignment(ALIGN_RIGHT),
                new TableColumn('', '', (row) => {
                    return row.validationString ? (
                            <Tooltip title={t('m_administrator:users.open_registration_link')}>
                                <IconButton onClick={() => {
                                    window.open(AppURLs.registration.replace(':validationString', row.validationString), '_blank');
                                }}>
                                    <Icon>how_to_reg</Icon>
                                </IconButton>
                            </Tooltip>
                    ) : null;
                }).width('40px').alignment(ALIGN_RIGHT).asAction()
            ], new FormConfig([
                new FormField('id', TYPES.ID).hide(),
                new FormField('firstname', TYPES.TEXTFIELD, t('m_administrator:users.firstname')).setGrid({xs: 6}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ]),
                new FormField('lastname', TYPES.TEXTFIELD, t('m_administrator:users.lastname')).setGrid({xs: 6}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ]),
                new FormField('email', TYPES.TEXTFIELD, t('m_administrator:users.email')).setGrid({xs: 12}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.EMAIL),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ])
            ])));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [tableConfig]);
    if (!tableConfig) {
        return null;
    }
    return (
        <DataTable tableConfig={tableConfig}/>
    );
}

export default Users;
