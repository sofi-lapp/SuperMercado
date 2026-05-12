import { useState, useEffect } from 'react';
import { AuthForm } from './components/AuthForm';
import { Header } from './components/Header';
import { ProductCatalog } from './components/ProductCatalog';
import { ShoppingCart } from './components/ShoppingCart';
import { Checkout } from './components/Checkout';
import { OrderTracking } from './components/OrderTracking';
import { OrdersList } from './components/OrdersList';

export default function App() {
  const [user, setUser] = useState(null);
  const [cartItems, setCartItems] = useState([]);
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [currentView, setCurrentView] = useState('catalog');
  const [currentOrder, setCurrentOrder] = useState(null);
  const [userOrders, setUserOrders] = useState([]);

  // Cargar usuario desde localStorage al montar el componente
  useEffect(() => {
    const savedUser = localStorage.getItem('supermarket_user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  // Cargar pedidos cuando el usuario inicia sesión
  useEffect(() => {
    if (user) {
      const savedOrders = localStorage.getItem('supermarket_orders');
      if (savedOrders) {
        const allOrders = JSON.parse(savedOrders);
        setUserOrders(allOrders.filter((order) => order.userId === user.id));
      }
    }
  }, [user]);

  const handleLogin = (loggedInUser) => {
    setUser(loggedInUser);
  };

  const handleLogout = () => {
    setUser(null);
    setCartItems([]);
    setIsCartOpen(false);
    setCurrentView('catalog');
    localStorage.removeItem('supermarket_user');
  };

  const handleAddToCart = (product) => {
    setCartItems((prevItems) => {
      const existingItem = prevItems.find((item) => item.product.id === product.id);

      if (existingItem) {
        // Si ya está en el carrito, aumentar cantidad si hay stock
        if (existingItem.quantity < product.stock) {
          return prevItems.map((item) =>
            item.product.id === product.id
              ? { ...item, quantity: item.quantity + 1 }
              : item
          );
        }
        return prevItems;
      } else {
        // Agregar nuevo producto al carrito
        return [...prevItems, { product, quantity: 1 }];
      }
    });
  };

  const handleUpdateQuantity = (productId, delta) => {
    setCartItems((prevItems) => {
      return prevItems
        .map((item) => {
          if (item.product.id === productId) {
            const newQuantity = item.quantity + delta;
            return { ...item, quantity: newQuantity };
          }
          return item;
        })
        .filter((item) => item.quantity > 0);
    });
  };

  const handleRemoveItem = (productId) => {
    setCartItems((prevItems) => prevItems.filter((item) => item.product.id !== productId));
  };

  const handleCheckout = () => {
    setIsCartOpen(false);
    setCurrentView('checkout');
  };

  const handleCheckoutComplete = (order) => {
    setCurrentOrder(order);
    setCartItems([]);
    setCurrentView('tracking');
    setUserOrders((prev) => [...prev, order]);
  };

  const handleNewOrder = () => {
    setCurrentOrder(null);
    setCurrentView('catalog');
  };

  const handleViewOrders = () => {
    setCurrentView('orders');
    setIsCartOpen(false);
  };

  const handleViewOrderDetail = (order) => {
    setCurrentOrder(order);
    setCurrentView('tracking');
  };

  const handleBackToCatalog = () => {
    setCurrentView('catalog');
  };

  const cartItemsCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  if (!user) {
    return <AuthForm onLogin={handleLogin} />;
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {currentView !== 'tracking' && (
        <Header
          user={user}
          cartItemsCount={cartItemsCount}
          onOpenCart={() => setIsCartOpen(true)}
          onLogout={handleLogout}
          onViewOrders={handleViewOrders}
        />
      )}

      <main>
        {currentView === 'catalog' && (
          <div className="container mx-auto px-4 py-4">
            <ProductCatalog
              onAddToCart={handleAddToCart}
              onUpdateQuantity={handleUpdateQuantity}
              cartItems={cartItems}
            />
          </div>
        )}

        {currentView === 'checkout' && (
          <Checkout
            items={cartItems}
            userId={user.id}
            onComplete={handleCheckoutComplete}
            onBack={() => setIsCartOpen(true)}
          />
        )}

        {currentView === 'tracking' && currentOrder && (
          <OrderTracking order={currentOrder} onNewOrder={handleNewOrder} />
        )}

        {currentView === 'orders' && (
          <OrdersList
            orders={userOrders}
            onBack={handleBackToCatalog}
            onViewOrder={handleViewOrderDetail}
          />
        )}
      </main>

      {/* Carrito de compras lateral */}
      {isCartOpen && (
        <>
          {/* Overlay/Fondo oscuro */}
          <div
            className="fixed inset-0 bg-black bg-opacity-50 z-40"
            onClick={() => setIsCartOpen(false)}
          />
          {/* Componente Carrito */}
          <ShoppingCart
            items={cartItems}
            onUpdateQuantity={handleUpdateQuantity}
            onRemoveItem={handleRemoveItem}
            onCheckout={handleCheckout}
            onClose={() => setIsCartOpen(false)}
          />
        </>
      )}
    </div>
  );
}