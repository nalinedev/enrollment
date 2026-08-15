import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IdentityDocument from './identity-document';
import IdentityDocumentDeleteDialog from './identity-document-delete-dialog';
import IdentityDocumentDetail from './identity-document-detail';
import IdentityDocumentUpdate from './identity-document-update';

const IdentityDocumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IdentityDocument />} />
    <Route path="new" element={<IdentityDocumentUpdate />} />
    <Route path=":id">
      <Route index element={<IdentityDocumentDetail />} />
      <Route path="edit" element={<IdentityDocumentUpdate />} />
      <Route path="delete" element={<IdentityDocumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IdentityDocumentRoutes;
