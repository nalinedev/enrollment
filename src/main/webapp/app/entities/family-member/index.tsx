import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FamilyMember from './family-member';
import FamilyMemberDeleteDialog from './family-member-delete-dialog';
import FamilyMemberDetail from './family-member-detail';
import FamilyMemberUpdate from './family-member-update';

const FamilyMemberRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FamilyMember />} />
    <Route path="new" element={<FamilyMemberUpdate />} />
    <Route path=":id">
      <Route index element={<FamilyMemberDetail />} />
      <Route path="edit" element={<FamilyMemberUpdate />} />
      <Route path="delete" element={<FamilyMemberDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FamilyMemberRoutes;
