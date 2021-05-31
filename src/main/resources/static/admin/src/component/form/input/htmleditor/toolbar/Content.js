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

function Content(props) {
    const { className, getSelection, openContextMenu } = props;
    const { t } = useTranslation();
    
    return (
            <ButtonGroup className={className}>
                <Tooltip title={t('component.htmleditor.toolbar.content.text')}>
                    <Button onClick={(e) => {
                        getSelection();
                        openContextMenu(e, 'TEXT');
                    }}><Icon>title</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.content.link')}>
                    <Button><Icon>insert_link</Icon></Button>
                </Tooltip>
            </ButtonGroup>
    );
}

export default Content;