import { Component, OnInit } from '@angular/core';
import { SummaryService, Summary } from '../summary.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-daily-summary',
  templateUrl: './daily-summary.component.html',
  styleUrls: ['./daily-summary.component.css'],
  standalone: true,
  imports: [CommonModule] // Ensure CommonModule is imported here for ngFor and other directives
})
export class DailySummaryComponent implements OnInit {
  summaries = [
    { clientInformation: 'Client A', productInformation: 'Product X', totalTransactionAmount: 100 },
    { clientInformation: 'Client B', productInformation: 'Product Y', totalTransactionAmount: 200 },
    // Add more sample data or fetch it from the service
  ];
  filteredSummaries: Summary[] = [];
  sortDirection: { [key: string]: 'asc' | 'desc' } = {};

  constructor(private summaryService: SummaryService) { }

  ngOnInit(): void {
    this.summaryService.getSummary().subscribe(data => {
      this.summaries = data;
      this.filteredSummaries = data;
    });
  }

  sort(column: string): void {
    const direction = this.sortDirection[column] === 'asc' ? 'desc' : 'asc';
    this.sortDirection[column] = direction;
    this.filteredSummaries = this.filteredSummaries.sort((a, b) => {
      const aVal = a[column];
      const bVal = b[column];
      return (aVal < bVal ? -1 : 1) * (direction === 'asc' ? 1 : -1);
    });
  }
}
