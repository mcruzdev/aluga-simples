import { Component, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonDirective } from "primeng/button";

@Component({
  selector: 'app-root',
  imports: [ToolbarModule, RouterOutlet, RouterLink, ButtonDirective],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly title = signal('aluga-simples');

}

