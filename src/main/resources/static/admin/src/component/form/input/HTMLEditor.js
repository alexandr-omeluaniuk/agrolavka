/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import HTMLEditorContextMenu from './htmleditor/HTMLEditorContextMenu';

const useStyles = makeStyles(theme => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        padding: theme.spacing(1)
    },
    row: {
        flex: 1,
        display: 'flex'
    },
    colLeft: {
        flex: 1
    },
    colRight: {
        flex: 1,
        padding: theme.spacing(2)
    },
    preview: {
        
    }
}));

function HTMLEditor (props) {
    const classes = useStyles();
    const { label, name, required, helperText, value, onChangeFieldValue } = props;
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [shadowRoot, setShadowRoot] = React.useState(null);
    const shadowRef = React.useRef(null);
    // ------------------------------------------- HOOKS ----------------------------------------------------------------------------------
    useEffect(() => {
        if (shadowRef && shadowRef.current && shadowRoot === null) {
            let shadow = shadowRef.current.attachShadow({mode: 'open'});
            shadow.innerHTML = `
                <style> @import "https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.5.0/mdb.min.css"; </style>
                <main class="card shadow-1-strong p-3" style="min-height: 100px;"></main>
            `;
            const main = shadow.querySelector('main');
            main.addEventListener('click', function(evt) {
//                const target = evt.target;
//                console.log(evt);
//                const tag = target.tagName.toLowerCase();
//                if (tag === 'textarea') {
//                    return;
//                }
//                console.log(tag);
//                evt.target.innerHTML = `
//                    <textarea class="form-control" />
//                `;
//                evt.target.querySelector('textarea').focus();
            }, true);
            main.addEventListener('blur', function (evt) {
//                const target = evt.target;
//                const tag = target.tagName.toLowerCase();
//                if (tag === 'textarea') {
//                    target.remove();
//                }
//                console.log(evt);
            }, true);
            main.addEventListener('contextmenu', function (evt) {
                evt.preventDefault();
                setAnchorEl(evt.target);
            }, true);
            setShadowRoot(main);
        }
    }, [shadowRef, shadowRoot]);
    useEffect(() => {
        if (shadowRoot) {
            shadowRoot.innerHTML = value !== null && value !== undefined ? value : '';
        }
    }, [value, shadowRoot]);
    // ------------------------------------------- RENDERING ------------------------------------------------------------------------------
    return (
            <Paper className={classes.container} elevation={0}>
                <div className={classes.row}>
                    <div className={classes.colLeft}>
                        <FormControl variant={'outlined'} fullWidth required={required}>
                            <div className={classes.colRight} ref={shadowRef}>

                            </div>
                            {helperText ? <FormHelperText variant={'outlined'} error={true}>{helperText}</FormHelperText> : null}
                        </FormControl>
                    </div>
                </div>
                <HTMLEditorContextMenu anchorEl={anchorEl} setAnchorEl={setAnchorEl}/>
            </Paper>
    );
}

export default HTMLEditor;
