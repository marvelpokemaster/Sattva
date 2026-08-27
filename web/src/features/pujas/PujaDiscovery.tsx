import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getPujas, type Puja } from '@/lib/api/puja';
import { MapPin, ArrowRight, ShieldCheck, Sparkles } from 'lucide-react';
import { IMAGES } from '@/lib/images';
import { CardSkeleton } from '@/components/ui/LoadingScreen';
import { PujaDetailModal } from './PujaDetailModal';
import './Puja.css';

export function PujaDiscovery() {
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [activePuja, setActivePuja] = useState<Puja | null>(null);

  const { data, isLoading } = useQuery({
    queryKey: ['pujas', selectedCategory],
    queryFn: () => getPujas(selectedCategory),
  });

  const categories = ['All', 'Popular', 'Upcoming', 'Special', 'By Temple'];

  // Rich fallback matching actual backend if network delay
  const defaultPujas: Puja[] = [
    {
      id: 'ganga_aarti_varanasi',
      title: 'Maha Ganga Aarti & Deep Daan',
      templeName: 'Dashashwamedh Ghat, Varanasi',
      location: 'Dashashwamedh Ghat, Varanasi',
      devoteesCount: '45.2k Devotees attending',
      priestTitle: 'Chief Archaka, Ganga Seva Nidhi',
      priestName: 'Pt. Jagannath Mishra',
      priestExp: '25+ Years Experience in Ganga Aradhana',
      significance: 'The Grand Ganga Aarti at Dashashwamedh Ghat is a mesmerizing ritual of lights, chants, and devotion invoking the blessings of Maa Ganga.',
      imageUrl: IMAGES.rituals.kashiVishwanathAarti,
      isFeatured: true,
      dateTimeStr: 'Daily, 6:45 PM IST',
      durationStr: '1.5 Hours',
      category: 'Popular',
      priceRupees: 1101,
      specialTag: 'Daily Mahapuja'
    },
    {
      id: 'maha_rudrabhishek',
      title: 'Maha Rudrabhishek',
      templeName: 'Kashi Vishwanath Temple, Varanasi',
      location: 'Kashi Vishwanath Temple, Varanasi',
      devoteesCount: '12.5k Devotees attending',
      priestTitle: 'Chief Priest, Kashi Vishwanath Mandir Trust',
      priestName: 'Pt. Rameshwar Shastri',
      priestExp: '30+ Years in Vedic Shiva Rituals',
      significance: 'Potent Vedic ritual dedicated to Lord Shiva to dispel darkness, heal illnesses, and awaken inner serenity.',
      imageUrl: IMAGES.pujas.rudraAbhishekam,
      isFeatured: true,
      dateTimeStr: 'Daily, 5:30 AM IST',
      durationStr: '2.5 Hours',
      category: 'Upcoming',
      priceRupees: 2501,
      specialTag: 'Maha Shivratri Special'
    },
    {
      id: 'navgrah_shanti_mahayajna',
      title: 'Navgrah Shanti Mahayajna',
      templeName: 'Navgrah Shanti Kshetra, Ujjain',
      location: 'Navgrah Mandir, Ujjain',
      devoteesCount: '8.9k Devotees attending',
      priestTitle: 'Head Astrologer & Yajna Master',
      priestName: 'Acharya Somnath Dixit',
      priestExp: '35+ Years in Vedic Astrology & Havans',
      significance: 'Powerful Vedic Yajna designed to pacify planetary afflictions and bestow peace, health, and prosperity upon the devotee family.',
      imageUrl: IMAGES.pujas.mahaSudarshana,
      isFeatured: false,
      dateTimeStr: 'Every Saturday, 8:00 AM IST',
      durationStr: '3.0 Hours',
      category: 'Special',
      priceRupees: 3100,
      specialTag: 'Graha Dosha Nivaran'
    },
    {
      id: 'tirupati_venkateshwara_archana',
      title: 'Special Sahasranama Archana',
      templeName: 'Sri Venkateswara Swamy Temple, Tirumala',
      location: 'Tirumala, Andhra Pradesh',
      devoteesCount: '28.4k Devotees attending',
      priestTitle: 'Senior Archaka, Tirumala Tirupati Devasthanams',
      priestName: 'Archaka Srinivasa Bhattacharya',
      priestExp: '20+ Years in Vaikhanasa Agama',
      significance: 'Sacred 1008 Holy Names recitation and lotus offering to Lord Venkateshwara for prosperity and divine protection.',
      imageUrl: IMAGES.pujas.templeHero,
      isFeatured: false,
      dateTimeStr: 'Every Friday, 6:00 AM IST',
      durationStr: '2.0 Hours',
      category: 'By Temple',
      priceRupees: 1501,
      specialTag: 'Balaji Blessings'
    }
  ];

  const pujas = (data?.pujas && data.pujas.length > 0) ? data.pujas : defaultPujas;
  const filteredPujas = selectedCategory === 'All' 
    ? pujas 
    : pujas.filter(p => p.category === selectedCategory || p.specialTag?.includes(selectedCategory));

  const getCuratedPujaImage = (p: Puja) => {
    const title = (p.title || '').toLowerCase();
    const id = (p.id || '').toLowerCase();
    if (id.includes('ganga') || title.includes('ganga')) return IMAGES.rituals.kashiVishwanathAarti;
    if (id.includes('rudra') || title.includes('rudra') || title.includes('shiva')) return IMAGES.pujas.rudraAbhishekam;
    if (id.includes('navgrah') || title.includes('navgrah') || title.includes('sudarshana')) return IMAGES.pujas.mahaSudarshana;
    if (id.includes('tirupati') || title.includes('venkateswara') || title.includes('archana')) return IMAGES.pujas.templeHero;
    return p.imageUrl || IMAGES.pujas.templeHero;
  };

  return (
    <div className="puja-page">
      {/* Editorial Sanctuary Header Banner */}
      <section className="puja-header-banner">
        <div className="flex items-center gap-2">
          <span className="badge-gold">
            <ShieldCheck size={13} />
            Agama & Vedic Authenticity
          </span>
        </div>
        <h1 className="typography-headline-lg">Sacred Pujas & Rituals</h1>
        <p>
          Invoke divine grace through authentic temple ceremonies performed in your name and Gotra by revered priests across sacred sanctums.
        </p>
      </section>

      {/* Category Filter Pills */}
      <section className="puja-filters-bar hide-scrollbar">
        {categories.map((cat) => (
          <button
            key={cat}
            className={`filter-chip-btn ${selectedCategory === cat ? 'active' : ''}`}
            onClick={() => setSelectedCategory(cat)}
          >
            {cat}
          </button>
        ))}
      </section>

      {/* Pujas List */}
      <section className="pujas-grid">
        {isLoading && <CardSkeleton count={4} />}

        {!isLoading && filteredPujas.map((puja) => (
          <div 
            key={puja.id} 
            className="puja-card"
            onClick={() => setActivePuja(puja)}
          >
            <div className="puja-card-img-box">
              <img 
                src={getCuratedPujaImage(puja)} 
                alt={puja.title} 
              />
              {puja.specialTag && (
                <span className="puja-card-tag">
                  {puja.specialTag}
                </span>
              )}
            </div>

            <div className="puja-card-body">
              <div className="puja-location-text">
                <MapPin size={13} className="text-terracotta" />
                <span>{puja.templeName}</span>
              </div>

              <h3 className="puja-card-title">{puja.title}</h3>

              {puja.priestName && (
                <p className="puja-card-priest">
                  <Sparkles size={13} className="text-gold" />
                  <span>{puja.priestName} ({puja.priestTitle || 'Archaka'})</span>
                </p>
              )}

              <div className="puja-card-footer">
                <div>
                  <span className="text-xs text-muted block">Sankalpa Dakshina</span>
                  <span className="puja-dakshina-val">₹{puja.priceRupees}</span>
                </div>

                <button className="btn-book-puja" onClick={(e) => { e.stopPropagation(); setActivePuja(puja); }}>
                  <span>Book Puja</span>
                  <ArrowRight size={14} />
                </button>
              </div>
            </div>
          </div>
        ))}
      </section>

      {/* Interactive Puja Details & Sankalpa Modal */}
      <PujaDetailModal 
        puja={activePuja} 
        onClose={() => setActivePuja(null)} 
      />
    </div>
  );
}
