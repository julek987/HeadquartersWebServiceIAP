import React from 'react';
import { authUtils } from '../../../services/api';
import { useNavigate } from 'react-router-dom';

const AdminDashboard = () => {
    const authData = authUtils.getAuthData();
    const user = authData?.user;
    const navigate = useNavigate();

    const handleManageUsers = () => {
        navigate('/users');
    };

    return (
        <div className="dashboard-container">
            <div className="welcome-header">
                <h1>Admin Dashboard</h1>
                <p className="welcome-message">
                    Welcome back, Administrator {user?.firstName} {user?.lastName}!
                </p>
            </div>

            <div className="dashboard-grid">
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
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>System Administration</h3>
                    </div>
                    <div className="card-content">
                        <div className="action-buttons">
                            <button className="action-btn primary" onClick={handleManageUsers}>Manage All Users</button>
                            <button className="action-btn primary">System Settings</button>
                            <button className="action-btn primary">View All Products</button>
                        </div>
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>System Overview</h3>
                    </div>
                    <div className="card-content">
                        <div className="stats-grid">
                            <div className="stat-item">
                                <div className="stat-number">3</div>
                                <div className="stat-label">Total Users</div>
                            </div>
                            <div className="stat-item">
                                <div className="stat-number">50</div>
                                <div className="stat-label">Total Products</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
