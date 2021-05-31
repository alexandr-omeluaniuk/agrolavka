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
    
    fromRanges(ranges) {
        throw new Error("Abstract method can't be instantiated.");
    }
    
    static _createElementFromHTML(htmlString) {
        var div = document.createElement('div');
        div.innerHTML = htmlString.trim();
        return div.firstChild; 
    }
    
    static _applySelection(ranges, fullElementHandler, partialElementHandler) {
        if (!ranges) {
            return;
        }
        ranges.forEach(range => {
            const nodes = AbstractComponent._getTextNodesFromRange(range);
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
                    fullElementHandler(node);
                } else {
                    partialElementHandler(node, startIndex, endIndex);
                }
            });
        });
    }
    
    static _insertNodeInTheMiddle(node, startIndex, endIndex, tag) {
        const endNode = node.splitText(endIndex);
        const middleNode = node.splitText(startIndex);
        const tagElement = document.createElement(tag);
        tagElement.appendChild(middleNode);
        node.parentElement.insertBefore(tagElement, endNode);
        return tagElement;
    }
    
    static _applyStyleToNode(node, cssProperty, cssValue) {
        const style = node.getAttribute('style');
        let styles = style ? style.split(';') : [];
        styles = styles.filter(s => s.indexOf(cssProperty) === -1 && s.length > 0);
        styles.push(cssProperty + ': ' + cssValue);
        node.setAttribute('style', styles.join(';'));
    }
    
    static _getTextNodesFromRange(range) {
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
