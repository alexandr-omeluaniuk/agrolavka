/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import StyledTreeView from '../../../component/tree/StyledTreeView';
import DataService from '../../../service/DataService';
import { TreeNode } from '../../../util/model/TreeNode';
import Form from './../../../component/form/Form';
import ConfirmDialog from '../../../component/window/ConfirmDialog';
import FormDialog from '../../../component/window/FormDialog';
import { FormConfig, FormField, Validator } from '../../../util/model/TableConfig';
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
    title: {
        display: 'flex',
        alignItems: 'center',
        flex: 1
    },
    titleIcon: {
        marginRight: theme.spacing(1)
    },
    toolbar: {
        display: 'flex',
        justifyContent: 'space-between'
    },
    newGroup: {
        color: theme.palette.success.main
    },
    editGroup: {
        color: theme.palette.warning.main
    },
    deleteGroup: {
        color: theme.palette.error.main
    }
}));

function ProductsGroups(props) {
    const classes = useStyles();
    const { onSelect } = props;
    const { t } = useTranslation();
    const [productGroups, setProductGroups] = React.useState(null);
    const [formConfig, setFormConfig] = React.useState(null);
    const [formTitle, setFormTitle] = React.useState('');
    const [formOpen, setFormOpen] = React.useState(false);
    const [formDisabled, setFormDisabled] = React.useState(false);
    const [record, setRecord] = React.useState(null);
    const [selectedProductGroup, setSelectedProductGroup] = React.useState(null);
    // ----------------------------------------------------- METHODS ----------------------------------------------------------------------
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
        roots.unshift(new TreeNode(-1, t('m_agrolavka:products.all_product_groups')));
        roots.forEach(root => {
            result.push(recursiveWalkTree(root));
        });
        return result;
    };
    const onProductGroupSelect = (node) => {
        if (onSelect) {
            onSelect(node.getId() > 0 ? node.getOrigin() : null);
        }
        setSelectedProductGroup(node.getId() > 0 ? node.getOrigin() : null);
    };
    const onFormSubmitAction = (data) => {
        setFormDisabled(true);
        if (selectedProductGroup) {
            data.externalId = selectedProductGroup.externalId;
        }
        dataService.post('/platform/entity/ss.entity.agrolavka.ProductsGroup', data).then(() => {
            setProductGroups(null);
            setFormDisabled(false);
            setFormOpen(false);
        });
    };
    const onCreateNewGroup = () => {
        setFormTitle(t('m_agrolavka:products_groups.new_group'));
        setFormOpen(true);
        setRecord(null);
    };
    // -------------------------------------------------------- HOOKS ---------------------------------------------------------------------
    useEffect(() => {
        if (productGroups === null) {
            dataService.get('/platform/entity/ss.entity.agrolavka.ProductsGroup').then(resp => {
                setProductGroups(resp.data);
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [productGroups]);
    useEffect(() => {
        if (formConfig === null) {
            setFormConfig(new FormConfig([
                new FormField('id', TYPES.ID).hide(),
                new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:products_groups.product_group_name'))
                        .setGrid({xs: 12}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ])
            ]));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [formConfig]);
    // --------------------------------------------------------- RENDERING ----------------------------------------------------------------
    if (productGroups === null) {
        return null;
    }
    return (
            <Paper elevation={1} className={classes.root}>
                <div className={classes.toolbar}>
                    <Typography variant={'h6'} className={classes.title}>
                        <Icon className={classes.titleIcon}>account_tree</Icon> {t('m_agrolavka:products.product_groups')}
                    </Typography>
                    <div>
                        {selectedProductGroup ? (
                            <React.Fragment>
                                <Tooltip title={t('m_agrolavka:products_groups.delete_group')}>
                                    <IconButton className={classes.deleteGroup}>
                                        <Icon>delete</Icon>
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title={t('m_agrolavka:products_groups.edit_group')}>
                                    <IconButton className={classes.editGroup}>
                                        <Icon>edit</Icon>
                                    </IconButton>
                                </Tooltip>
                            </React.Fragment>
                        ) : null}
                        <Tooltip title={t('m_agrolavka:products_groups.new_group')}>
                            <IconButton className={classes.newGroup} onClick={onCreateNewGroup}>
                                <Icon>add</Icon>
                            </IconButton>
                        </Tooltip>
                    </div>
                </div>
                <Divider className={classes.divider}/>
                <StyledTreeView data={buildTree()} onSelect={onProductGroupSelect}/>
                {formConfig ? (
                    <FormDialog title={formTitle} open={formOpen} handleClose={() => setFormOpen(false)}>
                        <Form formConfig={formConfig} onSubmitAction={onFormSubmitAction} record={record}
                            disabled={formDisabled}/>
                    </FormDialog>
                ) : null}
            </Paper>
    );
}

export default ProductsGroups;

