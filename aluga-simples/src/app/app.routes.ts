import { Routes } from '@angular/router';

import { Rent } from './pages/rent/rent';
import { Cars } from './pages/cars/cars';

export const routes: Routes = [
  {
    path: '',
    component: Cars
  },
  {
    path: 'rent/:id',
    component: Rent
  }
];
