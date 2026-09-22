import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'coursecart-ui';
  apiStatus = 'Pending';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get(`${environment.apiBaseUrl}/catalog/categories`).subscribe({
      next: () => this.apiStatus = 'Success',
      error: (err) => {
        console.error('API Error:', err);
        this.apiStatus = 'Error or Gateway Reached';
      }
    });
  }
}
