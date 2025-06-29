import React from 'react';
import './About.css';

const About = () => {
    return (
        <div className="about-container">
            {/* Section 1 - Who We Are */}
            <section className="about-section split-section">
                <div className="about-image image-circle">
                    <img src="/static/img/minimalist_lr.jpg" alt="Who We Are" />
                </div>
                <div className="about-content">
                    <h2>Who We Are</h2>
                    <p>
                        At <strong>Luxe Living</strong>, we design and deliver premium, hand-crafted furniture
                        tailored to elevate your home and lifestyle. With decades of experience in artisanal
                        techniques and cutting-edge design, we seamlessly blend form and function.
                    </p>
                </div>
            </section>

            {/* Section 2 - Our Craft */}
            <section className="about-section split-section reverse">
                <div className="about-image image-square">
                    <img src="/static/img/marble.jpeg" alt="Our Craft" />
                </div>
                <div className="about-content">
                    <h2>Our Craft</h2>
                    <p>
                        Each piece is born from a passion for detail, with premium materials sourced
                        sustainably. Our team of artisans ensures that every curve, texture, and joint
                        represents perfection.
                    </p>
                </div>
            </section>

            {/* Section 3 - Our Vision */}
            <section className="about-section split-section">
                <div className="about-image image-circle">
                    <img src="/static/img/marketing.jpg" alt="Our Vision" />
                </div>
                <div className="about-content">
                    <h2>Our Vision</h2>
                    <p>
                        We believe in creating furniture that inspires. Whether it's a grand statement sofa or
                        a minimalist oak table, Luxe Living sets the tone for tasteful living and timeless
                        design.
                    </p>
                </div>
            </section>
        </div>
    );
};

export default About;

