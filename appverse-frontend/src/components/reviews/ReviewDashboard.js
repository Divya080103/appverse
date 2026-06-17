import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import reviewService from '../../services/reviewService';

const ReviewDashboard = () => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadReviews();
  }, []);

  const loadReviews = async () => {
    try {
      const data = await reviewService.getMyReviews();
      setReviews(data);
    } catch (error) {
      toast.error('Error loading reviews');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this review?')) {
      try {
        await reviewService.deleteReview(id);
        toast.success('Review deleted');
        loadReviews();
      } catch (error) {
        toast.error('Error deleting review');
      }
    }
  };

  const renderStars = (rating) => {
    return Array.from({ length: 5 }, (_, i) => (
      <span key={i} className={i < rating ? 'text-warning' : 'text-muted'}>★</span>
    ));
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
      <h2 className="mb-4">My Reviews</h2>
      
      {reviews.length === 0 ? (
        <div className="text-center py-5">
          <h5 className="text-muted">You haven't written any reviews yet</h5>
          <p>Browse apps and share your feedback!</p>
        </div>
      ) : (
        <div className="row">
          {reviews.map((review) => (
            <div key={review.id} className="col-md-6 mb-3">
              <div className="review-card">
                <div className="d-flex justify-content-between align-items-start">
                  <div>
                    <h6 className="mb-1">{review.appName}</h6>
                    <div className="star-rating">{renderStars(review.rating)}</div>
                  </div>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDelete(review.id)}
                  >
                    Delete
                  </button>
                </div>
                <p className="mt-2 mb-1">{review.comment}</p>
                <small className="text-muted">
                  {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : ''}
                </small>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ReviewDashboard;
