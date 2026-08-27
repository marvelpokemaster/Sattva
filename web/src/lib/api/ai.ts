import { fetchApi } from './client';

export async function askRishi(prompt: string): Promise<{ answer: string }> {
  return fetchApi<{ answer: string }>('/api/v1/ai/ask', {
    method: 'POST',
    body: JSON.stringify({ prompt }),
  });
}
