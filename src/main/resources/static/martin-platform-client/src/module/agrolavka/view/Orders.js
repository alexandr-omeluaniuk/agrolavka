/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import Link from '@material-ui/core/Link';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    
}));

function Orders() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
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
            //new TableColumn('name', t('m_agrolavka:products.product_name')).setSortable(),
            new TableColumn('id', t('m_agrolavka:orders.order_number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                return num;
            }).width('100px').setSortable(),
            new TableColumn('id', t('m_agrolavka:orders.order_number'), (row) => {
                return <Link href={'tel:+375' + row.phone.replace(/\D/g,'')} color="secondary">{row.phone}</Link>;
            }).width('170px'),
            new TableColumn('id', t('m_agrolavka:orders.address'), (row) => {
                const adr = row.address;
                if (adr) {
                    return `${adr.postcode ? adr.postcode + ' ' : ''}${adr.city}, ${adr.street} д.${adr.house} ${adr.flat ? 'кв.' + adr.flat : ''}`;
                }
                return '';
            })
//            new TableColumn('code', t('m_agrolavka:products.product_code')).setSortable().width('160px').alignment(ALIGN_RIGHT),
//            new TableColumn('article', t('m_agrolavka:products.product_article')).setSortable().width('160px').alignment(ALIGN_RIGHT),
//            new TableColumn('buyPrice', t('m_agrolavka:products.product_buy_price'), (row) => {
//                return parseFloat(row.buyPrice).toFixed(2);
//            }).setSortable().width('100px').alignment(ALIGN_RIGHT),
//            new TableColumn('price', t('m_agrolavka:products.product_price'), (row) => {
//                return parseFloat(row.price).toFixed(2);
//            }).setSortable().width('100px').alignment(ALIGN_RIGHT)
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide()
        ])).setElevation(1);
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

