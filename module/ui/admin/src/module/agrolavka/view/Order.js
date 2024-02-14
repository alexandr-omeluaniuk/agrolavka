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
import Divider from '@material-ui/core/Divider';
import Link from '@material-ui/core/Link';
import Chip from '@material-ui/core/Chip';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import TextField from '@material-ui/core/TextField';
import Typography from '@material-ui/core/Typography';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import moment from 'moment';
import { WAITING_FOR_APPROVAL, APPROVED, DELIVERY, CLOSED } from '../constants/OrderStatus';
import DataTable from '../../../component/datatable/DataTable';
import { TableConfig, TableColumn, FormConfig, FormField, Validator, ALIGN_RIGHT, ApiURL } from '../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../service/DataTypeService';
import Price from '../component/Price';
import { NavLink } from "react-router-dom";
import useOrdersForPrint from '../hooks/useOrdersForPrint';

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
    },
    lastRow: {
        display: 'flex',
        justifyContent: 'space-between',
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(2),
        alignItems: 'center'
    },
    divider: {
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(2)
    },
    chip: {
        margin: theme.spacing(1)
    },
    formControl: {
        width: '100%',
        marginRight: theme.spacing(2)
    },
    actions: {
        display: 'flex',
        alignItems: 'center'
    },
    comment: {
        whiteSpace: 'pre'
    },
    adminComment: {
        marginTop: theme.spacing(2)
    },
    printAdd: {
        color: theme.palette.success.main
    },
    printRemove: {
        color: theme.palette.error.main
    }
}));

