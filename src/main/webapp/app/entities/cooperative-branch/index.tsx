import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CooperativeBranch from './cooperative-branch';
import CooperativeBranchDeleteDialog from './cooperative-branch-delete-dialog';
import CooperativeBranchDetail from './cooperative-branch-detail';
import CooperativeBranchUpdate from './cooperative-branch-update';

const CooperativeBranchRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CooperativeBranch />} />
    <Route path="new" element={<CooperativeBranchUpdate />} />
    <Route path=":id">
      <Route index element={<CooperativeBranchDetail />} />
      <Route path="edit" element={<CooperativeBranchUpdate />} />
      <Route path="delete" element={<CooperativeBranchDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CooperativeBranchRoutes;
