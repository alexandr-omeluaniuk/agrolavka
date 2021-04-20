/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react'

export const ToolbarContext = React.createContext({
    title: '',
    setTitle: () => {},
    icon: '',
    setIcon: () => {}
});

