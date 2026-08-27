import { ArrowRight, Utensils, Home as HomeIcon } from 'lucide-react';
import './Seva.css';

export function SevaExperience() {
  return (
    <div className="seva-page pb-[100px] md:pb-0">
      {/* Hero Section */}
      <section className="seva-hero content-section">
        <div className="hero-container">
          <img 
            src="https://lh3.googleusercontent.com/aida-public/AB6AXuD89HhevnEPqcKm9ufccQ8LzHvUi-WByT60GlH621Y7Y-TE1rZNsZXxzVZ93BSlpvtwHLg8et5sgP-hyc5aQHhqajL5ZZjg8q2WrMb8s05Dp_OypBILx70B3fASt_vpog31turESwHrC5j5NGo5LShkXg2DZqY5imGoBRoYkTcWWlO5zmn36CUbg-COBZ_mrASOj4yxkmdpHsPbZkVR-8p_u4Sb3nPSmXInQ6s07jljVcucJkZi9M8" 
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
              <img src="https://lh3.googleusercontent.com/aida-public/AB6AXuCgk4UrBIZUJjSm1Cr36avFw6KpOgNGHCE3t0K409FH9aQp1RAFOiO_CUM0z1oSqp3byZBWdJcrWemoxI2sk5aozUZ2kOWu9M5EEdEkOZGOdtpdUmxAtON221R2bxHr1nSq43DmLGifYYw2jgvBpI21wh_wTjK7dqmlWr-P-HS5Ap-XVjy_NgDGGnbprqsjNBWZErHXlD1l63LtIzZaVvdOEhzQNuquxc688fOVvJJ7vEcdJoZjRKU" alt="Nourishment" />
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
              <img src="https://lh3.googleusercontent.com/aida-public/AB6AXuBaD43YavHDAwUy7BcU1K9o_420VGs8qV3nxk4LfdPgzQqqQqA44ZksOfNkWHmCIs1szOqOZZkhcB5V_LuVJLi6PImyCpSq7gHjZ3Z-COiutctuVQ5QlqZW3MAnNF3S1XzlLzClc8OWRXHhLNInGAosV3v31oKI5PXTfyC69BRITsooDxC4dAfpd0QXtSzFLEIfuWQoJDtRREpDvNh60knkCgb1DiHOhWYB0eBdh8gI0RJrn3O6eao" alt="Healing" />
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
              <img src="https://lh3.googleusercontent.com/aida-public/AB6AXuCBO5BZpCFDb_EmO81fQQd3pY7OAJP9RD53MMxE15K2AV6TkDInpPTXLHEJQfAIAtBNIYaa36qxCR8m62AfJ2NgaHaHwmNHkeVDm_WS8bfasTJsKyPVMsiy6uoRSZCrsVI6irrYj_uwQmA_IKWUhr2uALE_zMLDDqSI3wbvEnUzmuIa2Puf3lOfQ6eYjhVkTcqp_7jQCc5ektBLCzTvm8p5HtcInBgkrTOU15RBL0IGmrXhOq7FrAc" alt="Sanctuary" />
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
