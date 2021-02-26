/* 
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import React, { useEffect } from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Copyright from '../component/Copyright';
import { useTranslation } from 'react-i18next';
import Form from '../component/form/Form';
import { TYPES, VALIDATORS } from '../service/DataTypeService'
import DataService from '../service/DataService';
import { history } from '../index';
import AppURLs from '../conf/app-urls';
import { FormField, FormConfig, Validator, FormSubmit } from '../util/model/TableConfig';
import background from '../assets/images/login-background.jpg';

let dataService = new DataService();

const useStyles = makeStyles(theme => ({
    background: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundImage: `url(${background})`
    }
}));

export default function Welcome() {
    const classes = useStyles();
    const { t } = useTranslation();
    useEffect(() => {
        return () => {
            dataService.abort();
        };
    }, []);
    const doLogin = (data) => {
        dataService.login(data).then(() => {
            console.log(AppURLs.app);
            history.push(AppURLs.app);
        });
    };
    let formConfig = new FormConfig([
        new FormField('username', TYPES.TEXTFIELD, t('component.welcome.sign_in'))
                .setGrid({xs: 12}).validation([new Validator(VALIDATORS.REQUIRED)]),
        new FormField('password', TYPES.PASSWORD, t('component.welcome.password'))
                .setGrid({xs: 12}).validation([new Validator(VALIDATORS.REQUIRED)])
    ]).setSubmit(new FormSubmit().setLabel(t('component.welcome.sign_in')).setIcon('login').setColor('primary'))
            .setVariant('outlined').setSpacing(2);
    return (
            <div className={classes.background}>
                <Container component="main" maxWidth="xs">
                    <CssBaseline />
                    <Card raised>
                        <CardHeader title={(<Typography variant="h5" align="center">{t('component.welcome.title')}</Typography>)}/>
                        <CardContent>
                            <Form onSubmitAction={doLogin} formConfig={formConfig}/>
                        </CardContent>
                    </Card>
                    <Box mt={8}>
                        <Copyright />
                    </Box>
                </Container>
            </div>
    );
}
