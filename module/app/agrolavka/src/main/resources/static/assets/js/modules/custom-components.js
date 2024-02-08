
class XElement extends HTMLElement {
    constructor() {
        super();
        this._contents = new DocumentFragment();
        this._contents.appendChild(this.createTemplate().content.cloneNode(true));
    }
    
    connectedCallback() {
        this.innerHTML = "";
        this.appendChild(this._contents);
        if (this.onClick) {
            this.addEventListener('click', this.onClick);
        }
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
        const image = this.getAttribute('data-image');
        template.innerHTML = `
            <a href="${href}" class="agr-menu-catalog-group-link ${isTopCategory ? 'agr-menu-item-top-category' : ''}" ${href ? 'data-catalog-nav-link' : ''} ${catalog ? `data-catalog="${catalog}"` : ''}>
                <div class="list-group-item list-group-item-action d-flex rounded-pill p-2 align-items-center">
                    <button class="btn btn-sm btn-light btn-floating me-2" type="button" style="background-size: cover; background-image: url('${image}')">
                    </button>
                    <span class="ms-2" style="flex: 1">${label}</span>
                    ${!href ? '<i class="fas fa-chevron-right fa-fw"></i>' : ''}
                </div>
            </a>
        `;
        return template;
    }
}
window.customElements.define('x-agr-catalog-menu-item', XCatalogMenuItem);

class XCategoryCard extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const name = this.getAttribute('data-name');
        const image = this.getAttribute('data-image');
        const imageCreatedDate = this.getAttribute('data-image-created');
        const link = this.getAttribute('data-link');
        const imageElement = image 
            ? `<div class="card-img-top agr-card-image" style="background-image: url('/media/${image}?timestamp=${imageCreatedDate}')"></div>` 
            : `<div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>`;
        template.innerHTML = `
            <div class="card shadow-1-strong mb-4 hover-shadow">
                <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                    ${imageElement}
                    <a href="${link}">
                        <div class="mask" style="background-color: rgba(0, 0, 0, 0.05)"></div>
                    </a>
                    <div class="card-body agr-card-body-category">
                        <h6 class="card-title mb-0">${name}</h6>
                    </div>
                </div>
            </div>
        `;
        return template;
    }
}
window.customElements.define('x-agr-category-card', XCategoryCard);

class XProductPrice extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const rowClass = this.getAttribute('data-row-class');
        const discount = this.getAttribute('data-discount');
        const rawPrice = this.getAttribute('data-price');
        const price = parseFloat(rawPrice).toFixed(2).split('.');
        const priceInt = price[0];
        const priceFloat = price[1];
        const discountPrice = parseFloat(rawPrice * (1 - discount/100)).toFixed(2).split('.');
        const discountPriceInt = discountPrice[0];
        const discountPriceFloat = discountPrice[1];
        template.innerHTML = `
            <div class="d-flex align-items-center justify-content-between ${rowClass}">
                <small class="text-muted">Цена</small>
                <span class="agr-price fw-bold ${discount ? 'text-decoration-line-through text-muted' : 'text-dark'}">
                    ${priceInt}<small>.${priceFloat}</small>
                    <small class="text-muted">BYN</small>
                </span>
            </div>
            ${discount ? `
                <div class="d-flex align-items-center justify-content-between ${rowClass}">
                    <small class="text-danger"><i class="fas fa-fire me-1"></i> Акция</small>
                    <div class="text-danger fw-bold">
                        ${discountPriceInt}<small>.${discountPriceFloat}</small>
                        <small>BYN</small>
                    </div>
                </div>
            ` : ''}
        `;
        return template;
    }
    
    _setPrice(price) {
        const priceBig = parseFloat(price).toFixed(2).split('.')[0];
        const priceSmall = parseFloat(price).toFixed(2).split('.')[1];
        this.querySelector('.agr-price').innerHTML = priceBig + ".<small>" + priceSmall
            + '</small> <small class="text-muted">BYN</small>';
    }
}
window.customElements.define('x-agr-product-price', XProductPrice);

class XProductRibbon extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const discount = this.getAttribute('data-discount');
        const inStock = this.getAttribute('data-in-stock');
        const hide = this.getAttribute('data-hide');
        if (hide && !discount) {
            return template;
        }
        let color;
        let label;
        const icon = discount ? '<i class="fas fa-fire me-1"></i>' : '';
        if (discount) {
            color = 'danger';
            label = ' -' + parseFloat(discount).toFixed(0) + '%';
        } else if (inStock) {
            color = 'success';
            label = 'в наличии';
        } else {
            color = 'gray';
            label = 'под заказ';
        }
        template.innerHTML = `
            <div class="ribbon ribbon-top-left">
                <span class="bg-${color}">
                    <small>${icon}${label}</small>
                </span>
            </div>
        `;
        return template;
    }
}
window.customElements.define('x-agr-product-ribbon', XProductRibbon);

