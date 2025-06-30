import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authUtils, ROLES } from '../../../services/api';
import './Login.css';

const Login = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    // TEST FUNCTIONS - Remove these when backend is ready
    const handleTestDirectorLogin = () => {
        // Create mock director user
        const mockDirectorUser = {
            id: 3,
            username: 'testdirector',
            firstName: 'Test',
            lastName: 'Director',
            email: 'director@test.com',
            role: ROLES.DIRECTOR // Director role = 0
        };

        // Set mock auth data
        authUtils.setAuthData('mock-token-director', mockDirectorUser);

        // Navigate to director dashboard
        navigate('/director-dashboard');
    };

    const handleTestAdminLogin = () => {
        // Create mock admin user
        const mockAdminUser = {
            id: 2,
            username: 'testadmin',
            firstName: 'Test',
            lastName: 'Admin',
            email: 'admin@test.com',
            role: ROLES.ADMIN // Admin role = 1
        };

        // Set mock auth data
        authUtils.setAuthData('mock-token-admin', mockAdminUser);

        // Navigate to admin dashboard
        navigate('/admin-dashboard');
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // Clear error when user starts typing
        if (error) setError('');
    };

    const validateForm = () => {
        if (!formData.username.trim()) return 'Username is required';
        if (!formData.password.trim()) return 'Password is required';
        return null;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('Backend authentication not available yet. Use test buttons below.');

        // TODO: Implement actual login when backend is ready
        /*
        const validationError = validateForm();
        if (validationError) {
            setError(validationError);
            return;
        }

        setLoading(true);
        setError('');

        try {
            const response = await authAPI.login(formData);
            authUtils.setAuthData(response.token, response.user);

            // Redirect based on role
            if (response.user.role === ROLES.DIRECTOR) {
                navigate('/director-dashboard');
            } else if (response.user.role === ROLES.ADMIN) {
                navigate('/admin-dashboard');
            } else {
                navigate('/dashboard');
            }
        } catch (error) {
            setError(error.response?.data?.message || 'Login failed. Please try again.');
        } finally {
            setLoading(false);
        }
        */
    };

    return (
        <div className="auth-container">
            <div className="auth-form">
                <h2>Sign In</h2>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            autoComplete="username"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            autoComplete="current-password"
                        />
                    </div>

                    <button
                        type="submit"
                        className="auth-button"
                        disabled={loading}
                    >
                        {loading ? 'Signing In...' : 'Sign In'}
                    </button>
                </form>

                {/* TEST BUTTONS - Remove when backend is ready */}
                <div className="test-login-section" style={{
                    marginTop: '2rem',
                    padding: '1.5rem',
                    background: 'linear-gradient(135deg, rgba(255, 235, 59, 0.1), rgba(255, 193, 7, 0.1))',
                    borderRadius: '12px',
                    border: '2px dashed #ffc107',
                    textAlign: 'center'
                }}>
                    <h4 style={{
                        color: '#322052',
                        marginBottom: '1rem',
                        fontSize: '1rem',
                        textTransform: 'uppercase',
                        letterSpacing: '0.5px'
                    }}>
                        Testing Mode (Remove when backend is ready)
                    </h4>

                    <div style={{ display: 'flex', gap: '1rem', flexDirection: 'column' }}>
                        <button
                            type="button"
                            onClick={handleTestDirectorLogin}
                            style={{
                                padding: '0.8rem 1.5rem',
                                background: 'linear-gradient(135deg, #28a745, #20c997)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '8px',
                                cursor: 'pointer',
                                fontWeight: '500',
                                textTransform: 'uppercase',
                                letterSpacing: '0.5px',
                                transition: 'all 0.3s ease'
                            }}
                            onMouseOver={(e) => e.target.style.transform = 'translateY(-2px)'}
                            onMouseOut={(e) => e.target.style.transform = 'translateY(0)'}
                        >
                            Test as Director (Role {ROLES.DIRECTOR})
                        </button>

                        <button
                            type="button"
                            onClick={handleTestAdminLogin}
                            style={{
                                padding: '0.8rem 1.5rem',
                                background: 'linear-gradient(135deg, #dc3545, #c82333)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '8px',
                                cursor: 'pointer',
                                fontWeight: '500',
                                textTransform: 'uppercase',
                                letterSpacing: '0.5px',
                                transition: 'all 0.3s ease'
                            }}
                            onMouseOver={(e) => e.target.style.transform = 'translateY(-2px)'}
                            onMouseOut={(e) => e.target.style.transform = 'translateY(0)'}
                        >
                            Test as Admin (Role {ROLES.ADMIN})
                        </button>
                    </div>

                    <p style={{
                        fontSize: '0.8rem',
                        color: '#666',
                        marginTop: '1rem',
                        fontStyle: 'italic'
                    }}>
                        These buttons simulate login without backend authentication
                    </p>
                </div>

                <div className="auth-links">
                    <p>Don't have an account? <a href="/register">Create one here</a></p>
                </div>

                <div className="test-credentials" style={{marginTop: '20px', padding: '15px', background: '#f0f8ff', borderRadius: '8px', fontSize: '14px'}}>
                    <h4>Current Role System:</h4>
                    <p><strong>Role 0:</strong> Director</p>
                    <p><strong>Role 1:</strong> Admin</p>
                    <p><em>Backend authentication not implemented yet</em></p>
                </div>
            </div>
        </div>
    );
};

export default Login;