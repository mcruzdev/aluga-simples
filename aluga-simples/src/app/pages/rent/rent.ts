import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CardModule } from "primeng/card";
import { ButtonModule } from 'primeng/button';
import { DatePickerModule } from 'primeng/datepicker'
import { FormsModule } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DividerModule } from 'primeng/divider';

import { Car } from '../../core/service/car';
import { CarModel } from '../../core/model/car';

@Component({
  selector: 'app-rent',
  imports: [CardModule, ButtonModule, DatePickerModule, FormsModule, FloatLabelModule, DividerModule, RouterLink],
  templateUrl: './rent.html',
  styleUrl: './rent.css'
})
export class Rent implements OnInit {

  private oneDayInMilliseconds = 24 * 60 * 60 * 1000;

  carClient = inject(Car);
  activatedRoute = inject(ActivatedRoute);
  car = signal<CarModel>({
    carTitle: '',
    brand: '',
    id: 0,
    model: '',
    status: 'AVAILABLE',
    engine: '',
    year: 0
  });

  start = signal<Date | null>(null);
  end = signal<Date | null>(null);
  minStart = signal<Date>(new Date());
  minEnd = computed(() => {
    const start = this.start();
    return start ? new Date(start.getTime() + this.oneDayInMilliseconds) : null;
  });

  ngOnInit() {
    this.carClient.getCarById(1).subscribe(carDetails => {
      if (carDetails) {
        this.car.set(carDetails);
      }
    });
  }

  reserveCar() {
    console.log('Car reserved:', this.car());
    console.log('Start date:', this.start());
    console.log('End date:', this.end());
    this.carClient.rentCar(this.car().id).subscribe(response => {
      console.log('Car reserved successfully', response);
    });
  }
}
