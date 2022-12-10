/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
import React, { useEffect, useContext } from 'react';
import { ToolbarContext } from '../../../context/ToolbarContext';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import { FormConfig, FormField, Validator } from '../../../util/model/TableConfig';
import { useTranslation } from 'react-i18next';
import DataService from '../../../service/DataService';
import { makeStyles } from '@material-ui/core/styles';
import Form from './../../../component/form/Form';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Paper from '@material-ui/core/Paper';
import Icon from '@material-ui/core/Icon';
import Divider from '@material-ui/core/Divider';
import { NavLink } from "react-router-dom";

const dataService = new DataService();

const useStyles = makeStyles(theme => ({
    paper: {
        padding: theme.spacing(2)
    },
    title: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    divider: {
        marginBottom: theme.spacing(4),
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1)
    }
}));

function Product(props) {
    const id = props.match.params.id;
    const { t } = useTranslation();
    const classes = useStyles();
    const { setTitle, setIcon } = useContext(ToolbarContext);
    const [product, setProduct] = React.useState(null);
    const [formDisabled, setFormDisabled] = React.useState(false);
    
    const formConfig = new FormConfig([
        new FormField('id', TYPES.ID).hide(),
        new FormField('name', TYPES.TEXTFIELD, t('m_agrolavka:products.product_name')).setGrid({xs: 12, md: 9}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ]),
        new FormField('price', TYPES.MONEY, t('m_agrolavka:products.product_price')).setGrid({xs: 12, md: 3}).validation([
            new Validator(VALIDATORS.REQUIRED),
            new Validator(VALIDATORS.MIN, {size: 0})
        ]).setAttributes({ decimalScale: 2, suffix: ' BYN', align: 'right' }),
        new FormField('description', TYPES.HTML, t('m_agrolavka:products.product_description')).setGrid({xs: 12})
                .setAttributes({ labelWidth: 200 }),
        new FormField('images', TYPES.IMAGES, t('m_agrolavka:products.product_images')).setGrid({xs: 12}),
        new FormField('videoURL', TYPES.TEXTFIELD, t('m_agrolavka:products.product_video_url')).setGrid({xs: 12}).validation([
            new Validator(VALIDATORS.MAX_LENGTH, {length: 300}),
            new Validator(VALIDATORS.WEB_URL)
        ]),
        new FormField('seoTitle', TYPES.TEXTFIELD, t('m_agrolavka:products.product_seo_title')).setGrid({xs: 12}).validation([
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ]),
        new FormField('seoDescription', TYPES.TEXTFIELD, t('m_agrolavka:products.product_seo_description')).setGrid({xs: 12})
                .validation([
            new Validator(VALIDATORS.MAX_LENGTH, {length: 255})
        ])
    ]);
    
    useEffect(() => {
        dataService.get('/agrolavka/protected/product/' + id).then(data => {
            data.images.forEach(i => {
                i.data = `/media/` + i.fileNameOnDisk;
            });
            setProduct(data);
            setIcon('fastfood');
            setTitle(data.name);
        });
    }, [id, setIcon, setTitle, t]);
    
    const onFormSubmitAction = (data) => {
        setFormDisabled(true);
        dataService.put('/platform/entity/ss.entity.agrolavka.Product', data).then(() => {
            setFormDisabled(false);
            props.history.push('/admin/app/agrolavka/products');
        });
    };
    
    if (product === null) {
        return null;
    }
    
    return (
        <Paper elevation={1} className={classes.paper}>
            <div className={classes.title}>
                <Typography variant="h6">{product.name}</Typography>
                <NavLink to={'/admin/app/agrolavka/products'}>
                    <IconButton aria-label="close" className={classes.closeButton}>
                        <Icon>close</Icon>
                    </IconButton>
                </NavLink>
            </div>
            <Divider className={classes.divider}/>
            <Form formConfig={formConfig} onSubmitAction={onFormSubmitAction} record={product} disabled={formDisabled}/>
        </Paper>
    );
}

export default Product;

