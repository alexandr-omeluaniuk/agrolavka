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
    
    static applyColorToSelectedText(color, ranges) {
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
                    if (node.parentNode) {
                        Text.applyStyleToNode(node.parentNode, 'color', color);
                    } else {
                        console.log(node);
                        // TODO: handle this case
                    }
                } else {
                    let span = Text.insertSpan(node, startIndex, endIndex);
                    Text.applyStyleToNode(span, 'color', color);
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
    
    static insertSpan(node, startIdx, endIdx) {
        const textContent = node.textContent;
        let html = `${textContent.substring(0, startIdx)}<span ${AbstractComponent.ATTRIBUTE_CLASS}="Text" ${AbstractComponent.ATTRIBUTE_TYPE}="${SPAN}">${textContent.substring(startIdx, endIdx)}</span>${textContent.substring(endIdx)}`;
        const parentNode = node.parentNode;
        parentNode.innerHTML = html;
        return parentNode.querySelector('span');
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
