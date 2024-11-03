import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WebsocketComponent } from './websocket/websocket.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {
    'path': ':userId',
    'component': HomeComponent
  },
  {
    'path': ':origin/to/:destiny',
    'component': WebsocketComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
