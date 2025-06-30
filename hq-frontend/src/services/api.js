import axios from 'axios';

// Base URL for your Spring backend - using proxy
const BASE_URL = '/api'; // Changed to use proxy

// Create axios instance
const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Role definitions for this project
export const ROLES = {
    DIRECTOR: 0,
    ADMIN: 1
};

// Helper function to get role name
export const getRoleName = (roleId) => {
    switch(roleId) {
        case ROLES.DIRECTOR: return 'Director';
        case ROLES.ADMIN: return 'Admin';
        default: return 'Unknown';
    }
};

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

    getUserRoleName: () => {
        const role = authUtils.getUserRole();
        return getRoleName(role);
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

    // Check if user has specific role
    hasRole: (requiredRole) => {
        const userRole = authUtils.getUserRole();
        return userRole === requiredRole;
    },

    // Check if user is Director
    isDirector: () => {
        return authUtils.hasRole(ROLES.DIRECTOR);
    },

    // Check if user is Admin
    isAdmin: () => {
        return authUtils.hasRole(ROLES.ADMIN);
    },

    // Logout method
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

// Product API endpoints
export const productAPI = {
    // Add a new product
    addProduct: async (productData) => {
        const token = authUtils.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };

        // Add authorization header if token exists
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.post('/product', productData, { headers });
        return response.data;
    },

    // Get all products
    getProducts: async () => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.get('/product', { headers });
        return response.data;
    },

    // Get product by ID
    getProduct: async (id) => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.get(`/product/${id}`, { headers });
        return response.data;
    },

    // Update product
    updateProduct: async (id, productData) => {
        const token = authUtils.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.put(`/product/${id}`, productData, { headers });
        return response.data;
    },

    // Delete product
    deleteProduct: async (id) => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.delete(`/product/${id}`, { headers });
        return response.data;
    }
};

// Image API endpoints - Add these for your ProductsList component
export const imageAPI = {
    // Get all images
    getImages: async () => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.get('/image', { headers });
        return response.data;
    },

    // Get images for a specific product
    getProductImages: async (productId) => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.get(`/image/product/${productId}`, { headers });
        return response.data;
    },

    // Add image for a product
    addImage: async (imageData) => {
        const token = authUtils.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.post('/image', imageData, { headers });
        return response.data;
    },

    // Update image
    updateImage: async (id, imageData) => {
        const token = authUtils.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.put(`/image/${id}`, imageData, { headers });
        return response.data;
    },

    // Delete image
    deleteImage: async (id) => {
        const token = authUtils.getToken();
        const headers = {};

        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }

        const response = await api.delete(`/image/${id}`, { headers });
        return response.data;
    }
};