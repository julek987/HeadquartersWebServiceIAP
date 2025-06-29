import axios from 'axios';

// Base URL for your Spring backend
const BASE_URL = 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Auth utilities for storing token and user info
export const authUtils = {
    setAuthData: (token, userInfo) => {
        // Store in memory (not localStorage due to Claude.ai restrictions)
        window.authData = { token, user: userInfo };
    },

    getAuthData: () => {
        return window.authData || null;
    },

    clearAuthData: () => {
        delete window.authData;
    },

    isAuthenticated: () => {
        return window.authData && window.authData.token;
    },

    getUserRole: () => {
        const authData = window.authData;
        return authData && authData.user ? authData.user.role : null;
    },

    getUsername: () => {
        const authData = window.authData;
        return authData && authData.user ? authData.user.username : null;
    },

    // Additional helper methods
    getUser: () => {
        const authData = window.authData;
        return authData ? authData.user : null;
    },

    getToken: () => {
        const authData = window.authData;
        return authData ? authData.token : null;
    },

    // ADD THIS MISSING LOGOUT METHOD
    logout: () => {
        // Clear auth data
        authUtils.clearAuthData();

        // Redirect to login page
        window.location.href = '/login';

        // Or if you're using React Router's navigate, you can modify this
        // to accept a navigate function as parameter
    }
};

// Auth API endpoints
export const authAPI = {
    // Login endpoint
    login: async (credentials) => {
        const response = await api.post('/auth/login', {
            username: credentials.username,
            password: credentials.password
        });
        return response.data;
    },

    // Register endpoint
    register: async (userData) => {
        return await api.post('/auth/register', userData);
    },

    // Get addresses for registration
    getAddresses: async () => {
        return await api.get('/auth/addresses');
    },

    // Optional: Add logout API call if your backend requires it
    logout: async () => {
        const token = authUtils.getToken();
        if (token) {
            try {
                await api.post('/auth/logout', {}, {
                    headers: { Authorization: `Bearer ${token}` }
                });
            } catch (error) {
                console.error('Logout API error:', error);
                // Continue with client-side logout even if API fails
            }
        }

        // Clear client-side auth data
        authUtils.logout();
    }
};