class XProductVolumes extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const cls = this.getAttribute('data-cls');
        const rawVolumes = this.getAttribute('data-volume');
        const volumes = JSON.parse(rawVolumes.replaceAll("'", '"'));
        const minPrice = volumes[0].price;
        const selectedQuantity = volumes[0].amount;
        let sb = '';
        const getLabel = (v) => {
            const fractional = v.amount - parseInt(v.amount);
            return (fractional >= 0.1 ? v.amount : parseInt(v.amount)) + "л";
        };
        for (let i = 0; i < volumes.length; i++) {
            const v = volumes[i];
            const btnClass = i === 0 ? "btn-info" : "btn-outline-info";
            sb += `
                <button type="button" class="agr-volume-btn btn ${btnClass} btn-rounded ${cls}" data-mdb-color="dark"
                        data-product-volume-price="${v.price}" data-product-volume-quantity="${v.amount}">
                    ${getLabel(v)}
                </button>
            `;
        }
        template.innerHTML = `
            <div class="btn-group w-100 shadow-0 mt-1" role="group" aria-label="Volumes" 
                data-volumes="${rawVolumes}" 
                data-selected-volume-quantity="${selectedQuantity}" 
                data-selected-volume-price="${minPrice}">
                    ${sb}
            </div>
        `;
        return template;
    }
}
window.customElements.define('x-agr-product-volumes', XProductVolumes);

class XProductVariant extends XElement {  
    
    onClick() {
        const variantsComponent = this.closest('x-agr-product-variants');
        variantsComponent._switchButtons(this.state.variant);
    }
    
    createTemplate() {
        this.state = {
            variant: null
        };
        this.state.variant = JSON.parse(this.getAttribute('data-variant').replaceAll("'", '"'));
        this.template = document.createElement('template');
        this.template.innerHTML = this.render();
        return this.template;
    }
    
    render() {
        const price = parseFloat(this.state.variant.price).toFixed(2).split('.');
        const priceInt = price[0];
        const priceFloat = price[1];
        return `<button type="button" class="w-100 mb-1 agr-variant-btn btn 
                    btn-sm btn-rounded btn-outline-primary" data-mdb-ripple-init
                    data-product-variant-id="${this.state.variant.id}"
                    data-product-variant-price="${this.state.variant.price}"
                    data-product-variant-name="${this.state.variant.name}">
                        <div class="d-flex justify-content-between">
                            <div>${this.state.variant.name}</div>
                            <div>${priceInt}<small>.${priceFloat}</small></div>
                        </div>
                    </button>`;
    };
}
window.customElements.define('x-agr-product-variant', XProductVariant);

class XProductVariants extends XElement {    
    createTemplate() {
        this.state = {
            selectedVariant: null,
            variants: []
        };
        const rawVariants = this.getAttribute('data-variants');
        this.state.variants = JSON.parse(rawVariants.replaceAll("'", '"'));
        this.state.selectedVariant = this.state.variants[0];
        this.template = document.createElement('template'); 
        this.template.innerHTML = this.render();
        return this.template;
    }
    
    render() {
        const buttons = this.state.variants.map(v => 
            `<x-agr-product-variant data-variant="${JSON.stringify(v).replaceAll('"', "'")}" 
                data-selected="${this.state.selectedVariant.id === v.id}"></x-agr-product-variant>`
        ).join('');
        return `
            <div class="d-flex flex-column mb-2">
                ${buttons}
            </div>
            <hr/>
        `;
    };
    
    _switchButtons(variant) {
        this.state.selectedVariant = variant;
        this.querySelectorAll('x-agr-product-variant').forEach(btn => {
            const isActive = btn.state.variant.id === variant.id;
            const innerBtn = btn.querySelector('button');
            if (isActive) {
                innerBtn.classList.remove("btn-outline-primary");
                innerBtn.classList.add("btn-primary");
            } else {
                innerBtn.classList.remove("btn-primary");
                innerBtn.classList.add("btn-outline-primary");
            }
        });
        this.closest('div').querySelector('x-agr-product-price')._setPrice(variant.price);
    }
}
window.customElements.define('x-agr-product-variants', XProductVariants);

