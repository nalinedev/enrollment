import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgriculturalProduction from './agricultural-production';
import AgriculturalProductionDeleteDialog from './agricultural-production-delete-dialog';
import AgriculturalProductionDetail from './agricultural-production-detail';
import AgriculturalProductionUpdate from './agricultural-production-update';

const AgriculturalProductionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgriculturalProduction />} />
    <Route path="new" element={<AgriculturalProductionUpdate />} />
    <Route path=":id">
      <Route index element={<AgriculturalProductionDetail />} />
      <Route path="edit" element={<AgriculturalProductionUpdate />} />
      <Route path="delete" element={<AgriculturalProductionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgriculturalProductionRoutes;
