import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/**
 * Root component. It only hosts the router outlet — the navbar lives in MainLayout,
 * which wraps the authenticated pages (see app.routes.ts).
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
