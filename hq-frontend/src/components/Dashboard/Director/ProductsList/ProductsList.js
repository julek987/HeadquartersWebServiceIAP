import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { authUtils } from '../../../../services/api';
import './ProductsList.css';

const ProductsList = () => {
    const navigate = useNavigate();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [error, setError] = useState(null);

    const authData = authUtils.getAuthData();
    const user = authData?.user;

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            setLoading(true);
            setError(null);

            const token = authUtils.getToken();
            const requestHeaders = {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            };

            if (token) {
                requestHeaders['Authorization'] = `Bearer ${token}`;
            }

            // Fetch products
            const productsResponse = await fetch('/api/product', {
                method: 'GET',
                headers: requestHeaders
            });

            if (!productsResponse.ok) {
                throw new Error(`Failed to fetch products: ${productsResponse.status} ${productsResponse.statusText}`);
            }

            const productsData = await productsResponse.json();

            // Fetch images
            let imagesData = [];
            try {
                const imagesResponse = await fetch('/api/image', {
                    headers: requestHeaders
                });

                if (imagesResponse.ok) {
                    imagesData = await imagesResponse.json();
                }
            } catch (imagesError) {
                console.warn('Images API error (continuing without images):', imagesError.message);
            }

            // Combine products with their images
            const enrichedProducts = productsData
                .filter(product => !product.archivedAt) // Only show non-archived products
                .map(product => {
                    const productImages = imagesData
                        .filter(img => img.productId === product.id)
                        .sort((a, b) => a.showOrder - b.showOrder);

                    return {
                        id: product.id,
                        productName: product.productName,
                        price: product.price,
                        dimensions: `${product.width} × ${product.depth} × ${product.height}`,
                        primaryImageUrl: productImages.length > 0 ? productImages[0].url : null,
                        width: product.width,
                        depth: product.depth,
                        height: product.height,
                        createdAt: product.createdAt,
                        modifiedAt: product.modifiedAt
                    };
                });

            setProducts(enrichedProducts);

        } catch (err) {
            console.error('Fetch products error:', err);
            setError('Failed to load products: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const searchProducts = (query) => {
        // Simple client-side search since we already have all products
        return products.filter(product =>
            product.productName.toLowerCase().includes(query.toLowerCase())
        );
    };

    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
    };

    const filteredProducts = searchQuery ? searchProducts(searchQuery) : products;

    const goBack = () => {
        navigate(-1); // Go back to previous page
    };

    if (loading) {
        return (
            <div className="products-container">
                <div className="loading">Loading products...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="products-container">
                <div className="error-container">
                    <div className="error">{error}</div>
                    <div className="error-actions">
                        <button onClick={fetchProducts}>Retry</button>
                        <button onClick={goBack}>Go Back</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="products-container">
            <div className="products-header">
                <div className="header-top">
                    <button className="back-btn" onClick={goBack}>
                        ← Back
                    </button>
                    <h1>Products Catalog</h1>
                </div>
                <p>Welcome {user?.firstName} {user?.lastName} - Viewing all products</p>
            </div>

            <div className="products-controls">
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search products..."
                        value={searchQuery}
                        onChange={handleSearchChange}
                        className="search-input"
                    />
                </div>
                <div className="products-count">
                    Showing {filteredProducts.length} of {products.length} products
                </div>
            </div>

            <div className="products-grid">
                {filteredProducts.length === 0 ? (
                    <div className="no-products">
                        {searchQuery ? `No products found for "${searchQuery}"` : 'No products available'}
                    </div>
                ) : (
                    filteredProducts.map(product => (
                        <div key={product.id} className="product-card">
                            <div className="product-image">
                                {product.primaryImageUrl ? (
                                    <img
                                        src={product.primaryImageUrl}
                                        alt={product.productName}
                                        onError={(e) => {
                                            e.target.style.display = 'none';
                                            e.target.nextSibling.style.display = 'flex';
                                        }}
                                    />
                                ) : null}
                                <div className="no-image" style={{display: product.primaryImageUrl ? 'none' : 'flex'}}>
                                    No Image Available
                                </div>
                            </div>

                            <div className="product-info">
                                <h3>{product.productName}</h3>
                                <p className="product-dimensions">Dimensions: {product.dimensions}</p>
                                <div className="product-price">${product.price?.toFixed(2)}</div>
                                <div className="product-meta">
                                    <small>Added: {new Date(product.createdAt).toLocaleDateString()}</small>
                                    {product.modifiedAt !== product.createdAt && (
                                        <small>Modified: {new Date(product.modifiedAt).toLocaleDateString()}</small>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default ProductsList;