import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CooperativeRole from './cooperative-role';
import CooperativeRoleDeleteDialog from './cooperative-role-delete-dialog';
import CooperativeRoleDetail from './cooperative-role-detail';
import CooperativeRoleUpdate from './cooperative-role-update';

const CooperativeRoleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CooperativeRole />} />
    <Route path="new" element={<CooperativeRoleUpdate />} />
    <Route path=":id">
      <Route index element={<CooperativeRoleDetail />} />
      <Route path="edit" element={<CooperativeRoleUpdate />} />
      <Route path="delete" element={<CooperativeRoleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CooperativeRoleRoutes;
