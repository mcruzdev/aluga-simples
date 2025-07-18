import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { map } from 'rxjs';

import { CarModel } from '../../core/model/car';
import { Car } from '../../core/service/car';
import { toSignal } from '@angular/core/rxjs-interop';
import { Divider } from "primeng/divider";

@Component({
  selector: 'app-cars',
  imports: [CardModule, DialogModule, ButtonModule, Divider],
  templateUrl: './cars.html',
  styleUrl: './cars.css'
})
export class Cars {

  carService = inject(Car)
  router = inject(Router);

  carsSignal = toSignal(this.carService.getCars().pipe(map(cars => cars.filter(car => car.status === 'AVAILABLE'))), {
    initialValue: []
  })

  totalCars = computed(() => {
    return this.carsSignal().length;
  })

  displayDialog = signal(false);

  selectedCar = signal<CarModel>({
    carTitle: '',
    brand: '',
    id: 0,
    model: '',
    status: 'AVAILABLE',
    year: 0,
    engine: '',
    accessories: []
  });

  seeDetails(car: CarModel): void {
    this.carService.getCarById(car.id).subscribe(carDetails => {
      if (carDetails) {
        this.displayDialog.set(true);
        this.selectedCar.set(carDetails);
      }
    });
  }

  rentCar(car: CarModel): void {
    console.log('Renting car:', car);
    this.router.navigate(['/rent', car.id]);
  }
}
