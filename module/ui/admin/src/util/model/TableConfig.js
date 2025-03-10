/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export const ALIGN_LEFT = 'left';
export const ALIGN_RIGHT = 'right';

export const SORT_ASC = 'asc';
export const SORT_DESC = 'desc';

export class TableConfig {
    constructor(title, apiUrl, columns, formConfig) {
        this.title = title;
        this.apiUrl = apiUrl;
        this.columns = columns;
        this.formConfig = formConfig;
        this.elevation = 1;
        this.toolbarFilter = null;
        this.isFormDialog = true;
        this.multipart = false;
    }
    
    setElevation(elevation) {
        this.elevation = elevation;
        return this;
    }
    
    setFilter(filter) {
        this.toolbarFilter = filter;
        return this;
    }
    
    getElevation() {
        return this.elevation;
    }
    
    setToolbarActionsBefore(el) {
        this.toolbarActionsBefore = el;
        return this;
    }
    
    setToolbarActionsAfter(el) {
        this.toolbarActionsAfter = el;
        return this;
    }
    
    disablePagination() {
        this.disablePaging = true;
        return this;
    }
    
    setLastRow(lastRow) {
        this.lastRow = lastRow;
        return this;
    }
    
    setMultipart(multipart) {
        this.multipart = multipart;
    }
}

export class TableColumn {
    constructor(name, label, renderer) {
        this.name = name;
        this.label = label;
        this.renderer = renderer;
        this.sortable = false;
        this.align = ALIGN_LEFT;
        this._sort_direction = null;
    }
    width(width) {
        this.width = width;
        return this;
    }
    alignment(align) {
        this.align = align;
        return this;
    }
    setSortable() {
        this.sortable = true;
        return this;
    }
    
    asAction() {
       this.type = 'action';
       return this;
    }
    
    _toggleSortDirection() {
        if (this._sort_direction === null) {
            this._sort_direction = SORT_ASC;
        } else {
            if (this._sort_direction === SORT_ASC) {
                this._sort_direction = SORT_DESC;
            } else {
                this._sort_direction = SORT_ASC;
            }
        }
        return this._sort_direction;
    }
}

export class FormConfig {
    constructor(formFields) {
        this.formFields = formFields;
        this.spacing = 1;
        this.variant = 'standard';
        this.submit = new FormSubmit();
        this.beforeOnEditRecord = null; // should return Promise.
    }
    setBeforeOnEditRecord(func) {
        this.beforeOnEditRecord = func;
        return this;
    }
    setVariant(variant) {
        this.variant = variant;
        return this;
    }
    setSubmit(submit) {
        this.submit = submit;
        return this;
    }
    setSpacing(spacing) {
        this.spacing = spacing;
        return this;
    }
}

export class FormField {
    constructor(name, type, label) {
        this.name = name;
        this.label = label;
        this.type = type;
    }
    hide() {
        this.hidden = true;
        return this;
    }
    setGrid(grid) {
        this.grid = grid;
        return this;
    }
    validation(validators) {
        this.validators = validators;
        return this;
    }
    setAttributes(attr) {
        this.attributes = attr;
        return this;
    }
}

export class FormSubmit {
    constructor() {
        this.variant = 'outlined';
        this.color = 'inherit';
        this.icon = 'check_circle_outline';
    }
    setLabel(label) {
        this.label = label;
        return this;
    }
    setIcon(icon) {
        this.icon = icon;
        return this;
    }
    setVariant(variant) {
        this.variant = variant;
        return this;
    }
    setColor(color) {
        this.color = color;
        return this;
    }
}

export class Validator {
    constructor(type, attributes) {
        this.type = type;
        this.attr = attributes;
        if (attributes && attributes.size) {
            this.size = attributes.size;
        }
    }
}

export class ApiURL {
    
    constructor(getUrl, postUrl, putUrl, deleteUrl) {
        this.getUrl = getUrl;
        this.postUrl = postUrl;
        this.putUrl = putUrl;
        this.deleteUrl = deleteUrl;
        this.getExtraParams = {};
        this.beforeCreate = null;
        this.beforeUpdate = null;
    }
    
    addGetExtraParam(name, value) {
        this.getExtraParams[name] = value;
        return this;
    }
    
    getGetExtraParams() {
        let params = [];
        for (let key in this.getExtraParams) {
            params.push(`${key}=${this.getExtraParams[key]}`);
        }
        return params.join('&');
    }
}
