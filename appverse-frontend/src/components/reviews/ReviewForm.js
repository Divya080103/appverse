import React, { useState } from 'react';

const ReviewForm = ({ onSubmit }) => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [comment, setComment] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (rating === 0) {
      alert('Please select a rating');
      return;
    }
    onSubmit({ rating, comment });
    setRating(0);
    setComment('');
  };

  return (
    <div className="card border-0 shadow-sm p-3 mb-3">
      <h6>Write a Review</h6>
      <form onSubmit={handleSubmit}>
        <div className="mb-2">
          <div className="d-flex gap-1" style={{ cursor: 'pointer' }}>
            {[1, 2, 3, 4, 5].map((star) => (
              <span
                key={star}
                style={{ fontSize: '1.5rem' }}
                className={star <= (hoverRating || rating) ? 'text-warning' : 'text-muted'}
                onClick={() => setRating(star)}
                onMouseEnter={() => setHoverRating(star)}
                onMouseLeave={() => setHoverRating(0)}
              >
                ★
              </span>
            ))}
          </div>
        </div>
        <div className="mb-2">
          <textarea
            className="form-control"
            rows="3"
            placeholder="Write your review..."
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            maxLength={1000}
          />
        </div>
        <button type="submit" className="btn btn-primary btn-sm">
          Submit Review
        </button>
      </form>
    </div>
  );
};

export default ReviewForm;
