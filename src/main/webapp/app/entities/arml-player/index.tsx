import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArmlPlayer from './arml-player';
import ArmlPlayerDetail from './arml-player-detail';
import ArmlPlayerUpdate from './arml-player-update';
import ArmlPlayerDeleteDialog from './arml-player-delete-dialog';

const ArmlPlayerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ArmlPlayer />} />
    <Route path="new" element={<ArmlPlayerUpdate />} />
    <Route path=":id">
      <Route index element={<ArmlPlayerDetail />} />
      <Route path="edit" element={<ArmlPlayerUpdate />} />
      <Route path="delete" element={<ArmlPlayerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArmlPlayerRoutes;
