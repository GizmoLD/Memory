class ChatViewDisconnected extends HTMLElement {
    constructor() {
        super()
        this.shadow = this.attachShadow({ mode: 'open' })
    }

    async connectedCallback() {
        // Carrega els estils CSS
        const style = document.createElement('style')
        style.textContent = await fetch('/shadows/chat_view_disconnected.css').then(r => r.text())
        this.shadow.appendChild(style)
    
        // Carrega els elements HTML
        const htmlContent = await fetch('/shadows/chat_view_disconnected.html').then(r => r.text())

        // Converteix la cadena HTML en nodes utilitzant un DocumentFragment
        const template = document.createElement('template');
        template.innerHTML = htmlContent;
        
        // Clona i afegeix el contingut del template al shadow
        this.shadow.appendChild(template.content.cloneNode(true));

        // Definir els 'eventListeners' dels objectes
        this.shadow.querySelector('#buttonConnect').addEventListener('click', this.actionConnect.bind(this))
    } 

    // Intentar connectar amb el servidor
    async actionConnect() {
        let server = this.shadow.querySelector('#server').value
        let port = this.shadow.querySelector('#port').value

        connect('ws', server, port)

        document.querySelector('chat-ws').showView('viewConnecting')
        
        await new Promise(resolve => setTimeout(resolve, 1500))

        if (socketConnected) {
            document.querySelector('chat-ws').showView('viewConnected')
        } else {
            document.querySelector('chat-ws').showView('viewDisconnected')
        }
    }
}

// Defineix l'element personalitzat
customElements.define('chat-view-disconnected', ChatViewDisconnected)