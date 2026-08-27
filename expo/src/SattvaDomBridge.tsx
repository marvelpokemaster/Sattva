'use dom';

import { MemoryRouter } from 'react-router-dom';
import { QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from '../../web/src/features/auth/AuthContext';
import { queryClient, SattvaAppContent } from '../../web/src/App';
import '../../web/src/index.css';

interface SattvaDomBridgeProps {
  initialRoute?: string;
  dom?: import('expo/dom').DOMProps;
}

export default function SattvaDomBridge({ initialRoute = '/' }: SattvaDomBridgeProps) {
  return (
    <div style={{ width: '100%', minHeight: '100vh', margin: 0, padding: 0, overflowX: 'hidden' }}>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <MemoryRouter initialEntries={[initialRoute]}>
            <SattvaAppContent />
          </MemoryRouter>
        </AuthProvider>
      </QueryClientProvider>
    </div>
  );
}
