import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { user } = useAuth();
  const [cart, setCart] = useState([]);
  const [cartOpen, setCartOpen] = useState(false);

  const cartKey = user ? `shopwave_cart_${user.username}` : null;

  useEffect(() => {
    if (cartKey) {
      const stored = localStorage.getItem(cartKey);
      setCart(stored ? JSON.parse(stored) : []);
    } else {
      setCart([]);
    }
  }, [cartKey]);

  const saveCart = (newCart) => {
    setCart(newCart);
    if (cartKey) localStorage.setItem(cartKey, JSON.stringify(newCart));
  };

  const addToCart = (product) => {
    const existing = cart.find(i => i.id === product.id);
    if (existing) {
      saveCart(cart.map(i => i.id === product.id ? { ...i, qty: i.qty + 1 } : i));
    } else {
      saveCart([...cart, { ...product, qty: 1 }]);
    }
  };

  const removeFromCart = (id) => saveCart(cart.filter(i => i.id !== id));

  const updateQty = (id, qty) => {
    if (qty < 1) { removeFromCart(id); return; }
    saveCart(cart.map(i => i.id === id ? { ...i, qty } : i));
  };

  const clearCart = () => saveCart([]);

  const total = cart.reduce((s, i) => s + i.price * i.qty, 0);
  const count = cart.reduce((s, i) => s + i.qty, 0);

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, updateQty, clearCart, total, count, cartOpen, setCartOpen }}>
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);
