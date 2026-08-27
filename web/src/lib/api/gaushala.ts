import { fetchApi } from './client';

export interface Animal {
  id: string;
  name: string;
  gaushalaId?: string;
  gaushalaName?: string;
  breed?: string;
  imageUrl?: string;
  needsSupport?: boolean;
  status?: string;
  ageStr?: string;
  healthStatus?: string;
  healthDescription?: string;
  monthlyGoalRupees?: number;
  raisedRupees?: number;
  isUrgent?: boolean;
  story?: string;
}

export interface Gaushala {
  id: string;
  name: string;
  location?: string;
  city?: string;
  state?: string;
  imageUrl?: string;
  animalsRescuedCount?: number;
  trustScorePercent?: number;
  transparencyTier?: string;
  shelterPercent?: number;
  fodderPercent?: number;
  medicalPercent?: number;
  missionQuote?: string;
  updatesCount?: number;
}

export interface WelfareStats {
  totalRescued: number;
  activeSanctuaries: number;
  totalMealsServed: number;
}

export async function getAnimals(gaushalaId?: string): Promise<{ animals: Animal[], count: number }> {
  const query = gaushalaId ? `?gaushalaId=${gaushalaId}` : '';
  return fetchApi<{ animals: Animal[], count: number }>(`/api/v1/catalog/animals${query}`);
}

export async function getGaushalas(city?: string): Promise<{ gaushalas: Gaushala[], count: number }> {
  const query = city ? `?city=${city}` : '';
  return fetchApi<{ gaushalas: Gaushala[], count: number }>(`/api/v1/catalog/gaushalas${query}`);
}

export async function getWelfareStats(): Promise<WelfareStats> {
  return fetchApi<WelfareStats>('/api/v1/welfare');
}
