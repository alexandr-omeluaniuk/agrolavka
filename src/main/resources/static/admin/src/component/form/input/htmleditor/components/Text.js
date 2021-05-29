/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AbstractComponent from '../AbstractComponent';

export default class Text extends AbstractComponent {
        
    edit(state) {
        this.state = state;
        console.log(this.state);
    }
    
}
