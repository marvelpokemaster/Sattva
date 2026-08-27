import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { 
  LogOut, 
  ShieldCheck, 
  Plus, 
  MapPin
} from 'lucide-react';
import { getProfile, getDonations, getFamily, addFamilyMember, type Donation, type FamilyMember } from '@/lib/api/profile';
import { getBookings, type PujaBooking } from '@/lib/api/puja';
import { useAuth } from '@/features/auth/AuthContext';
import { IMAGES } from '@/lib/images';
import './Profile.css';

export function Profile() {
  const { user, signOut } = useAuth();
  const [activeTab, setActiveTab] = useState<'seva' | 'pujas' | 'family' | 'settings'>('seva');
  const [showAddFamily, setShowAddFamily] = useState(false);
  const [memberName, setMemberName] = useState('');
  const [memberRelation, setMemberRelation] = useState('Spouse');
  const [memberNakshatra, setMemberNakshatra] = useState('');

  const { data: profileData } = useQuery({
    queryKey: ['profile'],
    queryFn: () => getProfile(),
    enabled: !!user,
  });

  const { data: donationsData } = useQuery({
    queryKey: ['donations'],
    queryFn: () => getDonations(),
    enabled: !!user,
  });

  const { data: bookingsData } = useQuery({
    queryKey: ['bookings'],
    queryFn: () => getBookings(),
    enabled: !!user,
  });

  const { data: familyData, refetch: refetchFamily } = useQuery({
    queryKey: ['family'],
    queryFn: () => getFamily(),
    enabled: !!user,
  });

  const profile = profileData?.profile || {
    displayName: user?.displayName || 'Devotee Arjun',
    city: 'Mathura / Vrindavan',
    gotra: 'Kashyapa',
    nakshatra: 'Mrigashirsha'
  };

  const defaultDonations: Donation[] = [
    {
      id: 'SEVA-882194',
      targetType: 'ANIMAL',
      targetName: 'Nandi (Injured Calf Care)',
      sevaCategory: 'Gau Seva & Healing',
      amountRupees: 1500,
      paymentStatus: 'COMPLETED',
      taxExempt80G: true,
      createdAt: 'Aug 24, 2026'
    },
    {
      id: 'SEVA-741029',
      targetType: 'SEVA_INITIATIVE',
      targetName: 'Monsoon Green Fodder & Hay',
      sevaCategory: 'Fodder Nourishment',
      amountRupees: 1100,
      paymentStatus: 'COMPLETED',
      taxExempt80G: true,
      createdAt: 'Aug 18, 2026'
    }
  ];

  const donations = (donationsData?.donations && donationsData.donations.length > 0)
    ? donationsData.donations 
    : defaultDonations;

  const defaultBookings: PujaBooking[] = [
    {
      id: 'BK-10928',
      pujaId: 'ganga_aarti_varanasi',
      pujaTitle: 'Maha Ganga Aarti & Deep Daan',
      templeName: 'Dashashwamedh Ghat, Varanasi',
      amountRupees: 1101,
      sankalpaName: profile.displayName || 'Devotee Arjun',
      gotra: profile.gotra || 'Kashyapa',
      status: 'CONFIRMED',
      bookingDate: 'Aug 28, 2026 • 6:45 PM'
    }
  ];

  const bookings = (bookingsData?.bookings && bookingsData.bookings.length > 0)
    ? bookingsData.bookings 
    : defaultBookings;

  const defaultFamily: FamilyMember[] = [
    { id: '1', name: 'Priya Sharma', relationship: 'Spouse', nakshatra: 'Rohini' },
    { id: '2', name: 'Aarav Sharma', relationship: 'Son', nakshatra: 'Pushya' }
  ];

  const family = (familyData?.family && familyData.family.length > 0)
    ? familyData.family 
    : defaultFamily;

  const totalContributions = donations.reduce((sum, d) => sum + (d.amountRupees || 0), 0);

  const handleAddFamily = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!memberName.trim()) return;

    try {
      await addFamilyMember({
        name: memberName.trim(),
        relationship: memberRelation,
        nakshatra: memberNakshatra.trim() || undefined,
      });
      setShowAddFamily(false);
      setMemberName('');
      refetchFamily();
    } catch (err) {
      setShowAddFamily(false);
    }
  };

  return (
    <div className="profile-page">
      {/* Devotee Header Identity Card */}
      <section className="devotee-profile-card">
        <div className="devotee-avatar-box">
          <img 
            src={user?.photoURL || IMAGES.profile.defaultAvatar} 
            alt={profile.displayName || 'Devotee'} 
          />
        </div>

        <h2 className="devotee-name-title">
          {profile.displayName || user?.displayName || 'Devotee Arjun'}
        </h2>
        <p className="devotee-tagline">
          {user?.email || 'devotee@sattva.org'}
        </p>

        <div className="devotee-badges-row">
          <span className="badge-gold">
            Gotra: {profile.gotra || 'Kashyapa'}
          </span>
          <span className="badge-tulsi">
            Nakshatra: {profile.nakshatra || 'Mrigashirsha'}
          </span>
          <span className="badge-gold">
            <MapPin size={12} />
            {profile.city || 'Vrindavan'}
          </span>
        </div>
      </section>

      {/* Profile Multi-Tab Selector */}
      <div className="profile-nav-tabs hide-scrollbar">
        <button 
          className={`profile-tab-btn ${activeTab === 'seva' ? 'active' : ''}`}
          onClick={() => setActiveTab('seva')}
        >
          My Seva (₹{totalContributions.toLocaleString()})
        </button>

        <button 
          className={`profile-tab-btn ${activeTab === 'pujas' ? 'active' : ''}`}
          onClick={() => setActiveTab('pujas')}
        >
          Puja Bookings ({bookings.length})
        </button>

        <button 
          className={`profile-tab-btn ${activeTab === 'family' ? 'active' : ''}`}
          onClick={() => setActiveTab('family')}
        >
          Sankalpa Family ({family.length})
        </button>

        <button 
          className={`profile-tab-btn ${activeTab === 'settings' ? 'active' : ''}`}
          onClick={() => setActiveTab('settings')}
        >
          Settings
        </button>
      </div>

      {/* Tab Panels */}
      {activeTab === 'seva' && (
        <div className="profile-content-panel">
          {donations.map((d) => (
            <div key={d.id} className="activity-item-card">
              <div>
                <h4 className="activity-meta-title">{d.targetName || 'Gau Seva Offering'}</h4>
                <p className="activity-meta-sub">
                  Ref: {d.id} • {d.createdAt || 'Recent'} • 
                  <span className="text-tulsi ml-1 font-semibold">{d.paymentStatus}</span>
                </p>
                {d.dedication && (
                  <p className="text-xs text-text-secondary italic mt-1">
                    "{d.dedication}"
                  </p>
                )}
              </div>

              <div className="text-right">
                <span className="font-serif font-bold text-lg text-terracotta block">
                  ₹{d.amountRupees}
                </span>
                <span className="text-xs text-gold flex items-center justify-end gap-1 mt-1">
                  <ShieldCheck size={12} /> 80G Receipt
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {activeTab === 'pujas' && (
        <div className="profile-content-panel">
          {bookings.map((b) => (
            <div key={b.id} className="activity-item-card">
              <div>
                <div className="badge-gold self-start mb-1 text-xs">
                  {b.status || 'CONFIRMED'}
                </div>
                <h4 className="activity-meta-title">{b.pujaTitle}</h4>
                <p className="activity-meta-sub">
                  {b.templeName} • {b.bookingDate || 'Scheduled Daily'}
                </p>
                <p className="text-xs text-text-secondary mt-1">
                  Chanted for: <strong>{b.sankalpaName}</strong> (Gotra: {b.gotra || 'Self'})
                </p>
              </div>

              <div className="text-right">
                <span className="font-serif font-bold text-base text-terracotta">
                  ₹{b.amountRupees}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {activeTab === 'family' && (
        <div className="profile-content-panel">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-semibold uppercase text-muted tracking-wider">
              Chanted in Sankalpas
            </span>
            <button 
              className="btn-secondary text-xs py-1.5 px-3 flex items-center gap-1"
              onClick={() => setShowAddFamily(!showAddFamily)}
            >
              <Plus size={14} />
              <span>Add Member</span>
            </button>
          </div>

          {showAddFamily && (
            <form onSubmit={handleAddFamily} className="temple-card p-4 flex flex-col gap-3">
              <div className="form-group">
                <label className="form-label">Full Name</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Radhika Sharma"
                  className="form-input"
                  value={memberName}
                  onChange={(e) => setMemberName(e.target.value)}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div className="form-group">
                  <label className="form-label">Relationship</label>
                  <select
                    className="form-input"
                    value={memberRelation}
                    onChange={(e) => setMemberRelation(e.target.value)}
                  >
                    <option value="Spouse">Spouse</option>
                    <option value="Child">Child</option>
                    <option value="Parent">Parent</option>
                    <option value="Sibling">Sibling</option>
                  </select>
                </div>

                <div className="form-group">
                  <label className="form-label">Nakshatra</label>
                  <input
                    type="text"
                    placeholder="e.g. Ashwini"
                    className="form-input"
                    value={memberNakshatra}
                    onChange={(e) => setMemberNakshatra(e.target.value)}
                  />
                </div>
              </div>

              <div className="flex gap-2 justify-end mt-2">
                <button 
                  type="button" 
                  className="btn-secondary text-xs py-2 px-3"
                  onClick={() => setShowAddFamily(false)}
                >
                  Cancel
                </button>
                <button type="submit" className="btn-primary text-xs py-2 px-4">
                  Save Member
                </button>
              </div>
            </form>
          )}

          {family.map((f) => (
            <div key={f.id} className="family-member-chip">
              <div>
                <h5 className="font-semibold text-sm text-text-primary">{f.name}</h5>
                <p className="text-xs text-text-muted">
                  {f.relationship} {f.nakshatra ? `• Nakshatra: ${f.nakshatra}` : ''}
                </p>
              </div>
              <span className="badge-tulsi text-xs">Included in Chants</span>
            </div>
          ))}
        </div>
      )}

      {activeTab === 'settings' && (
        <div className="profile-content-panel">
          <div className="activity-item-card">
            <div>
              <h4 className="activity-meta-title">Sacred Notifications</h4>
              <p className="activity-meta-sub">Daily Panchang, Muhurat & live Aarti updates</p>
            </div>
            <span className="badge-tulsi">Active</span>
          </div>

          <div className="activity-item-card">
            <div>
              <h4 className="activity-meta-title">Tax Exemption Profile</h4>
              <p className="activity-meta-sub">Automated 80G receipts compiled annually</p>
            </div>
            <span className="badge-gold">Configured</span>
          </div>

          <button className="btn-signout" onClick={signOut}>
            <LogOut size={16} />
            <span>Sign Out of Sattva Sanctuary</span>
          </button>
        </div>
      )}
    </div>
  );
}
