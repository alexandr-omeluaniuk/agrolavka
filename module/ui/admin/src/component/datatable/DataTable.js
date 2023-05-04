/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, {useEffect} from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { useTranslation } from 'react-i18next';
import DataService from '../../service/DataService';
import { DataTypeService } from '../../service/DataTypeService';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableContainer from '@material-ui/core/TableContainer';
import DataTableHead from './DataTableHead';
import DataTableToolbar from './DataTableToolbar';
import DataTableBody from './DataTableBody';
import DataTableBodyMobile from './DataTableBodyMobile';
import FormDialog from './../../component/window/FormDialog';
import Form from './../../component/form/Form';
import ConfirmDialog from '../window/ConfirmDialog';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import TablePagination from '@material-ui/core/TablePagination';
import { DENSE_PADDING, ITEMS_PER_PAGE } from '../../conf/local-storage-keys';
import { ApiURL } from '../../util/model/TableConfig';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';

let dataService = new DataService();

const LAST_PAGE_NUMBER = 'last-page-number';

const useStyles = makeStyles(theme => ({
    paperMobile: {
        backgroundColor: '#fff0'
    },
    paper: {
        padding: theme.spacing(2)
    },
    title: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
    }
}));

const getDefaultPage = () => {
    return sessionStorage.getItem(LAST_PAGE_NUMBER) ? parseInt(sessionStorage.getItem(LAST_PAGE_NUMBER)) : 0;
};

