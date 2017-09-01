import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from './app.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(private translate: TranslateService, private appService: AppService, private router: Router) {
  }

  public ngOnInit() {
    this.setDefaultLanguage();
  }

  public getSelectedLanguage(): string {
    return this.translate.currentLang;
  }

  public setLanguage(language): Observable<any> {
    this.translate.setDefaultLang(language);
    return this.translate.use(language);
  }

  private setDefaultLanguage(): void {
    let currentBrowserLanguage = this.translate.getBrowserCultureLang().slice(0, 2);
    if (currentBrowserLanguage === 'de') {
      this.setLanguage('de');
    } else {
      this.setLanguage('en');
    }
  }
}
