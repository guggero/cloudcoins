import { Component } from '@angular/core';
import { AppService } from '../app.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'overview',
  providers: [],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent {

  constructor(private appService: AppService, private route: ActivatedRoute, private router: Router,
              private translate: TranslateService) {

  }
}
