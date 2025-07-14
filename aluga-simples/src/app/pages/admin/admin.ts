import { Component, inject } from '@angular/core';
import { TableModule } from 'primeng/table';
import { Divider } from "primeng/divider";
import { toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from "primeng/button";
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';

import { Car } from '../../core/service/car';
import { filter, map } from 'rxjs';

@Component({
  selector: 'app-admin',
  imports: [TableModule, Divider, ButtonModule, TooltipModule, TagModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin {

  carService = inject(Car);

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

  sendToMaintenance(carId: number) {
    console.log('sending car to maitenance', carId)

  }

  receiveCar(carId: number) {
    console.log('receiving car', carId)
  }
}
