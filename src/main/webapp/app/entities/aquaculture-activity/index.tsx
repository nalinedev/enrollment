import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AquacultureActivity from './aquaculture-activity';
import AquacultureActivityDeleteDialog from './aquaculture-activity-delete-dialog';
import AquacultureActivityDetail from './aquaculture-activity-detail';
import AquacultureActivityUpdate from './aquaculture-activity-update';

const AquacultureActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AquacultureActivity />} />
    <Route path="new" element={<AquacultureActivityUpdate />} />
    <Route path=":id">
      <Route index element={<AquacultureActivityDetail />} />
      <Route path="edit" element={<AquacultureActivityUpdate />} />
      <Route path="delete" element={<AquacultureActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AquacultureActivityRoutes;
