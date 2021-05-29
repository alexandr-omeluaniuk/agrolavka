/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Popover from '@material-ui/core/Popover';

const useStyles = makeStyles(theme => ({
    palette: {
        display: 'flex',
        flexDirection: 'column',
        padding: theme.spacing(1)
    },
    row: {
        display: 'flex'
    },
    item: {
        flex: 1,
        width: theme.spacing(4),
        height: theme.spacing(4),
        margin: theme.spacing(1),
        borderRadius: theme.spacing(1),
        '&:hover': {
            cursor: 'pointer',
            boxShadow: theme.shadows[2]
        }
    }
}));

const COLORS = [
    '#0d6efd', '#6610f2', '#6f42c1', '#d63384',
    '#dc3545', '#fd7e14', '#ffc107', '#198754',
    '#20c997', '#0dcaf0', '#fff', '#6c757d',
    '#343a40', '#0d6efd', '#198754', '#0dcaf0',
    '#ffc107', '#dc3545', '#f8f9fa', '#212529'
];

function Colors(props) {
    const { getSelection, applyColor } = props;
    const classes = useStyles();
    const { t } = useTranslation();
    const [anchorEl, setAnchorEl] = React.useState(null);
    
    const onChangeColor = (color) => {
        handleClose();
        applyColor(color);
    };
    
    const handleClick = (event) => {
        getSelection();
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };
    
    const palette = () => {
        const grid = [];
        for (let i = 0; i < COLORS.length; i = i + 4) {
            let currentRow = [];
            for (let j = i; j < i + 4; j++) {
                currentRow.push(
                        <div className={classes.item} key={j} onClick={() => onChangeColor(COLORS[j])}
                            style={{backgroundColor: COLORS[j] ? COLORS[j] : null}}></div>);
            }
            grid.push(<div className={classes.row} key={i}>{currentRow}</div>);
        }
        return grid;
    };
    
    return (
            <React.Fragment>
                <Tooltip title={t('component.htmleditor.toolbar.colors')}>
                    <IconButton onClick={handleClick}>
                        <Icon>format_color_text</Icon>
                    </IconButton>
                </Tooltip>
                <Popover open={Boolean(anchorEl)} anchorEl={anchorEl} onClose={handleClose} anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left'
                }} transformOrigin={{
                    vertical: 'top',
                    horizontal: 'left'
                }}>
                    <div className={classes.palette}>
                        {palette()}
                    </div>
                </Popover>
            </React.Fragment>
    );
}

export default Colors;