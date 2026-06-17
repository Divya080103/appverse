import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import developerService from '../../services/developerService';

const DeveloperDashboard = () => {
  const [apps, setApps] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadApps();
  }, []);

  const loadApps = async () => {
    try {
      const data = await developerService.getMyApps();
      setApps(data);
    } catch (error) {
      toast.error('Error loading your apps');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this app?')) {
      try {
        await developerService.deleteApp(id);
        toast.success('App deleted successfully');
        loadApps();
      } catch (error) {
        toast.error('Error deleting app');
      }
    }
  };

  if (loading) {
    return (
      <div className="loading-spinner">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Developer Console</h2>
        <Link to="/developer/upload" className="btn btn-primary">
          + Publish New App
        </Link>
      </div>

      {apps.length === 0 ? (
        <div className="text-center py-5">
          <h5 className="text-muted">No apps published yet</h5>
          <p>Start by publishing your first app!</p>
          <Link to="/developer/upload" className="btn btn-primary">
            Publish App
          </Link>
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover">
            <thead className="table-light">
              <tr>
                <th>App Name</th>
                <th>Category</th>
                <th>Version</th>
                <th>Downloads</th>
                <th>Rating</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {apps.map((app) => (
                <tr key={app.id}>
                  <td><strong>{app.name}</strong></td>
                  <td><span className="category-badge">{app.categoryName}</span></td>
                  <td>{app.currentVersion}</td>
                  <td>{app.downloadCount || 0}</td>
                  <td>
                    <span className="text-warning">★</span> {app.averageRating?.toFixed(1) || '0.0'}
                  </td>
                  <td>
                    <span className={`badge ${app.active ? 'bg-success' : 'bg-secondary'}`}>
                      {app.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    <Link to={`/developer/manage/${app.id}`} className="btn btn-sm btn-outline-primary me-1">
                      Manage
                    </Link>
                    <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(app.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default DeveloperDashboard;
