/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import Button from '@material-ui/core/Button';
import Tooltip from '@material-ui/core/Tooltip';
import { TYPE_TEXT } from '../HTMLEditorContextMenu';
import { Link } from '../components/Link';

function Content(props) {
    const { className, getSelection, openContextMenu, openComponentControl, saveHTML } = props;
    const { t } = useTranslation();
    
    return (
            <ButtonGroup className={className}>
                <Tooltip title={t('component.htmleditor.toolbar.content.text')}>
                    <Button onClick={(e) => {
                        getSelection();
                        openContextMenu(e, TYPE_TEXT);
                    }}><Icon>title</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.content.link')}>
                    <Button onClick={(e) => {
                        const ranges = getSelection();
                        if (ranges) {
                            const component = new Link();
                            component.initComponentControl({}, ranges, t, saveHTML);
                            openComponentControl(component);
                        }
                    }}><Icon>insert_link</Icon></Button>
                </Tooltip>
            </ButtonGroup>
    );
}

export default Content;