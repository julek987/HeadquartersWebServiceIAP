import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './components/Layout/Header/header.css'
import './components/Layout/Footer/footer.css'
import Register from './components/Auth/Register/Register';
import Login from './components/Auth/Login/Login';
import Welcome from './components/Dashboard/Welcome';
import { authUtils } from './services/api';
import './App.css';
import Header from './components/Layout/Header/Header';
import Footer from './components/Layout/Footer/Footer';
import About from "./components/About/About";
import DirectorDashboard from "./components/Dashboard/Director/DirectorDashboard";
import AdminDashboard from "./components/Dashboard/Admin/AdminDashboard";


// Protected Route component with role checking
const ProtectedRoute = ({ children, allowedRoles = [] }) => {
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
    if (!authUtils.isAuthenticated()) {
        return children;
    }

    // Redirect to role-specific dashboard
    const userRole = authUtils.getUserRole();
    switch(userRole) {
        case 0: return <Navigate to="/user-dashboard" />;
        case 1: return <Navigate to="/manager-dashboard" />;
        case 2: return <Navigate to="/admin-dashboard" />;
        default: return <Navigate to="/dashboard" />;
    }
};

// Component to redirect authenticated users to their appropriate dashboard
const DashboardRedirect = () => {
    const userRole = authUtils.getUserRole();

    switch(userRole) {
        case 0: return <Navigate to="/user-dashboard" replace />;
        case 1: return <Navigate to="/manager-dashboard" replace />;
        case 2: return <Navigate to="/admin-dashboard" replace />;
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

                        {/*<Route path="/user-dashboard" element={<ProtectedRoute allowedRoles={[0]}><UserDashboard /></ProtectedRoute>} />*/}


                        {/*<Route path="/products-for-sales" element={<ProtectedRoute allowedRoles={[0]}><ProductsPageforSales /></ProtectedRoute>} />*/}
                        {/*<Route path="/checkout" element={<ProtectedRoute allowedRoles={[0]}><CheckoutPage /></ProtectedRoute>} />*/}
                        {/*<Route path="/sales-analytics" element={<ProtectedRoute allowedRoles={[1]}><SalesAnalytics /></ProtectedRoute>} />*/}

                        <Route path="/director-dashboard" element={<ProtectedRoute allowedRoles={[0]}><DirectorDashboard /></ProtectedRoute>} />
                        <Route path="/admin-dashboard" element={<ProtectedRoute allowedRoles={[1]}><AdminDashboard /></ProtectedRoute>} />

                        <Route path="/dashboard" element={<ProtectedRoute><DashboardRedirect /></ProtectedRoute>} />

                        {/*<Route path="/unauthorized" element={<div className="not-found"><h2>Access Denied</h2><p>You don't have permission to access this page.</p></div>} />*/}

                        {/*<Route path="/" element={authUtils.isAuthenticated() ? <DashboardRedirect /> : <Navigate to="/login" />} />*/}
                        <Route path="/" element={authUtils.isAuthenticated() ? <DashboardRedirect /> : <Navigate to="/login" />} />

                        <Route path="/about" element={<About />} />

                        <Route path="*" element={<div className="not-found"><h2>Page Not Found</h2><p>The page you're looking for doesn't exist.</p></div>} />

                        {/*<Route path="/users" element={<ProtectedRoute allowedRoles={[2]}><UserTable /></ProtectedRoute>} />*/}
                        {/*<Route path="/users-readonly" element={<ProtectedRoute allowedRoles={[1]}><UserTableManager /></ProtectedRoute>} />*/}
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;