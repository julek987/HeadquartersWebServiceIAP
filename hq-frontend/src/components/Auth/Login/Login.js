import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI, authUtils } from '../../../services/api';
import './Login.css';

const Login = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

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

        const validationError = validateForm();
        if (validationError) {
            setError(validationError);
            return;
        }

        setLoading(true);
        setError('');

        try {
            // Call real login API
            const response = await authAPI.login(formData);

            // Store auth data
            authUtils.setAuthData(response.token, response.user);

            // Role-based routing
            const userRole = response.user.role;

            switch(userRole) {
                case 0: // Manager role
                    navigate('/director-dashboard');
                    break;
                case 1: // Admin role
                    navigate('/admin-dashboard');
                    break;
                default:
                    navigate('/dashboard'); // Fallback
            }

        } catch (error) {
            console.error('Login error:', error);

            if (error.response?.status === 401) {
                setError('Invalid username or password');
            } else if (error.response?.status === 400) {
                setError('Please check your credentials');
            } else if (error.response?.data && typeof error.response.data === 'string') {
                setError(error.response.data);
            } else {
                setError('Login failed. Please try again later.');
            }
        } finally {
            setLoading(false);
        }
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

                <div className="auth-links">
                    <p>Don't have an account? <a href="/register">Create one here</a></p>
                </div>

                <div className="test-credentials" style={{marginTop: '20px', padding: '15px', background: '#f0f8ff', borderRadius: '8px', fontSize: '14px'}}>
                    <h4>Test Credentials:</h4>
                    <p><strong>User:</strong> usertest1 / TestUser@1</p>
                    <p><strong>Manager:</strong> managertest / TestManager@1</p>
                    <p><strong>Admin:</strong> admintest / TestAdmin@1</p>
                </div>
            </div>
        </div>
    );
};

export default Login;