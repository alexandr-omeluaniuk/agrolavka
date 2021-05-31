/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import Tooltip from '@material-ui/core/Tooltip';

const useStyles = makeStyles(theme => ({
    
}));

function TextAlignment(props) {
    const { className, getSelection, openContextMenu } = props;
    const classes = useStyles();
    const { t } = useTranslation();
    
    return (
            <ButtonGroup className={className}>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.left')}>
                    <Button><Icon>format_align_left</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.center')}>
                    <Button><Icon>format_align_center</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.right')}>
                    <Button><Icon>format_align_right</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.justify')}>
                    <Button><Icon>format_align_justify</Icon></Button>
                </Tooltip>
            </ButtonGroup>
    );
}

export default TextAlignment;
