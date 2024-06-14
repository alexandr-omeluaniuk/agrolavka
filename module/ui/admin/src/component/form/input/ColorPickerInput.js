/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from "react";
import { makeStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Icon from '@material-ui/core/Icon';
import Tooltip from '@material-ui/core/Tooltip';
import Slider from '@material-ui/core/Slider';
import { COLORS } from "../../../conf/theme";

const useStyles = makeStyles(theme => ({
    pickerContainer: {
        overflow: 'hidden',
        width: `${48 * 4 + theme.spacing(6)}px`,
        padding: theme.spacing(3)
    },
    item: {
        borderRadius: 0,
        height: '48px',
        width: '48px'
    },
    slider: {
    }
}));

function ColorPickerInput(props) {
    const { name, fieldValue, onChangeFieldValue, label } = props;
    const classes = useStyles();

    const contrastChanged = (event, contrast) => {
        onChangeFieldValue(name, {
            color: fieldValue.color,
            contrast: contrast
        });
    };
    const colorChanged = (c) => {
        onChangeFieldValue(name, {
            color: c,
            contrast: fieldValue.contrast
        });
    };

    return (
        <div className={classes.pickerContainer}>
            <Typography component="h5">{label}</Typography>
            <Slider marks value={fieldValue.contrast} min={100} max={900} step={100} onChange={contrastChanged} className={classes.slider} />
            <div>
                {COLORS.map((row, index) => (
                    <div key={index}>
                        {row.map((c, index2) => {
                            const clr = c.color[fieldValue.contrast];
                            return (<Tooltip title={c.label} key={index2}>
                                <IconButton className={classes.item} style={{ backgroundColor: clr }} onClick={() => colorChanged(c.label)}>
                                    {fieldValue.color === c.label ? (
                                        <Icon>checked</Icon>
                                    ) : null}
                                </IconButton>
                            </Tooltip>);
                        })}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ColorPickerInput;
