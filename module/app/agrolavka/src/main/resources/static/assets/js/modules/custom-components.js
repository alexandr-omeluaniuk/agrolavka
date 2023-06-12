
class XComponent extends HTMLElement {

    constructor() {
        super();

        this.createTemplate();

        const template = document.getElementById(this.tag()).content;
        const shadowRoot = this.attachShadow({mode: "open"});
        const stylesheet = document.createElement('link');
        stylesheet.setAttribute('href', 'https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.5.0/mdb.min.css');
        stylesheet.setAttribute('rel', 'stylesheet');
        shadowRoot.appendChild(stylesheet);
        shadowRoot.appendChild(template.cloneNode(true));
    }

    render() {
        throw Error('Render function is not defined!');
    }
    
    tag() {
        throw Error('Tag is not defined!');
    }
    
    createTemplate() {
        const template = document.createElement('template');
        template.setAttribute('id', this.tag());
        template.innerHTML = this.render();
        document.querySelector('body').appendChild(template);
    }
}

class Panel extends XComponent {

    constructor() {
        super();
    }

    render() {
        return `
        <div class="d-flex rounded-pill shadow-sm p-2">
            <slot></slot>
        </div>
        `;
    }
    
    tag() {
        return 'x-agr-panel';
    }
}

customElements.define('x-agr-panel', Panel);
