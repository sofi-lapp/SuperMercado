import React, { createContext, useContext, useState, useEffect } from 'react';

export type User = {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'CLIENT' | 'ADMIN';
};

export type Product = {
  id: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  unit: 'KG' | 'LITRO' | 'UNIDAD';
  weight?: number;
  imageUrl: string;
  categoryId: string;
  categoryName: string;
  active: boolean;
  dateAdded: string;
};

export type CartItem = {
  product: Product;
  quantity: number;
};

export type OrderStatus = 'PENDIENTE' | 'CONFIRMADO' | 'ENVIADO' | 'ENTREGADO' | 'CANCELADO';
export type PaymentMethod = 'TRANSFERENCIA' | 'TARJETA' | 'EFECTIVO';

export type Order = {
  id: string;
  userId: string;
  userName: string;
  items: { product: Product; quantity: number }[];
  total: number;
  status: OrderStatus;
  paymentMethod: PaymentMethod;
  paymentReference?: string;
  shippingAddress: Address;
  createdAt: string;
  updatedAt: string;
};

export type Address = {
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  notes?: string;
};

export type Notification = {
  id: string;
  message: string;
  type: 'order' | 'status' | 'stock' | 'promo';
  read: boolean;
  timestamp: string;
};

export type Category = {
  id: string;
  name: string;
  description?: string;
  parentId?: string;
  active: boolean;
  productCount: number;
};

type AppContextType = {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (data: { email: string; password: string; firstName: string; lastName: string }) => Promise<void>;
  logout: () => void;
  cart: CartItem[];
  addToCart: (product: Product, quantity: number) => void;
  removeFromCart: (productId: string) => void;
  updateCartQuantity: (productId: string, quantity: number) => void;
  clearCart: () => void;
  orders: Order[];
  createOrder: (shippingAddress: Address, paymentMethod: PaymentMethod, paymentReference?: string) => Promise<Order>;
  updateOrderStatus: (orderId: string, status: OrderStatus) => void;
  notifications: Notification[];
  markNotificationAsRead: (id: string) => void;
  products: Product[];
  categories: Category[];
  addProduct: (product: Omit<Product, 'id' | 'dateAdded'>) => void;
  updateProduct: (id: string, product: Partial<Product>) => void;
  deleteProduct: (id: string) => void;
  addCategory: (category: Omit<Category, 'id' | 'productCount'>) => void;
  updateCategory: (id: string, category: Partial<Category>) => void;
  deleteCategory: (id: string) => void;
};

