import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import developerService from '../../services/developerService';
import appService from '../../services/appService';

const AppManage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [app, setApp] = useState(null);
  const [versions, setVersions] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showVersionForm, setShowVersionForm] = useState(false);
  const [versionData, setVersionData] = useState({
    versionNumber: '',
    releaseNotes: '',
  });
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    categoryId: '',

    iconUrl: '',
  });

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const loadData = async () => {
    try {
      const [appData, versionsData, categoriesData] = await Promise.all([
        appService.getAppById(id),
        developerService.getVersions(id),
        appService.getCategories(),
      ]);
      setApp(appData);
      setVersions(versionsData);
      setCategories(categoriesData);
      setFormData({
        name: appData.name,
        description: appData.description,
        categoryId: appData.categoryId,
        iconUrl: appData.iconUrl || '',
      });
    } catch (error) {
      toast.error('Error loading app data');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateApp = async (e) => {
    e.preventDefault();
    try {
      await developerService.updateApp(id, {
        ...formData,
        categoryId: parseInt(formData.categoryId),
      });
      toast.success('App updated successfully');
    } catch (error) {
      toast.error('Error updating app');
    }
  };

  const handleAddVersion = async (e) => {
    e.preventDefault();
    try {
      await developerService.addVersion(id, versionData);
      toast.success('New version added');
      setShowVersionForm(false);
      setVersionData({ versionNumber: '', releaseNotes: '' });
      loadData();
    } catch (error) {
      toast.error('Error adding version');
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
        <h2>Manage: {app?.name}</h2>
        <button className="btn btn-outline-secondary" onClick={() => navigate('/developer')}>
          Back to Console
        </button>
      </div>

      <div className="row">
        {/* Update App Form */}
        <div className="col-md-7">
          <div className="card border-0 shadow-sm">
            <div className="card-body p-4">
              <h5>Update App Details</h5>
              <form onSubmit={handleUpdateApp}>
                <div className="mb-3">
                  <label className="form-label">App Name</label>
                  <input
                    type="text"
                    className="form-control"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Description</label>
                  <textarea
                    className="form-control"
                    rows="3"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  />
                </div>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Category</label>
                    <select
                      className="form-select"
                      value={formData.categoryId}
                      onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
                    >
                      {categories.map((cat) => (
                        <option key={cat.id} value={cat.id}>{cat.name}</option>
                      ))}
                    </select>
                  </div>
                </div>
                <button type="submit" className="btn btn-primary">Save Changes</button>
              </form>
            </div>
          </div>
        </div>

        {/* Version History */}
        <div className="col-md-5">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">Version History</h5>
                <button
                  className="btn btn-sm btn-primary"
                  onClick={() => setShowVersionForm(!showVersionForm)}
                >
                  + New Version
                </button>
              </div>

              {showVersionForm && (
                <form onSubmit={handleAddVersion} className="mb-3 p-3 bg-light rounded">
                  <div className="mb-2">
                    <input
                      type="text"
                      className="form-control form-control-sm"
                      placeholder="Version number (e.g., 2.0.0)"
                      value={versionData.versionNumber}
                      onChange={(e) => setVersionData({ ...versionData, versionNumber: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-2">
                    <textarea
                      className="form-control form-control-sm"
                      rows="2"
                      placeholder="Release notes"
                      value={versionData.releaseNotes}
                      onChange={(e) => setVersionData({ ...versionData, releaseNotes: e.target.value })}
                    />
                  </div>
                  <button type="submit" className="btn btn-sm btn-success">Add Version</button>
                </form>
              )}

              {versions.length === 0 ? (
                <p className="text-muted">No version history</p>
              ) : (
                <div className="list-group list-group-flush">
                  {versions.map((v) => (
                    <div key={v.id} className="list-group-item px-0">
                      <div className="d-flex justify-content-between">
                        <strong>v{v.versionNumber}</strong>
                        <small className="text-muted">
                          {v.releasedAt ? new Date(v.releasedAt).toLocaleDateString() : ''}
                        </small>
                      </div>
                      {v.releaseNotes && <p className="small text-muted mb-0">{v.releaseNotes}</p>}
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AppManage;
