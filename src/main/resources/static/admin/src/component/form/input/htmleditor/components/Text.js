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

export const P = 'P';
export const SPAN = 'SPAN';

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
    new TextType(H6, '<h6>{text}</h6>'),
    new TextType(P, '<p>{text}</p>'),
    new TextType(SPAN, '<span>{text}</span>')
]; 

export class Text extends AbstractComponent {
    
    constructor(type) {
        super();
        this.textType = TYPES.filter(t => t.getType() === type)[0];
    }
        
    fromRanges(ranges) {
        AbstractComponent._applySelection(ranges, (node) => {
            const textContent = node.textContent;
            const newElement = AbstractComponent._createElementFromHTML(this.textType.wrap(textContent));
            newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
            node.parentNode.replaceChild(newElement, node);
        }, (node, startIndex, endIndex) => {
            const nodeInTheMiddle = AbstractComponent._insertNodeInTheMiddle(node, startIndex, endIndex, this.textType.getType());
            nodeInTheMiddle.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            nodeInTheMiddle.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
        });
    }
    
    static applyColorToSelectedText(color, ranges) {
        AbstractComponent._applySelection(ranges, (node) => {
            AbstractComponent._applyStyleToNode(node.parentNode, 'color', color);
            node.parentNode.normalize();
        }, (node, startIndex, endIndex) => {
            const span = AbstractComponent._insertNodeInTheMiddle(node, startIndex, endIndex, 'span');
            AbstractComponent._applyStyleToNode(span, 'color', color);
        });
    }
    
    static applyAlignmentToSelectedText(alignment, ranges) {
        AbstractComponent._applySelection(ranges, (node) => {
            AbstractComponent._applyStyleToNode(node.parentNode, 'text-align', alignment);
            node.parentNode.normalize();
        }, (node, startIndex, endIndex) => {
            const span = AbstractComponent._insertNodeInTheMiddle(node, startIndex, endIndex, 'span');
            AbstractComponent._applyStyleToNode(span, 'text-align', alignment);
        });
    }
    
}
