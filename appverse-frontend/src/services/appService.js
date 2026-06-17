import api from './api';

const appService = {
  getAllApps: async () => {
    const response = await api.get('/apps');
    return response.data;
  },

  getAppById: async (id) => {
    const response = await api.get(`/apps/${id}`);
    return response.data;
  },

  getAppsByCategory: async (categoryId) => {
    const response = await api.get(`/apps/category/${categoryId}`);
    return response.data;
  },

  searchApps: async (keyword) => {
    const response = await api.get(`/apps/search?keyword=${encodeURIComponent(keyword)}`);
    return response.data;
  },

  getTrendingApps: async () => {
    const response = await api.get('/apps/trending');
    return response.data;
  },

  getTopRatedApps: async () => {
    const response = await api.get('/apps/top-rated');
    return response.data;
  },

  downloadApp: async (appId) => {
    const response = await api.post(`/apps/${appId}/download`);
    return response.data;
  },

  getCategories: async () => {
    const response = await api.get('/categories');
    return response.data;
  },
};

export default appService;
