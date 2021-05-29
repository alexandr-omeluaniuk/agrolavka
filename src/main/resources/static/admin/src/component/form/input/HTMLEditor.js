/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import HTMLEditorContextMenu from './htmleditor/HTMLEditorContextMenu';
import ComponentsFactory from './htmleditor/ComponentsFactory';

const useStyles = makeStyles(theme => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        padding: theme.spacing(1)
    },
    row: {
        flex: 1,
        display: 'flex'
    }
}));

function HTMLEditor (props) {
    const classes = useStyles();
    const { label, name, required, helperText, value, onChangeFieldValue } = props;
    const [contextMenuState, setContextMenuState] = React.useState({
        mouseX: null,
        mouseY: null,
        initiator: null
    });
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
                const element = evt.target;
                if (ComponentsFactory.isHTMLEditorComponent(element)) {
                    ComponentsFactory.getComponent(element).edit({
                        initiator: element,
                        onChange: () => {
                            onChangeFieldValue(name, main.innerHTML);
                        }
                    });
                }
            }, true);
            main.addEventListener('contextmenu', function (event) {
                event.preventDefault();
                setContextMenuState({
                    mouseX: event.clientX - 2,
                    mouseY: event.clientY - 4,
                    initiator: event.target,
                    onChange: () => {
                        onChangeFieldValue(name, main.innerHTML);
                    }
                });
            }, true);
            setShadowRoot(main);
            console.log('HTML editor init completed...');
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [shadowRef, shadowRoot]);
    useEffect(() => {
        if (shadowRoot) {
            shadowRoot.innerHTML = value !== null && value !== undefined ? value : '';
        }
    }, [value, shadowRoot]);
    // ------------------------------------------- RENDERING ------------------------------------------------------------------------------
    return (
            <Paper className={classes.container} elevation={0}>
                {label ? <Typography variant={'h6'}>{label}</Typography> : null}
                <div className={classes.row}>
                    <FormControl variant={'outlined'} fullWidth required={required}>
                        <div ref={shadowRef}>

                        </div>
                        {helperText ? <FormHelperText variant={'outlined'} error={true}>{helperText}</FormHelperText> : null}
                    </FormControl>
                </div>
                <HTMLEditorContextMenu state={contextMenuState} setState={setContextMenuState}/>
            </Paper>
    );
}

export default HTMLEditor;
