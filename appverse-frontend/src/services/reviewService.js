import api from './api';

const reviewService = {
  getReviewsByApp: async (appId) => {
    const response = await api.get(`/reviews/app/${appId}`);
    return response.data;
  },

  getMyReviews: async () => {
    const response = await api.get('/reviews/user');
    return response.data;
  },

  createReview: async (reviewData) => {
    const response = await api.post('/reviews', reviewData);
    return response.data;
  },

  updateReview: async (id, reviewData) => {
    const response = await api.put(`/reviews/${id}`, reviewData);
    return response.data;
  },

  deleteReview: async (id) => {
    const response = await api.delete(`/reviews/${id}`);
    return response.data;
  },
};

export default reviewService;
