/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global parseFloat */

import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import StyledTreeView from '../../../component/tree/StyledTreeView';
import DataService from '../../../service/DataService';
import { TreeNode } from '../../../util/model/TreeNode';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    root: {
        padding: theme.spacing(2)
    },
    divider: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1)
    }
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [productGroups, setProductGroups] = React.useState(null);
    const [selectedProductGroup, setSelectedProductGroup] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const buildTree = () => {
        const result = [];
        const map = {};
        const roots = [];
        productGroups.forEach(pg => {
            if (pg.parentId) {
                if (!map[pg.parentId]) {
                    map[pg.parentId] = [];
                }
                map[pg.parentId].push(pg);
            } else {
                roots.push(pg);
            }
        });
        function compare(a, b) {
            return a.name > b.name ? 1 : -1;
        }
        const recursiveWalkTree = (productGroup) => {
            const node = new TreeNode(productGroup.id, productGroup.name);
            node.setOrigin(productGroup);
            const children = [];
            const childProductGroups = map[productGroup.externalId];
            if (childProductGroups) {
                childProductGroups.sort(compare);
                childProductGroups.forEach(child => {
                    children.push(recursiveWalkTree(child));
                });
            }
            node.setChildren(children);
            return node;
        };
        roots.sort(compare);
        roots.forEach(root => {
            result.push(recursiveWalkTree(root));
        });
        return result;
    };
    const onProductGroupSelect = (node) => {
        setSelectedProductGroup(node.getOrigin());
    };
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
    if (productGroups === null) {
        return null;
    }
    const tableConfig = new TableConfig(t('m_agrolavka:agrolavka.products'),
        new ApiURL('/agrolavka/protected/product/search')
                .addGetExtraParam('groupId', selectedProductGroup ? selectedProductGroup.id : ''), [
        new TableColumn('name', t('m_agrolavka:products.product_name')).setSortable(),
        new TableColumn('price', t('m_agrolavka:products.product_price'), (row) => {
            return parseFloat(row.price).toFixed(2);
        }).setSortable().width('100px').alignment(ALIGN_RIGHT)
    ], new FormConfig([
        
    ])).setElevation(0);
    return (
            <Paper elevation={1} className={classes.root}>
                <Grid container spacing={2}>
                    <Grid item sm={3}>
                        <Typography variant={'h6'}>{t('m_agrolavka:products.product_groups')}</Typography>
                        <Divider className={classes.divider}/>
                        <StyledTreeView data={buildTree()} onSelect={onProductGroupSelect}/>
                    </Grid>
                    <Grid item sm={9}>
                        {selectedProductGroup ? <DataTable tableConfig={tableConfig}/> : null}
                    </Grid>
                </Grid>
            </Paper>
    );
}

export default Products;
