import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import NumberSequence from './number-sequence';
import NumberSequenceDeleteDialog from './number-sequence-delete-dialog';
import NumberSequenceDetail from './number-sequence-detail';
import NumberSequenceUpdate from './number-sequence-update';

const NumberSequenceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NumberSequence />} />
    <Route path="new" element={<NumberSequenceUpdate />} />
    <Route path=":id">
      <Route index element={<NumberSequenceDetail />} />
      <Route path="edit" element={<NumberSequenceUpdate />} />
      <Route path="delete" element={<NumberSequenceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NumberSequenceRoutes;
