/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Colors from './toolbar/Colors';

const useStyles = makeStyles(theme => ({
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1)
    }
}));

function HTMLEditorToolbar(props) {
    const { getSelection } = props;
    const classes = useStyles();
    return (
            <div className={classes.toolbar}>
                <div>
                    <Colors getSelection={getSelection}/>
                </div>
                <div>
            
                </div>
            </div>
    );
}

export default HTMLEditorToolbar;