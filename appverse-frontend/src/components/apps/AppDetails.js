import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import appService from '../../services/appService';
import reviewService from '../../services/reviewService';
import authService from '../../services/authService';
import ReviewForm from '../reviews/ReviewForm';
import ReviewCard from '../reviews/ReviewCard';

const AppDetails = () => {
  const { id } = useParams();
  const [app, setApp] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const isAuthenticated = authService.isAuthenticated();

  useEffect(() => {
    loadApp();
    loadReviews();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const loadApp = async () => {
    try {
      const data = await appService.getAppById(id);
      setApp(data);
    } catch (error) {
      toast.error('Error loading app details');
    } finally {
      setLoading(false);
    }
  };

  const loadReviews = async () => {
    try {
      const data = await reviewService.getReviewsByApp(id);
      setReviews(data);
    } catch (error) {
      console.error('Error loading reviews:', error);
    }
  };

  const handleDownload = async () => {
    if (!isAuthenticated) {
      toast.info('Please login to download');
      return;
    }
    try {
      await appService.downloadApp(id);
      toast.success('Download started!');
      loadApp(); // Refresh download count
    } catch (error) {
      toast.error('Error downloading app');
    }
  };

  const handleReviewSubmit = async (reviewData) => {
    try {
      await reviewService.createReview({ ...reviewData, appId: parseInt(id) });
      toast.success('Review submitted!');
      loadReviews();
      loadApp();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error submitting review');
    }
  };

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <span key={i} className={i <= Math.round(rating) ? 'text-warning' : 'text-muted'} style={{ fontSize: '1.5rem' }}>
          ★
        </span>
      );
    }
    return stars;
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

  if (!app) {
    return <div className="container py-5 text-center"><h3>App not found</h3></div>;
  }

  return (
    <div className="container py-4">
      <div className="row">
        {/* App Info */}
        <div className="col-md-8">
          <div className="card border-0 shadow-sm">
            <div className="card-body p-4">
              <div className="d-flex align-items-start">
                <div className="me-4" style={{ fontSize: '4rem' }}>
                  {app.iconUrl ? (
                    <img src={app.iconUrl} alt={app.name} style={{ width: '80px', height: '80px', borderRadius: '16px' }} />
                  ) : '📱'}
                </div>
                <div className="flex-grow-1">
                  <h2 className="mb-1">{app.name}</h2>
                  <p className="text-muted mb-1">by {app.developerName}</p>
                  <span className="category-badge">{app.categoryName}</span>
                  <div className="mt-2">
                    {renderStars(app.averageRating)}
                    <span className="ms-2 text-muted">
                      ({app.averageRating?.toFixed(1) || '0.0'}) • {app.downloadCount || 0} downloads
                    </span>
                  </div>
                </div>
              </div>
              <hr />
              <h5>About</h5>
              <p>{app.description}</p>
              <div className="row mt-3">
                <div className="col-sm-6">
                  <strong>Version:</strong> {app.currentVersion}
                </div>
                <div className="col-sm-6">
                  <strong>Published:</strong> {app.createdAt ? new Date(app.createdAt).toLocaleDateString() : 'N/A'}
                </div>
              </div>
            </div>
          </div>

          {/* Reviews Section */}
          <div className="mt-4">
            <h4>Reviews ({reviews.length})</h4>
            {isAuthenticated && (
              <ReviewForm onSubmit={handleReviewSubmit} />
            )}
            <div className="mt-3">
              {reviews.length === 0 ? (
                <p className="text-muted">No reviews yet. Be the first to review!</p>
              ) : (
                reviews.map((review) => (
                  <ReviewCard key={review.id} review={review} />
                ))
              )}
            </div>
          </div>
        </div>

        {/* Sidebar */}
        <div className="col-md-4">
          <div className="card border-0 shadow-sm">
            <div className="card-body text-center">
              <button className="btn btn-primary btn-lg w-100" onClick={handleDownload}>
                Download App
              </button>
              <p className="text-muted small mt-2">v{app.currentVersion}</p>
            </div>
          </div>

          <div className="card border-0 shadow-sm mt-3">
            <div className="card-body">
              <h6>App Info</h6>
              <ul className="list-unstyled">
                <li className="d-flex justify-content-between py-2 border-bottom">
                  <span className="text-muted">Category</span>
                  <span>{app.categoryName}</span>
                </li>
                <li className="d-flex justify-content-between py-2 border-bottom">
                  <span className="text-muted">Downloads</span>
                  <span>{app.downloadCount || 0}</span>
                </li>
                <li className="d-flex justify-content-between py-2 border-bottom">
                  <span className="text-muted">Rating</span>
                  <span>{app.averageRating?.toFixed(1) || '0.0'} / 5</span>
                </li>
                <li className="d-flex justify-content-between py-2">
                  <span className="text-muted">Developer</span>
                  <span>{app.developerName}</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AppDetails;
