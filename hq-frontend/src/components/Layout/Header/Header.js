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
            case 0: return 'User';
            case 1: return 'Manager';
            case 2: return 'Admin';
            default: return 'User';
        }
    };

    const getDashboardLink = () => {
        if (!isAuthenticated) return '/login';

        const role = authData.user.role;
        switch(role) {
            case 0: return '/user-dashboard';
            case 1: return '/manager-dashboard';
            case 2: return '/admin-dashboard';
            default: return '/dashboard';
        }
    };

    return (
        <header className="header">
            <div className="header-container">
                <div className="header-brand">
                    <Link to={isAuthenticated ? getDashboardLink() : '/login'}>
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
                                <Link
                                    to={getDashboardLink()}
                                    className="nav-link"
                                >
                                    Dashboard
                                </Link>
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