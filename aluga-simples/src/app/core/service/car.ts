import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { CarModel } from '../model/car';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Car {

  private http = inject(HttpClient);

  data: CarModel[] = [
  ]


  getCars(): Observable<CarModel[]> {
    console.log('Fetching cars from the server');
    return this.http.get<CarModel[]>('http://localhost:8080/api/v1/vehicles');
  }

  getCarById(id: number): Observable<CarModel | undefined> {
    return this.http.get<CarModel>(`http://localhost:8080/api/v1/vehicles/${id}`);
  }

  rentCar(id: number) {
    return of({
      success: true
    });
  }

}
