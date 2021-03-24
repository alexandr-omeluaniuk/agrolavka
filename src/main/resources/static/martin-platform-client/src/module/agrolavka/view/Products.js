/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import TextField from '@material-ui/core/TextField';
import Avatar from '@material-ui/core/Avatar';
import DataService from '../../../service/DataService';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import ProductsGroups from './ProductsGroups';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    root: {
        padding: theme.spacing(2),
        overflowX: 'auto'
    },
    divider: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1)
    },
    image: {
        borderRadius: 0
    }
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [selectedProductGroup, setSelectedProductGroup] = React.useState(null);
    const [tableConfig, setTableConfig] = React.useState(null);
    const [filterProductName, setFilterProductName] = React.useState(null);
    const [filterCode, setFilterCode] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const productsFilter = () => {
        return (
                <Grid container spacing={2}>
                    <Grid item xs={12} md={9}>
                        <TextField label={t('m_agrolavka:products.product_name')} variant="outlined" fullWidth onChange={(e) => {
                            setFilterProductName(e.target.value);
                        }}/>
                    </Grid>
                    <Grid item xs={12} md={3}>
                        <TextField label={t('m_agrolavka:products.product_code')} variant="outlined" fullWidth onChange={(e) => {
                            setFilterCode(e.target.value);
                        }}/>
                    </Grid>
                </Grid>
        );
    };
    const toolbarBefore = () => {
        return (
                <Tooltip title={t('m_agrolavka:products.synchronize')}>
                    <IconButton onClick={synchronizeData}>
                        <Icon color="primary">sync_alt</Icon>
                    </IconButton>
                </Tooltip>
        );
    };
    const synchronizeData = () => {
        dataService.put('/agrolavka/protected/mysklad/synchronize').then(resp => {
            //setProductGroups(null);
        });
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/product/search',
                selectedProductGroup ? '/platform/entity/ss.entity.agrolavka.Product' : null,
                '/platform/entity/ss.entity.agrolavka.Product',
                '/platform/entity/ss.entity.agrolavka.Product'
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
        apiUrl.addGetExtraParam('search_text', filterProductName ? filterProductName : '');
        apiUrl.addGetExtraParam('code', filterCode ? filterCode : '');
        const newTableConfig = new TableConfig(
                t('m_agrolavka:agrolavka.products') + (selectedProductGroup ? ` (${selectedProductGroup.name})` : ''), apiUrl, [
            new TableColumn('avatar', '', (row) => {
                return <Avatar className={classes.image} alt={row.name}
                        src={`/api/agrolavka/public/product-image/${row.id}?timestamp=${new Date().getTime()}`} />;
            }).setSortable().width('40px'),
            new TableColumn('name', t('m_agrolavka:products.product_name')).setSortable(),
            new TableColumn('group', t('m_agrolavka:products.product_groups'), (row) => {
                return row.group ? row.group.name : '';
            }).width('200px'),
            new TableColumn('code', t('m_agrolavka:products.product_code')).setSortable().width('160px').alignment(ALIGN_RIGHT),
            new TableColumn('article', t('m_agrolavka:products.product_article')).setSortable().width('160px').alignment(ALIGN_RIGHT),
            new TableColumn('buyPrice', t('m_agrolavka:products.product_buy_price'), (row) => {
                return parseFloat(row.buyPrice).toFixed(2);
            }).setSortable().width('100px').alignment(ALIGN_RIGHT),
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
            new FormField('article', TYPES.TEXTFIELD, t('m_agrolavka:products.product_article')).setGrid({xs: 12, md: 9}).validation([
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('buyPrice', TYPES.MONEY, t('m_agrolavka:products.product_buy_price')).setGrid({xs: 12, md: 3}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0})
            ]).setAttributes({ decimalScale: 2, suffix: ' BYN', align: 'right' }),
            new FormField('description', TYPES.HTML_EDITOR, t('m_agrolavka:products.product_description')).setGrid({xs: 12})
                    .setAttributes({ rows: 6, labelWidth: 200 }),
            new FormField('images', TYPES.IMAGES, t('m_agrolavka:products.product_images')).setGrid({xs: 12})
        ]).setBeforeOnEditRecord((record) => {
            return new Promise((resolve) => {
                dataService.get('/agrolavka/protected/product/images/' + record.id).then(images => {
                    images.forEach(i => {
                        i.data = `data:${i.type};base64,${i.data}`;
                    });
                    record.images = images;
                    resolve(record);
                });
            });
        })).setElevation(1).setFilter(productsFilter()).setToolbarActionsBefore(toolbarBefore());
        setTableConfig(newTableConfig);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedProductGroup, filterProductName, filterCode]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (tableConfig === null) {
        return null;
    }
    return (
            <Grid container spacing={2}>
                <Grid item sm={12} md={3}>
                    <ProductsGroups onSelect={setSelectedProductGroup}/>
                </Grid>
                <Grid item sm={12} md={9}>
                    <DataTable tableConfig={tableConfig}/>
                </Grid>
            </Grid>
    );
}

export default Products;
