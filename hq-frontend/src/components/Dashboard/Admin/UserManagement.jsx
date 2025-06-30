import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './UserManagement.css'

const UserManagement = ({ onBack }) => {
    const [users, setUsers] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [branches, setBranches] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [showEditForm, setShowEditForm] = useState(false);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);
    const [editingUser, setEditingUser] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const [newUser, setNewUser] = useState({
        firstName: '',
        middleName: '',
        lastName: '',
        login: '',
        email: '',
        phoneNumber: '',
        password: 'DefaultPass123!',
        confirmPassword: 'DefaultPass123!',
        addressId: '',
        branchId: '',
        role: 0
    });

    const [editUser, setEditUser] = useState({
        id: '',
        firstName: '',
        middleName: '',
        lastName: '',
        login: '',
        email: '',
        phoneNumber: '',
        addressId: '',
        branchId: '',
        role: 0
    });

    useEffect(() => {
        fetchUsers();
        fetchAddresses();
        fetchBranches();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await axios.get('/api/users');
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
            setError('Failed to fetch users');
        }
    };

    const fetchAddresses = async () => {
        try {
            // Fixed: Use correct endpoint from backend
            const response = await axios.get('/api/addresses');
            setAddresses(response.data);
        } catch (error) {
            console.error('Error fetching addresses:', error);
            setError('Failed to fetch addresses');
        }
    };

    const fetchBranches = async () => {
        try {
            // Assuming you have a branches endpoint
            const response = await axios.get('/api/branches');
            setBranches(response.data);
        } catch (error) {
            console.error('Error fetching branches:', error);
            // Don't set error here as branches might not be implemented yet
            console.warn('Branches endpoint not available');
        }
    };

    const getRoleName = (role) => {
        // Updated role mapping - adjust these based on your actual role values
        switch (role) {
            case 0: return 'Director'; // Based on your comment about having "0 director"
            case 1: return 'Admin';    // Based on your comment about having "1 admin"
            case 2: return 'Manager';
            case 3: return 'User';
            default: return 'Unknown';
        }
    };

    const getRoleClass = (role) => {
        switch (role) {
            case 0: return 'role-director';
            case 1: return 'role-admin';
            case 2: return 'role-manager';
            case 3: return 'role-user';
            default: return 'role-unknown';
        }
    };

    const handleDeleteClick = (user) => {
        setUserToDelete(user);
        setShowDeleteConfirm(true);
    };

    const handleDeleteConfirm = async () => {
        if (!userToDelete) return;

        setLoading(true);
        try {
            await axios.delete(`/api/users/${userToDelete.id}`);
            await fetchUsers();
            setSuccess('User deleted successfully!');
            setTimeout(() => setSuccess(''), 3000);
        } catch (error) {
            console.error('Error deleting user:', error);
            const errorMessage = error.response?.data?.message || error.response?.data || 'Failed to delete user';
            setError(errorMessage);
            setTimeout(() => setError(''), 5000);
        } finally {
            setLoading(false);
            setShowDeleteConfirm(false);
            setUserToDelete(null);
        }
    };

    const handleDeleteCancel = () => {
        setShowDeleteConfirm(false);
        setUserToDelete(null);
    };

    const handleEdit = (user) => {
        setEditingUser(user);
        setEditUser({
            id: user.id,
            firstName: user.firstName,
            middleName: user.middleName || '',
            lastName: user.lastName,
            login: user.login,
            email: user.email,
            phoneNumber: user.phoneNumber,
            addressId: user.addressId || '', // Fixed: use addressId directly
            branchId: user.branchId || '',   // Fixed: use branchId directly
            role: user.role
        });
        setShowEditForm(true);
        setError('');
        setSuccess('');
    };

    const handleInputChange = (e, isEdit = false) => {
        const { name, value } = e.target;
        const setter = isEdit ? setEditUser : setNewUser;

        setter(prev => ({ ...prev, [name]: value }));

        if (error) setError('');
        if (success) setSuccess('');
    };

    const validateForm = (userData, isEdit = false) => {
        if (!userData.firstName.trim()) return 'First name is required';
        if (!userData.lastName.trim()) return 'Last name is required';
        if (!userData.login.trim()) return 'Username is required';
        if (!userData.email.trim()) return 'Email is required';
        if (!userData.phoneNumber.trim()) return 'Phone number is required';
        if (!userData.addressId) return 'Please select an address';
        if (!userData.branchId) return 'Please select a branch';

        if (!isEdit) {
            if (!userData.password.trim()) return 'Password is required';
            if (userData.password !== userData.confirmPassword) return 'Passwords do not match';
            if (userData.password.length < 8) return 'Password must be at least 8 characters long';

            // Password complexity validation to match backend
            const passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/;
            if (!passwordRegex.test(userData.password)) {
                return 'Password must contain at least one digit, one lowercase, one uppercase, and one special character';
            }
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(userData.email)) return 'Please enter a valid email address';

        // Phone number validation to match backend
        const phoneRegex = /^[+]?[0-9]{10,15}$/;
        if (!phoneRegex.test(userData.phoneNumber.replace(/\s/g, ''))) {
            return 'Phone number should be 10-15 digits and may start with +';
        }

        return null;
    };

    const handleAddUser = async (e) => {
        e.preventDefault();

        const validationError = validateForm(newUser);
        if (validationError) {
            setError(validationError);
            return;
        }

        setLoading(true);
        setError('');

        try {
            const { confirmPassword, ...userData } = newUser;

            // Fixed: Send data in the format expected by backend DTO
            const registrationData = {
                firstName: userData.firstName.trim(),
                middleName: userData.middleName.trim() || null,
                lastName: userData.lastName.trim(),
                email: userData.email.trim(),
                phoneNumber: userData.phoneNumber.trim(),
                login: userData.login.trim(),
                password: userData.password,
                role: parseInt(userData.role),
                addressId: parseInt(userData.addressId),
                branchId: parseInt(userData.branchId)
            };

            await axios.post('/api/users', registrationData);
            await fetchUsers();
            setShowAddForm(false);
            setNewUser({
                firstName: '',
                middleName: '',
                lastName: '',
                login: '',
                email: '',
                phoneNumber: '',
                password: 'DefaultPass123!',
                confirmPassword: 'DefaultPass123!',
                addressId: '',
                branchId: '',
                role: 0
            });
            setSuccess('User added successfully!');
            setTimeout(() => setSuccess(''), 3000);

        } catch (error) {
            console.error('Error adding user:', error);
            let errorMessage = 'Failed to add user. Please try again.';

            if (error.response?.data) {
                if (typeof error.response.data === 'string') {
                    errorMessage = error.response.data;
                } else if (error.response.data.message) {
                    errorMessage = error.response.data.message;
                } else if (error.response.data.error) {
                    errorMessage = error.response.data.error;
                }
            }

            setError(errorMessage);
            setTimeout(() => setError(''), 8000);
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateUser = async (e) => {
        e.preventDefault();

        const validationError = validateForm(editUser, true);
        if (validationError) {
            setError(validationError);
            return;
        }

        setLoading(true);
        setError('');

        try {
            // Fixed: Send data in the format expected by backend DTO
            const updateData = {
                firstName: editUser.firstName.trim(),
                middleName: editUser.middleName.trim() || null,
                lastName: editUser.lastName.trim(),
                email: editUser.email.trim(),
                phoneNumber: editUser.phoneNumber.trim(),
                login: editUser.login.trim(),
                role: parseInt(editUser.role),
                addressId: parseInt(editUser.addressId),
                branchId: parseInt(editUser.branchId)
            };

            await axios.put(`/api/users/${editUser.id}`, updateData);
            await fetchUsers();
            setShowEditForm(false);
            setEditingUser(null);
            setSuccess('User updated successfully!');
            setTimeout(() => setSuccess(''), 3000);

        } catch (error) {
            console.error('Error updating user:', error);
            let errorMessage = 'Failed to update user. Please try again.';

            if (error.response?.data) {
                if (typeof error.response.data === 'string') {
                    errorMessage = error.response.data;
                } else if (error.response.data.message) {
                    errorMessage = error.response.data.message;
                } else if (error.response.data.error) {
                    errorMessage = error.response.data.error;
                }
            }

            setError(errorMessage);
            setTimeout(() => setError(''), 5000);
        } finally {
            setLoading(false);
        }
    };

    const closeModal = () => {
        setShowAddForm(false);
        setShowEditForm(false);
        setEditingUser(null);
        setError('');
        setSuccess('');
        setNewUser({
            firstName: '',
            middleName: '',
            lastName: '',
            login: '',
            email: '',
            phoneNumber: '',
            password: 'DefaultPass123!',
            confirmPassword: 'DefaultPass123!',
            addressId: '',
            branchId: '',
            role: 0
        });
    };

    const renderForm = (userData, handleSubmit, handleChange, isEdit = false) => (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h3>{isEdit ? 'Edit User' : 'Add New User'}</h3>
                    <button className="close-btn" onClick={closeModal}>
                        &times;
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="user-form">
                    {error && (
                        <div className="alert alert-error">
                            {error}
                        </div>
                    )}

                    <div className="form-row">
                        <div className="form-group">
                            <label>First Name *</label>
                            <input
                                type="text"
                                name="firstName"
                                value={userData.firstName}
                                onChange={(e) => handleChange(e, isEdit)}
                                required
                                disabled={loading}
                                className="form-input"
                                maxLength={50}
                            />
                        </div>
                        <div className="form-group">
                            <label>Last Name *</label>
                            <input
                                type="text"
                                name="lastName"
                                value={userData.lastName}
                                onChange={(e) => handleChange(e, isEdit)}
                                required
                                disabled={loading}
                                className="form-input"
                                maxLength={50}
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label>Middle Name</label>
                        <input
                            type="text"
                            name="middleName"
                            value={userData.middleName}
                            onChange={(e) => handleChange(e, isEdit)}
                            disabled={loading}
                            className="form-input"
                            maxLength={50}
                        />
                    </div>

                    <div className="form-group">
                        <label>Username *</label>
                        <input
                            type="text"
                            name="login"
                            value={userData.login}
                            onChange={(e) => handleChange(e, isEdit)}
                            required
                            disabled={loading}
                            className="form-input"
                            minLength={3}
                            maxLength={30}
                        />
                    </div>

                    <div className="form-group">
                        <label>Email *</label>
                        <input
                            type="email"
                            name="email"
                            value={userData.email}
                            onChange={(e) => handleChange(e, isEdit)}
                            required
                            disabled={loading}
                            className="form-input"
                            maxLength={100}
                        />
                    </div>

                    <div className="form-group">
                        <label>Phone Number *</label>
                        <input
                            type="tel"
                            name="phoneNumber"
                            value={userData.phoneNumber}
                            onChange={(e) => handleChange(e, isEdit)}
                            required
                            disabled={loading}
                            className="form-input"
                            maxLength={20}
                            placeholder="+1234567890 or 1234567890"
                        />
                    </div>

                    {!isEdit && (
                        <div className="form-row">
                            <div className="form-group">
                                <label>Password *</label>
                                <input
                                    type="password"
                                    name="password"
                                    value={userData.password}
                                    onChange={(e) => handleChange(e, isEdit)}
                                    required
                                    disabled={loading}
                                    className="form-input"
                                    minLength={8}
                                />
                                <small className="form-help">
                                    Must contain at least one digit, lowercase, uppercase, and special character
                                </small>
                            </div>
                            <div className="form-group">
                                <label>Confirm Password *</label>
                                <input
                                    type="password"
                                    name="confirmPassword"
                                    value={userData.confirmPassword}
                                    onChange={(e) => handleChange(e, isEdit)}
                                    required
                                    disabled={loading}
                                    className="form-input"
                                />
                            </div>
                        </div>
                    )}

                    <div className="form-group">
                        <label>Branch *</label>
                        <select
                            name="branchId"
                            value={userData.branchId}
                            onChange={(e) => handleChange(e, isEdit)}
                            required
                            disabled={loading}
                            className="form-select"
                        >
                            <option value="">Select a branch...</option>
                            {branches.map(branch => (
                                <option key={branch.id} value={branch.id}>
                                    {branch.name || `Branch ${branch.id}`}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Address *</label>
                        <select
                            name="addressId"
                            value={userData.addressId}
                            onChange={(e) => handleChange(e, isEdit)}
                            required
                            disabled={loading}
                            className="form-select"
                        >
                            <option value="">Select an address...</option>
                            {addresses.map(address => (
                                <option key={address.id} value={address.id}>
                                    {address.addressLine1}, {address.city}, {address.postalCode}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Role *</label>
                        <select
                            name="role"
                            value={userData.role}
                            onChange={(e) => handleChange(e, isEdit)}
                            disabled={loading}
                            className="form-select"
                        >
                            <option value={0}>Director</option>
                            <option value={1}>Admin</option>
                            <option value={2}>Manager</option>
                            <option value={3}>User</option>
                        </select>
                    </div>

                    <div className="form-actions">
                        <button
                            type="submit"
                            disabled={loading}
                            className={`btn btn-primary ${loading ? 'loading' : ''}`}
                        >
                            {loading ? (isEdit ? 'Updating...' : 'Creating...') : (isEdit ? 'Update User' : 'Create User')}
                        </button>
                        <button
                            type="button"
                            onClick={closeModal}
                            disabled={loading}
                            className="btn btn-secondary"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );

    return (
        <div className="user-management-container">
            <div className="page-header">
                <h1>User Management</h1>
                <div className="header-actions">
                    <button
                        onClick={() => setShowAddForm(true)}
                        className="btn btn-primary"
                    >
                        + Add New User
                    </button>
                    <button
                        onClick={onBack}
                        className="btn btn-secondary"
                    >
                        ← Back to Dashboard
                    </button>
                </div>
            </div>

            {/* Success/Error Messages */}
            {success && (
                <div className="alert alert-success">
                    {success}
                </div>
            )}
            {error && !showAddForm && !showEditForm && (
                <div className="alert alert-error">
                    {error}
                </div>
            )}

            {/* Add User Modal */}
            {showAddForm && renderForm(newUser, handleAddUser, handleInputChange)}

            {/* Edit User Modal */}
            {showEditForm && renderForm(editUser, handleUpdateUser, handleInputChange, true)}

            {/* Delete Confirmation Modal */}
            {showDeleteConfirm && userToDelete && (
                <div className="modal-overlay">
                    <div className="modal-content delete-modal">
                        <div className="modal-header">
                            <h3>Confirm Delete</h3>
                            <button className="close-btn" onClick={handleDeleteCancel}>
                                &times;
                            </button>
                        </div>

                        <div className="delete-content">
                            <div className="warning-icon">⚠️</div>
                            <p>Are you sure you want to delete this user?</p>
                            <div className="user-info">
                                <strong>{userToDelete.firstName} {userToDelete.lastName}</strong>
                                <br />
                                <span>{userToDelete.email}</span>
                            </div>
                            <p className="warning-text">This action cannot be undone.</p>
                        </div>

                        <div className="form-actions">
                            <button
                                onClick={handleDeleteConfirm}
                                disabled={loading}
                                className={`btn btn-danger ${loading ? 'loading' : ''}`}
                            >
                                {loading ? 'Deleting...' : 'Yes, Delete'}
                            </button>
                            <button
                                onClick={handleDeleteCancel}
                                disabled={loading}
                                className="btn btn-secondary"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Users Table */}
            <div className="table-container">
                <table className="users-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Branch</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length === 0 ? (
                        <tr>
                            <td colSpan="10" className="no-data">
                                No users found
                            </td>
                        </tr>
                    ) : (
                        users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.firstName}</td>
                                <td>{user.lastName}</td>
                                <td>{user.login}</td>
                                <td>{user.email}</td>
                                <td>{user.phoneNumber}</td>
                                <td>
                                    <span className={`role-badge ${getRoleClass(user.role)}`}>
                                        {getRoleName(user.role)}
                                    </span>
                                </td>
                                <td>{user.branchId || 'N/A'}</td>
                                <td>
                                    <span className={`status-badge ${user.active ? 'status-active' : 'status-inactive'}`}>
                                        {user.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td className="actions">
                                    <button
                                        onClick={() => handleEdit(user)}
                                        disabled={loading}
                                        className="btn btn-sm btn-info"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDeleteClick(user)}
                                        disabled={loading}
                                        className="btn btn-sm btn-danger"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserManagement;