/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Icon from '@material-ui/core/Icon';
import { useTranslation } from 'react-i18next';

function Dropdown (props) {
    const { t } = useTranslation();
    const { label, value, onChange, fullWidth, helperText, options, allowEmpty, ...other } = props;
    return (
        <FormControl fullWidth={fullWidth} error={helperText ? true : false} {...other}>
            <InputLabel>{label}</InputLabel>
            <Select value={value} onChange={onChange} label={label}>
                {allowEmpty ? (
                    <MenuItem value="">
                        <em>{t('component.form.none')}</em>
                    </MenuItem>
                ) : null}
                {options.map((opt, idx) => {
                    return (
                        <MenuItem value={opt.value} key={idx} disabled={opt.disabled ? true : false}>
                            {opt.icon ? (<Icon style={{width: '40px'}}>{opt.icon}</Icon>) : null}
                            {opt.label}
                        </MenuItem>
                    );
                })}
            </Select>
            {helperText ? (
                <FormHelperText>{helperText}</FormHelperText>
            ) : null}
        </FormControl>
    );
}

export default Dropdown;
