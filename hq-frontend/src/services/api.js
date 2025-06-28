// Base API configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Generic API call function
const apiCall = async (endpoint, options = {}) => {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
            ...options,
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
};

// API functions for your controllers
export const imageAPI = {
    getAll: () => apiCall('/image'),
    getById: (id) => apiCall(`/image/${id}`),
    create: (imageData) => apiCall('/image', {
        method: 'POST',
        body: JSON.stringify(imageData),
    }),
    update: (id, imageData) => apiCall(`/image/${id}`, {
        method: 'PUT',
        body: JSON.stringify(imageData),
    }),
    delete: (id) => apiCall(`/image/${id}`, {
        method: 'DELETE',
    }),
};

export const addressAPI = {
    getAll: () => apiCall('/addresses'),
    getById: (id) => apiCall(`/addresses/${id}`),
    create: (addressData) => apiCall('/addresses', {
        method: 'POST',
        body: JSON.stringify(addressData),
    }),
    update: (id, addressData) => apiCall(`/addresses/${id}`, {
        method: 'PUT',
        body: JSON.stringify(addressData),
    }),
    delete: (id) => apiCall(`/addresses/${id}`, {
        method: 'DELETE',
    }),
};

export const branchAPI = {
    getAll: () => apiCall('/branch'),
    getById: (id) => apiCall(`/branch/${id}`),
    create: (branchData) => apiCall('/branch', {
        method: 'POST',
        body: JSON.stringify(branchData),
    }),
    update: (id, branchData) => apiCall(`/branch/${id}`, {
        method: 'PUT',
        body: JSON.stringify(branchData),
    }),
    delete: (id) => apiCall(`/branch/${id}`, {
        method: 'DELETE',
    }),
};

// Simple API call for welcome message
export const getWelcomeMessage = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/welcome');
        return await response.text();
    } catch (error) {
        console.error('Failed to fetch welcome message:', error);
        throw error;
    }
};