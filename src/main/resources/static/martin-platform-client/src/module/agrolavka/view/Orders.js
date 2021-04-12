/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    
}));

function Orders() {
    const classes = useStyles();
    const { t } = useTranslation();
    return (
            <div>TODO</div>
    );
}

export default Orders;

