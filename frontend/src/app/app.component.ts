import { AfterViewInit, Component, OnInit, ViewEncapsulation } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from './app.service';
import { Observable } from 'rxjs/Observable';
import * as $ from 'jquery';
import { RoutesService } from './services/routes.service';

@Component({
  selector: 'app',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit {

  public currentPageKey: string;

  constructor(private translate: TranslateService, private routesService: RoutesService) {
  }

  public ngOnInit(): void {
    this.setDefaultLanguage();

    this.routesService.actualCurrentRouteData().subscribe((event) => {
      this.currentPageKey = event['pageKey'];
      window.scrollTo(0, 0);
    });
  }

  public ngAfterViewInit(): void {
    const $body = $('body');
    // Smooth scrolling using jQuery easing
    $body.on('click', 'a.js-scroll-trigger[href*="#"]:not([href="#"])', (e) => {
      if (location.pathname.replace(/^\//, '') === e.target.pathname.replace(/^\//, '') &&
        location.hostname === e.target.hostname) {
        let target = $(e.target.hash);
        target = target.length ? target : $('[name=' + e.target.hash.slice(1) + ']');
        if (target.length) {
          $('html, body').animate({
            scrollTop: (target.offset().top - 54)
          }, 1000, 'easeInOutExpo');
          return false;
        }
      }
    });

    // Closes responsive menu when a scroll trigger link is clicked
    $('.js-scroll-trigger').click(() => {
      $('.navbar-collapse').collapse('hide');
    });

    // Activate scrollspy to add active class to navbar items on scroll
    $body.scrollspy({
      target: '#mainNav',
      offset: 54
    });

    // add navbar-shrink on content pages
    const mainNav = $('#mainNav');

    // Collapse the navbar when page is scrolled
    $(window).scroll(() => {
      if (mainNav.offset().top > 100) {
        mainNav.addClass('navbar-shrink');
      } else if (!mainNav.hasClass('content-page')) {
        mainNav.removeClass('navbar-shrink');
      }
    });
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
