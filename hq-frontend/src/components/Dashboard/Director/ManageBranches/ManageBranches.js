import React, { useState, useEffect } from 'react';
import { branchAPI, addressAPI, userAPI } from '../../../../services/api';
import './ManageBranches.css';

const ManageBranches = () => {
    const [branches, setBranches] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [editingBranch, setEditingBranch] = useState(null);
    const [formData, setFormData] = useState({
        name: '',
        active: true,
        addressId: '',
        managerId: ''
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            setError(''); // Clear previous errors

            // Fetch branches first (most critical)
            console.log('Fetching branches...');
            const branchesData = await branchAPI.getBranches();
            setBranches(branchesData);
            console.log('Branches fetched successfully:', branchesData?.length);

            // Fetch addresses and users in parallel, but don't fail the whole operation if they error
            const [addressesResult, usersResult] = await Promise.allSettled([
                addressAPI.getAddresses().then(data => {
                    console.log('Addresses fetched successfully:', data?.length);
                    return data;
                }),
                userAPI.getUsers().then(data => {
                    console.log('Users fetched successfully:', data?.length);
                    return data;
                })
            ]);

            if (addressesResult.status === 'fulfilled') {
                setAddresses(addressesResult.value || []);
            } else {
                console.error('Failed to fetch addresses:', addressesResult.reason);
                setAddresses([]); // Fallback to empty array
                // Optionally show a warning but don't fail the whole operation
                console.warn('Addresses unavailable - address column will show "No address"');
            }

            if (usersResult.status === 'fulfilled') {
                setUsers(usersResult.value || []);
            } else {
                console.error('Failed to fetch users:', usersResult.reason);
                setUsers([]); // Fallback to empty array
                // Optionally show a warning but don't fail the whole operation
                console.warn('Users unavailable - manager column will show "No manager"');
            }

        } catch (error) {
            console.error('Critical error fetching branches:', error);
            setError('Failed to fetch branches: ' + error.message);
            setBranches([]); // Ensure we have an empty array if branches fail
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const branchData = {
                name: formData.name,
                active: formData.active,
                addressId: formData.addressId ? parseInt(formData.addressId) : null,
                managerId: formData.managerId ? parseInt(formData.managerId) : null
            };

            if (editingBranch) {
                await branchAPI.updateBranch(editingBranch.id, branchData);
                setSuccess('Branch updated successfully!');
            } else {
                await branchAPI.addBranch(branchData);
                setSuccess('Branch added successfully!');
            }

            fetchData();
            resetForm();
            setShowModal(false);
        } catch (error) {
            setError('Failed to save branch: ' + error.message);
        }
    };

    const handleEdit = (branch) => {
        setEditingBranch(branch);
        setFormData({
            name: branch.name,
            active: branch.active,
            addressId: branch.addressId || '',
            managerId: branch.managerId || ''
        });
        setShowModal(true);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this branch?')) {
            try {
                await branchAPI.deleteBranch(id);
                setSuccess('Branch deleted successfully!');
                fetchData();
            } catch (error) {
                setError('Failed to delete branch: ' + error.message);
            }
        }
    };

    const resetForm = () => {
        setFormData({
            name: '',
            active: true,
            addressId: '',
            managerId: ''
        });
        setEditingBranch(null);
    };

    const getAddressString = (addressId) => {
        const address = addresses.find(addr => addr.id === addressId);
        if (!address) return 'No address';
        return `${address.street || ''} ${address.city || ''} ${address.state || ''} ${address.zipCode || ''}`.trim();
    };

    const getManagerName = (managerId) => {
        const manager = users.find(user => user.id === managerId);
        return manager ? `${manager.firstName || ''} ${manager.lastName || ''}`.trim() : 'No manager';
    };

    if (loading) {
        return <div className="loading">Loading branches...</div>;
    }

    return (
        <div className="manage-branches-container">
            <div className="header">
                <h1>Manage Branches</h1>
                <button
                    className="btn btn-primary"
                    onClick={() => {
                        resetForm();
                        setShowModal(true);
                    }}
                >
                    Add New Branch
                </button>
            </div>

            {error && (
                <div className="alert alert-error">
                    {error}
                    <button onClick={() => setError('')} className="alert-close">&times;</button>
                </div>
            )}

            {success && (
                <div className="alert alert-success">
                    {success}
                    <button onClick={() => setSuccess('')} className="alert-close">&times;</button>
                </div>
            )}

            <div className="branches-table-container">
                <table className="branches-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Status</th>
                        <th>Address</th>
                        <th>Manager</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {branches.length === 0 ? (
                        <tr>
                            <td colSpan="6" className="no-data">No branches found</td>
                        </tr>
                    ) : (
                        branches.map(branch => (
                            <tr key={branch.id}>
                                <td>{branch.id}</td>
                                <td>{branch.name}</td>
                                <td>
                                    <span className={`status ${branch.active ? 'active' : 'inactive'}`}>
                                        {branch.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>{getAddressString(branch.addressId)}</td>
                                <td>{getManagerName(branch.managerId)}</td>
                                <td>
                                    <div className="action-buttons">
                                        <button
                                            className="btn btn-sm btn-edit"
                                            onClick={() => handleEdit(branch)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-sm btn-delete"
                                            onClick={() => handleDelete(branch.id)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>

            {/* Modal */}
            {showModal && (
                <div className="modal-overlay">
                    <div className="modal">
                        <div className="modal-header">
                            <h2>{editingBranch ? 'Edit Branch' : 'Add New Branch'}</h2>
                            <button
                                className="modal-close"
                                onClick={() => {
                                    setShowModal(false);
                                    resetForm();
                                }}
                            >
                                &times;
                            </button>
                        </div>
                        <form onSubmit={handleSubmit}>
                            <div className="modal-body">
                                <div className="form-group">
                                    <label htmlFor="name">Branch Name*</label>
                                    <input
                                        type="text"
                                        id="name"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleInputChange}
                                        required
                                        maxLength="100"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="addressId">Address</label>
                                    <select
                                        id="addressId"
                                        name="addressId"
                                        value={formData.addressId}
                                        onChange={handleInputChange}
                                    >
                                        <option value="">Select an address</option>
                                        {addresses.map(address => (
                                            <option key={address.id} value={address.id}>
                                                {getAddressString(address.id)}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="managerId">Manager</label>
                                    <select
                                        id="managerId"
                                        name="managerId"
                                        value={formData.managerId}
                                        onChange={handleInputChange}
                                    >
                                        <option value="">Select a manager</option>
                                        {users.map(user => (
                                            <option key={user.id} value={user.id}>
                                                {user.firstName} {user.lastName} ({user.username})
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label className="checkbox-label">
                                        <input
                                            type="checkbox"
                                            name="active"
                                            checked={formData.active}
                                            onChange={handleInputChange}
                                        />
                                        Active
                                    </label>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    onClick={() => {
                                        setShowModal(false);
                                        resetForm();
                                    }}
                                >
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary">
                                    {editingBranch ? 'Update Branch' : 'Add Branch'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ManageBranches;