/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { TableConfig, TableColumn, FormConfig, FormField, ApiURL , Validator } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import DataTable from '../../../component/datatable/DataTable';
import Avatar from '@material-ui/core/Avatar';

function Shops() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/platform/entity/ss.entity.agrolavka.Shop',
                '/platform/entity/ss.entity.agrolavka.Shop',
                '/platform/entity/ss.entity.agrolavka.Shop',
                '/platform/entity/ss.entity.agrolavka.Shop'
        );
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.shops'), apiUrl, [
            new TableColumn('avatar', '', (row) => {
                return <Avatar alt={row.name}
                        src={row.images && row.images.length > 0 && row.images[0].fileNameOnDisk 
                        ? `/media/${row.mainImage.fileNameOnDisk}?timestamp=${new Date().getTime()}`
                        : `/assets/img/no-image.png`} />;
            }).width('40px'),
            new TableColumn('title', t('m_agrolavka:shops.title'), (row) => {
                return row.title;
            })
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('title', TYPES.TEXTFIELD, t('m_agrolavka:shops.title')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('description', TYPES.TEXTAREA, t('m_agrolavka:shops.description')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 5000})
            ]),
            new FormField('address', TYPES.TEXTAREA, t('m_agrolavka:shops.address')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('workingHours', TYPES.TEXTAREA, t('m_agrolavka:shops.working_hours')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('latitude', TYPES.DOUBLE_NUMBER, t('m_agrolavka:shops.latitude')).setGrid({xs: 12, md: 6}).validation([
                new Validator(VALIDATORS.REQUIRED)
            ]).setAttributes({ decimalScale: 6}),
            new FormField('longitude', TYPES.DOUBLE_NUMBER, t('m_agrolavka:shops.longitude')).setGrid({xs: 12, md: 6}).validation([
                new Validator(VALIDATORS.REQUIRED)
            ]).setAttributes({ decimalScale: 6}),
            new FormField('mainImage', TYPES.IMAGE, t('m_agrolavka:shops.main_image')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED)
            ]),
            new FormField('images', TYPES.IMAGES, t('m_agrolavka:shops.images')).setGrid({xs: 12})
        ]).setBeforeOnEditRecord((record) => {
            return new Promise((resolve) => {
                record.images.forEach(i => {
                    i.data = `/media/` + i.fileNameOnDisk;
                });
                record.mainImage.data = `/media/` + record.mainImage.fileNameOnDisk;
                resolve(record);
            });
        })).setElevation(1);
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

export default Shops;

