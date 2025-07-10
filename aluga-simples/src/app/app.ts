import { Component, inject, OnInit, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';

import { Car } from './core/service/car';
import { CarModel } from './core/model/car';

@Component({
  selector: 'app-root',
  imports: [ButtonModule, ToolbarModule, CardModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  protected readonly title = signal('aluga-simples');
  cars: CarModel[] = []
  carService = inject(Car)

  ngOnInit(): void {
    this.carService.getCars().subscribe(cars => {
      this.cars = cars;
    })
  }


}

