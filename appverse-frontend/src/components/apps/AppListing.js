import React, { useState, useEffect } from 'react';
import appService from '../../services/appService';
import AppCard from './AppCard';

const AppListing = () => {
  const [apps, setApps] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadApps();
    loadCategories();
  }, []);

  const loadApps = async () => {
    try {
      setLoading(true);
      const data = await appService.getAllApps();
      setApps(data);
    } catch (error) {
      console.error('Error loading apps:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await appService.getCategories();
      setCategories(data);
    } catch (error) {
      console.error('Error loading categories:', error);
    }
  };

  const handleCategoryFilter = async (categoryId) => {
    try {
      setLoading(true);
      setSelectedCategory(categoryId);
      if (categoryId) {
        const data = await appService.getAppsByCategory(categoryId);
        setApps(data);
      } else {
        loadApps();
      }
    } catch (error) {
      console.error('Error filtering:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      loadApps();
      return;
    }
    try {
      setLoading(true);
      const data = await appService.searchApps(searchKeyword);
      setApps(data);
    } catch (error) {
      console.error('Error searching:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Hero Section */}
      <div className="hero-section text-center">
        <div className="container">
          <h1>Discover Amazing Apps</h1>
          <p className="lead mb-4">Explore, review, and download the best applications</p>
          <form onSubmit={handleSearch} className="search-bar">
            <div className="input-group">
              <input
                type="text"
                className="form-control"
                placeholder="Search apps..."
                value={searchKeyword}
                onChange={(e) => setSearchKeyword(e.target.value)}
              />
              <button className="btn btn-light" type="submit">
                Search
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="container">
        {/* Category Filter */}
        <div className="mb-4">
          <div className="d-flex flex-wrap gap-2">
            <button
              className={`btn btn-sm ${!selectedCategory ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => handleCategoryFilter(null)}
            >
              All
            </button>
            {categories.map((cat) => (
              <button
                key={cat.id}
                className={`btn btn-sm ${selectedCategory === cat.id ? 'btn-primary' : 'btn-outline-primary'}`}
                onClick={() => handleCategoryFilter(cat.id)}
              >
                {cat.name}
              </button>
            ))}
          </div>
        </div>

        {/* App Grid */}
        {loading ? (
          <div className="loading-spinner">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : apps.length === 0 ? (
          <div className="text-center py-5">
            <h4 className="text-muted">No apps found</h4>
            <p>Try a different search or category</p>
          </div>
        ) : (
          <div className="row">
            {apps.map((app) => (
              <div key={app.id} className="col-md-4 col-lg-3 mb-4">
                <AppCard app={app} />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AppListing;
