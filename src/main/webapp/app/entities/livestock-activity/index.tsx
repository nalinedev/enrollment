import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LivestockActivity from './livestock-activity';
import LivestockActivityDeleteDialog from './livestock-activity-delete-dialog';
import LivestockActivityDetail from './livestock-activity-detail';
import LivestockActivityUpdate from './livestock-activity-update';

const LivestockActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LivestockActivity />} />
    <Route path="new" element={<LivestockActivityUpdate />} />
    <Route path=":id">
      <Route index element={<LivestockActivityDetail />} />
      <Route path="edit" element={<LivestockActivityUpdate />} />
      <Route path="delete" element={<LivestockActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LivestockActivityRoutes;
