import api from './api';

const adminService = {
  getAllUsers: async () => {
    const response = await api.get('/admin/users');
    return response.data;
  },

  getUsersByRole: async (role) => {
    const response = await api.get(`/admin/users/role/${role}`);
    return response.data;
  },

  deleteUser: async (id) => {
    const response = await api.delete(`/admin/users/${id}`);
    return response.data;
  },

  getAllApps: async () => {
    const response = await api.get('/admin/apps');
    return response.data;
  },

  deactivateApp: async (id) => {
    const response = await api.put(`/admin/apps/${id}/deactivate`);
    return response.data;
  },

  getDashboard: async () => {
    const response = await api.get('/admin/dashboard');
    return response.data;
  },
};

export default adminService;
