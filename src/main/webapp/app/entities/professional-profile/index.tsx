import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProfessionalProfile from './professional-profile';
import ProfessionalProfileDeleteDialog from './professional-profile-delete-dialog';
import ProfessionalProfileDetail from './professional-profile-detail';
import ProfessionalProfileUpdate from './professional-profile-update';

const ProfessionalProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProfessionalProfile />} />
    <Route path="new" element={<ProfessionalProfileUpdate />} />
    <Route path=":id">
      <Route index element={<ProfessionalProfileDetail />} />
      <Route path="edit" element={<ProfessionalProfileUpdate />} />
      <Route path="delete" element={<ProfessionalProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProfessionalProfileRoutes;
