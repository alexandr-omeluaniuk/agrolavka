/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AppURLs from '../../conf/app-urls';

export class Module {
    
    constructor(id, path, items) {
        this.id = id;
        this.path = path;
        this.items = items;
        this.visible = true;
        this.roles = null;
    }
    
    setBackgroundImage(img) {
        this.background = img;
        return this;
    }
    
    getBackgroundImage() {
        return this.background;
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
    
    isVisible() {
        return this.visible;
    }
    
    setInvisible() {
        this.visible = false;
        return this;
    }
    
    setIcon(icon) {
        this.icon = icon;
        return this;
    }
    
    getCurrentItem() {
        let path = window.location.pathname.replace(AppURLs.app, '');
        return this._visitItem(this, path, '');
    }
    
    permitForRoles(roles) {
        this.roles = roles;
        return this;
    }
    
    isPermitted(role) {
        return this.roles === null || this.roles.includes(role);
    }
    
    getLabelKey(item) {
        let visitItem = function (i, key) {
            if (i.id === item.id) {
                return key;
            }
            if (i.items) {
                for (let j = 0; j < i.items.length; j++) {
                    let k = i.items[j];
                    let result = visitItem(k, `${key}.${k.id}`);
                    if (result) {
                        return result;
                    }
                }
            }
        };
        return visitItem(this, `m_${this.id}:${this.id}`);
    }
    
    _visitItem(item, path, parentSubPath) {
        if (item !== null) {
            let match;
            if (item.path.indexOf('/:') === -1) {   // for exact path
                match = parentSubPath + item.path === path;
            } else {    // for path templates
                let fullPath = (parentSubPath + item.path).trim().split('/');
                let fullPathSb = '';
                fullPath.filter(s => {
                    return s.length > 0;
                }).forEach(subPath => {
                    if (subPath.indexOf(':id') !== -1) {
                        //eslint-disable-next-line
                        fullPathSb += '\/[0-9]+';
                    } else {
                        //eslint-disable-next-line
                        fullPathSb += '\/' + subPath;
                    }
                });
                let regexp = new RegExp(fullPathSb);
                match = regexp.test(path);
            }
            if (match) {
                return item;
            }
            if (item.items) {
                for (let i = 0; i < item.items.length; i++) {
                    let result = this._visitItem(item.items[i], path, parentSubPath + item.path);
                    if (result) {
                        return result;
                    }
                }
            }
        }
    }
}
