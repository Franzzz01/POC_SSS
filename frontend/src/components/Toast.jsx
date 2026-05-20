import React, { createContext, useContext, useState, useCallback } from 'react';
import { CheckCircle, AlertCircle, X } from 'lucide-react';

const ToastContext = createContext(null);

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((message, type = 'success') => {
    const id = Date.now();
    setToasts(t => [...t, { id, message, type }]);
    setTimeout(() => setToasts(t => t.filter(toast => toast.id !== id)), 3500);
  }, []);

  const removeToast = (id) => setToasts(t => t.filter(toast => toast.id !== id));

  return (
    <ToastContext.Provider value={{ addToast }}>
      {children}
      <div className="fixed top-4 right-4 z-[9999] flex flex-col gap-2">
        {toasts.map(toast => (
          <div
            key={toast.id}
            className="animate-slide-in-right flex items-center gap-3 px-4 py-3 rounded-xl shadow-2xl min-w-[260px] max-w-xs"
            style={{ background: toast.type === 'success' ? '#1C1C1E' : '#991B1B', color: '#FAF8F5' }}
          >
            {toast.type === 'success'
              ? <CheckCircle size={18} color="#C9A84C" />
              : <AlertCircle size={18} color="#FCA5A5" />}
            <span className="flex-1 text-sm font-medium">{toast.message}</span>
            <button onClick={() => removeToast(toast.id)} className="opacity-60 hover:opacity-100 transition-opacity">
              <X size={14} />
            </button>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}

export const useToast = () => useContext(ToastContext);
