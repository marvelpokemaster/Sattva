import { fetchApi } from './client';

export interface Profile {
  id: string;
  displayName?: string;
  city?: string;
}

export interface Donation {
  id: string;
  targetType: string;
  amountRupees: number;
  paymentStatus: string;
  targetName?: string;
  sevaCategory?: string;
}

export async function getProfile(): Promise<{ profile: Profile }> {
  return fetchApi<{ profile: Profile }>('/api/v1/profile');
}

export async function getDonations(): Promise<{ donations: Donation[] }> {
  return fetchApi<{ donations: Donation[] }>('/api/v1/donations');
}
