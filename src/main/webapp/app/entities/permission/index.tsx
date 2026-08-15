import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Permission from './permission';
import PermissionDeleteDialog from './permission-delete-dialog';
import PermissionDetail from './permission-detail';
import PermissionUpdate from './permission-update';

const PermissionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Permission />} />
    <Route path="new" element={<PermissionUpdate />} />
    <Route path=":id">
      <Route index element={<PermissionDetail />} />
      <Route path="edit" element={<PermissionUpdate />} />
      <Route path="delete" element={<PermissionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PermissionRoutes;
