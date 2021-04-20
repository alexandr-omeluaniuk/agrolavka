/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import DataTable from '../../../component/datatable/DataTable';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT } from '../../../util/model/TableConfig';
import Icon from '@material-ui/core/Icon';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';

const useStyles = makeStyles(theme => ({
    active: {
        color: theme.palette.success.main
    },
    inactive: {
        color: theme.palette.grey.main
    }
}));

function EmailTemplates() {
    const classes = useStyles();
    const { t } = useTranslation();
    const config = new TableConfig(t('m_administrator:administrator.email.emailtemplates'),
            '/platform/entity/ss.entity.martin.EmailTemplate', [
        new TableColumn('subject', t('m_administrator:email_templates.subject')).setSortable()
    ], new FormConfig([
        new FormField('id', TYPES.ID).hide(),
        new FormField('subject', TYPES.TEXTFIELD, t('m_administrator:email_templates.subject')).setGrid({xs: 12}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ]),
        new FormField('content', TYPES.HTML_EDITOR, t('m_administrator:email_templates.content')).setGrid({xs: 12}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 2147483647})
        ]).setAttributes({
            labelWidth: 100,
            rows: 10
        })
    ]));
    return (
        <DataTable tableConfig={config}/>
    );
}

export default EmailTemplates;
