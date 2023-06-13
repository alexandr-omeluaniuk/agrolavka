
class XElement extends HTMLElement {
    constructor() {
        super();
        this._contents = new DocumentFragment();
        this._contents.appendChild(this.createTemplate().content.cloneNode(true));
    }
    
    connectedCallback() {
        this.appendChild(this._contents);
    }
    
    createTemplate() {
        throw new Error('Override me!');
    }
}

class XMenuItemIcon extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const color = this.getAttribute('color');
        const icon = this.getAttribute('icon');
        template.innerHTML = `
            <button class="btn btn-sm btn-${color} btn-floating me-2" type="button">
                <i class="fas fa-${icon} fa-fw me-3"></i>
            </button>
        `;
        return template;
    }
}
window.customElements.define('x-agr-menu-item-icon', XMenuItemIcon);

class XMenuItem extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const link = this.getAttribute('link');
        const icon = this.getAttribute('icon');
        const labelColor = this.getAttribute('label-color');
        const iconColor = this.getAttribute('icon-color');
        const label = this.getAttribute('label');
        const labelElement = this.hasAttribute('bold-label') ? 'b' : 'span';
        const endIcon = this.getAttribute('end-icon');
        const customLinkAttributes = this.getAttribute('link-attributes'); 
        template.innerHTML = `
            <a href="${link}" class="agr-mobile-menu-link" ${customLinkAttributes ? customLinkAttributes : ''}>
                <div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">
                    <x-agr-menu-item-icon icon="${icon}" color="${iconColor}"></x-agr-menu-item-icon>
                    <${labelElement} class="text-${labelColor}" style="flex: 1;">${label}</${labelElement}>
                    ${endIcon ? `<i class="fas fa-${endIcon} text-dark me-2"></i>` : ''}
                </div>
            </a>
        `;
        return template;
    }
}
window.customElements.define('x-agr-menu-item', XMenuItem);

class XCatalogMenuItem extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const label = this.getAttribute('data-label');
        const href = this.getAttribute('data-href');
        const isTopCategory = this.hasAttribute('data-top-category');
        const catalog = this.getAttribute('data-catalog');
        template.innerHTML = `
            <a href="${href}" class="agr-menu-catalog-group-link ${isTopCategory ? 'agr-menu-item-top-category' : ''}" ${href ? 'data-catalog-nav-link' : ''} ${catalog ? `data-catalog="${catalog}"` : ''}>
                <div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">
                    <span class="ms-2" style="flex: 1">${label}</span>
                    ${!href ? '<i class="fas fa-chevron-right fa-fw"></i>' : ''}
                </div>
            </a>
        `;
        return template;
    }
}
window.customElements.define('x-agr-catalog-menu-item', XCatalogMenuItem);
