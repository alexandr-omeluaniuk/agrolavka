/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';

import { makeStyles, useTheme } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

const useStyles = makeStyles(theme => ({
    editButton: {
        color: theme.palette.secondary.main
    },
    deleteButton: {
        color: theme.palette.error.main
    },
    rowContent: {
        paddingTop: 0
    },
    valueColumn: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    fieldTitle: {
        marginTop: theme.spacing(1),
        color: '#a5a5a5'
    },
    cardHeader: {
        paddingBottom: 0,
        paddingLeft: theme.spacing(2),
        paddingRight: theme.spacing(2),
        paddingTop: theme.spacing(1),
    },
    col: {
        display: 'flex',
        flexDirection: 'column'
    },
    fullWidth: {
        width: '100%'
    }
}));

function DataTableCard (props) {
    const { tableConfig, onEditRecord, onDeleteRecord, rowData, data, idx } = props;
    const classes = useStyles();
    const theme = useTheme();
    const { t } = useTranslation();
    const [anchorEl, setAnchorEl] = React.useState(null);
    // ====================================================== RENDERING ===================================================================
    const actions = [];
    if (onEditRecord) {
        actions.push((
            <Tooltip title={t('common.edit')} key={1}>
                <IconButton aria-label="edit record" className={classes.editButton} onClick={() => onEditRecord(rowData)}>
                    <Icon>edit</Icon>
                </IconButton>
            </Tooltip>
        ));
    }
    let isDeleteVisible = true;
    if (tableConfig.ajax && tableConfig.ajax.delete && tableConfig.ajax.delete.isVisible) {
        isDeleteVisible = tableConfig.ajax.delete.isVisible(rowData);
    }
    if (onDeleteRecord && isDeleteVisible) {
        actions.push((
            <Tooltip title={t('common.delete')} key={2}>
                <IconButton aria-label="delete record" className={classes.deleteButton}
                        onClick={() => onDeleteRecord(rowData)}>
                    <Icon>delete</Icon>
                </IconButton>
            </Tooltip>
        ));
    }
    let firstColumn = tableConfig.columns.filter(c => {
        return c.type !== 'action' && c.label;
    })[0];
    const title = (
        <React.Fragment>
            <Typography variant="subtitle2" className={classes.fieldTitle}>
                {t(firstColumn.label)}
            </Typography>
            <Typography variant="subtitle2">
                <span>
                    {firstColumn.renderer ? firstColumn.renderer(rowData, data) : rowData[firstColumn.name]}
                </span>
            </Typography>
        </React.Fragment>
    );
    const additionalActions = tableConfig.columns.filter(c => {
        return c.type === 'action';
    }).map((col, idx) => {
        return <React.Fragment key={idx}>
                    {col.renderer(rowData, data)}
                </React.Fragment>;
    });
    
    const actionButton = tableConfig.columns.filter(c => {
        return c.type === 'action-button';
    }).map((column, idx) => {
        let innerContent;
        if (column.renderer) {
            innerContent = column.renderer(rowData, data);
        } else {
            innerContent = rowData[column.name];
        }
        return (
            <div key={idx} className={classes.fullWidth}>
                {innerContent}
            </div>
        )
    });
    
    if (additionalActions.length > 3) {
        actions.push((
            <IconButton key={3} onClick={(event) => setAnchorEl(event.currentTarget)}>
                <Icon>more_vert</Icon>
            </IconButton>
        ));
    } else if (additionalActions.length <= 3) {
        let startKey = 3;
        additionalActions.forEach(a => {
            actions.push((
                <React.Fragment key={startKey++}>
                    {a}
                </React.Fragment>
            ));
        });
    }
    let cardStyle = {};
    if (idx !== 0) {
        cardStyle.marginTop = theme.spacing(1);
    }
    return (
        <Card style={cardStyle}>
            <CardHeader className={classes.cardHeader} title={title} action={actions}/>
            <CardContent className={classes.rowContent}>
                <Grid container>
                    {tableConfig.columns.filter(c => {
                        return c.label && c.name;
                    }).map((column, idx2) => {
                        if (column.name === firstColumn.name) {
                            return null;
                        }
                        let innerContent;
                        if (column.renderer) {
                            innerContent = column.renderer(rowData, data);
                        } else {
                            innerContent = rowData[column.name];
                        }
                        let columnUI = <div>{innerContent}</div>;
                        return (
                            <Grid item key={idx2} xs={column.mobileGrid ? column.mobileGrid : 12}>
                                <Grid item xs={12}>
                                    <Typography className={classes.fieldTitle} variant="subtitle2">{t(column.label)}</Typography>
                                </Grid>
                                <Grid item xs={12} className={classes.valueColumn}>
                                    {columnUI}
                                </Grid>
                            </Grid>
                        )
                    })}
                </Grid>
                {actionButton.length > 0 ? (
                    <CardActions>
                        {actionButton}
                    </CardActions>
                ) : null}
            </CardContent>
            
            {additionalActions.length > 3 ? (
                <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={() => setAnchorEl(null)} getContentAnchorEl={null}
                    anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'center'
                    }}
                    transformOrigin={{
                        vertical: 'top',
                        horizontal: 'center'
                    }}>
                    {additionalActions.map((item, k) => {
                        return <MenuItem key={k}>
                                    {item}
                                </MenuItem>;
                    })}
                </Menu>
            ) : null}
        </Card>
    );
}

export default DataTableCard;