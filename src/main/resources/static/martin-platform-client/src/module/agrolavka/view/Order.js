/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect, useContext } from 'react';
import { ToolbarContext } from '../../../context/ToolbarContext';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';

const dataService = new DataService();

function Order(props) {
    const id = props.match.params.id;
    const { setTitle, setIcon } = useContext(ToolbarContext);
    const [order, setOrder] = React.useState(null);
    const { t } = useTranslation();
    useEffect(() => {
        dataService.get('/agrolavka/protected/order/' + id).then(data => {
            setIcon('payments');
            let num = data.id.toString();
            while (num.length < 5) num = "0" + num;
            setTitle(t('m_agrolavka:order.title', {num: num}));
            setOrder(data);
            console.log(data);
        });
    }, [id, setIcon, setTitle]);
    return (
        <div>TODO</div>
    );
}

export default Order;

