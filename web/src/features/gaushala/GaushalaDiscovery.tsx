import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { MapPin, ShieldCheck, Award } from 'lucide-react';
import { getGaushalas, getAnimals, type Gaushala, type Animal } from '@/lib/api/gaushala';
import { IMAGES } from '@/lib/images';
import { CardSkeleton } from '@/components/ui/LoadingScreen';
import './Gaushala.css';

export function GaushalaDiscovery() {
  const { data: gaushalaData, isLoading: gaushalaLoading } = useQuery({
    queryKey: ['gaushalas'],
    queryFn: () => getGaushalas(),
  });

  const { data: animalData, isLoading: animalLoading } = useQuery({
    queryKey: ['animals'],
    queryFn: () => getAnimals(),
  });

  const defaultGaushala: Gaushala = {
    id: 'shri_krishna_gaushala',
    name: 'Shri Krishna Gaushala',
    location: 'Vrindavan, Mathura District',
    state: 'Uttar Pradesh',
    imageUrl: IMAGES.seva.sanctuary,
    animalsRescuedCount: 450,
    trustScorePercent: 98,
    transparencyTier: 'Gold Tier',
    shelterPercent: 85,
    fodderPercent: 65,
    medicalPercent: 40,
    missionQuote: 'Providing a lifelong, loving sanctuary and restorative herbal care for abandoned and injured cows.'
  };

  const gaushala = gaushalaData?.gaushalas?.[0] || defaultGaushala;

  const defaultAnimals: Animal[] = [
    {
      id: 'nandi_01',
      name: 'Nandi',
      breed: 'Vechur / Desi',
      ageStr: '3.5 Years',
      healthStatus: 'Recovering',
      healthDescription: 'Fractured left leg, healing with ayurvedic poultice.',
      monthlyGoalRupees: 5000,
      raisedRupees: 3250,
      imageUrl: IMAGES.animals.nandi,
      needsSupport: true,
    },
    {
      id: 'nandini_02',
      name: 'Nandini',
      breed: 'Vechur Dwarf',
      ageStr: '4.0 Years',
      healthStatus: 'Healthy',
      healthDescription: 'Gentle sanctuary mother, thriving on fresh green Napier grass.',
      monthlyGoalRupees: 4000,
      raisedRupees: 3800,
      imageUrl: IMAGES.animals.nandini,
      needsSupport: false,
    },
    {
      id: 'gauri_03',
      name: 'Gauri',
      breed: 'Gir Cow',
      ageStr: '5.2 Years',
      healthStatus: 'Healthy',
      healthDescription: 'Majestic horned Gir cow, fully adopted and flourishing.',
      monthlyGoalRupees: 6000,
      raisedRupees: 6000,
      imageUrl: IMAGES.animals.gauri,
      needsSupport: false,
    }
  ];

  const animals = (animalData?.animals && animalData.animals.length > 0) ? animalData.animals : defaultAnimals;

  const getCuratedAnimalImage = (a: Animal) => {
    const name = (a.name || '').toLowerCase();
    const id = (a.id || '').toLowerCase();
    if (id.includes('nandini') || name.includes('nandini')) return IMAGES.animals.nandini;
    if (id.includes('gauri') || name.includes('gauri')) return IMAGES.animals.gauri;
    if (id.includes('nandi') || name.includes('nandi')) return IMAGES.animals.nandi;
    return a.imageUrl?.startsWith('/images') ? a.imageUrl : IMAGES.animals.nandi;
  };

  return (
    <div className="gaushala-page">
      {/* Featured Sanctuary Spotlight */}
      <section className="sanctuary-spotlight-card">
        <div className="sanctuary-hero-img-box">
          <img 
            src={gaushala.imageUrl || IMAGES.seva.sanctuary} 
            alt={gaushala.name} 
          />
          <div className="sanctuary-badge-tag">
            <ShieldCheck size={14} className="text-gold" />
            <span>{gaushala.transparencyTier || 'Gold Tier'}</span>
          </div>
        </div>

        <div className="sanctuary-details-body">
          <div className="sanctuary-meta-row">
            <span className="flex items-center gap-1 text-xs text-muted font-medium">
              <MapPin size={13} className="text-terracotta" />
              {gaushala.location || 'Vrindavan, Uttar Pradesh'}
            </span>
            <span className="badge-gold">
              <Award size={12} />
              {gaushala.trustScorePercent || 98}% Trust Score
            </span>
          </div>

          <h2 className="sanctuary-title">{gaushala.name}</h2>

          <p className="sanctuary-quote">
            "{gaushala.missionQuote || 'Serving sacred cattle with lifelong devotion, organic fodder, and transparent veterinary welfare.'}"
          </p>

          {/* Transparency Breakdown Meters */}
          <div className="transparency-breakdown">
            <div className="transparency-meter">
              <span className="meter-label">Shelter Capacity</span>
              <div className="meter-bar-track">
                <div className="meter-bar-fill" style={{ width: `${gaushala.shelterPercent || 85}%` }} />
              </div>
              <span className="meter-pct">{gaushala.shelterPercent || 85}% Fulfilled</span>
            </div>

            <div className="transparency-meter">
              <span className="meter-label">Green Fodder</span>
              <div className="meter-bar-track">
                <div className="meter-bar-fill" style={{ width: `${gaushala.fodderPercent || 65}%` }} />
              </div>
              <span className="meter-pct">{gaushala.fodderPercent || 65}% Sourced</span>
            </div>

            <div className="transparency-meter">
              <span className="meter-label">Medical Fund</span>
              <div className="meter-bar-track">
                <div className="meter-bar-fill" style={{ width: `${gaushala.medicalPercent || 40}%` }} />
              </div>
              <span className="meter-pct">{gaushala.medicalPercent || 40}% Funded</span>
            </div>
          </div>
        </div>
      </section>

      {/* Resident Cattle Herd Showcase */}
      <div className="herd-section-header">
        <div>
          <span className="typography-label-sm text-terracotta">Resident Souls</span>
          <h3 className="herd-section-title">Meet the Vrindavan Herd</h3>
        </div>
        <span className="text-xs text-muted">
          {animals.length} Resident Cattle
        </span>
      </div>

      <section className="herd-grid">
        {(gaushalaLoading || animalLoading) && <CardSkeleton count={3} />}

        {!gaushalaLoading && !animalLoading && animals.map((animal) => {
          const raised = animal.raisedRupees || 3250;
          const goal = animal.monthlyGoalRupees || 5000;
          const pct = Math.min(100, Math.round((raised / goal) * 100));

          return (
            <Link 
              to={`/gaushala/animal/${animal.id}`} 
              key={animal.id} 
              className="animal-card-compact"
            >
              <div className="animal-thumb-box">
                <img 
                  src={getCuratedAnimalImage(animal)} 
                  alt={animal.name} 
                />
                <span className={`animal-status-chip ${animal.healthStatus === 'Recovering' ? 'status-recovering' : 'status-healthy'}`}>
                  {animal.healthStatus || 'Sanctuary Resident'}
                </span>
              </div>

              <div className="animal-info-body">
                <div className="flex items-center justify-between">
                  <h4 className="animal-name">{animal.name}</h4>
                  <span className="text-xs text-muted">{animal.breed || 'Desi'}</span>
                </div>

                <p className="animal-sub-desc line-clamp-2">
                  {animal.healthDescription || 'Loving resident cow residing at the Vrindavan sanctuary.'}
                </p>

                <div className="animal-progress-wrap">
                  <div className="animal-progress-text">
                    <span>Care Goal: ₹{raised} / ₹{goal}</span>
                    <span className="font-semibold text-terracotta">{pct}%</span>
                  </div>
                  <div className="meter-bar-track">
                    <div className="meter-bar-fill" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              </div>
            </Link>
          );
        })}
      </section>
    </div>
  );
}
