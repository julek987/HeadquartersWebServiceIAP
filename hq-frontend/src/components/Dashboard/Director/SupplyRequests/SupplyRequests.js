import React, { useState, useEffect } from 'react';

const SupplyRequests = () => {
    const [pendingRequests, setPendingRequests] = useState([]);
    const [expandedRequest, setExpandedRequest] = useState(null);
    const [requestDetails, setRequestDetails] = useState({});
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

    // Fetch pending requests on component mount
    useEffect(() => {
        fetchPendingRequests();
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

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-6xl mx-auto p-6 bg-gray-50 min-h-screen">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-2">Supply Request Management</h1>
                <p className="text-gray-600">Review and manage pending supply requests from branches</p>
            </div>

            {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                    <div className="flex items-center">
                        <X />
                        <p className="text-red-800 ml-2">{error}</p>
                    </div>
                    <button
                        onClick={() => setError(null)}
                        className="mt-2 text-sm text-red-600 hover:text-red-800 underline"
                    >
                        Dismiss
                    </button>
                </div>
            )}

            {pendingRequests.length === 0 ? (
                <div className="text-center py-12">
                    <div className="mx-auto mb-4 w-16 h-16 flex items-center justify-center">
                        <FileText />
                    </div>
                    <h3 className="text-xl font-medium text-gray-900 mb-2">No Pending Requests</h3>
                    <p className="text-gray-500">All supply requests have been processed.</p>
                    <button
                        onClick={fetchPendingRequests}
                        className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                    >
                        Refresh
                    </button>
                </div>
            ) : (
                <div className="space-y-4">
                    {pendingRequests.map((request) => (
                        <div key={request.id} className="bg-white rounded-lg shadow-md border border-gray-200">
                            {/* Request Header */}
                            <div className="p-6 border-b border-gray-200">
                                <div className="flex justify-between items-start">
                                    <div className="flex-1">
                                        <div className="flex items-center gap-4 mb-3">
                                            <h3 className="text-lg font-semibold text-gray-900">
                                                Request #{request.id}
                                            </h3>
                                            <span className="px-3 py-1 bg-yellow-100 text-yellow-800 text-sm font-medium rounded-full">
                        {request.status}
                      </span>
                                        </div>

                                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600">
                                            <div className="flex items-center gap-2">
                                                <User />
                                                <span>Branch ID: {request.branchId}</span>
                                            </div>
                                            <div className="flex items-center gap-2">
                                                <Calendar />
                                                <span>Created: {formatDate(request.createdAt)}</span>
                                            </div>
                                            <div className="flex items-center gap-2">
                                                <FileText />
                                                <span>Branch Request: #{request.branchRequestID}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="flex items-center gap-2 ml-4">
                                        <button
                                            onClick={() => toggleRequestDetails(request.id)}
                                            className="flex items-center gap-2 px-3 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
                                        >
                                            <Eye />
                                            View Details
                                            {expandedRequest === request.id ? <ChevronUp /> : <ChevronDown />}
                                        </button>
                                    </div>
                                </div>

                                {/* Action Buttons */}
                                <div className="flex gap-3 mt-4">
                                    <button
                                        onClick={() => handleRequestAction(request.id, 'accept')}
                                        disabled={actionLoading === request.id}
                                        className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                                    >
                                        <Check />
                                        {actionLoading === request.id ? 'Processing...' : 'Accept'}
                                    </button>

                                    <button
                                        onClick={() => {
                                            const annotation = prompt('Enter rejection reason (optional):');
                                            if (annotation !== null) { // User didn't cancel
                                                handleRequestAction(request.id, 'reject', annotation);
                                            }
                                        }}
                                        disabled={actionLoading === request.id}
                                        className="flex items-center gap-2 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                                    >
                                        <X />
                                        Reject
                                    </button>
                                </div>
                            </div>

                            {/* Expandable Request Details */}
                            {expandedRequest === request.id && (
                                <div className="p-6 bg-gray-50">
                                    <h4 className="text-md font-semibold text-gray-900 mb-4">Order Details</h4>

                                    {requestDetails[request.id] ? (
                                        <div className="space-y-3">
                                            {requestDetails[request.id].map((order, index) => (
                                                <div key={order.id} className="bg-white p-4 rounded-lg border border-gray-200">
                                                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4 text-sm">
                                                        <div>
                                                            <span className="font-medium text-gray-700">Order #{index + 1}</span>
                                                        </div>
                                                        <div>
                                                            <span className="text-gray-600">Product ID: </span>
                                                            <span className="font-medium">{order.productId}</span>
                                                        </div>
                                                        <div>
                                                            <span className="text-gray-600">Quantity: </span>
                                                            <span className="font-medium">{order.quantity}</span>
                                                        </div>
                                                        <div>
                                                            <span className="text-gray-600">Branch: </span>
                                                            <span className="font-medium">{order.branchId}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    ) : (
                                        <div className="flex justify-center py-8">
                                            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default SupplyRequests;