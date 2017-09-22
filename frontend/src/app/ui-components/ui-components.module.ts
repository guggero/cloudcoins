// third party modules
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { NotificationComponent } from './notification/notification.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule,
  ],
  declarations: [
    NotificationComponent,
  ],
  exports: [
    NotificationComponent,
  ]
})
export class UiComponentsModule {
}
