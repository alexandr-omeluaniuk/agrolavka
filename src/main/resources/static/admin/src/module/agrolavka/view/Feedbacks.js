/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import AppURLs from '../../../conf/app-urls';
import { NavLink } from "react-router-dom";
import moment from 'moment';

function Feedbacks() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/platform/entity/ss.entity.agrolavka.Feedback',
                null,
                null,
                '/platform/entity/ss.entity.agrolavka.Feedback'
        );
        apiUrl.addGetExtraParam('order_by', 'created');
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.feedbacks'), apiUrl, [
            new TableColumn('id', t('m_agrolavka:feedbacks.feedback_number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                return <NavLink to={AppURLs.app + '/agrolavka/feedback/' + row.id} color="primary">{num}</NavLink>;
            }).width('100px').setSortable(),
            new TableColumn('contact', t('m_agrolavka:feedbacks.contact'), (row) => {
                return row.contact;
            }).width('230px'),
            new TableColumn('message', t('m_agrolavka:feedbacks.message'), (row) => {
                return row.message;
            }),
            new TableColumn('created', t('m_agrolavka:feedbacks.created'), (row) => {
                return moment(row.created).locale('ru').format('DD.MM.yyyy HH:mm');
            }).width('160px').setSortable().alignment(ALIGN_RIGHT)
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide()
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

export default Feedbacks;

