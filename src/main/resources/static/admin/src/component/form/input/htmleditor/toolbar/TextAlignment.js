/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import Tooltip from '@material-ui/core/Tooltip';
import { Text } from '../components/Text';


function TextAlignment(props) {
    const { className, getSelection, saveHTML } = props;
    const { t } = useTranslation();
    
    const align = (alignment) => {
        const ranges = getSelection();
        Text.applyAlignmentToSelectedText(alignment, ranges);
        saveHTML();
    };
    
    return (
            <ButtonGroup className={className}>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.left')}>
                    <Button onClick={() => align('left')}><Icon>format_align_left</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.center')}>
                    <Button onClick={() => align('center')}><Icon>format_align_center</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.right')}>
                    <Button onClick={() => align('right')}><Icon>format_align_right</Icon></Button>
                </Tooltip>
                <Tooltip title={t('component.htmleditor.toolbar.text_alignment.justify')}>
                    <Button onClick={() => align('justify')}><Icon>format_align_justify</Icon></Button>
                </Tooltip>
            </ButtonGroup>
    );
}

export default TextAlignment;
