/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { createContext, useEffect, useCallback } from 'react';
import Notification from '../component/util/Notification';
import DataService from '../service/DataService';

export const NotificationContext = createContext({
    showNotification: () => {
        console.log('Notifications have not init yet...');
    }
});

export const NotificationProvider = ({ children }) => {
    const [openNotification, setOpenNotification] = React.useState(false);
    const [notificationMessage, setNotificationMessage] = React.useState(null);
    const [notificationDetails, setNotificationDetails] = React.useState(null);
    const [notificationType, setNotificationType] = React.useState('info');
    const [notificationDuration, setNotificationDuration] = React.useState(6000);
    
    const showNotification = useCallback((msg, details, type, duration) => {
        setNotificationMessage(msg);
        setNotificationDetails(details);
        setNotificationType(type ? type : 'info');
        setOpenNotification(true);
        setNotificationDuration(duration ? duration : 6000);
    }, []);
    
    useEffect(() => {
        if (!DataService.showNotification) {
            DataService.showNotification = showNotification;
        }
    }, [showNotification]);
    
    return (
            <NotificationContext.Provider value={{showNotification: showNotification}}>
                {children}
                <Notification open={openNotification} setOpen={setOpenNotification} message={notificationMessage} 
                                    details={notificationDetails} severity={notificationType} duration={notificationDuration}/>
            </NotificationContext.Provider>
    );
};
