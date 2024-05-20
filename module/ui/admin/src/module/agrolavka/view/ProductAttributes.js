/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ALIGN_RIGHT, ApiURL, Validator } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import AppURLs from '../../../conf/app-urls';
import { NavLink } from "react-router-dom";
import moment from 'moment';
import { Icon, IconButton, Tooltip } from '@material-ui/core';

const useStyles = makeStyles(theme => ({
    addItem: {
        color: theme.palette.success.main
    }
}));

function ProductAttributes() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    const classes = useStyles();
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const addItem = () => {

    };

    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes'
        );
        apiUrl.addGetExtraParam('order_by', 'name');
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.feedbacks'), apiUrl, [
            new TableColumn('id', t('m_agrolavka:attributes.number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                return <NavLink to={AppURLs.app + '/agrolavka/feedback/' + row.id} color="primary">{num}</NavLink>;
            }).width('100px').setSortable(),
            new TableColumn('name', t('m_agrolavka:attributes.name'), (row) => {
                return row.name;
            }).width('230px'),
            new TableColumn('items', t('m_agrolavka:attributes.items'), (row) => {
                return row.items;
            }),
            new TableColumn('created_date', t('m_agrolavka:attributes.created'), (row) => {
                return moment(row.created).locale('ru').format('DD.MM.yyyy HH:mm');
            }).width('160px').alignment(ALIGN_RIGHT),
            new TableColumn('btn_add_item', '', (row) => {
                return <Tooltip title={t('m_agrolavka:attributes.addItem')}>
                            <IconButton onClick={() => addItem(row)}>
                                <Icon className={classes.addItem}>post_add</Icon>
                            </IconButton>
                        </Tooltip>;
            }).width('40px').alignment(ALIGN_RIGHT).asAction()
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:attributes.name')).setGrid({xs: 12, md: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ])
        ])).setElevation(1);
        setTableConfig(newTableConfig);
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (tableConfig === null) {
            updateTable();
        }
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

export default ProductAttributes;
