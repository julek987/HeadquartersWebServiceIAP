import React from 'react';
import './footer.css';
import {Link} from "react-router-dom";

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-container">
                <div className="footer-content">
                    <div className="footer-section">
                        <h3>Headquarters Management</h3>
                        <p>
                            A modern web application built with React, featuring
                            role-based authentication and elegant design.
                        </p>
                        <p>
                            Empowering users with secure, intuitive and
                            beautiful digital experiences.
                        </p>
                    </div>

                    <div className="footer-section">
                        <h4>Quick Links</h4>
                        <ul>
                            <li><a href="/dashboard">Dashboard</a></li>
                            <li><Link to="/about">About</Link></li>
                        </ul>
                    </div>

                    <div className="footer-section">
                        <h4>Contact</h4>
                        <p>Email: luxeliving@furniture.com</p>
                        <p>Phone: (555) 123-4567</p>
                        <p>Address: 123 Main St, Lodz</p>
                    </div>
                </div>

                <div className="footer-bottom">
                    <p>&copy; {new Date().getFullYear()} LuxeLiving. All rights reserved.</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;