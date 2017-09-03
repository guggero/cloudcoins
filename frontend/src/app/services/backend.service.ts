import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Account, Keychain, KeyPosition } from './crypto.service';
import { SessionService } from './session.service';

export const AUTH_HEADER = 'Authorization';
export const AUTH_PREFIX = 'Bearer ';

@Injectable()
export class BackendService {

  constructor(private http: Http, private sessionService: SessionService) {
  }

  public createAccount(account: any, otp: number): Observable<any> {
    return this.http
      .post(`${API_URL}/accounts?otp=${otp}`, account, this.buildRequestOptions())
      .map((res: Response) => res.status)
      .catch(this.handleError);
  }

  public getSalt(email: string, otp: number): Observable<string> {
    return this.http
      .get(`${API_URL}/accounts/salt?email=${encodeURIComponent(email)}&otp=${otp}`)
      .map((response) => response.json().salt)
      .catch(this.handleError);
  }

  public login(account: Account, otp: number): Observable<string> {
    return this.http
      .post(`${API_URL}/accounts/login?otp=${otp}`, account.getPersistablePart(), this.buildRequestOptions())
      .map((response) => response.json().token)
      .catch(this.handleError);
  }

  public logout(): Observable<any> {
    return this.http
      .post(`${API_URL}/accounts/logout`, null, this.buildRequestOptions())
      .map((response) => response.json())
      .catch(this.handleError);
  }

  public getKeychains(): Observable<Keychain[]> {
    return this.http
      .get(`${API_URL}/keychains`, this.buildRequestOptions())
      .map((response) => response.json())
      .catch(this.handleError);
  }

  public createKeychain(keychain: Keychain): Observable<Keychain> {
    return this.http
      .post(`${API_URL}/keychains`, keychain, this.buildRequestOptions())
      .map((response) => response.json())
      .catch(this.handleError);
  }

  public increasePosition(keychain: Keychain, coinType: number): Observable<KeyPosition> {
    return this.http
      .post(`${API_URL}/keychains/${keychain.id}/positions/${coinType}`, null, this.buildRequestOptions())
      .map((response) => response.json())
      .catch(this.handleError);
  }

  private handleError(error: any) {
    return Observable.throw(error.json().errors);
  }

  private buildRequestOptions(): RequestOptions {
    let headers = new Headers({'Content-Type': 'application/json'});
    if (this.sessionService.isLoggedIn()) {
      headers.append(AUTH_HEADER, AUTH_PREFIX + this.sessionService.getToken());
    }
    return new RequestOptions({headers});
  }
}
