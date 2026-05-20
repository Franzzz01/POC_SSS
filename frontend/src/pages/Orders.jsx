import React from 'react';
import { Link } from 'react-router-dom';
import { ShoppingBag, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Orders() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-cream pt-24 px-6">
      <div className="max-w-3xl mx-auto">
        <div className="mb-10">
          <p className="text-xs font-bold uppercase tracking-[0.2em] text-accent mb-2">Account</p>
          <h1 className="font-display text-4xl font-bold text-charcoal">My Orders</h1>
          <p className="text-muted mt-1">@{user?.username}</p>
        </div>
        <div className="flex flex-col items-center justify-center py-24 gap-4 text-center bg-white rounded-3xl border border-surface">
          <div className="w-16 h-16 rounded-full bg-surface flex items-center justify-center">
            <ShoppingBag size={28} className="text-muted" />
          </div>
          <p className="font-semibold text-charcoal">No orders yet</p>
          <p className="text-sm text-muted">Your completed orders will appear here</p>
          <Link to="/shop"
            className="mt-2 flex items-center gap-2 px-6 py-3 rounded-full text-sm font-semibold btn-shimmer"
            style={{ background: '#1C1C1E', color: '#FAF8F5' }}>
            Start Shopping <ArrowRight size={14} />
          </Link>
        </div>
      </div>
    </div>
  );
}
