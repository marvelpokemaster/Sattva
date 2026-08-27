import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { getAnimals } from '@/lib/api/gaushala';
import { ArrowLeft, Share2, ShieldCheck } from 'lucide-react';
import './AnimalPassport.css';
import { IMAGES } from '@/lib/images';
import { LoadingScreen } from '@/components/ui/LoadingScreen';

export function AnimalPassport() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data, isLoading } = useQuery({
    queryKey: ['animals'],
    queryFn: () => getAnimals(),
  });

  const animal = data?.animals?.find(a => a.id === id) || {
    id: id,
    name: 'Nandi',
    breed: 'Vechur',
    gaushalaName: 'Sattva Gaushala, Kerala',
    imageUrl: IMAGES.animals.nandi,
    status: 'Needs Support'
  };

  if (isLoading) {
    return <LoadingScreen message="Accessing Cattle Passport..." subtext="Retrieving lineage and welfare records" />;
  }

  return (
    <div className="passport-page">
      {/* App Bar overlay */}
      <header className="passport-app-bar">
        <button onClick={() => navigate(-1)} className="icon-btn text-white glass-surface">
          <ArrowLeft size={20} />
        </button>
        <button className="icon-btn text-white glass-surface">
          <Share2 size={20} />
        </button>
      </header>

      {/* Hero Section */}
      <section className="passport-hero">
        <img src={animal.imageUrl} alt={animal.name} className="hero-image" />
        <div className="hero-gradient"></div>
        <div className="hero-content content-section pb-stack-lg">
          <div className="hero-badges mb-4">
            <span className="badge badge-glass">
              <ShieldCheck size={14} /> Verified Origin
            </span>
            <span className="badge badge-glass">Kerala, India</span>
          </div>
          <h1 className="typography-display-lg text-white mb-2">{animal.name}</h1>
          <p className="typography-headline-md text-white-opacity italic mb-6">
            Indigenous {animal.breed} Breed
          </p>

          <div className="stats-grid">
            <div className="stat-card glass-surface border-l-secondary">
              <p className="typography-label-sm text-white-opacity uppercase">Age</p>
              <p className="typography-body-lg text-white">4 Years</p>
            </div>
            <div className="stat-card glass-surface border-l-secondary">
              <p className="typography-label-sm text-white-opacity uppercase">Health</p>
              <p className="typography-body-lg text-white">Excellent</p>
            </div>
            <div className="stat-card glass-surface border-l-secondary">
              <p className="typography-label-sm text-white-opacity uppercase">Lineage</p>
              <p className="typography-body-lg text-white">Purebred</p>
            </div>
          </div>
        </div>
      </section>

      {/* Details Section */}
      <section className="passport-details content-section py-stack-xl">
        <h2 className="typography-headline-lg text-primary mb-stack-md">The Sanctuary Story</h2>
        
        <div className="story-layout">
          <div className="story-text">
            <p className="typography-body-lg text-variant mb-4 leading-relaxed">
              {animal.name} represents a vital link to India's agricultural heritage. As a purebred {animal.breed}, conservation is paramount. Known for their resilience and quality, indigenous breeds were once on the brink of extinction.
            </p>
            <p className="typography-body-md text-variant opacity-80">
              Rescued from an urban area, {animal.name} now thrives in the tranquil environments of {animal.gaushalaName}. The calm demeanor and curious nature make this soul a favorite among the Seva volunteers.
            </p>
          </div>
          
          <div className="story-sidebar">
            <div className="status-card bg-surface-container">
              <h3 className="typography-label-md text-primary uppercase mb-2 tracking-widest">Conservation Status</h3>
              <p className="typography-headline-md text-terracotta mb-1">Critical</p>
              <p className="typography-label-sm text-variant">Globally recognized rare breed.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Floating Actions */}
      <div className="floating-actions-container">
        <div className="floating-actions glass-dark">
          <button className="action-btn-outline typography-label-md">Support</button>
          <button className="action-btn-filled typography-label-md text-primary">Adopt {animal.name}</button>
        </div>
      </div>
    </div>
  );
}
