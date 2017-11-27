import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Account, Keychain, KeyPosition } from './crypto.service';
import { SessionService } from './session.service';

export const AUTH_HEADER = 'Authorization';
export const AUTH_PREFIX = 'Bearer ';

@Injectable()
export class BackendService {

  constructor(private http: HttpClient, private sessionService: SessionService) {
  }

  public createMessage(captcha: any, message: any): Observable<void> {
    return this.http
      .post(`${API_URL}/contact?captcha=${encodeURIComponent(captcha)}`, message, this.getOptions())
      .catch(this.handleError);
  }

  public createAccount(account: any, otp: number): Observable<void> {
    return this.http
      .post(`${API_URL}/accounts?otp=${otp}`, account, this.getOptions())
      .catch(this.handleError);
  }

  public getSalt(username: string, otp: number): Observable<any> {
    return this.http
      .get<any>(`${API_URL}/accounts/salt?username=${encodeURIComponent(username)}&otp=${otp}`)
      .catch(this.handleError);
  }

  public login(account: Account, otp: number): Observable<any> {
    return this.http
      .post<any>(`${API_URL}/accounts/login?otp=${otp}`, account.getPersistablePart(), this.getOptions())
      .catch(this.handleError);
  }

  public logout(): Observable<void> {
    return this.http
      .post(`${API_URL}/accounts/logout`, null, this.getOptions())
      .catch(this.handleError);
  }

  public getKeychains(): Observable<Keychain[]> {
    return this.http
      .get<Keychain[]>(`${API_URL}/keychains`, this.getOptions())
      .catch(this.handleError);
  }

  public createKeychain(keychain: Keychain): Observable<Keychain> {
    return this.http
      .post<Keychain>(`${API_URL}/keychains`, keychain, this.getOptions())
      .catch(this.handleError);
  }

  public increasePosition(keychain: Keychain, coinType: number): Observable<KeyPosition> {
    return this.http
      .post<KeyPosition>(`${API_URL}/keychains/${keychain.id}/positions/${coinType}/increase`, null, this.getOptions())
      .catch(this.handleError);
  }

  public addCustomPosition(keychain: Keychain, coinType: number, index: number): Observable<KeyPosition> {
    return this.http
      .post<KeyPosition>(`${API_URL}/keychains/${keychain.id}/positions/${coinType}/custom?index=${index}`, null, this.getOptions())
      .catch(this.handleError);
  }

  private handleError(error: any) {
    return Observable.throw(error.json());
  }

  private getOptions(): any {
    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    if (this.sessionService.isLoggedIn()) {
      headers.append(AUTH_HEADER, AUTH_PREFIX + this.sessionService.getToken());
    }
    return {headers};
  }
}
