import React, { useState, useEffect } from 'react';
import { userAPI, addressAPI, ROLES, getRoleName } from '../../../services/api';
import './UserManagement.css'; // Make sure to import the CSS file

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        firstName: '', lastName: '', email: '', phoneNumber: '',
        login: '', password: '', role: '', addressId: '', branchId: 1
    });

    useEffect(() => {
        fetchUsers();
        fetchAddresses();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const data = await userAPI.getUsers();
            setUsers(data);
        } catch (err) {
            setError('Failed to fetch users');
        } finally {
            setLoading(false);
        }
    };

    const fetchAddresses = async () => {
        try {
            const data = await addressAPI.getAddresses();
            setAddresses(data);
        } catch (err) {
            console.error('Failed to fetch addresses:', err);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            console.log('Submitting formData:', formData);
            if (editingUser) {
                await userAPI.updateUser(editingUser.id, formData);
            } else {
                await userAPI.addUser(formData);
            }
            fetchUsers();
            resetForm();
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to save user');
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (user) => {
        setEditingUser(user);
        setFormData({
            firstName: user.firstName, lastName: user.lastName,
            email: user.email, phoneNumber: user.phoneNumber || '',
            login: user.login, password: '', role: user.role,
            addressId: user.addressId, branchId: user.branchId || 1
        });
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this user?')) return;
        try {
            await userAPI.deleteUser(id);
            fetchUsers();
        } catch (err) {
            setError('Failed to delete user');
        }
    };

    const resetForm = () => {
        setFormData({
            firstName: '', lastName: '', email: '', phoneNumber: '',
            login: '', password: '', role: '', addressId: '', branchId: 1
        });
        setShowForm(false);
        setEditingUser(null);
        setError('');
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'role' || name === 'addressId' ? parseInt(value) : value
        }));
    };

    if (loading && users.length === 0) return <div>Loading...</div>;

    return (
        <div className="user-management-container">
            <div className="page-header">
                <h1>User Management</h1>
                <div className="header-actions">
                    <button
                        className="btn btn-primary"
                        onClick={() => setShowForm(!showForm)}
                    >
                        {showForm ? 'Cancel' : 'Add User'}
                    </button>
                </div>
            </div>

            {error && (
                <div className="alert alert-error">
                    {error}
                </div>
            )}

            {showForm && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h3>{editingUser ? 'Edit User' : 'Add User'}</h3>
                            <button className="close-btn" onClick={resetForm}>&times;</button>
                        </div>
                        <form onSubmit={handleSubmit} className="user-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label>First Name</label>
                                    <input
                                        className="form-input"
                                        name="firstName"
                                        value={formData.firstName}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Last Name</label>
                                    <input
                                        className="form-input"
                                        name="lastName"
                                        value={formData.lastName}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Email</label>
                                    <input
                                        className="form-input"
                                        name="email"
                                        type="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Phone</label>
                                    <input
                                        className="form-input"
                                        name="phoneNumber"
                                        value={formData.phoneNumber}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Username</label>
                                    <input
                                        className="form-input"
                                        name="login"
                                        value={formData.login}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Password</label>
                                    <input
                                        className="form-input"
                                        name="password"
                                        type="password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required={!editingUser}
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Role</label>
                                    <select
                                        className="form-select"
                                        name="role"
                                        value={formData.role}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="">Select Role</option>
                                        <option value={ROLES.DIRECTOR}>Director</option>
                                        <option value={ROLES.ADMIN}>Admin</option>
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label>Address</label>
                                    <select
                                        className="form-select"
                                        name="addressId"
                                        value={formData.addressId}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="">Select Address</option>
                                        {addresses.map(addr => (
                                            <option key={addr.id} value={addr.id}>
                                                {addr.addressLine1}, {addr.city}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            </div>

                            <div className="form-actions">
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    onClick={resetForm}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className={`btn btn-primary ${loading ? 'loading' : ''}`}
                                    disabled={loading}
                                >
                                    {loading ? '' : (editingUser ? 'Update' : 'Add')}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="table-container">
                <table className="users-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length > 0 ? (
                        users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.firstName} {user.lastName}</td>
                                <td>{user.email}</td>
                                <td>{user.login}</td>
                                <td>
                                        <span className={`role-badge role-${getRoleName(user.role).toLowerCase()}`}>
                                            {getRoleName(user.role)}
                                        </span>
                                </td>
                                <td>
                                    <div className="actions">
                                        <button
                                            className="btn btn-info btn-sm"
                                            onClick={() => handleEdit(user)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleDelete(user.id)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6" className="no-data">
                                No users found. Click "Add User" to create the first user.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserManagement;