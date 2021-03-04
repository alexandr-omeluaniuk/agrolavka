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
import { TreeNode } from '../../../util/model/TreeNode';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    
}));

function Products() {
    const classes = useStyles();
    const { t } = useTranslation();
    const [productGroups, setProductGroups] = React.useState(null);
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
        const recursiveWalkTree = (productGroup) => {
            const node = new TreeNode(productGroup.id, productGroup.name);
            const children = [];
            const childProductGroups = map[productGroup.externalId];
            if (childProductGroups) {
                childProductGroups.forEach(child => {
                    children.push(recursiveWalkTree(child));
                });
            }
            node.setChildren(children);
            return node;
        };
        roots.forEach(root => {
            result.push(recursiveWalkTree(root));
        });
        return result;
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
    return (
            <Grid container>
                <Grid item sm={3}>
                    <StyledTreeView data={buildTree()}/>
                </Grid>
                <Grid item sm={9}>
                    Table
                </Grid>
            </Grid>
    );
}

export default Products;
