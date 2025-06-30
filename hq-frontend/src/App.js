import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './components/Layout/Header/header.css'
import './components/Layout/Footer/footer.css'
import Register from './components/Auth/Register/Register';
import Login from './components/Auth/Login/Login';
import Welcome from './components/Dashboard/Welcome';
import * as apiServices from './services/api'; // Import everything from api
import './App.css';
import Header from './components/Layout/Header/Header';
import Footer from './components/Layout/Footer/Footer';
import About from "./components/About/About";
import DirectorDashboard from "./components/Dashboard/Director/DirectorDashboard";
import AdminDashboard from "./components/Dashboard/Admin/AdminDashboard";
import AddProducts from "./components/Dashboard/Director/AddProducts/AddProducts";
import ProductsList from "./components/Dashboard/Director/ProductsList/ProductsList";
import SupplyRequests from "./components/Dashboard/Director/SupplyRequests/SupplyRequests";

// Get authUtils from the imported services
const { authUtils } = apiServices;

// Protected Route component with role checking
const ProtectedRoute = ({ children, allowedRoles = [] }) => {
    // Add safety check
    if (!authUtils) {
        console.error('authUtils is not available');
        return <Navigate to="/login" />;
    }

    const authData = authUtils.getAuthData();

    if (!authUtils.isAuthenticated()) {
        return <Navigate to="/login" />;
    }

    // If specific roles are required, check user role
    if (allowedRoles.length > 0 && !allowedRoles.includes(authData.user.role)) {
        return <Navigate to="/unauthorized" />;
    }

    return children;
};

// Public Route component (redirect to appropriate dashboard if already authenticated)
const PublicRoute = ({ children }) => {
    // Add safety check
    if (!authUtils) {
        return children;
    }

    if (!authUtils.isAuthenticated()) {
        return children;
    }

    // Redirect to role-specific dashboard based on your current role system
    const userRole = authUtils.getUserRole();
    switch(userRole) {
        case 0: return <Navigate to="/director-dashboard" />; // Director
        case 1: return <Navigate to="/admin-dashboard" />;    // Admin
        default: return <Navigate to="/dashboard" />;
    }
};

// Component to redirect authenticated users to their appropriate dashboard
const DashboardRedirect = () => {
    // Add safety check
    if (!authUtils) {
        return <Navigate to="/login" />;
    }

    const userRole = authUtils.getUserRole();

    switch(userRole) {
        case 0: return <Navigate to="/director-dashboard" replace />; // Director
        case 1: return <Navigate to="/admin-dashboard" replace />;    // Admin
        default: return <Welcome />;
    }
};

function App() {
    return (
        <Router>
            <div className="App">
                <Header />
                <main className="main-content">
                    <Routes>
                        <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />
                        <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />

                        {/* Role-based dashboard routes */}
                        <Route path="/director-dashboard" element={<ProtectedRoute allowedRoles={[0]}><DirectorDashboard /></ProtectedRoute>} />
                        <Route path="/admin-dashboard" element={<ProtectedRoute allowedRoles={[1]}><AdminDashboard /></ProtectedRoute>} />

                        {/* Product management routes - only for Directors */}
                        <Route path="/add-products" element={<ProtectedRoute allowedRoles={[0]}><AddProducts /></ProtectedRoute>} />
                        <Route path="/products-list" element={<ProtectedRoute allowedRoles={[0]}><ProductsList /></ProtectedRoute>} />

                        {/* Supply requests route - only for Directors */}
                        <Route path="/supply-requests" element={<ProtectedRoute allowedRoles={[0]}><SupplyRequests /></ProtectedRoute>} />

                        {/* General dashboard redirect */}
                        <Route path="/dashboard" element={<ProtectedRoute><DashboardRedirect /></ProtectedRoute>} />

                        {/* Unauthorized access page */}
                        <Route path="/unauthorized" element={
                            <div className="not-found">
                                <h2>Access Denied</h2>
                                <p>You don't have permission to access this page.</p>
                                <p>Your role: {authUtils && authUtils.getUserRole() !== null ?
                                    (authUtils.getUserRole() === 0 ? 'Director' :
                                        authUtils.getUserRole() === 1 ? 'Admin' : 'Unknown')
                                    : 'Not authenticated'}
                                </p>
                            </div>
                        } />

                        {/* Root route */}
                        <Route path="/" element={authUtils && authUtils.isAuthenticated() ? <DashboardRedirect /> : <Navigate to="/login" />} />

                        {/* Public pages */}
                        <Route path="/about" element={<About />} />

                        {/* 404 page */}
                        <Route path="*" element={
                            <div className="not-found">
                                <h2>Page Not Found</h2>
                                <p>The page you're looking for doesn't exist.</p>
                            </div>
                        } />

                        {/* Future routes - uncommented and ready to use */}
                        {/*<Route path="/users" element={<ProtectedRoute allowedRoles={[1]}><UserTable /></ProtectedRoute>} />*/}
                        {/*<Route path="/users-readonly" element={<ProtectedRoute allowedRoles={[0]}><UserTableManager /></ProtectedRoute>} />*/}
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;