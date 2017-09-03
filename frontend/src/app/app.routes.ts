import { Routes } from '@angular/router';
import { OverviewComponent } from './pages/overview';
import { LoginComponent } from './pages/login/login.component';
import { CreateAccountComponent } from './pages/create-account/create-account.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { MyKeychainsComponent } from './pages/my-keychains/my-keychains.component';

export const ROUTES: Routes = [
  {path: '', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'index.html', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'create-account', component: CreateAccountComponent, data: {pageKey: 'create-account'}},
  {path: 'login', component: LoginComponent, data: {pageKey: 'login'}},
  {path: 'logout', component: LogoutComponent, data: {pageKey: 'logout'}},
  {path: 'my-keychains', component: MyKeychainsComponent, data: {pageKey: 'my-keychains'}},
  {path: 'overview', component: OverviewComponent, data: {pageKey: 'overview'}},
];
