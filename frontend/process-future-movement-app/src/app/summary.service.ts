import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Summary {
  clientInformation: string;
  productInformation: string;
  totalTransactionAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class SummaryService {
  private apiUrl = '/api/get_daily_summary_report'; // Ensure this is a relative path

  constructor(private http: HttpClient) { }

  getSummary(): Observable<Summary[]> {
    return this.http.get<Summary[]>(this.apiUrl);
  }
}
