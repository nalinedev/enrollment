import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AquacultureProduction from './aquaculture-production';
import AquacultureProductionDeleteDialog from './aquaculture-production-delete-dialog';
import AquacultureProductionDetail from './aquaculture-production-detail';
import AquacultureProductionUpdate from './aquaculture-production-update';

const AquacultureProductionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AquacultureProduction />} />
    <Route path="new" element={<AquacultureProductionUpdate />} />
    <Route path=":id">
      <Route index element={<AquacultureProductionDetail />} />
      <Route path="edit" element={<AquacultureProductionUpdate />} />
      <Route path="delete" element={<AquacultureProductionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AquacultureProductionRoutes;