function DataTable(props) {
    const classes = useStyles();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const { t } = useTranslation();
    const { tableConfig } = props;
    const [load, setLoad] = React.useState(null);
    const [data, setData] = React.useState(null);
    const [total, setTotal] = React.useState(0);
    const [order, setOrder] = React.useState('asc');
    const [orderBy, setOrderBy] = React.useState(null);
    const [formTitle, setFormTitle] = React.useState('');
    const [formOpen, setFormOpen] = React.useState(false);
    const [record, setRecord] = React.useState(null);
    const [confirmDialogOpen, setConfirmDialogOpen] = React.useState(false);
    const [dense, setDense] = React.useState(
            localStorage.getItem(DENSE_PADDING) && localStorage.getItem(DENSE_PADDING) !== 'false' ? true : false);
    const [page, setPage] = React.useState(null);
    const [rowsPerPage, setRowsPerPage] = React.useState(
            localStorage.getItem(ITEMS_PER_PAGE) ? parseInt(localStorage.getItem(ITEMS_PER_PAGE)) : 5);
    const [formDisabled, setFormDisabled] = React.useState(false);
    // ============================================================ HOOKS =================================================================
    useEffect(() => {
        setPage(getDefaultPage());
    }, [tableConfig]);
    useEffect(() => {
        if (page !== null) {
            let params = tableConfig.disablePaging ? '?' : `?page=${page + 1}&page_size=${rowsPerPage}`;
            if (order && orderBy) {
                params += `&order=${order}&order_by=${orderBy}`;
            }
            if (tableConfig.apiUrl instanceof ApiURL && tableConfig.apiUrl.getGetExtraParams()) {
                params += '&' + tableConfig.apiUrl.getGetExtraParams();
            }
            dataService.get(`${tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.getUrl : tableConfig.apiUrl}${params}`).then(resp => {
                if (resp) {
                    setData(tableConfig.disablePaging ? resp : resp.data);
                    setTotal(tableConfig.disablePaging ? resp.length : resp.total);
                    const maxPage = Math.floor(resp.total / rowsPerPage);
                    if (page > maxPage) {
                        setPage(0);
                    }
                }
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [load, tableConfig, page, rowsPerPage, order, orderBy]);
//    useEffect(() => {
//        return () => {
//            dataService.abort();
//        };
//        // eslint-disable-next-line react-hooks/exhaustive-deps
//    }, []);
    // ============================================================ METHODS ===============================================================
    const onSort = (column) => {
        setOrder(column._toggleSortDirection());
        setOrderBy(column.name);
        setLoad(!load);
    };
    const onNewRecord = () => {
        setRecord(null);
        setFormTitle(t('component.datatable.new'));
        setFormOpen(true);
    };
    const onEditRecord = (rowData) => {
        const doEdit = (entity) => {
            setRecord(entity);
            setFormTitle(t('component.datatable.edit'));
            setFormOpen(true);
        };
        if (tableConfig.formConfig.beforeOnEditRecord) {
            tableConfig.formConfig.beforeOnEditRecord(rowData).then(entity => {
                doEdit(rowData);
            });
        } else {
            doEdit(rowData);
        }
        
    };
    const onDeleteRecord = (rowData) => {
        setRecord(rowData);
        setConfirmDialogOpen(true);
    };
    const doDeleteRecord = () => {
        let id = DataTypeService.getIdValue(tableConfig.formConfig, record);
        dataService.delete(`${tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.deleteUrl : tableConfig.apiUrl}/${id}`)
                .then(data => {
            setLoad(!load);
        });
    };
    const onFormSubmitAction = (data) => {
        setFormDisabled(true);
        let id = DataTypeService.getIdValue(tableConfig.formConfig, data);
        if (id) {
            if (tableConfig.apiUrl.beforeUpdate) {
                data = tableConfig.apiUrl.beforeUpdate(data, record);
            }
            dataService.put(tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.putUrl : tableConfig.apiUrl, data).then(() => {
                setFormDisabled(false);
                setFormOpen(false);
                setLoad(!load);
            });
        } else {
            if (tableConfig.apiUrl.beforeCreate) {
                data = tableConfig.apiUrl.beforeCreate(data);
            }
            dataService.post(tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.postUrl : tableConfig.apiUrl, data).then(() => {
                setFormDisabled(false);
                setFormOpen(false);
                setLoad(!load);
            });
        }
    };
    const onRefresh = () => {
        setLoad(!load);
    };
    const handleChangeDense = (event) => {
        setDense(event.target.checked);
        localStorage.setItem(DENSE_PADDING, event.target.checked);
    };
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
        sessionStorage.setItem(LAST_PAGE_NUMBER, newPage);
    };
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        localStorage.setItem(ITEMS_PER_PAGE, parseInt(event.target.value, 10));
        setPage(0);
    };
    // ============================================================ RENDERING =============================================================
    const pagination = () => {
        let maxPage = Math.floor(total / rowsPerPage);
        let c = function () {
            return <TablePagination rowsPerPageOptions={[5, 10, 25]} component="div" count={total}
                    rowsPerPage={rowsPerPage} page={page >= maxPage ? maxPage : page}
                    onChangePage={handleChangePage} onChangeRowsPerPage={handleChangeRowsPerPage}/>;
        };
        return <Paper elevation={tableConfig.getElevation()}>{c()}</Paper>;
    };
    let actualFormConfig = tableConfig.formConfig;
    if (record && tableConfig.formConfigEdit) {
        actualFormConfig = tableConfig.formConfigEdit;
    }
    const isEditable = tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.putUrl : (tableConfig.apiUrl ? true : false);
    const isDeletable = tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.deleteUrl : (tableConfig.apiUrl ? true : false);
    const isCreatable = tableConfig.apiUrl instanceof ApiURL ? tableConfig.apiUrl.postUrl : (tableConfig.apiUrl ? true : false);
    if (formOpen && !tableConfig.isFormDialog) {
        return (
                <Paper elevation={1} className={classes.paper}>
                    <div className={classes.title}>
                        <Typography variant="h6">{formTitle}</Typography>
                        <IconButton aria-label="close" className={classes.closeButton} onClick={() => setFormOpen(false)}>
                            <Icon>close</Icon>
                        </IconButton>
                    </div>
                    <Form formConfig={actualFormConfig} onSubmitAction={onFormSubmitAction} record={record} disabled={formDisabled}/>
                </Paper>
        );
    }
    return (
        <div>
            <Paper className={isMobile ? classes.paperMobile : null} elevation={isMobile ? 0 : (tableConfig.getElevation())}>
                <DataTableToolbar tableConfig={tableConfig} onNewRecord={isCreatable ? onNewRecord : null}
                        onRefresh={onRefresh} isMobile={isMobile}/>
                {isMobile ? (
                    <DataTableBodyMobile tableConfig={tableConfig} data={data} onEditRecord={isEditable ? onEditRecord : null}
                        onDeleteRecord={isDeletable ? onDeleteRecord : null}/>
                ) : (
                    <TableContainer>
                        <Table size={dense ? 'small' : 'medium'}>
                            <DataTableHead tableConfig={tableConfig} onSort={onSort} orderBy={orderBy} />
                            <DataTableBody tableConfig={tableConfig} data={data} onEditRecord={isEditable ? onEditRecord : null}
                                    onDeleteRecord={isDeletable ? onDeleteRecord : null}/>
                        </Table>
                    </TableContainer>
                )}
                {tableConfig.disablePaging ? null : pagination()}
            </Paper>
            {isMobile ? null : (
                <FormControlLabel control={<Switch checked={dense} onChange={handleChangeDense} />} label={t('component.datatable.dense_padding')} />
            )}
            {tableConfig.isFormDialog ? (
                <FormDialog title={formTitle} open={formOpen} handleClose={() => setFormOpen(false)}>
                    <Form formConfig={actualFormConfig} onSubmitAction={onFormSubmitAction} record={record} disabled={formDisabled}/>
                </FormDialog>
            ) : null}
            <ConfirmDialog open={confirmDialogOpen} handleClose={() => setConfirmDialogOpen(false)} title={t('component.datatable.delete')}
                contentText={t('component.datatable.confirm_delete_message')} acceptBtnLabel={t('component.datatable.confirm')}
                declineBtnLabel={t('component.datatable.cancel')} declineBtnOnClick={() => setConfirmDialogOpen(false)}
                acceptBtnOnClick={doDeleteRecord}/>
        </div>
    );
}

export default DataTable;
