import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WebsocketComponent } from './websocket/websocket.component';

const routes: Routes = [
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
