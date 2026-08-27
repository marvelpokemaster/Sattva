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
  location?: string;
  dateTimeStr?: string;
  durationStr?: string;
  priestName?: string;
  priestTitle?: string;
  priestExp?: string;
  significance?: string;
  specialTag?: string;
  devoteesCount?: string;
}

export interface PujaBooking {
  id?: string;
  pujaId: string;
  pujaTitle: string;
  templeName: string;
  amountRupees: number;
  sankalpaName: string;
  gotra?: string;
  nakshatra?: string;
  familyMembers?: string[];
  status?: string;
  bookingDate?: string;
}

export async function getPujas(category?: string, search?: string): Promise<{ pujas: Puja[], count: number }> {
  const params = new URLSearchParams();
  if (category && category !== 'All') params.append('category', category);
  if (search) params.append('search', search);
  
  const query = params.toString() ? `?${params.toString()}` : '';
  return fetchApi<{ pujas: Puja[], count: number }>(`/api/v1/catalog/pujas${query}`);
}

export async function getBookings(): Promise<{ bookings: PujaBooking[] }> {
  return fetchApi<{ bookings: PujaBooking[] }>('/api/v1/bookings');
}

export async function createBooking(booking: Omit<PujaBooking, 'id' | 'status' | 'bookingDate'>): Promise<{ success: boolean, bookingId: string }> {
  return fetchApi<{ success: boolean, bookingId: string }>('/api/v1/bookings', {
    method: 'POST',
    body: JSON.stringify(booking),
  });
}
