import api from './api';

const developerService = {
  getMyApps: async () => {
    const response = await api.get('/developer/apps');
    return response.data;
  },

  publishApp: async (appData) => {
    const response = await api.post('/developer/apps', appData);
    return response.data;
  },

  updateApp: async (id, appData) => {
    const response = await api.put(`/developer/apps/${id}`, appData);
    return response.data;
  },

  deleteApp: async (id) => {
    const response = await api.delete(`/developer/apps/${id}`);
    return response.data;
  },

  addVersion: async (appId, versionData) => {
    const response = await api.post(`/developer/apps/${appId}/versions`, versionData);
    return response.data;
  },

  getVersions: async (appId) => {
    const response = await api.get(`/developer/apps/${appId}/versions`);
    return response.data;
  },

  getAnalytics: async () => {
    const response = await api.get('/developer/analytics');
    return response.data;
  },
};

export default developerService;
