import { Component, OnInit } from '@angular/core';
import { BackendService } from '../../services/backend.service';
import { SessionService } from '../../services/session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'logout',
  providers: [],
  template: ''
})
export class LogoutComponent implements OnInit {

  constructor(private backendService: BackendService, private sessionService: SessionService, private router: Router) {
  }

  public ngOnInit(): void {
    this.backendService.logout();
    this.sessionService.logout();
    this.router.navigate(['/login']);
  }
}
