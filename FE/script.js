const app = {
    user: null,
    cart: [],
    view: 'catalog', // catalog, checkout, orders
    products: [
        { id: '1', name: 'Leche', price: 1.5, stock: 10 },
        { id: '2', name: 'Pan', price: 0.8, stock: 5 },
        { id: '3', name: 'Huevos', price: 2.2, stock: 12 }
    ],

    init() {
        const savedUser = localStorage.getItem('supermarket_user');
        if (savedUser) {
            this.user = JSON.parse(savedUser);
            this.showMainApp();
        }
        this.render();
    },

    login() {
        const name = document.getElementById('username').value;
        if (!name) return alert("Pon un nombre");
        this.user = { id: Date.now().toString(), name };
        localStorage.setItem('supermarket_user', JSON.stringify(this.user));
        this.showMainApp();
    },

    logout() {
        this.user = null;
        this.cart = [];
        localStorage.removeItem('supermarket_user');
        location.reload();
    },

    showMainApp() {
        document.getElementById('auth-view').classList.add('hidden');
        document.getElementById('main-app').classList.remove('hidden');
        document.getElementById('user-name').innerText = `Hola, ${this.user.name}`;
        this.render();
    },

    setView(newView) {
        this.view = newView;
        this.render();
    },

    toggleCart() {
        document.getElementById('cart-sidebar').classList.toggle('hidden');
        document.getElementById('overlay').classList.toggle('hidden');
    },

    addToCart(productId) {
        const product = this.products.find(p => p.id === productId);
        const existing = this.cart.find(item => item.product.id === productId);

        if (existing) {
            if (existing.quantity < product.stock) existing.quantity++;
        } else {
            this.cart.push({ product, quantity: 1 });
        }
        this.render();
    },

    render() {
        // Actualizar contador
        const count = this.cart.reduce((acc, item) => acc + item.quantity, 0);
        document.getElementById('cart-count').innerText = count;

        // Renderizar Contenido
        const container = document.getElementById('content-area');
        container.innerHTML = '';

        if (this.view === 'catalog') {
            this.products.forEach(p => {
                container.innerHTML += `
                    <div class="product-card">
                        <h3>${p.name}</h3>
                        <p>$${p.price}</p>
                        <button onclick="app.addToCart('${p.id}')">Agregar</button>
                    </div>
                `;
            });
        } else if (this.view === 'checkout') {
            container.innerHTML = `<h2>Resumen de compra</h2><p>Total a pagar: $${this.cart.reduce((a, b) => a + (b.product.price * b.quantity), 0).toFixed(2)}</p><button onclick="alert('Compra realizada'); app.logout()">Pagar ahora</button>`;
        }

        // Renderizar Carrito en Sidebar
        const cartItemsDiv = document.getElementById('cart-items');
        cartItemsDiv.innerHTML = this.cart.map(item => `
            <div>${item.product.name} x ${item.quantity} - $${(item.product.price * item.quantity).toFixed(2)}</div>
        `).join('');
    }
};

// Arrancar app
app.init();