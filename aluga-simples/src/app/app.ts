import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from "primeng/button";
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-root',
  imports: [ToolbarModule, RouterOutlet, RouterLink, ButtonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly title = signal('aluga-simples');

  keycloakService = inject(Keycloak)
  router = inject(Router)

  doLogin() {
    if (!this.keycloakService.authenticated) {
      this.keycloakService.login().then(() => {
        // Handle successful login
        console.log('Login successful');
      }).catch(error => {
        // Handle login error
        console.error('Login failed', error);
      });
    } else {
      this.router.navigate(['/admin']);
    }
  }

  doLogout() {
    this.keycloakService.logout().then(() => {
      // Handle successful logout
      console.log('Logout successful');
      this.router.navigate(['/']);
    }).catch(error => {
      // Handle logout error
      console.error('Logout failed', error);
    });
  }
}

