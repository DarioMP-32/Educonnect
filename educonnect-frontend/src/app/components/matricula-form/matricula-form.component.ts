import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatriculaService } from '../../services/matricula.service';
import { CrearMatriculaPayload } from '../../models/matricula.model';

@Component({
  selector: 'app-matricula-form',
  imports: [FormsModule, RouterLink],
  templateUrl: './matricula-form.component.html',
  styleUrl: './matricula-form.component.css'
})
export class MatriculaFormComponent {
  private matriculaService = inject(MatriculaService);
  private router = inject(Router);

  // Modelo enlazado con [(ngModel)]
  payload: CrearMatriculaPayload = {
    estudianteId: 1,
    profesorId: 1,
    nombreCurso: '',
    creditos: 3,
    periodo: 'II-2026',
    montoArancel: 0
  };

  guardando = signal(false);
  errorMensaje = signal<string | null>(null);

  guardar(): void {
    this.guardando.set(true);
    this.errorMensaje.set(null);

    this.matriculaService.crearMatricula(this.payload).subscribe({
      next: () => this.router.navigate(['/matriculas']),
      error: (err) => {
        // El backend responde con ProblemDetail (RFC 7807): el mensaje viene en "detail"
        this.errorMensaje.set(err.error?.detail ?? 'No se pudo crear la matrícula.');
        this.guardando.set(false);
      }
    });
  }
}
