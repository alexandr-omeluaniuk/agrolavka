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
import HTMLEditorToolbar from './htmleditor/HTMLEditorToolbar';
import HTMLEditorContextMenu from './htmleditor/HTMLEditorContextMenu';
import { Text } from './htmleditor/components/Text';
import AbstractComponent from './htmleditor/AbstractComponent';

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
    const [shadow, setShadow] = React.useState(null);
    const [selectionRanges, setSelectionRanges] = React.useState(null);
    const shadowRef = React.useRef(null);
    // ------------------------------------------- METHODS --------------------------------------------------------------------------------
    const getSelection = () => {
        const s = shadow.getSelection();
        if (s.rangeCount === 0) {
            return;
        }
        const ranges = [];
        for (let i = 0; i < s.rangeCount; i++) {
            const range = s.getRangeAt(i);
            ranges.push(range);
        }
        setSelectionRanges(ranges);
    };
    const applyColor = (color) => {
        Text.applyColorToSelectedText(color, selectionRanges);
        saveHTML();
    };
    const saveHTML = () => {
        onChangeFieldValue(name, shadow.querySelector('main').innerHTML);
    };
    const openContextMenu = (event, menuType) => {
        setContextMenuState({
            mouseX: event.clientX - 2,
            mouseY: event.clientY - 4,
            initiator: event.target,
            type: menuType
        });
    };
    // ------------------------------------------- HOOKS ----------------------------------------------------------------------------------
    useEffect(() => {
        if (shadowRef && shadowRef.current && shadowRoot === null) {
            let shadow = shadowRef.current.attachShadow({mode: 'open'});
            shadow.innerHTML = `
                <style>
                    @import "https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.5.0/mdb.min.css";
                    [${AbstractComponent.ATTRIBUTE_CLASS}]:hover {
                        cursor: pointer;
                        box-shadow: rgb(0 0 0 / 20%) 0px 2px 1px -1px, rgb(0 0 0 / 14%) 0px 1px 1px 0px, rgb(0 0 0 / 12%) 0px 1px 3px 0px;
                        transition: .3s all;
                    }
                </style>
                <main class="card shadow-1-strong p-3" style="min-height: 100px;" role="textbox" contenteditable="true">
                    
                </main>
            `;
            const main = shadow.querySelector('main');
            main.addEventListener('contextmenu', function (event) {
                event.preventDefault();
                openContextMenu(event, 'CONTEXTMENU');
            }, true);
            main.addEventListener('blur', function (event) {
                onChangeFieldValue(name, main.innerHTML);
            });
            setShadowRoot(main);
            setShadow(shadow);
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
                <HTMLEditorToolbar getSelection={getSelection} applyColor={applyColor} openContextMenu={openContextMenu}/>
                <div className={classes.row}>
                    <FormControl variant={'outlined'} fullWidth required={required}>
                        <div ref={shadowRef}>

                        </div>
                        {helperText ? <FormHelperText variant={'outlined'} error={true}>{helperText}</FormHelperText> : null}
                    </FormControl>
                </div>
                <HTMLEditorContextMenu state={contextMenuState} setState={setContextMenuState} saveHTML={saveHTML}/>
            </Paper>
    );
}

export default HTMLEditor;
