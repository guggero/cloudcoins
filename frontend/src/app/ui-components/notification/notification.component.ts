import { Component, Input } from '@angular/core';

@Component({
  selector: 'notification',
  providers: [],
  templateUrl: './notification.component.html'
})
export class NotificationComponent {

  @Input() public notificationKey: string;
  @Input() public isSuccess: boolean = false;
}
