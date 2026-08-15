import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CropVariety from './crop-variety';
import CropVarietyDeleteDialog from './crop-variety-delete-dialog';
import CropVarietyDetail from './crop-variety-detail';
import CropVarietyUpdate from './crop-variety-update';

const CropVarietyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CropVariety />} />
    <Route path="new" element={<CropVarietyUpdate />} />
    <Route path=":id">
      <Route index element={<CropVarietyDetail />} />
      <Route path="edit" element={<CropVarietyUpdate />} />
      <Route path="delete" element={<CropVarietyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CropVarietyRoutes;
