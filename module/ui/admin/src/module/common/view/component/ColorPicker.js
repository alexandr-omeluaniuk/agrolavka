/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from "react";
import { makeStyles } from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Icon from '@material-ui/core/Icon';
import Tooltip from '@material-ui/core/Tooltip';
import { COLORS } from '../../../../conf/theme';
import Slider from '@material-ui/core/Slider';

const useStyles = makeStyles(theme => ({
    pickerContainer: {
        margin: theme.spacing(2),
        overflow: 'hidden'
    },
    item: {
        borderRadius: 0,
        height: '48px',
        width: '48px'
    },
    slider: {
        paddingTop: theme.spacing(2),
        paddingBottom: theme.spacing(2)
    }
}));

function ColorPicker(props) {
    const { title, open, onClose, color, contrast, contrastChanged, colorChanged } = props;
    const classes = useStyles();
    return (
        <Dialog aria-labelledby="simple-dialog-title" open={open} onClose={onClose}>
            <div className={classes.pickerContainer}>
                <Typography component="h5" align="center">{title}</Typography>
                <Slider marks value={contrast} min={100} max={900} step={100} onChange={contrastChanged} className={classes.slider}/>
                <div>
                    {COLORS.map((row, index) => (
                        <div key={index}>
                            {row.map((c, index2) => {
                                const clr = c.color[contrast];
                                return (<Tooltip title={c.label} key={index2}>
                                    <IconButton className={classes.item} style={{backgroundColor: clr}} onClick={() => colorChanged(c.label)}>
                                        { color === c.label ? (
                                            <Icon>checked</Icon>
                                        ) : null}
                                    </IconButton>
                                </Tooltip>);
                            })}
                        </div>
                    ))}
                </div>
            </div>
        </Dialog>
    );
};

export default ColorPicker;
