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
import ComponentsFactory from './htmleditor/ComponentsFactory';
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
    const [selectionNodes, setSelectionNodes] = React.useState(null);
    const [selectionStartPosition, setSelectionStartPosition] = React.useState(null);
    const [selectionEndPosition, setSelectionEndPosition] = React.useState(null);
    const shadowRef = React.useRef(null);
    // ------------------------------------------- METHODS --------------------------------------------------------------------------------
    const getSelection = () => {
        const s = shadow.getSelection();
        const range = s.getRangeAt(0);
        setSelectionStartPosition(range.startOffset);
        setSelectionEndPosition(range.endOffset);
        var _iterator = document.createNodeIterator(
            s.getRangeAt(0).commonAncestorContainer, NodeFilter.SHOW_TEXT, {
                acceptNode: function (node) {
                    return NodeFilter.FILTER_ACCEPT;
                }
            }
        );
        var _nodes = [];
        while (_iterator.nextNode()) {
            if (_nodes.length === 0 && _iterator.referenceNode !== range.startContainer) continue;
                _nodes.push(_iterator.referenceNode);
            if (_iterator.referenceNode === range.endContainer) break;
        }
        setSelectionNodes(_nodes);
    };
    const applyColor = (color) => {
        selectionNodes.forEach(n => {
            const style = n.parentNode.getAttribute('style');
            let styles = style.split(';');
            styles = styles.filter(s => s.indexOf('color') === -1 && s.length > 0);
            styles.push('color: ' + color);
            n.parentNode.setAttribute('style', styles.join(';'));
            onChangeFieldValue(name, shadow.querySelector('main').innerHTML);
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
                <main class="card shadow-1-strong p-3" style="min-height: 100px;"></main>
            `;
            const main = shadow.querySelector('main');
            main.addEventListener('dblclick', function(evt) {
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
                <HTMLEditorToolbar getSelection={getSelection} applyColor={applyColor}/>
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
