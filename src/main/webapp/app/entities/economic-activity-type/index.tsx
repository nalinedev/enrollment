import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EconomicActivityType from './economic-activity-type';
import EconomicActivityTypeDeleteDialog from './economic-activity-type-delete-dialog';
import EconomicActivityTypeDetail from './economic-activity-type-detail';
import EconomicActivityTypeUpdate from './economic-activity-type-update';

const EconomicActivityTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EconomicActivityType />} />
    <Route path="new" element={<EconomicActivityTypeUpdate />} />
    <Route path=":id">
      <Route index element={<EconomicActivityTypeDetail />} />
      <Route path="edit" element={<EconomicActivityTypeUpdate />} />
      <Route path="delete" element={<EconomicActivityTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EconomicActivityTypeRoutes;
