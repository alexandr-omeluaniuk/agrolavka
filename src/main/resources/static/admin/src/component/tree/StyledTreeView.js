/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import Icon from '@material-ui/core/Icon';
import Typography from '@material-ui/core/Typography';
import { TreeNode } from '../../util/model/TreeNode';

const useStyles = makeStyles({
    root: {
        flexGrow: 1,
        width: '100%'
    },
    label: {
        display: 'flex',
        alignItems: 'center'
    },
    labelText: {
        flex: 1
    }
});

function StyledTreeView(props) {
    const classes = useStyles();
    const { data, onSelect } = props;
    // ----------------------------------------------------- METHODS ----------------------------------------------------------------------
    const renderLabel = (treeNode) => {
        return (
                <div className={classes.label}>
                    {treeNode.avatar ? treeNode.avatar : null}
                    <Typography variant={'subtitle1'} className={classes.labelText}>
                        {treeNode.name}
                    </Typography>
                    {treeNode.icon ? treeNode.icon : null}
                </div>
        );
    };
    const renderTree = (treeNode) => (
        <TreeItem key={treeNode.id} nodeId={treeNode.id} label={renderLabel(treeNode)} onClick={() => onSelect(treeNode)}>
            {Array.isArray(treeNode.getChildren()) ? treeNode.getChildren().map((node) => renderTree(node)) : null}
        </TreeItem>
    );
    // ----------------------------------------------------- RENDERING --------------------------------------------------------------------
    return (
            <TreeView className={classes.root} defaultCollapseIcon={(<Icon>expand_more</Icon>)}
                defaultExpandIcon={(<Icon>chevron_right</Icon>)}>
                {data.length > 0 ? data.map((node, idx) => {
                    return renderTree(node);
                }) : null}
            </TreeView>
    );
}

StyledTreeView.propTypes = {
    data: PropTypes.arrayOf(PropTypes.instanceOf(TreeNode)).isRequired,
    onSelect: PropTypes.func
};

export default StyledTreeView;