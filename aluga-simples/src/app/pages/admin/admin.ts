import { Component, inject, signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { Divider } from "primeng/divider";
import { toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from "primeng/button";
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { map } from 'rxjs';
import { Dialog } from "primeng/dialog";
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';

import { Car } from '../../core/service/car';

@Component({
  selector: 'app-admin',
  imports: [TableModule, Divider, ButtonModule, TooltipModule, TagModule, Dialog, TextareaModule, FloatLabelModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin {

  carService = inject(Car);

  showMaintenance = signal<boolean>(false);
  maintenanceReason = signal<string>("");
  currentCar = signal<number>(0);

  cars = toSignal(this.carService.getCars().pipe(
    map(cars => {
      return cars.map(car => {

        const status = this.carStatus(car.status);

        return {
          ...car,
          statusLabel: status.label,
          statusColor: status.color

        }
      })
    })
  ), { initialValue: [] });

  private carStatus(status: string): { label: string, color: string } {
    switch (status) {
      case 'AVAILABLE':
        return {
          label: 'Disponível',
          color: 'success'
        };
      case 'IN_MAINTENANCE':
        return {
          label: 'Em manutenção',
          color: 'warn'
        }
      default:
        return {
          label: 'Alugado',
          color: 'danger'
        }
    }
  }

  removeCar(carId: number) {
    console.log('removing car', carId)
  }

  showMaintenanceDialog(carId: number) {
    this.showMaintenance.set(true)
    this.currentCar.set(carId)
  }

  moveForMaintenance() {
    console.log(`sending car with ID ${this.currentCar()} to maintenance with reason: "${this.maintenanceReason()}"`)
  }

  receiveCar(carId: number) {
    console.log('receiving car', carId)
  }
}
