import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AquaticSpecies from './aquatic-species';
import AquaticSpeciesDeleteDialog from './aquatic-species-delete-dialog';
import AquaticSpeciesDetail from './aquatic-species-detail';
import AquaticSpeciesUpdate from './aquatic-species-update';

const AquaticSpeciesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AquaticSpecies />} />
    <Route path="new" element={<AquaticSpeciesUpdate />} />
    <Route path=":id">
      <Route index element={<AquaticSpeciesDetail />} />
      <Route path="edit" element={<AquaticSpeciesUpdate />} />
      <Route path="delete" element={<AquaticSpeciesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AquaticSpeciesRoutes;
