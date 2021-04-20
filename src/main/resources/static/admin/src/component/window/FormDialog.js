import React from 'react';
import PropTypes from 'prop-types';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    root: {
        margin: 0,
        padding: theme.spacing(2)
    },
    closeButton: {
        position: 'absolute',
        right: theme.spacing(1),
        top: theme.spacing(1),
        color: theme.palette.grey[500]
    },
    noScroll: {
        overflow: 'hidden',
        display: 'flex'
    }
}));

function FormDialog(props) {
    const { title, open, handleClose, children, maxWidth, noScroll } = props;
    const classes = useStyles();
    return (
            <Dialog open={open} onClose={handleClose} scroll={'paper'} maxWidth={maxWidth ? maxWidth : 'md'} fullWidth={true}>
                <MuiDialogTitle disableTypography className={classes.root}>
                    <Typography variant="h6">{title}</Typography>
                    <IconButton aria-label="close" className={classes.closeButton} onClick={handleClose}>
                        <Icon>close</Icon>
                    </IconButton>
                </MuiDialogTitle>
                <DialogContent dividers={true} classes={{root: noScroll ? classes.noScroll: null}}>
                    {children}
                </DialogContent>
            </Dialog>
    );
}

FormDialog.propTypes = {
    title: PropTypes.string.isRequired,
    open: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired
};

export default FormDialog;