import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { Bar, Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import developerService from '../../services/developerService';

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend);

const DeveloperAnalytics = () => {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAnalytics();
  }, []);

  const loadAnalytics = async () => {
    try {
      const data = await developerService.getAnalytics();
      setAnalytics(data);
    } catch (error) {
      toast.error('Error loading analytics');
    } finally {
      setLoading(false);
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

  if (!analytics) {
    return <div className="container py-4"><p>No analytics data available</p></div>;
  }

  const barChartData = {
    labels: analytics.downloadsByApp ? Object.keys(analytics.downloadsByApp) : [],
    datasets: [
      {
        label: 'Downloads per App',
        data: analytics.downloadsByApp ? Object.values(analytics.downloadsByApp) : [],
        backgroundColor: ['#6c63ff', '#ff6584', '#4facfe', '#43e97b', '#f093fb'],
      },
    ],
  };

  const doughnutData = {
    labels: analytics.downloadsByApp ? Object.keys(analytics.downloadsByApp) : [],
    datasets: [
      {
        data: analytics.downloadsByApp ? Object.values(analytics.downloadsByApp) : [],
        backgroundColor: ['#6c63ff', '#ff6584', '#4facfe', '#43e97b', '#f093fb', '#ffc107'],
      },
    ],
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">Developer Analytics</h2>

      {/* Stats Cards */}
      <div className="row mb-4">
        <div className="col-md-3 mb-3">
          <div className="stat-card downloads">
            <h3>{analytics.totalDownloads || 0}</h3>
            <p className="mb-0">Total Downloads</p>
          </div>
        </div>
        <div className="col-md-3 mb-3">
          <div className="stat-card apps">
            <h3>{analytics.totalApps || 0}</h3>
            <p className="mb-0">Published Apps</p>
          </div>
        </div>
        <div className="col-md-3 mb-3">
          <div className="stat-card rating">
            <h3>{analytics.averageRating?.toFixed(1) || '0.0'}</h3>
            <p className="mb-0">Avg Rating</p>
          </div>
        </div>
        <div className="col-md-3 mb-3">
          <div className="stat-card reviews">
            <h3>{analytics.totalReviews || 0}</h3>
            <p className="mb-0">Total Reviews</p>
          </div>
        </div>
      </div>

      {/* Charts */}
      <div className="row">
        <div className="col-md-7 mb-4">
          <div className="analytics-chart">
            <h5>Downloads per App</h5>
            <Bar data={barChartData} options={{ responsive: true, plugins: { legend: { display: false } } }} />
          </div>
        </div>
        <div className="col-md-5 mb-4">
          <div className="analytics-chart">
            <h5>Download Distribution</h5>
            <Doughnut data={doughnutData} options={{ responsive: true }} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeveloperAnalytics;