const AppContext = createContext<AppContextType | undefined>(undefined);

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [cart, setCart] = useState<CartItem[]>([]);
  const [orders, setOrders] = useState<Order[]>([]);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);

  // Initialize with mock data
  useEffect(() => {
    initializeMockData();
  }, []);

  const initializeMockData = () => {
    // Mock categories
    const mockCategories: Category[] = [
      { id: '1', name: 'Frutas', active: true, productCount: 5 },
      { id: '2', name: 'Verduras', active: true, productCount: 4 },
      { id: '3', name: 'Lácteos', active: true, productCount: 3 },
      { id: '4', name: 'Carnes', active: true, productCount: 3 },
      { id: '5', name: 'Bebidas', active: true, productCount: 2 },
    ];
    setCategories(mockCategories);

    // Mock products
    const mockProducts: Product[] = [
      {
        id: '1',
        name: 'Manzanas Rojas',
        description: 'Manzanas rojas frescas y jugosas, perfectas para comer o cocinar.',
        price: 3.99,
        stock: 100,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400&h=400&fit=crop',
        categoryId: '1',
        categoryName: 'Frutas',
        active: true,
        dateAdded: '2026-05-01',
      },
      {
        id: '2',
        name: 'Plátanos',
        description: 'Plátanos maduros, dulces y cremosos.',
        price: 2.49,
        stock: 150,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1603833665858-e61d17a86224?w=400&h=400&fit=crop',
        categoryId: '1',
        categoryName: 'Frutas',
        active: true,
        dateAdded: '2026-05-02',
      },
      {
        id: '3',
        name: 'Naranjas',
        description: 'Naranjas frescas y jugosas, ricas en vitamina C.',
        price: 4.99,
        stock: 80,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1582979512210-99b6a53386f9?w=400&h=400&fit=crop',
        categoryId: '1',
        categoryName: 'Frutas',
        active: true,
        dateAdded: '2026-05-03',
      },
      {
        id: '4',
        name: 'Fresas',
        description: 'Fresas frescas y dulces, perfectas para postres.',
        price: 5.99,
        stock: 50,
        unit: 'KG',
        weight: 0.5,
        imageUrl: 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=400&h=400&fit=crop',
        categoryId: '1',
        categoryName: 'Frutas',
        active: true,
        dateAdded: '2026-05-04',
      },
      {
        id: '5',
        name: 'Uvas',
        description: 'Uvas verdes sin semillas, dulces y refrescantes.',
        price: 6.99,
        stock: 60,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1599819177406-6859e1d03cae?w=400&h=400&fit=crop',
        categoryId: '1',
        categoryName: 'Frutas',
        active: true,
        dateAdded: '2026-05-05',
      },
      {
        id: '6',
        name: 'Tomates',
        description: 'Tomates maduros y jugosos, perfectos para ensaladas y salsas.',
        price: 3.49,
        stock: 120,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1546470427-227a993b51ba?w=400&h=400&fit=crop',
        categoryId: '2',
        categoryName: 'Verduras',
        active: true,
        dateAdded: '2026-05-06',
      },
      {
        id: '7',
        name: 'Lechugas',
        description: 'Lechugas frescas y crujientes, ideales para ensaladas.',
        price: 2.99,
        stock: 90,
        unit: 'UNIDAD',
        imageUrl: 'https://images.unsplash.com/photo-1622206151226-18ca2c9ab4a1?w=400&h=400&fit=crop',
        categoryId: '2',
        categoryName: 'Verduras',
        active: true,
        dateAdded: '2026-05-07',
      },
      {
        id: '8',
        name: 'Zanahorias',
        description: 'Zanahorias frescas, dulces y crujientes.',
        price: 2.49,
        stock: 110,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=400&h=400&fit=crop',
        categoryId: '2',
        categoryName: 'Verduras',
        active: true,
        dateAdded: '2026-05-08',
      },
      {
        id: '9',
        name: 'Brócoli',
        description: 'Brócoli fresco, rico en nutrientes y fibra.',
        price: 3.99,
        stock: 70,
        unit: 'KG',
        weight: 0.5,
        imageUrl: 'https://images.unsplash.com/photo-1583663848850-46af132dc08e?w=400&h=400&fit=crop',
        categoryId: '2',
        categoryName: 'Verduras',
        active: true,
        dateAdded: '2026-05-09',
      },
      {
        id: '10',
        name: 'Leche Entera',
        description: 'Leche fresca y entera, rica en calcio.',
        price: 4.49,
        stock: 200,
        unit: 'LITRO',
        imageUrl: 'https://images.unsplash.com/photo-1563636619-e9143da7973b?w=400&h=400&fit=crop',
        categoryId: '3',
        categoryName: 'Lácteos',
        active: true,
        dateAdded: '2026-05-10',
      },
      {
        id: '11',
        name: 'Yogurt Natural',
        description: 'Yogurt natural sin azúcar, cremoso y saludable.',
        price: 3.99,
        stock: 150,
        unit: 'LITRO',
        imageUrl: 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400&h=400&fit=crop',
        categoryId: '3',
        categoryName: 'Lácteos',
        active: true,
        dateAdded: '2026-05-11',
      },
      {
        id: '12',
        name: 'Queso Fresco',
        description: 'Queso fresco artesanal, suave y delicioso.',
        price: 7.99,
        stock: 80,
        unit: 'KG',
        weight: 0.5,
        imageUrl: 'https://images.unsplash.com/photo-1452195100486-9cc805987862?w=400&h=400&fit=crop',
        categoryId: '3',
        categoryName: 'Lácteos',
        active: true,
        dateAdded: '2026-05-12',
      },
      {
        id: '13',
        name: 'Pollo Entero',
        description: 'Pollo fresco y tierno, ideal para asar o guisar.',
        price: 12.99,
        stock: 50,
        unit: 'KG',
        weight: 2,
        imageUrl: 'https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=400&h=400&fit=crop',
        categoryId: '4',
        categoryName: 'Carnes',
        active: true,
        dateAdded: '2026-05-13',
      },
      {
        id: '14',
        name: 'Carne de Res',
        description: 'Carne de res de primera calidad, perfecta para bistecs.',
        price: 18.99,
        stock: 40,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1603048297172-c92544798d5a?w=400&h=400&fit=crop',
        categoryId: '4',
        categoryName: 'Carnes',
        active: true,
        dateAdded: '2026-05-14',
      },
      {
        id: '15',
        name: 'Carne de Cerdo',
        description: 'Carne de cerdo fresca, jugosa y sabrosa.',
        price: 14.99,
        stock: 45,
        unit: 'KG',
        weight: 1,
        imageUrl: 'https://images.unsplash.com/photo-1602470520998-f4a52199a3d6?w=400&h=400&fit=crop',
        categoryId: '4',
        categoryName: 'Carnes',
        active: true,
        dateAdded: '2026-05-15',
      },
      {
        id: '16',
        name: 'Agua Mineral',
        description: 'Agua mineral natural, pura y refrescante.',
        price: 1.99,
        stock: 300,
        unit: 'LITRO',
        imageUrl: 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=400&h=400&fit=crop',
        categoryId: '5',
        categoryName: 'Bebidas',
        active: true,
        dateAdded: '2026-05-16',
      },
      {
        id: '17',
        name: 'Jugo de Naranja',
        description: 'Jugo de naranja natural, recién exprimido.',
        price: 5.99,
        stock: 100,
        unit: 'LITRO',
        imageUrl: 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400&h=400&fit=crop',
        categoryId: '5',
        categoryName: 'Bebidas',
        active: true,
        dateAdded: '2026-05-17',
      },
    ];
    setProducts(mockProducts);
  };

  const login = async (email: string, password: string) => {
    // Mock login
    await new Promise((resolve) => setTimeout(resolve, 500));

    const isAdmin = email.includes('admin');
    const mockUser: User = {
      id: isAdmin ? 'admin-1' : 'user-1',
      email,
      firstName: isAdmin ? 'Admin' : 'Juan',
      lastName: isAdmin ? 'User' : 'Pérez',
      role: isAdmin ? 'ADMIN' : 'CLIENT',
    };
    setUser(mockUser);

    // Add welcome notification
    addNotification({
      message: `Bienvenido ${mockUser.firstName}!`,
      type: 'order',
    });
  };

  const register = async (data: { email: string; password: string; firstName: string; lastName: string }) => {
    // Mock register
    await new Promise((resolve) => setTimeout(resolve, 500));

    const mockUser: User = {
      id: 'user-' + Date.now(),
      email: data.email,
      firstName: data.firstName,
      lastName: data.lastName,
      role: 'CLIENT',
    };
    setUser(mockUser);

    addNotification({
      message: 'Cuenta creada exitosamente!',
      type: 'order',
    });
  };

  const logout = () => {
    setUser(null);
    setCart([]);
  };

  const addToCart = (product: Product, quantity: number) => {
    setCart((prev) => {
      const existing = prev.find((item) => item.product.id === product.id);
      if (existing) {
        return prev.map((item) =>
          item.product.id === product.id
            ? { ...item, quantity: item.quantity + quantity }
            : item
        );
      }
      return [...prev, { product, quantity }];
    });

    addNotification({
      message: `${product.name} agregado al carrito`,
      type: 'order',
    });
  };

  const removeFromCart = (productId: string) => {
    setCart((prev) => prev.filter((item) => item.product.id !== productId));
  };

  const updateCartQuantity = (productId: string, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(productId);
      return;
    }
    setCart((prev) =>
      prev.map((item) =>
        item.product.id === productId ? { ...item, quantity } : item
      )
    );
  };

  const clearCart = () => {
    setCart([]);
  };

  const createOrder = async (shippingAddress: Address, paymentMethod: PaymentMethod, paymentReference?: string): Promise<Order> => {
    await new Promise((resolve) => setTimeout(resolve, 500));

    const total = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);
    const newOrder: Order = {
      id: 'ORD-' + Date.now(),
      userId: user?.id || '',
      userName: user ? `${user.firstName} ${user.lastName}` : '',
      items: cart.map((item) => ({ product: item.product, quantity: item.quantity })),
      total,
      status: 'PENDIENTE',
      paymentMethod,
      paymentReference,
      shippingAddress,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    setOrders((prev) => [newOrder, ...prev]);
    clearCart();

    addNotification({
      message: `Pedido ${newOrder.id} creado exitosamente`,
      type: 'order',
    });

    return newOrder;
  };

  const updateOrderStatus = (orderId: string, status: OrderStatus) => {
    setOrders((prev) =>
      prev.map((order) =>
        order.id === orderId
          ? { ...order, status, updatedAt: new Date().toISOString() }
          : order
      )
    );

    const order = orders.find((o) => o.id === orderId);
    if (order) {
      addNotification({
        message: `Pedido ${orderId} actualizado a ${status}`,
        type: 'status',
      });
    }
  };

  const addNotification = (data: { message: string; type: Notification['type'] }) => {
    const notification: Notification = {
      id: Date.now().toString(),
      message: data.message,
      type: data.type,
      read: false,
      timestamp: new Date().toISOString(),
    };
    setNotifications((prev) => [notification, ...prev]);
  };

  const markNotificationAsRead = (id: string) => {
    setNotifications((prev) =>
      prev.map((notif) => (notif.id === id ? { ...notif, read: true } : notif))
    );
  };

  const addProduct = (product: Omit<Product, 'id' | 'dateAdded'>) => {
    const newProduct: Product = {
      ...product,
      id: Date.now().toString(),
      dateAdded: new Date().toISOString().split('T')[0],
    };
    setProducts((prev) => [newProduct, ...prev]);

    // Update category product count
    setCategories((prev) =>
      prev.map((cat) =>
        cat.id === product.categoryId
          ? { ...cat, productCount: cat.productCount + 1 }
          : cat
      )
    );
  };

  const updateProduct = (id: string, updates: Partial<Product>) => {
    setProducts((prev) =>
      prev.map((product) => (product.id === id ? { ...product, ...updates } : product))
    );
  };

  const deleteProduct = (id: string) => {
    const product = products.find((p) => p.id === id);
    setProducts((prev) => prev.filter((p) => p.id !== id));

    // Update category product count
    if (product) {
      setCategories((prev) =>
        prev.map((cat) =>
          cat.id === product.categoryId
            ? { ...cat, productCount: Math.max(0, cat.productCount - 1) }
            : cat
        )
      );
    }
  };

  const addCategory = (category: Omit<Category, 'id' | 'productCount'>) => {
    const newCategory: Category = {
      ...category,
      id: Date.now().toString(),
      productCount: 0,
    };
    setCategories((prev) => [newCategory, ...prev]);
  };

  const updateCategory = (id: string, updates: Partial<Category>) => {
    setCategories((prev) =>
      prev.map((cat) => (cat.id === id ? { ...cat, ...updates } : cat))
    );

    // Update products with this category
    if (updates.name) {
      setProducts((prev) =>
        prev.map((product) =>
          product.categoryId === id
            ? { ...product, categoryName: updates.name! }
            : product
        )
      );
    }
  };

  const deleteCategory = (id: string) => {
    setCategories((prev) => prev.filter((cat) => cat.id !== id));

    // Deactivate products in this category
    setProducts((prev) =>
      prev.map((product) =>
        product.categoryId === id ? { ...product, active: false } : product
      )
    );
  };

  return (
    <AppContext.Provider
      value={{
        user,
        login,
        register,
        logout,
        cart,
        addToCart,
        removeFromCart,
        updateCartQuantity,
        clearCart,
        orders,
        createOrder,
        updateOrderStatus,
        notifications,
        markNotificationAsRead,
        products,
        categories,
        addProduct,
        updateProduct,
        deleteProduct,
        addCategory,
        updateCategory,
        deleteCategory,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useApp() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useApp must be used within AppProvider');
  }
  return context;
}
