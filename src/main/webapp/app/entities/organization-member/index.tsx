import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrganizationMember from './organization-member';
import OrganizationMemberDeleteDialog from './organization-member-delete-dialog';
import OrganizationMemberDetail from './organization-member-detail';
import OrganizationMemberUpdate from './organization-member-update';

const OrganizationMemberRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrganizationMember />} />
    <Route path="new" element={<OrganizationMemberUpdate />} />
    <Route path=":id">
      <Route index element={<OrganizationMemberDetail />} />
      <Route path="edit" element={<OrganizationMemberUpdate />} />
      <Route path="delete" element={<OrganizationMemberDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrganizationMemberRoutes;
