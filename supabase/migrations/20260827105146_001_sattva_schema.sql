/*
# Sattva Prototype Schema

1. Overview
- Creates the minimum tables needed for the current Sattva prototype screens.
- Auth lives in Firebase (not Supabase), so user-owned rows are keyed by
  `firebase_uid text` — the verified Firebase identity derived server-side
  by the sattva-api edge function.
- Public catalog tables: gaushalas, animals, welfare_updates, pujas.
- User-owned tables: profiles, seva_contributions, puja_bookings, family_members.

2. New Tables
- profiles: one row per Firebase user (firebase_uid, display_name, city, photo_url, totals).
- gaushalas: sanctuary catalog (name, city, image, trust/audit fields, counts).
- animals: residents belonging to a gaushala (gaushala_id FK, breed, status, image).
- welfare_updates: impact updates tied to a gaushala and/or animal.
- pujas: ritual catalog (title, temple, price, category, image).
- seva_contributions: user-owned donation records (firebase_uid, target, amount, status).
- puja_bookings: user-owned puja bookings (firebase_uid, puja, sankalpa, dates, status).
- family_members: user-owned family members for Sankalpa (firebase_uid, name, relation, gotra).

3. Relationships
- gaushalas 1→N animals
- animals 1→N welfare_updates (optional)
- gaushalas 1→N welfare_updates (optional)
- profiles keyed by firebase_uid (no FK to auth.users — auth is Firebase)

4. Security
- RLS enabled on every table.
- Catalog tables: read-only to anon + authenticated (public content).
- User-owned tables: owner-scoped by firebase_uid. Because the Supabase client
  uses the anon key, RLS on user tables is effectively locked to the service-role
  path used by the edge function; policies are written for `authenticated` but the
  real enforcement is that only the edge function (service role) touches these
  tables, scoping by the verified Firebase UID.
- No table allows client-supplied UID as proof of identity — the edge function
  derives firebase_uid from the verified Firebase ID token.

5. Notes
- All image columns store URLs/paths, never binaries.
- Demo seed rows are inserted in migration 002.
*/

