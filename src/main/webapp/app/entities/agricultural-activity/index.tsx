import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgriculturalActivity from './agricultural-activity';
import AgriculturalActivityDeleteDialog from './agricultural-activity-delete-dialog';
import AgriculturalActivityDetail from './agricultural-activity-detail';
import AgriculturalActivityUpdate from './agricultural-activity-update';

const AgriculturalActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgriculturalActivity />} />
    <Route path="new" element={<AgriculturalActivityUpdate />} />
    <Route path=":id">
      <Route index element={<AgriculturalActivityDetail />} />
      <Route path="edit" element={<AgriculturalActivityUpdate />} />
      <Route path="delete" element={<AgriculturalActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgriculturalActivityRoutes;
