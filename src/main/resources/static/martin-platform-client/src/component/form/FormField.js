/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { useTranslation } from 'react-i18next';
import { TYPES, VALIDATORS, SERVER_DATE_FORMAT, CLIENT_DATE_FORMAT, TIME_FORMAT, DATETIME_FORMAT } from '../../service/DataTypeService';
import TextField from '@material-ui/core/TextField';
import Icon from '@material-ui/core/Icon';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import NumberField from './input/NumberField';
import Dropdown from './input/Dropdown';
import FileUpload from './input/FileUpload';
import MultipleSelect from './input/MultipleSelect';
import HTMLEditor from './input/HTMLEditor';
import PasswordField from './input/PasswordField';
import { MuiPickersUtilsProvider, KeyboardTimePicker, KeyboardDatePicker, DateTimePicker } from '@material-ui/pickers';
import MomentUtils from '@date-io/moment';
import moment from 'moment';
//import "moment/locale/de";

function FormField (props) {
    const { t, i18n } = useTranslation();
    const { fieldConfig, invalidFields, fieldValue, onChangeFieldValue, entryId, variant } = props;
    // ==================================================== RENDERING =====================================================================
    const renderFormField = () => {
        let name = fieldConfig.name;
        let label = fieldConfig.label ? fieldConfig.label : '';
        let attributes = fieldConfig.attributes ? fieldConfig.attributes : {};
        let isRequired = fieldConfig.validators && fieldConfig.validators.filter(v => {
            return v.type === VALIDATORS.REQUIRED;
        }).length > 0;
        if (fieldConfig.type === TYPES.ID) {
            return <TextField value={fieldValue ? fieldValue : ''} name={name} />;
        } else if (fieldConfig.type === TYPES.TEXTFIELD) {
            const inputProps = {};
            if (attributes.readOnly !== undefined) {
                inputProps.readOnly = attributes.readOnly;
            }
            return <TextField label={label} fullWidth={true} required={isRequired} variant={variant}
                            onChange={(e) => onChangeFieldValue(name, e.target.value)}
                            value={fieldValue ? fieldValue : ''} name={name} error={invalidFields.has(name)} 
                            helperText={invalidFields.get(name)} InputProps={inputProps}/>;
        } else if (fieldConfig.type === TYPES.TEXTAREA) {
            return <TextField label={label} fullWidth={true} onChange={(e) => onChangeFieldValue(name, e.target.value)}
                                value={fieldValue ? fieldValue : ''} name={name} error={invalidFields.has(name)}
                                helperText={invalidFields.get(name)} multiline variant={variant}
                                rows={attributes.rows ? attributes.rows : 3} required={isRequired}/>;
        } else if (fieldConfig.type === TYPES.INTEGER_NUMBER) {
            return <NumberField label={label} value={fieldValue ? fieldValue : ''} variant={variant}
                            onChange={(e) => onChangeFieldValue(name, e.target.value)} fullWidth={true}
                            helperText={invalidFields.get(name)} required={isRequired}/>;
        } else if (fieldConfig.type === TYPES.SELECT) {
            return <Dropdown label={label} value={fieldValue ? fieldValue : ''} required={isRequired} variant={variant}
                            onChange={(e) => {
                                onChangeFieldValue(name, e.target.value);
                                if (attributes.onChange) {
                                    attributes.onChange(e.target.value);
                                }
                            }} fullWidth={true}
                            helperText={invalidFields.get(name)} options={attributes.options ? attributes.options : []}
                            allowEmpty={attributes.allowEmpty ? true : false}/>;
        } else if (fieldConfig.type === TYPES.FILE) {
            return <FileUpload label={label} onChange={onChangeFieldValue} fullWidth={true} value={fieldValue ? fieldValue : ''} 
                            helperText={invalidFields.get(name)} name={name} required={isRequired} avatar={attributes.avatar}
                            entryId={entryId} variant={variant}/>;
        } else if (fieldConfig.type === TYPES.PASSWORD) {
            return <PasswordField label={label} fullWidth={true} required={isRequired} type="password"
                            onChange={(e) => onChangeFieldValue(name, e.target.value)}
                            value={fieldValue ? fieldValue : ''} name={name} error={invalidFields.has(name)} 
                            helperText={invalidFields.get(name)} variant={variant}/>;
        } else if (fieldConfig.type === TYPES.TIME) {
            let value = fieldValue;
            if (value) {
                value = moment(value, TIME_FORMAT);
            } else {
                value = null;
            }
            return (
                <MuiPickersUtilsProvider utils={MomentUtils} libInstance={moment} locale={i18n.language}>
                    <KeyboardTimePicker label={label} fullWidth={true} required={isRequired} clearable ampm={false} mask="__:__"
                        onChange={(date) => {
                            onChangeFieldValue(name, date !== null ? date.format(TIME_FORMAT) : null);
                        }} value={value} name={name} error={invalidFields.has(name)} 
                        helperText={invalidFields.get(name)} variant={variant}
                        cancelLabel={t('component.form.cancel')} clearLabel={t('component.form.clear')}
                        keyboardIcon={(<Icon>access_time</Icon>)}/>
                </MuiPickersUtilsProvider>
            );
        } else if (fieldConfig.type === TYPES.DATE) {
            //moment.locale(i18n.language);
            let value = fieldValue ? moment(fieldValue, SERVER_DATE_FORMAT) : null;
            return (
                    <MuiPickersUtilsProvider utils={MomentUtils} libInstance={moment} locale={i18n.language}>
                        <KeyboardDatePicker disableToolbar format={CLIENT_DATE_FORMAT} margin="normal" required={isRequired}
                            label={label} onChange={(date) => onChangeFieldValue(name, date)} name={name} value={value} autoOk={true}
                            fullWidth={true} error={invalidFields.has(name)} helperText={invalidFields.get(name)} variant={variant}/>
                    </MuiPickersUtilsProvider>
            );
        } else if (fieldConfig.type === TYPES.DATETIME) {
            return (
                    <MuiPickersUtilsProvider utils={MomentUtils} libInstance={moment} locale={i18n.language}>
                        <DateTimePicker value={fieldValue ? fieldValue : null} onChange={(date) => onChangeFieldValue(name, date)}
                            autoOk={true} label={label} ampm={false} showTodayButton={true} fullWidth={true}
                            format={DATETIME_FORMAT} clearLabel={t('component.form.clear')} required={isRequired}
                            cancelLabel={t('component.form.cancel')} todayLabel={t('component.form.today')} variant={variant}/>
                    </MuiPickersUtilsProvider>
            );
        } else if (fieldConfig.type === TYPES.MULTIPLESELECT) {
            return <MultipleSelect label={label} options={attributes.options ? attributes.options : []} name={name}
                        value={fieldValue ? fieldValue : []} fullWidth={true} onChangeFieldValue={onChangeFieldValue}
                        required={isRequired} helperText={invalidFields.get(name)} variant={variant}/>;
        } else if (fieldConfig.type === TYPES.BOOLEAN) {
            return <FormControlLabel label={label} variant={variant} control={(
                        <Checkbox checked={fieldValue ? true : false} onChange={(e) => onChangeFieldValue(name, e.target.checked)} 
                            name={name} color="secondary"/>
                    )}/>;
        } else if (fieldConfig.type === TYPES.HTML_EDITOR) {
            return <HTMLEditor label={label} name={name} required={isRequired} helperText={invalidFields.get(name)}
                        onChangeFieldValue={onChangeFieldValue} value={fieldValue} labelWidth={attributes.labelWidth}
                        rows={attributes.rows}/>;
        } else if (fieldConfig.type === TYPES.CUSTOM) {
            return fieldConfig.render(name, fieldValue, onChangeFieldValue);
        }
        return null;
    };
    return renderFormField();
}

export default FormField;
