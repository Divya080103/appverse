import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer';
import PrivateRoute from './components/common/PrivateRoute';

import Login from './components/auth/Login';
import Register from './components/auth/Register';
import AppListing from './components/apps/AppListing';
import AppDetails from './components/apps/AppDetails';
import ReviewDashboard from './components/reviews/ReviewDashboard';
import DeveloperDashboard from './components/developer/DeveloperDashboard';
import AppUpload from './components/developer/AppUpload';
import AppManage from './components/developer/AppManage';
import DeveloperAnalytics from './components/developer/DeveloperAnalytics';
import AdminDashboard from './components/admin/AdminDashboard';
import UserManagement from './components/admin/UserManagement';

function App() {
  return (
    <div className="App d-flex flex-column min-vh-100">
      <Navbar />
      <main className="flex-grow-1">
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<AppListing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/apps" element={<AppListing />} />
          <Route path="/apps/:id" element={<AppDetails />} />

          {/* Protected Routes */}
          <Route path="/reviews" element={<PrivateRoute><ReviewDashboard /></PrivateRoute>} />

          {/* Developer Routes */}
          <Route path="/developer" element={<PrivateRoute roles={['DEVELOPER', 'ADMIN']}><DeveloperDashboard /></PrivateRoute>} />
          <Route path="/developer/upload" element={<PrivateRoute roles={['DEVELOPER', 'ADMIN']}><AppUpload /></PrivateRoute>} />
          <Route path="/developer/manage/:id" element={<PrivateRoute roles={['DEVELOPER', 'ADMIN']}><AppManage /></PrivateRoute>} />
          <Route path="/developer/analytics" element={<PrivateRoute roles={['DEVELOPER', 'ADMIN']}><DeveloperAnalytics /></PrivateRoute>} />

          {/* Admin Routes */}
          <Route path="/admin" element={<PrivateRoute roles={['ADMIN']}><AdminDashboard /></PrivateRoute>} />
          <Route path="/admin/users" element={<PrivateRoute roles={['ADMIN']}><UserManagement /></PrivateRoute>} />
        </Routes>
      </main>
      <Footer />
      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}

export default App;
