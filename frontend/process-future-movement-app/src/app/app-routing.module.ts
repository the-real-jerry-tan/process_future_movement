import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DailySummaryComponent } from './daily-summary/daily-summary.component';

const routes: Routes = [
  { path: '', component: DailySummaryComponent }, // Default route
  { path: 'report/daily_summary', component: DailySummaryComponent },
  { path: '**', redirectTo: '' } // Redirect any unknown paths to the default
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
