import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { Login } from './features/auth/login/login';
import { MainLayout } from './layout/main-layout/main-layout';
import { Dashboard } from './features/dashboard/dashboard';
import { ClientList } from './features/clients/client-list/client-list';
import { MachineList } from './features/machines/machine-list/machine-list';
import { FicheList } from './features/fiches/fiche-list/fiche-list';
import { FicheDetail } from './features/fiches/fiche-detail/fiche-detail';
import { ClientDetail } from './features/clients/client-detail/client-detail';
import { UserAuditLog } from './features/admin/user-audit-log/user-audit-log';

import { UserAdmin } from './features/admin/user-admin/user-admin';
import { adminGuard } from './core/guards/role.guard';

import { MachineDetail } from './features/machines/machine-detail/machine-detail';




export const routes: Routes = [
  { path: 'login', component: Login },

  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      
      { path: 'dashboard', component: Dashboard },

      { path: 'machines', component: MachineList },
      { path: 'machines/:id', component: MachineDetail },


      { path: 'fiches', component: FicheList },
      { path: 'fiches/:id', component: FicheDetail },

      { path: 'clients', component: ClientList },
      { path: 'clients/:id', component: ClientDetail },

      { path: 'utilisateurs', component: UserAdmin, canActivate: [adminGuard] },
      { path: 'utilisateurs/:id/audit', component: UserAuditLog, canActivate: [adminGuard] },



      
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },

  { path: '**', redirectTo: '' },
];