import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import developerService from '../../services/developerService';
import appService from '../../services/appService';

const AppUpload = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    currentVersion: '1.0.0',
    categoryId: '',
    iconUrl: '',
    screenshotUrl: '',
  });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const data = await appService.getCategories();
      setCategories(data);
    } catch (error) {
      toast.error('Error loading categories');
    }
  };

  const handleChange = (e) => {
    const value = e.target.type === 'number' ? parseFloat(e.target.value) : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      await developerService.publishApp({
        ...formData,
        categoryId: parseInt(formData.categoryId),
      });
      toast.success('App published successfully!');
      navigate('/developer');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error publishing app');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-4">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card border-0 shadow-sm">
            <div className="card-body p-4">
              <h3 className="mb-4">Publish New App</h3>
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">App Name *</label>
                  <input
                    type="text"
                    className="form-control"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                    minLength={2}
                    maxLength={100}
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Description *</label>
                  <textarea
                    className="form-control"
                    name="description"
                    rows="4"
                    value={formData.description}
                    onChange={handleChange}
                    required
                    maxLength={2000}
                  />
                </div>

                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Category *</label>
                    <select
                      className="form-select"
                      name="categoryId"
                      value={formData.categoryId}
                      onChange={handleChange}
                      required
                    >
                      <option value="">Select Category</option>
                      {categories.map((cat) => (
                        <option key={cat.id} value={cat.id}>{cat.name}</option>
                      ))}
                    </select>
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Version *</label>
                    <input
                      type="text"
                      className="form-control"
                      name="currentVersion"
                      value={formData.currentVersion}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>

                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Icon URL</label>
                    <input
                      type="url"
                      className="form-control"
                      name="iconUrl"
                      value={formData.iconUrl}
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="mb-3">
                  <label className="form-label">Screenshot URL</label>
                  <input
                    type="url"
                    className="form-control"
                    name="screenshotUrl"
                    value={formData.screenshotUrl}
                    onChange={handleChange}
                  />
                </div>

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Publishing...' : 'Publish App'}
                  </button>
                  <button type="button" className="btn btn-outline-secondary" onClick={() => navigate('/developer')}>
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AppUpload;
