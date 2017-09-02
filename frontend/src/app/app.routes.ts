import { Routes } from '@angular/router';
import { OverviewComponent } from './pages/overview';
import { LoginComponent } from './pages/login/login.component';
import { CreateAccountComponent } from './pages/create-account/create-account.component';

export const ROUTES: Routes = [
  {path: '', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'index.html', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'create-account', component: CreateAccountComponent, data: {pageKey: 'create-account'}},
  {path: 'login', component: LoginComponent, data: {pageKey: 'login'}},
  {path: 'overview', component: OverviewComponent, data: {pageKey: 'overview'}},
];
