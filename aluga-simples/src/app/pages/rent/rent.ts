import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CardModule } from "primeng/card";
import { ButtonModule } from 'primeng/button';
import { DatePickerModule } from 'primeng/datepicker'
import { FormsModule } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BlockUIModule } from 'primeng/blockui';

import { Car } from '../../core/service/car';
import { CarModel } from '../../core/model/car';

@Component({
  selector: 'app-rent',
  imports: [CardModule, ButtonModule, DatePickerModule, FormsModule, FloatLabelModule, DividerModule, RouterLink, ToastModule, BlockUIModule],
  templateUrl: './rent.html',
  styleUrl: './rent.css',
  providers: [MessageService],
})
export class Rent implements OnInit {

  private readonly oneDayInMilliseconds = 24 * 60 * 60 * 1000;
  private readonly fiveSecondsInMilliseconds = 5000;

  carClient = inject(Car);
  activatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  messageService = inject(MessageService);
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
  redirecting = signal<boolean>(false);

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      const carId = +params['id'];
      this.carClient.getCarById(carId).subscribe(car => {
        if (car) {
          this.car.set(car);
        } else {
          this.router.navigate(['/']);
        }
      });
    });
  }

  reserveCar() {
    this.carClient.rentCar(this.car().id).subscribe(() => {

      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        life: this.fiveSecondsInMilliseconds,
        detail: `Thank you for your reservation! Check your email for details.

        You will be redirected to the home page in 5 seconds.`,
        closable: true,
      });

      this.redirecting.set(true);

      setTimeout(() => {
        this.router.navigate(['/']);
      }, this.fiveSecondsInMilliseconds);
    });
  }
}
