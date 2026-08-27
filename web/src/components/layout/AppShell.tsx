import { NavLink, Outlet } from 'react-router-dom';
import { Home, Flame, Map, User } from 'lucide-react';
import './AppShell.css';

export function AppShell() {
  const navItems = [
    { name: 'Home', path: '/', icon: Home },
    { name: 'Pujas', path: '/pujas', icon: Flame },
    { name: 'Gaushala', path: '/gaushala', icon: Map },
    { name: 'Profile', path: '/profile', icon: User },
  ];

  return (
    <div className="app-shell">
      {/* Desktop Side Navigation */}
      <nav className="side-nav">
        <div className="nav-brand">
          <h2>Sattva</h2>
        </div>
        <div className="nav-links">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            >
              <item.icon size={24} />
              <span>{item.name}</span>
            </NavLink>
          ))}
        </div>
      </nav>

      {/* Main Content Area */}
      <main className="main-content">
        <div className="page-container">
          <Outlet />
        </div>
      </main>

      {/* Mobile Bottom Navigation */}
      <nav className="bottom-nav glass-surface">
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => `bottom-nav-item ${isActive ? 'active' : ''}`}
          >
            <item.icon size={24} strokeWidth={1.5} />
            <span>{item.name}</span>
          </NavLink>
        ))}
      </nav>
    </div>
  );
}
