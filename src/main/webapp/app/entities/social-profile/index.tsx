import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SocialProfile from './social-profile';
import SocialProfileDeleteDialog from './social-profile-delete-dialog';
import SocialProfileDetail from './social-profile-detail';
import SocialProfileUpdate from './social-profile-update';

const SocialProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SocialProfile />} />
    <Route path="new" element={<SocialProfileUpdate />} />
    <Route path=":id">
      <Route index element={<SocialProfileDetail />} />
      <Route path="edit" element={<SocialProfileUpdate />} />
      <Route path="delete" element={<SocialProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SocialProfileRoutes;
