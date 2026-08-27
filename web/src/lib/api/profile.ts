import { fetchApi } from './client';

export interface Profile {
  id?: string;
  displayName?: string;
  city?: string;
  gotra?: string;
  nakshatra?: string;
}

export interface Donation {
  id: string;
  targetType: string;
  amountRupees: number;
  paymentStatus: string;
  targetName?: string;
  sevaCategory?: string;
  dedication?: string;
  taxExempt80G?: boolean;
  createdAt?: string;
}

export interface FamilyMember {
  id: string;
  name: string;
  relationship: string;
  rashi?: string;
  nakshatra?: string;
}

export async function getProfile(): Promise<{ profile: Profile }> {
  return fetchApi<{ profile: Profile }>('/api/v1/profile');
}

export async function updateProfile(profile: Partial<Profile>): Promise<{ success: boolean }> {
  return fetchApi<{ success: boolean }>('/api/v1/profile', {
    method: 'PUT',
    body: JSON.stringify(profile),
  });
}

export async function getDonations(): Promise<{ donations: Donation[] }> {
  return fetchApi<{ donations: Donation[] }>('/api/v1/donations');
}

export async function createDonation(donation: {
  amountRupees: number;
  targetType: string;
  targetName?: string;
  sevaCategory?: string;
  dedication?: string;
  taxExempt80G?: boolean;
}): Promise<{ success: boolean, donationId: string }> {
  return fetchApi<{ success: boolean, donationId: string }>('/api/v1/donations', {
    method: 'POST',
    body: JSON.stringify(donation),
  });
}

export async function getFamily(): Promise<{ family: FamilyMember[] }> {
  return fetchApi<{ family: FamilyMember[] }>('/api/v1/family');
}

export async function addFamilyMember(member: Omit<FamilyMember, 'id'>): Promise<{ success: boolean, memberId: string }> {
  return fetchApi<{ success: boolean, memberId: string }>('/api/v1/family', {
    method: 'POST',
    body: JSON.stringify(member),
  });
}
