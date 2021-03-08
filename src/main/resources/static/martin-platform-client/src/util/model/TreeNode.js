/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export class TreeNode {
    
    constructor(id, name, children) {
        this.id = `${id}`;
        this.name = name;
    }
    
    setChildren(children) {
        this.children = children;
    }
    
    getChildren() {
        return this.children;
    }
    
    setOrigin(origin) {
        this.origin = origin;
    }
    
    getOrigin() {
        return this.origin;
    }
    
    getId() {
        return this.id;
    }
}
