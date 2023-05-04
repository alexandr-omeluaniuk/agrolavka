/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { useContext } from 'react';
import { NotificationContext } from '../context/NotificationContext';

const useNotification = () => useContext(NotificationContext);

export default useNotification;

