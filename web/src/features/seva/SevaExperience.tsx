import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Heart, ShieldCheck, ArrowRight, Award, Sparkles, Utensils } from 'lucide-react';
import { getWelfareStats } from '@/lib/api/gaushala';
import { IMAGES } from '@/lib/images';
import { DonationModal } from './DonationModal';
import './Seva.css';

export function SevaExperience() {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedInitiative, setSelectedInitiative] = useState('Monsoon Green Fodder & Hay');
  const [selectedAmount, setSelectedAmount] = useState(501);

  const { data: stats } = useQuery({
    queryKey: ['welfareStats'],
    queryFn: () => getWelfareStats(),
  });

  const totalCows = stats?.totalRescued || 450;

  const initiatives = [
    {
      id: 'fodder',
      title: 'Monsoon Green Fodder & Nutritious Hay',
      price: 501,
      priceLabel: '₹501 / Day',
      image: IMAGES.seva.fodderMonsoon,
      desc: 'Provide fresh green hybrid Napier grass, organic jaggery, and dry sorghum stalks to nourish the cows during seasonal rains.',
      impact: 'Feeds 15 cows for an entire day with high-nutrition roughage'
    },
    {
      id: 'healing',
      title: 'Veterinary Healing & Herbal Medicine',
      price: 1101,
      priceLabel: '₹1,101 / Course',
      image: IMAGES.seva.healing,
      desc: 'Support compassionate medical treatments, ayurvedic joint oils, sterile bandaging, and antibiotic care for injured or elderly resident cattle.',
      impact: 'Full 7-day therapeutic recovery kit for one rescued resident'
    },
    {
      id: 'sanctuary',
      title: 'Sanctuary Shelter & Winter Bedding',
      price: 2501,
      priceLabel: '₹2,501 / Month',
      image: IMAGES.seva.sanctuary,
      desc: 'Maintain weather-proof shed roofing, clean drinking water borewells, organic dry straw bedding, and daily hawan mosquito repulsion.',
      impact: 'Shelters 5 mother cows with dry bedding and clean ventilation'
    }
  ];

  const handleOpenSeva = (initiativeTitle: string, defaultAmt: number) => {
    setSelectedInitiative(initiativeTitle);
    setSelectedAmount(defaultAmt);
    setModalOpen(true);
  };

  return (
    <div className="seva-page">
      {/* Seva Editorial Hero */}
      <section className="seva-hero-banner">
        <div className="flex items-center gap-2">
          <span className="badge-gold">
            <ShieldCheck size={13} />
            Direct Sanctuary Offering
          </span>
        </div>
        <h1 className="typography-headline-lg">
          Sacred Gau Seva Initiatives
        </h1>
        <p>
          Every rupee offered flows directly into daily food, medical care, and shelter for {totalCows} rescued cows at Shri Krishna Gaushala.
        </p>

        <div className="flex flex-wrap gap-4 mt-2">
          <button 
            className="btn-primary"
            onClick={() => handleOpenSeva('Monsoon Green Fodder & Hay', 1100)}
          >
            <Heart size={16} fill="currentColor" />
            <span>Make a Contribution</span>
          </button>
        </div>
      </section>

      {/* Initiatives List */}
      <section className="seva-initiatives-list">
        {initiatives.map((item) => (
          <div key={item.id} className="seva-card">
            <div className="seva-card-img-box">
              <img src={item.image} alt={item.title} />
            </div>

            <div className="seva-card-content">
              <div className="seva-card-header">
                <h3 className="seva-card-title">{item.title}</h3>
                <span className="seva-price-pill">{item.priceLabel}</span>
              </div>

              <p className="seva-card-desc">{item.desc}</p>

              <div className="flex items-center gap-2 text-xs text-tulsi font-medium">
                <Sparkles size={14} />
                <span>Impact: {item.impact}</span>
              </div>

              <div className="seva-card-footer">
                <span className="text-xs text-muted">Direct to Gaushala Ledger</span>
                <button 
                  className="btn-secondary"
                  onClick={() => handleOpenSeva(item.title, item.price)}
                >
                  <span>Contribute</span>
                  <ArrowRight size={14} />
                </button>
              </div>
            </div>
          </div>
        ))}
      </section>

      {/* Trust & Transparency Guarantee */}
      <section className="trust-guarantee-panel">
        <div className="trust-item">
          <div className="trust-icon-box">
            <ShieldCheck size={20} />
          </div>
          <div>
            <h4 className="trust-text-title">Direct Sanctuary Allocation</h4>
            <p className="trust-text-desc">
              All contributions directly support daily green fodder, veterinary medical kits, and shelter maintenance.
            </p>
          </div>
        </div>

        <div className="trust-item">
          <div className="trust-icon-box">
            <Award size={20} />
          </div>
          <div>
            <h4 className="trust-text-title">Digital Offering Receipts</h4>
            <p className="trust-text-desc">
              Receive an instant digital receipt and contribution reference recorded in your Devotee Profile.
            </p>
          </div>
        </div>

        <div className="trust-item">
          <div className="trust-icon-box">
            <Utensils size={20} />
          </div>
          <div>
            <h4 className="trust-text-title">Resident Care Updates</h4>
            <p className="trust-text-desc">
              Follow regular sanctuary welfare updates and recovery progress for sheltered resident cattle.
            </p>
          </div>
        </div>
      </section>

      {/* Interactive Contribution Modal */}
      <DonationModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        defaultInitiative={selectedInitiative}
        defaultAmount={selectedAmount}
      />
    </div>
  );
}
