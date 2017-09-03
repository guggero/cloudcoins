import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Account } from './crypto.service';

@Injectable()
export class BackendService {

  constructor(private http: Http) {
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

  public login(account: Account, otp: number): Observable<any> {
    return this.http
      .post(`${API_URL}/accounts/login?otp=${otp}`, account.getPersistablePart(), this.buildRequestOptions())
      .map((response) => response.json())
      .catch(this.handleError);
  }

  private handleError(error: any) {
    return Observable.throw(error.json().error);
  }

  private buildRequestOptions(): RequestOptions {
    let headers = new Headers({'Content-Type': 'application/json'});
    return new RequestOptions({headers});
  }
}
