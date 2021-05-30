/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export default class AbstractComponent {
        
    static ATTRIBUTE_CLASS = 'data-html-editor-component-class';
    static ATTRIBUTE_TYPE = 'data-html-editor-component-type';
        
    constructor() {
        if (this.constructor === AbstractComponent) {
            throw new Error("Abstract class can't be instantiated.");
        }
    }
    
    create(state) {
        throw new Error("Abstract method can't be instantiated.");
    }
    
    _createElementFromHTML(htmlString) {
        var div = document.createElement('div');
        div.innerHTML = htmlString.trim();
        return div.firstChild; 
    }
}
