import React from 'react';
import { authUtils } from '../../../services/api';
import { useNavigate } from 'react-router-dom';
import './Director.css';

const DirectorDashboard = () => {
    const navigate = useNavigate();
    const authData = authUtils.getAuthData();
    const user = authData?.user;

    const handleAddProducts = () => {
        navigate('/add-products');
    };

    const handleSupplyRequests = () => {
        navigate('/supply-requests');
    };

    const handleViewProducts = () => {
        navigate('/products-list');
    };

    const handleManageBranches = () => {
        navigate('/manage-branches');
    };

    return (
        <div className="dashboard-container">
            <div className="welcome-header">
                <h1>Director Dashboard</h1>
                <p className="welcome-message">
                    Welcome back, {user?.firstName} {user?.lastName}!
                </p>
            </div>

            <div className="dashboard-grid">
                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>My Profile</h3>
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
                            <span className="value">Director</span>
                        </div>
                    </div>
                </div>

                <div className="dashboard-card">
                    <div className="card-header">
                        <h3>Director Actions</h3>
                    </div>
                    <div className="card-content">
                        <div className="director-actions">
                            <button
                                className="director-btn add-products"
                                onClick={handleAddProducts}
                            >
                                Add Products
                            </button>
                            <button
                                className="director-btn supply-requests"
                                onClick={handleSupplyRequests}
                            >
                                Supply Requests
                            </button>
                            <button
                                className="director-btn view-products"
                                onClick={handleViewProducts}
                            >
                                Display Products
                            </button>
                            <button
                                className="director-btn manage-branches"
                                onClick={handleManageBranches}
                            >
                                Manage Branches
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DirectorDashboard;