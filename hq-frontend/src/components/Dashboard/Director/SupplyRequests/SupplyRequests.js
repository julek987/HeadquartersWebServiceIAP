import React, { useState, useEffect } from 'react';
import './SupplyRequests.css'; // Import your CSS file

const SupplyRequests = () => {
    const [pendingRequests, setPendingRequests] = useState([]);
    const [expandedRequest, setExpandedRequest] = useState(null);
    const [requestDetails, setRequestDetails] = useState({});
    const [branchNames, setBranchNames] = useState({}); // Store branch names by ID
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null);
    const [error, setError] = useState(null);

    // CSS-based icons as components
    const ChevronDown = () => (
        <span className="inline-block w-4 h-4 border-r-2 border-b-2 border-current transform rotate-45 -translate-y-0.5"></span>
    );

    const ChevronUp = () => (
        <span className="inline-block w-4 h-4 border-r-2 border-b-2 border-current transform -rotate-135 translate-y-0.5"></span>
    );

    const Check = () => (
        <span className="inline-block w-4 h-4 border-b-2 border-r-2 border-current transform rotate-45 translate-x-0.5 -translate-y-1"></span>
    );

    const X = () => (
        <span className="inline-block w-4 h-4 relative">
            <span className="absolute inset-0 border-b-2 border-current transform rotate-45"></span>
            <span className="absolute inset-0 border-b-2 border-current transform -rotate-45"></span>
        </span>
    );

    const Eye = () => (
        <span className="inline-block w-4 h-4 border-2 border-current rounded-full relative">
            <span className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-current rounded-full transform -translate-x-1/2 -translate-y-1/2"></span>
        </span>
    );

    const Calendar = () => (
        <span className="inline-block w-4 h-4 border-2 border-current rounded relative">
            <span className="absolute top-0 left-1 w-0.5 h-1 bg-current"></span>
            <span className="absolute top-0 right-1 w-0.5 h-1 bg-current"></span>
            <span className="absolute top-1.5 left-0.5 right-0.5 h-0.5 bg-current"></span>
        </span>
    );

    const User = () => (
        <span className="inline-block w-4 h-4 relative">
            <span className="absolute top-0 left-1/2 w-2 h-2 border-2 border-current rounded-full transform -translate-x-1/2"></span>
            <span className="absolute bottom-0 left-0 right-0 h-2 border-2 border-current border-t-0 rounded-b-full"></span>
        </span>
    );

    const FileText = () => (
        <span className="inline-block w-4 h-4 border-2 border-current rounded relative">
            <span className="absolute top-1 left-1 right-1 h-0.5 bg-current"></span>
            <span className="absolute top-2 left-1 right-1 h-0.5 bg-current"></span>
            <span className="absolute top-3 left-1 right-2 h-0.5 bg-current"></span>
        </span>
    );

    const Building = () => (
        <span className="inline-block w-4 h-4 border-2 border-current relative">
            <span className="absolute top-1 left-1 w-1 h-1 border border-current"></span>
            <span className="absolute top-1 right-1 w-1 h-1 border border-current"></span>
            <span className="absolute bottom-1 left-1 right-1 h-1 border-t border-current"></span>
        </span>
    );

    // Fetch branch names
    const fetchBranchNames = async () => {
        try {
            const response = await fetch('/api/branch');
            if (!response.ok) throw new Error('Failed to fetch branches');
            const branches = await response.json();

            // Create a map of branch ID to branch name
            const branchNameMap = {};
            branches.forEach(branch => {
                branchNameMap[branch.id] = branch.name;
            });
            setBranchNames(branchNameMap);
        } catch (err) {
            console.error('Error loading branch names:', err.message);
            // Don't set error here as this is not critical - we can fall back to showing IDs
        }
    };

    // Fetch pending requests on component mount
    useEffect(() => {
        const loadData = async () => {
            await Promise.all([
                fetchPendingRequests(),
                fetchBranchNames()
            ]);
        };
        loadData();
    }, []);

    const fetchPendingRequests = async () => {
        try {
            setLoading(true);
            const response = await fetch('/api/supply-requests/pending');
            if (!response.ok) throw new Error('Failed to fetch pending requests');
            const data = await response.json();
            setPendingRequests(data);
        } catch (err) {
            setError('Error loading pending requests: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchRequestDetails = async (requestId) => {
        try {
            const response = await fetch(`/api/supply-requests/${requestId}`);
            if (!response.ok) throw new Error('Failed to fetch request details');
            const data = await response.json();
            setRequestDetails(prev => ({
                ...prev,
                [requestId]: data
            }));
        } catch (err) {
            setError('Error loading request details: ' + err.message);
        }
    };

    const toggleRequestDetails = (requestId) => {
        if (expandedRequest === requestId) {
            setExpandedRequest(null);
        } else {
            setExpandedRequest(requestId);
            // Fetch details if we don't have them yet
            if (!requestDetails[requestId]) {
                fetchRequestDetails(requestId);
            }
        }
    };

    const handleRequestAction = async (requestId, action, annotation = '') => {
        try {
            setActionLoading(requestId);
            const response = await fetch(`/api/supply-requests/${requestId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    status: action === 'accept' ? 'ACCEPTED' : 'REJECTED',
                    annotation: annotation || (action === 'accept' ? 'Request approved' : 'Request rejected')
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || `Failed to ${action} request`);
            }

            // Remove the request from pending list since it's no longer pending
            setPendingRequests(prev => prev.filter(req => req.id !== requestId));

            // Clear expanded state if this request was expanded
            if (expandedRequest === requestId) {
                setExpandedRequest(null);
            }

            // Show success message
            alert(`Request ${action}ed successfully!`);

        } catch (err) {
            setError(`Error ${action}ing request: ` + err.message);
        } finally {
            setActionLoading(null);
        }
    };

    // Helper function to get branch name or fall back to ID
    const getBranchDisplayName = (branchId) => {
        return branchNames[branchId] || `Branch ${branchId}`;
    };

    if (loading) {
        return (
            <div className="loading">
                Loading pending requests...
            </div>
        );
    }

    return (
        <div className="supply-requests-container">
            <div className="page-header">
                <h1>Supply Request Management</h1>
                <p>Review and manage pending supply requests from branches</p>
            </div>

            {error && (
                <div className="error-message">
                    <div>
                        <X />
                        <span>{error}</span>
                    </div>
                    <button
                        onClick={() => setError(null)}
                        className="close-error"
                    >
                        ×
                    </button>
                </div>
            )}

            <div className="requests-section">
                <h2>Pending Requests</h2>

                {pendingRequests.length === 0 ? (
                    <div className="no-requests">
                        <div>
                            <FileText />
                        </div>
                        <h3>No Pending Requests</h3>
                        <p>All supply requests have been processed.</p>
                        <button
                            onClick={fetchPendingRequests}
                            className="back-btn"
                        >
                            Refresh
                        </button>
                    </div>
                ) : (
                    <div className="requests-table">
                        <table>
                            <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Branch</th>
                                <th>Status</th>
                                <th>Branch Request ID</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {pendingRequests.map((request) => (
                                <React.Fragment key={request.id}>
                                    <tr>
                                        <td>#{request.id}</td>
                                        <td>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <Building />
                                                {getBranchDisplayName(request.branchId)}
                                            </div>
                                        </td>
                                        <td>
                                                <span className={`status-badge ${request.status.toLowerCase()}`}>
                                                    {request.status}
                                                </span>
                                        </td>
                                        <td>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <FileText />
                                                #{request.branchRequestID}
                                            </div>
                                        </td>
                                        <td>
                                            <div className="action-buttons">
                                                <button
                                                    onClick={() => toggleRequestDetails(request.id)}
                                                    className="view-btn"
                                                >
                                                    <Eye />
                                                    {expandedRequest === request.id ? 'Hide' : 'View'} Details
                                                </button>
                                                <button
                                                    onClick={() => handleRequestAction(request.id, 'accept')}
                                                    disabled={actionLoading === request.id}
                                                    className="accept-btn"
                                                >
                                                    <Check />
                                                    {actionLoading === request.id ? 'Processing...' : 'Accept'}
                                                </button>
                                                <button
                                                    onClick={() => {
                                                        const annotation = prompt('Enter rejection reason (optional):');
                                                        if (annotation !== null) {
                                                            handleRequestAction(request.id, 'reject', annotation);
                                                        }
                                                    }}
                                                    disabled={actionLoading === request.id}
                                                    className="reject-btn"
                                                >
                                                    <X />
                                                    Reject
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    {expandedRequest === request.id && (
                                        <tr>
                                            <td colSpan="5" style={{ padding: 0 }}>
                                                <div style={{ padding: '2rem', background: '#f8f9fa', borderTop: '1px solid #dee2e6' }}>
                                                    <h4>Order Details</h4>
                                                    {requestDetails[request.id] ? (
                                                        <div className="orders-table">
                                                            <table>
                                                                <thead>
                                                                <tr>
                                                                    <th>Order #</th>
                                                                    <th>Product ID</th>
                                                                    <th>Quantity</th>
                                                                    <th>Branch</th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                {requestDetails[request.id].map((order, index) => (
                                                                    <tr key={order.id}>
                                                                        <td>{index + 1}</td>
                                                                        <td>{order.productId}</td>
                                                                        <td>{order.quantity}</td>
                                                                        <td>{getBranchDisplayName(order.branchId)}</td>
                                                                    </tr>
                                                                ))}
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    ) : (
                                                        <div className="loading">
                                                            Loading order details...
                                                        </div>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    )}
                                </React.Fragment>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};

export default SupplyRequests;