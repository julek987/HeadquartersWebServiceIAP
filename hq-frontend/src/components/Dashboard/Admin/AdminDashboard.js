import React, { useState } from 'react';
import { authUtils } from '../../../services/api';
import { useNavigate } from 'react-router-dom';
import UserManagement from './UserManagement';

const AdminDashboard = () => {
    const [currentView, setCurrentView] = useState('dashboard');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const authData = authUtils.getAuthData();
    const user = authData?.user;
    const navigate = useNavigate();

    const handleManageUsers = () => {
        setCurrentView('userManagement');
    };

    const handleBackToDashboard = () => {
        setCurrentView('dashboard');
        setError('');
        setSuccess('');
    };

    const handleSystemSettings = () => {
        // TODO: Implement system settings navigation
        console.log('Navigate to system settings');
    };

    const handleViewAllProducts = () => {
        // TODO: Implement products navigation
        console.log('Navigate to view all products');
    };

    // If we're in user management view, render that component with a back button
    if (currentView === 'userManagement') {
        return (
            <div>
                <button
                    onClick={handleBackToDashboard}
                    style={{
                        marginBottom: '20px',
                        padding: '10px 20px',
                        backgroundColor: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    ← Back to Dashboard
                </button>
                <UserManagement />
            </div>
        );
    }

    // Main dashboard view
    return (
        <div className="dashboard-container">
            <div className="welcome-header">
                <h1>Admin Dashboard</h1>
                <p className="welcome-message">
                    Welcome back, Administrator {user?.firstName} {user?.lastName}!
                </p>
            </div>

            {/* Success/Error Messages */}
            {success && (
                <div className="alert alert-success">
                    {success}
                </div>
            )}
            {error && (
                <div className="alert alert-error">
                    {error}
                </div>
            )}

            <div className="dashboard-grid">
                {/* Admin Profile Card */}
                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>Admin Profile</h3>
                    </div>
                    <div className="card-content">
                        <div className="info-row">
                            <span className="label">Name:</span>
                            <span className="value">{user?.firstName} {user?.lastName}</span>
                        </div>
                        <div className="info-row">
                            <span className="label">Email:</span>
                            <span className="value">{user?.email}</span>
                        </div>
                        <div className="info-row">
                            <span className="label">Role:</span>
                            <span className="value">Administrator</span>
                        </div>
                        <div className="info-row">
                            <span className="label">Phone:</span>
                            <span className="value">{user?.phoneNumber || 'Not provided'}</span>
                        </div>
                    </div>
                </div>

                {/* System Administration Card */}
                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>System Administration</h3>
                    </div>
                    <div className="card-content">
                        <div className="action-buttons">
                            <button
                                className="action-btn primary"
                                onClick={handleManageUsers}
                                disabled={loading}
                            >
                                Manage All Users
                            </button>
                            <button
                                className="action-btn primary"
                                onClick={handleSystemSettings}
                                disabled={loading}
                            >
                                System Settings
                            </button>
                            <button
                                className="action-btn primary"
                                onClick={handleViewAllProducts}
                                disabled={loading}
                            >
                                View All Products
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;