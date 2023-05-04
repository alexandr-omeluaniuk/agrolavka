/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import NumberFormat from 'react-number-format';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';

function NumberFormatInteger(props) {
    const { inputRef, onChange, ...other } = props;
    const onValueChange = (values) => {
        onChange({
            target: {
                name: props.name,
                value: values.value
            }
        });
    };
    return (
            <NumberFormat {...other} getInputRef={inputRef} onValueChange={onValueChange} isNumericString/>
  );
}

const useStyles = makeStyles(theme => ({
    alignRight: {
        textAlign: 'right'
    }
}));

function NumberField (props) {
    const classes = useStyles();
    const { label, value, onChange, helperText, align, ...other } = props;
    return (
            <FormControl error={helperText ? true : false} {...other}>
                <InputLabel htmlFor="formatted-text-mask-input">{label}</InputLabel>
                <Input value={value} onChange={onChange}
                    inputComponent={NumberFormatInteger} classes={{input: align === 'right' ? classes.alignRight : null}}/>
                <FormHelperText error={helperText ? true : false}>{helperText ? helperText : ''}</FormHelperText>
            </FormControl>
    );
}

export default NumberField;
