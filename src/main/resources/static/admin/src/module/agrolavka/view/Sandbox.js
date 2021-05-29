/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import React from 'react';
import HTMLEditor from '../../../component/form/input/HTMLEditor';

function Sandbox(props) {
    const [value, setValue] = React.useState(null);
    return (
            <HTMLEditor label={'HTML Editor'} name={'html'} value={value} labelWidth={160} onChangeFieldValue={(name, v) => {
                setValue(v);
            }}/>
    );
}

export default Sandbox;