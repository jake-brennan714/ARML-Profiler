import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArmlProfile from './arml-profile';
import ArmlProfileDetail from './arml-profile-detail';
import ArmlProfileUpdate from './arml-profile-update';
import ArmlProfileDeleteDialog from './arml-profile-delete-dialog';

const ArmlProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ArmlProfile />} />
    <Route path="new" element={<ArmlProfileUpdate />} />
    <Route path=":id">
      <Route index element={<ArmlProfileDetail />} />
      <Route path="edit" element={<ArmlProfileUpdate />} />
      <Route path="delete" element={<ArmlProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArmlProfileRoutes;
