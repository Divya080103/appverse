import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import adminService from '../../services/adminService';

const AdminDashboard = () => {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const data = await adminService.getDashboard();
      setDashboard(data);
    } catch (error) {
      toast.error('Error loading dashboard');
    } finally {
      setLoading(false);
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
      <h2 className="mb-4">Admin Dashboard</h2>

      {/* Stats Overview */}
      <div className="row mb-4">
        <div className="col-md-4 mb-3">
          <div className="stat-card downloads">
            <h3>{dashboard?.totalUsers || 0}</h3>
            <p className="mb-0">Total Users</p>
          </div>
        </div>
        <div className="col-md-4 mb-3">
          <div className="stat-card apps">
            <h3>{dashboard?.totalApps || 0}</h3>
            <p className="mb-0">Total Apps</p>
          </div>
        </div>
        <div className="col-md-4 mb-3">
          <div className="stat-card rating">
            <h3>{dashboard?.totalCategories || 0}</h3>
            <p className="mb-0">Categories</p>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="row">
        <div className="col-md-6 mb-3">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <h5>User Management</h5>
              <p className="text-muted">Manage users, roles, and permissions</p>
              <Link to="/admin/users" className="btn btn-primary">
                Manage Users
              </Link>
            </div>
          </div>
        </div>
        <div className="col-md-6 mb-3">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <h5>App Management</h5>
              <p className="text-muted">Review and manage published apps</p>
              <Link to="/apps" className="btn btn-outline-primary">
                View Apps
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Recent Users */}
      {dashboard?.users && (
        <div className="card border-0 shadow-sm mt-4">
          <div className="card-body">
            <h5>Recent Users</h5>
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Joined</th>
                  </tr>
                </thead>
                <tbody>
                  {dashboard.users.slice(0, 5).map((user) => (
                    <tr key={user.id}>
                      <td>{user.username}</td>
                      <td>{user.email}</td>
                      <td>
                        <span className={`badge ${user.role === 'ADMIN' ? 'bg-danger' : user.role === 'DEVELOPER' ? 'bg-primary' : 'bg-secondary'}`}>
                          {user.role}
                        </span>
                      </td>
                      <td>{user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
