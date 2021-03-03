/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
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
    //const { data } = props;
    const data = {
  id: 'root',
  name: 'Parent',
  children: [
    {
      id: '1',
      name: 'Child - 1',
    },
    {
      id: '3',
      name: 'Child - 3',
      children: [
        {
          id: '4',
          name: 'Child - 4',
        },
      ],
    },
  ],
};
    const renderTree = (nodes) => (
        <TreeItem key={nodes.id} nodeId={nodes.id} label={nodes.name}>
            {Array.isArray(nodes.children) ? nodes.children.map((node) => renderTree(node)) : null}
        </TreeItem>
    );
    return (
            <TreeView className={classes.root} defaultCollapseIcon={(<Icon>expand_more</Icon>)} 
                defaultExpanded={['root']} defaultExpandIcon={(<Icon>chevron_right</Icon>)}>
                {renderTree(data)}
            </TreeView>
    );
}

export default StyledTreeView;