import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LivestockProduction from './livestock-production';
import LivestockProductionDeleteDialog from './livestock-production-delete-dialog';
import LivestockProductionDetail from './livestock-production-detail';
import LivestockProductionUpdate from './livestock-production-update';

const LivestockProductionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LivestockProduction />} />
    <Route path="new" element={<LivestockProductionUpdate />} />
    <Route path=":id">
      <Route index element={<LivestockProductionDetail />} />
      <Route path="edit" element={<LivestockProductionUpdate />} />
      <Route path="delete" element={<LivestockProductionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LivestockProductionRoutes;
