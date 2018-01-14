import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { PreloadAllModules, RouterModule } from '@angular/router';
// third party angular modules
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgPipesModule } from 'ngx-pipes';
import { ReCaptchaModule } from 'angular2-recaptcha';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MarkdownModule } from 'angular2-markdown';
// general app configuration
import { environment } from 'environments/environment';
import { ROUTES } from './app.routes';
// app components
import { AppComponent } from './app.component';
import { APP_RESOLVER_PROVIDERS } from './app.resolver';
import { RoutesService } from './services/routes.service';
import { CreateAccountComponent } from './pages/create-account';
import { LoginComponent } from './pages/login/login.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { OverviewComponent } from './pages/overview';
import { UiComponentsModule } from './ui-components';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/mergeMap';
import 'tether';
import 'jquery-easing';
import '../styles/styles.scss';
import { BackendService } from './services/backend.service';
import { CryptoService } from './services/crypto.service';
import { SessionService } from './services/session.service';
import { MyKeychainsComponent } from './pages/my-keychains/my-keychains.component';
import { AddKeychainComponent } from './pages/add-keychain/add-keychain.component';
import { ChangelogComponent } from './pages/changelog';

// Application wide providers
const APP_PROVIDERS = [
  ...APP_RESOLVER_PROVIDERS,
  BackendService,
  CryptoService,
  RoutesService,
  SessionService,
];

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json?_=' + (new Date()).getTime());
}

@NgModule({
  bootstrap: [AppComponent],
  declarations: [
    AppComponent,
    CreateAccountComponent,
    LoginComponent,
    LogoutComponent,
    AddKeychainComponent,
    MyKeychainsComponent,
    OverviewComponent,
    ChangelogComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(ROUTES, {useHash: false, preloadingStrategy: PreloadAllModules}),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    NgPipesModule,
    ReCaptchaModule,
    UiComponentsModule,
    NgbModule.forRoot(),
    MarkdownModule
  ],
  providers: [
    environment.ENV_PROVIDERS,
    APP_PROVIDERS
  ]
})
export class AppModule {
}
