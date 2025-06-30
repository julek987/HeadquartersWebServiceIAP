import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authUtils } from '../../../services/api';
import './header.css';

const Header = () => {
    const navigate = useNavigate();
    const isAuthenticated = authUtils.isAuthenticated();
    const authData = isAuthenticated ? authUtils.getAuthData() : null;

    const handleLogout = () => {
        authUtils.logout();
        navigate('/login');
    };

    const getRoleName = (role) => {
        switch(role) {
            case 0: return 'Director';
            case 1: return 'Admin';
            default: return 'Unknown';
        }
    };

    const getDashboardPath = () => {
        if (!isAuthenticated) return '/login';

        const role = authData.user.role;
        switch(role) {
            case 0: return '/director-dashboard';
            case 1: return '/admin-dashboard';
            default: return '/dashboard';
        }
    };

    const handleDashboardClick = (e) => {
        e.preventDefault();
        const dashboardPath = getDashboardPath();
        // Force navigation even if we're already on the dashboard
        navigate(dashboardPath, { replace: true });
        // Alternative: use navigate(dashboardPath) without replace if you want it in history
    };

    return (
        <header className="header">
            <div className="header-container">
                <div className="header-brand">
                    <Link to={isAuthenticated ? getDashboardPath() : '/login'}>
                        <h1>Luxe Living</h1>
                    </Link>
                </div>

                <nav className="header-nav">
                    {isAuthenticated ? (
                        <>
                            <span className="welcome-text">
                                Welcome, {authData.user.name} ({getRoleName(authData.user.role)})
                            </span>
                            <div className="nav-links">
                                <button
                                    onClick={handleDashboardClick}
                                    className="nav-link dashboard-btn"
                                >
                                    Dashboard
                                </button>
                                <button
                                    onClick={handleLogout}
                                    className="logout-btn"
                                >
                                    Logout
                                </button>
                            </div>
                        </>
                    ) : (
                        <div className="nav-links">
                            <Link to="/login" className="nav-link">
                                Login
                            </Link>
                            <Link to="/register" className="nav-link">
                                Register
                            </Link>
                        </div>
                    )}
                </nav>
            </div>
        </header>
    );
};

export default Header;