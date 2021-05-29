/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import HTMLEditorToolbar from './htmleditor/HTMLEditorToolbar';

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
    const { label, name, required, helperText, value, onChangeFieldValue, labelWidth, rows } = props;
    const [showSourceCode, setShowSourceCode] = React.useState(true);
    const [shadowRoot, setShadowRoot] = React.useState(null);
    const shadowRef = React.useRef(null);
    // ------------------------------------------- HOOKS ----------------------------------------------------------------------------------
    useEffect(() => {
        if (shadowRef && shadowRef.current && shadowRoot === null) {
            let shadow = shadowRef.current.attachShadow({mode: 'open'});
            setShadowRoot(shadow);
        }
    }, [shadowRef, shadowRoot]);
    useEffect(() => {
        console.log(shadowRoot);
        if (shadowRoot) {
            shadowRoot.innerHTML = `<body>${value !== null && value !== undefined ? value : ''}</body>`;
        }
    }, [value, shadowRoot]);
    // ------------------------------------------- RENDERING ------------------------------------------------------------------------------
    return (
            <Paper className={classes.container} elevation={0}>
                <HTMLEditorToolbar showSourceCode={showSourceCode} setShowSourceCode={setShowSourceCode}/>
                {showSourceCode ? (
                    <div className={classes.row}>
                        <div className={classes.colLeft}>
                            <FormControl variant={'outlined'} fullWidth required={required}>
                                <InputLabel>{label}</InputLabel>
                                <OutlinedInput value={value ? value : ''} multiline={true} rows={rows} onChange={(e) => {
                                    onChangeFieldValue(name, e.target.value);
                                }} labelWidth={labelWidth} />
                                {helperText ? <FormHelperText variant={'outlined'} error={true}>{helperText}</FormHelperText> : null}
                            </FormControl>
                        </div>
                        <div className={classes.colRight} ref={shadowRef}>
    
                        </div>
                    </div>
                ) : (
                    <div className={classes.preview} dangerouslySetInnerHTML={{ __html: value }}>
    
                    </div>
                )}
            </Paper>
    );
}

export default HTMLEditor;
