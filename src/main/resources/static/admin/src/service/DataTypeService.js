/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export const CLIENT_DATE_FORMAT = 'DD.MM.YYYY';
export const SERVER_DATE_FORMAT = 'YYYY-MM-DD';
export const TIME_FORMAT = 'HH:mm';
export const DATETIME_FORMAT = 'DD.MM.YYYY HH:mm';
// eslint-disable-next-line no-useless-escape
const EMAIL_REGEX = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
// eslint-disable-next-line no-useless-escape
const WEB_URL_REGEX = /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)$/i;

export let TYPES = {
    TEXTFIELD: 'TEXTFIELD',
    ID: 'ID',
    TEXTAREA: 'TEXTAREA',
    INTEGER_NUMBER: 'INTEGER_NUMBER',
    DOUBLE_NUMBER:'DOUBLE_NUMBER',
    MONEY: 'MONEY',
    SELECT: 'SELECT',
    MULTIPLESELECT: 'MULTIPLESELECT',
    FILE: 'FILE',
    IMAGES: 'IMAGES',
    IMAGE: 'IMAGE',
    PASSWORD: 'PASSWORD',
    TIME: 'TIME',
    DATE: 'DATE',
    BOOLEAN: 'BOOLEAN',
    CUSTOM: 'CUSTOM'
};

export let VALIDATORS = {
    REQUIRED: 'REQUIRED',
    MIN_LENGTH: 'MIN_LENGTH',
    MAX_LENGTH: 'MAX_LENGTH',
    MIN: 'MIN',
    MAX: 'MAX',
    EXTENSION: 'EXTENSION',
    EMAIL: 'EMAIL',
    CUSTOM: 'CUSTOM',
    WEB_URL: "WEB_URL"
};

export class DataTypeService {
    static getIdValue = (formConfig, data) => {
        let idField = formConfig.formFields.filter(f => {
            return f.type === TYPES.ID;
        })[0];
        let result = null;
        if (idField && data) {
            result = data[idField.name];
        }
        return result;
    }
    static validateField = (field, value, t, formData) => {
        let invalidFields = new Map();
        let fieldName = field.name;
        if (field.grid && field.validators) {
            field.validators.forEach(v => {
                if (v.type === VALIDATORS.CUSTOM && v.isValid) {
                    if (!v.isValid(value, formData)) {
                        invalidFields.set(fieldName, v.message);
                    }
                }
                if (v.type === VALIDATORS.REQUIRED && (value === null || value === undefined || value.length === 0)) {
                    invalidFields.set(fieldName, t('validation.required'));
                }
                if (v.type === VALIDATORS.MIN_LENGTH && (value !== null && value.length < v.length)) {
                    invalidFields.set(fieldName, t('validation.min_length', {length: v.length}));
                }
                if (v.type === VALIDATORS.MAX_LENGTH && (value !== null && value.length > v.length)) {
                    invalidFields.set(fieldName, t('validation.max_length', {length: v.length}));
                }
                if (v.type === VALIDATORS.MIN && (value !== null && parseInt(value) < v.size)) {
                    invalidFields.set(fieldName, t('validation.min', {size: v.size}));
                }
                if (v.type === VALIDATORS.MAX && (value !== null && parseInt(value) > v.size)) {
                    invalidFields.set(fieldName, t('validation.max', {size: v.size}));
                }
                if (field.type === TYPES.FILE && v.type === VALIDATORS.EXTENSION && value && value.name) {
                    let extensions = v.extensions;
                    let parts = value.name.split('.');
                    let fileExtension = parts[parts.length - 1].toLowerCase();
                    if (!extensions.includes(fileExtension)) {
                        invalidFields.set(fieldName, t('validation.extension', {extensions: extensions.join(',')}));
                    }
                }
                if (v.type === VALIDATORS.EMAIL && value) {
                    if (!EMAIL_REGEX.test(value)) {
                        invalidFields.set(fieldName, t('validation.email'));
                    }
                }
                if (v.type === VALIDATORS.WEB_URL && value) {
                    if (!WEB_URL_REGEX.test(value)) {
                        invalidFields.set(fieldName, t('validation.web_url'));
                    }
                }
            });
        }
        if (field.type === TYPES.TIME && value !== null) {
            const timeRegexp = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
            if (!timeRegexp.test(value)) {
                invalidFields.set(fieldName, t('validation.invalid_time_format'));
            }
        }
        return invalidFields;
    }
    static convertUIValueToServerFormat = (field, value) => {
        if (value && value.format && field.type === TYPES.DATE) {
            return value.format(SERVER_DATE_FORMAT);
        } else if (value && value.format && field.type === TYPES.DATETIME) {
            return value.format(DATETIME_FORMAT);
        } else if (value && value.format && field.type === TYPES.TIME) {
            return value.format(TIME_FORMAT);
        } else if (value && field.type === TYPES.IMAGES) {
            const images = [];
            value.forEach(v => {
                images.push({
                    id: v.id,
                    name: v.name,
                    size: v.size,
                    data: v.data,
                    type: v.type
                });
            });
            return images;
        } else if (value && field.type === TYPES.IMAGE) {
            return {
                id: value.id,
                name: value.name,
                size: value.size,
                data: value.data,
                type: value.type
            };
        }
        return value;
    }
}