/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { useContext } from 'react';
import { OrdersForPrintContext } from './OrdersForPrintContext';

const useOrdersForPrint = () => useContext(OrdersForPrintContext);

export default useOrdersForPrint;