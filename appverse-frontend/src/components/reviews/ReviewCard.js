import React from 'react';

const ReviewCard = ({ review }) => {
  const renderStars = (rating) => {
    return Array.from({ length: 5 }, (_, i) => (
      <span key={i} className={i < rating ? 'text-warning' : 'text-muted'}>★</span>
    ));
  };

  return (
    <div className="review-card">
      <div className="d-flex justify-content-between align-items-start">
        <div>
          <strong>{review.username}</strong>
          <div className="star-rating small">{renderStars(review.rating)}</div>
        </div>
        <small className="text-muted">
          {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : ''}
        </small>
      </div>
      {review.comment && <p className="mt-2 mb-0">{review.comment}</p>}
    </div>
  );
};

export default ReviewCard;
