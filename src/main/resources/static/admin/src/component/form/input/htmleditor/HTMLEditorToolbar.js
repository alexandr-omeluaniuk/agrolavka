/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Colors from './toolbar/Colors';
import Content from './toolbar/Content';

const useStyles = makeStyles(theme => ({
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1)
    },
    toolbarLeft: {
        display: 'flex',
        alignItems: 'center'
    }
}));

function HTMLEditorToolbar(props) {
    const { getSelection, applyColor } = props;
    const classes = useStyles();
    return (
            <div className={classes.toolbar}>
                <div className={classes.toolbarLeft}>
                    <Colors getSelection={getSelection} applyColor={applyColor}/>
                    <Content getSelection={getSelection}/>
                </div>
                <div>
            
                </div>
            </div>
    );
}

export default HTMLEditorToolbar;