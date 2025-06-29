import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI } from '../../../services/api';

const Register = () => {
    const navigate = useNavigate();
    const [addresses, setAddresses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const [formData, setFormData] = useState({
        firstName: '',
        middleName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        login: '',
        password: '',
        confirmPassword: '',
        addressId: '',
        role: 0
    });

    useEffect(() => {
        const fetchAddresses = async () => {
            try {
                const response = await authAPI.getAddresses();
                console.log("Fetched addresses:", response.data); // Debug
                setAddresses(response.data);
            } catch (error) {
                console.error('Error fetching addresses:', error);
                setError('Failed to load addresses. Please refresh the page.');
            }
        };

        fetchAddresses();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));

        if (error) setError('');
        if (success) setSuccess('');
    };

    const validateForm = () => {
        if (!formData.firstName.trim()) return 'First name is required';
        if (!formData.lastName.trim()) return 'Last name is required';
        if (!formData.email.trim()) return 'Email is required';
        if (!formData.phoneNumber.trim()) return 'Phone number is required';
        if (!formData.login.trim()) return 'Username is required';
        if (!formData.password.trim()) return 'Password is required';
        if (formData.password !== formData.confirmPassword) return 'Passwords do not match';
        if (!formData.addressId) return 'Please select an address';

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) return 'Please enter a valid email address';
        if (formData.password.length < 6) return 'Password must be at least 6 characters long';

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
            const { confirmPassword, ...userData } = formData;
            const parsedAddressId = parseInt(userData.addressId);
            if (isNaN(parsedAddressId)) {
                setError('Please select a valid address');
                setLoading(false);
                return;
            }
            userData.addressId = parsedAddressId;

            await authAPI.register(userData);

            setSuccess('Registration successful! You can now log in.');
            setFormData({
                firstName: '',
                middleName: '',
                lastName: '',
                email: '',
                phoneNumber: '',
                login: '',
                password: '',
                confirmPassword: '',
                addressId: '',
                role: 0
            });

            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (error) {
            console.error('Registration error:', error);
            if (error.response?.data) {
                setError(typeof error.response.data === 'string'
                    ? error.response.data
                    : 'Registration failed. Please try again.');
            } else if (error.response?.status === 400) {
                setError('Invalid registration data. Please check your inputs.');
            } else {
                setError('Registration failed. Please try again later.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-form">
                <h2>Create Account</h2>

                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="firstName">First Name *</label>
                            <input
                                type="text"
                                id="firstName"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                required
                                disabled={loading}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="lastName">Last Name *</label>
                            <input
                                type="text"
                                id="lastName"
                                name="lastName"
                                value={formData.lastName}
                                onChange={handleChange}
                                required
                                disabled={loading}
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="middleName">Middle Name</label>
                        <input
                            type="text"
                            id="middleName"
                            name="middleName"
                            value={formData.middleName}
                            onChange={handleChange}
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email *</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="phoneNumber">Phone Number *</label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="login">Username *</label>
                        <input
                            type="text"
                            id="login"
                            name="login"
                            value={formData.login}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="password">Password *</label>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                                disabled={loading}
                                minLength="6"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="confirmPassword">Confirm Password *</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                required
                                disabled={loading}
                                minLength="6"
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="addressId">Address *</label>
                        <select
                            id="addressId"
                            name="addressId"
                            value={formData.addressId}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        >
                            <option value="">Select an address...</option>
                            {addresses.map(address => (
                                <option key={address.id} value={address.id.toString()}>
                                    {address.street}, {address.city}, {address.postalCode}
                                </option>
                            ))}
                        </select>
                    </div>

                    <button
                        type="submit"
                        className="auth-button"
                        disabled={loading}
                    >
                        {loading ? 'Creating Account...' : 'Create Account'}
                    </button>
                </form>

                <div className="auth-links">
                    <p>Already have an account? <a href="/login">Sign in here</a></p>
                </div>
            </div>
        </div>
    );
};

export default Register;

