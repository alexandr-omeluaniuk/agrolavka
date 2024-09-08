/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
import React, { useEffect, useContext } from 'react';
import { ToolbarContext } from '../../../context/ToolbarContext';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import { FormConfig, FormField, Validator } from '../../../util/model/TableConfig';
import { useTranslation } from 'react-i18next';
import DataService from '../../../service/DataService';
import { makeStyles } from '@material-ui/core/styles';
import Form from './../../../component/form/Form';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Paper from '@material-ui/core/Paper';
import Icon from '@material-ui/core/Icon';
import Divider from '@material-ui/core/Divider';
import { NavLink } from "react-router-dom";
import useNotification from '../../../hooks/useNotification';

const dataService = new DataService();

const useStyles = makeStyles(theme => ({
    paper: {
        padding: theme.spacing(2)
    },
    title: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    divider: {
        marginBottom: theme.spacing(4),
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1)
    }
}));

function SystemSettings(props) {
    const classes = useStyles();
    const [systemSettings, setSystemSettings] = React.useState(null);
    const [formDisabled, setFormDisabled] = React.useState(false);
    const { showNotification } = useNotification();
    
    const formConfig = new FormConfig([
        new FormField('id', TYPES.ID).hide(),
        new FormField('showAllProductVariants', TYPES.BOOLEAN, 'Показать все модификации продуктов')
            .setGrid({xs: 12, md: 12}),
        new FormField('deliveryConditions', TYPES.TEXTAREA, 'Об условиях доставки').setGrid({xs: 12, md: 4}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 65535})
        ]).setAttributes({ rows: 10, labelWidth: 200 }),
        new FormField('deliveryOrder', TYPES.TEXTAREA, 'Об условиях оформления заказа').setGrid({xs: 12, md: 4}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 65535})
        ]).setAttributes({ rows: 10, labelWidth: 200 }),
        new FormField('deliveryPaymentDetails', TYPES.TEXTAREA, 'Способы оплаты').setGrid({xs: 12, md: 4}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 65535})
        ]).setAttributes({ rows: 10, labelWidth: 200 })
    ]);
    
    useEffect(() => {
        if (systemSettings == null) {
            dataService.get('/agrolavka/protected/system-settings').then(data => {
                setSystemSettings(data);
            });
        }
    }, [systemSettings]);
    
    const onFormSubmitAction = (data) => {
        setFormDisabled(true);
        dataService.put('/agrolavka/protected/system-settings', data).then(() => {
            setFormDisabled(false);
            setSystemSettings(null);
            showNotification("Успешно сохранено", '', 'success');
        });
    };
    
    if (systemSettings === null) {
        return null;
    }
    
    return (
        <Paper elevation={1} className={classes.paper}>
            <div className={classes.title}>
                <Typography variant="h6">Системные настройки</Typography>
            </div>
            <Divider className={classes.divider}/>
            <Form formConfig={formConfig} onSubmitAction={onFormSubmitAction} record={systemSettings} disabled={formDisabled}/>
        </Paper>
    );
}

export default SystemSettings;

