/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Avatar from '@material-ui/core/Avatar';
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
    const [tableConfig, setTableConfig] = React.useState(null);
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
    useEffect(() => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/product/search',
                '/platform/entity/ss.agrolavka.entity.Product',
                '/platform/entity/ss.agrolavka.entity.Product'
        );
        apiUrl.beforeCreate = (data) => {
            data.group = selectedProductGroup;
            return data;
        };
        apiUrl.beforeUpdate = (data, record) => {
            data.group = record.group;
            data.externalId = record.externalId;
            return data;
        };
        if (selectedProductGroup) {
            apiUrl.addGetExtraParam('group_id', selectedProductGroup.id);
        }
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.products'), apiUrl, [
            new TableColumn('avatar', '', (row) => {
                return <Avatar alt={row.name} src={`/api/agrolavka/public/product-image/${row.id}`} />;
            }).setSortable().width('40px'),
            new TableColumn('name', t('m_agrolavka:products.product_name')).setSortable(),
            new TableColumn('price', t('m_agrolavka:products.product_price'), (row) => {
                return parseFloat(row.price).toFixed(2);
            }).setSortable().width('100px').alignment(ALIGN_RIGHT)
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:products.product_name')).setGrid({xs: 12, md: 9}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('price', TYPES.MONEY, t('m_agrolavka:products.product_price')).setGrid({xs: 12, md: 3}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0})
            ]).setAttributes({ decimalScale: 2, suffix: ' BYN', align: 'right' }),
            new FormField('image', TYPES.IMAGES, t('m_agrolavka:products.product_images')).setGrid({xs: 12})
        ])).setElevation(1);
        setTableConfig(newTableConfig);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedProductGroup]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (productGroups === null) {
        return null;
    }
    return (
            <Grid container spacing={2}>
                <Grid item sm={12} md={3} lg={2}>
                    <Paper elevation={1} className={classes.root}>
                        <Typography variant={'h6'}>{t('m_agrolavka:products.product_groups')}</Typography>
                        <Divider className={classes.divider}/>
                        <StyledTreeView data={buildTree()} onSelect={onProductGroupSelect}/>
                    </Paper>
                </Grid>
                <Grid item sm={12} md={9} lg={10}>
                    <DataTable tableConfig={tableConfig}/>
                </Grid>
            </Grid>
    );
}

export default Products;
