/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ALIGN_RIGHT, ApiURL, Validator } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import moment from 'moment';
import { Chip, Icon, IconButton, Tooltip } from '@material-ui/core';
import FormDialog from '../../../component/window/FormDialog';
import Form from '../../../component/form/Form';
import DataService from '../../../service/DataService';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    addItem: {
        color: theme.palette.success.main
    },
    chip: {
        marginRight: theme.spacing(1)
    }
}));

function ProductAttributes() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    const classes = useStyles();
    const [formConfig, setFormConfig] = React.useState(null);
    const [formTitle, setFormTitle] = React.useState('');
    const [formOpen, setFormOpen] = React.useState(false);
    const [formDisabled, setFormDisabled] = React.useState(false);
    const [record, setRecord] = React.useState(null);
    const [reload, setReload] = React.useState(false);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const addItem = (row) => {
        setFormTitle(t('m_agrolavka:attributes.newItem') + ' [' + row.name + ']');
        setRecord({
            product_attribute_id: row.id
        });
        setFormOpen(true);
    };

    const onDeleteItem = async (id) => {
        await dataService.delete('/agrolavka/protected/product-attributes/item/' + id);
        setReload(!reload);
    }

    const onEditItem = (item) => {
        setFormTitle(t('m_agrolavka:attributes.editItem') + ' [' + item.name + ']');
        setRecord(item);
        setFormOpen(true);
    };

    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes',
                '/agrolavka/protected/product-attributes'
        );
        apiUrl.addGetExtraParam('order_by', 'name');
        apiUrl.addGetExtraParam('order', 'asc');
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.feedbacks'), apiUrl, [
            new TableColumn('id', t('m_agrolavka:attributes.number'), (row) => {
                let num = row.id.toString();
                while (num.length < 5) num = "0" + num;
                return num;
            }).width('100px').setSortable(),
            new TableColumn('name', t('m_agrolavka:attributes.name'), (row) => {
                return row.name;
            }).width('230px'),
            new TableColumn('items', t('m_agrolavka:attributes.items'), (row) => {
                const items = [];
                const attrList = row.items;
                attrList.sort((a, b) => {
                    if (a.name < b.name) {
                        return -1;
                    }
                    if (a.name > b.name) {
                        return 1;
                    }
                    return 0;
                });
                attrList.forEach(i => {
                    items.push(
                        <Chip label={i.name} clickable key={i.id} color="secondary" className={classes.chip}
                            onDelete={() => onDeleteItem(i.id)} onClick={() => onEditItem(i)}/>
                    );
                });
                return <React.Fragment>
                    {items}
                </React.Fragment>;
            }),
            new TableColumn('created_date', t('m_agrolavka:attributes.created'), (row) => {
                return moment(row.created).locale('ru').format('DD.MM.yyyy HH:mm');
            }).width('160px').alignment(ALIGN_RIGHT),
            new TableColumn('btn_add_item', '', (row) => {
                return <Tooltip title={t('m_agrolavka:attributes.addItem')}>
                            <IconButton onClick={() => addItem(row)}>
                                <Icon className={classes.addItem}>post_add</Icon>
                            </IconButton>
                        </Tooltip>;
            }).width('40px').alignment(ALIGN_RIGHT).asAction()
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:attributes.name')).setGrid({xs: 12, md: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('color', TYPES.COLOR, t('m_agrolavka:attributes.color')).setGrid({xs: 12, md: 12})
        ])).setElevation(1);
        setTableConfig(newTableConfig);
    };

    const onFormSubmitAction = async (data) => {
        setFormDisabled(true);
        if (data.id) {
            await dataService.put('/agrolavka/protected/product-attributes/item', data);
        } else {
            const parentId = data.product_attribute_id;
            delete data.product_attribute_id;
            await dataService.post('/agrolavka/protected/product-attributes/item/' + parentId, data);
        }
        setFormDisabled(false);
        setFormOpen(false);
        setReload(!reload);
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (tableConfig === null) {
            updateTable();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [tableConfig]);
    useEffect(() => {
        if (formConfig === null) {
            setFormConfig(new FormConfig([
                new FormField('id', TYPES.ID).hide(),
                new FormField('product_attribute_id', TYPES.INTEGER_NUMBER).hide(),
                new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:attributes.itemName'))
                        .setGrid({xs: 12, md: 12}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ])
            ]));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [formConfig]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (tableConfig === null) {
        return null;
    }
    return (
        <React.Fragment>
            <DataTable tableConfig={tableConfig} reload={reload}/>
            {formConfig ? (
                <FormDialog title={formTitle} open={formOpen} handleClose={() => setFormOpen(false)} fullScreen={false}>
                    <Form formConfig={formConfig} onSubmitAction={onFormSubmitAction} record={record}
                        disabled={formDisabled}/>
                </FormDialog>
            ) : null}
        </React.Fragment>
    );
}

export default ProductAttributes;
