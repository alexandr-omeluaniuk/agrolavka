/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export class ModuleView {
    
    constructor(id, path, icon, component, items) {
        this.id = id;
        this.path = path;
        this.icon = icon;
        this.component = component;
        this.items = items;
    }
    
    getIcon() {
        return this.icon;
    }
    
    getPath() {
        return this.path;
    }
    
    getId() {
        return this.id;
    }
    
    getItems() {
        return this.items;
    }
}
