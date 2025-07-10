import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { CarModel, CarStatus } from '../model/car';

@Injectable({
  providedIn: 'root'
})
export class Car {

  data: CarModel[] = [
    {
      carTitle: 'Fiat Mobi 1.0',
      brand: 'Fiat',
      id: 1,
      model: 'Mobi',
      status: CarStatus.DISPONIVEL,
      year: 2022,
      engine: '1.0'
    },
    {
      carTitle: 'Hyundai HB20S 1.0',
      brand: 'Hyundai',
      id: 2,
      model: 'HB20S',
      status: CarStatus.DISPONIVEL,
      year: 2022,
      engine: '1.0'
    },
    {
      carTitle: 'VW Polo 1.0',
      brand: 'Volkswagen',
      id: 3,
      model: 'Polo',
      status: CarStatus.DISPONIVEL,
      year: 2021,
      engine: '1.0'
    },
    {
      carTitle: 'Renault Kwid 1.0',
      brand: 'Renault',
      id: 4,
      model: 'Kwid',
      status: CarStatus.DISPONIVEL,
      year: 2023,
      engine: '1.0'
    },
    {
      carTitle: 'Toyota Corolla Cross 2.0',
      brand: 'Toyota',
      id: 5,
      model: 'Corolla Cross',
      status: CarStatus.DISPONIVEL,
      year: 2025,
      engine: '2.0'
    }
  ]

  getCars(): Observable<CarModel[]> {
    return of(this.data)
  }

}
