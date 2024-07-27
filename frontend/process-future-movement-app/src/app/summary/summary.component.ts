import { Component, OnInit } from '@angular/core';
import { SummaryService, Summary } from '../summary.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {
  summaries: Summary[] = [];
  filteredSummaries: Summary[] = [];

  constructor(private summaryService: SummaryService) { }

  ngOnInit(): void {
    this.summaryService.getSummary().subscribe(data => {
      this.summaries = data;
      this.filteredSummaries = data;
    }, error => {
      console.error('Error fetching summary data:', error);
    });
  }

  filter(column: string, value: string): void {
    this.filteredSummaries = this.summaries.filter(summary =>
      summary[column].toString().toLowerCase().includes(value.toLowerCase())
    );
  }
}
