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
    }
}));

function ProductsGroups(props) {
    const classes = useStyles();
    const { onSelect } = props;
    const { t } = useTranslation();
    const [productGroups, setProductGroups] = React.useState(null);
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
                        <Tooltip title={t('m_agrolavka:products_group.new_group')}>
                            <IconButton className={classes.newGroup}>
                                <Icon>add</Icon>
                            </IconButton>
                        </Tooltip>
                    </div>
                </div>
                <Divider className={classes.divider}/>
                <StyledTreeView data={buildTree()} onSelect={onProductGroupSelect}/>
            </Paper>
    );
}

export default ProductsGroups;

