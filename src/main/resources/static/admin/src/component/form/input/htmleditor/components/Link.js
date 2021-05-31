/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AbstractComponent from '../AbstractComponent';


export class Link extends AbstractComponent {
    
    constructor() {
        super();
    }
    
    fromRanges(ranges) {
        AbstractComponent._applySelection(ranges, (node) => {
            const textContent = node.textContent;
            const newElement = AbstractComponent._createElementFromHTML(`<a>${textContent}</a>`);
            newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
            node.parentNode.replaceChild(newElement, node);
        }, (node, startIndex, endIndex) => {
            const nodeInTheMiddle = AbstractComponent._insertNodeInTheMiddle(node, startIndex, endIndex, 'a');
            nodeInTheMiddle.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            nodeInTheMiddle.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
        });
    }
}
