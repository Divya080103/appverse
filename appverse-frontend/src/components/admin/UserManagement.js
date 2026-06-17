import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import adminService from '../../services/adminService';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [filterRole, setFilterRole] = useState('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filterRole]);

  const loadUsers = async () => {
    try {
      setLoading(true);
      let data;
      if (filterRole === 'ALL') {
        data = await adminService.getAllUsers();
      } else {
        data = await adminService.getUsersByRole(filterRole);
      }
      setUsers(data);
    } catch (error) {
      toast.error('Error loading users');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id, username) => {
    if (window.confirm(`Are you sure you want to delete user "${username}"?`)) {
      try {
        await adminService.deleteUser(id);
        toast.success('User deleted');
        loadUsers();
      } catch (error) {
        toast.error('Error deleting user');
      }
    }
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">User Management</h2>

      {/* Filter */}
      <div className="mb-3">
        <div className="btn-group">
          {['ALL', 'USER', 'DEVELOPER', 'ADMIN'].map((role) => (
            <button
              key={role}
              className={`btn btn-sm ${filterRole === role ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => setFilterRole(role)}
            >
              {role}
            </button>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="loading-spinner">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <div className="card border-0 shadow-sm">
          <div className="card-body">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead className="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Full Name</th>
                    <th>Role</th>
                    <th>Joined</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td><strong>{user.username}</strong></td>
                      <td>{user.email}</td>
                      <td>{user.fullName}</td>
                      <td>
                        <span className={`badge ${user.role === 'ADMIN' ? 'bg-danger' : user.role === 'DEVELOPER' ? 'bg-primary' : 'bg-secondary'}`}>
                          {user.role}
                        </span>
                      </td>
                      <td>{user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => handleDelete(user.id, user.username)}
                          disabled={user.role === 'ADMIN'}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <p className="text-muted small mt-2">Total: {users.length} users</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserManagement;
