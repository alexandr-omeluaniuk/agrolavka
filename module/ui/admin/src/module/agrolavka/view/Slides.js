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

function Slides() {
    const { t } = useTranslation();
    const [tableConfig, setTableConfig] = React.useState(null);
    // ------------------------------------------------------- METHODS --------------------------------------------------------------------
    const updateTable = () => {
        const apiUrl = new ApiURL(
                '/agrolavka/protected/slide',
                '/agrolavka/protected/slide',
                '/agrolavka/protected/slide',
                '/agrolavka/protected/slide'
        );
        const newTableConfig = new TableConfig(t('m_agrolavka:agrolavka.slides'), apiUrl, [
            new TableColumn('avatar', '', (row) => {
                return <Avatar alt={row.name}
                        src={row.images && row.images.length > 0 && row.images[0].fileNameOnDisk 
                        ? `/media/${row.images[0].fileNameOnDisk}?timestamp=${new Date().getTime()}`
                        : `/assets/img/no-image.png`} />;
            }).width('40px'),
            new TableColumn('title', t('m_agrolavka:slides.name'), (row) => {
                return row.title;
            })
        ], new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:slides.name')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('title', TYPES.TEXTFIELD, t('m_agrolavka:slides.title')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.MAX_LENGTH, {length: 1000})
            ]),
            new FormField('subtitle', TYPES.TEXTFIELD, t('m_agrolavka:slides.subtitle')).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.MAX_LENGTH, {length: 3000})
            ]),
            new FormField('buttonText', TYPES.TEXTFIELD, t('m_agrolavka:slides.button_text')).setGrid({xs: 12, md: 6}).validation([
                new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
            ]),
            new FormField('buttonLink', TYPES.TEXTFIELD, t('m_agrolavka:slides.button_link')).setGrid({xs: 12, md: 6}).validation([
                new Validator(VALIDATORS.MAX_LENGTH, {length: 1000})
            ]),
            new FormField('images', TYPES.IMAGES, t('m_agrolavka:shops.images')).setAttributes({valueType: 'file', multipart: 'image'}).setGrid({xs: 12}).validation([
                new Validator(VALIDATORS.REQUIRED)
            ])
        ]).setBeforeOnEditRecord((record) => {
            return new Promise((resolve) => {
                record.images.forEach(i => {
                    i.data = `/media/` + i.fileNameOnDisk;
                });
                resolve(record);
            });
        })).setElevation(1);
        newTableConfig.setMultipart(true);
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

export default Slides;

