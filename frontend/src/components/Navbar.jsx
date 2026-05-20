import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { ShoppingBag, User, LogOut, Menu, X, Search } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const { count, setCartOpen } = useCart();
  const navigate = useNavigate();
  const location = useLocation();
  const [scrolled, setScrolled] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  const handleLogout = () => {
    logout();
    setUserMenuOpen(false);
    navigate('/');
  };

  const navLinks = [
    { to: '/', label: 'Home' },
    { to: '/shop', label: 'Shop' },
    { to: '/shop?cat=phones', label: 'Phones' },
    { to: '/shop?cat=laptops', label: 'Laptops' },
    { to: '/shop?cat=monitors', label: 'Monitors' },
  ];

  return (
    <nav className={`fixed top-0 left-0 right-0 z-50 transition-all duration-500 ${scrolled ? 'bg-cream/95 backdrop-blur-md shadow-sm border-b border-surface' : 'bg-transparent'
      }`}>
      <div className="px-4 mx-auto max-w-7xl sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 group">
            <div className="flex items-center justify-center w-8 h-8 rounded-lg" style={{ background: '#1C1C1E' }}>
              <span className="text-xs font-bold" style={{ color: '#C9A84C' }}>SW</span>
            </div>
            <span className="text-xl font-semibold tracking-tight font-display text-charcoal">
              Shop<span className="gradient-text">Wave</span>
            </span>
          </Link>

          {/* Desktop Nav */}
          <div className="items-center hidden gap-8 md:flex">
            {navLinks.map(link => (
              <Link
                key={link.to}
                to={link.to}
                className={`nav-link text-sm font-medium transition-colors duration-200 ${location.pathname === link.to ? 'text-accent' : 'text-charcoal/70 hover:text-charcoal'
                  }`}
              >
                {link.label}
              </Link>
            ))}
          </div>

          {/* Right Actions */}
          <div className="flex items-center gap-3">
            {/* Cart */}
            <button
              onClick={() => { if (user) setCartOpen(true); else navigate('/login'); }}
              className="relative p-2 transition-colors duration-200 rounded-full hover:bg-surface"
            >
              <ShoppingBag size={20} className="text-charcoal" />
              {count > 0 && (
                <span className="absolute -top-0.5 -right-0.5 w-4 h-4 rounded-full text-[10px] font-bold flex items-center justify-center"
                  style={{ background: '#C9A84C', color: '#1C1C1E' }}>
                  {count > 9 ? '9+' : count}
                </span>
              )}
            </button>

            {/* User */}
            {user ? (
              <div className="relative">
                <button
                  onClick={() => setUserMenuOpen(!userMenuOpen)}
                  className="flex items-center gap-2 px-3 py-1.5 rounded-full border border-surface hover:border-accent-light transition-colors duration-200"
                >
                  <div className="flex items-center justify-center w-6 h-6 text-xs font-bold rounded-full"
                    style={{ background: '#C9A84C', color: '#1C1C1E' }}>
                    {user.name ? user.name[0].toUpperCase() : user.username[0].toUpperCase()}
                  </div>
                  <span className="hidden text-sm font-medium text-charcoal sm:block">
                    {user.name || user.username}
                  </span>
                </button>
                {userMenuOpen && (
                  <div className="absolute right-0 w-48 mt-2 overflow-hidden bg-white border shadow-2xl top-full rounded-2xl border-surface animate-fade-in">
                    <div className="px-4 py-3 border-b border-surface">
                      <p className="text-xs text-muted">Signed in as</p>
                      <p className="text-sm font-semibold truncate text-charcoal">@{user.username}</p>
                    </div>
                    <Link to="/orders" className="flex items-center gap-2 px-4 py-2.5 text-sm text-charcoal/70 hover:text-charcoal hover:bg-surface transition-colors"
                      onClick={() => setUserMenuOpen(false)}>
                      <ShoppingBag size={14} /> My Orders
                    </Link>
                    <button onClick={handleLogout}
                      className="w-full flex items-center gap-2 px-4 py-2.5 text-sm text-red-500 hover:bg-red-50 transition-colors">
                      <LogOut size={14} /> Sign Out
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <div className="items-center hidden gap-2 sm:flex">
                <Link to="/login" className="px-4 py-1.5 text-sm font-medium text-charcoal/70 hover:text-charcoal transition-colors">
                  Sign In
                </Link>
                <Link to="/register" className="bg-amber-50 px-4 py-1.5 text-sm font-semibold rounded-full text-cream btn-shimmer transition-all duration-300"
                >
                  Join Free
                </Link>
              </div>
            )}

            {/* Mobile menu btn */}
            <button className="p-2 rounded-full md:hidden hover:bg-surface" onClick={() => setMobileOpen(!mobileOpen)}>
              {mobileOpen ? <X size={20} /> : <Menu size={20} />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileOpen && (
        <div className="border-t md:hidden bg-cream border-surface animate-fade-in">
          <div className="flex flex-col gap-3 px-4 py-4">
            {navLinks.map(link => (
              <Link key={link.to} to={link.to}
                className="py-1 text-sm font-medium text-charcoal/70 hover:text-charcoal"
                onClick={() => setMobileOpen(false)}>
                {link.label}
              </Link>
            ))}
            {!user && (
              <div className="flex gap-2 pt-2 border-t border-surface">
                <Link to="/login" className="flex-1 px-4 py-2 text-sm text-center transition-colors border rounded-full border-surface hover:bg-surface"
                  onClick={() => setMobileOpen(false)}>Sign In</Link>
                <Link to="/register" className="flex-1 px-4 py-2 text-sm font-semibold text-center rounded-full text-cream"
                  style={{ background: '#1C1C1E' }} onClick={() => setMobileOpen(false)}>Join Free</Link>
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  );
}
