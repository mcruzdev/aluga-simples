import { Routes } from '@angular/router';

import { Rent } from './pages/rent/rent';
import { Cars } from './pages/cars/cars';
import { Admin } from './pages/admin/admin';
import { canActivateAuthRole } from './auth-guard';
import { Forbidden } from './pages/forbidden/forbidden';

export const routes: Routes = [
  {
    path: '',
    component: Cars
  },
  {
    path: 'rent/:id',
    component: Rent
  },
  {
    path: 'admin',
    component: Admin,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' }
  },
  {
    path: 'forbidden',
    component: Forbidden
  }
];
