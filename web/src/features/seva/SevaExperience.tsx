import { ArrowRight, Utensils, Home as HomeIcon } from 'lucide-react';
import './Seva.css';
import { IMAGES } from '@/lib/images';

export function SevaExperience() {
  return (
    <div className="seva-page pb-[100px] md:pb-0">
      {/* Hero Section */}
      <section className="seva-hero content-section">
        <div className="hero-container">
          <img 
            src={IMAGES.seva.fodderMonsoon} 
            alt="Fodder for Monsoon" 
            className="hero-image"
          />
          <div className="hero-gradient"></div>
          
          <div className="hero-content">
            <div className="badge-dark mb-4">
              <span className="pulse-dot"></span>
              <span className="typography-label-sm text-white uppercase tracking-widest">Featured Initiative</span>
            </div>
            <h2 className="typography-headline-lg text-white mb-2">Fodder for Monsoon</h2>
            <p className="typography-body-lg text-white-opacity max-w-lg mb-6">
              Ensure warmth and nourishment for the gentle residents of the Kerala sanctuary during the heavy rains.
            </p>
            <button className="btn-terracotta typography-label-md">Contribute Now</button>
          </div>
        </div>
      </section>

      {/* Impact Statistics */}
      <section className="impact-section content-section py-stack-xl">
        <div className="impact-grid">
          <div className="impact-text">
            <h3 className="typography-headline-md text-primary mb-4">Your impact resonates.</h3>
            <p className="typography-body-md text-variant">
              Every contribution flows directly to those who need it most, creating a ripple of compassion and care across our sanctuaries.
            </p>
          </div>
          
          <div className="impact-stats">
            <div className="stat-card border-l-terracotta">
              <div>
                <span className="typography-display-lg text-primary block leading-none">120</span>
                <span className="typography-label-sm text-variant uppercase tracking-widest mt-2 block">Animals Fed Today</span>
              </div>
              <Utensils size={40} className="text-terracotta opacity-50" />
            </div>
            
            <div className="stat-card border-l-secondary ml-8">
              <div>
                <span className="typography-display-lg text-primary block leading-none">45</span>
                <span className="typography-label-sm text-variant uppercase tracking-widest mt-2 block">Shelters Reinforced</span>
              </div>
              <HomeIcon size={40} className="text-secondary opacity-50" />
            </div>
          </div>
        </div>
      </section>

      {/* Seva Options */}
      <section className="seva-options content-section mb-stack-xl">
        <h3 className="typography-headline-md text-primary mb-6">Choose Your Seva</h3>
        
        <div className="options-list">
          {/* Option 1 */}
          <div className="seva-option-card group">
            <div className="option-image-container">
              <img src={IMAGES.seva.nourishment} alt="Nourishment" />
            </div>
            <div className="option-content">
              <div className="option-header">
                <h4 className="typography-headline-md text-primary">Nourishment (Fodder)</h4>
                <span className="price-badge">₹500 / day</span>
              </div>
              <p className="typography-body-md text-variant mb-4">
                Provide rich, organic hay and fresh green fodder to ensure the daily nutritional needs of the cows are met.
              </p>
              <div className="action-link text-primary mt-auto">
                <ArrowRight size={16} />
                <span className="typography-label-sm uppercase tracking-widest">Select Offering</span>
              </div>
            </div>
          </div>

          {/* Option 2 */}
          <div className="seva-option-card group">
            <div className="option-image-container">
              <img src={IMAGES.seva.healing} alt="Healing" />
            </div>
            <div className="option-content">
              <div className="option-header">
                <h4 className="typography-headline-md text-primary">Healing (Medicine)</h4>
                <span className="price-badge">₹1200 / course</span>
              </div>
              <p className="typography-body-md text-variant mb-4">
                Support necessary veterinary care, vaccinations, and holistic herbal treatments for sick or elderly animals.
              </p>
              <div className="action-link text-primary mt-auto">
                <ArrowRight size={16} />
                <span className="typography-label-sm uppercase tracking-widest">Select Offering</span>
              </div>
            </div>
          </div>

          {/* Option 3 */}
          <div className="seva-option-card group">
            <div className="option-image-container">
              <img src={IMAGES.seva.sanctuary} alt="Sanctuary" />
            </div>
            <div className="option-content">
              <div className="option-header">
                <h4 className="typography-headline-md text-primary">Sanctuary (Shelter)</h4>
                <span className="price-badge">₹2500 / month</span>
              </div>
              <p className="typography-body-md text-variant mb-4">
                Contribute to the structural maintenance of the Gaushala, ensuring a dry, safe, and warm environment year-round.
              </p>
              <div className="action-link text-primary mt-auto">
                <ArrowRight size={16} />
                <span className="typography-label-sm uppercase tracking-widest">Select Offering</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
