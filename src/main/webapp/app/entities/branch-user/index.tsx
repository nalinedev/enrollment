import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BranchUser from './branch-user';
import BranchUserDeleteDialog from './branch-user-delete-dialog';
import BranchUserDetail from './branch-user-detail';
import BranchUserUpdate from './branch-user-update';

const BranchUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BranchUser />} />
    <Route path="new" element={<BranchUserUpdate />} />
    <Route path=":id">
      <Route index element={<BranchUserDetail />} />
      <Route path="edit" element={<BranchUserUpdate />} />
      <Route path="delete" element={<BranchUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BranchUserRoutes;
