/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React, { createContext } from 'react';

export const OrdersForPrintContext = createContext({
    addOrder: () => {
        console.log('Not init yet...');
    },
    removeOrder: () => {
        console.log('Not init yet...');
    },
    getOrders: () => {
        console.log('Not init yet...');
    }
});

export const OrdersForPrintProvider = ({ children }) => {
    const [ordersForPrint, setOrdersForPrint] = React.useState([]);
    
    const addOrder = (newOrder) => {
        const orders = [];
        ordersForPrint.forEach(order => orders.push(order));
        ordersForPrint.push(newOrder);
        setOrdersForPrint(orders);
    };
    
    const removeOrder = (orderForRemove) => {
        setOrdersForPrint(ordersForPrint.filter(order => order.id !== orderForRemove.id));
    };
    
    const getOrders = () => {
        console.log(ordersForPrint);
        return ordersForPrint;
    };
    
    return (
            <OrdersForPrintContext.Provider value={{ addOrder, removeOrder, getOrders }}>
                {children}
            </OrdersForPrintContext.Provider>
    );
};
