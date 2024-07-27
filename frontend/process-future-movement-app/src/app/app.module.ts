import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SummaryService } from './summary.service';
import {DailySummaryComponent} from "./daily-summary/daily-summary.component";

@NgModule({
  declarations: [
    DailySummaryComponent // Ensure this is declared here if not standalone
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [SummaryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
