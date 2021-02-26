import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Input from '@material-ui/core/Input';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import ListItemText from '@material-ui/core/ListItemText';
import Select from '@material-ui/core/Select';
import Checkbox from '@material-ui/core/Checkbox';
import InputLabel from '@material-ui/core/InputLabel';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';
import FormHelperText from '@material-ui/core/FormHelperText';

const useStyles = makeStyles(theme => ({
    header: {
        display: 'flex',
        alignItems: 'center'
    },
    title: {
        flex: 1,
        marginLeft: theme.spacing(2)
    },
    button: {
        marginRight: theme.spacing(1)
    }
}));

function MultipleSelect(props) {
    const { label, name, options, onChangeFieldValue, value, fullWidth, helperText, ...other } = props;
    const componentValue = value.length === 0 ? [] : value.split(',');
    const classes = useStyles();
    const { t } = useTranslation();
    const [open, setOpen] = React.useState(false);
    return (
            <FormControl fullWidth={fullWidth} error={helperText ? true : false} {...other}>
                <InputLabel>{label}</InputLabel>
                <Select multiple value={componentValue} input={<Input />} name={name} open={open} onOpen={() => {
                    setOpen(true);
                }} onClose={() => {
                    setOpen(false);
                }} renderValue={(selected) => {
                    let sb = [];
                    options.filter(o => {
                        return selected.indexOf(o.value) !== -1;
                    }).forEach(o => {
                        sb.push(o.label);
                    });
                    return sb.join(', ');
                }}
                onChange={(e) => {
                    onChangeFieldValue(name, e.target.value.join(','));
                }}>
                    <div className={classes.header}>
                        <Typography variant="h6" className={classes.title}>{label}</Typography>
                        <Tooltip title={t('common.close')}>
                            <IconButton onClick={() => { setOpen(false); }} className={classes.button}>
                                <Icon>close</Icon>
                            </IconButton>
                        </Tooltip>
                    </div>
                    {options.map((item, idx) => (
                        <MenuItem key={idx} value={item.value}>
                            <Checkbox checked={componentValue.indexOf(item.value) > -1} />
                            <ListItemText primary={item.label} />
                        </MenuItem>
                    ))}
                </Select>
                {helperText ? (
                    <FormHelperText>{helperText}</FormHelperText>
                ) : null}
            </FormControl>
    );
}

export default MultipleSelect;