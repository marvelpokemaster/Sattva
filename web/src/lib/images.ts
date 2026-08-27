const BASE_URL = (typeof (globalThis as any).process !== 'undefined' && (globalThis as any).process?.env?.EXPO_BASE_URL) || '';

export const IMAGES = {
  rituals: {
    kashiVishwanathAarti: `${BASE_URL}/images/rituals/kashi-vishwanath-aarti.jpg`,
  },
  animals: {
    nandini: `${BASE_URL}/images/animals/nandini.jpg`,
    gauri: `${BASE_URL}/images/animals/gauri.jpg`,
    nandi: `${BASE_URL}/images/animals/nandi.jpg`,
  },
  pujas: {
    templeHero: `${BASE_URL}/images/pujas/temple-hero.jpg`,
    mahaSudarshana: `${BASE_URL}/images/pujas/maha-sudarshana.jpg`,
    rudraAbhishekam: `${BASE_URL}/images/pujas/rudra-abhishekam.jpg`,
  },
  seva: {
    fodderMonsoon: `${BASE_URL}/images/seva/fodder-monsoon.jpg`,
    nourishment: `${BASE_URL}/images/seva/nourishment.jpg`,
    healing: `${BASE_URL}/images/seva/healing.jpg`,
    sanctuary: `${BASE_URL}/images/seva/sanctuary.jpg`,
  },
  profile: {
    defaultAvatar: `${BASE_URL}/images/profile/default-avatar.jpg`,
  },
  backgrounds: {
    authBg: `${BASE_URL}/images/backgrounds/auth-bg.jpg`,
    impactBg: `${BASE_URL}/images/backgrounds/impact-bg.jpg`,
  },
} as const;

export function getSafeImageUrl(url: string | undefined, fallback: string): string {
  if (!url) return fallback;
  return url;
}
