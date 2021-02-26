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

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    paperMobile: {
        backgroundColor: '#fff0'
    }
}));

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
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(
            localStorage.getItem(ITEMS_PER_PAGE) ? parseInt(localStorage.getItem(ITEMS_PER_PAGE)) : 5);
    // ============================================================ HOOKS =================================================================
    useEffect(() => {
        let params = `?page=${page + 1}&page_size=${rowsPerPage}`;
        if (order && orderBy) {
            params += `&order=${order}&order_by=${orderBy}`;
        }
        dataService.get(`${tableConfig.apiUrl}${params}`).then(resp => {
            if (resp) {
                setData(resp.data);
                setTotal(resp.total);
            }
        });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [load]);
//    useEffect(() => {
//        return () => {
//            dataService.abort();
//        };
//        // eslint-disable-next-line react-hooks/exhaustive-deps
//    }, []);
    // ============================================================ METHODS ===============================================================
    const onSort = (column) => {
        console.log(column);
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
        setRecord(rowData);
        setFormTitle(t('component.datatable.edit'));
        setFormOpen(true);
    };
    const onDeleteRecord = (rowData) => {
        setRecord(rowData);
        setConfirmDialogOpen(true);
    };
    const doDeleteRecord = () => {
        let id = DataTypeService.getIdValue(tableConfig.formConfig, record);
        dataService.delete(`${tableConfig.apiUrl}/${id}`).then(data => {
            setLoad(!load);
        });
    };
    const onFormSubmitAction = (data) => {
        let id = DataTypeService.getIdValue(tableConfig.formConfig, data);
        if (id) {
            dataService.put(tableConfig.apiUrl, data).then(() => {
                setFormOpen(false);
                setLoad(!load);
            });
        } else {
            dataService.post(tableConfig.apiUrl, data).then(() => {
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
    };
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        localStorage.setItem(ITEMS_PER_PAGE, parseInt(event.target.value, 10));
        setPage(0);
    };
    // bind component functions for using in parent components
    tableConfig.api = {
        onRefresh: onRefresh
    };
    // ============================================================ RENDERING =============================================================
    const pagination = () => {
        let c = function () {
            return <TablePagination rowsPerPageOptions={[5, 10, 25]} component="div" count={total}
                    rowsPerPage={rowsPerPage} page={page}
                    onChangePage={handleChangePage} onChangeRowsPerPage={handleChangeRowsPerPage}/>;
        };
        return <Paper elevation={1}>{c()}</Paper>;
    };
    let actualFormConfig = tableConfig.formConfig;
    if (record && tableConfig.formConfigEdit) {
        actualFormConfig = tableConfig.formConfigEdit;
    }
    return (
        <div className={classes.root}>
            <Paper className={isMobile ? classes.paperMobile : null} elevation={isMobile ? 0 : 3}>
                <DataTableToolbar tableConfig={tableConfig} onNewRecord={onNewRecord}
                        onRefresh={onRefresh} isMobile={isMobile}/>
                {isMobile ? (
                    <DataTableBodyMobile tableConfig={tableConfig} data={data} onEditRecord={onEditRecord} onDeleteRecord={onDeleteRecord}/>
                ) : (
                    <TableContainer>
                        <Table size={dense ? 'small' : 'medium'}>
                            <DataTableHead tableConfig={tableConfig} onSort={onSort} orderBy={orderBy} />
                            <DataTableBody tableConfig={tableConfig} data={data} onEditRecord={onEditRecord} onDeleteRecord={onDeleteRecord}/>
                        </Table>
                    </TableContainer>
                )}
                {pagination()}
            </Paper>
            {isMobile ? null : (
                <FormControlLabel control={<Switch checked={dense} onChange={handleChangeDense} />} label={t('component.datatable.dense_padding')} />
            )}
            <FormDialog title={formTitle} open={formOpen} handleClose={() => setFormOpen(false)}>
                <Form formConfig={actualFormConfig} onSubmitAction={onFormSubmitAction} record={record}/>
            </FormDialog>
            <ConfirmDialog open={confirmDialogOpen} handleClose={() => setConfirmDialogOpen(false)} title={t('component.datatable.delete')}
                contentText={t('component.datatable.confirm_delete_message')} acceptBtnLabel={t('component.datatable.confirm')}
                declineBtnLabel={t('component.datatable.cancel')} declineBtnOnClick={() => setConfirmDialogOpen(false)}
                acceptBtnOnClick={doDeleteRecord}/>
        </div>
    );
}

export default DataTable;
