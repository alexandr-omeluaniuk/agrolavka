/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { Text } from './components/Text';
import AbstractComponent from './AbstractComponent';

const COMPONENTS = { Text };

export default class ComponentsFactory {
    
    static isHTMLEditorComponent(element) {
        return element.getAttribute(AbstractComponent.ATTRIBUTE_CLASS) ? true : false;
    }
    
    static getComponent(element) {
        const componentClass = element.getAttribute(AbstractComponent.ATTRIBUTE_CLASS);
        const componentType = element.getAttribute(AbstractComponent.ATTRIBUTE_TYPE);
        return new COMPONENTS[componentClass](componentType);
    }
}
