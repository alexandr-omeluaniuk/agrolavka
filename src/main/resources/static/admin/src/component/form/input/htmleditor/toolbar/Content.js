/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';

const useStyles = makeStyles(theme => ({
    root: {
        marginLeft: theme.spacing(2)
    }
}));

function Content(props) {
    const { getSelection } = props;
    const classes = useStyles();
    const { t } = useTranslation();
    
    return (
            <div className={classes.root}>
                <Tooltip title={t('component.htmleditor.toolbar.content.link')}>
                    <IconButton><Icon>insert_link</Icon></IconButton>
                </Tooltip>
            </div>
    );
}

export default Content;