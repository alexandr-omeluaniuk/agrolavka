/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import { TableConfig, TableColumn, FormConfig, FormField, ApiURL , Validator } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';

const DAYS = [
    {
        value: 1,
        label: 'Понедельник'
    },
    {
        value: 2,
        label: 'Вторник'
    },
    {
        value: 3,
        label: 'Среда'
    },
    {
        value: 4,
        label: 'Четверг'
    },
    {
        value: 5,
        label: 'Пятница'
    },
    {
        value: 6,
        label: 'Суббота'
    },
    {
        value: 7,
        label: 'Воскресенье'
    }
]

function ScheduleSettings() {
    const [tableConfig, setTableConfig] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/schedule-settings',
                '/agrolavka/protected/schedule-settings',
                '/agrolavka/protected/schedule-settings',
                '/agrolavka/protected/schedule-settings'
        );
        const newTableConfig = new TableConfig('Запрет показа товаров', apiUrl, [
            new TableColumn('dayOfWeek', 'День недели', (row) => {
                return DAYS.filter(d => d.value === row.dayOfWeek)[0].label;
            }),
            new TableColumn('fromHours', 'От', (row) => {
                return `${row.fromHours < 10 ? `0${row.fromHours}` : row.fromHours}:${row.fromMinutes < 10 ? `0${row.fromMinutes}` : row.fromMinutes}`;
            }),
            new TableColumn('toHours', 'До', (row) => {
                return `${row.toHours < 10 ? `0${row.toHours}` : row.toHours}:${row.toMinutes < 10 ? `0${row.toMinutes}` : row.toMinutes}`;
            }),
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('dayOfWeek', TYPES.SELECT, 'День недели').setAttributes({options: DAYS}).setGrid({lg: 4, xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED)
            ]),
            new FormField('fromHours', TYPES.INTEGER_NUMBER, "Часы (от)").setGrid({lg: 2, xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0}),
                new Validator(VALIDATORS.MAX, {size: 23})
            ]),
            new FormField('fromMinutes', TYPES.INTEGER_NUMBER, "Минуты (от)").setGrid({lg: 2, xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0}),
                new Validator(VALIDATORS.MAX, {size: 59})
            ]),
            new FormField('toHours', TYPES.INTEGER_NUMBER, "Часы (до)").setGrid({lg: 2, xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0}),
                new Validator(VALIDATORS.MAX, {size: 23})
            ]),
            new FormField('toMinutes', TYPES.INTEGER_NUMBER, "Минуты (до)").setGrid({lg: 2, xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MIN, {size: 0}),
                new Validator(VALIDATORS.MAX, {size: 59})
            ]),
        ])).setElevation(1);
        setTableConfig(newTableConfig);
    };
    // ------------------------------------------------------- HOOKS ----------------------------------------------------------------------
    useEffect(() => {
        if (tableConfig === null) {
            updateTable();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [tableConfig]);
    // ------------------------------------------------------- RENDERING ------------------------------------------------------------------
    if (tableConfig === null) {
        return null;
    }
    return (
            <DataTable tableConfig={tableConfig}/>
    );
}

export default ScheduleSettings;

