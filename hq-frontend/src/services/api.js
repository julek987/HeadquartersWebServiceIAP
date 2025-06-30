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

// Initialize auth data safely
if (typeof window !== 'undefined' && !window.authData) {
    window.authData = null;
}

// Auth utilities for storing token and user info
export const authUtils = {
    setAuthData: (token, userInfo) => {
        // Store in memory (not localStorage due to Claude.ai restrictions)
        if (typeof window !== 'undefined') {
            window.authData = { token, user: userInfo };
        }
    },

    getAuthData: () => {
        if (typeof window === 'undefined') return null;
        return window.authData || null;
    },

    clearAuthData: () => {
        if (typeof window !== 'undefined') {
            delete window.authData;
        }
    },

    isAuthenticated: () => {
        if (typeof window === 'undefined') return false;
        return window.authData && window.authData.token;
    },

    getUserRole: () => {
        if (typeof window === 'undefined') return null;
        const authData = window.authData;
        return authData && authData.user ? authData.user.role : null;
    },

    getUserRoleName: () => {
        const role = authUtils.getUserRole();
        return getRoleName(role);
    },

    getUsername: () => {
        if (typeof window === 'undefined') return null;
        const authData = window.authData;
        return authData && authData.user ? authData.user.username : null;
    },

    // Additional helper methods
    getUser: () => {
        if (typeof window === 'undefined') return null;
        const authData = window.authData;
        return authData ? authData.user : null;
    },

    getToken: () => {
        if (typeof window === 'undefined') return null;
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
        if (typeof window !== 'undefined') {
            window.location.href = '/login';
        }

        // Or if you're using React Router's navigate, you can modify this
        // to accept a navigate function as parameter
    }
};

// Ensure authUtils is properly initialized
if (typeof authUtils === 'undefined') {
    console.error('authUtils failed to initialize');
}

// Auth API endpoints
export const authAPI = {
    // Login endpoint
    login: async (credentials) => {
        try {
            const response = await api.post('/auth/login', {
                username: credentials.username,
                password: credentials.password
            });
            return response.data;
        } catch (error) {
            // Re-throw with a consistent error format
            if (error.response && error.response.data) {
                throw new Error(error.response.data);
            }
            throw new Error('Login failed');
        }
    },

    // Register endpoint - FIXED VERSION
    register: async (userData) => {
        try {
            const response = await api.post('/auth/register', userData);
            return response.data; // Return just the data, not the full response
        } catch (error) {
            // Handle different types of error responses
            if (error.response) {
                // Server responded with error status
                const errorMessage = error.response.data || `Registration failed with status ${error.response.status}`;
                throw new Error(errorMessage);
            } else if (error.request) {
                // Request was made but no response received
                throw new Error('No response from server. Please check your connection.');
            } else {
                // Something else happened
                throw new Error('Registration failed: ' + error.message);
            }
        }
    },

    // Get addresses for registration
    getAddresses: async () => {
        try {
            const response = await api.get('/auth/addresses');
            return response.data;
        } catch (error) {
            if (error.response && error.response.data) {
                throw new Error(error.response.data);
            }
            throw new Error('Failed to fetch addresses');
        }
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

// User API endpoints
export const userAPI = {
    // Get all users
    getUsers: async () => {
        const token = authUtils.getToken();
        const headers = {};
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }
        const response = await api.get('/users', { headers });
        return response.data;
    },

    // Add a new user
    addUser: async (userData) => {
        const token = authUtils.getToken();
        const headers = { 'Content-Type': 'application/json' };
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }
        const response = await api.post('/users', userData, { headers });
        return response.data;
    },

    // Update user
    updateUser: async (id, userData) => {
        const token = authUtils.getToken();
        const headers = { 'Content-Type': 'application/json' };
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }
        const response = await api.put(`/users/${id}`, userData, { headers });
        return response.data;
    },

    // Delete user
    deleteUser: async (id) => {
        const token = authUtils.getToken();
        const headers = {};
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }
        const response = await api.delete(`/users/${id}`, { headers });
        return response.data;
    }
};

// Address API endpoints
export const addressAPI = {
    getAddresses: async () => {
        const token = authUtils.getToken();
        const headers = {};
        if (token) {
            headers.Authorization = `Bearer ${token}`;
        }
        const response = await api.get('/addresses', { headers });
        return response.data;
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