/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import Paper from '@material-ui/core/Paper';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    container: {
        display: 'flex',
        flexDirection: 'column',
        padding: theme.spacing(1)
    },
    row: {
        flex: 1
    },
    toolbar: {
        marginBottom: theme.spacing(1)
    },
    preview: {
        
    }
}));

function HTMLEditor (props) {
    const classes = useStyles();
    const { label, name, required, helperText, value, onChangeFieldValue, labelWidth, rows } = props;
    const [showSourceCode, setShowSourceCode] = React.useState(false);
    return (
            <Paper className={classes.container} elevation={1}>
                <div className={classes.toolbar}>
                    <Tooltip title={'Source code'}>
                        <IconButton onClick={() => {
                            setShowSourceCode(!showSourceCode);
                        }}>
                            <Icon>{showSourceCode ? 'code' : 'code_off'}</Icon>
                        </IconButton>
                    </Tooltip>
                </div>
                {showSourceCode ? (
                    <div className={classes.row}>
                        <FormControl variant={'outlined'} fullWidth required={required}>
                            <InputLabel>{label}</InputLabel>
                            <OutlinedInput value={value} multiline={true} rows={rows} onChange={(e) => {
                                onChangeFieldValue(name, e.target.value);
                            }} labelWidth={labelWidth}/>
                            {helperText ? <FormHelperText variant={'outlined'} error={true}>{helperText}</FormHelperText> : null}
                        </FormControl>
                    </div>
                ) : (
                    <div className={classes.preview} dangerouslySetInnerHTML={{ __html: value }}>
    
                    </div>
                )}
            </Paper>
    );
}

export default HTMLEditor;
