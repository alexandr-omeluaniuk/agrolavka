/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React, { useEffect, useContext } from 'react';
import { ToolbarContext } from '../../../context/ToolbarContext';

function Order(props) {
    console.log(props);
    const { setTitle, setIcon } = useContext(ToolbarContext);
    useEffect(() => {
        
    }, []);
    return (
        <div>TODO</div>
    );
}

export default Order;

