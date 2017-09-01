import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { Http, HttpModule } from '@angular/http';
import { ApplicationRef, NgModule } from '@angular/core';
import { createInputTransfer, createNewHosts, removeNgStyles } from '@angularclass/hmr';
import { PreloadAllModules, RouterModule } from '@angular/router';
// third party angular modules
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgPipesModule } from 'ngx-pipes';
// general app configuration
import { ENV_PROVIDERS } from './environment';
import { ROUTES } from './app.routes';
// app components
import { AppComponent } from './app.component';
import { APP_RESOLVER_PROVIDERS } from './app.resolver';
import { AppService } from './app.service';
import { OverviewComponent } from './overview';
import { UiComponentsModule } from './ui-components';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/mergeMap';
import 'tether';
import 'bootstrap';
import '../styles/styles.scss';

// Application wide providers
const APP_PROVIDERS = [
  ...APP_RESOLVER_PROVIDERS,
  AppService,
];

type StoreType = {
  restoreInputValues: () => void,
  disposeOldHosts: () => void
};

export function createTranslateLoader(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json?_=' + (new Date()).getTime());
}

@NgModule({
  bootstrap: [AppComponent],
  declarations: [
    AppComponent,
    OverviewComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(ROUTES, {useHash: false, preloadingStrategy: PreloadAllModules}),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [Http]
      }
    }),
    NgPipesModule,
    UiComponentsModule
  ],
  providers: [
    ENV_PROVIDERS,
    APP_PROVIDERS
  ]
})
export class AppModule {

  constructor(public appRef: ApplicationRef) {
  }

  public hmrOnInit(store: StoreType) {
    if (!store) {
      return;
    }
    // set input values
    if ('restoreInputValues' in store) {
      let restoreInputValues = store.restoreInputValues;
      setTimeout(restoreInputValues);
    }

    this.appRef.tick();
    delete store.restoreInputValues;
  }

  public hmrOnDestroy(store: StoreType) {
    const cmpLocation = this.appRef.components.map((cmp) => cmp.location.nativeElement);
    // recreate root elements
    store.disposeOldHosts = createNewHosts(cmpLocation);
    // save input values
    store.restoreInputValues = createInputTransfer();
    // remove styles
    removeNgStyles();
  }

  public hmrAfterDestroy(store: StoreType) {
    // display new elements
    store.disposeOldHosts();
    delete store.disposeOldHosts;
  }

}