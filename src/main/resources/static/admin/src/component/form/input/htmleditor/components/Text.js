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
        
    call(ranges) {
        console.log(ranges);
        ranges.forEach(range => {
            const nodes = Text.getTextNodesFromRange(range);
            nodes.forEach(node => {
                const textContent = node.textContent;
                let startIndex = 0;
                let endIndex = textContent.length;
                if (node === range.startContainer && range.startOffset) {
                    startIndex = range.startOffset;
                }
                if (node === range.endContainer && range.endOffset) {
                    endIndex = range.endOffset;
                }
                
                if (startIndex === 0 && endIndex === textContent.length) {
                    const newElement = Text._createElementFromHTML(this.textType.wrap(textContent));
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
                    node.parentNode.replaceChild(newElement, node);
                } else {
                    const newElement = Text._createElementFromHTML(this.textType.wrap(textContent.substring(startIndex, endIndex)));
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, this.textType.getType());
                    let html = `${textContent.substring(0, startIndex)}${newElement.outerHTML}${textContent.substring(endIndex)}`;
                    node.parentElement.innerHTML = html;
                }
            });
        });
    }
    
    static applyColorToSelectedText(color, ranges) {
        if (!ranges) {
            return;
        }
        ranges.forEach(range => {
            const nodes = Text.getTextNodesFromRange(range);
            nodes.forEach(node => {
                const textContent = node.textContent;
                let startIndex = 0;
                let endIndex = textContent.length;
                if (node === range.startContainer && range.startOffset) {
                    startIndex = range.startOffset;
                }
                if (node === range.endContainer && range.endOffset) {
                    endIndex = range.endOffset;
                }
                if (startIndex === 0 && endIndex === textContent.length) {
                    Text.applyStyleToNode(node.parentNode, 'color', color);
                } else {
                    const newElement = Text._createElementFromHTML(`<span>${textContent.substring(startIndex, endIndex)}</span>`);
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, 'Text');
                    newElement.setAttribute(AbstractComponent.ATTRIBUTE_TYPE, SPAN);
                    let html = `${textContent.substring(0, startIndex)}${newElement.outerHTML}${textContent.substring(endIndex)}`;
                    node.parentElement.innerHTML = html;
                    Text.applyStyleToNode(newElement, 'color', color);
                }
            });
        });
    }
    
    static applyStyleToNode(node, cssProperty, cssValue) {
        const style = node.getAttribute('style');
        let styles = style ? style.split(';') : [];
        styles = styles.filter(s => s.indexOf(cssProperty) === -1 && s.length > 0);
        styles.push(cssProperty + ': ' + cssValue);
        node.setAttribute('style', styles.join(';'));
    }
    
    static getTextNodesFromRange(range) {
        var _iterator = document.createNodeIterator(
            range.commonAncestorContainer, NodeFilter.SHOW_TEXT, {
                acceptNode: function (node) {
                    return NodeFilter.FILTER_ACCEPT;
                }
            }
        );
        var _nodes = [];
        while (_iterator.nextNode()) {
            if (_nodes.length === 0 && _iterator.referenceNode !== range.startContainer) continue;
                _nodes.push(_iterator.referenceNode);
            if (_iterator.referenceNode === range.endContainer) break;
        }
        return _nodes;
    }
}
