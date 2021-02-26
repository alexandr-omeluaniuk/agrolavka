import React, { Component } from 'react';
import { Card, CardHeader, CardContent, Avatar, IconButton, Icon, Typography } from '@material-ui/core';
import AppURLs from '../../conf/app-urls';
import { withStyles } from '@material-ui/core';

const styles = (theme) => ({
    card: {
        backgroundColor: theme.palette.error.main
    },
    text: {
        color: 'white'
    },
    avatar: {
        backgroundColor: theme.palette.error.dark
    },
    closeButtonIcon: {
        color: 'white'
    }
});

class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            stackTrace: null
        };
    }
    componentDidCatch(error) {
        this.setState({ hasError: true, stackTrace: error.stack });
    }
    closeError() {
        window.location.href = AppURLs.app;
    }
    render() {
        const { classes } = this.props;
        if (this.state.hasError) {
            return (
                <Card className={classes.card}>
                    <CardHeader
                        avatar={(<Avatar className={classes.avatar}><Icon>error</Icon></Avatar>)}
                        action={(
                            <IconButton onClick={this.closeError}>
                                <Icon className={classes.closeButtonIcon}>close</Icon>
                            </IconButton>
                        )}
                        title={(<Typography variant="subtitle1" className={classes.text}>
                                <b>Error.</b>
                                </Typography>)}/>
                    <CardContent style={{paddingTop: 0, whiteSpace: "pre"}}>
                        <Typography variant="body2"component="p" className={classes.text}>
                            {this.state.stackTrace}
                        </Typography>
                    </CardContent>
                </Card>
            );
        }
        return this.props.children;
    }
}

export default withStyles(styles)(ErrorBoundary);