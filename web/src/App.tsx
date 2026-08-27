import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider, useAuth } from '@/features/auth/AuthContext';
import { AppShell } from '@/components/layout/AppShell';

import { Home } from '@/features/home/Home';
import { Auth } from '@/features/auth/Auth';

import { GaushalaDiscovery } from '@/features/gaushala/GaushalaDiscovery';
import { AnimalPassport } from '@/features/gaushala/AnimalPassport';
import { PujaDiscovery } from '@/features/pujas/PujaDiscovery';
import { SevaExperience } from '@/features/seva/SevaExperience';
import { Profile } from '@/features/profile/Profile';

// Protected Route Wrapper
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { user, loading } = useAuth();
  
  if (loading) return <div>Loading...</div>; // TODO: Implement loading spinner
  
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  
  return <>{children}</>;
};

const queryClient = new QueryClient();

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Auth />} />
            
            <Route element={<AppShell />}>
              <Route path="/" element={<Home />} />
              <Route path="/pujas" element={<PujaDiscovery />} />
              <Route path="/gaushala" element={<GaushalaDiscovery />} />
              <Route path="/gaushala/animal/:id" element={<AnimalPassport />} />
              <Route path="/seva" element={<SevaExperience />} />
              <Route 
                path="/profile" 
                element={
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                } 
              />
            </Route>
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  );
}
