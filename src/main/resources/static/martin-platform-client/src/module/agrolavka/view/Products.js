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
    
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    
    return (
            <div>TODO</div>
    );
}

export default Products;
