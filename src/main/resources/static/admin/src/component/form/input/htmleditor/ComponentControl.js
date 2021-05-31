/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from 'react';
import FormDialog from '../../../window/FormDialog';
import Form from '../../Form';

function ComponentControl(props) {
    const { open, handleClose, component } = props;
    
    return (
            <FormDialog title={component.settingsTitle} open={open} handleClose={handleClose}>
                <Form formConfig={component.formConfig} onSubmitAction={component.onSubmit} record={component.settings} disabled={false}></Form>
            </FormDialog>
    );
}

export default ComponentControl;
