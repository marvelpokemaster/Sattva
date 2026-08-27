import React, { useState } from 'react';
import { signInWithEmailAndPassword, createUserWithEmailAndPassword } from 'firebase/auth';
import { auth } from '@/lib/firebase';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import './Auth.css';

export function Auth() {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isLogin) {
        await signInWithEmailAndPassword(auth, email, password);
      } else {
        await createUserWithEmailAndPassword(auth, email, password);
      }
      navigate('/');
    } catch (err: any) {
      setError(err.message || 'Authentication failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-background">
        <div className="auth-overlay"></div>
      </div>
      
      <main className="auth-main">
        <div className="auth-header">
          <h1 className="typography-display-lg text-white mb-2 text-shadow">
            {isLogin ? 'Welcome Back' : 'Join Sattva'}
          </h1>
          <p className="typography-body-lg text-white-opacity text-shadow">
            Connect deeply with ancient traditions through acts of selfless service.
          </p>
        </div>

        <div className="auth-form-container glass-surface">
          <form onSubmit={handleSubmit} className="auth-form">
            {error && <div className="auth-error">{error}</div>}
            
            <div className="input-group">
              <label className="typography-label-sm text-white">Email Address</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="auth-input"
                placeholder="Enter your email"
              />
            </div>
            
            <div className="input-group">
              <label className="typography-label-sm text-white">Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="auth-input"
                placeholder="Enter your password"
              />
            </div>

            <Button
              type="submit"
              fullWidth
              disabled={loading}
              className="auth-submit-btn"
            >
              {loading ? 'Processing...' : (isLogin ? 'Sign In' : 'Create Account')}
            </Button>

            <button
              type="button"
              className="auth-toggle typography-label-sm text-white"
              onClick={() => setIsLogin(!isLogin)}
            >
              {isLogin ? "Don't have an account? Sign up" : 'Already have an account? Sign in'}
            </button>
          </form>
        </div>
      </main>
    </div>
  );
}
