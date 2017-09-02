import * as randomBytes from 'randombytes';
import { crypto } from 'bitcoinjs-lib';
import { Injectable } from '@angular/core';

@Injectable()
export class CryptoService {

  public createSalt(length: number): string {
    return randomBytes(length).toString('hex');
  }

  public sha256String(str: string): Buffer {
    return crypto.sha256(Buffer.from(str, 'utf8'));
  }

  public sha256Buffer(buffer: Buffer): Buffer {
    return crypto.sha256(buffer);
  }
}
