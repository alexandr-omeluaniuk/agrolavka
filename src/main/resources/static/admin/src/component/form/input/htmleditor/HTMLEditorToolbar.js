/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Colors from './toolbar/Colors';
import Content from './toolbar/Content';
import TextAlignment from './toolbar/TextAlignment';

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
    },
    toolbarSection: {
        marginRight: theme.spacing(1)
    }
}));

function HTMLEditorToolbar(props) {
    const { getSelection, ranges, openContextMenu, saveHTML, openComponentControl } = props;
    const classes = useStyles();
    return (
            <div className={classes.toolbar}>
                <div className={classes.toolbarLeft}>
                    <Colors getSelection={getSelection} ranges={ranges} saveHTML={saveHTML} className={classes.toolbarSection}/>
                    <Content getSelection={getSelection} openContextMenu={openContextMenu} className={classes.toolbarSection}
                        openComponentControl={openComponentControl} saveHTML={saveHTML}/>
                    <TextAlignment getSelection={getSelection} saveHTML={saveHTML} className={classes.toolbarSection}/>
                </div>
                <div>
            
                </div>
            </div>
    );
}

export default HTMLEditorToolbar;