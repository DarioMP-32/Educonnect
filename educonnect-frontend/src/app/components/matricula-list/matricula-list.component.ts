import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatriculaService } from '../../services/matricula.service';
import { MatriculaCurso } from '../../models/matricula.model';

@Component({
  selector: 'app-matricula-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './matricula-list.component.html',
  styleUrl: './matricula-list.component.css'
})
export class MatriculaListComponent implements OnInit {
  private matriculaService = inject(MatriculaService);

  // Writable signals
  matriculas = signal<MatriculaCurso[]>([]);
  filtroEstado = signal<string>('TODAS');

  // Computed signal para filtrado reactivo
  matriculasFiltradas = computed(() => {
    const estado = this.filtroEstado();
    if (estado === 'TODAS') {
      return this.matriculas();
    }
    return this.matriculas().filter(m => m.estado === estado);
  });

  ngOnInit(): void {
    this.cargarMatriculas();
  }

  cargarMatriculas(): void {
    this.matriculaService.getMatriculas().subscribe({
      next: (data) => this.matriculas.set(data),
      error: (err) => console.error('Error al cargar:', err)
    });
  }

  cambiarFiltro(nuevoEstado: string): void {
    this.filtroEstado.set(nuevoEstado);
  }

  avanzarEstado(item: MatriculaCurso): void {
    if (!item.id) return;
    const siguienteEstado = item.estado === 'ACTIVA' ? 'COMPLETADA' : 'CANCELADA';
    this.matriculaService.actualizarEstado(item.id, siguienteEstado).subscribe({
      next: () => this.cargarMatriculas(),
      error: (err) => alert('Error al actualizar: ' + err.message)
    });
  }
}
