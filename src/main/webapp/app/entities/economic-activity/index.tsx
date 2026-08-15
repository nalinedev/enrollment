import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EconomicActivity from './economic-activity';
import EconomicActivityDeleteDialog from './economic-activity-delete-dialog';
import EconomicActivityDetail from './economic-activity-detail';
import EconomicActivityUpdate from './economic-activity-update';

const EconomicActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EconomicActivity />} />
    <Route path="new" element={<EconomicActivityUpdate />} />
    <Route path=":id">
      <Route index element={<EconomicActivityDetail />} />
      <Route path="edit" element={<EconomicActivityUpdate />} />
      <Route path="delete" element={<EconomicActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EconomicActivityRoutes;
