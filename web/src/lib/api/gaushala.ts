import { fetchApi } from './client';

export interface Animal {
  id: string;
  name: string;
  gaushalaId: string;
  gaushalaName?: string;
  breed?: string;
  imageUrl?: string;
  needsSupport?: boolean;
  status?: string;
}

export interface Gaushala {
  id: string;
  name: string;
  city?: string;
  imageUrl?: string;
}

export async function getAnimals(gaushalaId?: string): Promise<{ animals: Animal[], count: number }> {
  const query = gaushalaId ? `?gaushalaId=${gaushalaId}` : '';
  return fetchApi<{ animals: Animal[], count: number }>(`/api/v1/catalog/animals${query}`);
}

export async function getGaushalas(city?: string): Promise<{ gaushalas: Gaushala[], count: number }> {
  const query = city ? `?city=${city}` : '';
  return fetchApi<{ gaushalas: Gaushala[], count: number }>(`/api/v1/catalog/gaushalas${query}`);
}