-- profiles (one per Firebase user)
CREATE TABLE IF NOT EXISTS profiles (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  firebase_uid text UNIQUE NOT NULL,
  display_name text,
  city text,
  photo_url text,
  total_contributed integer NOT NULL DEFAULT 0,
  puja_count integer NOT NULL DEFAULT 0,
  is_verified_admin boolean NOT NULL DEFAULT false,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "profiles_select_own" ON profiles;
CREATE POLICY "profiles_select_own" ON profiles FOR SELECT
  TO authenticated USING (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "profiles_insert_own" ON profiles;
CREATE POLICY "profiles_insert_own" ON profiles FOR INSERT
  TO authenticated WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "profiles_update_own" ON profiles;
CREATE POLICY "profiles_update_own" ON profiles FOR UPDATE
  TO authenticated USING (auth.uid()::text = firebase_uid) WITH CHECK (auth.uid()::text = firebase_uid);

-- gaushalas (public catalog)
CREATE TABLE IF NOT EXISTS gaushalas (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL,
  city text,
  location text,
  description text,
  image_url text,
  trust_score numeric,
  audit_tier text,
  animals_rescued_count integer NOT NULL DEFAULT 0,
  is_active boolean NOT NULL DEFAULT true,
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE gaushalas ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "gaushalas_read_public" ON gaushalas;
CREATE POLICY "gaushalas_read_public" ON gaushalas FOR SELECT
  TO anon, authenticated USING (true);

-- animals (public catalog, belongs to gaushala)
CREATE TABLE IF NOT EXISTS animals (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  gaushala_id uuid NOT NULL REFERENCES gaushalas(id) ON DELETE CASCADE,
  name text NOT NULL,
  breed text,
  gaushala_name text,
  image_url text,
  needs_support boolean NOT NULL DEFAULT false,
  status text NOT NULL DEFAULT 'Available',
  age_years integer,
  health_status text,
  lineage text,
  story text,
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE animals ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "animals_read_public" ON animals;
CREATE POLICY "animals_read_public" ON animals FOR SELECT
  TO anon, authenticated USING (true);

CREATE INDEX IF NOT EXISTS idx_animals_gaushala_id ON animals(gaushala_id);

-- welfare_updates (public catalog, tied to gaushala and/or animal)
CREATE TABLE IF NOT EXISTS welfare_updates (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  gaushala_id uuid REFERENCES gaushalas(id) ON DELETE CASCADE,
  animal_id uuid REFERENCES animals(id) ON DELETE CASCADE,
  title text NOT NULL,
  body text,
  image_url text,
  meals_served integer NOT NULL DEFAULT 0,
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE welfare_updates ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "welfare_read_public" ON welfare_updates;
CREATE POLICY "welfare_read_public" ON welfare_updates FOR SELECT
  TO anon, authenticated USING (true);

CREATE INDEX IF NOT EXISTS idx_welfare_gaushala_id ON welfare_updates(gaushala_id);
CREATE INDEX IF NOT EXISTS idx_welfare_animal_id ON welfare_updates(animal_id);

-- pujas (public catalog)
CREATE TABLE IF NOT EXISTS pujas (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  title text NOT NULL,
  temple_name text,
  location text,
  description text,
  price_rupees integer NOT NULL DEFAULT 0,
  category text NOT NULL DEFAULT 'Special',
  is_featured boolean NOT NULL DEFAULT false,
  image_url text,
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE pujas ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "pujas_read_public" ON pujas;
CREATE POLICY "pujas_read_public" ON pujas FOR SELECT
  TO anon, authenticated USING (true);

CREATE INDEX IF NOT EXISTS idx_pujas_category ON pujas(category);
CREATE INDEX IF NOT EXISTS idx_pujas_featured ON pujas(is_featured);

-- seva_contributions (user-owned, keyed by firebase_uid)
CREATE TABLE IF NOT EXISTS seva_contributions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  firebase_uid text NOT NULL,
  target_type text NOT NULL DEFAULT 'GAUSHALA',
  target_id text,
  target_name text,
  amount_rupees integer NOT NULL DEFAULT 0,
  seva_category text,
  payment_status text NOT NULL DEFAULT 'PENDING',
  date_str text NOT NULL DEFAULT to_char(now(), 'YYYY-MM-DD'),
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE seva_contributions ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "seva_select_own" ON seva_contributions;
CREATE POLICY "seva_select_own" ON seva_contributions FOR SELECT
  TO authenticated USING (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "seva_insert_own" ON seva_contributions;
CREATE POLICY "seva_insert_own" ON seva_contributions FOR INSERT
  TO authenticated WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "seva_update_own" ON seva_contributions;
CREATE POLICY "seva_update_own" ON seva_contributions FOR UPDATE
  TO authenticated USING (auth.uid()::text = firebase_uid) WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "seva_delete_own" ON seva_contributions;
CREATE POLICY "seva_delete_own" ON seva_contributions FOR DELETE
  TO authenticated USING (auth.uid()::text = firebase_uid);

CREATE INDEX IF NOT EXISTS idx_seva_firebase_uid ON seva_contributions(firebase_uid);

-- puja_bookings (user-owned, keyed by firebase_uid)
CREATE TABLE IF NOT EXISTS puja_bookings (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  firebase_uid text NOT NULL,
  puja_id text,
  devotee_name text,
  gotra text,
  booking_date_str text NOT NULL DEFAULT to_char(now(), 'YYYY-MM-DD'),
  scheduled_date_str text,
  ai_generated_sankalpa text,
  status text NOT NULL DEFAULT 'PENDING',
  payment_status text NOT NULL DEFAULT 'PENDING',
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE puja_bookings ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "bookings_select_own" ON puja_bookings;
CREATE POLICY "bookings_select_own" ON puja_bookings FOR SELECT
  TO authenticated USING (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "bookings_insert_own" ON puja_bookings;
CREATE POLICY "bookings_insert_own" ON puja_bookings FOR INSERT
  TO authenticated WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "bookings_update_own" ON puja_bookings;
CREATE POLICY "bookings_update_own" ON puja_bookings FOR UPDATE
  TO authenticated USING (auth.uid()::text = firebase_uid) WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "bookings_delete_own" ON puja_bookings;
CREATE POLICY "bookings_delete_own" ON puja_bookings FOR DELETE
  TO authenticated USING (auth.uid()::text = firebase_uid);

CREATE INDEX IF NOT EXISTS idx_bookings_firebase_uid ON puja_bookings(firebase_uid);

-- family_members (user-owned, keyed by firebase_uid)
CREATE TABLE IF NOT EXISTS family_members (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  firebase_uid text NOT NULL,
  name text NOT NULL,
  relation text,
  gotra text,
  nakshatra text,
  created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE family_members ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "family_select_own" ON family_members;
CREATE POLICY "family_select_own" ON family_members FOR SELECT
  TO authenticated USING (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "family_insert_own" ON family_members;
CREATE POLICY "family_insert_own" ON family_members FOR INSERT
  TO authenticated WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "family_update_own" ON family_members;
CREATE POLICY "family_update_own" ON family_members FOR UPDATE
  TO authenticated USING (auth.uid()::text = firebase_uid) WITH CHECK (auth.uid()::text = firebase_uid);

DROP POLICY IF EXISTS "family_delete_own" ON family_members;
CREATE POLICY "family_delete_own" ON family_members FOR DELETE
  TO authenticated USING (auth.uid()::text = firebase_uid);

CREATE INDEX IF NOT EXISTS idx_family_firebase_uid ON family_members(firebase_uid);