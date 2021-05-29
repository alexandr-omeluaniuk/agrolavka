/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AbstractComponent from '../AbstractComponent';

export default class Text extends AbstractComponent {
        
    constructor(wrapper) {
        super();
        this.wrapper = wrapper;
    }
        
    edit(state) {
        this.state = state;
        const initiator = this.state.initiator;
        initiator.innerHTML = `<textarea class="form-control" />`;
        const textarea = initiator.querySelector('textarea');
        textarea.addEventListener('blur', (evt) => {
            const text = textarea.value;
            initiator.innerHTML = this.wrapper.replaceAll('{text}', text);
        }, true);
        setTimeout(() => {
            textarea.focus();
        }, 100);
    }
    
}
