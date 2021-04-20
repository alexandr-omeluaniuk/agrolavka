/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Icon from '@material-ui/core/Icon';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import { history } from '../../../index';
import AppURLs from '../../../conf/app-urls';
import DataService from '../../../service/DataService';
import { modules } from '../../../conf/modules';

const dataService = new DataService();

const useStyles = makeStyles(theme => ({
    media: {
        height: `260px`
    },
    paper: {
        padding: theme.spacing(3)
    },
    content: {
        display: 'flex',
        position: 'absolute',
        bottom: 0,
        left: 0,
        right: 0,
        backgroundColor: `#ffffffd1`
    },
    title: {
        flex: 1
    },
    icon: {
        fontSize: '2rem'
    }
}));

function Applications(props) {
    const classes = useStyles();
    const { t } = useTranslation();
    const [applications, setApplications] = React.useState(null);
    // ============================================================ METHODS ===============================================================
    const openApplication = (app) => {
        history.push(AppURLs.app + app.path);
    };
    // ============================================================ HOOKS =================================================================
    useEffect(() => {
        if (!applications) {
            setApplications(modules().filter(m => m.isVisible()));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [applications]);
    useEffect(() => {
        return () => {
            dataService.abort();
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    // ============================================================ RENDERING =============================================================
    if (!applications) {
        return null;
    }
    return (
            <Container>
                <Grid container spacing={2}>
                    {applications.map((app, idx) => {
                        return (
                            <Grid item xs={12} md={4} lg={4} key={idx}>
                                <Card raised={true} onClick={() => openApplication(app)}>
                                    <CardActionArea>
                                        <CardMedia className={classes.media} image={app.getBackgroundImage()}
                                            title={t(`m_${app.id}:title`)}/>
                                        <CardContent className={classes.content}>
                                            <Typography gutterBottom variant="h5" component="h2" className={classes.title}>
                                                {t(`m_${app.id}:title`)}
                                            </Typography>
                                            {app.getIcon() ? <Icon className={classes.icon}>{app.getIcon()}</Icon> : null}
                                        </CardContent>
                                    </CardActionArea>
                                </Card>
                            </Grid>
                        );
                    })}
                </Grid>
            </Container>
    );
}

export default Applications;

