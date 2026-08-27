import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { getAnimals } from '@/lib/api/gaushala';
import { Heart, MapPin, CheckCircle, ChevronDown, Filter } from 'lucide-react';
import './Gaushala.css';

export function GaushalaDiscovery() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['animals'],
    queryFn: () => getAnimals(),
  });

  // Mock data to match the Stitch design visually if API fails or returns empty
  const mockAnimals = [
    {
      id: '1',
      name: 'Nandini',
      breed: 'Vechur',
      gaushalaName: 'Govardhan Eco Village, Palghar',
      imageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuAWpO-hNWCs-38x81EB8pfgCs0K6t7Go7V2nm75hICMM0HzqrlisZRwugfxtogoVCEGi5IoCE8w4mUBWE_pvS_U85BdKUZtCi9483uorXoM-abjddeR41sKBQDCOPJ17GHMkVFjHhsQ55uW3GnLyCAYMeeVVbeiC-gTWZV_bBQesij9Vllr5Nlx0FiaTYWM2xLKCyHNR-TPTu-WLZB7et25-TQMIRNZt8y3HlMj5b9QqteQNM5DF1Q',
      needsSupport: true,
      status: 'Available'
    },
    {
      id: '2',
      name: 'Gauri',
      breed: 'Gir',
      gaushalaName: 'Pathmeda Godham, Rajasthan',
      imageUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuCtJiPx7ghRZtmYreQsTigtuSYVA3_jlqu7LAFN6c-5JD-_CExHmEZ8CvXHmaDpL6B_aVyEV69Bv3ybybxy4oGms-zEcQvu4E4QvCFa7uD5jP4uhulfDXzhoVaJxwikS1pQZK3Zej0K2SB3ePwkGbQXEQddXq0kyc6TtutJHVlxX2fAXd-cbkhciABv5TN72tQ-BZfRbaLmqzLPeWLCR3Y9uWeI8SHfP0rWrXPQ7sQDPH0CF3YE7HI',
      needsSupport: false,
      status: 'Adopted'
    }
  ];

  const animals = data?.animals?.length ? data.animals : mockAnimals;

  return (
    <div className="gaushala-page">
      <section className="gaushala-header content-section">
        <h2 className="typography-display-lg text-primary mb-2">Meet the Herd</h2>
        <p className="typography-body-lg text-variant max-w-2xl">
          Discover indigenous souls residing in sanctuaries across India. Each has a story, a lineage, and a need for connection.
        </p>
      </section>

      {/* Filters */}
      <section className="gaushala-filters sticky-top glass-surface">
        <div className="horizontal-scroll filter-scroll">
          <button className="filter-chip glass-surface text-primary">
            <Filter size={16} /> All
          </button>
          <button className="filter-chip active text-white">
            Seeking Support
          </button>
          <button className="filter-chip glass-surface text-primary">
            Breed <ChevronDown size={16} />
          </button>
          <button className="filter-chip glass-surface text-primary">
            Sanctuary <ChevronDown size={16} />
          </button>
        </div>
      </section>

      {/* Grid */}
      <section className="gaushala-grid content-section">
        {isLoading && <p>Loading animals...</p>}
        {error && <p className="text-error">Error loading data.</p>}
        
        {!isLoading && !error && animals.map((animal) => (
          <Link to={`/gaushala/animal/${animal.id}`} key={animal.id} className="animal-card group">
            <div className="animal-image-container">
              <img 
                src={animal.imageUrl} 
                alt={animal.name} 
                className="animal-image group-hover-scale"
              />
              <div className="animal-gradient"></div>
              
              <div className="animal-badges">
                <span className="badge badge-glass">{animal.breed}</span>
                {animal.needsSupport ? (
                  <span className="badge badge-terracotta">
                    <Heart size={14} fill="currentColor" /> Needs Support
                  </span>
                ) : (
                  <span className="badge badge-sandstone">
                    <CheckCircle size={14} /> {animal.status || 'Adopted'}
                  </span>
                )}
              </div>

              <div className="animal-content">
                <h3 className="typography-headline-lg text-white text-shadow">{animal.name}</h3>
                <div className="animal-location text-white-opacity">
                  <MapPin size={16} />
                  <span>{animal.gaushalaName}</span>
                </div>
              </div>
            </div>
          </Link>
        ))}
      </section>
      
      <div className="load-more-container">
        <button className="typography-label-md text-secondary border-b-secondary pb-1">
          Load More Residents
        </button>
      </div>
    </div>
  );
}
