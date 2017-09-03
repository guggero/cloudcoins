import { Injectable } from '@angular/core';

@Injectable()
export class SessionService {

  private tokenKey: string = 'loginToken';
  private encKey: string = 'encKey';

  public storeSession(token: string, key: string): void {
    sessionStorage.setItem(this.tokenKey, token);
    sessionStorage.setItem(this.encKey, key);
  }

  public getToken(): string {
    return sessionStorage.getItem(this.tokenKey);
  }

  public getKey(): string {
    return sessionStorage.getItem(this.encKey);
  }

  public isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  public logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.encKey);
  }
}
