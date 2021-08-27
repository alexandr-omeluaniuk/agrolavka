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
import Avatar from '@material-ui/core/Avatar';
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
    },
    favoriteIcon: {
        color: theme.palette.secondary.main
    },
    small: {
        width: theme.spacing(3),
        height: theme.spacing(3),
        marginRight: theme.spacing(1)
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
    const [confirmDialogOpen, setConfirmDialogOpen] = React.useState(false);
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
            if (productGroup.topCategory) {
                node.setIcon(<Icon className={classes.favoriteIcon}>favorite</Icon>);
            }
            node.setAvatar(<Avatar className={classes.small} src={
                productGroup.hasImages ? `/media/${productGroup.images[0].fileNameOnDisk}` : `/assets/img/no-image.png`} />);
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
        if (data.id) {
            data.parentId = record.parentId;
            data.externalId = record.externalId;
            dataService.put('/platform/entity/ss.entity.agrolavka.ProductsGroup', data).then((group) => {
                const updated = [];
                productGroups.forEach(pg => {
                    updated.push(group.id === pg.id ? group : pg);
                });
                setProductGroups(updated);
                setFormDisabled(false);
                setFormOpen(false);
                setSelectedProductGroup(group);
            });
        } else {
            if (selectedProductGroup) {
                data.parentId = selectedProductGroup.externalId;
            }
            dataService.post('/platform/entity/ss.entity.agrolavka.ProductsGroup', data).then((group) => {
                const updated = [];
                productGroups.forEach(pg => {
                    updated.push(pg);
                });
                updated.push(group);
                setProductGroups(updated);
                setFormDisabled(false);
                setFormOpen(false);
            });
        }
    };
    const onCreateNewGroup = () => {
        setFormTitle(t('m_agrolavka:products_groups.new_group'));
        setFormOpen(true);
        setRecord(null);
    };
    const onEditGroup = () => {
        setFormTitle(t('m_agrolavka:products_groups.edit_group'));
        dataService.get('/agrolavka/protected/product-group/images/' + selectedProductGroup.id).then(images => {
            console.log(images);
            images.forEach(i => {
                i.data = `data:${i.type};base64,${i.data}`;
            });
            selectedProductGroup.images = images;
            setRecord(selectedProductGroup);
            setFormOpen(true);
        });
    };
    const onDeleteGroup = () => {
        setConfirmDialogOpen(true);
    };
    const doDelete = () => {
        setConfirmDialogOpen(false);
        dataService.delete('/platform/entity/ss.entity.agrolavka.ProductsGroup/' + selectedProductGroup.id).then(() => {
            setProductGroups(null);
        });
    };
    const normalize = () => {
        dataService.put('/agrolavka/protected/product-group/normalize').then(() => {
            setProductGroups(null);
        });
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
                        .setGrid({xs: 12, md: 9}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ]),
                new FormField('topCategory', TYPES.BOOLEAN, t('m_agrolavka:products_groups.product_group_top_category'))
                        .setGrid({xs: 12, md: 3}),
                new FormField('description', TYPES.HTML_EDITOR, t('m_agrolavka:products_groups.product_group_description'))
                        .setGrid({xs: 12}).validation([
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 4096})
                ]).setAttributes({ rows: 6, labelWidth: 200 }),
                new FormField('seoTitle', TYPES.TEXTFIELD, t('m_agrolavka:products_groups.product_group_seo_title'))
                        .setGrid({xs: 12}).validation([
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ]),
                new FormField('seoDescription', TYPES.TEXTFIELD, t('m_agrolavka:products_groups.product_group_seo_description'))
                        .setGrid({xs: 12})
                        .validation([
                    new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
                ]),
                new FormField('images', TYPES.IMAGES, t('m_agrolavka:products_groups.image')).setGrid({xs: 12})
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
                                    <IconButton className={classes.deleteGroup} onClick={onDeleteGroup}>
                                        <Icon>delete</Icon>
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title={t('m_agrolavka:products_groups.edit_group')}>
                                    <IconButton className={classes.editGroup} onClick={onEditGroup}>
                                        <Icon>edit</Icon>
                                    </IconButton>
                                </Tooltip>
                            </React.Fragment>
                        ) : null}
                        <Tooltip title={t('m_agrolavka:products_groups.normalize')}>
                            <IconButton onClick={normalize}>
                                <Icon>compress</Icon>
                            </IconButton>
                        </Tooltip>
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
                <ConfirmDialog open={confirmDialogOpen} handleClose={() => setConfirmDialogOpen(false)} title={t('component.datatable.delete')}
                    contentText={t('component.datatable.confirm_delete_message')} acceptBtnLabel={t('component.datatable.confirm')}
                    declineBtnLabel={t('component.datatable.cancel')} declineBtnOnClick={() => setConfirmDialogOpen(false)}
                    acceptBtnOnClick={doDelete}/>
            </Paper>
    );
}

export default ProductsGroups;

