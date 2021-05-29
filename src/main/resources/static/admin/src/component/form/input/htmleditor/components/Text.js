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
        
    create(state) {
        this.state = state;
        const initiator = this.state.initiator;
        this.textarea = this._createElementFromHTML(`<textarea class="form-control" />`);
        initiator.appendChild(this.textarea);
        this.textarea.addEventListener('blur', (evt) => {
            this._finishCreation(evt);
        }, true);
        setTimeout(() => {
            this.textarea.focus();
        }, 100);
    }
    
    edit(state) {
        this.state = state;
        const initiator = this.state.initiator;
        this.textarea = this._createElementFromHTML(`<textarea class="form-control" />`);
        this.textarea.value = initiator.innerHTML;
        initiator.style.display = 'none';
        initiator.parentElement.appendChild(this.textarea);
        this.textarea.addEventListener('blur', (evt) => {
            this._finishEditing(evt);
        }, true);
        setTimeout(() => {
            this.textarea.focus();
        }, 100);
    }
    
    _finishCreation(evt) {
        const text = this.textarea.value;
        const initiator = this.state.initiator;
        const newElement = this._createElementFromHTML(this.wrapper.replaceAll('{text}', text));
        newElement.htmlEditorComponent = this;
        initiator.appendChild(newElement);
        this.textarea.remove();
    }
    
    _finishEditing(evt) {
        const text = this.textarea.value;
        const initiator = this.state.initiator;
        initiator.style.display = null;
        initiator.innerHTML = text;
        this.textarea.remove();
    }
    
}
