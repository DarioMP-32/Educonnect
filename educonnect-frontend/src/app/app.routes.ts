import { Routes } from '@angular/router';
import { MatriculaFormComponent } from './components/matricula-form/matricula-form.component';
import { MatriculaListComponent } from './components/matricula-list/matricula-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'matriculas', pathMatch: 'full' },
  { path: 'matriculas', component: MatriculaListComponent },
  { path: 'nueva-matricula', component: MatriculaFormComponent }
];
