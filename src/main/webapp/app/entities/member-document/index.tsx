import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MemberDocument from './member-document';
import MemberDocumentDeleteDialog from './member-document-delete-dialog';
import MemberDocumentDetail from './member-document-detail';
import MemberDocumentUpdate from './member-document-update';

const MemberDocumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MemberDocument />} />
    <Route path="new" element={<MemberDocumentUpdate />} />
    <Route path=":id">
      <Route index element={<MemberDocumentDetail />} />
      <Route path="edit" element={<MemberDocumentUpdate />} />
      <Route path="delete" element={<MemberDocumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MemberDocumentRoutes;
