/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AbstractComponent from '../AbstractComponent';
import { FormConfig, FormField, Validator } from '../../../../../util/model/TableConfig';
import { TYPES, VALIDATORS } from '../../../../../service/DataTypeService';

export class Link extends AbstractComponent {
    
    initComponentControl(settings, ranges, t) {
        this.settingsTitle = t('component.htmleditor.toolbar.content.links.settings');
        this.settings = settings;
        this.ranges = ranges;
        this.formConfig = new FormConfig([
            new FormField('id', TYPES.ID).hide(),
            new FormField('url', TYPES.TEXTFIELD, 'Link').setGrid({xs: 12, md: 12}).validation([
                new Validator(VALIDATORS.REQUIRED),
                new Validator(VALIDATORS.WEB_URL)
            ])
        ]);
    }
    
    onSubmit = (data) => {
        const ranges = this.ranges;
        AbstractComponent._applySelection(ranges, (node) => {
            const textContent = node.textContent;
            const newElement = AbstractComponent._createElementFromHTML(`<a href="${data.url}">${textContent}</a>`);
            newElement.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            node.parentNode.replaceChild(newElement, node);
        }, (node, startIndex, endIndex) => {
            const nodeInTheMiddle = AbstractComponent._insertNodeInTheMiddle(node, startIndex, endIndex, 'a');
            nodeInTheMiddle.setAttribute(AbstractComponent.ATTRIBUTE_CLASS, this.constructor.name);
            nodeInTheMiddle.setAttribute("href", data.url);
        });
    };
}
