import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Keychain, KeyPosition } from './crypto.service';
import { SessionService } from './session.service';
import { Observable } from 'rxjs/Observable';
import { Account } from './account';
import { formatString } from '../ui-components/functions';

export const AUTH_HEADER = 'Authorization';
export const AUTH_PREFIX = 'Bearer ';
export const BALANCE_API_KEY = '22ea057c8f38';
export const BALANCE_API_URL = `https://chainz.cryptoid.info/%s/api.dws?q=multiaddr&key=${BALANCE_API_KEY}&active=%s`;

@Injectable()
export class BackendService {

  constructor(private http: HttpClient, private sessionService: SessionService) {
  }

  public loadBalance(coinName: string, address: string): Observable<any> {
    return this.http.get<Object>(formatString(BALANCE_API_URL, coinName, address));
  }

  public createMessage(captcha: any, message: any): Observable<string> {
    return this.http.post(`${API_URL}/contact?captcha=${encodeURIComponent(captcha)}`, message, {
      responseType: 'text',
      headers: this.getHeaders()
    });
  }

  public createAccount(account: any, otp: number): Observable<string> {
    return this.http.post(`${API_URL}/accounts?otp=${otp}`, account, {
      responseType: 'text',
      headers: this.getHeaders()
    });
  }

  public getSalt(username: string, otp: number): Observable<any> {
    return this.http.get<any>(`${API_URL}/accounts/salt?username=${encodeURIComponent(username)}&otp=${otp}`);
  }

  public login(account: Account, otp: number): Observable<any> {
    return this.http.post<any>(`${API_URL}/accounts/login?otp=${otp}`, account.getPersistablePart(), {
      observe: 'body',
      headers: this.getHeaders()
    });
  }

  public logout(): Observable<HttpEvent<void>> {
    return this.http.post<void>(`${API_URL}/accounts/logout`, null, {
      observe: 'events',
      headers: this.getHeaders()
    });
  }

  public getKeychains(): Observable<Keychain[]> {
    return this.http.get<Keychain[]>(`${API_URL}/keychains`, {
      observe: 'body',
      headers: this.getHeaders()
    });
  }

  public createKeychain(keychain: Keychain): Observable<Keychain> {
    return this.http.post<Keychain>(`${API_URL}/keychains`, keychain, {
      observe: 'body',
      headers: this.getHeaders()
    });
  }

  public deleteKeychain(keychain: Keychain): Observable<HttpEvent<void>> {
    return this.http.delete<void>(`${API_URL}/keychains/${keychain.id}`, {
      observe: 'events',
      headers: this.getHeaders()
    });
  }

  public increasePosition(keychain: Keychain, coinType: number): Observable<KeyPosition> {
    return this.http.post<KeyPosition>(`${API_URL}/keychains/${keychain.id}/positions/${coinType}/increase`, null, {
      observe: 'body',
      headers: this.getHeaders()
    });
  }

  public addCustomPosition(keychain: Keychain, coinType: number, index: number): Observable<KeyPosition> {
    return this.http.post<KeyPosition>(`${API_URL}/keychains/${keychain.id}/positions/${coinType}/custom?index=${index}`, null, {
      observe: 'body',
      headers: this.getHeaders()
    });
  }

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    if (this.sessionService.isLoggedIn()) {
      headers = headers.append(AUTH_HEADER, AUTH_PREFIX + this.sessionService.getToken());
    }
    return headers;
  }
}
