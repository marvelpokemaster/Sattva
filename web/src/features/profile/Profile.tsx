import { useQuery } from '@tanstack/react-query';
import { getProfile, getDonations } from '@/lib/api/profile';
import { useAuth } from '@/features/auth/AuthContext';
import { Edit2, Star, Tent, Heart, ArrowRight, Dog, Leaf } from 'lucide-react';
import './Profile.css';

export function Profile() {
  const { user, signOut } = useAuth();
  
  const { data: profileData } = useQuery({
    queryKey: ['profile'],
    queryFn: () => getProfile(),
    enabled: !!user
  });

  const { data: donationsData } = useQuery({
    queryKey: ['donations'],
    queryFn: () => getDonations(),
    enabled: !!user
  });

  const profile = profileData?.profile || { displayName: user?.displayName || 'Seeker' };
  const donations = donationsData?.donations || [];

  const totalContributions = donations.reduce((sum, d) => sum + (d.paymentStatus === 'COMPLETED' ? d.amountRupees : 0), 0) || 12500; // Mock total if no completed donations

  return (
    <div className="profile-page pb-[100px] md:pb-0">
      <div className="profile-container content-section pt-stack-xl">
        {/* User Identity */}
        <section className="profile-identity flex-col-center mb-stack-xl relative">
          <div className="profile-avatar-container mb-stack-md">
            <img 
              src={user?.photoURL || "https://lh3.googleusercontent.com/aida-public/AB6AXuCLAkSLlnrrfmz-n5-SThAWAAuxKmF28TKKimti2kHsbwnknWyxsKRhMTBQ9SXjmQP68TwoOnXRtxfYBg95gkSM2vFFihTmmGpC8RUGPNR5wZQnEmNtsKvvSDJ5OuvXLmPpXiHoBHASS7CFHxvdQOgdJRNBjrcMRce91GfPMwIgtf3cz6gMOsHcu7eWIe77Ab5LiRQmxkYbJOverx_TL2ZSaJtbOV6Pmix-Uw4Fa9Mnwf0VxvHzI5I"} 
              alt="User Profile" 
              className="profile-avatar"
            />
            <button className="edit-btn">
              <Edit2 size={16} className="text-primary" />
            </button>
          </div>
          
          <h2 className="typography-headline-lg text-on-surface mb-stack-sm text-center">
            {profile.displayName}
          </h2>
          
          <div className="level-badge">
            <Star size={16} className="text-secondary fill-current" />
            <span className="typography-label-md text-secondary uppercase tracking-wider">Sattva Level: Seekers</span>
          </div>
        </section>

        {/* My Devotion (Pujas) */}
        <section className="devotion-section mb-stack-xl">
          <h3 className="section-title">
            <Tent size={24} className="text-primary-container" />
            My Devotion
          </h3>
          
          <div className="devotion-grid">
            {/* Upcoming */}
            <div className="devotion-card upcoming group">
              <div className="upcoming-accent transition-transform group-hover-scale"></div>
              <div className="devotion-card-header relative z-10">
                <span className="tag-upcoming">Upcoming</span>
                <span className="typography-label-sm text-variant">Tomorrow, 6:00 AM</span>
              </div>
              <h4 className="typography-body-lg font-medium text-on-surface mb-2 relative z-10">
                Maha Mrityunjaya Homa
              </h4>
              <p className="typography-body-md text-variant mb-stack-md relative z-10">
                For health and longevity
              </p>
              <button className="view-details-btn relative z-10">
                View Details <ArrowRight size={16} />
              </button>
            </div>

            {/* History */}
            <div className="devotion-history-card">
              <h4 className="typography-label-md text-on-surface mb-stack-sm uppercase tracking-wider">
                Recent History
              </h4>
              <ul className="history-list">
                <li className="history-item">
                  <div>
                    <p className="typography-body-md font-medium">Ganesh Puja</p>
                    <p className="typography-label-sm text-variant">Oct 12, 2023</p>
                  </div>
                  <span className="tag-completed">Completed</span>
                </li>
                <li className="history-item">
                  <div>
                    <p className="typography-body-md font-medium">Navagraha Shanti</p>
                    <p className="typography-label-sm text-variant">Sep 28, 2023</p>
                  </div>
                  <span className="tag-completed">Completed</span>
                </li>
              </ul>
            </div>
          </div>
        </section>

        {/* My Impact (Seva) */}
        <section className="impact-section mb-stack-xl">
          <h3 className="section-title">
            <Heart size={24} className="text-terracotta" />
            My Impact
          </h3>
          
          <div className="impact-summary-card">
            <div className="impact-bg"></div>
            <div className="impact-content relative z-10">
              <div className="impact-total">
                <p className="typography-label-md text-variant uppercase tracking-widest mb-2">Total Contributions</p>
                <p className="typography-display-lg text-on-surface">₹ {totalContributions.toLocaleString()}</p>
              </div>
              
              <div className="impact-stats-small">
                <div className="stat-pill">
                  <Dog size={20} className="text-primary mb-1" />
                  <span className="typography-label-sm text-variant">Animals Supported</span>
                  <span className="typography-body-lg font-semibold text-on-surface">4</span>
                </div>
                <div className="stat-pill">
                  <Leaf size={20} className="text-secondary mb-1" />
                  <span className="typography-label-sm text-variant">Meals Provided</span>
                  <span className="typography-body-lg font-semibold text-on-surface">120</span>
                </div>
              </div>
            </div>
            
            <div className="make-seva-container relative z-10">
              <button className="btn-make-seva w-full typography-label-md">Make a New Seva</button>
            </div>
          </div>
        </section>
        
        {/* Actions */}
        <section className="profile-actions mb-stack-xl flex justify-center">
           <button onClick={signOut} className="btn-outline typography-label-md text-error border-error">
             Sign Out
           </button>
        </section>
      </div>
    </div>
  );
}
