/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from "react";
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import ColorPicker from './ColorPicker';
import { createTheme, getPrimaryColor, getSecondaryColor, getPrimaryColorContrast, getSecondaryColorContrast,
    savePrimaryColorContrast, saveSecondaryColorContrast, savePrimaryColor, saveSecondaryColor,
    ALL_THEMES, setDefaultTheme, getDefaultTheme, THEME_STANDARD } from '../../../../conf/theme';
import { changeTheme } from '../../../../index';
import Dropdown from '../../../../component/form/input/Dropdown';
import LanguageDropdown from './LanguageDropdown';

const useStyles = makeStyles(theme => ({
    root: {
        padding: theme.spacing(2)
    },
    divider: {
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(2)
    },
    cell: {
        display: 'flex',
        alignItems: 'center',
        minHeight: '56px'
    },
    row: {
        minHeight: '56px',
        marginBottom: theme.spacing(1)
    }
}));

function UserInterface(props) {
    const classes = useStyles();
    const { t } = useTranslation();
    const isStandardTheme = getDefaultTheme() === THEME_STANDARD.id;
    // -------------------------------------------------- STATE ---------------------------------------------------------------------------
    const [open, setOpen] = React.useState(false);
    const [title, setTitle] = React.useState('');
    const [type, setType] = React.useState('');
    const [color, setColor] = React.useState(null);
    const [contrast, setContrast] = React.useState(null);
    const [defTheme, setDefTheme] = React.useState(getDefaultTheme());
    // -------------------------------------------------- FUNCTIONS -----------------------------------------------------------------------
    const openColorPicker = (type) => {
        let title, color, contrast;
        if ('PRIMARY' === type) {
            title = t('component.account_menu.t_settings.palette_colors.primary');
            color = getPrimaryColor();
            contrast = getPrimaryColorContrast();
        } else {
            title = t('component.account_menu.t_settings.palette_colors.secondary');
            color = getSecondaryColor();
            contrast = getSecondaryColorContrast();
        }
        setTitle(title);
        setOpen(true);
        setType(type);
        setContrast(contrast);
        setColor(color);
    };
    const closeColorPicker = () => {
        setOpen(false);
    };
    const contrastChanged = (event, contrast) => {
        if (type === 'PRIMARY') {
            savePrimaryColorContrast(contrast);
        } else if (type === 'SECONDARY') {
            saveSecondaryColorContrast(contrast);
        }
        setContrast(contrast);
        changeTheme(createTheme());
    };
    const colorChanged = (color) => {
        if (type === 'PRIMARY') {
            savePrimaryColor(color);
        } else if (type === 'SECONDARY') {
            saveSecondaryColor(color);
        }
        setColor(color);
        changeTheme(createTheme());
    };
    let themeOptions = [];
    ALL_THEMES.forEach(th => {
        themeOptions.push({
            label: t('component.account_menu.t_settings.themes.' + th.id),
            value: th.id
        });
    });
    // -------------------------------------------------------- RENDER --------------------------------------------------------------------
    return (
            <Paper elevation={0} className={classes.root}>
    
                <Grid container className={classes.row}>
                    <Grid item xs={12} md={4} className={classes.cell}>
                        <Typography variant="subtitle1">{t('component.account_menu.t_settings.language')}</Typography>
                    </Grid>
                    <Grid item xs={12} md={8} className={classes.cell}>
                        <LanguageDropdown/>
                    </Grid>
                </Grid>
    
                <Grid container className={classes.row}>
                    <Grid item xs={12} md={4} className={classes.cell}>
                        <Typography variant="subtitle1">{t('component.account_menu.t_settings.theme')}</Typography>
                    </Grid>
                    <Grid item xs={12} md={8} className={classes.cell}>
                        <Dropdown fullWidth={true} label={t('component.account_menu.t_settings.theme')} 
                                variant="outlined" options={themeOptions} value={defTheme} onChange={(e) => {
                            let newTheme = e.target.value;
                            setDefaultTheme(newTheme);
                            setDefTheme(newTheme);
                            changeTheme(createTheme());
                        }}/>
                    </Grid>
                </Grid>
    
                {isStandardTheme ? (
                <Grid container className={classes.row}>
                    <Grid item xs={12} md={4} className={classes.cell}>
                        <Typography variant="subtitle1">{t('component.account_menu.t_settings.palette')}</Typography>
                    </Grid>
                    <Grid item xs={12} md={8} className={classes.cell}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} md={6}>
                                <Button fullWidth variant="contained" color="primary" onClick={() => openColorPicker('PRIMARY')}>
                                    {t('component.account_menu.t_settings.palette_colors.primary')}
                                </Button>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <Button fullWidth variant="contained" color="secondary" onClick={() => openColorPicker('SECONDARY')}>
                                    {t('component.account_menu.t_settings.palette_colors.secondary')}
                                </Button>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
                ) : null}
                
                <ColorPicker open={open} title={title} onClose={closeColorPicker} color={color}
                    contrast={contrast} contrastChanged={contrastChanged} colorChanged={colorChanged}/>
            </Paper>
    );
};

export default UserInterface;
