import React from 'react';
import { Link } from 'react-router-dom';

const AppCard = ({ app }) => {
  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <span key={i} className={i <= Math.round(rating) ? 'text-warning' : 'text-muted'}>
          ★
        </span>
      );
    }
    return stars;
  };

  return (
    <div className="card app-card h-100">
      <div className="card-img-top d-flex align-items-center justify-content-center bg-light" style={{ height: '150px' }}>
        {app.iconUrl ? (
          <img src={app.iconUrl} alt={app.name} style={{ maxHeight: '100px', maxWidth: '100px' }} />
        ) : (
          <span style={{ fontSize: '3rem' }}>📱</span>
        )}
      </div>
      <div className="card-body d-flex flex-column">
        <h6 className="card-title mb-1">{app.name}</h6>
        <p className="text-muted small mb-1">{app.developerName}</p>
        <span className="category-badge mb-2" style={{ fontSize: '0.75rem', width: 'fit-content' }}>
          {app.categoryName}
        </span>
        <p className="card-text small text-muted flex-grow-1">
          {app.description?.substring(0, 80)}...
        </p>
        <div className="d-flex justify-content-between align-items-center mt-auto">
          <div className="star-rating small">
            {renderStars(app.averageRating)}
            <span className="text-muted ms-1">({app.averageRating?.toFixed(1) || '0.0'})</span>
          </div>
          <span className="text-muted small">{app.downloadCount || 0} downloads</span>
        </div>
        <div className="d-flex justify-content-between align-items-center mt-2">
          <Link to={`/apps/${app.id}`} className="btn btn-sm btn-outline-primary">
            View
          </Link>
        </div>
      </div>
    </div>
  );
};

export default AppCard;
