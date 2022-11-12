/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Tooltip from '@material-ui/core/Tooltip';
import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import CardActions from '@material-ui/core/CardActions';

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        padding: theme.spacing(2)
    },
    labelRow: {
        display: 'flex'
    },
    label: {
        flex: 1
    },
    media: {
        height: 0,
        paddingTop: '100%',
        backgroundSize: 'cover'
    },
    deleteImage: {
        color: theme.palette.error.main
    },
    imageActions: {
        display: 'flex'
    },
    imageName: {
        flex: 1
    },
    addImage: {
        color: theme.palette.success.main
    }
}));

function Images (props) {
    const { label, value, name, helperText, onChange, ...other } = props;
    const classes = useStyles();
    const { t } = useTranslation();
    let inputRef = React.createRef();
    // ---------------------------------------------------------- METHODS -----------------------------------------------------------------
    const openDialog = (e) => {
        e.stopPropagation();
        inputRef.current.click();
    };
    const onFileSelected = (e) => {
        let file = e.target.files[0];
        var reader = new FileReader();
        reader.onloadend = function() {
            file.data = reader.result;
            value.push(file);
            onChange(name, value);
        };
        reader.readAsDataURL(file);
    };
    const deleteImage = (img) => {
        const newValue = value.filter(v => {
            return v.size !== img.size && v.name !== img.name;
        });
        onChange(name, newValue);
    };
    // --------------------------------------------------------- RENDERING ----------------------------------------------------------------
    return (
            <React.Fragment>
                <FormControl {...other} error={helperText ? true : false} className={classes.root}>
                    <div className={classes.labelRow}>
                        <div className={classes.label}>
                            <InputLabel htmlFor={name} >{label + (value.length > 0 ? ` (${value.length})` : '')}</InputLabel>
                        </div>
                        <Tooltip title={t('component.form.select_image')}>
                            <IconButton color="primary" onClick={(e) => openDialog(e)}>
                                <Icon className={classes.addImage}>image</Icon>
                            </IconButton>
                        </Tooltip>
                    </div>
                    <FormHelperText error={helperText ? true : false}>{helperText ? helperText : ''}</FormHelperText>
                    <input type="file" onChange={onFileSelected} hidden ref={inputRef} accept="image/x-png,image/gif,image/jpeg"/>
                </FormControl>
                <Grid container spacing={2}>
                    {value.map((img, idx) => {
                        return (
                                <Grid item key={idx} xs={12} md={4} lg={2}>
                                    <Card>
                                        <CardMedia className={classes.media} image={img.data} title={img.name}/>
                                        <CardActions className={classes.imageActions}>
                                            <Typography className={classes.imageName} variant={'caption'}>
                                                {img.name.length > 20 ? img.name.substring(0, 20) + '...' : img.name}
                                            </Typography>
                                            <Tooltip title={t('component.form.remove_image')}>
                                                <IconButton onClick={() => deleteImage(img)}>
                                                    <Icon className={classes.deleteImage}>delete</Icon>
                                                </IconButton>
                                            </Tooltip>
                                        </CardActions>
                                    </Card>
                                </Grid>
                        );
                    })}
                </Grid>
            </React.Fragment>
    );
}

export default Images;