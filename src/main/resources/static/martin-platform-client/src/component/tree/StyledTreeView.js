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

const useStyles = makeStyles({
    root: {
        flexGrow: 1,
        width: '100%'
    }
});

function StyledTreeView(props) {
    const classes = useStyles();
    const { data } = props;
    // ----------------------------------------------------- METHODS ----------------------------------------------------------------------
    const renderTree = (nodes) => (
        <TreeItem key={nodes.id} nodeId={nodes.id} label={nodes.name}>
            {Array.isArray(nodes.getChildren()) ? nodes.getChildren().map((node) => renderTree(node)) : null}
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
    data: PropTypes.array.isRequired
};

export default StyledTreeView;