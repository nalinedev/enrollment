import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LivestockType from './livestock-type';
import LivestockTypeDeleteDialog from './livestock-type-delete-dialog';
import LivestockTypeDetail from './livestock-type-detail';
import LivestockTypeUpdate from './livestock-type-update';

const LivestockTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LivestockType />} />
    <Route path="new" element={<LivestockTypeUpdate />} />
    <Route path=":id">
      <Route index element={<LivestockTypeDetail />} />
      <Route path="edit" element={<LivestockTypeUpdate />} />
      <Route path="delete" element={<LivestockTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LivestockTypeRoutes;
