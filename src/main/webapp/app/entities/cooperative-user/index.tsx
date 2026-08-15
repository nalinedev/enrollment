import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CooperativeUser from './cooperative-user';
import CooperativeUserDeleteDialog from './cooperative-user-delete-dialog';
import CooperativeUserDetail from './cooperative-user-detail';
import CooperativeUserUpdate from './cooperative-user-update';

const CooperativeUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CooperativeUser />} />
    <Route path="new" element={<CooperativeUserUpdate />} />
    <Route path=":id">
      <Route index element={<CooperativeUserDetail />} />
      <Route path="edit" element={<CooperativeUserUpdate />} />
      <Route path="delete" element={<CooperativeUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CooperativeUserRoutes;
