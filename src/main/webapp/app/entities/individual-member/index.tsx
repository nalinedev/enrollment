import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IndividualMember from './individual-member';
import IndividualMemberDeleteDialog from './individual-member-delete-dialog';
import IndividualMemberDetail from './individual-member-detail';
import IndividualMemberUpdate from './individual-member-update';

const IndividualMemberRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IndividualMember />} />
    <Route path="new" element={<IndividualMemberUpdate />} />
    <Route path=":id">
      <Route index element={<IndividualMemberDetail />} />
      <Route path="edit" element={<IndividualMemberUpdate />} />
      <Route path="delete" element={<IndividualMemberDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IndividualMemberRoutes;
