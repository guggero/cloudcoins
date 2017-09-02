import * as randomBytes from 'randombytes';
import { crypto } from 'bitcoinjs-lib';
import { Injectable } from '@angular/core';
import { pbkdf2Sync } from 'pbkdf2';

export const PBKDF2_HMAC_LEN = 64;
export const PBKDF2_ROUNDS = 10240;
export const PBKDF2_DIGEST = 'sha512';

export function createSalt(length: number): Buffer {
  return randomBytes(length);
}

export function sha256String(str: string): Buffer {
  return crypto.sha256(Buffer.from(str, 'utf8'));
}

export function sha256Buffer(buffer: Buffer): Buffer {
  return crypto.sha256(buffer);
}

export class Account {
  constructor(public email: string,
              public salt: string,
              public backendIdentification: string,
              public otpAuthKey?: string) {
  }

  public getPersistablePart(): any {
    return {
      email: this.email,
      salt: this.salt,
      password: this.backendIdentification,
      otpAuthKey: this.otpAuthKey,
    };
  }
}

@Injectable()
export class CryptoService {

  public createAccount(email: string, password: string, otpAuthKey: string): Account {
    const salt = createSalt(16);
    const saltStr = salt.toString('hex');
    const pwHash1 = sha256String(saltStr + password);
    const pwHash2 = sha256String(saltStr + pwHash1.toString('hex'));
    return new Account(email, saltStr, pwHash2.toString('hex'), otpAuthKey);
  }

  public getLoginData(email: string, password: string, salt: string): Account {
    const pwHash1 = sha256String(salt + password);
    const pwHash2 = sha256String(salt + pwHash1.toString('hex'));
    return new Account(email, salt, pwHash2.toString('hex'));
  }
}