class XProductActions extends XElement {    
    createTemplate() {
        this.state = {
            id: null,
            inCart: false
        };
        let template = document.createElement('template');
        this.state.id = this.getAttribute('data-id');
        const cls = this.getAttribute('data-cls');
        this.state.inCart = this.getAttribute('data-in-cart');
        const volumes = this.getAttribute('data-volume');
        const rawVariants = this.getAttribute('data-variants');
        const hasVariants = rawVariants && rawVariants.length > 2;
        template.innerHTML = `
            ${hasVariants ? `<x-agr-product-variants data-variants="${rawVariants}" data-in-cart-variants="${this.getAttribute('data-in-cart-variants')}"></x-agr-product-variants>` : ''}
            ${volumes && !hasVariants ? `<x-agr-product-volumes data-cls="${cls}" data-volume="${volumes}"></x-agr-product-volumes>` : ''}
            <button class="btn btn-outline-info btn-rounded w-100 mt-1 ${cls}" data-product-id="${this.state.id}" data-order="">
                <i class="far fa-hand-point-up me-2"></i> Заказать сразу
            </button>
            ${this.state.inCart ? `
                <button class="btn btn-outline-danger btn-rounded w-100 mt-1 ${cls}" data-product-id="${this.state.id}" data-remove="">
                    <i class="fas fa-minus-circle me-2"></i> Из корзины
                </button>
            ` : `
                <button class="btn btn-outline-success btn-rounded w-100 mt-1 ${cls}" data-product-id="${this.state.id}" data-add="">
                    <i class="fas fa-cart-plus me-2"></i> В корзину
                </button>
            `}
        `;
        return template;
    }
}
window.customElements.define('x-agr-product-actions', XProductActions);

class XProductCard extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const id = this.getAttribute('data-id');
        const discount = this.getAttribute('data-discount');
        const inStock = this.getAttribute('data-in-stock');
        const hide = this.getAttribute('data-hide-ribbon');
        const inCart = this.getAttribute('data-in-cart');
        const createdDate = this.getAttribute('data-created');
        const name = this.getAttribute('data-name');
        const price = this.getAttribute('data-price');
        const image = this.getAttribute('data-image');
        const imageCreatedDate = this.getAttribute('data-image-created');
        const link = this.getAttribute('data-link');
        const volumes = this.getAttribute('data-volume');
        const imageElement = image 
            ? `<div class="card-img-top agr-card-image" style="background-image: url('/media/${image}?timestamp=${imageCreatedDate}')"></div>` 
            : `<div class="card-img-top agr-card-image" style="background-image: url('/assets/img/no-image.png')"></div>`;
        template.innerHTML = `
            <a href="${link}">
                <div class="card shadow-1-strong mb-4 hover-shadow">
                    <x-agr-product-ribbon data-discount="${discount}" data-in-stock="${inStock}" data-hide="${hide}"></x-agr-product-ribbon>
                    <div class="bg-image hover-overlay ripple" data-mdb-ripple-color="light">
                        ${imageElement}
                        <div class="card-body" style="min-height: 100px;">
                            <h6 class="card-title text-dark text-left" style="min-height: 60px;">${name}</h6>
                            <x-agr-product-price data-row-class="agr-card-line" data-discount="${discount}" data-price="${price}"></x-agr-product-price>
                            ${createdDate ? `
                                <div class="d-flex align-items-center justify-content-between mb-1 agr-card-line">
                                    <small class="text-muted">Добавлено</small>
                                    <small class="text-muted">${createdDate}</small>
                                </div>
                            ` : ''}
                            <x-agr-product-actions data-id="${id}" data-cls="agr-card-button" data-in-cart="${inCart}" data-volume="${volumes}"></x-agr-product-actions>
                        </div>
                    </div>
                </div>
            </a>
        `;
        return template;
    }
}
window.customElements.define('x-agr-product-card', XProductCard);

class XProductDescription extends XElement {    
    createTemplate() {
        let template = document.createElement('template');
        const name = this.getAttribute('data-name');
        const video = this.getAttribute('data-video');
        const description = this.getAttribute('data-description');
        template.innerHTML = `
            <h4>${name}</h4>
            ${video ? `
                <iframe src="${video}" width="100%"  height="350"
                    title="YouTube video player" frameborder="0" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
                </iframe>
            ` : ''}
            ${description ? `<div class="text-justify mb-3" style="white-space: pre-line;">${description}</div>` : ''}
        `;
        return template;
    }
    
    connectedCallback() {
        const node = this.appendChild(this._contents);
        this.querySelectorAll('table').forEach(el => {
            const parent = el.parentElement;
            el.remove();
            const responsiveDiv = document.createElement('div');
            responsiveDiv.setAttribute("class", "table-responsive");
            parent.appendChild(responsiveDiv);
            responsiveDiv.appendChild(el);
            el.setAttribute("class", "table table-sm table-bordered table-light");
            el.setAttribute("style", "");
            el.querySelectorAll("td").forEach(td => td.setAttribute("style", ""));
            el.querySelectorAll("th").forEach(td => td.setAttribute("style", ""));
        });
    }
}
window.customElements.define('x-agr-product-description', XProductDescription);
