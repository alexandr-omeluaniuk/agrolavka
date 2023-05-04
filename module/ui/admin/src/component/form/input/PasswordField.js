/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import Input from '@material-ui/core/Input';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import InputLabel from '@material-ui/core/InputLabel';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';

function PasswordField (props) {
    const { label, value, name, onChange, helperText, align, variant,  ...other } = props;
    const [showPassword, setShowPassword] = React.useState(false);
    const button = () => {
        return (
                <InputAdornment position="end">
                    <IconButton onClick={() => {
                        setShowPassword(!showPassword);
                    }}>
                        <Icon>{showPassword ? 'visibility' : 'visibility_off'}</Icon>
                    </IconButton>
                </InputAdornment>
        );
    };
    const fromVariant = () => {
        if (variant === 'outlined') {
            return <OutlinedInput type={showPassword ? 'text' : 'password'} value={value} onChange={onChange}
                    endAdornment={button()} autoComplete="new-password" labelWidth={80}/>;
        } else {
            return <Input type={showPassword ? 'text' : 'password'} value={value} onChange={onChange}
                    endAdornment={button()} autoComplete="new-password"/>;
        }
    };
    return (
            <FormControl error={helperText ? true : false} variant={variant} {...other}>
                <InputLabel>{label}</InputLabel>
                {fromVariant()}
                <FormHelperText error={helperText ? true : false}>{helperText ? helperText : ''}</FormHelperText>
            </FormControl>
    );
}

export default PasswordField;