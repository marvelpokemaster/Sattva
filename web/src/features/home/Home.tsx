import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Sun, CloudRain, Play } from 'lucide-react';
import './Home.css';
import { useAuth } from '@/features/auth/AuthContext';

export function Home() {
  const { user } = useAuth();
  const userName = user?.displayName?.split(' ')[0] || 'Devotee';

  return (
    <div className="home-page">
      <header className="home-header">
        <button className="icon-btn">
           {/* Language / Location icon */}
        </button>
        <h1 className="typography-display-lg text-center">Sattva</h1>
        <div className="profile-avatar">
          {user?.photoURL ? (
            <img src={user.photoURL} alt="Profile" />
          ) : (
            <div className="avatar-placeholder">{userName[0]}</div>
          )}
        </div>
      </header>

      <div className="home-content content-section">
        {/* Greeting */}
        <section className="greeting-section">
          <p className="typography-label-sm greeting-subtitle">Suprabhat</p>
          <h2 className="typography-headline-lg greeting-title">Good Morning, {userName}.</h2>
        </section>

        {/* Panchang & Intention */}
        <Card className="panchang-card hoverable">
          <div className="panchang-header">
            <div>
              <h3 className="typography-headline-md text-primary">Margashirsha, Shukla Purnima</h3>
              <p className="typography-label-md text-variant flex-align">
                <Sun size={16} className="text-terracotta" /> Sunrise: 06:42 AM
              </p>
            </div>
            <div className="text-right">
              <p className="typography-label-sm text-terracotta">Nakshatra</p>
              <p className="typography-body-md">Mrigashirsha</p>
            </div>
          </div>
          <div className="intention-section border-top">
            <p className="typography-label-sm text-variant mb-3">Daily Intention</p>
            <blockquote className="intention-quote typography-body-lg text-primary italic border-left-terracotta">
              "Find stillness within the movement; let every action today be an offering of grace."
            </blockquote>
          </div>
        </Card>

        {/* Featured Ritual */}
        <section className="featured-section content-section">
          <div className="section-header">
            <h3 className="typography-headline-md text-primary">Featured Ritual</h3>
            <button className="typography-label-sm text-secondary uppercase-btn">View All</button>
          </div>
          <div className="featured-hero-card">
            <img 
              src="https://lh3.googleusercontent.com/aida-public/AB6AXuD9r5nB62u7mXo-RV4fq08ukZTzQby-rkfUchUd-UqYzx0ctg_WGQI5ryfuowzJHL7_EPNIxtgJYp5L8JuDwraNV7n_p1W7xETpfjQenmYsLCbOnABzCdotSmiNRuQwM037wvN89dUBe-MjH-HUAJJzDz08Y1c4PIYTEztWlgwXFHNW8yOBn1QuiSvGfApGOBo_2WRHG253ZFhkXNje07a6V2Vc6iWaYWN0MJn46mYLY76uW61GJNM" 
              alt="Kashi Vishwanath Aarti" 
              className="hero-image"
            />
            <div className="hero-gradient"></div>
            <div className="hero-badge glass-surface">
              <Play size={14} fill="currentColor" /> Live Now
            </div>
            <div className="hero-content">
              <h4 className="typography-headline-lg text-white mb-2">Kashi Vishwanath Aarti</h4>
              <p className="typography-body-md text-white-opacity mb-4 line-clamp-2">
                Experience the divine energy of Varanasi's most sacred evening ritual from your sanctuary.
              </p>
              <Button fullWidth className="hero-btn">Participate Now</Button>
            </div>
          </div>
        </section>

        {/* Daily Insight */}
        <section className="insight-section content-section">
          <h3 className="typography-headline-md text-primary mb-6">Daily Insight</h3>
          <div className="horizontal-scroll">
            <Card variant="elevated" className="insight-card active-card">
              <div className="insight-header">
                <div className="icon-circle bg-terracotta text-white">
                  <Sun size={20} />
                </div>
                <div>
                  <h4 className="typography-label-md text-primary">Mesha (Aries)</h4>
                  <p className="typography-label-sm text-variant">Today</p>
                </div>
              </div>
              <p className="typography-body-md line-clamp-3">
                A day of decisive action. The planetary alignment favors starting new spiritual practices or clearing old energetic debts.
              </p>
            </Card>

            <Card className="insight-card inactive-card">
              <div className="insight-header">
                <div className="icon-circle bg-variant text-primary">
                  <CloudRain size={20} />
                </div>
                <div>
                  <h4 className="typography-label-md text-primary">Vrishabha (Taurus)</h4>
                  <p className="typography-label-sm text-variant">Today</p>
                </div>
              </div>
              <p className="typography-body-md line-clamp-3">
                Focus on grounding rituals. Spending time near water or performing simple jal-arpana will bring mental clarity.
              </p>
            </Card>
          </div>
        </section>
      </div>
    </div>
  );
}
