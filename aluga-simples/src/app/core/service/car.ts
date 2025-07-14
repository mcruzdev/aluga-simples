import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { CarModel } from '../model/car';

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
      status: 'AVAILABLE',
      year: 2022,
      engine: '1.0'
    },
    {
      carTitle: 'Hyundai HB20S 1.0',
      brand: 'Hyundai',
      id: 2,
      model: 'HB20S',
      status: 'AVAILABLE',
      year: 2022,
      engine: '1.0'
    },
    {
      carTitle: 'VW Polo 1.0',
      brand: 'Volkswagen',
      id: 3,
      model: 'Polo',
      status: 'AVAILABLE',
      year: 2021,
      engine: '1.0'
    },
    {
      carTitle: 'Renault Kwid 1.0',
      brand: 'Renault',
      id: 4,
      model: 'Kwid',
      status: 'AVAILABLE',
      year: 2023,
      engine: '1.0'
    },
    {
      carTitle: 'Toyota Corolla Cross 2.0',
      brand: 'Toyota',
      id: 5,
      model: 'Corolla Cross',
      status: 'AVAILABLE',
      year: 2025,
      engine: '2.0'
    },
    {
      carTitle: 'Chevrolet Onix Plus 1.0 Turbo',
      brand: 'Chevrolet',
      id: 6,
      model: 'Onix Plus',
      status: 'RENTED',
      year: 2024,
      engine: '1.0 Turbo'
    },
    {
      carTitle: 'Honda HR-V 1.5',
      brand: 'Honda',
      id: 7,
      model: 'HR-V',
      status: 'AVAILABLE',
      year: 2023,
      engine: '1.5'
    },
    {
      carTitle: 'Jeep Compass 2.0 Diesel',
      brand: 'Jeep',
      id: 8,
      model: 'Compass',
      status: 'IN_MAINTENANCE',
      year: 2022,
      engine: '2.0 Diesel'
    },
    {
      carTitle: 'Nissan Kicks 1.6',
      brand: 'Nissan',
      id: 9,
      model: 'Kicks',
      status: 'RENTED',
      year: 2021,
      engine: '1.6'
    },
    {
      carTitle: 'Ford Ranger 3.2 Diesel',
      brand: 'Ford',
      id: 10,
      model: 'Ranger',
      status: 'IN_MAINTENANCE',
      year: 2024,
      engine: '3.2 Diesel'
    },
    {
      carTitle: 'BMW X1 2.0',
      brand: 'BMW',
      id: 11,
      model: 'X1',
      status: 'RENTED',
      year: 2023,
      engine: '2.0'
    },
    {
      carTitle: 'Mercedes-Benz Classe C 1.5 Turbo',
      brand: 'Mercedes-Benz',
      id: 12,
      model: 'Classe C',
      status: 'IN_MAINTENANCE',
      year: 2022,
      engine: '1.5 Turbo'
    }
  ]

  getCars(): Observable<CarModel[]> {
    return of(this.data)
  }

  getCarById(id: number): Observable<CarModel | undefined> {
    const car = this.data.find(car => car.id === id);
    return of(car);
  }

  rentCar(id: number) {
    return of({
      success: true
    });
  }

}
