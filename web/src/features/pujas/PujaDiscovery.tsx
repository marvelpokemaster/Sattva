import { useQuery } from '@tanstack/react-query';
import { getPujas } from '@/lib/api/puja';
import { Sparkles, Sun, Flame, User, ArrowRight } from 'lucide-react';
import './Puja.css';
import { IMAGES } from '@/lib/images';
import { CardSkeleton } from '@/components/ui/LoadingScreen';

export function PujaDiscovery() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['pujas'],
    queryFn: () => getPujas(),
  });

  // Mock data if API is empty
  const mockPujas = [
    {
      id: 'maha_sudarshana',
      title: 'Maha Sudarshana Homam',
      templeName: 'KANCHIPURAM',
      description: 'A powerful ancient ritual to seek protection and overcome obstacles, performed at the historic Varadharaja Perumal Temple.',
      priceRupees: 2501,
      category: 'Special',
      isFeatured: true,
      imageUrl: IMAGES.pujas.mahaSudarshana
    },
    {
      id: 'rudra_abhishekam',
      title: 'Rudra Abhishekam',
      templeName: 'VARANASI',
      description: 'Sacred bathing of the Shiva Lingam to invoke peace, health, and spiritual awakening at the Kashi Vishwanath Temple.',
      priceRupees: 1500,
      category: 'Daily',
      isFeatured: true,
      imageUrl: IMAGES.pujas.rudraAbhishekam
    }
  ];

  const pujas = data?.pujas?.length ? data.pujas : mockPujas;

  return (
    <div className="puja-page">
      {/* Immersive Hero */}
      <section className="puja-hero">
        <img 
          src={IMAGES.pujas.templeHero} 
          alt="Temple Interior" 
          className="hero-image"
        />
        <div className="hero-gradient"></div>
        <div className="hero-content content-section pb-stack-xl">
          <h2 className="typography-display-lg text-white mb-stack-sm text-shadow max-w-80">
            Devotion in Every Corner
          </h2>
          <p className="typography-body-lg text-white-opacity max-w-90 text-shadow">
            Discover and participate in sacred rituals from revered temples across India.
          </p>
        </div>
      </section>

      {/* Puja Categories */}
      <section className="puja-categories content-section py-stack-md">
        <div className="horizontal-scroll hide-scrollbar snap-x">
          <button className="category-chip active snap-start">
            <Sparkles size={20} className="text-primary" />
            <span>Special Occasions</span>
          </button>
          <button className="category-chip snap-start">
            <Sun size={20} className="text-primary" />
            <span>Daily Rituals</span>
          </button>
          <button className="category-chip snap-start">
            <Flame size={20} className="text-primary" />
            <span>Temple Vazhipadu</span>
          </button>
          <button className="category-chip snap-start">
            <User size={20} className="text-primary" />
            <span>Personalized Puja</span>
          </button>
        </div>
      </section>

      {/* Featured Pujas */}
      <section className="featured-pujas content-section mb-stack-xl">
        <div className="section-header">
          <h3 className="typography-headline-md text-primary">Featured Sevas</h3>
          <button className="typography-label-md text-secondary uppercase-btn">View All</button>
        </div>
        
        <div className="horizontal-scroll hide-scrollbar snap-x">
          {isLoading && <CardSkeleton count={2} />}
          {error && <p className="text-error">Error loading data.</p>}
          
          {!isLoading && !error && pujas.map((puja) => (
            <div key={puja.id} className="puja-card group snap-center">
              <div className="puja-image-container">
                <img 
                  src={puja.imageUrl} 
                  alt={puja.title} 
                  className="puja-image group-hover-scale"
                />
                <div className="puja-gradient"></div>
                
                <div className="puja-badges">
                  <span className="badge badge-glass uppercase">{puja.templeName}</span>
                </div>
                
                <div className="puja-card-content">
                  <h4 className="typography-headline-md text-white mb-2 leading-tight">
                    {puja.title}
                  </h4>
                  <p className="typography-body-md text-white-opacity line-clamp-2 mb-4">
                    {puja.description}
                  </p>
                  <button className="book-btn w-full typography-label-md uppercase">
                    Book Seva <ArrowRight size={18} />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
