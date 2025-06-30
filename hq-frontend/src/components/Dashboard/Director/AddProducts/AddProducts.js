import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authUtils, productAPI, imageAPI } from '../../../../services/api';
import './AddProducts.css'; // Import the CSS file

const AddProducts = () => {
    const navigate = useNavigate();
    const authData = authUtils.getAuthData();

    const [formData, setFormData] = useState({
        productName: '',
        price: '',
        width: '',
        depth: '',
        height: '',
        imageUrl: '' // Single image URL
    });

    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitMessage, setSubmitMessage] = useState('');
    const [debugInfo, setDebugInfo] = useState('');
    const [debugPanelOpen, setDebugPanelOpen] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // Clear error for this field when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const validateForm = () => {
        const newErrors = {};

        // Product name validation
        if (!formData.productName.trim()) {
            newErrors.productName = 'Product name is required';
        } else if (formData.productName.length > 200) {
            newErrors.productName = 'Product name must be less than 200 characters';
        }

        // Price validation
        if (!formData.price) {
            newErrors.price = 'Price is required';
        } else if (parseFloat(formData.price) <= 0) {
            newErrors.price = 'Price must be greater than 0';
        }

        // Width validation
        if (!formData.width) {
            newErrors.width = 'Width is required';
        } else if (!Number.isInteger(parseFloat(formData.width)) || parseFloat(formData.width) <= 0) {
            newErrors.width = 'Width must be a positive integer';
        }

        // Depth validation
        if (!formData.depth) {
            newErrors.depth = 'Depth is required';
        } else if (!Number.isInteger(parseFloat(formData.depth)) || parseFloat(formData.depth) <= 0) {
            newErrors.depth = 'Depth must be a positive integer';
        }

        // Height validation
        if (!formData.height) {
            newErrors.height = 'Height is required';
        } else if (!Number.isInteger(parseFloat(formData.height)) || parseFloat(formData.height) <= 0) {
            newErrors.height = 'Height must be a positive integer';
        }

        // Image URL validation (optional field)
        if (formData.imageUrl.trim()) {
            if (formData.imageUrl.length > 255) {
                newErrors.imageUrl = 'Image URL must be less than 255 characters';
            }
            // Basic URL validation
            try {
                new URL(formData.imageUrl);
            } catch {
                newErrors.imageUrl = 'Please enter a valid URL';
            }
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    // Test connectivity function
    const testConnection = async () => {
        try {
            setDebugInfo('Testing connection...');
            const response = await productAPI.getProducts();
            setDebugInfo(`✅ GET /api/product works! Found ${response.length} products`);
        } catch (error) {
            setDebugInfo(`❌ GET /api/product failed: ${error.message}`);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        // Check if authData exists and has user info
        if (!authData || !authData.user || !authData.user.id) {
            setSubmitMessage('Error: User authentication data is missing. Please log in again.');
            return;
        }

        setIsSubmitting(true);
        setSubmitMessage('');
        setDebugInfo('');

        try {
            const productData = {
                productName: formData.productName.trim(),
                price: parseFloat(formData.price),
                width: parseInt(formData.width),
                depth: parseInt(formData.depth),
                height: parseInt(formData.height),
                addedById: authData.user.id
            };

            setDebugInfo(`Sending POST to /api/product with data: ${JSON.stringify(productData, null, 2)}`);
            console.log('Sending product data:', productData);

            const response = await productAPI.addProduct(productData);
            console.log('Server response:', response);

            const productId = response.id;
            setDebugInfo('✅ Product added successfully!');

            // Add image if URL is provided
            if (formData.imageUrl.trim()) {
                const imageData = {
                    productId: productId,
                    url: formData.imageUrl.trim(),
                    showOrder: 1 // Always 1 since there's only one image
                };

                try {
                    await imageAPI.addImage(imageData);
                    console.log('Image added successfully');
                    setDebugInfo('✅ Product and image added successfully!');
                    setSubmitMessage('Product added successfully with image!');
                } catch (imageError) {
                    console.error('Error adding image:', imageError);
                    setDebugInfo('✅ Product added, but image failed to upload');
                    setSubmitMessage('Product added successfully, but image upload failed');
                }
            } else {
                setSubmitMessage('Product added successfully!');
            }

            // Reset form
            setFormData({
                productName: '',
                price: '',
                width: '',
                depth: '',
                height: '',
                imageUrl: ''
            });

            // Redirect back to dashboard after a short delay
            setTimeout(() => {
                navigate('/director-dashboard');
            }, 2000);
        } catch (error) {
            console.error('Error adding product:', error);

            let errorDetails = '';

            if (error.response) {
                // Server responded with error status
                console.error('Server error response:', error.response.data);
                console.error('Status code:', error.response.status);
                console.error('Response headers:', error.response.headers);

                errorDetails = `
Status: ${error.response.status}
Response: ${JSON.stringify(error.response.data, null, 2)}
URL: ${error.config?.url || 'Unknown'}
Method: ${error.config?.method?.toUpperCase() || 'Unknown'}
                `;

                if (error.response.status === 401) {
                    setSubmitMessage('Error: Unauthorized. Please log in again.');
                } else if (error.response.status === 400) {
                    setSubmitMessage(`Error: ${error.response.data.message || error.response.data.detail || 'Invalid data provided'}`);
                } else if (error.response.status === 500) {
                    setSubmitMessage('Error: Server error. Check console and debug info below.');
                } else {
                    setSubmitMessage(`Error: ${error.response.data.message || error.response.data.detail || 'Failed to add product'}`);
                }
            } else if (error.request) {
                // Request was made but no response received
                console.error('No response received:', error.request);
                errorDetails = `Request made but no response received. Check if backend is running on port 8081.`;
                setSubmitMessage('Error: Cannot connect to server. Please check if the server is running on http://localhost:8081');
            } else {
                // Something else happened
                console.error('Request setup error:', error.message);
                errorDetails = `Request setup error: ${error.message}`;
                setSubmitMessage(`Error: ${error.message}`);
            }

            setDebugInfo(`❌ Error Details:\n${errorDetails}`);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleCancel = () => {
        navigate('/director-dashboard');
    };

    return (
        <div className="add-products-container">
            <div className="add-products-header">
                <h1>Add New Product</h1>
                <p>Fill in the details below to add a new product to the inventory.</p>
            </div>

            {/* Collapsible Debug panel */}
            <div className="debug-panel">
                <div className="debug-panel-header" onClick={() => setDebugPanelOpen(!debugPanelOpen)}>
                    <h3>Details {debugPanelOpen ? '▼' : '▶'}</h3>
                    {debugPanelOpen && (
                        <button
                            type="button"
                            onClick={(e) => {
                                e.stopPropagation();
                                testConnection();
                            }}
                            className="debug-test-btn"
                        >
                            Test GET /api/product
                        </button>
                    )}
                </div>
                {debugPanelOpen && (
                    <div className="debug-info">
                        <div><strong>User ID:</strong> {authData?.user?.id || 'Not found'}</div>
                        <div><strong>Has Token:</strong> {authData?.token ? 'Yes' : 'No'}</div>
                        <div><strong>Token (first 20 chars):</strong> {authData?.token ? authData.token.substring(0, 20) + '...' : 'None'}</div>
                        <div><strong>Proxy URL:</strong> /api/product</div>
                        <div><strong>Backend URL:</strong> http://localhost:8081/api/product</div>
                        {debugInfo && (
                            <pre>{debugInfo}</pre>
                        )}
                    </div>
                )}
            </div>

            <form onSubmit={handleSubmit} className="add-products-form">
                <div className="form-group">
                    <label htmlFor="productName">Product Name *</label>
                    <input
                        type="text"
                        id="productName"
                        name="productName"
                        value={formData.productName}
                        onChange={handleInputChange}
                        className={errors.productName ? 'error' : ''}
                        maxLength="200"
                        placeholder="Enter product name"
                    />
                    {errors.productName && (
                        <span className="error-message">
                            {errors.productName}
                        </span>
                    )}
                </div>

                <div className="form-group">
                    <label htmlFor="price">Price * ($)</label>
                    <input
                        type="number"
                        id="price"
                        name="price"
                        value={formData.price}
                        onChange={handleInputChange}
                        className={errors.price ? 'error' : ''}
                        step="0.01"
                        min="0.01"
                        placeholder="0.00"
                    />
                    {errors.price && (
                        <span className="error-message">
                            {errors.price}
                        </span>
                    )}
                </div>

                <div className="dimensions-grid">
                    <div className="form-group">
                        <label htmlFor="width">Width * (cm)</label>
                        <input
                            type="number"
                            id="width"
                            name="width"
                            value={formData.width}
                            onChange={handleInputChange}
                            className={errors.width ? 'error' : ''}
                            min="1"
                            placeholder="0"
                        />
                        {errors.width && (
                            <span className="error-message">
                                {errors.width}
                            </span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="depth">Depth * (cm)</label>
                        <input
                            type="number"
                            id="depth"
                            name="depth"
                            value={formData.depth}
                            onChange={handleInputChange}
                            className={errors.depth ? 'error' : ''}
                            min="1"
                            placeholder="0"
                        />
                        {errors.depth && (
                            <span className="error-message">
                                {errors.depth}
                            </span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="height">Height * (cm)</label>
                        <input
                            type="number"
                            id="height"
                            name="height"
                            value={formData.height}
                            onChange={handleInputChange}
                            className={errors.height ? 'error' : ''}
                            min="1"
                            placeholder="0"
                        />
                        {errors.height && (
                            <span className="error-message">
                                {errors.height}
                            </span>
                        )}
                    </div>
                </div>

                {/* Single Image URL field */}
                <div className="form-group">
                    <label htmlFor="imageUrl">Product Image URL (Optional)</label>
                    <input
                        type="url"
                        id="imageUrl"
                        name="imageUrl"
                        value={formData.imageUrl}
                        onChange={handleInputChange}
                        className={errors.imageUrl ? 'error' : ''}
                        placeholder="https://example.com/image.jpg"
                    />
                    {errors.imageUrl && (
                        <span className="error-message">
                            {errors.imageUrl}
                        </span>
                    )}
                </div>

                {submitMessage && (
                    <div className={`submit-message ${submitMessage.includes('Error') ? 'submit-error' : 'submit-success'}`}>
                        {submitMessage}
                    </div>
                )}

                <div className="form-actions">
                    <button
                        type="button"
                        onClick={handleCancel}
                        className="btn btn-cancel"
                        disabled={isSubmitting}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className="btn btn-submit"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Adding Product...' : 'Add Product'}
                    </button>
                </div>
            </form>

            <div className="form-footer">
                <p>* Required fields</p>
                <p>Note: Dimensions should be provided in centimeters as whole numbers.</p>
                <p>Image URL is optional. If provided, it will be displayed with your product.</p>
            </div>
        </div>
    );
};

export default AddProducts;