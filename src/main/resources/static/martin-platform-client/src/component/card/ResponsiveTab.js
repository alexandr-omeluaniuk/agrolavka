/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Slide from '@material-ui/core/Slide';
import Paper from '@material-ui/core/Paper';
import InlineTabHeader from '../util/InlineTabHeader';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Dropdown from '../form/input/Dropdown';

const useStyles = makeStyles(theme => ({
    avatar: {
        backgroundColor: theme.palette.secondary.main
    },
    tabs: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(2)
    },
    divider: {
        marginBottom: theme.spacing(2)
    },
    dropdown: {
        marginBottom: theme.spacing(1)
    },
    paper: {
        width: '100%',
        display: 'flex',
        flexDirection: 'column'
    },
    content: {
        overflowY: 'auto'
    },
    paperSpacing: {
        padding: theme.spacing(1)
    }
}));

function ResponsiveTab (props) {
    const { tabNames, tabs, scrollable } = props;
    const classes = useStyles();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));
    const [activeTab, setActiveTab] = React.useState(0);
    const handleChange = (event, newValue) => {
        setActiveTab(newValue);
    };
    const tabContent = () => {
        return (
            <div className={scrollable ? classes.content : null}>                
                {tabs.map((tabItem, idx) => {
                    return (
                        <Slide direction={'left'} in={activeTab === idx} mountOnEnter unmountOnExit key={idx}
                            timeout={{ enter: theme.transitions.duration.enteringScreen, exit: 0}}>
                            <Paper elevation={0} className={classes.paper}>
                                {tabItem}
                            </Paper>
                        </Slide>
                    ); 
                })}
            </div>
        );
    };
    return (
            <Paper elevation={1} className={scrollable ? classes.paper : classes.paperSpacing}>
                {isMobile ? (
                    <Dropdown value={activeTab} fullWidth={true} options={tabNames} variant="outlined" onChange={(e) => {
                        setActiveTab(e.target.value);
                    }} className={classes.dropdown}/>
                ) : (
                    <Tabs value={activeTab} onChange={handleChange} className={classes.tabs}>
                        {tabNames.map((item, key) => {
                            return (
                                <Tab key={key} label={(<InlineTabHeader icon={item.icon} title={item.label}/>)} />
                            );
                        })}    
                    </Tabs>
                )}
                {tabContent()}
            </Paper>
    );
}

export default ResponsiveTab;