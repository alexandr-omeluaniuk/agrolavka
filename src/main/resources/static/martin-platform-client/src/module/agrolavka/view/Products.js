/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import StyledTreeView from '../../../component/tree/StyledTreeView';
import DataService from '../../../service/DataService';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [productGroups, setProductGroups] = React.useState(null);
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (productGroups === null) {
            dataService.get('/agrolavka/protected/ProductsGroup').then(resp => {
                setProductGroups(resp.data);
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [productGroups]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    return (
            <Grid container>
                <Grid item sm={3}>
                    <StyledTreeView />
                </Grid>
                <Grid item sm={9}>
                    Table
                </Grid>
            </Grid>
    );
}

export default Products;
