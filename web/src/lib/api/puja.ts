import { fetchApi } from './client';

export interface Puja {
  id: string;
  title: string;
  templeName: string;
  priceRupees: number;
  category: string;
  isFeatured: boolean;
  imageUrl?: string;
  description?: string;
}

export async function getPujas(category?: string, search?: string): Promise<{ pujas: Puja[], count: number }> {
  const params = new URLSearchParams();
  if (category) params.append('category', category);
  if (search) params.append('search', search);
  
  const query = params.toString() ? `?${params.toString()}` : '';
  return fetchApi<{ pujas: Puja[], count: number }>(`/api/v1/catalog/pujas${query}`);
}
