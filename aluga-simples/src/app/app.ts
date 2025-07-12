import { Component, inject, OnInit, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog'

import { Car } from './core/service/car';
import { CarModel } from './core/model/car';

@Component({
  selector: 'app-root',
  imports: [ButtonModule, ToolbarModule, CardModule, DialogModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  protected readonly title = signal('aluga-simples');
  cars: CarModel[] = []
  carService = inject(Car)
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

}

