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
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import InputAdornment from '@material-ui/core/InputAdornment';
import Avatar from '@material-ui/core/Avatar';

const useStyles = makeStyles(theme => ({
    container: {
        display: 'inline-flex'
    },
    button: {
        padding: '4px'
    },
    avatar: {
        width: theme.spacing(7),
        height: theme.spacing(7)
    },
    root: {
        display: 'flex'
    }
}));

function FileUpload (props) {
    const { label, value, name, helperText, onChange, avatar, entryId, ...other } = props;
    const [base64, setBase64] = React.useState(null);
    const classes = useStyles();
    const { t } = useTranslation();
    let inputRef = React.createRef();
    
    const openDialog = (e) => {
        e.stopPropagation();
        inputRef.current.click();
    };
    
    const onFileSelected = (e) => {
        let file = e.target.files[0];
        if (avatar) {
            var reader = new FileReader();
            reader.onloadend = function() {
                setBase64(reader.result);
            };
            reader.readAsDataURL(file);
        }
        onChange(name, file);
    };
    
    const onFileClear = (e) => {
        e.stopPropagation();
        onChange(name, null);
    };
    return (
            <div className={classes.root}>
                <FormControl {...other} error={helperText ? true : false}>
                    <InputLabel htmlFor="file-upload-hidden">{label}</InputLabel>
                    <Input type="text" readOnly id="file-upload-hidden" value={value && value.name ? value.name : (value ? value : '')}
                            onClick={openDialog} startAdornment={
                        <InputAdornment position="start">
                            <Icon>description</Icon>
                        </InputAdornment>
                    } endAdornment={
                        !value ? (
                            <Tooltip title={t('common.selectFile')}>
                                <IconButton className={classes.button} onClick={openDialog}>
                                    <Icon>get_app</Icon>
                                </IconButton>
                            </Tooltip>
                        ) : (
                            <Tooltip title={t('common.clear')}>
                                <IconButton className={classes.button} onClick={onFileClear}>
                                    <Icon>close</Icon>
                                </IconButton>
                            </Tooltip>
                        )
                    }/>
                    <FormHelperText error={helperText ? true : false}>{helperText ? helperText : ''}</FormHelperText>
                    <input type="file" onChange={onFileSelected} hidden ref={inputRef}/>
                </FormControl>
                {avatar && value ? <Avatar className={classes.avatar} src={avatar(value, entryId, base64)} /> : null}
            </div>
    );
}

export default FileUpload;