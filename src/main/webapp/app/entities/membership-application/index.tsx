import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MembershipApplication from './membership-application';
import MembershipApplicationDeleteDialog from './membership-application-delete-dialog';
import MembershipApplicationDetail from './membership-application-detail';
import MembershipApplicationUpdate from './membership-application-update';

const MembershipApplicationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MembershipApplication />} />
    <Route path="new" element={<MembershipApplicationUpdate />} />
    <Route path=":id">
      <Route index element={<MembershipApplicationDetail />} />
      <Route path="edit" element={<MembershipApplicationUpdate />} />
      <Route path="delete" element={<MembershipApplicationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MembershipApplicationRoutes;