function Order(props) {
    const classes = useStyles();
    const id = props.match.params.id;
    const { setTitle, setIcon } = useContext(ToolbarContext);
    const [order, setOrder] = React.useState(null);
    const [tableConfig, setTableConfig] = React.useState(null);
    const [adminComment, setAdminComment] = React.useState(null);
    const { t } = useTranslation();
    const { addOrder, removeOrder, ordersForPrint } = useOrdersForPrint();
    // -------------------------------------------------------- METHODS -------------------------------------------------------------------
    const getNum = (order) => {
        let num = order.id.toString();
        while (num.length < 5) num = "0" + num;
        return num;
    };
    const onStatusChanged = (status) => {
        order.status = status;
        dataService.put('/agrolavka/protected/order', order).then(() => {
            setOrder(JSON.parse(JSON.stringify(order)));
        });
    };
    const onAdminCommentChanged = () => {
        order.adminComment = adminComment;
        dataService.put('/agrolavka/protected/order', order).then(() => {
            setOrder(JSON.parse(JSON.stringify(order)));
        });
    };
    // -------------------------------------------------------- HOOKS ---------------------------------------------------------------------
    useEffect(() => {
        dataService.get('/agrolavka/protected/order/' + id).then(data => {
            setOrder(data);
            setIcon('payments');
            setTitle(t('m_agrolavka:order.title', {num: getNum(data)}));
        });
    }, [id, setIcon, setTitle, t]);
    useEffect(() => {
        if (order) {
            setAdminComment(order.adminComment);
            setTableConfig(new TableConfig(t('m_agrolavka:order.positions'), new ApiURL(
                        '/agrolavka/protected/order-position/' + order.id,
                        null,
                        '/agrolavka/protected/order-position',
                        '/agrolavka/protected/order-position'
                    ), [
                new TableColumn('avatar', '', (row) => {
                    return <Avatar className={classes.image} alt={row.name}
                            src={row.product && row.product.images && row.product.images.length > 0
                            ? `/media/${row.product.images[0].fileNameOnDisk}?timestamp=${new Date().getTime()}`
                                : `/assets/img/no-image.png`} />;
                }).setSortable().width('40px'),
                new TableColumn('name', t('m_agrolavka:order.position.name'), (row) => {
                    if (row.variant) {
                        return row.variant.name;
                    } else if (row.product) {
                        return row.product.name;
                    } else {
                        return '<--->';
                    }
                }).setSortable(),
                new TableColumn('volume', t('m_agrolavka:order.position.volume'), (row) => {
                    let volume = '';
                    if (row.product && row.product.productVolumes && row.product.productVolumes.length > 0) {
                        const volume = row.product.productVolumes.filter(item => item.price === row.price)[0];
                        if (volume) {
                            return volume.amount + (volume.unit === 'LITER' ? 'л' : '');
                        } else {
                            console.log(row);
                        }
                    }
                    return volume;
                }).width('100px').alignment(ALIGN_RIGHT),
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
                ]).setAttributes({ decimalScale: 2, suffix: ' BYN', align: 'left' })
            ])).setElevation(0).disablePagination().setLastRow((data) => {
                let total = 0;
                data.forEach(pos => {
                    total += pos.quantity * pos.price;
                });
                return (
                        <div className={classes.lastRow}>
                            <Typography variant={'h5'}>{t('m_agrolavka:order.total')}</Typography>
                            <Price price={total}/>
                        </div>
                );
            }));
        }
    }, [order, classes.image, classes.lastRow, id, t]);
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
        const inPrintList = order && ordersForPrint.filter(o => o.id === order.id).length > 0;
        return (
                <div className={classes.actions}>
                    {inPrintList ? (
                    <Tooltip title={t('m_agrolavka:orders.print_remove')} key="1">
                        <IconButton onClick={() => removeOrder(order)}>
                            <Icon className={classes.printRemove}>playlist_remove</Icon>
                        </IconButton>
                    </Tooltip>
                    ) : (
                    <Tooltip title={t('m_agrolavka:orders.print_add')} key="2">
                        <IconButton onClick={() => addOrder(order)}>
                            <Icon className={classes.printAdd}>playlist_add</Icon>
                        </IconButton>
                    </Tooltip>
                    )}
                    <Tooltip title={t('m_agrolavka:orders.print')} key="3">
                        <IconButton onClick={() => {
                                dataService.getFile('/api/agrolavka/protected/order/print/' + order.id, `Заказ №${order.id}.pdf`).then(resp => {
                                });
                        }}>
                            <Icon>print</Icon>
                        </IconButton>
                    </Tooltip>
                    <NavLink to={'/admin/app/agrolavka/orders'} key="4">
                        <Tooltip title={t('m_agrolavka:order.close')} key="5">
                            <IconButton>
                                <Icon color="primary">chevron_left</Icon>
                            </IconButton>
                        </Tooltip>
                    </NavLink>
                </div>
                
        );
    };
    const contactInfo = () => {
        const result = [];
        let fullname = [];
        if (order.address) {
            if (order.address.lastname) {
                fullname.push(order.address.lastname);
            }
            if (order.address.firstname) {
                fullname.push(order.address.firstname);
            }
            if (order.address.middlename) {
                fullname.push(order.address.middlename);
            }
        }
        if (order.europostLocationSnapshot) {
            if (order.europostLocationSnapshot.lastname) {
                fullname.push(order.europostLocationSnapshot.lastname);
            }
            if (order.europostLocationSnapshot.firstname) {
                fullname.push(order.europostLocationSnapshot.firstname);
            }
            if (order.europostLocationSnapshot.middlename) {
                fullname.push(order.europostLocationSnapshot.middlename);
            }
        }
        if (fullname.length > 0) {
            result.push(<React.Fragment key="1"><Typography variant={'caption'}>{fullname.join(' ')}</Typography><br/></React.Fragment>);
        }
        result.push(<Link href={'tel:+375' + order.phone.replace(/\D/g,'')} color="primary" key="2">{order.phone}</Link>);
        return result;
    };
    const chip = (label, value) => {
        return (
                <React.Fragment>
                    <Typography variant="caption" component="span" color="textSecondary"><b>{label}</b></Typography>
                    <Typography variant="subtitle2" component="span">{value}</Typography>
                </React.Fragment>
        );
    };
    const address = () => {
        const result = [];
        if (order.address.region) {
            result.push(<Chip label={chip('Область: ', order.address.region)} key={4} color="secondary" className={classes.chip}/>);
        }
        if (order.address.district) {
            result.push(<Chip label={chip('Район: ', order.address.district)} key={5} color="secondary" className={classes.chip}/>);
        }
        if (order.address.city) {
            result.push(<Chip label={chip('Населенный пункт: ', order.address.city)} key={6} color="secondary" className={classes.chip}/>);
        }
        if (order.address.street) {
            result.push(<Chip label={chip('Улица: ', order.address.street)} key={7} color="secondary" className={classes.chip}/>);
        }
        if (order.address.house) {
            result.push(<Chip label={chip('Дом: ', order.address.house)} key={8} color="secondary" className={classes.chip}/>);
        }
        if (order.address.flat) {
            result.push(<Chip label={chip('Квартира: ', order.address.flat)} key={9} color="secondary" className={classes.chip}/>);
        }
        if (order.address.postcode) {
            result.push(<Chip label={chip('Почтовый индекс: ', order.address.postcode)} key={10} color="secondary" className={classes.chip}/>);
        }
        return result;
    };
    const europost = () => {
        const result = [];
        result.push(<Chip label={chip('Способ доставки: ', t('m_agrolavka:orders.delivery_europost'))} key={1} color="secondary" className={classes.chip}/>);
        result.push(<Chip label={chip('Отделение: ', order.europostLocationSnapshot.address)} key={2} color="secondary" className={classes.chip}/>);
        let fullname = [];
        if (order.europostLocationSnapshot.lastname) {
            fullname.push(order.europostLocationSnapshot.lastname);
        }
        if (order.europostLocationSnapshot.firstname) {
            fullname.push(order.europostLocationSnapshot.firstname);
        }
        if (order.europostLocationSnapshot.middlename) {
            fullname.push(order.europostLocationSnapshot.middlename);
        }
        if (fullname.length > 0) {
            result.push(<Chip label={chip('Получатель: ', fullname.join(' '))} key={3} color="secondary" className={classes.chip}/>);
        }
        return result;
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
                    {order ? (
                            <React.Fragment>
                                <Divider className={classes.divider}/>
                                <FormControl variant="outlined" className={classes.formControl}>
                                    <InputLabel>{t('m_agrolavka:order.status')}</InputLabel>
                                    <Select value={order.status} onChange={(e) => onStatusChanged(e.target.value)}
                                            label={t('m_agrolavka:order.status')}>
                                        <MenuItem value={'WAITING_FOR_APPROVAL'}>
                                            {t('m_agrolavka:order.statusConst.WAITING_FOR_APPROVAL')}</MenuItem>
                                        <MenuItem value={'APPROVED'}>{t('m_agrolavka:order.statusConst.APPROVED')}</MenuItem>
                                        <MenuItem value={'DELIVERY'}>{t('m_agrolavka:order.statusConst.DELIVERY')}</MenuItem>
                                        <MenuItem value={'CLOSED'}>{t('m_agrolavka:order.statusConst.CLOSED')}</MenuItem>
                                    </Select>
                                </FormControl>
                                <TextField label={t('m_agrolavka:order.adminComment')} fullWidth={true} 
                                    onChange={(e) => {
                                        setAdminComment(e.target.value);
                                    }} onBlur={onAdminCommentChanged} className={classes.adminComment}
                                    value={adminComment ? adminComment : ''} name={'admin_comment'} multiline variant={'outlined'} rows={4}/>
                            </React.Fragment>
                    ) : null}
                    <Divider className={classes.divider} key="divider-1"/>
                    <Typography variant={'h6'}>
                        {t('m_agrolavka:order.contacts')}
                    </Typography>
                    {contactInfo()}
                    {order.oneClick ? <i>{t('m_agrolavka:orders.one_click')}</i> : null}
                    {order.comment ? (
                            <React.Fragment>
                                <Divider className={classes.divider} key="divider-5"/>
                                <Typography variant={'h6'}>
                                    {t('m_agrolavka:order.comment')}
                                </Typography>
                                <Typography variant={'caption'} className={classes.comment}>
                                    {order.comment}
                                </Typography>
                            </React.Fragment>
                    ) : null}
                    {order.address ? (
                        <React.Fragment>
                            <Divider className={classes.divider} key="divider-2"/>
                            <Typography variant={'h6'}>
                                {t('m_agrolavka:orders.address')}
                            </Typography>
                            {address()}
                        </React.Fragment>) : null}
                    {order.europostLocationSnapshot ? (<React.Fragment><Divider className={classes.divider} key="divider-3"/>{europost()}</React.Fragment>) : null}
                    <Divider className={classes.divider} key="divider-4"/>
                    <DataTable tableConfig={tableConfig}/>
                </CardContent>
            </Card>
    );
}

export default Order;

