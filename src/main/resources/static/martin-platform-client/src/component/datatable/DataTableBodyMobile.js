/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Spinner from '../util/Spinner';
import DataTableCard from './DataTableCard';

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowY: 'auto',
        height: 'calc(100vh - 210px)',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1),
        padding: '2px'
    }
}));

function DataTableBodyMobile (props) {
    const classes = useStyles();
    const { tableConfig, data } = props;
    if (data === null) {
        return (
                <div style={{position: 'relative', height: '150px'}}>
                    <Spinner open={true} />
                </div>
        );
    }

    return (
            <div className={classes.pageContent}>
            {data.map((rowData, idx) => (
                <DataTableCard tableConfig={tableConfig} rowData={rowData} idx={idx} key={idx}/>
            ))}
            </div>
    );
}

export default DataTableBodyMobile;