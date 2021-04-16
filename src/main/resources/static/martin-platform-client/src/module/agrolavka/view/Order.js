/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect, useContext } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { ToolbarContext } from '../../../context/ToolbarContext';
import DataService from '../../../service/DataService';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import moment from 'moment';
import { WAITING_FOR_APPROVAL, APPROVED, DELIVERY, CLOSED } from '../constants/OrderStatus';
import DataTable from '../../../component/datatable/DataTable';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import Price from '../component/Price';
import { NavLink } from "react-router-dom";

const dataService = new DataService();

const useStyles = makeStyles(theme => ({
    WAITING_FOR_APPROVAL: {
        backgroundColor: theme.palette.warning.main
    },
    APPROVED: {
        backgroundColor: theme.palette.info.main
    },
    DELIVERY: {
        backgroundColor: theme.palette.info.main
    },
    CLOSED: {
        backgroundColor: theme.palette.success.main
    },
    image: {
        borderRadius: 0
    }
}));

function Order(props) {
    const classes = useStyles();
    const id = props.match.params.id;
    const { setTitle, setIcon } = useContext(ToolbarContext);
    const [order, setOrder] = React.useState(null);
    const [tableConfig, setTableConfig] = React.useState(null);
    const { t } = useTranslation();
    // -------------------------------------------------------- METHODS -------------------------------------------------------------------
    const getNum = (order) => {
        let num = order.id.toString();
        while (num.length < 5) num = "0" + num;
        return num;
    };
    // -------------------------------------------------------- HOOKS ---------------------------------------------------------------------
    useEffect(() => {
        dataService.get('/agrolavka/protected/order/' + id).then(data => {
            setOrder(data);
            console.log(data);
            setIcon('payments');
            setTitle(t('m_agrolavka:order.title', {num: getNum(data)}));
        });
    }, [id, setIcon, setTitle, t]);
    useEffect(() => {
        if (order) {
            setTableConfig(new TableConfig(t('m_agrolavka:order.positions'), new ApiURL(
                        '/agrolavka/protected/order/positions/' + order.id,
                        '/agrolavka/protected/order/position/' + id,
                        '/agrolavka/protected/order/position',
                        '/agrolavka/protected/order/position/' + id
                    ), [
                new TableColumn('avatar', '', (row) => {
                    return <Avatar className={classes.image} alt={row.name}
                            src={`/api/agrolavka/public/product-image/${row.productId}?timestamp=${new Date().getTime()}`} />;
                }).setSortable().width('40px'),
                new TableColumn('name', t('m_agrolavka:order.position.name'), (row) => {
                    return row.product.name;
                }).setSortable(),
                new TableColumn('quantity', t('m_agrolavka:order.position.quantity'), (row) => {
                    return row.quantity;
                }).setSortable().width('100px').alignment(ALIGN_RIGHT),
                new TableColumn('price', t('m_agrolavka:order.position.price'), (row) => {
                    return <Price price={row.price} />;
                }).setSortable().width('160px').alignment(ALIGN_RIGHT),
                new TableColumn('total', t('m_agrolavka:order.position.subtotal'), (row) => {
                    return <Price price={row.price * row.quantity} />;
                }).setSortable().width('160px').alignment(ALIGN_RIGHT)
            ], new FormConfig([
                new FormField('id', TYPES.ID).hide(),
                new FormField('quantity', TYPES.INTEGER_NUMBER, t('m_agrolavka:order.position.quantity')).setGrid({xs: 12, md: 6}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MIN, {size: 1})
                ]),
                new FormField('price', TYPES.MONEY, t('m_agrolavka:order.position.price')).setGrid({xs: 12, md: 6}).validation([
                    new Validator(VALIDATORS.REQUIRED),
                    new Validator(VALIDATORS.MIN, {size: 0})
                ]).setAttributes({ decimalScale: 2, suffix: ' BYN', align: 'right' })
            ])).setElevation(0).disablePagination());
        }
    }, [order, classes.image, id, t]);
    // -------------------------------------------------------- RENDERING -----------------------------------------------------------------
    const avatar = () => {
        let icon;
        if (order.status === WAITING_FOR_APPROVAL) {
            icon = 'hourglass_empty';
        } else if (order.status === APPROVED) {
            icon = 'check_circle';
        } else if (order.status === DELIVERY) {
            icon = 'local_shipping';
        } else if (order.status === CLOSED) {
            icon = 'done_all';
        }
        return <Avatar className={classes[order.status]}><Icon>{icon}</Icon></Avatar>;
    };
    const actions = () => {
        return (
                <NavLink to={'/admin/app/agrolavka/orders'}>
                    <Tooltip title={t('m_agrolavka:order.close')}>
                        <IconButton>
                            <Icon color="primary">chevron_left</Icon>
                        </IconButton>
                    </Tooltip>
                </NavLink>
        );
    };
    if (!order || !tableConfig) {
        return null;
    }
    return (
            <Card>
                <CardHeader title={t('m_agrolavka:order.title', {num: getNum(order)})} avatar={avatar()} action={actions()}
                        subheader={t('m_agrolavka:order.created') + ': '  + moment(order.created).locale('ru').format('DD MMMM yyyy HH:mm')}>
                </CardHeader>
                <CardContent>
                    <DataTable tableConfig={tableConfig}/>
                </CardContent>
            </Card>
    );
}

export default Order;

