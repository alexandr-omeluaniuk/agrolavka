/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow'
import Spinner from '../util/Spinner';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';

const useStyles = makeStyles(theme => ({
    actionCell: {
        padding: 0,
        margin: 0
    },
    editButton: {
        color: theme.palette.secondary.main
    },
    deleteButton: {
        color: theme.palette.error.main
    }
}));

function DataTableBody (props) {
    const { t } = useTranslation();
    const classes = useStyles();
    const { tableConfig, data, onEditRecord, onDeleteRecord } = props;
    let actionColumns = 2;
    if (data === null) {
        return (
                <TableBody>
                    <TableRow>
                        <TableCell colSpan={tableConfig.columns.length + actionColumns} style={{position: 'relative', height: '150px'}}>
                            <Spinner open={true} />
                        </TableCell>
                    </TableRow>
                </TableBody>
        );
    }
    
    if (data.length === 0) {
        return (
                <TableBody>
                    <TableRow>
                        <TableCell colSpan={tableConfig.columns.length + actionColumns} align={'center'}>
                            {t('component.datatable.no_data')}
                        </TableCell>
                    </TableRow>
                </TableBody>
        );
    }
    
    const tableRow = (rowData) => {
        return (
            <TableRow>
                {tableConfig.columns.map((column, idx2) => {
                    let columnUI;
                    let innerContent;
                    if (column.renderer) {
                        innerContent = column.renderer(rowData, data);
                    } else {
                        innerContent = rowData[column.name];
                    }
                    let className = null;
                    if (column.type === 'action') {
                        className = classes.actionCell;
                    }
                    let align = column.align ? column.align : 'left';
                    let cellStyle = {};
                    if (column.style) {
                        for (let k in column.style) {
                            cellStyle[k] = column.style[k];
                        }
                    }
                    if (idx2 === 0) {
                        columnUI = (
                            <TableCell align={align} component="th" scope="row" key={idx2} className={className} style={cellStyle}>
                                {innerContent}
                            </TableCell>
                        );
                    } else {
                        columnUI = <TableCell align={align} className={className} key={idx2} style={cellStyle}>{innerContent}</TableCell>;
                    }
                    return columnUI;
                })}
                {onEditRecord ? (
                    <TableCell className={classes.actionCell}>
                        <Tooltip title={t('component.datatable.edit')}>
                            <IconButton aria-label="edit record" className={classes.editButton} onClick={() => onEditRecord(rowData)}>
                                <Icon>edit</Icon>
                            </IconButton>
                        </Tooltip>
                    </TableCell>
                ) : null}
                {onDeleteRecord ? (
                    <TableCell className={classes.actionCell}>
                        <Tooltip title={t('component.datatable.delete')}>
                            <IconButton aria-label="delete record" className={classes.deleteButton}
                                    onClick={() => onDeleteRecord(rowData)}>
                                <Icon>delete</Icon>
                            </IconButton>
                        </Tooltip>
                    </TableCell>
                ) : null}
            </TableRow>
        );
    };
    
    return (
        <TableBody>
            {data.map((rowData, idx) => (
                <React.Fragment key={idx}>
                    {tableRow(rowData)}
                </React.Fragment>
            ))}
        </TableBody>
    );
}

export default DataTableBody;