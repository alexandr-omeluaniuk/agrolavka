/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect, useContext } from 'react';
import { ToolbarContext } from '../../../context/ToolbarContext';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import moment from 'moment';

const dataService = new DataService();

function Order(props) {
    const id = props.match.params.id;
    const { setTitle, setIcon } = useContext(ToolbarContext);
    const [order, setOrder] = React.useState(null);
    const { t } = useTranslation();
    // -------------------------------------------------------- METHODS -------------------------------------------------------------------
    const getNum = (order) => {
        let num = order.id.toString();
        while (num.length < 5) num = "0" + num;
        return num;
    };
    // -------------------------------------------------------- HOOKS ---------------------------------------------------------------------
    useEffect(() => {
        dataService.get('/agrolavka/protected/order/' + id).then(data => {
            setOrder(data);
            setIcon('payments');
            setTitle(t('m_agrolavka:order.title', {num: getNum(data)}));
        });
    }, [id, setIcon, setTitle, t]);
    if (!order) {
        return null;
    }
    return (
            <Card>
                <CardHeader title={t('m_agrolavka:order.title', {num: getNum(order)})} 
                        subheader={moment(order.created).locale('ru').format('LLL')}>
            
                </CardHeader>
            </Card>
    );
}

export default Order;

