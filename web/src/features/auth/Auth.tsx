import React, { useState, useEffect } from 'react';
import { signInWithEmailAndPassword, createUserWithEmailAndPassword } from 'firebase/auth';
import { auth } from '@/lib/firebase';
import { useNavigate } from 'react-router-dom';
import { ArrowRight, Sparkles } from 'lucide-react';
import { useAuth } from '@/features/auth/AuthContext';
import { LoadingScreen } from '@/components/ui/LoadingScreen';
import './Auth.css';

export function Auth() {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { user, loading: authLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      navigate('/', { replace: true });
    }
  }, [user, navigate]);

  if (authLoading) {
    return <LoadingScreen message="Verifying Devotee..." subtext="Accessing your sacred journey" />;
  }

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
      setError(err.message || 'Authentication failed. Please verify credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleDevoteeDemo = () => {
    localStorage.setItem('dev_auth', 'true');
    window.location.href = '/';
  };

  return (
    <div className="auth-page">
      <div className="auth-background" />

      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-om-emblem">ॐ</div>
          <h2 className="auth-title">
            {isLogin ? 'Welcome, Devotee' : 'Enter the Sanctuary'}
          </h2>
          <p className="auth-subtitle">
            {isLogin 
              ? 'Connect with Vedic rituals, sacred cow care, and lifelong seva.' 
              : 'Join a sanctuary of compassionate devotion and verified transparency.'}
          </p>
        </div>

        <div className="auth-tabs">
          <button 
            type="button"
            className={`auth-tab ${isLogin ? 'active' : ''}`}
            onClick={() => { setIsLogin(true); setError(''); }}
          >
            Sign In
          </button>
          <button 
            type="button"
            className={`auth-tab ${!isLogin ? 'active' : ''}`}
            onClick={() => { setIsLogin(false); setError(''); }}
          >
            Create Account
          </button>
        </div>

        {error && <div className="auth-error">{error}</div>}

        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input
              type="email"
              required
              className="form-input"
              placeholder="devotee@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <input
              type="password"
              required
              className="form-input"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button 
            type="submit" 
            className="btn-primary auth-submit-btn"
            disabled={loading}
          >
            <span>{loading ? 'Processing...' : (isLogin ? 'Enter App' : 'Begin Journey')}</span>
            <ArrowRight size={16} />
          </button>
        </form>

        <div className="auth-divider">or explore sanctuary</div>

        <button 
          type="button" 
          className="demo-guest-btn"
          onClick={handleDevoteeDemo}
        >
          <Sparkles size={16} className="text-gold" />
          <span>Continue as Devotee Guest</span>
        </button>
      </div>
    </div>
  );
}
