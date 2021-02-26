/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Collapse from '@material-ui/core/Collapse';
import Divider from '@material-ui/core/Divider';
import { SHOW_FILTER } from '../../conf/local-storage-keys';

const useStyles = makeStyles(theme => ({
    root: {
        paddingLeft: theme.spacing(2),
        paddingRight: theme.spacing(1)
    },
    title: {
        flex: '1 1 100%',
        paddingTop: theme.spacing(1)
    },
    greenButton: {
        color: theme.palette.success.main
    },
    sortableIcon: {
        color: theme.palette.secondary.light,
        position: 'absolute',
        top: `-${theme.spacing(1)}px`,
        left: `-${theme.spacing(1)}px`
    },
    filter: {
        padding: theme.spacing(2),
        paddingTop: '0px !important'
    }
}));

function DataTableToolbar(props) {
    const classes = useStyles();
    const { t } = useTranslation();
    const { tableConfig, onNewRecord, onRefresh, isMobile } = props;
    let toolbarActionsBefore = tableConfig.toolbarActionsBefore ? tableConfig.toolbarActionsBefore : null;
    let toolbarActionsAfter = tableConfig.toolbarActionsAfter ? tableConfig.toolbarActionsAfter : null;
    let toolbarFilter = tableConfig.toolbarFilter ? tableConfig.toolbarFilter : null;
    const [showFilters, setShowFilters] = React.useState(
            localStorage.getItem(SHOW_FILTER) && localStorage.getItem(SHOW_FILTER) !== 'false' ? true : false);
    const handleShowFilters = () => {
        localStorage.setItem(SHOW_FILTER, !showFilters);
        setShowFilters(!showFilters);
    };
    return (
        <Paper elevation={isMobile ? 1 : 0}>
            <Toolbar className={classes.root}>            
                {tableConfig.onSortEnd ? (
                    <Tooltip title={t('component.sortableTable')}>
                        <Icon className={classes.sortableIcon}>sort</Icon>
                    </Tooltip>) : null
                }
                <Grid container>
                    <Grid item container>
                        <Grid item xs={6}>
                            {tableConfig.title ? (
                                <Typography className={classes.title} variant="h6" component="div">
                                    {t(tableConfig.title)}
                                </Typography>
                            ) : null}
                        </Grid>               
                        <Grid item xs={6} container justify="flex-end">
                            {toolbarActionsBefore}
                            {onNewRecord ? (
                                <Tooltip title={t('component.datatable.new')}>
                                    <IconButton aria-label="new record" onClick={() => onNewRecord()}>
                                        <Icon className={classes.greenButton}>add</Icon>
                                    </IconButton>
                                </Tooltip>
                            ) : null}
                            {toolbarFilter ? (
                                <Tooltip title={t('queueManagement.view.queue.tableFilters')}>
                                    <IconButton onClick={handleShowFilters}>
                                        <Icon color="primary">filter_list</Icon>
                                    </IconButton>
                                </Tooltip>) : null 
                            }
                            <Tooltip title={t('component.datatable.refresh')}>
                                <IconButton aria-label="refresh" onClick={onRefresh}>
                                    <Icon color="secondary">loop</Icon>
                                </IconButton>
                            </Tooltip>
                            {toolbarActionsAfter}
                        </Grid>
                    </Grid>
                </Grid>  
            </Toolbar>
            {toolbarFilter ? (
                <Collapse in={showFilters} timeout="auto" unmountOnExit>
                    <Paper elevation={0} className={classes.filter}>
                        {toolbarFilter}
                    </Paper>
                    <Divider />
                </Collapse>
            ) : null}
            
        </Paper>
    );
}

export default DataTableToolbar;

