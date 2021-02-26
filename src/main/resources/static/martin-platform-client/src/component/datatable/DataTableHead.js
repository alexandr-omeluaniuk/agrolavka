/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import { SORT_ASC } from '../../util/model/TableConfig';

const useStyles = makeStyles(theme => ({
    visuallyHidden: {
        border: 0,
        clip: 'rect(0 0 0 0)',
        height: 1,
        margin: -1,
        overflow: 'hidden',
        padding: 0,
        position: 'absolute',
        top: 20,
        width: 1
    },
    actionCell: {
        width: `${theme.spacing(1) + 48}px`
    }
}));

function DataTableHead(props) {
    const { t } = useTranslation();
    const classes = useStyles();
    const { tableConfig, orderBy, onSort } = props;
    let actionColumns = 2;
    let actionColumnsUI = [];
    for (let i = 0; i < actionColumns; i++) {
        actionColumnsUI.push(<TableCell className={classes.actionCell} key={i}></TableCell>);
    }
    return (
            <TableHead>
                <TableRow>
                    {tableConfig.columns.map((column, idx) => {
                        let colStyle = {};
                        if (column.width) {
                            colStyle['width'] = column.width;
                        }
                        let align = column.align ? column.align : 'left';
                        const order = column._sort_direction ? column._sort_direction : SORT_ASC;
                        return (
                                <TableCell key={idx} style={colStyle} align={align}>
                                    {column.sortable === true ? (
                                        <TableSortLabel active={orderBy === column.name} direction={order} onClick={() => onSort(column)}>
                                            {t(column.label)}
                                            {orderBy === column.name ? (
                                                <span className={classes.visuallyHidden}>
                                                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                                                </span>
                                            ) : null}
                                        </TableSortLabel>
                                    ) : t(column.label)}
                                </TableCell>
                        );
                    })}
                    {actionColumnsUI}
                </TableRow>
            </TableHead>
    );
}

export default DataTableHead;