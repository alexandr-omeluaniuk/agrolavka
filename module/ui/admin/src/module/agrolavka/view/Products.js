/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import clsx from 'clsx';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import TextField from '@material-ui/core/TextField';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Avatar from '@material-ui/core/Avatar';
import DataService from '../../../service/DataService';
import { TableConfig, TableColumn, FormConfig, ALIGN_RIGHT, ApiURL, Validator } from '../../../util/model/TableConfig';
import DataTable from '../../../component/datatable/DataTable';
import ProductsGroups from './ProductsGroups';
import Price from '../component/Price';
import { NavLink } from "react-router-dom";
import AppURLs from '../../../conf/app-urls';
import Form from '../../../component/form/Form';
import FormDialog from '../../../component/window/FormDialog';
import FormField from '../../../component/form/FormField';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';

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
        borderRadius: '10%',
        border: '1px solid rgba(0,0,0,.275)'
    },
    imageLarge: {
        borderRadius: '10%',
        border: '1px solid red'
    },
    quantityAvailable: {
        color: theme.palette.success.main
    },
    quantityNotAvailable: {
        color: theme.palette.error.main
    },
    quantityZero: {
        color: theme.palette.warning.main
    },
    productGroup: {
        color: theme.palette.text.hint
    },
    filterAvailable: {
        display: 'flex',
        alignItems: 'center'
    },
    hidden: {
        opacity: '.5',
        textDecoration: 'line-through'
    },
    addAttributes: {
        color: theme.palette.success.main
    }
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [selectedProductGroup, setSelectedProductGroup] = React.useState(null);
    const [tableConfig, setTableConfig] = React.useState(null);
    const [filterProductName, setFilterProductName] = React.useState(null);
    const [filterCode, setFilterCode] = React.useState(null);
    const [filterAvailable, setFilterAvailable] = React.useState(false);
    const [filterDiscounts, setFilterDiscounts] = React.useState(false);

    const [formConfig, setFormConfig] = React.useState(null);
    const [formTitle, setFormTitle] = React.useState('');
    const [formOpen, setFormOpen] = React.useState(false);
    const [formDisabled, setFormDisabled] = React.useState(false);
    const [record, setRecord] = React.useState(null);
    const [reload, setReload] = React.useState(false);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const productsFilter = () => {
        return (
                <Grid container spacing={2}>
                    <Grid item xs={12} md={6}>
                        <TextField label={t('m_agrolavka:products.product_name')} variant="outlined" fullWidth onChange={(e) => {
                            setFilterProductName(e.target.value);
                        }}/>
                    </Grid>
                    <Grid item xs={12} md={2}>
                        <TextField label={t('m_agrolavka:products.product_code')} variant="outlined" fullWidth onChange={(e) => {
                            setFilterCode(e.target.value);
                        }}/>
                    </Grid>
                    <Grid item xs={6} md={2} className={classes.filterAvailable}>
                        <FormControlLabel label={t('m_agrolavka:products.available')} control={(
                            <Switch checked={filterAvailable} onChange={(e) => {
                                setFilterAvailable(e.target.checked);
                            }}/>
                        )}/>
                    </Grid>
                    <Grid item xs={6} md={2} className={classes.filterAvailable}>
                        <FormControlLabel label={t('m_agrolavka:products.discounts')} control={(
                            <Switch checked={filterDiscounts} onChange={(e) => {
                                setFilterDiscounts(e.target.checked);
                            }}/>
                        )}/>
                    </Grid>
                </Grid>
        );
    };
    const toolbarBefore = () => {
        return (
                <React.Fragment>
                    <Tooltip title={t('m_agrolavka:group_products_by_volumes')}>
                        <IconButton onClick={groupProductsByVolume}>
                            <Icon color="primary">dashboard</Icon>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title={t('m_agrolavka:backup')}>
                        <IconButton onClick={backup}>
                            <Icon color="primary">backup</Icon>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title={t('m_agrolavka:products.synchronize')}>
                        <IconButton onClick={synchronizeData}>
                            <Icon color="primary">sync_alt</Icon>
                        </IconButton>
                    </Tooltip>
                </React.Fragment>
        );
    };
    const synchronizeData = () => {
        dataService.put('/agrolavka/protected/mysklad/synchronize').then(resp => {
            //setProductGroups(null);
        });
    };
    const backup = () => {
        dataService.getFile('/api/agrolavka/protected/mysklad/backup').then(resp => {
        });
    };
    const groupProductsByVolume = () => {
        dataService.put('/agrolavka/protected/product/group').then(resp => {
        });
    };
    const addAttributes = (product) => {
        setFormTitle(t('m_agrolavka:products.addAttributes') + ' [' + product.name + ']');
        setRecord({});
        setFormOpen(true);
    };
    const onFormSubmitAction = async (data) => {
        setFormDisabled(true);
        if (data.id) {
            
        } else {
            
        }
        setFormDisabled(false);
        setFormOpen(false);
        setReload(!reload);
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/product/search',
                selectedProductGroup ? '/agrolavka/protected/product' : null,
                null,
                '/agrolavka/protected/product'
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
        apiUrl.addGetExtraParam('available', filterAvailable);
        apiUrl.addGetExtraParam('discounts', filterDiscounts);
        apiUrl.addGetExtraParam('includesHidden', true);
        const newTableConfig = new TableConfig(
                t('m_agrolavka:agrolavka.products') + (selectedProductGroup ? ` (${selectedProductGroup.name})` : ''), apiUrl, [
            new TableColumn('avatar', '', (row) => {
                const isLargeImage = row.images && row.images[0] && row.images[0].size > 200000;
                return <Avatar className={isLargeImage ? classes.imageLarge : classes.image} alt={row.name}
                        src={row.images && row.images.length > 0 && row.images[0].fileNameOnDisk 
                        ? `/media/${row.images[0].fileNameOnDisk}?timestamp=${new Date().getTime()}`
                        : `/assets/img/no-image.png`} />;
            }).setSortable().width('40px'),
            new TableColumn('name', t('m_agrolavka:products.product_name'), (row) => {
                return (
                        <React.Fragment>
                            <NavLink to={AppURLs.app + '/agrolavka/product/' + row.id} color="primary">
                                <span className={row.hidden ? classes.hidden : null}>{row.name}</span>
                            </NavLink>
                            <br/>
                            <small className={classes.productGroup}>{row.group ? row.group.name : ''}</small>
                        </React.Fragment>
                );
            }).setSortable(),
            new TableColumn('code', t('m_agrolavka:products.product_code')).setSortable().width('150px').alignment(ALIGN_RIGHT),
            new TableColumn('price', t('m_agrolavka:products.product_price'), (row) => {
                if (row.minPrice && row.maxPrice) {
                    return <React.Fragment>
                        <Price price={row.minPrice}/>
                        <Price price={row.maxPrice}/>
                    </React.Fragment>;
                } else {
                    return <Price price={row.price}/>;
                }
            }).setSortable().width('174px').alignment(ALIGN_RIGHT),
            new TableColumn('quantity', t('m_agrolavka:products.quantity'), (row) => {
                    const quantityStyle = clsx({
                        [classes.quantityZero]: row.quantity === 0,
                        [classes.quantityNotAvailable]: row.quantity < 0,
                        [classes.quantityAvailable]: row.quantity > 0
                    });
                return <span className={quantityStyle}>{row.quantity}</span>;
            }).width('100px').alignment(ALIGN_RIGHT),
            new TableColumn('btn_add_item', '', (row) => {
                if (row.minPrice && row.maxPrice) {
                    return null;
                }
                return <Tooltip title={t('m_agrolavka:products.addAttributes')}>
                            <IconButton onClick={() => addAttributes(row)}>
                                <Icon className={classes.addAttributes}>post_add</Icon>
                            </IconButton>
                        </Tooltip>;
            }).width('40px').alignment(ALIGN_RIGHT).asAction()
        ], new FormConfig([
            
        ]).setBeforeOnEditRecord((record) => {
            return new Promise((resolve) => {
                record.images.forEach(i => {
                    i.data = `/media/` + i.fileNameOnDisk;
                });
                resolve(record);
            });
        })).setElevation(1).setFilter(productsFilter()).setToolbarActionsBefore(toolbarBefore());
        newTableConfig.isRowEditable = (row) => {
            return !(row.minPrice && row.maxPrice);
        };
        newTableConfig.isRowDeletable = (row) => {
            return !(row.minPrice && row.maxPrice);
        };
        newTableConfig.isFormDialog = false;
        setTableConfig(newTableConfig);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedProductGroup, filterProductName, filterCode, filterAvailable, filterDiscounts]);
    useEffect(() => {
        if (formConfig === null) {
            setFormConfig(new FormConfig([
                // new FormField('id', TYPES.ID).hide(),
                // new FormField('product_id', TYPES.INTEGER_NUMBER).hide(),
                // new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:attributes.itemName'))
                //         .setGrid({xs: 12, md: 12}).validation([
                //     new Validator(VALIDATORS.REQUIRED),
                //     new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                // ])
            ]));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [formConfig]);
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
                    {selectedProductGroup ? <DataTable tableConfig={tableConfig} reload={reload}/> : null}
                </Grid>
                {formConfig ? (
                    <FormDialog title={formTitle} open={formOpen} handleClose={() => setFormOpen(false)} fullScreen={false}>
                        <Form formConfig={formConfig} onSubmitAction={onFormSubmitAction} record={record}
                            disabled={formDisabled}/>
                    </FormDialog>
                ) : null}
            </Grid>
    );
}

export default Products;
