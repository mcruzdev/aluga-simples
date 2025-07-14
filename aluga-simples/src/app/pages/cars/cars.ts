import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

import { CarModel } from '../../core/model/car';
import { Car } from '../../core/service/car';

@Component({
  selector: 'app-cars',
  imports: [CardModule, DialogModule, ButtonModule],
  templateUrl: './cars.html',
  styleUrl: './cars.css'
})
export class Cars implements OnInit {
  cars: CarModel[] = []
  carService = inject(Car)
  router = inject(Router);
  displayDialog = signal(false);

  selectedCar = signal<CarModel>({
    carTitle: '',
    brand: '',
    id: 0,
    model: '',
    status: 'AVAILABLE',
    year: 0,
    engine: ''
  });

  ngOnInit(): void {
    this.carService.getCars().subscribe(cars => {
      this.cars = cars;
    })
  }

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
