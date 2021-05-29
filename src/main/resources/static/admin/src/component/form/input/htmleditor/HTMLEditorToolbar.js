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
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles(theme => ({
    toolbar: {
        marginBottom: theme.spacing(1),
        display: 'flex',
        justifyContent: 'space-between'
    }
}));

function HTMLEditorToolbar(props) {
    const { showSourceCode, setShowSourceCode } = props;
    const [anchorEl, setAnchorEl] = React.useState(null);
    const classes = useStyles();
    const { t } = useTranslation();
    return (
            <div className={classes.toolbar}>
                <div className={classes.toolbarLeft}>
                    <Tooltip title={t('component.htmleditor.toolbar.headers')}>
                        <IconButton onClick={(event) => {
                            setAnchorEl(event.currentTarget);
                        }}>
                            <Icon>title</Icon>
                        </IconButton>
                    </Tooltip>
                    <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={() => {setAnchorEl(null)}}>
                        <MenuItem value={'h1'}><Typography variant={'h1'}>{t('component.htmleditor.toolbar.header.h1')}</Typography></MenuItem>
                        <MenuItem value={'h2'}><Typography variant={'h2'}>{t('component.htmleditor.toolbar.header.h2')}</Typography></MenuItem>
                        <MenuItem value={'h3'}><Typography variant={'h3'}>{t('component.htmleditor.toolbar.header.h3')}</Typography></MenuItem>
                        <MenuItem value={'h4'}><Typography variant={'h4'}>{t('component.htmleditor.toolbar.header.h4')}</Typography></MenuItem>
                        <MenuItem value={'h5'}><Typography variant={'h5'}>{t('component.htmleditor.toolbar.header.h5')}</Typography></MenuItem>
                        <MenuItem value={'h6'}><Typography variant={'h6'}>{t('component.htmleditor.toolbar.header.h6')}</Typography></MenuItem>
                    </Menu>
                </div>
                <div className={classes.toolbarRight}>
                    <Tooltip title={'Source code'}>
                        <IconButton onClick={() => {
                            setShowSourceCode(!showSourceCode);
                        }}>
                            <Icon>{showSourceCode ? 'code' : 'code_off'}</Icon>
                        </IconButton>
                    </Tooltip>
                </div>
            </div>
    );
}

export default HTMLEditorToolbar;
