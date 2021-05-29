/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AbstractComponent from '../AbstractComponent';

export const H1 = 'H1';
export const H2 = 'H2';
export const H3 = 'H3';
export const H4 = 'H4';
export const H5 = 'H5';
export const H6 = 'H6';

class TextType {
    
    constructor(type, template) {
        this.type = type;
        this.template = template;
    }
    
    getType() {
        return this.type;
    }
    
    getTemplate() {
        return this.template;
    }
    
    wrap(text) {
        return this.template.replaceAll('{text}', text);
    }
}

export const TYPES = [
    new TextType(H1, '<h1>{text}</h1>'),
    new TextType(H2, '<h2>{text}</h2>'),
    new TextType(H3, '<h3>{text}</h3>'),
    new TextType(H4, '<h4>{text}</h4>'),
    new TextType(H5, '<h5>{text}</h5>'),
    new TextType(H6, '<h6>{text}</h6>')
]; 

export class Text extends AbstractComponent {
    
    constructor(type) {
        super();
        this.textType = TYPES.filter(t => t.getType() === type)[0];
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
        const newElement = this._createElementFromHTML(this.textType.wrap(text));
        newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
        newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
        initiator.appendChild(newElement);
        this.textarea.remove();
        this.state.onChange();
    }
    
    _finishEditing(evt) {
        const text = this.textarea.value;
        const initiator = this.state.initiator;
        initiator.style.display = null;
        initiator.innerHTML = text;
        this.textarea.remove();
        this.state.onChange();
    }
    
}
