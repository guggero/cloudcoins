import { Routes } from '@angular/router';
import { OverviewComponent } from './overview';

export const ROUTES: Routes = [
  {path: '', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'index.html', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'overview', component: OverviewComponent, data: {pageKey: 'overview'}},
];
