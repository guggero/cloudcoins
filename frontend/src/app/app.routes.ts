import { Routes } from '@angular/router';
import { OverviewComponent } from './overview';
import { LoginComponent } from './login/login.component';

export const ROUTES: Routes = [
  {path: '', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'index.html', redirectTo: 'overview', pathMatch: 'full'},
  {path: 'overview', component: OverviewComponent },
  {path: 'login', component: LoginComponent },
];
