/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import Icon from '@material-ui/core/Icon';
import FormField from './FormField';
import { DataTypeService } from '../../service/DataTypeService';

const useStyles = makeStyles(theme => ({
    saveButton: {
        color: theme.palette.primary.main
    },
    actions: {
        display: 'flex',
        justifyContent: 'flex-end'
    },
    submitIcon: {
        marginRight: theme.spacing(1)
    }
}));

function Form (props) {
    const { formConfig, onSubmitAction, record } = props;
    const classes = useStyles();
    const { t } = useTranslation();
    const [formData, setFormData] = React.useState(new Map());
    const [invalidFields, setInvalidFields] = React.useState(new Map());
    let id = DataTypeService.getIdValue(formConfig, record);
    let submit = formConfig.submit ? formConfig.submit : {};
    // ========================================================= HOOKS ====================================================================
    useEffect(() => {
        if (record) {
            let dataMap = new Map();
            for (let k in record) {
                dataMap.set(k, record[k]);
            }
            setFormData(dataMap);
            if (formConfig.onLoadRecord) {
                formConfig.onLoadRecord(record);
            }
        } else {
            let dataMap = new Map();
            formConfig.formFields.filter(f => {
                return f.defaultValue !== undefined;
            }).forEach(f => {
                dataMap.set(f.name, f.defaultValue);
            });
            setFormData(dataMap);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [record]);
    // ========================================================= METHODS ==================================================================
    const onChangeFieldValue = (name, value) => {
        formData.set(name, value);
        if (formConfig.onChangeListener) {
            formConfig.onChangeListener(formData, name, value);
        }
        setFormData(new Map(formData));
        setInvalidFields(isFormValid());
    };
    const isFormValid = () => {
        let newInvalidField = new Map();
        formConfig.formFields.filter(f => {
            return !f.hidden && !(id && f.editable === false);
        }).forEach(field => {
            let fieldName = field.name;
            let value = formData.has(fieldName) ? formData.get(fieldName) : null;
            newInvalidField = new Map([...newInvalidField, ...DataTypeService.validateField(field, value, t, formData)]);
        });
        return newInvalidField;
    };
    const saveChanges = () => {
        let validationResult = isFormValid();
        if (validationResult.size === 0) {
            let data = {};
            for (const [key, value] of formData.entries()) {
                let field = formConfig.formFields.filter(f => { return f.name === key; })[0];
                if (field) {
                    data[key] = DataTypeService.convertUIValueToServerFormat(field, value);
                }
            }
            // set default values
            let id = DataTypeService.getIdValue(formConfig, data);
            if (!id) {
                formConfig.formFields.filter(f => {
                    return f.constValue !== undefined;
                }).forEach(f => {
                    data[f.name] = f.constValue;
                });
            }
            onSubmitAction(data);
        } else {
            setInvalidFields(validationResult);
        }
    };
    const _sizeOf = (val) => {
        if (val === "false") {
            return false;
        } else if (val === "true") {
            return true;
        } else if (val === "auto") {
            return val;
        } else {
            return parseInt(val);
        }
    };
    // bind component functions for using in parent components
    formConfig.api = {
        onChangeFieldValue: onChangeFieldValue
    };
    // ========================================================= RENDERING ================================================================
    return (
            <Grid container spacing={formConfig.spacing}>
                {formConfig.formFields.filter(f => {
                    return !(id && f.editable === false); 
                }).map((field, idx) => {
                    let style = {};
                    if (field.hidden) {
                        style['display'] = 'none';
                    }
                    let gridProps = {};
                    if (field.grid) {
                        for (let k in field.grid) {
                            gridProps[k] = _sizeOf(field.grid[k]);
                        }
                    }
                    return (
                            <Grid item key={idx} {...gridProps} style={style}>
                                <FormField fieldConfig={field} onChangeFieldValue={onChangeFieldValue} entryId={id} 
                                        variant={formConfig.variant}
                                        invalidFields={invalidFields} fieldValue={formData.get(field.name)}/>
                            </Grid>
                    );
                })}
                <Grid item xs={12} lg={12} sm={12} md={12} className={classes.actions}>
                    <Button variant={submit.variant} color={submit.color} className={classes.saveButton} 
                            disabled={invalidFields.size > 0} onClick={saveChanges}>
                        <Icon className={classes.submitIcon}>
                            {submit.icon}</Icon> {submit.label ? submit.label : t('component.form.save')}
                    </Button>
                </Grid>
            </Grid>
    );
}

export default Form;
