import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  imports: [CardModule, ButtonModule, DatePickerModule, FormsModule, FloatLabelModule, DividerModule],
  templateUrl: './rent.html',
  styleUrl: './rent.css'
})
export class Rent implements OnInit {

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

  start: Date | null = null;
  end: Date | null = null;


  ngOnInit() {
    this.carClient.getCarById(1).subscribe(carDetails => {
      if (carDetails) {
        this.car.set(carDetails);
      }
    });
  }

  // Additional logic for the Rent component can be added here
}
