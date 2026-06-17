import React from 'react';

const Footer = () => {
  return (
    <footer className="footer mt-auto">
      <div className="container">
        <div className="row">
          <div className="col-md-4">
            <h5>🚀 AppVerse</h5>
            <p className="text-muted">Smart App Marketplace & Analytics Platform</p>
          </div>
          <div className="col-md-4">
            <h6>Quick Links</h6>
            <ul className="list-unstyled">
              <li><a href="/apps" className="text-muted text-decoration-none">Browse Apps</a></li>
              <li><a href="/register" className="text-muted text-decoration-none">Become a Developer</a></li>
            </ul>
          </div>
          <div className="col-md-4">
            <h6>Contact</h6>
            <p className="text-muted mb-0">support@appverse.com</p>
          </div>
        </div>
        <hr className="my-3" style={{ borderColor: '#636e72' }} />
        <p className="text-center text-muted mb-0">
          &copy; {new Date().getFullYear()} AppVerse. All rights reserved.
        </p>
      </div>
    </footer>
  );
};

export default Footer;
